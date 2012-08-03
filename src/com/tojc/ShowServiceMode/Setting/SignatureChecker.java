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
package com.tojc.ShowServiceMode.Setting;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.tojc.ShowServiceMode.Utility.Value;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Log;

public class SignatureChecker
{
	private Context contextApplication;
	private PackageManager pm;

    private boolean debugSignature = false;
	private boolean releaseSignature = false;

	public SignatureChecker(Context contextApplication)
	{
		this.contextApplication = contextApplication;
		this.pm = this.contextApplication.getPackageManager();

        this.debugSignature = this.isSignature(Value.DB01) || this.isSignature(Value.DB02);
        this.releaseSignature = this.isSignature(Value.DB03);
		Log.d(this.getClass().getSimpleName(), "isSignature D:" + this.debugSignature + ", R:" + this.releaseSignature);
	}

	public boolean isSignature(String target)
	{
		boolean result = false;
		try
		{
			PackageInfo packageInfo = pm.getPackageInfo(
					this.contextApplication.getPackageName(),
					PackageManager.GET_SIGNATURES);
			for (int i = 0; i < packageInfo.signatures.length; i++)
			{
				Signature signature = packageInfo.signatures[i];
				//Log.d(this.getClass().getSimpleName(), "Signature:" + signature.toCharsString());
				String value = SignatureChecker.digest(signature.toCharsString()).toUpperCase();
				//Log.d(this.getClass().getSimpleName(), "Value:" + value);
				if(value.equals(target))
				{
					result = true;
					break;
				}
			}
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public boolean isDebugSignature()
	{
		return this.debugSignature;
	}
	public boolean isReleaseSignature()
	{
		return this.releaseSignature;
	}

	public static String digest(String message)
	{
		String result = "";
		MessageDigest md;
		byte[] data;
		try
		{
			md = MessageDigest.getInstance("SHA-256");
			md.update(message.getBytes("UTF-8"));
			data = md.digest();
			result = String.format("%0" + (data.length * 2) + 'x', new BigInteger(1, data));
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return result;
	}

}
