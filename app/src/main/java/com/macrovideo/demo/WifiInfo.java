package com.macrovideo.demo;

public class WifiInfo implements Comparable<Object>{
	private String bssid=null;
	private int frequency=0;
	private int signalLevel=0;
	private int flags=0;
	private String ssid=null;
	private String protectDesc=null;//
	
	

	public WifiInfo(String bssid, int frequency, int signalLevel, int flags, String ssid, String protectDesc){
		 this.bssid = bssid;
		 this.ssid = ssid;
		 this.frequency = frequency;
		 this.signalLevel = signalLevel;
		 this.flags = flags;
		 this.protectDesc = protectDesc;
	}

	public String getProtectDesc() {
		return protectDesc;
	}
	
	public String getBssid() {
		return bssid;
	}
	public int getFrequency() {
		return frequency;
	}
	public double getSignalLevel() {
		return signalLevel;
	}
	public int getFlags() {
		return flags;
	}
	public String getSsid() {
		return ssid;
	}

	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		WifiInfo info = (WifiInfo) another;
		if(this.signalLevel==info.signalLevel){
			if(this.flags==info.flags){
				if(this.ssid.compareToIgnoreCase(info.getSsid())>0){
					return -1;
				}else{
					return 1;
				}
				
			}else if(this.flags>info.getFlags()){
				return -1;
			}else{
				return 1;
			}
			
		}else if(this.signalLevel>info.getSignalLevel()){
			return -1;
		}else{
			return 1;
		}
		
	}
}
