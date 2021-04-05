package com.macrovideo.demo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.macrovideo.sdk.defines.Defines;
import com.macrovideo.sdk.media.LoginHandle;
import com.macrovideo.sdk.objects.DeviceInfo;
import com.macrovideo.sdk.objects.ObjectAlarmMessage;
import com.macrovideo.sdk.objects.PTZXPoint;
import com.macrovideo.sdk.objects.RecordVideoInfo;
import com.macrovideo.sdk.setting.AlarmAndPromptInfo;
import com.macrovideo.sdk.setting.RecordInfo;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class LocalDefines {
//	public static final String H264_CONFIG = "H264_HW_CONFIG";
//	public static final String H265_CONFIG = "H265_HW_CONFIG";
	public static final String HW_DECODE_STATUS = "HW_DECODE_STATUS";
	
	public static String ossKeyId = "LTAIWs2WDx2YTwvJ";
    public static String ossKeySecret = "CK9aIGZokIyd8l1abggSALPJTEratA";
    public static String ossBucketName = "aierror";
	public static String APP_OSS_PWD_V380="MacrovideoOSS";
	public static String ossEndpoint = "http://oss-cn-shenzhen.aliyuncs.com";
	
	public static final int WECHAT_PAYMENT_VER = 2;
//	public static final int WECHAT_PAYMENT_VER = 2;
	public static final String CLOUD_STORE_URL = "http://www.av380.net/site/login?access_token=";
//	public static final String CLOUD_STORE_URL = "http://114.215.27.232/site/login?access_token=";
	
//	public static final String CLOUD_STORE_URL_FOR_TEST = "http://114.215.27.232/site/login?access_token=";
	
	public static final int BIND_DEVICE_RESULT_CODE = 800;
	
	public static final String VERSION_PREF = "version_pref";
	/**
	 * 是否第一次进入程序
	 */
	
	public static final int AD_APP_ID = 1001; // app广告对应的app_id
//	public static final String AD_IS_HAVE_PREFS = "AD_IS_HAVE_PREFS";
//	public static final String AD_PATH_PREFS = "AD_PATH_PREFS"; // Ad 存放的位置
	public static final String AD_ID = "AD_ID"; // Ad 对应的唯一标识
	public static final String AD_SHOW_DURATION= "AD_SHOW_DURATION"; // Ad 的持续时间
	public static final String AD_SHOW_FREQUENCY= "AD_SHOW_FREQUENCY"; // Ad 的播放次数
	public static final String AD_SHOW_ALREADY= "AD_SHOW_ALREADY"; // Ad 已经播放的次数
	public static final String AD_MS_ID= "AD_MS_ID"; // MS对应的唯一标识
	public static final String AD_GET_TIME = "AD_GET_TIME";
	public static String MSG_TITLE = "MSG_TITLE";
	public static String MSG = "MSG";
	public static boolean HAS_NEW_MSG = false;
	
	public static boolean isFirstIn = false;
	
	public static final String APP_VERSION = "app_version";
	public static final String IS_FIRST_IN = "isFirstIn"; //add 2015/12/24

	public static int areaID = 0; // add by mai 20151218 锟斤拷诺锟角帮拷锟斤拷锟斤拷id
	public static LoginHandle Device_LoginHandle = null;  //add by mai 20151215
	
	//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟绞癸拷锟�
	public static boolean B_ALTER_PASSWORD = false;  //add by mai 2015-5-14 锟叫讹拷锟角凤拷锟斤拷锟睫革拷锟斤拷锟斤拷锟斤拷锟斤拷
	public static boolean B_TESTING_PASSWORD = false; // add by mai 2015-5-14 锟叫讹拷锟角凤拷锟斤拷锟斤拷证锟斤拷锟斤拷锟斤拷锟�
	public static boolean B_OFF_PASSWORD = false; // add by mai 2015-5-14 锟叫讹拷锟角凤拷锟斤拷锟斤拷要锟截憋拷锟斤拷锟斤拷锟斤拷锟斤拷
	public static boolean B_INTENT_ACTIVITY = false; //add by mai 2015-5-15 锟斤拷师锟角凤拷锟斤拷转锟斤拷锟斤拷锟絘ctivity
	public static boolean B_DELETE_PASSWORD = false;  // add by mai 2015-6-5 锟斤拷锟轿撅拷锟斤拷锟斤拷锟� 
	public static boolean B_CREATE_PASSWORD = false; //add by mai 2015-6-5 锟叫讹拷锟角凤拷锟角达拷锟斤拷锟斤拷锟斤拷
	public static boolean SOFTWARE_PASSWORD_OPEN = false; //add by mai 2015-6-5 锟叫断碉拷前锟角凤拷锟斤拷锟斤拷锟斤拷
	public static String SOFTWARE_PASSWORD_STATE = "software_password_state"; // add by mai 2015-6-5 
	//========
	
	
	//===锟斤拷锟斤拷锟斤拷示
	public static int nClientDeviceSettingThreadID=0;
	public static int nClientRegistThreadID=0;
	public static boolean isVibrate=false;
	public static boolean isSound=false;
	public static String strSysLan = null;  //add by mai 2015-5-27 锟斤拷取锟街伙拷前锟斤拷锟斤拷锟斤拷
	public static String SOUND_SHAKE_STORE_NAME = "ShoundShake";  // add by mai 2015-4-3 SharedPreferences 锟芥储锟斤拷锟斤拷
	public static String DEVICE_MESSAGE_RECEIVE = "alarmMessageReceive"; // add by mai 2015-6-8 锟芥储锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷息
	public static String ALARM_SOUND = "alarm_sound";  // add by mai 2015-4-3 锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	public static String ALARM_SHAKE = "alarm_shake";  //add by mai 2015-4-3 锟斤拷取锟金动碉拷锟斤拷锟斤拷锟斤拷锟斤拷
	public static String RECV_ALARM_MSG = "recv_msg"; // add by mai 2015-5-27 锟斤拷取锟角凤拷锟斤拷毡锟斤拷锟斤拷锟较拷锟斤拷锟斤拷锟斤拷锟�
	public static String ALARM_DEVICE_LOGIN = "alarmDeviceLogin"; // add by mai 2015-5-27 锟斤拷取锟斤拷一锟斤拷注锟斤拷锟角凤拷晒锟�
	
	public static String ALARM_SETTINGS = "alarm_settings_sharedpreference"; // add by mai 2015-5-27 锟斤拷取锟斤拷一锟斤拷注锟斤拷锟角凤拷晒锟�
	public static boolean isRecvMsg = true;  // add by mai 2015-5-26 锟斤拷锟斤拷全锟街匡拷锟斤拷锟斤拷锟斤拷只锟斤拷欠锟斤拷锟秸憋拷锟斤拷锟斤拷息
	
	public static String SDCardPath = "demo";

	public static String DownAPPName = "demo.apk";
	public static String DownAPPDir = "demo_download";
	public static Map<String, Object> loginHandleMap = new HashMap<String, Object>();

	public static void loadAlarmSettings(Activity context){
		if(context==null)return;
		SharedPreferences alarmSettingSharedPreferences=context.getSharedPreferences(ALARM_SETTINGS, Activity.MODE_PRIVATE);;
		if(alarmSettingSharedPreferences!=null){
			isRecvMsg = alarmSettingSharedPreferences.getBoolean(RECV_ALARM_MSG, true);
			isVibrate=alarmSettingSharedPreferences.getBoolean(ALARM_SHAKE, false);
			isSound=alarmSettingSharedPreferences.getBoolean(ALARM_SOUND, true);
		}

		strSysLan = context.getString(R.string.country_code);
	}
	
	public static void saveAlarmSettings(Activity context){
		if(context==null)return;
		SharedPreferences alarmSettingSharedPreferences=context.getSharedPreferences(ALARM_SETTINGS, Activity.MODE_PRIVATE);;
		if(alarmSettingSharedPreferences!=null){
			Editor editor = alarmSettingSharedPreferences.edit();
			if(editor!=null){
				editor.putBoolean(RECV_ALARM_MSG, isRecvMsg);
				editor.putBoolean(ALARM_SHAKE, isVibrate);
				editor.putBoolean(ALARM_SOUND, isSound);
				editor.commit();
			}

		}
	}


	public static void setDeviceInfoToSP(Activity context, DeviceInfo deviceInfo) {
		String deviceInfoJson;
		if (deviceInfo == null) {
			return;
		} else {
			deviceInfoJson = JSON.toJSONString(deviceInfo);
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences("sdk_demo_deviceInfo", context.MODE_PRIVATE);
		if (sharedPreferences != null) {
			Editor editor = sharedPreferences.edit();
			if (editor != null) {
				editor.putString("deviceInfoJson", deviceInfoJson);
				editor.apply();
			}
		}
	}

	public static DeviceInfo getDeviceInfoFromSP(Activity context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("sdk_demo_deviceInfo", context.MODE_PRIVATE);
		if (sharedPreferences != null) {
			String deviceInfoJson = sharedPreferences.getString("deviceInfoJson",null );
			if (TextUtils.isEmpty(deviceInfoJson)) {
				return null;
			} else {
				return JSON.parseObject(deviceInfoJson, DeviceInfo.class);
			}
		}
		return null;
	}

	//********
	
	
	//add by luo 20150805
	public static boolean _isQRResultOK=false;
	public static int _nQRResultDevice=0;
	//end add by luo 20150805
	
	public static DeviceInfo DEVICE_MSG = null;
	public static ArrayList<DeviceInfo> _severInfoListData=new ArrayList<DeviceInfo>();
    public static int _PlaybackListviewSelectedPosition =0;//add by luo 20141010
    public static int _PlaybackRecListviewFirstPosition =0;//add by luo 20141010
    public static DeviceInfo configDeviceInfo = null; // add by mai 2015-6-15
    public static boolean bIsConfig = false; // add by mai 2015-6-15
	public static boolean bIsBackPlay = false; // add by mai 2015-10-16
	
	
	//锟斤拷锟斤拷歉锟斤拷锟斤拷锟�
	static final int PAGE_INDEX_SERVERLIST=10;
	static final int PAGE_INDEX_ADDDEVICE=11;
	static final int PAGE_INDEX_MODIFYDEVICE=12;
	static final int PAGE_INDEX_CONFIG=13;
	static final int PAGE_INDEX_CONFIG_NETWORK=14;
	static final int PAGE_INDEX_CONFIG_RECORD=15;
	static final int PAGE_INDEX_CONFIG_DATETIME=16;
	static final int PAGE_INDEX_CONFIG_ACOUNT=17;
	static final int PAGE_INDEX_CONFIG_VERSION=18;
	static final int PAGE_INDEX_CONFIG_ALARMANDPROMPT=19;//add by luo 20141011
	static final int PAGE_INDEX_CONFIG_STATIC_IP=20;//add 2016锟斤拷4锟斤拷13锟斤拷
	//=====
	
	//锟借备锟斤拷锟斤拷臃锟绞�
	public static int SERVER_SAVE_TYPE_ADD = 1011;//
	public static int SERVER_SAVE_TYPE_SEARCH=1012;//
	public static int SERVER_SAVE_TYPE_DEMO=1013;//
	//=======
	
	
	//锟借备锟斤拷锟斤拷状态
	public static final int ON_LINE_STAT_UNKNOW = -1;//锟斤拷锟节硷拷锟�
	public static final int ON_LINE_STAT_READY = 0;//锟斤拷锟斤拷锟斤拷锟斤拷
	public static final int ON_LINE_STAT_OFF = 100;//锟斤拷锟斤拷
	public static final int ON_LINE_STAT_LAN = 101;//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	public static final int ON_LINE_STAT_WAN = 102;//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	//======
	
	//锟斤拷锟斤拷锟斤拷锟斤拷式
    static final int SERVER_TYPE_DOMAIN = 100;//锟斤拷锟斤拷
    static final int SERVER_TYPE_IP = 101;//ip锟斤拷址
    
    //锟斤拷锟斤拷锟斤拷锟斤拷
    public static final int NV_LANGUAGE_CN=1000; //锟斤拷锟斤拷
    public static final int NV_LANGUAGE_EN=1100;//英锟斤拷
    
    public static final int EXALARMMODE_EXTERNAL=10;	//锟解部锟斤拷锟斤拷系统(expand detector)
    public static final int EXALARMMODE_INTERNAL=11;	//锟节诧拷锟斤拷锟斤拷系统(current detector)
    public static final int EXALARMMODE_MANUALOFF=12;	//锟街讹拷锟斤拷锟狡关憋拷锟斤拷锟�(manually control)
    public static final int EXALARMMODE_MANUALON=13;	//锟街讹拷锟斤拷锟狡匡拷锟斤拷锟斤拷锟�(manually control)
    public static AlarmAndPromptInfo _AlarmAndPromptConfig= new AlarmAndPromptInfo();
	public static final int ALARM_MODEL_315 = 315; //锟斤拷锟斤拷锟借备锟斤拷315
	public static final int ALARM_MODEL_433 = 433; //锟斤拷锟斤拷锟借备锟斤拷433
	public static final int NV_IPC_REMOTE_GET_REQUEST = 880;  //远锟教伙拷取
	public static final int NV_IPC_REMOTE_SET_REQUEST = 881;  //远锟斤拷锟斤拷锟斤拷
	public static DeviceInfo alarmRelecanceDeviceInfo = null;
	
	
	public static final int notificationAlarmID = 0x000102;
	public static final int notificationID = 0x000101;
	
	
	//锟斤拷锟斤拷锟借备锟斤拷锟斤拷
	public static final int DEVICE_OTHER = 0; //锟斤拷锟斤拷
	public static final int DEVICE_ACCESS_CTRL = 1; //锟脚斤拷
	public static final int DEVICE_SMOKE_FEELING = 2; //锟教革拷
	public static final int DEVICE_GAS = 3; //煤锟斤拷
	public static final int DEVICE_WARM_FEELING = 4; //锟铰讹拷
		
	//锟斤拷锟斤拷锟斤拷锟斤拷
	public static final int ALARM_LEVEL_L = 1; // 锟斤拷
	public static final int ALARM_LEVEL_M = 2; //锟斤拷
	public static final int ALARM_LEVEL_H = 3; //锟斤拷
	
	
	//add by mai 2015-8-14
	public static final int NV_IP_ALARM_DEVICE_LIST_REQUEST = 130;  //锟斤拷取锟斤拷锟斤拷拥谋锟斤拷锟斤拷璞钢革拷锟斤拷锟�
	public static final int NV_IP_ALARM_DEVICE_LIST_RESPONSE = 230; // 锟斤拷取锟斤拷锟斤拷拥谋锟斤拷锟斤拷璞革拷锟斤拷锟街革拷锟斤拷锟�
	public static final int NV_IP_ALARM_DEVICE_LIST_REQUEST_EX = 330;  //锟斤拷取锟斤拷锟斤拷拥谋锟斤拷锟斤拷璞钢革拷锟斤拷锟�
	public static final int NV_IP_ALARM_DEVICE_LIST_RESPONSE_EX = 430; // 锟斤拷取锟斤拷锟斤拷拥谋锟斤拷锟斤拷璞革拷锟斤拷锟街革拷锟斤拷锟�
	public static final int NV_IP_ALARM_DEVICE_SEARCH_REQUEST = 131; // 锟斤拷锟街憋拷锟斤拷锟借备(锟借备锟斤拷锟斤拷)指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_SEARCH_RESPONSE  = 231; // 锟斤拷锟街憋拷锟斤拷锟借备(锟借备锟斤拷锟斤拷)锟斤拷锟斤拷指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_SEARCH_REQUEST_EX = 331; 
	public static final int NV_IP_ALARM_DEVICE_SEARCH_RESPONSE_EX = 431; 
	public static final int NV_IP_ALARM_DEVICE_ADD_REQUEST = 132; //锟斤拷颖锟斤拷锟斤拷璞钢革拷锟斤拷锟�
	public static final int NV_IP_ALARM_DEVICE_ADD_RESPONSE  = 232; // 锟斤拷颖锟斤拷锟斤拷璞革拷锟斤拷锟街革拷锟斤拷锟�
	public static final int NV_IP_ALARM_DEVICE_ADD_REQUEST_EX = 332; //锟斤拷颖锟斤拷锟斤拷璞钢革拷锟斤拷锟�
	public static final int NV_IP_ALARM_DEVICE_ADD_RESPONSE_EX  = 432; // 锟斤拷颖锟斤拷锟斤拷璞革拷锟斤拷锟街革拷锟斤拷锟�
	public static final int NV_IP_ALARM_DEVICE_MODIFY_REQUEST = 133; //锟睫改憋拷锟斤拷锟借备指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_MODIFY_RESPONSE = 233; //锟睫改憋拷锟斤拷锟借备锟斤拷锟斤拷指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_MODIFY_REQUEST_EX = 333; //锟睫改憋拷锟斤拷锟借备指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_MODIFY_RESPONSE_EX = 433; //锟睫改憋拷锟斤拷锟借备锟斤拷锟斤拷指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_DELETE_REQUEST = 134; //删锟斤拷锟借备指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_DELETE_RESPONSE = 234; // 删锟斤拷锟借备锟斤拷锟斤拷指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_DELETE_REQUEST_EX = 334; //删锟斤拷锟借备指锟斤拷锟斤拷
	public static final int NV_IP_ALARM_DEVICE_DELETE_RESPONSE_EX = 434; // 删锟斤拷锟借备锟斤拷锟斤拷指锟斤拷锟斤拷
	public static final int ALARM_DEVICE_SEARCH_ON  = 100; // 锟斤拷始锟斤拷锟斤拷
	public static final int ALARM_DEVICE_SEARCH_OFF = 101; //锟截闭硷拷锟斤拷
	
	public static final int NV_RESULT_SUCCEED = 1001; //锟斤拷锟斤拷锟斤拷锟轿拷晒锟�
	public static final int NV_RESULT_FAILED = 2001; //锟斤拷锟斤拷锟斤拷锟轿э拷锟�
	
	

	public static ArrayList<DeviceInfo> _severInfoWithoutImageListData=new ArrayList<DeviceInfo>();
	public static boolean isClientRegisted=false;
	public static boolean isDeviceListSet=false;
	public static boolean isDeviceBinded=false;
	
	public static ArrayList<Integer> LocalAlarmAreaList = new ArrayList<Integer>();
	public static ArrayList<Integer> ServerAlarmAreaList = new ArrayList<Integer>();

	public static int ServerAlarmSwitch1 = 0; // 自定义时间段开关 1 为打开
	public static int ServerAlarmSwitch2 = 0; // 自定义时间段开关 1 为打开
	public static int ServerAlarmSwitch3 = 0; // 自定义时间段开关 1 为打开
	public static ArrayList<Integer> ServerStartHour = new ArrayList<Integer>();
	public static ArrayList<Integer> ServerStartMin = new ArrayList<Integer>();
	public static ArrayList<Integer> ServerStartSec = new ArrayList<Integer>();
	public static ArrayList<Integer> ServerEndHour = new ArrayList<Integer>();
	public static ArrayList<Integer> ServerEndMin = new ArrayList<Integer>();
	public static ArrayList<Integer> ServerEndSec = new ArrayList<Integer>();
//	public static ArrayList<Integer> ServerAlarmSwitch = new ArrayList<Integer>();

	public static ArrayList<String> ServerStartHour_Str = new ArrayList<String>();
	public static ArrayList<String> ServerStartMin_Str = new ArrayList<String>();
	public static ArrayList<String> ServerStartSec_Str = new ArrayList<String>();

	public static ArrayList<String> ServerStartTime_Str = new ArrayList<String>();
	public static ArrayList<String> ServerEndTime_Str = new ArrayList<String>();

	public static ArrayList<String> ServerEndHour_Str = new ArrayList<String>();
	public static ArrayList<String> ServerEndMin_Str = new ArrayList<String>();
	public static ArrayList<String> ServerEndSec_Str = new ArrayList<String>();

	public static Map<Integer, ArrayList<Integer>> Localmap_Select_area = new HashMap<Integer, ArrayList<Integer>>();
	public static Map<Integer, Boolean> Localmap_Update_area = new HashMap<Integer, Boolean>();
	public static boolean shouldUpdateSelectArea = false;
	public static int alarmrows = 4;
	public static int alarmcolumns = 8;
	public static int effectiveTime1, effectiveTime2, effectiveTime3 = 0; // 记录时分不为0的个数，为0则不在界面中显示该时间
    public static int CanSetTime = 1;
    
    public static final ArrayList<LoginHandle> _LoginHandleListData=new ArrayList<LoginHandle>();
	
	public static void addAlarmTime(int starthour1, int starthour2,
			int starthour3, int startmin1, int startmin2, int startmin3,
			int startsec1, int startsec2, int startsec3, int endhour1,
			int endhour2, int endhour3, int endmin1, int endmin2, int endmin3,
			int endsec1, int endsec2, int endsec3) {
		
			if (starthour1 != 0 || startmin1 != 0 || endhour1 != 0
					|| endmin1 != 0) {
				effectiveTime1 = 1;
			}else {
				effectiveTime1 = 0;
			}
			if (starthour2 != 0 || startmin2 != 0 || endhour2 != 0
					|| endmin2 != 0) {
				effectiveTime2 = 1;
			}else {
				effectiveTime2 = 0;
			}
			if (starthour3 != 0 || startmin3 != 0 || endhour3 != 0
					|| endmin3 != 0) {
				effectiveTime3 = 1;
			}else {
				effectiveTime3 = 0;
			}
		
		Log.i("TAG", "effectiveTime = " + effectiveTime1 + " " + effectiveTime2 + " " + effectiveTime3 + " ");
		// ServerAlarmSwitch.add(0, alarmswitch1);
		// ServerAlarmSwitch.add(1, alarmswitch2);
		// ServerAlarmSwitch.add(3, alarmswitch3);

		ServerStartHour.add(0, starthour1);
		ServerStartHour.add(1, starthour2);
		ServerStartHour.add(2, starthour3);
		ServerStartMin.add(0, startmin1);
		ServerStartMin.add(1, startmin2);
		ServerStartMin.add(2, startmin3);
		ServerStartSec.add(0, startsec1);
		ServerStartSec.add(1, startsec2);
		ServerStartSec.add(2, startsec3);

		ServerEndHour.add(0, endhour1);
		ServerEndHour.add(1, endhour2);
		ServerEndHour.add(2, endhour3);
		ServerEndMin.add(0, endmin1);
		ServerEndMin.add(1, endmin2);
		ServerEndMin.add(2, endmin3);
		ServerEndSec.add(0, endsec1);
		ServerEndSec.add(1, endsec2);
		ServerEndSec.add(2, endsec3);

		String str_starthour1, str_starthour2, str_starthour3, str_startmin1, str_startmin2, str_startmin3, str_startsec1, str_startsec2, str_startsec3;

		String str_endhour1, str_endhour2, str_endhour3, str_endmin1, str_endmin2, str_endmin3, str_endsec1, str_endsec2, str_endsec3;

		if (starthour1 < 10) {
			str_starthour1 = "0" + starthour1;
		} else {
			str_starthour1 = "" + starthour1;
		}
		if (startmin1 < 10) {
			str_startmin1 = "0" + startmin1;
		} else {
			str_startmin1 = "" + startmin1;
		}
		if (startsec1 < 10) {
			str_startsec1 = "0" + startsec1;
		} else {
			str_startsec1 = "" + startsec1;
		}
		if (endhour1 < 10) {
			str_endhour1 = "0" + endhour1;
		} else {
			str_endhour1 = "" + endhour1;
		}
		if (endmin1 < 10) {
			str_endmin1 = "0" + endmin1;
		} else {
			str_endmin1 = "" + endmin1;
		}
		if (endsec1 < 10) {
			str_endsec1 = "0" + endsec1;
		} else {
			str_endsec1 = "" + endsec1;
		}

		if (starthour2 < 10) {
			str_starthour2 = "0" + starthour2;
		} else {
			str_starthour2 = "" + starthour2;
		}
		if (startmin2 < 10) {
			str_startmin2 = "0" + startmin2;
		} else {
			str_startmin2 = "" + startmin2;
		}
		if (startsec2 < 10) {
			str_startsec2 = "0" + startsec2;
		} else {
			str_startsec2 = "" + startsec2;
		}
		if (endhour2 < 10) {
			str_endhour2 = "0" + endhour2;
		} else {
			str_endhour2 = "" + endhour2;
		}
		if (endmin2 < 10) {
			str_endmin2 = "0" + endmin2;
		} else {
			str_endmin2 = "" + endmin2;
		}
		if (endsec2 < 10) {
			str_endsec2 = "0" + endsec2;
		} else {
			str_endsec2 = "" + endsec2;
		}

		if (starthour3 < 10) {
			str_starthour3 = "0" + starthour3;
		} else {
			str_starthour3 = "" + starthour3;
		}
		if (startmin3 < 10) {
			str_startmin3 = "0" + startmin3;
		} else {
			str_startmin3 = "" + startmin3;
		}
		if (startsec3 < 10) {
			str_startsec3 = "0" + startsec3;
		} else {
			str_startsec3 = "" + startsec3;
		}
		if (endhour3 < 10) {
			str_endhour3 = "0" + endhour3;
		} else {
			str_endhour3 = "" + endhour3;
		}
		if (endmin3 < 10) {
			str_endmin3 = "0" + endmin3;
		} else {
			str_endmin3 = "" + endmin3;
		}
		if (endsec3 < 10) {
			str_endsec3 = "0" + endsec3;
		} else {
			str_endsec3 = "" + endsec3;
		}

		ServerStartHour_Str.add(0, str_starthour1);
		ServerStartHour_Str.add(1, str_starthour2);
		ServerStartHour_Str.add(2, str_starthour3);
		ServerStartMin_Str.add(0, str_startmin1);
		ServerStartMin_Str.add(1, str_startmin2);
		ServerStartMin_Str.add(2, str_startmin3);
		ServerStartSec_Str.add(0, str_startsec1);
		ServerStartSec_Str.add(1, str_startsec2);
		ServerStartSec_Str.add(2, str_startsec3);

		ServerEndHour_Str.add(0, str_endhour1);
		ServerEndHour_Str.add(1, str_endhour2);
		ServerEndHour_Str.add(2, str_endhour3);
		ServerEndMin_Str.add(0, str_endmin1);
		ServerEndMin_Str.add(1, str_endmin2);
		ServerEndMin_Str.add(2, str_endmin3);
		ServerEndSec_Str.add(0, str_endsec1);
		ServerEndSec_Str.add(1, str_endsec2);
		ServerEndSec_Str.add(2, str_endsec3);

		ServerStartTime_Str.add(0, "" + str_starthour1 + ":" + str_startmin1);
		ServerStartTime_Str.add(1, "" + str_starthour2 + ":" + str_startmin2);
		ServerStartTime_Str.add(2, "" + str_starthour3 + ":" + str_startmin3);

		ServerStartTime_Str.add(0, "" + str_starthour1 + ":" + str_startmin1);
		ServerStartTime_Str.add(1, "" + str_starthour2 + ":" + str_startmin2);
		ServerStartTime_Str.add(2, "" + str_starthour3 + ":" + str_startmin3);

		ServerEndTime_Str.add(0, "" + str_endhour1 + ":" + str_endmin1);
		ServerEndTime_Str.add(1, "" + str_endhour2 + ":" + str_endmin2);
		ServerEndTime_Str.add(2, "" + str_endhour3 + ":" + str_endmin3);

		ServerEndTime_Str.add(0, "" + str_endhour1 + ":" + str_endmin1);
		ServerEndTime_Str.add(1, "" + str_endhour2 + ":" + str_endmin2);
		ServerEndTime_Str.add(2, "" + str_endhour3 + ":" + str_endmin3);
	}
	
//	public static int reloadDeviceInfoList(){
//		
//		_severInfoListData.clear();
//		_severInfoWithoutImageListData.clear();
//
//		DeviceInfo[] array = DatabaseManager.GetAllServerInfo();
//		    
//		if (array != null) {
//			for (int i = 0; i < array.length; i++) {
//				if(array[i]!=null){
//					_severInfoListData.add(array[i]);  
//					
//					_severInfoWithoutImageListData.add(array[i].copyDeviceInfoWithoutImage());
//				}
//				
//			}
//
//		}
//		Functions.mDeviceListChangedCallback.onDeviceListChanged(Functions.getDeviceListType(_severInfoListData));
//		return _severInfoListData.size();
//	}
	
	public static final int DOWNREQUEST=100;
    static final int STOPDOWN=101;
    static final int UPLOADQUEST=102;
    static final int STOPUPLOAD=103;
	public static final int FILESTART=110;
	public static final int FILEDATAEND=111;
	public static final int FILEDATA=112;
	public static final int FILEERROR=113;
	public static final int USERVERIFY=167;
	public static final int USERVERIFYRESULT=168;
	public static final int USERVERIFYRESULTMR=169;//通锟斤拷转锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫伙拷锟斤拷录校锟斤拷
	public static final int TESTCONNECTION=222;
	public static final int DISCONNECT=253;
	public static final int CUSTOMBUF=254;
	
	static String _DemoMRServer="rs31.nvdvr.cn";
	public static int _DemoMRPort=8800;
	static final String MV_DOMAIN_SUFFIX=".nvdvr.net";
			//add by luo 2013-02-18
	static String _MRServer="127.0.0.1";
	public static int _MRPort=8800;
	public static int _OnLinePort=8900; //add by mai 2015-6-19
	static boolean _bUseMRServer=true;
	public static int _MRServerIndex=0;
	
	public static final int CMD_MR_LOGIN=1001;//准锟斤拷通讯指锟斤拷
	public static final int CMD_MR_PLAY=1002;//锟斤拷取一帧图锟斤拷指锟斤拷
	public static final int CMD_MR_GET_IMAGE=1003;//锟斤拷取一帧图锟斤拷指锟斤拷
	public static final int CMD_MR_LOGIN_RESPONSE=2001;//锟斤拷录确锟斤拷指锟斤拷
	public static final int CMD_MR_PLAY_RESPONSE=2002;//锟斤拷锟斤拷图锟斤拷确锟斤拷指锟斤拷
	public static final int OS_PHONE_TYPE = Defines.PHONE_TYPE_ANDROID;// 锟街伙拷锟斤拷锟斤拷
	
	
	
	
	//模式锟斤拷锟藉：
	public static final int NV_WIFI_MODE_UNKNOW = 0000; //AP模式
	public static final int NV_WIFI_MODE_AP = 1001; //AP模式
	public static final int NV_WIFI_MODE_STATION = 1002; // STATION 模式
	public static final int NV_WIFI_MODE_ALL = 1003; // 锟斤拷应锟缴版本锟斤拷系统
	
	
	public static boolean B_UPDATE_LISTVIEW = true; // add by mai 2015-3-27 锟叫憋拷锟角凤拷锟斤拷要锟斤拷锟斤拷刷锟斤拷
	public static final int LIST_MODE_NORMAL = 200;//锟斤拷
	public static final int LIST_MODE_EDIT = 201;//删锟斤拷锟铰�
	public static final int LIST_MODE_SEARCHING = 202;//锟斤拷锟斤拷锟借备
	public static final int LIST_MODE_CONFIG = 203;//锟斤拷锟斤拷锟借备
	public static final int LIST_MODE_DEMONSTRATE = 204;//锟斤拷示
	
	public static int _nListMode=LIST_MODE_NORMAL;
	
	
	
	 //锟斤拷锟斤拷锟斤拷锟斤拷
    public static final int ALARM_MSG_TYPE_UNDIFINED	=111;	// 锟斤拷锟斤拷
    public static final int ALARM_MSG_TYPE_SMOG			=100;	//锟斤拷锟�?锟斤拷
    public static final int ALARM_MSG_TYPE_MOTION		=200;	//锟狡讹拷锟斤拷锟斤拷
    public static final int ALARM_MSG_TYPE_PIR			=300;	//PIR 锟斤拷锟斤拷锟接︼拷锟斤拷锟�
    public static final int ALARM_MSG_TYPE_ACCESS_CTRL	=400;	//锟脚斤拷
    public static final int ALARM_MSG_TYPE_GAS			=500;	//煤锟斤拷
    public static final int ALARM_MSG_TYPE_WARM			=600;	//锟铰度憋拷锟斤拷

    public static final int CRITICAL =300;		//锟斤拷锟截告警
    public static final int MAJOR =400;		//锟截达拷婢�
    public static final int MINOR =500;		//锟斤拷要锟芥警
    public static final int NOTICE =600;		//锟斤拷示锟斤拷息
	
	 //锟斤拷锟斤拷锟斤拷锟斤拷
	private static  SoundPool soudPool=null;
	private static int sourceid_alarm_notice=0;
	private static int sourceid_alarm_warm=0;
	private static long lPlayTime = 0;
	private static Vibrator vibrator=null;  //add by mai 2015-4-3 锟斤拷锟斤拷系统锟斤拷
	@SuppressWarnings("deprecation")
	public static synchronized void initNoticeSound(Context context){
		if(soudPool==null){
			
			soudPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5); 
		}
	
		sourceid_alarm_notice = soudPool.load(context, R.raw.alarm_notice, 0); 
		sourceid_alarm_warm = soudPool.load(context, R.raw.alarm_warm, 0); 

			if(vibrator==null){
				vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);  // 锟斤拷锟斤拷锟斤拷
			}
			
	}
	public static synchronized void playNoticeSound(int nAlarmType){

		 if(System.currentTimeMillis() - lPlayTime<=1000) return;
		 
		if(isSound && soudPool!=null){
			
			if(nAlarmType==ALARM_MSG_TYPE_SMOG || 
		    		nAlarmType==ALARM_MSG_TYPE_PIR || 
		    		nAlarmType==ALARM_MSG_TYPE_ACCESS_CTRL ||
		    		nAlarmType==ALARM_MSG_TYPE_GAS ||
		    		nAlarmType==ALARM_MSG_TYPE_WARM){
				soudPool.play(sourceid_alarm_warm, 1, 1, 0, 0, 1);
		    }else{
		    	soudPool.play(sourceid_alarm_notice, 1, 1, 0, 0, 1);
		    }
			
			lPlayTime = System.currentTimeMillis();
		}
		
		if(isVibrate && vibrator!=null)
		{  
//			long [] pattern = {500,1000,500,1000};   // 停止 锟斤拷锟斤拷 停止 锟斤拷锟斤拷   
	        vibrator.vibrate(pattern,-1);           //锟截革拷一锟斤拷锟斤拷锟斤拷锟絧attern只锟截革拷一锟斤拷锟斤拷锟轿�-1
	       // vibrator.cancel();
		}
		
	}
	
	static final long [] pattern = {500,1000,500,1000};   // 停止 锟斤拷锟斤拷 停止 锟斤拷锟斤拷
	public static final int HANDLE_SHOW_WINDOW_ADD=0x111;//锟斤拷息锟斤拷锟斤拷锟绞撅拷穑夯锟饺★拷锟斤拷锟斤拷锟斤拷媒锟斤拷
	public static boolean _isNeedGetPreviewDeviceList=true;
	public static ArrayList<DeviceInfo> _demoSeverInfoListData=new ArrayList<DeviceInfo>();
	
    public static String PUSH_RECEIVER = "com.macrovideo.update.push"; //add by mai 2015-6-4 锟姐播锟斤拷识
    public static String PUSH_TITLE = "pushTitle"; // add by mai 2015-6-4 锟斤拷锟斤拷锟斤拷息状态锟斤拷示锟斤拷头
    public static String PUSH_TYPE = "pushType"; // add by mai 2015-6-4 锟斤拷锟斤拷锟斤拷锟酵癸拷锟斤拷锟斤拷锟斤拷息锟斤拷锟斤拷锟斤拷
    public static String PUSH_MSG = "pushMsg"; //add by mai 2015-6-4 锟斤拷锟斤拷锟斤拷锟酵癸拷锟斤拷锟斤拷息锟斤拷锟斤拷锟斤拷
    public static String PUSH_TIME = "pushTime"; //add by mai 2015-6-4 锟斤拷锟斤拷锟斤拷锟酵癸拷锟斤拷锟斤拷锟斤拷息锟斤拷时锟斤拷
    public static String PUSH_ID = "pushId"; //add by mai 2015-6-4 锟斤拷锟斤拷锟斤拷锟酵癸拷锟斤拷锟斤拷锟斤拷息锟斤拷id
    public static int _listviewFisrtPosition=0;//add by luo 20141010 锟叫憋拷锟斤拷锟斤拷锟轿伙拷锟�
