package com.tojc.ShowServiceMode.Hide;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class MobileDataManager
{
	private Context contextApplication = null;
	private ConnectivityManager connectivityManager = null;

	public MobileDataManager(Context contextApplication)
	{
		this.contextApplication = contextApplication;
		this.connectivityManager = (ConnectivityManager)this.contextApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public void setMobileDataEnabled(boolean enabled)
	{
		try
		{
			this.connectivityManager.setMobileDataEnabled(enabled);
		}
		catch(Exception e)
		{
			Log.e(this.getClass().getSimpleName(),"setMobileDataEnabled Failed", e);
			e.printStackTrace();
		}
	}

	public boolean getMobileDataEnabled()
	{
		boolean result = false;
		try
		{
			result = this.connectivityManager.getMobileDataEnabled();
		}
		catch(Exception e)
		{
			Log.e(this.getClass().getSimpleName(),"getMobileDataEnabled Failed", e);
			e.printStackTrace();
		}
		return result;
	}
}
