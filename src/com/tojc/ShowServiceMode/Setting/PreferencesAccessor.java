/*
Copyright (c) 2012, Jaken
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list
 of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this
 list of conditions and the following disclaimer in the documentation and/or
 other materials provided with the distribution.
Neither the name of the Jaken's laboratory nor the names of its contributors may
 be used to endorse or promote products derived from this software without
 specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.tojc.ShowServiceMode.Setting;

import java.util.UUID;

import com.tojc.ShowServiceMode.Utility.Mode;
import com.tojc.ShowServiceMode.Utility.Version;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferencesAccessor
{
	private Context contextApplication;
	private SharedPreferences sp;
	
	private final String PREFERENCE_FIRSTSTART = "FirstStart";
	private final String PREFERENCE_LAST_LANGUAGE = "LastLanguage";
	private final String PREFERENCE_BOOT_COUNT = "BootCount";
	private final String PREFERENCE_APPLICATION_MODE = "ApplicationMode";
	private final String PREFERENCE_UUID = "UUID";
	private final String PREFERENCE_PREFERENCES_VERSION = "PreferencesVersion";
	private final String PREFERENCE_USE_DEFAULT_SETTINGS = "UseDefaultSettings";
	private final String PREFERENCE_CALLING_METHOD_TYPE_NAME = "CallingMethodTypeName";

	// under Ver3.1.0
	private final String PREFERENCE_PROCESSING_TYPE_ID = "ProcessingTypeId";


	public PreferencesAccessor(Context contextApplication)
	{
		this.contextApplication = contextApplication;
		this.sp = PreferenceManager.getDefaultSharedPreferences(this.contextApplication);
	}

	public void clear()
	{
		this.sp.edit().clear().commit();
	}

	public void executeFirstStart(MachineInformation machineinfo)
	{
        Log.d(this.getClass().getSimpleName(),"executeFirstStart");

        Version versionThis = machineinfo.getVersionShowServiceMode();
		String typeDefaultProcessingTypeName = machineinfo.getModelType().getTypeDefaultProcessingTypeName();

		// TODO: 場当たり的な対応
		// ServiceModeAppの存在有無でデフォルトを切り替える。
		if(machineinfo.getModelType().getKeyId() == 0)
		{
			// UNKNOWN
			PackageManager manager = this.contextApplication.getPackageManager();
			try
			{
				ActivityInfo info = manager.getActivityInfo(new ComponentName("com.sec.android.app.servicemodeapp", "com.sec.android.app.servicemodeapp.ServiceModeApp"), 0);
			}
			catch(NameNotFoundException e)
			{
				typeDefaultProcessingTypeName = "DEF4001_DIRECTLY_CALL_ACTIVITY_RADIOINFO";
			}
		}

		// set default
        Editor editer = sp.edit();
		editer.putBoolean(PREFERENCE_FIRSTSTART, true);
		editer.putString(PREFERENCE_LAST_LANGUAGE, machineinfo.getLocaleLanguage().toString());
		editer.putLong(PREFERENCE_BOOT_COUNT, 0);
		editer.putString(PREFERENCE_APPLICATION_MODE, machineinfo.getMode().toString());
		editer.putString(PREFERENCE_UUID, UUID.randomUUID().toString());
		editer.putString(PREFERENCE_PREFERENCES_VERSION, versionThis.toString());
		editer.putBoolean(PREFERENCE_USE_DEFAULT_SETTINGS, true);
		editer.putString(PREFERENCE_CALLING_METHOD_TYPE_NAME, typeDefaultProcessingTypeName);
		editer.commit();
	}

	public void executeUpdateStart(MachineInformation machineinfo)
	{
        Log.d(this.getClass().getSimpleName(),"executeUpdateStart");

		Version versionPreferences = getPreferencesVersion();
		Version versionThis = machineinfo.getVersionShowServiceMode();
		String typeDefaultProcessingTypeName = machineinfo.getModelType().getTypeDefaultProcessingTypeName();

		if(versionPreferences.toString().equals("0.0.0"))
		{
			// under Ver3.1.0

			// add PREFERENCE_BOOT_COUNT
			setBootCount(0);

        	// add PREFERENCE_UUID
			setUUID(UUID.randomUUID());

        	// delete PREFERENCE_PROCESSING_TYPE_ID
        	String processingTypeId = this.sp.getString(PREFERENCE_PROCESSING_TYPE_ID, "NOTHING");
			sp.edit().remove(PREFERENCE_PROCESSING_TYPE_ID).commit();

        	// add PREFERENCE_CALLING_METHOD_TYPE_NAME
			String callingMethodTypeName = convertProcessingTypeIdToCallingMethodTypeName(processingTypeId, typeDefaultProcessingTypeName);
			setCallingMethodTypeName(callingMethodTypeName);
		}

		// add PREFERENCE_APPLICATION_MODE
		setApplicationMode(machineinfo.getMode());

		// MEMO: OnLanguageIsChangedのケースでLastLanguageの更新が必要
		// add PREFERENCE_LAST_LANGUAGE
		setLastLanguage(machineinfo.getLocaleLanguage().toString());

        // update PREFERENCE_PREFERENCES_VERSION
        setPreferencesVersion(versionThis);
	}

	public void executeNormalStart(MachineInformation machineinfo)
	{
        Log.d(this.getClass().getSimpleName(),"executeNormalStart");
	}















	private String convertProcessingTypeIdToCallingMethodTypeName(String processingTypeId, String defaultName)
	{
		String result = "";

		if(processingTypeId.equals("NOTHING"))
		{
			// NOTHING
			result = defaultName;
		}
		else if(processingTypeId.equals("DIAL2263_01"))
		{
			// DIAL2263_01
			result = "DEF1001_DIAL_NUMBER_2263";
		}
		else if(processingTypeId.equals("DIAL197328640_01") || processingTypeId.equals("DIAL197328640_02"))
		{
			// DIAL197328640_01
			// DIAL197328640_02
			result = "DEF1002_DIAL_NUMBER_197328640";
		}
		else if(processingTypeId.equals("BROADCAST_01"))
		{
			// BROADCAST_01
			result = "DEF2001_BROADCAST_SECRET_CODE_2263";
		}
		else if(processingTypeId.equals("DIRECTLYCALL2263_01"))
		{
			// DIRECTLYCALL2263_01
			result = "DEF3001_DIRECTLY_CALL_ACTIVITY_SERVICEMODEAPP_2263";
		}
		else if(processingTypeId.equals("DIRECTLYCALL197328640_01"))
		{
			// DIRECTLYCALL197328640_01
			result = "DEF3002_DIRECTLY_CALL_ACTIVITY_SERVICEMODEAPP_197328640";
		}
		else if(processingTypeId.equals("RADIOINFO"))
		{
			// RADIOINFO
			result = "DEF4001_DIRECTLY_CALL_ACTIVITY_RADIOINFO";
		}
		else if(processingTypeId.equals("DIALFREENUMBER"))
		{
			// DIALFREENUMBER
			result = "DEF9999_NOTHING";
		}
		else
		{
			result = defaultName;
		}
		return result;
	}










	// FirstStart
	public void setFirstStart(boolean value)
	{
		this.sp.edit().putBoolean(PREFERENCE_FIRSTSTART, value).commit();
	}
	public boolean getFirstStart()
	{
		return this.sp.getBoolean(PREFERENCE_FIRSTSTART, true);
	}

	// LastLanguage
	public void setLastLanguage(String value)
	{
		this.sp.edit().putString(PREFERENCE_LAST_LANGUAGE, value).commit();
	}
	public String getLastLanguage()
	{
		return this.sp.getString(PREFERENCE_LAST_LANGUAGE, "");
	}

	// BootCount
	public void setBootCount(long value)
	{
		this.sp.edit().putLong(PREFERENCE_BOOT_COUNT, value).commit();
	}
	public long getBootCount()
	{
		return this.sp.getLong(PREFERENCE_BOOT_COUNT, 0);
	}

	// ApplicationMode
	public void setApplicationMode(Mode value)
	{
		this.sp.edit().putString(PREFERENCE_APPLICATION_MODE, value.toString()).commit();
	}
	public Mode getApplicationMode()
	{
		return Mode.toMode(this.sp.getString(PREFERENCE_APPLICATION_MODE, ""));
	}

	// UUID
	public void setUUID(UUID value)
	{
		this.sp.edit().putString(PREFERENCE_UUID, value.toString()).commit();
	}
	public UUID getUUID()
	{
		return UUID.fromString(this.sp.getString(PREFERENCE_UUID, UUID.randomUUID().toString()));
	}

	// PreferencesVersion
	public void setPreferencesVersion(Version value)
	{
		this.sp.edit().putString(PREFERENCE_PREFERENCES_VERSION, value.toString()).commit();
	}
	public Version getPreferencesVersion()
	{
		return new Version(this.sp.getString(PREFERENCE_PREFERENCES_VERSION, "0.0.0"));
	}

	// UseDefaultSettings
	public void setUseDefaultSettings(boolean value)
	{
		this.sp.edit().putBoolean(PREFERENCE_USE_DEFAULT_SETTINGS, value).commit();
	}
	public boolean getUseDefaultSettings()
	{
		return this.sp.getBoolean(PREFERENCE_USE_DEFAULT_SETTINGS, true);
	}

	// CallingMethodTypeName
	public void setCallingMethodTypeName(String value)
	{
		this.sp.edit().putString(PREFERENCE_CALLING_METHOD_TYPE_NAME, value).commit();
	}
	public String getCallingMethodTypeName()
	{
		return this.sp.getString(PREFERENCE_CALLING_METHOD_TYPE_NAME, "");
	}


}