//    public static ArrayList<ServerInfoForAlarm> _severInfoListDataForAlarm=new ArrayList<ServerInfoForAlarm>();
    
    
    public static int loadDeviceList(boolean isCheck){
		int nCount =0;
		if(isCheck){
			if(_severInfoListData.size()>0){
				return _severInfoListData.size();
			}
		}
		
		
		// add by luo 20141030
		if (_severInfoListData.size() > 0) {
			for (int i = _severInfoListData.size() - 1; i >= 0; i--) {
				DeviceInfo info = _severInfoListData.get(i);
				if (info != null) {
					info.releaseImage();
				}
			}

			System.gc();// add by luo 20141223
		}
		// end add by luo 20141030

//		reloadDeviceInfoList();
		
		return nCount;
	}
    
    
    
  //锟借备锟斤拷锟斤拷锟斤拷锟街�
  public static final int DEVICE_SEARCH_RESULT=1001;//add by luo 2013-04-15 锟借备锟斤拷锟斤拷锟斤拷锟�
  public static final int DEVICE_SEARCH_RESULT_OK=101;//add by luo 2013-04-15 锟借备锟斤拷锟斤拷锟斤拷锟�
  public static final int DEVICE_SEARCH_RESULT_FAIL=102;//add by luo 2013-04-15 锟借备锟斤拷锟斤拷锟斤拷锟�
  	
  public static final int DEVICE_IP_UPDATE=1002;//add by luo 2013-04-15 锟借备锟斤拷锟斤拷锟斤拷锟�
  		
  public static final int DEVICE_SOUND_WAVE_END=1003;//add by luo 20150407锟借备锟斤拷锟斤拷锟斤拷锟�
  public static final int NV_DEVICE_NET_STATE_ONLINE = 1200; // add by mai 2015-6-19 锟借备锟斤拷锟斤拷远锟斤拷刷锟铰成癸拷
  
  /**
   * 根据index获取服务器地址
   * @param index
   * @return
   */
	public static String getAlarmServerByIndex(int index) {
		String server = null;
		switch (index) {
		case 0:
			server = _strAlarmServerRecv;
			break;
		case 1:
			server = _strAlarmServerRecv1;
			break;
		case 2:
			server = _strAlarmServerRecv2;
			break;

		default:
			server = _strAlarmServerRecv;
			break;
		}
		return server;
	}
  
  public static int _nAlarmPort =8888;  //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟剿口猴拷
