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
import java.util.Random;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Obfuscator;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;
import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.ShowServiceModeCore;
import com.tojc.ShowServiceMode.Adapter.CallingMethodTypeListRowAdapter;
import com.tojc.ShowServiceMode.Hide.NetworkTypeManager;
import com.tojc.ShowServiceMode.Processing.ProcessingBase;
import com.tojc.ShowServiceMode.Processing.TypeToInstance;
import com.tojc.ShowServiceMode.Utility.ClipboardTextManager;
import com.tojc.ShowServiceMode.Utility.Mode;
import com.tojc.ShowServiceMode.Utility.Value;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodType;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodTypeXmlManager;
import com.tojc.ShowServiceMode.XmlObject.Model.ModelType;
import com.tojc.ShowServiceMode.XmlObject.Processing.ProcessingType;
import com.tojc.ShowServiceMode.XmlObject.SecretCode.SecretCodeType;
import com.tojc.ShowServiceMode.Processing.ProcessingBase;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnItemSelectedListener
{
	private static final int DIALOG_ID_FIRSTSTART = 1;
	private static final int DIALOG_ID_ASKYOU = 2;
	private static final int DIALOG_ID_ASKYOU_PIRATES = 3;
	private static final int DIALOG_ID_INPUT_SHORTCUT_NAME = 4;

	private ShowServiceModeCore core = null;
	private NetworkTypeManager networkTypeManager = null;

	private EditText edtInput = null;

	private boolean startAskYouFlg = true;


    private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;
    // A handler on the UI thread.
    private Handler mHandler;


    @Override
	public void onCreate(Bundle savedInstanceState)
	{
        Log.d(this.getClass().getSimpleName(),"onCreate Start");

        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		this.edtInput = new EditText(this);

		if(savedInstanceState != null)
        {
			Log.d(this.getClass().getSimpleName(),"savedInstanceState recovery");

			this.startAskYouFlg = savedInstanceState.getBoolean("startAskYouFlg", true);
    		this.edtInput.setText(savedInstanceState.getString("edtInput"));
        }

        this.mHandler = new Handler();

        String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        this.mLicenseCheckerCallback = new LooseLicenseCheckerCallback();
        this.mChecker = new LicenseChecker(this, new ServerManagedPolicy(this, (Obfuscator)new AESObfuscator(Value.SALT, getPackageName(), deviceId)), Value.PBKEY);
        this.mChecker.checkAccess(mLicenseCheckerCallback);

        Log.d(this.getClass().getSimpleName(),"onCreate End");
	}

    @Override
    public void onRestart()
    {
		Log.d(this.getClass().getSimpleName(),"onRestart Start");
        super.onRestart();
		Log.d(this.getClass().getSimpleName(),"onRestart End");
    }

    @Override
    public void onStart()
    {
		Log.d(this.getClass().getSimpleName(),"onStart Start");
        super.onStart();
		Log.d(this.getClass().getSimpleName(),"onStart End");
    }

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		Log.d(this.getClass().getSimpleName(),"onRestoreInstanceState Start");
		super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
			Log.d(this.getClass().getSimpleName(),"savedInstanceState recovery");

			this.startAskYouFlg = savedInstanceState.getBoolean("startAskYouFlg", true);
    		this.edtInput.setText(savedInstanceState.getString("edtInput"));
        }

        Log.d(this.getClass().getSimpleName(),"onRestoreInstanceState End");
	}

    @Override
    public void onResume()
    {
		Log.d(this.getClass().getSimpleName(),"onResume Start");
        super.onResume();

		// Create Core
        if(this.core == null)
        {
    		this.core = new ShowServiceModeCore(this.getApplicationContext());
        }

        this.setTitle(this.getString(R.string.title_activity_setting)
        		+ " " + this.core.getDisplayVersion());


        // Event Trigger
        //this.core.getMachineInformation().checkStartStatusAndEventHandling();

        //
        if(this.core.getMachineInformation().getPreferencesAccessor().getFirstStart())
        {
    		this.core.getMachineInformation().getPreferencesAccessor().setFirstStart(false);

    		if (!isFinishing())
    		{
        		// 初回起動メッセージ
        		showDialog(DIALOG_ID_FIRSTSTART);

        		// 初回は、お願いメッセージも出したことにする。（回転時の表示回避）
        		this.startAskYouFlg = false;
    		}
        }
        else if(this.startAskYouFlg)
        {
            Mode mode = this.core.getMachineInformation().getMode();
            Log.i(this.getClass().getSimpleName(),"Mode:" + mode.toString());
            switch(mode)
            {
            	case Release:
            		break;
            	case BuildOwn:
            		break;

            	case Free:
            		if (!isFinishing())
            		{
                    	// お願いメッセージ
                		showDialog(DIALOG_ID_ASKYOU);
            		}
                    break;

            	case Pirates:
            		if (!isFinishing())
            		{
                    	// お願いメッセージ
                		showDialog(DIALOG_ID_ASKYOU_PIRATES);
            		}
                    break;
            }
        	this.startAskYouFlg = false;
        }

        //
        final TextView textNetworkType = (TextView)this.findViewById(R.id.textNetworkType);
		switch(this.core.getMachineInformation().getMode())
		{
			case BuildOwn:
			case Release:
			//case Free:
				textNetworkType.setEnabled(true);
				textNetworkType.setVisibility(View.VISIBLE);

				this.networkTypeManager = new NetworkTypeManager(
					this.getApplicationContext(),
					new NetworkTypeManager.NetworkTypeChangedListener()
				{
					@Override
					public void onNetworkTypeChanged(NetworkTypeManager manager)
					{
		        		String labelnetworktype = getString(R.string.network_type) + " " + manager.toString();
		        		textNetworkType.setText(labelnetworktype);
					}
				});
				break;

			default:
				textNetworkType.setEnabled(false);
				textNetworkType.setVisibility(View.GONE);
				break;
		}

        //
        TextView description = (TextView)this.findViewById(R.id.ModelDescription);
        description.setText(this.core.getMachineInformation().getModelType().getTypeDescription().replace("\\n", "\n"));

        //
        CheckBox checkBoxUseDefaultSettings = (CheckBox)this.findViewById(R.id.checkBoxUseDefaultSettings);
        boolean checked = this.core.getMachineInformation().getPreferencesAccessor().getUseDefaultSettings();
		checkBoxUseDefaultSettings.setChecked(checked);

        //
        setEnabledControl(checked);

		//
		CallingMethodTypeXmlManager methodmanager = this.core.getUserListCallingMethodType();
		Spinner spinnerProcessingType = (Spinner)this.findViewById(R.id.spinnerProcessingType);

        SimpleAdapter adapter = new CallingMethodTypeListRowAdapter(
        		this,
        		CallingMethodTypeListRowAdapter.CreateArrayListHashMap(methodmanager),
        		this.core.getDefaultProcessingType());
        spinnerProcessingType.setAdapter(adapter);
        spinnerProcessingType.setPrompt(getString(R.string.please_select_from_following_processing_types));
        spinnerProcessingType.setOnItemSelectedListener(this);
        //
        selectItemSpinnerProcessingType(spinnerProcessingType, this.core.getMachineInformation().getPreferencesAccessor().getCallingMethodTypeName());

        //
        setUpdateDisplayInformation();
//        TextView BasicInformation = (TextView)this.findViewById(R.id.BasicInformation);
//        BasicInformation.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View view)
//            {
//            	ClipboardTextManager clip = new ClipboardTextManager(SettingActivity.this.getApplicationContext());
//            	clip.copy(core.getDebugOutput());
//
//            	showToast(getString(R.string.clipboard_message));
//
//            	Uri uri = Uri.parse("http://jakenjarvis.wordpress.com/showservicemode-for-galaxy-lte/discussion/");
//        		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        		startActivity(intent);
//            }
//        });

        //
		Button buttonBuyShowServiceMode = (Button)this.findViewById(R.id.buttonBuyShowServiceMode);
        switch(this.core.getMachineInformation().getMode())
        {
        	case Release:
        		buttonBuyShowServiceMode.setEnabled(false);
        		buttonBuyShowServiceMode.setVisibility(View.GONE);
        		break;

        	case Pirates:
        		checked = true;
        		checkBoxUseDefaultSettings.setChecked(checked);
        		checkBoxUseDefaultSettings.setEnabled(false);
        		this.core.getMachineInformation().getPreferencesAccessor().setUseDefaultSettings(checked);
        		setEnabledControl(checked);
        		//not break;

        	default:
        		String label = getString(R.string.BuyShowServiceMode);
        		String random = getString(R.string.BuyShowServiceModeRandom);
        		if(random.trim().length() != 0)
        		{
        			String[] array = random.split(",", -1);
        			int index = (int)Math.floor(Math.random() * array.length);
            		buttonBuyShowServiceMode.setText(array[index] + label);
        		}
    			break;
        }

        Log.d(this.getClass().getSimpleName(),"onResume End");
    }

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		Log.d(this.getClass().getSimpleName(),"onSaveInstanceState Start");
		super.onSaveInstanceState(savedInstanceState);
		
        if(savedInstanceState != null)
        {
			Log.d(this.getClass().getSimpleName(),"savedInstanceState saved");

			savedInstanceState.putBoolean("startAskYouFlg", this.startAskYouFlg);
    		savedInstanceState.putString("edtInput", this.edtInput.getText().toString());
        }

		Log.d(this.getClass().getSimpleName(),"onSaveInstanceState End");
	}

    @Override
    public void onPause()
    {
		Log.d(this.getClass().getSimpleName(),"onPause Start");
        super.onPause();
    	if(isFinishing())
    	{
    	}
		Log.d(this.getClass().getSimpleName(),"onPause End");
    }

    @Override
    public void onStop()
    {
		Log.d(this.getClass().getSimpleName(),"onStop Start");
        super.onStop();
		Log.d(this.getClass().getSimpleName(),"onStop End");
    }

    @Override
    public void onDestroy()
    {
		Log.d(this.getClass().getSimpleName(),"onDestroy Start");
        super.onDestroy();
        this.mChecker.onDestroy();
		Log.d(this.getClass().getSimpleName(),"onDestroy End");
    }

	@Override
	public void onLowMemory()
	{
        super.onLowMemory();
	}

	@Override
    protected Dialog onCreateDialog(int id, Bundle args)
	{
		Log.d(this.getClass().getSimpleName(),"onCreateDialog Start");

		Dialog result = null;

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        switch (id)
		{
        	case DIALOG_ID_FIRSTSTART:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_FIRSTSTART");

	    		alertDialog.setTitle(getString(R.string.notes));
                alertDialog.setMessage(getString(R.string.disclaimer));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                	public void onClick(DialogInterface dialog, int whichButton)
                	{
                		setResult(RESULT_OK);
                	}
               	});
                result = alertDialog.create();
                break;

        	case DIALOG_ID_ASKYOU:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_ASKYOU");

	    		alertDialog.setTitle(getString(R.string.AskYou));
                alertDialog.setMessage(getString(R.string.AskYouFreeMessage));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                	public void onClick(DialogInterface dialog, int whichButton)
                	{
                		setResult(RESULT_OK);
                	}
               	});
                result = alertDialog.create();
                break;

        	case DIALOG_ID_ASKYOU_PIRATES:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_ASKYOU_PIRATES");

	    		alertDialog.setTitle(getString(R.string.AskYou));
                alertDialog.setMessage(getString(R.string.AskYouPiratesMessage));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                	public void onClick(DialogInterface dialog, int whichButton)
                	{
                		setResult(RESULT_OK);
                	}
               	});
                result = alertDialog.create();
        		break;

	        case DIALOG_ID_INPUT_SHORTCUT_NAME:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_INPUT_SHORTCUT_NAME");

