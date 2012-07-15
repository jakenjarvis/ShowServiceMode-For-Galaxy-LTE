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
package com.tojc.ShowServiceMode.ProcessingType;

import android.app.Activity;
import android.content.Intent;
import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;
import com.tojc.ShowServiceMode.Processing.ProcessingBase;

public class ProcessingNothing extends ProcessingBase
{
	public ProcessingNothing(Activity parent, ProcessingTypeId processingTypeId)
	{
		super(parent, 
				processingTypeId,
				parent.getString(R.string.nothing_title),
				parent.getString(R.string.nothing_subtitle),
				R.drawable.ic_shortcut
				);
	}

	@Override
	public boolean isListItem()
	{
		return false;
	}

	@Override
	protected Intent CreateIntent()
	{
		return null;
	}

	@Override
	protected void ExecuteShowServiceMode(Activity parent, Intent intent)
	{
	}

}
