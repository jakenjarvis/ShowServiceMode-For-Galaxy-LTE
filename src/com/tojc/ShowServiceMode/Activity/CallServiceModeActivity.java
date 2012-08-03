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

import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.ShowServiceModeCore;
import com.tojc.ShowServiceMode.Processing.ProcessingBase;
import com.tojc.ShowServiceMode.Utility.Value;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodType;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodTypeXmlManager;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodTypes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class CallServiceModeActivity extends Activity
{
	private static final int DIALOG_ID_CALL_ERROR = 10;
	private static final int DIALOG_ID_PARAM_ERROR = 11;

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVisible(false);
        setContentView(R.layout.activity_call_service_mode);

        Log.d(this.getClass().getSimpleName(),"CallServiceModeActivity Start");

		boolean execute = false;
		int errorcode = 0;
        try
		{
            Intent intent = getIntent();
            if(intent != null)
            {
            	if(intent.hasExtra(ProcessingBase.class.getName()))
            	{
            		// メインからの呼び出し
            		// ProcessingBase Object
            		ProcessingBase processing = (ProcessingBase)intent.getSerializableExtra(ProcessingBase.class.getName());
                    Log.d(this.getClass().getSimpleName(),"processing:" + processing);

                	if(processing != null)
                	{
                    	showToast(processing.getId() + ":" + processing.getName() + "\n" + processing.getTitle());
                    	execute = processing.Execute(this);
                    	if(!execute)
                    	{
                    		errorcode = DIALOG_ID_CALL_ERROR;
                    	}
                	}
            	}
            	else if(intent.hasExtra(CallingMethodType.class.getName()))
            	{
            		// ショートカットからの呼び出し
            		// CallingMethodType Name
            		ShowServiceModeCore core = new ShowServiceModeCore(this.getApplicationContext());

                	String methodTypeName = intent.getStringExtra(CallingMethodType.class.getName());
                    CallingMethodType callingMethodType = core.getUserListCallingMethodType().getItemFromName(methodTypeName);

                	ProcessingBase processing = core.getDefaultProcessingType().getItemFromName(callingMethodType.getTypeProcessingTypeName()).createProcessingInstance(callingMethodType);
                    Log.d(this.getClass().getSimpleName(),"processing:" + processing);

                	if(processing != null)
                	{
                    	showToast(processing.getId() + ":" + processing.getName() + "\n" + processing.getTitle());
                    	execute = processing.Execute(this);
                    	if(execute)
                    	{
                    		int count = callingMethodType.getTypeCount();
                    		int setcount = (count >= Integer.MAX_VALUE) ? Value.DETERMINE_NUMBER_OF_NORMAL + 1 : count + 1;
                    		callingMethodType.setTypeCount(setcount);
                            Log.d(this.getClass().getSimpleName(),"Count: " + count + " -> " + setcount);
                    		if(setcount >= Value.DETERMINE_NUMBER_OF_NORMAL)
                    		{
                    			// TODO:
                    			callingMethodType.setTypeConditions("AUTOMATIC_CERTIFICATION");
                    		}

                    		// Save
                    		core.getUserListCallingMethodType().saveObjectToDataDataFilesXmlFile(this.getApplicationContext(), CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);
                    	}
                    	else
                    	{
                    		errorcode = DIALOG_ID_CALL_ERROR;
                    	}
                	}
            	}
            	else
            	{
            		errorcode = DIALOG_ID_PARAM_ERROR;
            	}
            }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		if(errorcode != 0)
    	{
    		if (!isFinishing())
    		{
    			showDialog(errorcode);
    		}
        }
		else
		{
    		finish();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args)
	{
		Log.d(this.getClass().getSimpleName(),"onCreateDialog Start");
		
		Dialog result = null;

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setTitle(getString(R.string.title_activity_setting));
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
        	public void onClick(DialogInterface dialog, int whichButton)
        	{
        		setResult(RESULT_OK);
        		finish();
        	}
       	});

		switch (id)
		{
			case DIALOG_ID_CALL_ERROR:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_CALL_ERROR");
	            alertDialog.setMessage(getString(R.string.call_error));
				break;

			case DIALOG_ID_PARAM_ERROR:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_PARAM_ERROR");
	            alertDialog.setMessage(getString(R.string.param_error));
				break;

	        default:
	            throw new IllegalArgumentException("Unknown Dialog Id:" + id);
		}

		result = alertDialog.create();

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
			case DIALOG_ID_CALL_ERROR:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_CALL_ERROR");
				break;

			case DIALOG_ID_PARAM_ERROR:
	    		Log.d(this.getClass().getSimpleName(),"DIALOG_ID_PARAM_ERROR");
				break;

	        default:
	            throw new IllegalArgumentException("Unknown Dialog Id:" + id);
		}

		Log.d(this.getClass().getSimpleName(),"onPrepareDialog End");
	}














	private void showToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	public static Intent createIntentForCallServiceModeActivity(Context contextApplication, ProcessingBase method)
	{
		Intent result = new Intent(Intent.ACTION_VIEW);
		result.setClassName(contextApplication, CallServiceModeActivity.class.getName());
		
		result.putExtra(ProcessingBase.class.getName(), method);
		//result.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return result;
	}

	public static Intent createIntentForCallServiceModeActivity(Context contextApplication, String methodname)
	{
		Intent result = new Intent(Intent.ACTION_VIEW);
		result.setClassName(contextApplication, CallServiceModeActivity.class.getName());
		
		result.putExtra(CallingMethodType.class.getName(), methodname);
		//result.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return result;
	}
}