//  public static String _strAlarmServer = "alarm1.nvdvr.cn";  //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
  public static String _strAlarmServerSend = "alarm1.nvdvr.cn";  //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
  public static String _strAlarmServerRecv = "alarm1.nvdvr.cn";  //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
  public static String _strAlarmServerRecv1 = "alarmrec1.nvdvr.cn";
  public static String _strAlarmServerRecv2 = "alarmrec2.nvdvr.cn";
//  public static String _strAlarmServerRecv = "alarmrec2.nvdvr.cn";  //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
  public static String _strDefaultAlarmServerRecv = null;
  public static String _strDeviceDemoServer = "preview.nvdvr.cn";  //锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
  public static final int ALARM_SUCCESS = 0; //锟斤拷取锟斤拷锟斤拷锟斤拷息锟缴癸拷
  public static final int ALARM_FALLURE = 1; //锟斤拷取锟斤拷锟斤拷锟斤拷息失锟斤拷
  public static final String JSP_SERVER_CONNECT_MSG = "/GetAlarmMsg/AlarmSelectServletMsg?param=";  //锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
  public static final String JSP_SERVER_CONNECT_PICTURE = "/GetAlarmMsg/AlarmSelectServletPicture?param=";  //锟斤拷取锟斤拷锟斤拷锟斤拷细锟斤拷息锟斤拷锟斤拷
  public static final String JSP_SERVER_CONNECT_PICTURE_CONDITION = "/GetAlarmMsg/AlarmSelectServletPictureCondition?param=";  //锟斤拷取锟斤拷锟斤拷锟斤拷细锟斤拷息锟斤拷锟斤拷
  public static final String JSP_SERVER_CONNECT_PICTURE_LARGE = "/GetAlarmMsg/AlarmSelectServletPictureByID?param=";  //锟斤拷取锟斤拷锟斤拷锟斤拷细锟斤拷息锟斤拷锟斤拷
  public static final String JSP_SERVER_CONNECT_PICTUER_PREVIEW ="/GetAlarmMsg/PreviewSelectServletDeviceInfo?param="; //锟斤拷示锟斤拷
  public static final String JSP_SERVER_CONNECT_PICTUER_PREVIEW_DEFAULT ="/GetPreviewServer/PreviewSelectServletDeviceInfo?param="; //锟斤拷示锟斤拷
  // add 2016-12-23 
  public static final String JSP_SERVER_CONNECT_PICTUER_PATH ="/GetAlarmMsg/AlarmGetPictureByPath?param="; // 图片不在同一个服务器，获取大图
  public static final String JSP_SERVER_CONNECT_PICTUER_BY_ID ="/GetAlarmMsg/AlarmGetPictureByID?param="; // 获取大图
  public static final String JSP_SERVER_ALARM_GET_MESSAGE_LIST ="/GetAlarmMsg/AlarmGetMessageList?param="; // 获取列表
  public static final String JSP_SERVER_ALARM_GET_MORE_MESSAGE_LIST ="/GetAlarmMsg/AlarmGetMessageListWithCondition?param="; // 获取更多列表
  
  //获取最新的报警图片大图  add 2016.12.12
