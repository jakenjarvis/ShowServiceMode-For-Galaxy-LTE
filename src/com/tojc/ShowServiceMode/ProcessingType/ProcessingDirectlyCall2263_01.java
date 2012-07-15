package com.tojc.ShowServiceMode.ProcessingType;

import android.app.Activity;

import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;
import com.tojc.ShowServiceMode.Processing.ProcessingDirectlyCallBase;

public class ProcessingDirectlyCall2263_01 extends ProcessingDirectlyCallBase
{
	public ProcessingDirectlyCall2263_01(Activity parent, ProcessingTypeId processingTypeId)
	{
		super(parent, 
				processingTypeId,
				parent.getString(R.string.directlycall2263_01_title),
				parent.getString(R.string.directlycall2263_01_subtitle),
				R.drawable.ic_shortcut
				);
		this.keyString = "2263";
	}
}
