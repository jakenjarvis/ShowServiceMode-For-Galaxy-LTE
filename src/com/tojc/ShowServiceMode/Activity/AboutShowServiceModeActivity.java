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
package com.tojc.ShowServiceMode.Activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.ShowServiceModeCore;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AboutShowServiceModeActivity extends Activity
{
	private ShowServiceModeCore core = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.d(this.getClass().getSimpleName(),"AboutShowServiceModeActivity.onCreate Start");

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_showservicemode);

        //
        this.core = new ShowServiceModeCore(this.getApplicationContext());

		//
        TextView displayVersion = (TextView)this.findViewById(R.id.DisplayVersion);
        displayVersion.setText(this.getString(R.string.title_activity_setting)
        		+ " " + this.core.getDisplayVersion());

        //
        TextView acknowledgmentText = (TextView)this.findViewById(R.id.AcknowledgmentText);
        String acktxt = loadAssetsTxtFile(this, R.raw.acknowledgment);
        acknowledgmentText.setText(acktxt);

//
        TextView versionHistoryText = (TextView)this.findViewById(R.id.VersionHistoryText);
        String vhsttxt = loadAssetsTxtFile(this, R.raw.version_history);
        versionHistoryText.setText(vhsttxt);

        Log.d(this.getClass().getSimpleName(),"AboutShowServiceModeActivity.onCreate End");
	}



	private String loadAssetsTxtFile(Context contextApplication, int resourceId)
	{
		String result = "";
		try
		{
			StringBuilder sb = new StringBuilder();

			Resources res = contextApplication.getResources();
    		InputStream stream = res.openRawResource(resourceId);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));

			String line;
			while((line = br.readLine()) != null)
			{
			    sb.append(line +"\n");
			}
			result = sb.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}












}