//  public static final String JSP_SERVER_CONNECT_PICTUER_TOP_NEW ="/GetAlarmMsg/AlarmSelectServletTopNewPicture?param=";
  public static String _strAlarmLargePosition = "as114.nvdvr.cn";
  
  public static final int NV_IPC_ONLINE_CHECK_REQUEST = 172; //add by mai 2015-6-18 锟斤拷锟竭硷拷锟斤拷锟斤拷锟街革拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
  public static final int NV_IPC_ONLINE_CHECK_RESPONSE = 272; // add by mai 2015-6-18 锟斤拷锟竭硷拷夥碉拷锟街革拷锟�
  public static final int NV_IPC_ONLINE_CHECK_ONLINE = 1; // add by mai 2015-6-18 锟斤拷示锟借备锟斤拷锟斤拷
  public static final int NV_IPC_ONLINE_CHECK_OFFLINE = 0; // add by mai 2015-6-18 锟斤拷识锟借备锟斤拷锟斤拷锟斤拷
  public static final int NV_IPC_ONLINE_CHECK_UNKNOWN = 10; // add by mai 2015-6-18 锟斤拷识锟借备锟斤拷锟斤拷锟斤拷
  
  public static final int HANDLE_MSG_CODE_WIFI_SEARCH_RESULT=0x20;//锟斤拷息锟斤拷锟斤拷锟绞撅拷穑旱锟铰硷拷锟斤拷
  public static final int WIFI_SEARCH_RESULT_CODE_SUCCESS=0x0011;//WIFI锟斤拷锟斤拷锟缴癸拷
  public static final int WIFI_SEARCH_RESULT_CODE_FAIL_NET_DOWN=0x1011;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷锟界不通
  public static final int WIFI_SEARCH_RESULT_CODE_FAIL_NET_POOL=0x1012;//WIFI锟斤拷锟斤拷锟缴癸拷-锟斤拷锟斤拷睿拷锟斤拷映锟绞�
	
  public static final int WIFI_SEARCH_RESULT_CODE_FAIL_SERVER_OFFLINE=0x1013;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
  public static final int WIFI_SEARCH_RESULT_CODE_FAIL_VERIFY_FAILED=0x1014;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷证锟斤拷锟斤拷
  public static final int WIFI_SEARCH_RESULT_CODE_FAIL_USER_NOEXIST=0x1015;//WIFI锟斤拷锟斤拷失锟斤拷-锟矫伙拷锟斤拷锟斤拷锟�
  public static final int WIFI_SEARCH_RESULT_CODE_FAIL_PWD_ERROR=0x1016;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷锟斤拷锟斤拷锟�
  public static final int WIFI_SEARCH_RESULT_CODE_FAIL_OLD_VERSON=0x1017;//WIFI锟斤拷锟斤拷失锟斤拷-前锟剿版本锟斤拷支锟斤拷
  
  
  public static RecordInfo _objRecordConfig= new RecordInfo();
