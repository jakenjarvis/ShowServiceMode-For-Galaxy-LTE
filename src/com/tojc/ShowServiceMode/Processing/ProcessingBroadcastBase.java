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

import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class ProcessingBroadcastBase extends ProcessingBase
{
	protected String action;
	protected String uri;
	
	public ProcessingBroadcastBase(Activity parent, ProcessingTypeId processingTypeId, String title, String subtitle, int shortcutid)
	{
		super(parent, processingTypeId, title, subtitle, shortcutid);
	}

	@Override
	public boolean isListItem()
	{
		return true;
	}

	@Override
	public void ExecuteShowServiceMode(Activity parent, List<HashMap<String, Object>> list)
	{
        Log.d(this.getClass().getSimpleName(),"ExecuteShowServiceMode Start:" + this.toString());
		try
		{
			Intent intent = CreateIntent();
			parent.sendBroadcast(intent);
		}
		catch(Exception e)
		{
	        Log.e(this.getClass().getSimpleName(),"Failed to Broadcast", e);
	        Intent intent = CreateMainActivityIntent(e.getStackTrace().toString());
			this.parent.startActivity(intent);
		}
        Log.d(this.getClass().getSimpleName(),"ExecuteShowServiceMode End:" + this.toString());
	}

	@Override
	protected Intent CreateIntent()
	{
		return new Intent(this.action, Uri.parse(this.uri));
	}

}
