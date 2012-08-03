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

import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnDowngradeStartListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnFirstStartListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnLanguageIsChangedListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnNormalStartListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.OnUpdateStartListener;
import com.tojc.ShowServiceMode.Event.StartStatusEvent.StartStatusEventObject;
import com.tojc.ShowServiceMode.Processing.ProcessingBase;
import com.tojc.ShowServiceMode.Setting.MachineInformation;
import com.tojc.ShowServiceMode.Utility.Mode;
import com.tojc.ShowServiceMode.Utility.Value;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodType;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodTypeXmlManager;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodTypes;
import com.tojc.ShowServiceMode.XmlObject.Model.ModelTypeXmlManager;
import com.tojc.ShowServiceMode.XmlObject.Model.ModelTypes;
import com.tojc.ShowServiceMode.XmlObject.Processing.ProcessingTypeXmlManager;
import com.tojc.ShowServiceMode.XmlObject.Processing.ProcessingTypes;
import com.tojc.ShowServiceMode.XmlObject.SecretCode.SecretCodeTypeXmlManager;
import com.tojc.ShowServiceMode.XmlObject.SecretCode.SecretCodeTypes;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class ShowServiceModeCore
{
	private Context contextApplication;

	private ProcessingTypeXmlManager processingTypeDefault;
	private ModelTypeXmlManager modelTypeDefault;
	private SecretCodeTypeXmlManager secretCodeTypeDefault;
	private CallingMethodTypeXmlManager callingMethodTypeDefault;

	private CallingMethodTypeXmlManager callingMethodTypeUserList;

	private MachineInformation machineInformation;
	
	public ShowServiceModeCore(Context contextApplication)
	{
        Log.i(this.getClass().getSimpleName(),"ShowServiceModeCore Init Start");

		this.contextApplication = contextApplication;

		// Default
		this.processingTypeDefault = new ProcessingTypeXmlManager();
		this.modelTypeDefault = new ModelTypeXmlManager();
		this.secretCodeTypeDefault = new SecretCodeTypeXmlManager();
		this.callingMethodTypeDefault = new CallingMethodTypeXmlManager();
		// UserList
		this.callingMethodTypeUserList = new CallingMethodTypeXmlManager();


		// for output test
//		this.processingTypeDefault.serializeSampleDataFile(this.contextApplication);
//		this.modelTypeDefault.serializeSampleDataFile(this.contextApplication);
//		this.secretCodeTypeDefault.serializeSampleDataFile(this.contextApplication);
//		this.callingMethodTypeDefault.serializeSampleDataFile(this.contextApplication);
		// for load test
//		this.processingTypeDefault.deserializeDefaultDataFile(this.contextApplication);
//		this.modelTypeDefault.deserializeDefaultDataFile(this.contextApplication);
//		this.secretCodeTypeDefault.deserializeDefaultDataFile(this.contextApplication);
//		this.callingMethodTypeDefault.deserializeDefaultDataFile(this.contextApplication);

		// for test
		//this.callingMethodTypeUserList.deserializeDefaultDataFile(this.contextApplication);
		//this.callingMethodTypeDefault.deepCopy(this.contextApplication, CallingMethodTypes.class, this.callingMethodTypeUserList.getItems());



		// load default res/raw
		this.processingTypeDefault.loadResRawXmlFileToObject(this.contextApplication, ProcessingTypes.class, R.raw.processing_types);
		this.modelTypeDefault.loadResRawXmlFileToObject(this.contextApplication, ModelTypes.class, R.raw.model_types);
		this.secretCodeTypeDefault.loadResRawXmlFileToObject(this.contextApplication, SecretCodeTypes.class, R.raw.secret_code_types);

		//
		this.machineInformation = new MachineInformation(this.contextApplication, this.modelTypeDefault);

		//
		this.registStartStatusEventListener();

		//
		long bootCount = this.machineInformation.getPreferencesAccessor().getBootCount();
		long setBootCount = (bootCount >= Long.MAX_VALUE) ? Value.LICENSE_GRACE_MAX_BOOT_COUNT + 1 : bootCount + 1;
		this.machineInformation.getPreferencesAccessor().setBootCount(setBootCount);
        Log.d(this.getClass().getSimpleName(),"BootCount: " + bootCount + " -> " + setBootCount);




		//
		this.machineInformation.checkStartStatusAndEventHandling();

		Log.i(this.getClass().getSimpleName(),"ShowServiceModeCore Init End");
	}

	public void registStartStatusEventListener()
	{
        Log.d(this.getClass().getSimpleName(),"ShowServiceModeCore registStartStatusEventListener");

		this.machineInformation.addOnFirstStartListener(new OnFirstStartListener()
        {
			@Override
			public void onFirstStart(StartStatusEventObject e)
			{
		        Log.d(this.getClass().getSimpleName(),"ShowServiceModeCore onFirstStart");
		        //
		        machineInformation.getPreferencesAccessor().executeFirstStart(machineInformation);

		        // モード判定
		        machineInformation.getPreferencesAccessor().setApplicationMode(judgeModeFromSignature());

				// 初回
		        loadCallingMethodTypeDefault();
				callingMethodTypeUserList.deepCopy(contextApplication, CallingMethodTypes.class, callingMethodTypeDefault.getItems());
				callingMethodTypeUserList.saveObjectToDataDataFilesXmlFile(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);
			}
		});

        this.machineInformation.addOnUpdateStartListener(new OnUpdateStartListener()
        {
			@Override
			public void onUpdateStart(StartStatusEventObject e)
			{
		        Log.d(this.getClass().getSimpleName(),"ShowServiceModeCore onUpdateStart");
		        //
		        machineInformation.getPreferencesAccessor().executeUpdateStart(machineInformation);

		        // モード判定
		        machineInformation.getPreferencesAccessor().setApplicationMode(judgeModeFromSignature());

		        //
		        loadCallingMethodTypeDefault();
		        callingMethodTypeUserList.loadDataDataFilesXmlFileToObject(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);

				for(CallingMethodType callingMethodType : callingMethodTypeUserList.getItems().items())
		        {
		        	if(callingMethodType.getTypeId() < Value.DEFAULT_PROCESSING_ID_START)
		        	{
		        		callingMethodTypeDefault.getItems().items().add(callingMethodType);
		        	}
		        }
				callingMethodTypeDefault.saveObjectToDataDataFilesXmlFile(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);

		        callingMethodTypeUserList.loadDataDataFilesXmlFileToObject(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);
			}
		});

        this.machineInformation.addOnDowngradeStartListener(new OnDowngradeStartListener()
        {
			@Override
			public void onDowngradeStart(StartStatusEventObject e)
			{
		        Log.d(this.getClass().getSimpleName(),"ShowServiceModeCore onDowngradeStart");

//		        machineInformation.getPreferencesAccessor().clear();
//
//		        // 初回と同じ動きとする。
//		        machineInformation.getPreferencesAccessor().executeFirstStart(machineInformation);
//
//		        loadCallingMethodTypeDefault();
//				callingMethodTypeUserList.deepCopy(contextApplication, CallingMethodTypes.class, callingMethodTypeDefault.getItems());
//				callingMethodTypeUserList.saveObjectToDataDataFilesXmlFile(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);

		        // モード判定
		        machineInformation.getPreferencesAccessor().setApplicationMode(judgeModeFromSignature());

		        // ノーマルと同じ動きとする。
		        //
		        machineInformation.getPreferencesAccessor().executeNormalStart(machineInformation);

		        //
		        callingMethodTypeUserList.loadDataDataFilesXmlFileToObject(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);
			}
		});

        this.machineInformation.addOnNormalStartListener(new OnNormalStartListener()
        {
			@Override
			public void onNormalStart(StartStatusEventObject e)
			{
		        Log.d(this.getClass().getSimpleName(),"ShowServiceModeCore onNormalStart");

		        //
		        machineInformation.getPreferencesAccessor().executeNormalStart(machineInformation);

		        //
		        callingMethodTypeUserList.loadDataDataFilesXmlFileToObject(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);
			}
		});

        this.machineInformation.addOnLanguageIsChangedListener(new OnLanguageIsChangedListener()
        {
			@Override
			public void onLanguageIsChanged(StartStatusEventObject e)
			{
		        Log.d(this.getClass().getSimpleName(),"ShowServiceModeCore onLanguageIsChanged");
		        // アップデートと同じ動きとする。
		        // MEMO:LastLanguageがこのケースで更新されること。
		        machineInformation.getPreferencesAccessor().executeUpdateStart(machineInformation);

		        // 異なる言語で生成された一覧を再生成する必要がある。
		        loadCallingMethodTypeDefault();
		        callingMethodTypeUserList.loadDataDataFilesXmlFileToObject(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);

				for(CallingMethodType callingMethodType : callingMethodTypeUserList.getItems().items())
		        {
		        	if(callingMethodType.getTypeId() < Value.DEFAULT_PROCESSING_ID_START)
		        	{
		        		callingMethodTypeDefault.getItems().items().add(callingMethodType);
		        	}
		        }
				callingMethodTypeDefault.saveObjectToDataDataFilesXmlFile(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);

		        callingMethodTypeUserList.loadDataDataFilesXmlFileToObject(contextApplication, CallingMethodTypes.class, CallingMethodTypeXmlManager.DEFAULT_FILE_NAME);
			}
		});

	}


	private void loadCallingMethodTypeDefault()
	{
		this.callingMethodTypeDefault.loadResRawXmlFileToObject(this.contextApplication, CallingMethodTypes.class, R.raw.calling_method_types);

        // デフォルトの補完処理
		for(CallingMethodType callingMethodType : this.callingMethodTypeDefault.getItems().items())
		{
        	ProcessingBase processing = this.processingTypeDefault.getItemFromName(callingMethodType.getTypeProcessingTypeName()).createProcessingInstance(callingMethodType);

        	String target;
        	target = callingMethodType.getTypeName();
			if(target.indexOf("%default%") >= 0)
			{
				target = target.replace("%default%", processing.makeDefaultName());
				callingMethodType.setTypeName(target);
			}

        	target = callingMethodType.getTypeCaption();
			if(target.indexOf("%default%") >= 0)
			{
				target = target.replace("%default%", processing.makeDefaultCaption());
				callingMethodType.setTypeCaption(target);
			}

        	target = callingMethodType.getTypeTitle();
			if(target.indexOf("%default%") >= 0)
			{
				target = target.replace("%default%", processing.makeDefaultTitle());
				callingMethodType.setTypeTitle(target);
			}

        	target = callingMethodType.getTypeDescription();
			if(target.indexOf("%default%") >= 0)
			{
				target = target.replace("%default%", processing.makeDefaultDescription());
				callingMethodType.setTypeDescription(target);
			}
		}
	}

	public String getDebugOutput()
	{
		return "SSM" + this.machineInformation.getVersionShowServiceMode().toString() + " " + this.machineInformation.getMode().toString()
			+ " :(" + this.machineInformation.getMachineModel() + "[" + this.machineInformation.getVersionOS() + "]" + this.machineInformation.getDisplay() + ")"
			+ " " + this.machineInformation.getLocaleLanguage().getLanguage()
			+ " :DT=" + this.machineInformation.getModelType().getTypeDefaultProcessingTypeName().toString()
			+ " :ST=" + this.machineInformation.getPreferencesAccessor().getCallingMethodTypeName().toString();
	}

	public String getDisplayVersion()
	{
		return this.getMachineInformation().getVersionShowServiceMode().toString()
        		+ " " + this.getMachineInformation().getMode().toString()
        		+ "";
	}


	public Mode judgeModeFromSignature()
	{
        Mode result = Mode.Free;
		if(this.machineInformation.getSignatureChecker().isReleaseSignature())
		{
			result = Mode.Release;
		}
		else if(this.machineInformation.getSignatureChecker().isDebugSignature())
		{
			result = Mode.Free;
		}
		else
		{
			result = Mode.BuildOwn;
		}
		Log.i(this.getClass().getSimpleName(),"judgeModeFromSignature: " + result.toString());
		return result;
	}




	public Context getApplicationContext()
	{
		return this.contextApplication;
	}


	public ProcessingTypeXmlManager getDefaultProcessingType()
	{
		return this.processingTypeDefault;
	}

	public ModelTypeXmlManager getDefaultModelType()
	{
		return this.modelTypeDefault;
	}

	public SecretCodeTypeXmlManager getDefaultSecretCodeType()
	{
		return this.secretCodeTypeDefault;
	}

	public CallingMethodTypeXmlManager getDefaultCallingMethodType()
	{
		return this.callingMethodTypeDefault;
	}


	public CallingMethodTypeXmlManager getUserListCallingMethodType()
	{
		return this.callingMethodTypeUserList;
	}


	public MachineInformation getMachineInformation()
	{
		return this.machineInformation;
	}



	public AssetManager getAssets()
	{
		return this.contextApplication.getResources().getAssets();
	}

}