//录锟斤拷状态
  public static final int  RECORD_STAT_UNKOWN =0;
  public static final int  RECORD_STAT_RUN_OK =10;
  public static final int  RECORD_STAT_NO_SDCARD =-11;
  public static final int  RECORD_STAT_SDCARD_WRITE_ERR =-12;
  
  public static String ANDROID_APP_NAME = "demo";   //锟斤拷锟斤拷锟斤拷锟�

  public static int APP_UPDATE_RESULT = 2221;
  public static int APP_UPDATE_RESULT_NO_NEW = 1;
  public static int APP_UPDATE_RESULT_HAS_NEW = 2;
  public static int _nCheckVersionNUm = 0;
  public static long _lCheckTime=0;
  public static int APP_ID_ICAMSEE=1011;
  public static int APP_ID_ICAMSEE2=1021;
  public static int APP_ID_V380=1120;
//  public static int APP_ID_V380=101120;
  
  public static String _strUpdate = "update.nvdvr.cn";  //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷  
  public static final String JSP_SERVER_SOFTWARE_UPDATE= "/AppManage/UpdateSelectServletAppVersionCheck?param=";  //锟芥本锟斤拷锟斤拷锟斤拷锟斤拷
  
  static final int RADIO_INDEX_HOME=100;
  static final int RADIO_INDEX_PIECTURE=101;
  static final int RADIO_INDEX_PLAYBACK=102;
  static final int RADIO_INDEX_HELP=103;
  
  public static boolean IsSoftwareOpen = false;
  public static boolean IsSoftwareRunning = false;
  public static boolean PLAY_BACK_BACK = false; 
  
  public static long _lHandle = -1;
  public static int _nDeviceID =-1;
  public static boolean _isMRMode=false;
  public static String _strMRServer=null;
  public static int _nMRPort=0;
  public static String _strPassword = null; // add by mai 2015-7-21
  public static String _strUserName = null; //add by mai 2015-7-21
	
  public static long _lHandleGetTime = 0;
	
  public static int _nSearchDevID=-1;
  public static String _strSearchIP="127.0.0.1";
  public static  int _nSearchPort=8800;
  public static String _strSearchDomain="127.0.0.1";
	//end add by luo 20141112
  static final int nBuildCount=11;//锟斤拷锟节憋拷识锟角凤拷锟揭伙拷锟斤拷锟斤拷锟絘pp
  
  static MRServerInfo mrServer1=new MRServerInfo();
  static MRServerInfo mrServer2=new MRServerInfo();
  static MRServerInfo mrServer3=new MRServerInfo();
  
  