//	    		String shortcutname = args.getString("shortcutname");

	        	this.edtInput.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//	            this.edtInput.setText(shortcutname);

	        	alertDialog.setIcon(R.drawable.ic_shortcut);
				alertDialog.setTitle(getString(R.string.enter_name_of_shortcut));
				alertDialog.setView(this.edtInput);

				alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						// ショートカットをHOMEに作成する
				        Intent shortcutIntent = CreateCallServiceModeActivityIntent(false);
				        String shortcutname = "";
				        if(edtInput.getText().toString().trim().length() != 0)
						{
							shortcutname = edtInput.getText().toString().trim();
						}
				        SendCreateShortcutIntent(shortcutIntent, shortcutname);
                		setResult(RESULT_OK);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						
					}
				});
                result = alertDialog.create();
	            break;

	        default:
	            throw new IllegalArgumentException("Unknown Dialog Id:" + id);
		}

		Log.d(this.getClass().getSimpleName(),"onCreateDialog End");
		return result;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args)
	{
		Log.d(this.getClass().getSimpleName(),"onPrepareDialog Start");
		super.onPrepareDialog(id, dialog, args);

        AlertDialog alertDialog = (AlertDialog)dialog;

        switch (id)
		{
        	case DIALOG_ID_FIRSTSTART:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_FIRSTSTART");
                break;

        	case DIALOG_ID_ASKYOU:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_ASKYOU");
                break;

        	case DIALOG_ID_ASKYOU_PIRATES:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_ASKYOU_PIRATES");
                break;

	        case DIALOG_ID_INPUT_SHORTCUT_NAME:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_INPUT_SHORTCUT_NAME");
//	    		String shortcutname = args.getString("shortcutname");
//	            this.edtInput.setText(shortcutname);

				alertDialog.setView(this.edtInput);
	            break;

	        default:
	            throw new IllegalArgumentException("Unknown Dialog Id:" + id);
		}
		Log.d(this.getClass().getSimpleName(),"onPrepareDialog End");
	}



	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
        Spinner spinner = (Spinner)arg0;
		@SuppressWarnings("unchecked")
		HashMap<String, Object> item = (HashMap<String, Object>)spinner.getSelectedItem();

		String name = (String)item.get("name");
		this.core.getMachineInformation().getPreferencesAccessor().setCallingMethodTypeName(name);

		Log.d(this.getClass().getSimpleName(),"onItemSelected:" + name);

        setUpdateDisplayInformation();

		//String message = (String)item.get("title") + "\nを選択しました。";
		//showToast(message);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
        Log.d(this.getClass().getSimpleName(),"onNothingSelected");
        
        //this.core.getMachineInformation().getPreferencesAccessor().getCallingMethodTypeName()
        
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		getMenuInflater().inflate(R.menu.activity_setting, menu);
//		return true;
//	}

	public void onClickCheckBoxUseDefaultSettings(View view)
	{
		CheckBox checkBox = (CheckBox)view;
		boolean checked = checkBox.isChecked();

		this.core.getMachineInformation().getPreferencesAccessor().setUseDefaultSettings(checked);
		setEnabledControl(checked);
	}

	public void onClickButtonExecuteCallServiceModeActivity(View view)
	{
        Log.d(this.getClass().getSimpleName(),"onClickButtonExecuteCallServiceModeActivity Start");

        Intent shortcutIntent = CreateCallServiceModeActivityIntent(true);
        // what warn...? java.lang.SecurityException: Permission Denial: getTasks() from pid=4826, uid=10173 requires android.permission.GET_TASKS
		startActivity(shortcutIntent);

		Log.d(this.getClass().getSimpleName(),"onClickButtonExecuteCallServiceModeActivity End");
	}

	public void onClickButtonCreateShortcut(View view)
	{
        Log.d(this.getClass().getSimpleName(),"onClickButtonCreateShortcut Start");

        // MEMO: 初期値のセットは、ボタン押下時のみとする。
        // ダイアログ表示状態での端末回転、及び、ライフサイクルに対応
        String methodTypeName = this.core.getMachineInformation().getPreferencesAccessor().getCallingMethodTypeName();
		int typeid = this.core.getUserListCallingMethodType().getItemFromName(methodTypeName).getTypeId();
		String shortcutname = "SSM" + typeid;

		Bundle args = new Bundle();
		args.putString("shortcutname", shortcutname);

		this.edtInput.setText(shortcutname);

		if (!isFinishing())
		{
			showDialog(DIALOG_ID_INPUT_SHORTCUT_NAME);
//			showDialog(DIALOG_ID_INPUT_SHORTCUT_NAME, args);
		}

        Log.d(this.getClass().getSimpleName(),"onClickButtonCreateShortcut End");
	}


	public void onClickButtonBasicInformation(View view)
	{
        Log.d(this.getClass().getSimpleName(),"onClickButtonBasicInformation Start");

    	ClipboardTextManager clip = new ClipboardTextManager(SettingActivity.this.getApplicationContext());
    	clip.copy(this.core.getDebugOutput());

    	showToast(getString(R.string.clipboard_message));

        Log.d(this.getClass().getSimpleName(),"onClickButtonBasicInformation End");
	}

	public void onClickButtonOpenHomepage(View view)
	{
        Log.d(this.getClass().getSimpleName(),"onClickButtonOpenHomepage Start");

    	ClipboardTextManager clip = new ClipboardTextManager(SettingActivity.this.getApplicationContext());
    	clip.copy(this.core.getDebugOutput());

    	showToast(getString(R.string.clipboard_message));

    	Uri uri = Uri.parse("http://jakenjarvis.wordpress.com/showservicemode-for-galaxy-lte/discussion320/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

        Log.d(this.getClass().getSimpleName(),"onClickButtonOpenHomepage End");
	}

	public void onClickButtonAboutShowServiceMode(View view)
	{
        Log.d(this.getClass().getSimpleName(),"onClickButtonAboutShowServiceMode Start");

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName(this, AboutShowServiceModeActivity.class.getName());

		this.startActivity(intent);

        Log.d(this.getClass().getSimpleName(),"onClickButtonAboutShowServiceMode End");
	}

	public void onClickButtonBuyShowServiceMode(View view)
	{
        Log.d(this.getClass().getSimpleName(),"onClickButtonBuyShowServiceMode Start");

        Uri uri = Uri.parse("market://details?id=com.tojc.ShowServiceMode");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Log.d(this.getClass().getSimpleName(),"onClickButtonBuyShowServiceMode End");
	}




	private void showToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	private void selectItemSpinnerProcessingType(Spinner spinner, String keyname)
	{
		Log.d(this.getClass().getSimpleName(), "selectItemSpinnerProcessingType:" + this.core.getMachineInformation().getPreferencesAccessor().getCallingMethodTypeName());

		for(int i = 0; i < spinner.getCount(); i++)
        {
        	@SuppressWarnings("unchecked")
			HashMap<String, Object> item = (HashMap<String, Object>)spinner.getItemAtPosition(i);
        	
        	if(keyname.equals((String)item.get("name")))
        	{
        		Log.d(this.getClass().getSimpleName(), "setSelection:" + keyname);

        		// MEMO: setSelection(i)だと、0番目が選択されることがある。
        		// onRestartから走るケースで発生。
        		spinner.setSelection(i, true);
        		break;
        	}
        }
	}

	private void setEnabledControl(boolean useDefaultSettings)
	{
		Log.d(this.getClass().getSimpleName(), "setEnabledControl:" + this.core.getMachineInformation().getModelType().getTypeDefaultProcessingTypeName());

		Spinner spinnerProcessingType = (Spinner)this.findViewById(R.id.spinnerProcessingType);
		if(useDefaultSettings)
		{
			spinnerProcessingType.setEnabled(false);
	        selectItemSpinnerProcessingType(spinnerProcessingType, this.core.getMachineInformation().getModelType().getTypeDefaultProcessingTypeName());
		}
		else
		{
			spinnerProcessingType.setEnabled(true);
		}
	}

	private void setUpdateDisplayInformation()
	{
		// get select item
		Spinner spinnerProcessingType = (Spinner)this.findViewById(R.id.spinnerProcessingType);
    	@SuppressWarnings("unchecked")
		HashMap<String, Object> item = (HashMap<String, Object>)spinnerProcessingType.getSelectedItem();

    	String name = (String)item.get("name");
    	CallingMethodType callingMethodType = this.core.getUserListCallingMethodType().getItemFromName(name);

    	String processingtypename = (String)item.get("processingtypename");
    	ProcessingType processingType = this.core.getDefaultProcessingType().getItemFromName(processingtypename);

    	ProcessingBase processing = TypeToInstance.createProcessingInstance(processingType, callingMethodType);

    	// ButtonBasicInformation
		Button buttonBasicInformation = (Button)this.findViewById(R.id.buttonBasicInformation);
		String text = getString(R.string.basic_information_clipboard);
        buttonBasicInformation.setText(text + "\n" + this.core.getDebugOutput());

        // ProcessingTypeDescription
        TextView processingTypeDescription = (TextView)this.findViewById(R.id.ProcessingTypeDescription);
    	String text2 = processingType.getTypeTitle() + "\n" + processingType.getTypeDescription();
    	processingTypeDescription.setText(text2);
	
    	// SecretCodeTypeDescription
        TextView secretCodeTypeDescription = (TextView)this.findViewById(R.id.SecretCodeTypeDescription);
        String secretcodekey = processing.findSecretCodeValueFromSpecialKey();
        if(secretcodekey != null)
        {
        	ModelType modelType = this.core.getMachineInformation().getModelType();
        	
        	String title = this.core.getDefaultSecretCodeType().getTitleFromModelType(modelType, secretcodekey);
        	String description = this.core.getDefaultSecretCodeType().getDescriptionFromModelType(modelType, secretcodekey);
        	
        	if((title != null) && (description != null))
        	{
            	if((title.length() != 0) && (description.length() != 0))
            	{
            		secretCodeTypeDescription.setText(title + "\n" + description.replace("\\n", "\n"));
            	}
        	}
        }
	
	}

	private Intent CreateCallServiceModeActivityIntent(boolean createInstance)
	{
		Log.d(this.getClass().getSimpleName(), "CreateCallServiceModeActivityIntent");

		Intent result = null;
		
		String methodTypeName = this.core.getMachineInformation().getPreferencesAccessor().getCallingMethodTypeName();
        CallingMethodType callingMethodType = this.core.getUserListCallingMethodType().getItemFromName(methodTypeName);

        if(createInstance)
        {
        	ProcessingBase processingInstance = this.core.getDefaultProcessingType().getItemFromName(callingMethodType.getTypeProcessingTypeName()).createProcessingInstance(callingMethodType);
            result = CallServiceModeActivity.createIntentForCallServiceModeActivity(this, processingInstance);
        }
        else
        {
            result = CallServiceModeActivity.createIntentForCallServiceModeActivity(this, callingMethodType.getTypeName());
        }
        return result;
	}

	private void SendCreateShortcutIntent(Intent shortcutIntent, String shortcutname)
	{
		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutname);
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_shortcut);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");


		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		sendBroadcast(intent);
	}





    private class LooseLicenseCheckerCallback implements LicenseCheckerCallback
    {
        public void allow(int policyReason)
        {
    		Log.i(this.getClass().getSimpleName(), "LicenseChecker allow");
            if (!isFinishing())
            {
                mHandler.post(new Runnable()
                {
                    public void run()
                    {
                    	Mode judgeMode = core.judgeModeFromSignature();
                    	if(judgeMode == Mode.Release)
                    	{
                    		Mode preferencesMode = core.getMachineInformation().getPreferencesAccessor().getApplicationMode();
                    		if(preferencesMode != Mode.Release)
                    		{
                        		Log.i(this.getClass().getSimpleName(), "LicenseChecker Certification Release");
                        		core.getMachineInformation().getPreferencesAccessor().setApplicationMode(Mode.Release);
                        		setUpdateDisplayInformation();
                    		}
                    	}
                    }
                });
            }
        }

        public void dontAllow(int policyReason)
        {
    		Log.i(this.getClass().getSimpleName(), "LicenseChecker dontAllow:" + policyReason);
    		// LICENSED     = 0x0100; 256
    		// NOT_LICENSED = 0x0231; 561
    		// RETRY        = 0x0123; 291
            if (!isFinishing())
            {
            	switch(policyReason)
            	{
            		case Policy.NOT_LICENSED:
                        mHandler.post(new Runnable()
                        {
                            public void run()
                            {
                            	long bootCount = core.getMachineInformation().getPreferencesAccessor().getBootCount();
                            	Mode judgeMode = core.judgeModeFromSignature();
                            	if((judgeMode == Mode.Release) && (bootCount >= Value.LICENSE_GRACE_MAX_BOOT_COUNT))
                            	{
                            		Log.i(this.getClass().getSimpleName(), "LicenseChecker Certification Pirates");
                            		core.getMachineInformation().getPreferencesAccessor().setApplicationMode(Mode.Pirates);
                            		setUpdateDisplayInformation();
                            	}
                            }
                        });
            			break;

            		case Policy.RETRY:
            			break;

            		default:
        				break;
            	}
            }
        }

        public void applicationError(int errorCode)
        {
        	Log.i(this.getClass().getSimpleName(), "LicenseChecker applicationError:" + errorCode);
        	// ERROR_INVALID_PACKAGE_NAME = 1;
        	// ERROR_NON_MATCHING_UID     = 2;
        	// ERROR_NOT_MARKET_MANAGED   = 3;
        	// ERROR_CHECK_IN_PROGRESS    = 4;
        	// ERROR_INVALID_PUBLIC_KEY   = 5;
        	// ERROR_MISSING_PERMISSION   = 6;
            if (!isFinishing())
            {
            }
        }
    }



}

