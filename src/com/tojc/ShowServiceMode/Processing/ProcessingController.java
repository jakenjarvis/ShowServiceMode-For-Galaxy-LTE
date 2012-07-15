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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.Enum.ModelType;
import com.tojc.ShowServiceMode.Enum.OsMajorVersion;
import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingBroadcast01;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingDial197328640_01;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingDial197328640_02;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingDial2263_01;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingDialFreeNumber;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingDirectlyCall197328640_01;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingDirectlyCall2263_01;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingNothing;
import com.tojc.ShowServiceMode.ProcessingType.ProcessingRadioInfo;
import com.tojc.ShowServiceMode.Setting.SignatureChecker;

public class ProcessingController extends ArrayList<ProcessingBase>
{
	private static final long serialVersionUID = 1L;

	private Activity parent;
	public String ShowServiceModeVersion;
	
	public final String model;
	public final String version;

	public final OsMajorVersion os;
	public final ModelType modeltype;

	private SignatureChecker signaturechecker;
	
	private ProcessingBase instance;

	public ProcessingController(Activity parent)
	{
		this.parent = parent;

		this.add(new ProcessingNothing(parent, ProcessingTypeId.NOTHING));
		this.add(new ProcessingDial2263_01(parent, ProcessingTypeId.DIAL2263_01));
		this.add(new ProcessingDial197328640_01(parent, ProcessingTypeId.DIAL197328640_01));
		this.add(new ProcessingDial197328640_02(parent, ProcessingTypeId.DIAL197328640_02));
		this.add(new ProcessingBroadcast01(parent, ProcessingTypeId.BROADCAST_01));
		this.add(new ProcessingDirectlyCall2263_01(parent, ProcessingTypeId.DIRECTLYCALL2263_01));
		this.add(new ProcessingDirectlyCall197328640_01(parent, ProcessingTypeId.DIRECTLYCALL197328640_01));
		this.add(new ProcessingRadioInfo(parent, ProcessingTypeId.RADIOINFO));
		this.add(new ProcessingDialFreeNumber(parent, ProcessingTypeId.DIALFREENUMBER));
		//
		
		this.trimToSize();

		String edition = "";
        this.signaturechecker = new SignatureChecker(parent);
        if(this.isDebugSignature())
        {
        	edition = "Free";
        }

        try
        {
        	this.ShowServiceModeVersion = parent.getPackageManager().getPackageInfo(parent.getPackageName(), 0).versionName + " " + edition;
        }
        catch (NameNotFoundException e)
        {
        	this.ShowServiceModeVersion = "";
        }

        this.model = android.os.Build.MODEL;
        this.version = android.os.Build.VERSION.RELEASE;

        Log.d(this.getClass().getSimpleName(),"MODEL: " + this.model);
        Log.d(this.getClass().getSimpleName(),"VERSION.RELEASE: " + this.version);

        this.modeltype = ModelType.toModelType(this.model);
        Log.d(this.getClass().getSimpleName(),"ModelType: " + this.modeltype);

        this.os = OsMajorVersion.toOsMajorVersion(this.version);
        Log.d(this.getClass().getSimpleName(),"OsMajorVersion: " + this.os);

        setInstanceById(getDefaultProcessingTypeId());
	}

	public boolean isDebugSignature()
	{
		return this.signaturechecker.isSignature(this.parent.getString(R.string.debug_signature));
	}

	public ProcessingBase getInstance()
	{
		return this.instance;
	}

	public void setInstanceById(ProcessingTypeId id)
	{
		this.instance = this.getInstanceById(id);
	}

	public ProcessingBase getInstanceById(ProcessingTypeId id)
	{
		ProcessingBase result = null;
		for (ProcessingBase pb : this)
		{
			if(pb.getProcessingTypeId() == id)
			{
				result = pb;
				break;
			}
		}
		return result;
	}

	public ProcessingTypeId getProcessingTypeId()
	{
		return this.instance.getProcessingTypeId();
	}

	public ProcessingTypeId getDefaultProcessingTypeId()
	{
		ProcessingTypeId result = ProcessingTypeId.NOTHING;

		switch(this.modeltype)
		{
			case SC03D:
				switch(this.os)
				{
					case Android2:
						//Confirmed
						result = ProcessingTypeId.DIRECTLYCALL2263_01;
						break;
					case Android4:
						//Unconfirmed
						result = ProcessingTypeId.DIRECTLYCALL197328640_01;
						break;
				}
				break;

			case SC05D:
				switch(this.os)
				{
					case Android2:
						//Confirmed
						result = ProcessingTypeId.DIRECTLYCALL2263_01;
						break;
					case Android4:
						//Unconfirmed
						result = ProcessingTypeId.DIRECTLYCALL197328640_01;
						break;
				}
				break;

			case SC06D:
				switch(this.os)
				{
					case Android4:
						//Confirmed
						result = ProcessingTypeId.DIRECTLYCALL197328640_01;
						break;
				}
				break;

			case SC01D:
				switch(this.os)
				{
					case Android3:
						//Unconfirmed
						result = ProcessingTypeId.DIRECTLYCALL2263_01;
						break;
					case Android4:
						//Unconfirmed
						result = ProcessingTypeId.DIRECTLYCALL197328640_01;
						break;
				}
				break;
		}
		return result;
	}

	public List<HashMap<String, Object>> CreateArrayListHashMap(boolean itemonly)
	{
		//TODO: Sort and ClassObject
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        Map<String, Object> map;

        for (ProcessingBase pb : this)
        {
        	if(!((!(itemonly & pb.isListItem())) & itemonly))
        	{
				// itemonly    isListItem      ans
				//          0          0           1
				//          0          1           1
				//          1          0           0
				//          1          1           1
        		map = new HashMap<String, Object>();
                map.put("id", pb.getProcessingTypeId());
                map.put("name", pb.getName());
                map.put("title", pb.getTitle());
                map.put("subtitle", pb.getSubTitle());
                result.add((HashMap<String, Object>) map);
        	}
        }
        return result;
	}

}
