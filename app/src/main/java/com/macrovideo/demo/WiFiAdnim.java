package com.macrovideo.demo;

import java.util.List;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WiFiAdnim {
	
	private WifiManager mWifiManager;  //����һ��WifiManager����
	private WifiInfo mWifiInfo;   //����һ��WifiInfo����
	private List<ScanResult> mWifiList;  //ɨ������������б�
	private List<WifiConfiguration> mWifiConfigurations;    //���������б�
	
	WifiLock mWifiLock;
	
	public WiFiAdnim(Context context)
	{
		//ȡ�õ�mWifiManager����
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		//ȡ��WifiInfo����
		mWifiInfo = mWifiManager.getConnectionInfo();
		
	}
	
	
	/**
	 * ��Wifi
	 */
	public void openWifi()
	{
		if(!mWifiManager.isWifiEnabled())
		{
			mWifiManager.setWifiEnabled(true);
			
		}
	}
	
	/**
	 * �ر�Wifi
	 */
	public void closeWifi()
	{
		if(mWifiManager.isWifiEnabled())
		{
			mWifiManager.setWifiEnabled(false);
		}
	}
	
	/**
	 * ��鵱ǰWifi״̬
	 */
	public int checkState()
	{
		return mWifiManager.getWifiState();
	}

	/**
	 * ��Wifi״̬
	 */
	public void acquireWifiLock()
	{
			mWifiLock.acquire();
	}
	
	/**
	 * ����WifiLock
	 */
	public void releaseWifiLock()
	{
		if(mWifiLock.isHeld())
		{ //�ж��Ƿ���
			mWifiLock.acquire();
		}
	}
	
	/**
	 * ����һ��WifiLock
	 */
	public void createWifiLock()
	{
		mWifiLock = mWifiManager.createWifiLock("test");
	}
	
	/**
	 * �õ����úõ�����
	 */
	public List<WifiConfiguration> getConfiguration()
	{
		return mWifiConfigurations;
	}
	
	/**
	 * ָ�����úõ������������
	 */
	public void connetionConfiguration(int index)
	{
		if(index > mWifiConfigurations.size())
		{
			return;
		}
		//�������úõ�ָ��ID����
		mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
	}
	
	/**
	 * ɨ������
	 */
	public void startScan(){
		//����ɨ��
		mWifiManager.startScan();
		//�õ�ɨ����
		mWifiList = mWifiManager.getScanResults();
		//�õ����úõ���������
		mWifiConfigurations = mWifiManager.getConfiguredNetworks();
		
		
	}
	
	/**
	 * �õ������б�
	 */
	public List<ScanResult> getWifiList()
	{
		return mWifiList;
	}
	
	/**
	 * �鿴ɨ����
	 */
	public StringBuilder lookUpScan()
	{
		StringBuilder sb = new StringBuilder();
			for(int i=0; i<mWifiList.size(); i++)
			{
				sb.append("Index_"+ new Integer(i+1).toString()+":");
				//��ScanResult��Ϣת��Ϊһ���ַ��
				//���а���:BSSID SSID capabilities frequency level
				sb.append((mWifiList.get(i)).toString()).append("\n");
			}
			return sb;
	}
	
	//���MAC��ַ
	public String getMacAddress()
	{  
		   return (mWifiInfo == null)?"NULL":mWifiInfo.getMacAddress();  
	} 
	
	//��ý�����BSSID
	public String getBSSID()
	{  
		   return (mWifiInfo == null)?"NULL":mWifiInfo.getBSSID();  
	}  
	
	//�õ�IP��ַ
	public int getIpAddress()
	{  
		   return (mWifiInfo == null)?0:mWifiInfo.getIpAddress();  
	}
	
	/**
	 * �õ�����ID
	 */
	public int getNetWordId()
	{
		return (mWifiInfo == null)?0:mWifiInfo.getNetworkId();
	}
	
	/**
	 * ȡ������mWifiInfo����Ϣ
	 */
	public String getWifiInfo()
	{
		return(mWifiInfo == null)?"NULL":mWifiInfo.toString();
	}
	
	/**
	 * ���һ�����粢����
	 */
	public void addNetWork(WifiConfiguration configuration)
	{
		int wcgId = mWifiManager.addNetwork(configuration);
//		Log.d("dqx", "wcgId = " + wcgId);
		mWifiManager.enableNetwork(wcgId, true);
//		Log.d("dqx", "enableNetwork ok");
	}
	
	/**
	 * �Ͽ�ָ��ID����
	 */
	public void disConnectionWifi(int netId)
	{
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}
	
	/**
	 * һ������wifi����
	 * 
	 * @param SSID wifi����
	 * @param Password wifi����
	 * @param Type ��������
	 * @return
	 */
	public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
			mWifiManager.saveConfiguration();  // add by dqx 2016��6��1��  ��������
		}

		if (Type == 1) // WIFICIPHER_NOPASS
		{
			// edit by dqx 2016��5��26�� ���������Ӳ�������
			// config.wepKeys[0] = "";
			config.priority = 200000;
			config.wepKeys[0] = "\"" + "\"";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
			// Log.d("dqx", "Type == 2");
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
			config.wepTxKeyIndex = 0;

			// -----
		}
		if (Type == 3) // WIFICIPHER_WPA
		{
//			 Log.d("dqx", "Type == 3");
			// config.preSharedKey = "\""+Password+"\"";
			// config.hiddenSSID = true;
			// config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			// config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			// config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			// config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			// config.status = WifiConfiguration.Status.ENABLED;

			// -----------
			config.priority = 200000;
			config.preSharedKey = "\"" + Password + "\"";
			config.status = WifiConfiguration.Status.ENABLED;
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			// ������ӣ���������·���޷�����
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			 

		}
		return config;
	}     
	      
	    private WifiConfiguration IsExsits(String SSID)    
	    {    
	        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();    
	           for (WifiConfiguration existingConfig : existingConfigs)     
	           {    
	             if (existingConfig.SSID.equals("\""+SSID+"\""))    
	             {    
	                 return existingConfig;    
	             }    
	           }    
	        return null;     
	    }   

}










