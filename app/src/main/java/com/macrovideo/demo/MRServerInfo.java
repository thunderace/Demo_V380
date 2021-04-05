package com.macrovideo.demo;

public class MRServerInfo {
	

	private int nID=-1;
	private int nServerID=0;//�豸ID
	private boolean bIsInit=false;
	private long lInitTime=0;
	private String strDomain=null;
	private String strIP=null;
 
	
	public int getnID() {
		return nID;
	}
	public void setnID(int nID) {
		this.nID = nID;
	}
	public int getnServerID() {
		return nServerID;
	}
	public void setnServerID(int nServerID) {
		this.nServerID = nServerID;
	}
	public boolean isbIsInit() {
		return bIsInit;
	}
	public void setbIsInit(boolean bIsInit) {
		this.bIsInit = bIsInit;
	}
	public long getlInitTime() {
		return lInitTime;
	}
	public void setlInitTime(long lInitTime) {
		this.lInitTime = lInitTime;
	}
	public String getStrDomain() {
		return strDomain;
	}
	public void setStrDomain(String strDomain) {
		this.strDomain = strDomain;
	}
	public String getStrIP() {
		return strIP;
	}
	public void setStrIP(String strIP) {
		this.strIP = strIP;
	}
	
	public MRServerInfo(){
		
	}
	
	  
	public MRServerInfo(int nID,int nServerID, boolean bIsInit, long lInitTime, String strDomain, String strIP){
		this.nID = nID;
		this.nServerID = nServerID;
		this.bIsInit = bIsInit;
		this.lInitTime=lInitTime;
		this.strDomain = strDomain;
		this.strIP = strIP;
	}
	 
}
