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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.tojc.ShowServiceMode.Utility.Value;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodParameterType;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodType;
import com.tojc.ShowServiceMode.XmlObject.Processing.ProcessingType;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class ProcessingBase implements Serializable
{
	private static final long serialVersionUID = 7926929612894985195L;

	protected ProcessingType processingType;
	protected CallingMethodType callingMethodType;

	protected Map<String, String> paramProperty;
	protected Map<String, String> paramExtraProperty;

	protected Map<String, String> paramList;

	public ProcessingBase(ProcessingType processingtype, CallingMethodType callingmethodtype)
	{
		this.processingType = processingtype;
		this.callingMethodType = callingmethodtype;

		this.paramProperty = null;
		this.paramExtraProperty = null;
		this.paramList = new HashMap<String, String>();

		if(this.callingMethodType.getTypeParameter() != null)
		{
			this.paramProperty = this.callingMethodType.getTypeParameter().getTypeProperty();
			this.paramExtraProperty = this.callingMethodType.getTypeParameter().getTypeExtraProperty();

			if(this.paramProperty != null)
			{
				Set<Entry<String,String>> keySet = this.paramProperty.entrySet();
				for(Iterator<Entry<String,String>> it = keySet.iterator(); it.hasNext();)
				{
					Entry<String,String> entry = it.next();
					this.paramList.put(entry.getKey(), entry.getValue());
				}
			}

			if(this.paramExtraProperty != null)
			{
				Set<Entry<String,String>> keySet = this.paramExtraProperty.entrySet();
				for(Iterator<Entry<String,String>> it = keySet.iterator(); it.hasNext();)
				{
					Entry<String,String> entry = it.next();
					this.paramList.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	public boolean Execute(Context contextApplication)
	{
		boolean result = false;
        Log.d(this.getClass().getSimpleName(),"Execute Start:" + this.toString());
        try
		{
			Intent intent = implementsCreateIntent(contextApplication);
			implementsExecute(contextApplication, intent);
			result = true;
		}
		catch(Exception e)
		{
			Log.e(this.getClass().getSimpleName(),"Execute Failed:" + this.toString(), e);
			e.printStackTrace();
		}
        Log.d(this.getClass().getSimpleName(),"Execute End:" + this.toString());
        return result;
	}


	public int getId()
	{
		return this.callingMethodType.getTypeId();
	}

	public String getName()
	{
		return this.callingMethodType.getTypeName();
	}

	public String getCaption()
	{
		return this.callingMethodType.getTypeCaption();
	}

	public String getTitle()
	{
		return this.callingMethodType.getTypeTitle();
	}
	
	public String getDescription()
	{
		return this.callingMethodType.getTypeDescription();
	}

	public String getSpecialKey()
	{
		String result = null;
		CallingMethodParameterType paramtype = this.callingMethodType.getTypeParameter();
		if(paramtype != null)
		{
			result = paramtype.getTypeSpecialKey();
		}
		return result;
	}

	protected abstract Intent implementsCreateIntent(Context contextApplication);
	protected abstract void implementsExecute(Context contextApplication, Intent intent);

	protected String makeNamePrefix()
	{
		String result = "";
		int id = this.getId();
		if(id >= Value.DEFAULT_PROCESSING_ID_START)
		{
			result = String.format("DEF%1$04d", id);
		}
		else
		{
			result = String.format("USR%1$04d", id);
		}
		return result;
	}


	public String makeDefaultName()
	{
		String result;

		//String specialValue = findValueFromSpecialKey();
		String specialValue = implementsGetSpecialValue();
		if(specialValue != null)
		{
			result = makeNamePrefix() + "_" + implementsGetProcessingTypeName() + "_" + specialValue;
		}
		else
		{
			String specialKey = getSpecialKey();
			if(specialKey != null)
			{
				result = makeNamePrefix() + "_" + implementsGetProcessingTypeName() + "_" + specialKey;
			}
			else
			{
				result = makeNamePrefix() + "_" + implementsGetProcessingTypeName();
			}
		}
		return result.toUpperCase();
	}

	public String makeDefaultCaption()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.processingType.getTypeTitle());

		String specialValue = findValueFromSpecialKey();
		if(specialValue != null)
		{
			result.append(" (");
			result.append(specialValue);
			result.append(")");
		}
		return result.toString().trim();
	}

	public String makeDefaultTitle()
	{
		// TODO:未実装、無くてもいい気がする。
		return this.callingMethodType.getTypeTitle();
	}

	public String makeDefaultDescription()
	{
		String result = this.processingType.getTypeDefaultDescription();

		Set<Entry<String,String>> keySet = this.paramList.entrySet();
		for(Iterator<Entry<String,String>> it = keySet.iterator(); it.hasNext();)
		{
			Entry<String,String> entry = it.next();
			result = result.replace("%" + entry.getKey() + "%", entry.getValue());
		}
		return result.trim();
	}

	public String findValueFromSpecialKey()
	{
		String result = null;
		String spkey = getSpecialKey();
		if(spkey != null)
		{
			result = this.paramList.get(spkey);
		}
		return result;
	}

	public String findSecretCodeValueFromSpecialKey()
	{
		return implementsGetSpecialValue();
	}

	protected abstract String implementsGetProcessingTypeName();
	protected abstract String implementsGetSpecialValue();

	@Override
	public String toString()
	{
		return "Processing-Instance: " + this.callingMethodType.toString();
	}
}
