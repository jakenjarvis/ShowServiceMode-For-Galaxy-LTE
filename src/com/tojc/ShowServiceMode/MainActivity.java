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
package com.tojc.ShowServiceMode;

import java.util.HashMap;
import java.util.List;
import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;
import com.tojc.ShowServiceMode.Processing.ProcessingController;
import com.tojc.ShowServiceMode.Setting.PreferencesAccessor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.ClipboardManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity
	implements OnItemSelectedListener
{
	private String debugOutput = "";
	private ProcessingController pController;
	private PreferencesAccessor pAccessor;
	
	private EditText edtInput;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		Log.d(this.getClass().getSimpleName(),"MainActivity.onCreate");

        //
        this.pController = new ProcessingController(this);
        this.pAccessor = new PreferencesAccessor(this, this.pController.getDefaultProcessingTypeId());

        // set title version
        this.setTitle(this.getTitle() + " " + this.pController.ShowServiceModeVersion);

        //
        if(this.pController.isDebugSignature())
        {
            Log.d(this.getClass().getSimpleName(),"debug mode dialog.");

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(getString(R.string.AskYou));
            alertDialog.setMessage(getString(R.string.WillAskYouToProvideInformation));
            alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener()
            {
            	public void onClick(DialogInterface dialog, int whichButton)
            	{
            		setResult(RESULT_OK);
            	}
           	});
            alertDialog.create();
            alertDialog.show();
        }

        //
        boolean firststart = this.pAccessor.getFirstStart();
        if(firststart)
        {
        	//初回
            Log.d(this.getClass().getSimpleName(),"FirstStart setDefault");
            this.pAccessor.setDefault();
        
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(getString(R.string.notes));
            alertDialog.setMessage(getString(R.string.disclaimer));
            alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener()
            {
            	public void onClick(DialogInterface dialog, int whichButton)
            	{
            		setResult(RESULT_OK);
            	}
           	});
            alertDialog.create();
            alertDialog.show();
        }
        else
        {
            Log.d(this.getClass().getSimpleName(),"NotFirstStart");
            this.pController.setInstanceById(this.pAccessor.getProcessingTypeId());
        }
        
        //
        TextView description = (TextView)this.findViewById(R.id.ModelDescription);
        switch(this.pController.modeltype)
        {
        	case SC03D:
        		description.setText(R.string.description_sc03d);
        		break;
        	case SC05D:
        		description.setText(R.string.description_sc05d);
        		break;
        	case SC06D:
        		description.setText(R.string.description_sc06d);
        		break;
        	case SC01D:
        		description.setText(R.string.description_sc01d);
        		break;
        	default:
        		description.setText(R.string.description_other);
        		break;
        }
        
        //
        CheckBox checkBoxUseDefaultSettings = (CheckBox)this.findViewById(R.id.checkBoxUseDefaultSettings);

        boolean checked = this.pAccessor.getUseDefaultSettings();
		checkBoxUseDefaultSettings.setChecked(checked);

		//
        Spinner spinnerProcessingType;
        spinnerProcessingType = (Spinner)this.findViewById(R.id.spinnerProcessingType);

        List<HashMap<String, Object>> myData = this.pController.CreateArrayListHashMap(true);

        SimpleAdapter adapter = new SimpleAdapter(this, myData,
                R.layout.list_row, new String[] { "title", "subtitle" },
                new int[] { R.id.title, R.id.subtitle });
        spinnerProcessingType.setAdapter(adapter);
        spinnerProcessingType.setPrompt(getString(R.string.please_select_from_following_processing_types));
        spinnerProcessingType.setOnItemSelectedListener(this);
        selectItemSpinnerProcessingType(spinnerProcessingType, this.pController.getProcessingTypeId());

        //
        setEnabledControl(checked);

        //
        setBasicInformation();
        TextView BasicInformation = (TextView)this.findViewById(R.id.BasicInformation);
        BasicInformation.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
            	@SuppressWarnings("deprecation")
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            	clipboard.setText(debugOutput);
            	showToast(getString(R.string.clipboard_message));
            	
            	Uri uri = Uri.parse("http://jakenjarvis.wordpress.com/showservicemode-for-galaxy-lte/discussion/");
        		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        		startActivity(intent);
            }
        });

        //
        Intent intent = getIntent();
    	String param = intent.getStringExtra("com.tojc.ShowServiceMode");
        if(param != null)
        {
            Log.d(this.getClass().getSimpleName(),"Intent getStringExtra:" + param);

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(getString(R.string.error_detection));
            alertDialog.setMessage(getString(R.string.error_detection_message));
            alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener()
            {
            	public void onClick(DialogInterface dialog, int whichButton)
            	{
            		setResult(RESULT_OK);
            	}
           	});
            alertDialog.create();
            alertDialog.show();
        }

        Log.d(this.getClass().getSimpleName(),"onCreate end");
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		Spinner spinner = (Spinner)parent;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> item = (HashMap<String, Object>)spinner.getSelectedItem();

		this.pAccessor.setProcessingTypeId((ProcessingTypeId)item.get("id"));
		this.pController.setInstanceById((ProcessingTypeId)item.get("id"));
        setBasicInformation();

		//String message = (String)item.get("title") + "\nを選択しました。";
		//showToast(message);
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
	}
	
	public void onClickCheckBoxUseDefaultSettings(View view)
	{
		CheckBox checkBox = (CheckBox)view;
		boolean checked = checkBox.isChecked();

		this.pAccessor.setUseDefaultSettings(checked);

		setEnabledControl(checked);
	}
	
	public void onClickButtonExecuteShowServiceMode(View view)
	{
        Log.d(this.getClass().getSimpleName(),"onClickButtonExecuteShowServiceMode start");

        Intent shortcutIntent = CreateShowServiceModeActivityIntent();
        // what warn...? java.lang.SecurityException: Permission Denial: getTasks() from pid=4826, uid=10173 requires android.permission.GET_TASKS
		startActivity(shortcutIntent);

		Log.d(this.getClass().getSimpleName(),"onClickButtonExecuteShowServiceMode end");
	}
	
	public void onClickButtonCreateShortcut(View view)
	{
		edtInput = new EditText(this);
		edtInput.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

		//String ptypename = pController.getInstance().getTitle();

		new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_shortcut)
			.setTitle(getString(R.string.enter_name_of_shortcut))
			.setMessage(getString(R.string.when_you_press_ok_button_without_entering_with_an_automatically_generated_name))
			.setView(edtInput)
			.setPositiveButton("OK", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					// ショートカットをHOMEに作成する
			        Intent shortcutIntent = CreateShowServiceModeActivityIntent();
			        String shortcutname = "SSM " + pController.getProcessingTypeId().toString();
			        int shortcutid = pController.getInstance().getShortcutId();

			        if(edtInput.getText().toString().trim().length() != 0)
					{
						shortcutname = edtInput.getText().toString().trim();
					}

			        SendCreateShortcutIntent(shortcutIntent, shortcutname, shortcutid);
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
				}
			})
			.show();
	}

	private Intent CreateShowServiceModeActivityIntent()
	{
		Log.d(this.getClass().getSimpleName(),"CreateShowServiceModeActivityIntent");

		Intent result = new Intent(Intent.ACTION_VIEW);
		result.setClassName(this, ShowServiceModeActivity.class.getName());
		result.putExtra("com.tojc.ShowServiceMode.Enum.ProcessingTypeId",this.pController.getProcessingTypeId().toString());
		return result;
	}

	private void SendCreateShortcutIntent(Intent shortcutIntent, String shortcutname, int iconResourceId)
	{
		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutname);
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this, iconResourceId);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		sendBroadcast(intent);
	}


	private void setEnabledControl(boolean useDefaultSettings)
	{
        Spinner spinnerProcessingType = (Spinner)this.findViewById(R.id.spinnerProcessingType);
        
		if(useDefaultSettings)
		{
			spinnerProcessingType.setEnabled(false);
	        selectItemSpinnerProcessingType(spinnerProcessingType, this.pController.getDefaultProcessingTypeId());
		}
		else
		{
			spinnerProcessingType.setEnabled(true);
		}
	}
	
	private void selectItemSpinnerProcessingType(Spinner spinner, ProcessingTypeId pt)
	{
        for(int i = 0; i < spinner.getCount(); i++)
        {
        	@SuppressWarnings("unchecked")
			HashMap<String, Object> item = (HashMap<String, Object>)spinner.getItemAtPosition(i);
        	
        	if (pt == (ProcessingTypeId)item.get("id"))
        	{
        		spinner.setSelection(i);
        		break;
        	}
        }
	}
	
	private void setBasicInformation()
	{
        TextView BasicInformation = (TextView)this.findViewById(R.id.BasicInformation);

        String text = getString(R.string.basic_information);

        debugOutput = "SSM" + this.pController.ShowServiceModeVersion
			+ " :(" + this.pController.model + "-" + this.pController.version + ")"
			+ " :DT=" + this.pController.getDefaultProcessingTypeId().toString()
			+ " :ST=" + this.pController.getProcessingTypeId().toString();

        BasicInformation.setText(text + debugOutput);
	}

	private void showToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}
	
	
	
	
}
