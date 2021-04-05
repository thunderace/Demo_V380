package com.macrovideo.demo;

import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

	private WifiManager mWiFiManager;
	private StringBuilder sb = new StringBuilder();
	private List<ScanResult> list2 ;
	private ScanResult mScanResult;
	private List<Object> list = new ArrayList<Object>();
	@Override
	public void onReceive(Context context, Intent intent) {
		
        sb = new StringBuilder();
        list2 = mWiFiManager.getScanResults();
        
        if(null != list2){
			
			for(int i=0; i<list2.size(); i++){
	
				 mScanResult =  list2.get(i); 
				sb = sb.append(mScanResult.SSID) ; 
				list.add(sb.toString());
				
			}
			Log.d("aaaaa",""+list.get(0) );
       }
		
	}

}
