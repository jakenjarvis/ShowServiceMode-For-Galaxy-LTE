package com.tojc.ShowServiceMode.Setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ActivityExistsChecker
{
	public static boolean check(Context context, String pkg, String cls)
	{
		boolean result = false;
		try
		{
			PackageManager manager = context.getPackageManager();
			@SuppressWarnings("unused")
			ActivityInfo info = manager.getActivityInfo(new ComponentName(pkg, cls), 0);
			result = true;
		}
		catch(NameNotFoundException e)
		{
			result = false;
		}
		return result;
	}

	public static String setDefaultProcessingTypeName(Context context, String typeDefaultProcessingTypeName)
	{
		String result = "";
		if(ActivityExistsChecker.check(context, "com.sec.android.app.servicemodeapp", "com.sec.android.app.servicemodeapp.ServiceModeApp"))
		{
			result = typeDefaultProcessingTypeName;
		}
		else
		{
			if(ActivityExistsChecker.check(context, "com.sec.android.RilServiceModeApp", "com.sec.android.RilServiceModeApp.ServiceModeApp"))
			{
				result = "DEF3011_DIRECTLY_CALL_ACTIVITY_SERVICEMODEAPP_2263";
			}
			else
			{
				result = "DEF4001_DIRECTLY_CALL_ACTIVITY_RADIOINFO";
			}
		}
		return result;
	}

}
