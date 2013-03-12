package com.tojc.ShowServiceMode.Hide;

import android.content.Context;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkTypeManager extends PhoneStateListener
{
	public interface NetworkTypeChangedListener
	{
		public void onNetworkTypeChanged(NetworkTypeManager manager);
	}

	private WapperTelephonyManager telephonyManager = null;
	private NetworkTypeChangedListener listener = null;

	private int networkType = 0;
	private NetworkClass networkClass = NetworkClass.NETWORK_CLASS_UNKNOWN;

	public NetworkTypeManager(Context contextApplication)
	{
		this(contextApplication, null);
	}

	public NetworkTypeManager(Context context, NetworkTypeChangedListener listener)
	{
		this.telephonyManager = new WapperTelephonyManager(context);
		this.telephonyManager.listen(this, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

		this.listener = listener;

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
		this.networkClass = this.telephonyManager.getNetworkClass(this.networkType);
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
		return this.telephonyManager.getNetworkTypeName();
	}

	public int getNetworkClass()
	{
		return this.networkClass.getCode();
	}

	public String getNetworkClassName()
	{
		return this.networkClass.toString();
	}

	@Override
	public String toString()
	{
		return "(" + getNetworkClassName() + "):" + getNetworkTypeName();
	}

	public enum NetworkClass
	{
		NETWORK_CLASS_UNKNOWN(0, "UNKNOWN"),
		NETWORK_CLASS_2_G(1, "2G"),
		NETWORK_CLASS_3_G(2, "3G"),
		NETWORK_CLASS_4_G(3, "4G");
		
		private NetworkClass(int code, String name)
		{
			this.code = code;
			this.name = name;
		}

	    private final int code;
	    private final String name;

	    public int getCode()
	    {
	    	return this.code;
	    }

	    public String getName()
	    {
	        return this.name;
	    }

	    @Override
	    public String toString()
	    {
	        return this.name;
	    }

	    public static NetworkClass toNetworkClass(int code)
	    {
	    	NetworkClass result = NETWORK_CLASS_UNKNOWN;
	        for (NetworkClass item : values())
	        {
	            if (item.getCode() == code)
	            {
	                result = item;
	                break;
	            }
	        }
	        return result;
	    }
	}

	public class WapperTelephonyManager
	{
		private Context context = null;
		private TelephonyManager telephonyManager = null;

		public WapperTelephonyManager(Context context)
		{
			this.context = context;
			this.telephonyManager = (TelephonyManager)this.context.getSystemService(Context.TELEPHONY_SERVICE);
		}

		public Context getContext()
		{
			return this.context;
		}

		public void listen(PhoneStateListener listener, int events)
		{
			try
			{
				this.telephonyManager.listen(listener, events);
			}
			catch(Exception e)
			{
				Log.e(this.getClass().getSimpleName(),"telephonyManager.listen Failed", e);
				e.printStackTrace();
			}
		}

		public int getNetworkType()
		{
			int result = 0;
			try
			{
				result = this.telephonyManager.getNetworkType();
			}
			catch(Exception e)
			{
				Log.e(this.getClass().getSimpleName(),"telephonyManager.getNetworkType Failed", e);
				e.printStackTrace();
			}
			return result;
		}

		public String getNetworkTypeName()
		{
			String result = "";
			try
			{
				result = this.telephonyManager.getNetworkTypeName();
			}
			catch(Exception e)
			{
				Log.e(this.getClass().getSimpleName(),"TelephonyManager.getNetworkTypeName Failed", e);
				e.printStackTrace();
			}
			return result;
		}

		public NetworkClass getNetworkClass(int networkType)
		{
			NetworkClass result = NetworkClass.NETWORK_CLASS_UNKNOWN;
			try
			{
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				{
					int networkClass = TelephonyManager.getNetworkClass(networkType);
					result = NetworkClass.toNetworkClass(networkClass);
				}
				else
				{
			        switch (networkType)
			        {
			            case TelephonyManager.NETWORK_TYPE_GPRS:
			            case TelephonyManager.NETWORK_TYPE_EDGE:
			            case TelephonyManager.NETWORK_TYPE_CDMA:
			            case TelephonyManager.NETWORK_TYPE_1xRTT:
			            case TelephonyManager.NETWORK_TYPE_IDEN:
			            	result = NetworkClass.NETWORK_CLASS_2_G;
			            	break;

			            case TelephonyManager.NETWORK_TYPE_UMTS:
			            case TelephonyManager.NETWORK_TYPE_EVDO_0:
			            case TelephonyManager.NETWORK_TYPE_EVDO_A:
			            case TelephonyManager.NETWORK_TYPE_HSDPA:
			            case TelephonyManager.NETWORK_TYPE_HSUPA:
			            case TelephonyManager.NETWORK_TYPE_HSPA:
			            case TelephonyManager.NETWORK_TYPE_EVDO_B:
			            case TelephonyManager.NETWORK_TYPE_EHRPD:
			            case 15: //TelephonyManager.NETWORK_TYPE_HSPAP:
			            	result = NetworkClass.NETWORK_CLASS_3_G;
			            	break;

			            case TelephonyManager.NETWORK_TYPE_LTE:
			            	result = NetworkClass.NETWORK_CLASS_4_G;
			            	break;

			            default:
			            	result = NetworkClass.NETWORK_CLASS_UNKNOWN;
			            	break;
			        }
				}
			}
			catch(Exception e)
			{
				Log.e(this.getClass().getSimpleName(),"TelephonyManager.getNetworkClass Failed", e);
				e.printStackTrace();
			}
			return result;
		}
	}
}
