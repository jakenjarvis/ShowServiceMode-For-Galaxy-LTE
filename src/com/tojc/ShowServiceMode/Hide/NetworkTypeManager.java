package com.tojc.ShowServiceMode.Hide;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkTypeManager extends PhoneStateListener
{
	public interface NetworkTypeChangedListener
	{
		public void onNetworkTypeChanged(NetworkTypeManager manager);
	}

	private Context contextApplication = null;

	private NetworkTypeChangedListener listener = null;

	private TelephonyManager telephonyManager = null;
	private int networkType = 0;
	private int networkClass = 0;

	public NetworkTypeManager(Context contextApplication)
	{
		this(contextApplication, null);
	}

	public NetworkTypeManager(Context contextApplication, NetworkTypeChangedListener listener)
	{
		this.contextApplication = contextApplication;

		this.listener = listener;

		this.telephonyManager = (TelephonyManager)this.contextApplication.getSystemService(Context.TELEPHONY_SERVICE);
		this.telephonyManager.listen(this, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

		getInfomation();
		fireOnNetworkTypeChanged();
	}

	@Override
	public void onDataConnectionStateChanged(int state, int networkType)
	{
		Log.d(this.getClass().getSimpleName(),"TelephonyManager.onDataConnectionStateChanged()");
		getInfomation();
		fireOnNetworkTypeChanged();
	}

	private void getInfomation()
	{
		this.networkType = this.telephonyManager.getNetworkType();
		this.networkClass = TelephonyManager.getNetworkClass(this.networkType);
	}

	public void listen(NetworkTypeChangedListener listener)
	{
		this.listener = listener;
	}

	private void fireOnNetworkTypeChanged()
	{
		if(this.listener != null)
		{
			this.listener.onNetworkTypeChanged(this);
		}
	}

	public int getNetworkType()
	{
		return this.networkType;
	}

	public String getNetworkTypeName()
	{
		return TelephonyManager.getNetworkTypeName(this.networkType);
	}

	public int getNetworkClass()
	{
		return this.networkClass;
	}

	public String getNetworkClassName()
	{
		String result = "";
		switch(this.networkClass)
		{
			case TelephonyManager.NETWORK_CLASS_2_G:
				result = "2G";
				break;

			case TelephonyManager.NETWORK_CLASS_3_G:
				result = "3G";
				break;

			case TelephonyManager.NETWORK_CLASS_4_G:
				result = "4G";
				break;

			default:	// NETWORK_CLASS_UNKNOWN
				result = "UNKNOWN";
				break;
		}
		return result;
	}

	@Override
	public String toString()
	{
		return "(" + getNetworkClassName() + "):" + getNetworkTypeName();
	}
}
