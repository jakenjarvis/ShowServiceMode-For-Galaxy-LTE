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
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodType;
import com.tojc.ShowServiceMode.XmlObject.Processing.ProcessingType;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ProcessingBroadcastSecretCode extends ProcessingBase implements Serializable
{
	private static final long serialVersionUID = 1421193171634801276L;

	public static final String PROCESSING_TYPE_NAME = "BROADCAST_SECRET_CODE";

	public static final String EXTRAPROPERTY_SECRETCODE = "secretcode";

	public ProcessingBroadcastSecretCode(ProcessingType processingtype, CallingMethodType callingmethodtype)
	{
		super(processingtype, callingmethodtype);
	}

	@Override
	protected Intent implementsCreateIntent(Context contextApplication)
	{
		String paramSecretCode = this.paramExtraProperty.get(EXTRAPROPERTY_SECRETCODE);

		Intent result = new Intent("android.provider.Telephony.SECRET_CODE", Uri.parse("android_secret_code://" + paramSecretCode));
		result.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return result;
	}

	@Override
	protected void implementsExecute(Context contextApplication, Intent intent)
	{
		contextApplication.sendBroadcast(intent);
	}

	@Override
	protected String implementsGetProcessingTypeName()
	{
		return PROCESSING_TYPE_NAME;
	}

	@Override
	protected String implementsGetSpecialValue()
	{
		return findValueFromSpecialKey();
	}


}