//  static boolean loadMRServer(){
//		boolean bResult=false;
//		
////		System.out.println("loadMRServer 1");//add for test
//		
//		
//		MRServerInfo mrServers[] = DatabaseManager.GetALLMRServer();
//		if(mrServers==null){//锟斤拷始锟斤拷锟斤拷锟�
//			mrServer1.setnServerID(1);
//			mrServer1.setbIsInit(false);
//			 
//			mrServer2.setnServerID(2);
//			mrServer2.setbIsInit(false);
//			 
//			mrServer3.setnServerID(3);
//			mrServer3.setbIsInit(false);
//			 
////			System.out.println("loadMRServer init");//add for test
//			
//			DatabaseManager.AddMRServerInfo(mrServer1);
//			DatabaseManager.AddMRServerInfo(mrServer2);
//			DatabaseManager.AddMRServerInfo(mrServer3);
//			
//		}else{
//			
//			
//			
//			for(int i=mrServers.length-1; i>=0; i--){
//				if(mrServers[i]!=null){
//					
////					System.out.println("mrServers["+i+"]: "+mrServers[i].getnServerID()+", "+mrServers[i].getlInitTime()+", "+mrServers[i].getStrDomain()+","+mrServers[i].getStrIP());//add for test
//					
//					if(mrServers[i].getnServerID()==1){
//						mrServer1.setnServerID(1);
//						mrServer1.setnID(mrServers[i].getnID());
//						mrServer1.setbIsInit(mrServers[i].isbIsInit());
//						mrServer1.setlInitTime(mrServers[i].getlInitTime());
//						mrServer1.setStrDomain(mrServers[i].getStrDomain());
//						mrServer1.setStrIP(mrServers[i].getStrIP());
//						
////						System.out.println("mrServer1: "+mrServer1.isbIsInit()+", "+mrServer1.getnServerID()+", "+mrServer1.getlInitTime()+", "+mrServer1.getStrDomain()+","+mrServer1.getStrIP());//add for test
//						
//					}else if(mrServers[i].getnServerID()==2){
//						mrServer2.setnServerID(2);
//						mrServer2.setnID(mrServers[i].getnID());
//						mrServer2.setbIsInit(mrServers[i].isbIsInit());
//						mrServer2.setlInitTime(mrServers[i].getlInitTime());
//						mrServer2.setStrDomain(mrServers[i].getStrDomain());
//						mrServer2.setStrIP(mrServers[i].getStrIP());
//						
////						System.out.println("mrServer2: "+mrServer2.isbIsInit()+", "+mrServer2.getnServerID()+", "+mrServer2.getlInitTime()+", "+mrServer2.getStrDomain()+","+mrServer2.getStrIP());//add for test
//						
//					}else if(mrServers[i].getnServerID()==3){
//						mrServer3.setnServerID(3);
//						mrServer3.setnID(mrServers[i].getnID());
//						mrServer3.setbIsInit(mrServers[i].isbIsInit());
//						mrServer3.setlInitTime(mrServers[i].getlInitTime());
//						mrServer3.setStrDomain(mrServers[i].getStrDomain());
//						mrServer3.setStrIP(mrServers[i].getStrIP());
//						
////						System.out.println("mrServer3: "+mrServer3.isbIsInit()+", "+mrServer3.getnServerID()+", "+mrServer3.getlInitTime()+", "+mrServer3.getStrDomain()+","+mrServer3.getStrIP());//add for test
//						
//					} 
//				}
//			}
//		}
//		return bResult;
//	}
  
  public static final int XGPUSH_VALID_TOKEN_LENGTH = 40; // 信鸽推送合法的token长度为40
  
  public static String strClientID="android";
  public static String strPhoneNumber="unknow";
  public static String strApiKey="A99643DHNZPV";
  public static String strSecretKey="b21daf3cdeaf5a5ba82d4a685d1cdef3";
  public static String strUserID="unknow";
  public static Long lChannelID=2100259645L;
  public static String strSoundFile="alarm.mp3";
  
  
  public static ArrayList<ObjectAlarmMessage> alarmInfoListData=new ArrayList<ObjectAlarmMessage>();
  public static int _nCurrentID=0;
  public static final int HANDLE_MSG_CODE_LOGIN_RESULT=0x10;//锟斤拷息锟斤拷锟斤拷锟绞撅拷穑旱锟铰硷拷锟斤拷
  public static final int LOGIN_RESULT_CODE_SUCCESS=0x0001;//锟斤拷录锟缴癸拷
  public static final int LOGIN_RESULT_CODE_FAIL_NET_DOWN=0x1001;//锟斤拷录失锟斤拷-锟斤拷锟界不通
  public static final int LOGIN_RESULT_CODE_FAIL_NET_POOL=0x1002;//锟斤拷录锟缴癸拷-锟斤拷锟斤拷睿拷锟斤拷映锟绞�
  public static final int LOGIN_RESULT_CODE_FAIL_SERVER_OFFLINE=0x1003;//锟斤拷录失锟斤拷-锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
  public static final int LOGIN_RESULT_CODE_FAIL_VERIFY_FAILED=0x1004;//锟斤拷录失锟斤拷-锟斤拷证锟斤拷锟斤拷
  public static final int LOGIN_RESULT_CODE_FAIL_USER_NOEXIST=0x1005;//锟斤拷录失锟斤拷-锟矫伙拷锟斤拷锟斤拷锟�
  public static final int LOGIN_RESULT_CODE_FAIL_PWD_ERROR=0x1006;//锟斤拷录失锟斤拷-锟斤拷锟斤拷锟斤拷锟�
  public static final int LOGIN_RESULT_CODE_FAIL_OLD_VERSON=0x1007;//锟斤拷录失锟斤拷-前锟剿版本锟斤拷支锟斤拷
  
  
  public static byte [] _playFaceYUVData = new byte[704*576*3];
  public static final int _YUVWidth=0;//640; 
  public static final int _YUVHeight=480; 
  static byte [] _SockBuf = new byte[Defines.MAX_BUFFER_SIZE]; 
  static final String _fileName= "systemConfig.xml";
  static boolean _isAutoLoginEnable=true;//锟角凤拷始锟皆讹拷锟斤拷录锟斤拷锟杰ｏ拷锟斤拷要锟角憋拷锟斤拷系统锟斤拷锟截碉拷录锟斤拷锟斤拷锟斤拷锟斤拷锟皆讹拷锟斤拷录 
  static boolean isResourrceLoaded=false;
  public static byte [] _ImagePixel = new byte[1280*1280*3];
  public static ByteBuffer _capbuffer = ByteBuffer.wrap(_ImagePixel);
  public static  int _capWidth=640; 
  public static  int _capHeight=480; 
	
  public static  float _PTZXWidth=120; 
  public static  float _PTZXHeight=80; 
  static void loadResource(Resources resource){
		if(resource==null)return;
		if(!isResourrceLoaded){
			if(_ImagePixel==null) _ImagePixel = new byte[1280*960*3];
			
			if(_SockBuf==null)_SockBuf = new byte[Defines.MAX_BUFFER_SIZE];
			
			 
			 ////锟斤拷取play_welcome_face锟斤拷yuv锟斤拷锟�
			 if(_playFaceYUVData==null) _playFaceYUVData = new byte[704*576*3];
			 
			 Arrays.fill(_playFaceYUVData, (byte) ((byte)256*256*256));
			 /*int byteread = 0;
		     InputStream inStream = null;
		     try {
		       
		        	 inStream = resource.openRawResource(R.raw.play_welcome_face1);
			  } catch (Exception e) {
		        	return;
		      }
		      try {
		        	byteread = inStream.read(Defines._playFaceYUVData);
		      } catch (IOException e) {
		        	// TODO Auto-generated catch block
		        	e.printStackTrace();
		        }finally{
		        	try {
						inStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		      }
*/
			 ////end 锟斤拷取play_welcome_face锟斤拷yuv锟斤拷锟�
			isResourrceLoaded=true;
		}
		
	   	 
	}
  
  

  public static int _PTZXPointDevID = 0;
  public static HashMap<Integer, PTZXPoint> _ptzxPointList=new HashMap<Integer, PTZXPoint>();
  public static boolean updatePTZXPoints(int nDevID, int nPTZXID, Bitmap image){
		boolean bResult = false;
		PTZXPoint ptzxPoint = null;
		if(_PTZXPointDevID==nDevID){
			
			ptzxPoint = _ptzxPointList.get(nPTZXID);
		}else{
			_ptzxPointList.clear();
		}
		if(ptzxPoint!=null){//锟斤拷锟斤拷
			
		}else{//锟斤拷锟斤拷碌锟�
			 ptzxPoint = new PTZXPoint(0, nDevID, nPTZXID, System.currentTimeMillis(), null);
//			 bResult = DatabaseManager.AddPTZXInfo(ptzxPoint);
		}
		
		if(ptzxPoint!=null){
			ptzxPoint.setFaceImage(image);
			ptzxPoint.setlSaveTime(System.currentTimeMillis());
//			bResult =DatabaseManager.updatePTZXPoint(ptzxPoint);
			
			_ptzxPointList.remove(nPTZXID);
			_ptzxPointList.put(nPTZXID, ptzxPoint);
			_PTZXPointDevID = nDevID;
		}
		ptzxPoint=null;
		return bResult;
	}
