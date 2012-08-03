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
package com.tojc.ShowServiceMode.XmlObject.SecretCode;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import com.tojc.ShowServiceMode.XmlObject.XmlManagerBase;
import com.tojc.ShowServiceMode.XmlObject.Model.ModelType;

import android.content.Context;
import android.util.Log;

public class SecretCodeTypeXmlManager extends XmlManagerBase<SecretCodeTypes, SecretCodeType>
{
	public static final String DEFAULT_FILE_NAME = "SecretCodeTypes.xml";

	public SecretCodeTypeXmlManager()
	{
		super(new SecretCodeTypes());
	}

	public SecretCodeTypeXmlManager(SecretCodeTypes items)
	{
		super(items);
	}

	@Override
	protected XmlManagerBase<SecretCodeTypes, SecretCodeType> createManagerInstance()
	{
		return new SecretCodeTypeXmlManager();
	}

	@Override
	protected XmlManagerBase<SecretCodeTypes, SecretCodeType> createManagerInstance(SecretCodeTypes items)
	{
		return new SecretCodeTypeXmlManager(items);
	}



	public String getTitleFromModelType(ModelType modelType, String secretcodekey)
	{
		String result = null;
		String modelname = modelType.getTypeName();

    	SecretCodeType secretCodeType = this.getItemFromName(secretcodekey);
    	if(secretCodeType != null)
    	{
    		if(secretCodeType.getTypeTitle().containsKey(modelname))
    		{
    			result = secretCodeType.getTypeTitle().get(modelname).toString();
    		}
    		else if(secretCodeType.getTypeTitle().containsKey("DEFAULT"))
    		{
    			result = secretCodeType.getTypeTitle().get("DEFAULT").toString();
    		}
    	}
		return result;
	}

	public String getDescriptionFromModelType(ModelType modelType, String secretcodekey)
	{
		String result = null;
		String modelname = modelType.getTypeName();

    	SecretCodeType secretCodeType = this.getItemFromName(secretcodekey);
    	if(secretCodeType != null)
    	{
    		if(secretCodeType.getTypeDescription().containsKey(modelname))
    		{
    			result = secretCodeType.getTypeDescription().get(modelname).toString();
    		}
    		else if(secretCodeType.getTypeDescription().containsKey("DEFAULT"))
    		{
    			result = secretCodeType.getTypeDescription().get("DEFAULT").toString();
    		}
    	}
		return result;
	}








	@Deprecated @Override
	public void serializeSampleDataFile(Context contextApplication)
	{
		HashMap<String, String> test1 = new HashMap<String, String>();
		test1.put("key1", "value1");
		test1.put("key2", "value2");
		this.getItems().items().add(new SecretCodeType(
				0,
				"NOTHING0",
				false,
				test1,
				test1
				));
		File xmlFile = new File(contextApplication.getFilesDir().getPath() + "/" + SecretCodeTypeXmlManager.DEFAULT_FILE_NAME);
		try
		{
			this.serialize(xmlFile);
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}

	@Deprecated @Override
	public void deserializeDefaultDataFile(Context contextApplication)
	{
		try
		{
    		InputStream stream = contextApplication.getAssets().open(SecretCodeTypeXmlManager.DEFAULT_FILE_NAME);
			this.deserialize(SecretCodeTypes.class, stream, true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		SecretCodeTypes cmt = this.getItems();
		for (SecretCodeType item : cmt.items())
		{
			Log.d(this.getClass().getSimpleName(),item.toString());
		}
	}

}
