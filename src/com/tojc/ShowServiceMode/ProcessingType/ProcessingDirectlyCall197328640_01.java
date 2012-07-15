package com.tojc.ShowServiceMode.ProcessingType;

import android.app.Activity;

import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;
import com.tojc.ShowServiceMode.Processing.ProcessingDirectlyCallBase;

public class ProcessingDirectlyCall197328640_01 extends ProcessingDirectlyCallBase
{
	public ProcessingDirectlyCall197328640_01(Activity parent, ProcessingTypeId processingTypeId)
	{
		super(parent, 
				processingTypeId,
				parent.getString(R.string.directlycall197328640_01_title),
				parent.getString(R.string.directlycall197328640_01_subtitle),
				R.drawable.ic_shortcut
				);
		this.keyString = "197328640";
	}

}