//	public static int loadPTZXPoints(int nDevID){
//		
//		
//		boolean isToLoad = true;
//		if(_PTZXPointDevID==nDevID){
//			 isToLoad = false;
//		}
//		
//		if(isToLoad){
//			PTZXPoint [] ptzxPointsList= DatabaseManager.GetPTZXInfos(nDevID);
//			if(ptzxPointsList!=null && ptzxPointsList.length>0){
//				_ptzxPointList.clear();
//				for(int i=ptzxPointsList.length-1; i>=0; i--){
//					_ptzxPointList.put(ptzxPointsList[i].getnPTZXID(), ptzxPointsList[i]);
//				}
//				_PTZXPointDevID=nDevID;
//			}
//		}
//		return _ptzxPointList.size();
//	}
	
	
	
	 public static List<RecordVideoInfo> listMapPlayerBackFile = new ArrayList<RecordVideoInfo>();
	  //录锟斤拷锟侥硷拷锟斤拷锟斤拷
     static final int FILE_TYPE_ALL= 0; //全锟斤拷
     static final int FILE_TYPE_NORMAL= 1; //锟斤拷通
     static final int FILE_TYPE_MOTION= 2;//锟斤拷锟斤拷
     static final int FILE_TYPE_ALARM= 3;//锟斤拷锟斤拷
     
     public static final int REC_FILE_SEARCH_RESULT_CODE_SUCCESS=0x0011;//WIFI锟斤拷锟斤拷锟缴癸拷
	 public static final int REC_FILE_SEARCH_RESULT_CODE_FAIL_NET_DOWN=-0x1011;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷锟界不通
	 public static final int REC_FILE_SEARCH_RESULT_CODE_FAIL_NET_POOL=-0x1012;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷锟斤拷睿拷锟斤拷映锟绞�
	  		
	 public static final int REC_FILE_SEARCH_RESULT_CODE_FAIL_SERVER_OFFLINE=-0x1013;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 public static final int REC_FILE_SEARCH_RESULT_CODE_FAIL_VERIFY_FAILED=-0x1014;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷证锟斤拷锟斤拷
	 public static final int REC_FILE_SEARCH_RESULT_CODE_FAIL_USER_NOEXIST=-0x1015;//WIFI锟斤拷锟斤拷失锟斤拷-锟矫伙拷锟斤拷锟斤拷锟�
	 public static final int REC_FILE_SEARCH_RESULT_CODE_FAIL_PWD_ERROR=-0x1016;//WIFI锟斤拷锟斤拷失锟斤拷-锟斤拷锟斤拷锟斤拷锟�
	 public static final int REC_FILE_SEARCH_RESULT_CODE_FAIL_OLD_VERSON=-0x1017;//WIFI锟斤拷锟斤拷失锟斤拷-前锟剿版本锟斤拷支锟斤拷

	public static final byte AuthModeOpen = 0x00;
	public static final byte AuthModeWEPOPEN = 0x01;
	public static final byte AuthModeWEPSHARE = 0x02;

	public static final byte AuthModeWPAEAP = 0x03;
	public static final byte AuthModeWPA2EAP = 0x04;
	public static final byte AuthModeWPA1EAPWPA2EAP = 0x05;

	public static final byte AuthModeWPAPSKAES = 0x06;
	public static final byte AuthModeWPAPSKTKIP = 0x07;
	public static final byte AuthModeWPAPSKTKIPAES = 0x08;

	public static final byte AuthModeWPA2PSKAES = 0x09;
	public static final byte AuthModeWPA2PSKTKIP = 10;
	public static final byte AuthModeWPA2PSKTKIPAES = 11;

	public static final byte AuthModeWPA1PSKWPA2PSKAES = 12;
	public static final byte AuthModeWPA1PSKWPA2PSKTKIP = 13;
	public static final byte AuthModeWPA1PSKWPA2PSKTKIPAES = 14;
	public static int _nConfigID = 0;
	public static long _lConfigIDGenTime = 0;

	public static int getConfigID() {
		if (_nConfigID <= 0 || (System.currentTimeMillis() - _lConfigIDGenTime >= 60000)) {
			_nConfigID = 1 + (int) (Math.random() * 10);
			if (_nConfigID >= 10) {
				_nConfigID = 9;
			} else if (_nConfigID <= 0) {
				_nConfigID = 1;
			}
		}
		return _nConfigID;
	}

	public static final String JSP_SERVER_REGIST_CLIENT = "/GetAlarmMsg/XGPhoneClientRegistered?param="; // 锟斤拷取锟斤拷锟斤拷锟斤拷细锟斤拷息锟斤拷锟斤拷
	public static final String JSP_SERVER_SET_DEVICE_ALARM = "/GetAlarmMsg/ClientWithDeivceRegistered?param="; // 锟斤拷取锟斤拷锟斤拷锟斤拷细锟斤拷息锟斤拷锟斤拷
	public static final String JSP_SERVER_SET_DEVICE_ARRAY_ALARM = "/GetAlarmMsg/ClientWithDeivceArrayRegistered?param="; // 锟斤拷取锟斤拷锟斤拷锟斤拷

	public static boolean isZh(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			return true;
		else
			return false;
	}

	/**
	 * dp2px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px2dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	public static List<RecordVideoInfo> cloudRecordFileList = new ArrayList<RecordVideoInfo>();
	public static List<Map<String, Object>> localRecordFileList = new ArrayList<Map<String, Object>>();
	
	/**
	 * 锟角凤拷锟斤拷锟斤拷没锟斤拷璞革拷斜锟�
	 */
	public static boolean shouldLoadUserDeviceList = false;
	public static boolean shouldRefreshDeviceList = false;
	public static boolean unbindingServiceFromMail = false;

