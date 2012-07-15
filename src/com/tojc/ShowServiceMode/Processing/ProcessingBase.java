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
package com.tojc.ShowServiceMode.Processing;

import java.util.HashMap;
import java.util.List;

import com.tojc.ShowServiceMode.MainActivity;
import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public abstract class ProcessingBase
{
	protected Activity parent;
	protected ProcessingTypeId processingTypeId;
	protected String title;
	protected String subtitle;
	protected int shortcutid;

	public ProcessingBase(Activity parent, ProcessingTypeId processingTypeId, String title, String subtitle, int shortcutid)
	{
		this.parent = parent;
		this.processingTypeId = processingTypeId;
		this.title = title;
		this.subtitle = subtitle;
		this.shortcutid = shortcutid;
	}

	public ProcessingTypeId getProcessingTypeId()
	{
		return this.processingTypeId;
	}

	public abstract boolean isListItem();

	public String getName()
	{
		return this.processingTypeId.toString();
	}

	public String getTitle()
	{
		return this.title;
	}
	
	public String getSubTitle()
	{
		return this.subtitle;
	}

	public int getShortcutId()
	{
		return this.shortcutid;
	}
	
	@Override
	public String toString()
	{
		return this.processingTypeId.toString();
	}

	public void Execute(Activity parent, List<HashMap<String, Object>> list)
	{
        Log.d(this.getClass().getSimpleName(),"Execute Start:" + this.toString());
		try
		{
			Intent intent = CreateIntent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ExecuteShowServiceMode(parent, intent);
		}
		catch(Exception e)
		{
	        Log.e(this.getClass().getSimpleName(),"Failed to Dial", e);
	        Intent intent = CreateMainActivityIntent(e.getStackTrace().toString());
			this.parent.startActivity(intent);
		}
        Log.d(this.getClass().getSimpleName(),"Execute End:" + this.toString());
	}

	protected abstract Intent CreateIntent();
	protected abstract void ExecuteShowServiceMode(Activity parent, Intent intent);


	protected Intent CreateMainActivityIntent(String message)
	{
		Log.d(this.getClass().getSimpleName(),"CreateMainActivityIntent");

		Intent result = new Intent(Intent.ACTION_MAIN);
		result.setClassName(this.parent, MainActivity.class.getName());
		result.putExtra("com.tojc.ShowServiceMode", message);
		// MainActivityが起動中でも非起動中でも、先のActivityを終了して新しく作り直す。
		result.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		return result;
	}

}
