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

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.UUID;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.tojc.ShowServiceMode.Event.EventMulticaster;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnDowngradeStartListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnFirstStartListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnLanguageIsChangedListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnNormalStartListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnUpdateStartListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.StartStatusEventObject;
import com.tojc.ShowServiceMode.Utility.Mode;
import com.tojc.ShowServiceMode.Utility.Version;
import com.tojc.ShowServiceMode.XmlObject.Model.ModelType;
import com.tojc.ShowServiceMode.XmlObject.Model.ModelTypeXmlManager;

public class MachineInformation
{
	private Context contextApplication;
	private ModelTypeXmlManager modelTypeXmlManager;

	private PreferencesAccessor preferencesAccessor;
	private SignatureChecker signaturechecker;

	private final Version versionShowServiceMode;
	private final Version versionOS;
	private final String display;
	private final String buildInfo;

	private final String machineModel;
	private final ModelType modelType;

	private EventMulticaster eventMulticaster;

	private final Locale localeLanguage;

	public MachineInformation(Context contextApplication, ModelTypeXmlManager modelTypeXmlManager)
	{
        Log.d(this.getClass().getSimpleName(),"MachineInformation Start");

        this.contextApplication = contextApplication;
		this.modelTypeXmlManager = modelTypeXmlManager;

		//
        this.signaturechecker = new SignatureChecker(this.contextApplication);
		//
		this.preferencesAccessor = new PreferencesAccessor(this.contextApplication);
        //
        this.versionShowServiceMode = new Version(this.getPackageVersion());
        Log.i(this.getClass().getSimpleName(),"ShowServiceMode: " + this.versionShowServiceMode.toDebugString());
        //
        this.versionOS = new Version(android.os.Build.VERSION.RELEASE);
        Log.i(this.getClass().getSimpleName(),"VERSION.RELEASE: " + this.versionOS.toDebugString());
        //
        this.machineModel = android.os.Build.MODEL;
        Log.i(this.getClass().getSimpleName(),"MODEL          : " + this.machineModel);
        //
        this.display = android.os.Build.DISPLAY;
        Log.i(this.getClass().getSimpleName(),"DISPLAY        : " + this.display);

        //
        StringBuffer sb = new StringBuffer();
        Field[] fields = android.os.Build.class.getDeclaredFields();

        String sep = "";
        for(Field field : fields)
        {
        	field.setAccessible(true);
        	try
        	{
        		sb.append(sep);
        		sb.append("[" + field.getName() + ":" + field.get(null) + "]");

        		//Log.d(this.getClass().getSimpleName(),"[" + field.getName() + ":" + field.get(null) + "]");
        	}
        	catch(Exception e)
        	{
                e.printStackTrace();
        	}
        	sep = ",";
        }
        this.buildInfo = sb.toString();
        Log.i(this.getClass().getSimpleName(),"buildInfo      : " + this.buildInfo);

        //
		this.modelType = this.modelTypeXmlManager.getItemFromNameNullToIndexZero(this.machineModel);
		// TODO: 場当たり的な対応
		// ServiceModeAppの存在有無でデフォルトを切り替える。
		// UNKNOWN
		PackageManager manager = this.contextApplication.getPackageManager();
		try
		{
			ActivityInfo info = manager.getActivityInfo(new ComponentName("com.sec.android.app.servicemodeapp", "com.sec.android.app.servicemodeapp.ServiceModeApp"), 0);
		}
		catch(NameNotFoundException e)
		{
			String typeDefaultProcessingTypeName = "DEF4001_DIRECTLY_CALL_ACTIVITY_RADIOINFO";
			this.modelType.setTypeDefaultProcessingTypeName(typeDefaultProcessingTypeName);
		}
		Log.i(this.getClass().getSimpleName(),"ModelType      : " + this.modelType.toDebugString());

        //
        this.eventMulticaster = new EventMulticaster();
        this.registMultiEventDispatcher();

		//
		this.localeLanguage = this.contextApplication.getResources().getConfiguration().locale;
        Log.i(this.getClass().getSimpleName(),"localeLanguage : " + this.localeLanguage.getLanguage());
		//Locale.JAPANESE.getLanguage()




        // last execute
//        this.preferencesAccessor.setUpdateDefault(this);

        Log.d(this.getClass().getSimpleName(),"MachineInformation End");
	}


