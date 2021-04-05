package com.macrovideo.demo;

import android.graphics.Bitmap;

public class PTZXPoint {
	private int nID=-1; 
	private int nDevID=0;
	private int nPTZXID=0;
	private long lSaveTime=0;
	private Bitmap faceImage=null;
	
	public PTZXPoint(int nID, int nDevID, int nPTZXID, long lSaveTime, Bitmap faceImage){
		this.nID = nID;
		this.nDevID = nDevID;
		this.nPTZXID = nPTZXID;
		this.lSaveTime = lSaveTime;
		this.faceImage = faceImage;
	}
	public int getnID() {
		return nID;
	}
	public void setnID(int nID) {
		this.nID = nID;
	}
	
	public int getnDevID() {
		return nDevID;
	}
	public void setnDevID(int nDevID) {
		this.nDevID = nDevID;
	}
	public int getnPTZXID() {
		return nPTZXID;
	}
	public void setnPTZXID(int nPTZXID) {
		this.nPTZXID = nPTZXID;
	}
 	 
	public long getlSaveTime() {
		return lSaveTime;
	}
	public void setlSaveTime(long lSaveTime) {
		this.lSaveTime = lSaveTime;
	}
	public Bitmap getFaceImage() {
		return faceImage;
	}
	public void setFaceImage(Bitmap faceImage) {
		this.faceImage = faceImage;
	}
	

}
