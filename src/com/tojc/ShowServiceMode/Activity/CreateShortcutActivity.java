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

import java.util.HashMap;
import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.ShowServiceModeCore;
import com.tojc.ShowServiceMode.Adapter.CallingMethodTypeListRowAdapter;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodTypeXmlManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CreateShortcutActivity extends Activity implements OnItemClickListener
{
	private ShowServiceModeCore core = null;

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
		Log.d(this.getClass().getSimpleName(),"CreateShortcutActivity.onCreate Start");

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shortcut);

		this.core = new ShowServiceModeCore(this.getApplicationContext());

		CallingMethodTypeXmlManager methodmanager = this.core.getUserListCallingMethodType();

        ListView list = (ListView)this.findViewById(R.id.listView);

        SimpleAdapter adapter = new CallingMethodTypeListRowAdapter(
        		this,
        		CallingMethodTypeListRowAdapter.CreateArrayListHashMap(methodmanager),
        		this.core.getDefaultProcessingType());
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> item = (HashMap<String, Object>)parent.getItemAtPosition(position);

		String methodTypeName = (String)item.get("name");

		int typeid = this.core.getUserListCallingMethodType().getItemFromName(methodTypeName).getTypeId();
		String shortcutname = "SSM" + typeid;

		Intent shortcutIntent = CallServiceModeActivity.createIntentForCallServiceModeActivity(this, methodTypeName);

		Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutname);
        //intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, R.drawable.ic_shortcut);
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_shortcut);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

        setResult(RESULT_OK, intent);
        finish();
	}

}