	public String getPackageVersion()
	{
		String result = "";
        try
        {
        	result = this.contextApplication.getPackageManager().getPackageInfo(this.contextApplication.getPackageName(), 0).versionName;
        }
        catch (NameNotFoundException e)
        {
        }
        return result;
	}


	public SignatureChecker getSignatureChecker()
	{
		return this.signaturechecker;
	}

	public PreferencesAccessor getPreferencesAccessor()
	{
		return this.preferencesAccessor;
	}

	public UUID getUUID()
	{
		return this.preferencesAccessor.getUUID();
	}

	public Mode getMode()
	{
		return this.preferencesAccessor.getApplicationMode();
	}

	public Version getVersionShowServiceMode()
	{
		return this.versionShowServiceMode;
	}

	public Version getVersionOS()
	{
		return this.versionOS;
	}

	public String getMachineModel()
	{
		return this.machineModel;
	}

	public String getDisplay()
	{
		return this.display;
	}
	
	public String getBuildInfo()
	{
		return this.buildInfo;
	}

	public ModelType getModelType()
	{
		return this.modelType;
	}

	public Locale getLocaleLanguage()
	{
		return this.localeLanguage;
	}


	public void checkStartStatusAndEventHandling()
	{
		boolean firststart = this.preferencesAccessor.getFirstStart();
		Version versionPreferences = this.preferencesAccessor.getPreferencesVersion();
		Version versionThis = this.getVersionShowServiceMode();
		String lastLanguage = this.preferencesAccessor.getLastLanguage();

		if(firststart)
		{
	        Log.d(this.getClass().getSimpleName(),"checkStartAndEventHandling OnFirstStart");
	        this.raiseOnFirstStart(new StartStatusEventObject(this));
		}
		else if(versionThis.getWeight() > versionPreferences.getWeight())
		{
	        Log.d(this.getClass().getSimpleName(),"checkStartAndEventHandling OnUpdateStart");
	        this.raiseOnUpdateStart(new StartStatusEventObject(this));
		}
		else if(versionThis.getWeight() < versionPreferences.getWeight())
		{
	        Log.d(this.getClass().getSimpleName(),"checkStartAndEventHandling OnDowngradeStart");
	        this.raiseOnDowngradeStart(new StartStatusEventObject(this));
		}
		else
		{
	        Log.d(this.getClass().getSimpleName(),"checkStartAndEventHandling OnNormalStart");
	        this.raiseOnNormalStart(new StartStatusEventObject(this));
		}

		if(!lastLanguage.equals(this.getLocaleLanguage().toString()))
		{
	        Log.d(this.getClass().getSimpleName(),"checkStartAndEventHandling OnLanguageIsChanged");
	        this.raiseOnLanguageIsChanged(new StartStatusEventObject(this));
		}

	}


	public void addOnFirstStartListener(OnFirstStartListener listener)
	{
        Log.d(this.getClass().getSimpleName(),"addOnFirstStartListener");
        this.eventMulticaster.addEventListener(OnFirstStartListener.class, listener);
    }

	public void addOnUpdateStartListener(OnUpdateStartListener listener)
	{
        Log.d(this.getClass().getSimpleName(),"addOnUpdateStartListener");
        this.eventMulticaster.addEventListener(OnUpdateStartListener.class, listener);
    }

	public void addOnDowngradeStartListener(OnDowngradeStartListener listener)
	{
        Log.d(this.getClass().getSimpleName(),"addOnDowngradeStartListener");
        this.eventMulticaster.addEventListener(OnDowngradeStartListener.class, listener);
    }

	public void addOnNormalStartListener(OnNormalStartListener listener)
	{
        Log.d(this.getClass().getSimpleName(),"addOnNormalStartListener");
        this.eventMulticaster.addEventListener(OnNormalStartListener.class, listener);
    }

