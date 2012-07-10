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

import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferencesAccessor
{
	private Activity parent;
	private SharedPreferences sp;
	private ProcessingTypeId defaultProcessingType;

	public PreferencesAccessor(Activity parent, ProcessingTypeId defaultProcessingType)
	{
		this.parent = parent;
		this.sp = PreferenceManager.getDefaultSharedPreferences(this.parent);
		this.defaultProcessingType = defaultProcessingType;
	}

	public void setDefault()
	{
		Editor editer = this.sp.edit();
		editer.putBoolean("FirstStart", false); // after getFirstStart
		editer.putBoolean("UseDefaultSettings", true);
		editer.putString("ProcessingType", this.defaultProcessingType.toString());
		editer.commit();
	}

	// FirstStart
	public void setFirstStart(boolean value)
	{
		this.sp.edit().putBoolean("FirstStart", value).commit();
	}
	public boolean getFirstStart()
	{
		return this.sp.getBoolean("FirstStart", true);
	}

	// UseDefaultSettings
	public void setUseDefaultSettings(boolean value)
	{
		this.sp.edit().putBoolean("UseDefaultSettings", value).commit();
	}
	public boolean getUseDefaultSettings()
	{
		return this.sp.getBoolean("UseDefaultSettings", true);
	}

	// ProcessingType
	public void setProcessingTypeId(ProcessingTypeId value)
	{
		this.sp.edit().putString("ProcessingTypeId", value.toString()).commit();
	}
	public ProcessingTypeId getProcessingTypeId()
	{
		String value = this.sp.getString("ProcessingTypeId", "");
		if(value.length() == 0)
		{
			value = this.defaultProcessingType.toString();
		}
		return ProcessingTypeId.toProcessingTypeId(value);
	}
}
