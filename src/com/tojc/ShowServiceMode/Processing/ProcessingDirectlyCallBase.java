package com.tojc.ShowServiceMode.Processing;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.tojc.ShowServiceMode.Enum.ProcessingTypeId;

public class ProcessingDirectlyCallBase extends ProcessingBase
{
	protected String keyString;

	public ProcessingDirectlyCallBase(Activity parent, ProcessingTypeId processingTypeId, String title, String subtitle, int shortcutid)
	{
		super(parent, processingTypeId, title, subtitle, shortcutid);
	}

	@Override
	public boolean isListItem()
	{
		return true;
	}

	@Override
	protected Intent CreateIntent()
	{
		Intent result = new Intent(Intent.ACTION_MAIN);
		ComponentName localComponentName = new ComponentName("com.sec.android.app.servicemodeapp", "com.sec.android.app.servicemodeapp.ServiceModeApp");
		result.setComponent(localComponentName);
		result.putExtra("keyString", this.keyString);
		return result;
	}

	@Override
	protected void ExecuteShowServiceMode(Activity parent, Intent intent)
	{
		parent.startActivity(intent);
	}

}