	public void addOnLanguageIsChangedListener(OnLanguageIsChangedListener listener)
	{
        Log.d(this.getClass().getSimpleName(),"addOnLanguageIsChangedListener");
        this.eventMulticaster.addEventListener(OnLanguageIsChangedListener.class, listener);
    }


	public void raiseOnFirstStart(StartStatusEventObject param)
	{
        Log.d(this.getClass().getSimpleName(),"raiseOnFirstStart Start");
        this.eventMulticaster.fireEvent(OnFirstStartListener.class, param);
        Log.d(this.getClass().getSimpleName(),"raiseOnFirstStart End");
    }

	public void raiseOnUpdateStart(StartStatusEventObject param)
	{
        Log.d(this.getClass().getSimpleName(),"raiseOnUpdateStart Start");
        this.eventMulticaster.fireEvent(OnUpdateStartListener.class, param);
        Log.d(this.getClass().getSimpleName(),"raiseOnUpdateStart End");
    }

	public void raiseOnDowngradeStart(StartStatusEventObject param)
	{
        Log.d(this.getClass().getSimpleName(),"raiseOnDowngradeStart Start");
        this.eventMulticaster.fireEvent(OnDowngradeStartListener.class, param);
        Log.d(this.getClass().getSimpleName(),"raiseOnDowngradeStart End");
    }

	public void raiseOnNormalStart(StartStatusEventObject param)
	{
        Log.d(this.getClass().getSimpleName(),"raiseOnNormalStart Start");
        this.eventMulticaster.fireEvent(OnNormalStartListener.class, param);
        Log.d(this.getClass().getSimpleName(),"raiseOnNormalStart End");
    }

	public void raiseOnLanguageIsChanged(StartStatusEventObject param)
	{
        Log.d(this.getClass().getSimpleName(),"raiseOnLanguageIsChanged Start");
        this.eventMulticaster.fireEvent(OnLanguageIsChangedListener.class, param);
        Log.d(this.getClass().getSimpleName(),"raiseOnLanguageIsChanged End");
    }


	private void registMultiEventDispatcher()
	{
        // OnFirstStartListener
        this.eventMulticaster.registMultiEventDispatcher(
        		OnFirstStartListener.class,
        		new EventMulticaster.MultiEventDispatcher<OnFirstStartListener, StartStatusEventObject>()
        {
			@Override
			public void dispatch(OnFirstStartListener listener, StartStatusEventObject param)
			{
				listener.onFirstStart(param);
			}
        });

        // OnUpdateStartListener
        this.eventMulticaster.registMultiEventDispatcher(
        		OnUpdateStartListener.class,
        		new EventMulticaster.MultiEventDispatcher<OnUpdateStartListener, StartStatusEventObject>()
        {
			@Override
			public void dispatch(OnUpdateStartListener listener, StartStatusEventObject param)
			{
				listener.onUpdateStart(param);
			}
        });

        // OnDowngradeStartListener
        this.eventMulticaster.registMultiEventDispatcher(
        		OnDowngradeStartListener.class,
        		new EventMulticaster.MultiEventDispatcher<OnDowngradeStartListener, StartStatusEventObject>()
        {
			@Override
			public void dispatch(OnDowngradeStartListener listener, StartStatusEventObject param)
			{
				listener.onDowngradeStart(param);
			}
        });

        // OnNormalStartListener
        this.eventMulticaster.registMultiEventDispatcher(
        		OnNormalStartListener.class,
        		new EventMulticaster.MultiEventDispatcher<OnNormalStartListener, StartStatusEventObject>()
        {
			@Override
			public void dispatch(OnNormalStartListener listener, StartStatusEventObject param)
			{
				listener.onNormalStart(param);
			}
        });

        // OnLanguageIsChangedListener
        this.eventMulticaster.registMultiEventDispatcher(
        		OnLanguageIsChangedListener.class,
        		new EventMulticaster.MultiEventDispatcher<OnLanguageIsChangedListener, StartStatusEventObject>()
        {
			@Override
			public void dispatch(OnLanguageIsChangedListener listener, StartStatusEventObject param)
			{
				listener.onLanguageIsChanged(param);
			}
        });

	}




}