//	public static HomePageActivity homePageActivity = null;
	
	
	public static String getCurrentConnectedWifiSSIDName(WifiManager mWiFiManager) {
		String currentConnectedSSIDName = mWiFiManager.getConnectionInfo()
				.getSSID();
		currentConnectedSSIDName = (String) currentConnectedSSIDName
				.subSequence(1, currentConnectedSSIDName.length() - 1);

		if (currentConnectedSSIDName != null
				&& currentConnectedSSIDName.length() > 0
				&& !currentConnectedSSIDName.equalsIgnoreCase("0x")) {

			if (currentConnectedSSIDName.substring(0, 1).equals("\"")
					&& currentConnectedSSIDName.substring(
							currentConnectedSSIDName.length() - 1,
							currentConnectedSSIDName.length()).equals("\"")) {
				currentConnectedSSIDName = currentConnectedSSIDName.substring(
						1, (currentConnectedSSIDName.length() - 1)); // 锟矫碉拷锟斤拷前锟斤拷锟接碉拷锟矫伙拷锟斤拷去锟斤拷前锟斤拷双锟斤拷锟�
			}
		}
		return currentConnectedSSIDName;
	}
	/**
	 * camType == 1 ----> 锟揭憋拷锟斤拷锟桔撅拷头
	 */
	public static final int CAMTYPE_WALL = 1; // camType == 1 ----> 锟揭憋拷锟斤拷锟桔撅拷头
	/**
	 * camType == 2 ----> 锟斤拷锟斤拷锟桔撅拷头
	 */
	public static final int CAMTYPE_CEIL = 2; // camType == 2 ----> 锟斤拷锟斤拷锟桔撅拷头
	
	/**
	 * fixType == 1 ----> 锟揭憋拷锟斤拷锟桔撅拷头
	 */
	public static final int FIXTYPE_WALL = 1; // fixType == 1 ----> 锟揭憋拷锟斤拷锟桔撅拷头
	/**
	 * fixType == 0 ----> 锟斤拷锟斤拷锟桔撅拷头
	 */
	public static final int FIXTYPE_CEIL = 0; // fixType == 0 ----> 锟斤拷锟斤拷锟桔撅拷头
	
	
	/**
	 * 锟斤拷取状态锟斤拷锟竭讹拷 add 2016锟斤拷9锟斤拷30锟斤拷
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
	/**
	 * 获得关闭播放相关活动的Action
	 * 2016年10月25日 add
	 */
	public static String getReceiverActionString(Context context){
		return context.getPackageName() + ".CLOSE_PLAYER_ACTIVITY";
	}
	
	/**
	 * add 2016年10月26日
	 */
//	public static CloudStorageActivity sCloudStorageActivity = null;
	
	// ---- 灯光状态信息 2017-5-27----
	/**
	 * 灯光设备不支持：0
	 */
	public static final int LIGHT_STATE_NOT_SUPPORT = 0;
	/**
	 * 灯光开启：1
	 */
	public static final int LIGHT_STATE_ON = 1;
	/**
	 * 灯光关闭：2
	 */
	public static final int LIGHT_STATE_OFF = 2;
	/**
	 * 灯光自动：3
	 */
	public static final int LIGHT_STATE_AUTO = 3;
	
	
	public static boolean isAECEnable() {
		Date date = new Date();
		Calendar calendarDeadLine = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")); // GMT+8:00
		calendarDeadLine.set(2017, 8, 30, 23, 59, 59);
		return calendarDeadLine.getTime().compareTo(date) > 0; // 大于0表示没有过期
	}
////////////////////add by luo 20170703
public static final String JSP_SERVER_CONNECT_PICTUER_TOP_NEW ="/GetAlarmMsg/AlarmSelectServletTopNewPicture?param=";

public static final String JSP_SERVER_REGIST_CLIENT_V20 = "/GetAlarmMsg/NVClientPushTokenUpdate"; // 
public static final String JSP_SERVER_SET_DEVICE_ARRAY_ALARM_V20 = "/GetAlarmMsg/NVClientDeviceListUpdate"; // 取

public static final String GET_PICTUER_BY_PATH_V20 ="/GetAlarmMsg/NVGetAlarmPictureByPath"; // 图片不在同一个服务器，获取大图
public static final String GET_PICTUER_BY_ID_V20 ="/GetAlarmMsg/NVGetAlarmPictureByID"; // 获取大图
public static final String GET_MESSAGE_LIST_V20 ="/GetAlarmMsg/NVGetAlarmNewMessageList"; // 获取列表
public static final String GET_MORE_MESSAGE_LIST_V20 ="/GetAlarmMsg/NVGetAlarmMessageListMore"; // 获取更多列表

public static boolean isAutoBindService = false;
public static int nOrderID = 0;
public static int nServiceID = 0;
public static String strEndTime = "";
public static String strIntroduction = "";

public static String spForUpdateServiceData = "isUpdateServiceData";

public static String spForUnBindServiceData = "isUpdateServiceData";

/////////////////////end add by luo 20170703
}
