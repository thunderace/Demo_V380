package com.macrovideo.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.macrovideo.sdk.defines.Defines;
import com.macrovideo.sdk.defines.ResultCode;
import com.macrovideo.sdk.media.CloudServiceHelper;
import com.macrovideo.sdk.media.IDownloadCallback;
import com.macrovideo.sdk.media.ILoginDeviceCallback;
import com.macrovideo.sdk.media.IRecFileCallback;
import com.macrovideo.sdk.media.LoginHandle;
import com.macrovideo.sdk.media.LoginHelper;
import com.macrovideo.sdk.media.RecordFileHelper;
import com.macrovideo.sdk.media.RecordVideoDownloader;
import com.macrovideo.sdk.objects.DeviceInfo;
import com.macrovideo.sdk.objects.LoginParam;
import com.macrovideo.sdk.objects.RecordFileParam;
import com.macrovideo.sdk.objects.RecordVideoInfo;
import com.macrovideo.sdk.tools.Functions;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.macrovideo.materialshowcaseview.MaterialShowcaseView;
//import com.macrovideo.materialshowcaseview.ShowcaseConfig;

public class PlaybackFragment extends Fragment implements OnClickListener, OnItemClickListener {

    // 下载
    private static final int FILE_DOWNLOAD_STATE_NORMAL = 2;
    // 下载成功
    private static final int FILE_DOWNLOAD_STATE_SUCCESS = 1;
    // 下载中
    private static final int FILE_DOWNLOAD_STATE_DOWNLOADING = 0;
    // 下载出错重新下载
    private static final int FILE_DOWNLOAD_STATE_ERROR = -1;
    // 下载连接中
    private static final int FILE_DOWNLOAD_STATE_CONNECTING = -2;


    private static final int HANDLE_MSG_CODE_LOGIN_FOR_SEARCH_RECORD_FILES = 1;

    static final int DATETIME_MODE_UNDEFINE = 000;
    static final int DATETIME_MODE_DATE = 100;
    static final int DATETIME_MODE_STARTTIME = 101;
    static final int DATETIME_MODE_ENDTIME = 102;

    private Activity relateAtivity = null;
    private View contentView = null;

    private RelativeLayout layoutSearchParam = null;
    private LinearLayout /* layoutSearchParam = null, */layoutRecFileList = null;
    private Button btnListVisible = null, btnStartSearch = null;
    private boolean isListVisible = false;

    private LinearLayout layoutDevice = null, layoutSearchDate = null, layoutSearchEndTime = null, layoutSearchStartTime = null;

    private ListView serverlistView = null;
    private boolean bIsRecFileSearching = false;
    private ListView recFileListView = null;
    private int m_nLoginExID = 0;
    private ArrayList<RecordVideoInfo> fileList = new ArrayList<RecordVideoInfo>();

    private View deviceSelectConctentView = null, datetimeSelectConctentView = null, datetimeSelectConctentViewCloud;
    private Dialog deviceSelectDialog = null, datetimeSelectDialog = null, datetimeSelectDialogCloud = null;

    private TextView textViewDevice = null, textViewDate = null, textViewStartTime = null, textViewEndTime = null, tvDate = null, tvStartTime = null, tvEndTime = null;
    private Button btnDeviceSelectCancel = null;

    // ===add by mai 2015-1-23===============
    private ImageView ivNvplayerBack, ivPlayerBackType, btnDeviceSelectBack, ivPlayerBackType2;
    private LinearLayout /* llPlayerBackListView, */llPlayerBackType, llSearchType, llCloudRec, llTFCardRec, llPlayerBackType_non_CN;

    private TextView tvCloudVideo, tvTFVideo;
    private TextView tvPlayerBackType, tvPlayerBackType2;
    private boolean bSearchType = true;
    private LinearLayout llAll;
    private int optOf;
    // ===end add by mai 2015-1-23===========

    private int nDatetimeMode = DATETIME_MODE_UNDEFINE;
    private TextView tvDateTimeTitle = null, tvDateTimeCurrent = null;
    private DatePicker mSelectDatePicker = null, mSelectDatePickerCloud = null;
    private TimePicker mSelectTimePicker = null, mSelectTimePickerCloud = null;
    private LinearLayout layoutDatePicker = null, layoutTimePicker = null, layoutDatePickerCloud = null, layoutTimePickerCloud = null;
    private Button btnDatetimeSelectCancel = null, btnDatetimeSelectOK = null;

    private LinearLayout llPlayBackTitle; // add by mai 2015-7-9
    // 锟斤拷锟节讹拷态锟斤拷锟矫憋拷锟斤拷图片
    private int REC_FILE_FAIL = 10010; // add by mai 20151216 锟斤拷锟斤拷录锟斤拷锟侥硷拷锟斤拷时

    // search param
    private int nSearchChn = 0;
    private int nSearchType = LocalDefines.FILE_TYPE_ALL;

    boolean isInit = false;
    // 锟斤拷锟斤拷锟斤拷锟斤拷锟秸ｏ拷锟斤拷锟节ｏ拷
    private short nYear = 2000;
    private short nMonth = 0;
    private short nDay = 0;
    // 锟斤拷锟斤拷锟斤拷始时锟斤拷锟诫（时锟戒）
    private short nStartHour = 0;
    private short nStartMin = 0;
    private short nStartSec = 0;
    // 锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷锟诫（时锟戒）
    private short nEndHour = 23;
    private short nEndMin = 59;
    private short nEndSec = 0;

    private short nYear_Cloud = 2000;
    private short nMonth_Cloud = 0;
    private short nDay_Cloud = 0;

    private short nStartHour_Cloud = 0;
    private short nStartMin_Cloud = 0;
    private short nStartSec_Cloud = 0;

    private short nEndHour_Cloud = 23;
    private short nEndMin_Cloud = 59;
    private short nEndSec_Cloud = 0;

    private boolean isActive = false;
    private boolean isLoadFromDatabase = false;
    private DeviceInfo deviceInfo = null; // add by mai 2015-7-10 锟斤拷前锟借备
    private LoginHandle _deviceParam = null;// add by lin 20151210
    private PopupWindow popupListView;
    private LinearLayout llTypeAll, llTypeAll_non_CN;
    private LinearLayout llTypeAuto, llTypeAuto_non_CN;
    private LinearLayout llTypeAlarm, llTypeAlarm_non_CN;
    private ImageView ivTypeAll, ivTypeAll_non_CN;
    private ImageView ivTypeAuto, ivTypeAuto_non_CN;
    private ImageView ivTypeAlarm, ivTypeAlarm_non_CN;

    private Button /*btnStartSearchCloud*/ btnSearch;
    private ImageView btnDeviceSelectBack_Cloud;
    private LinearLayout ll_cloud_record, ll_search_date, ll_search_start_time, ll_search_end_time;

    private LinearLayout llChannels, llChannel, ll_choose1_channel, ll_choose2_channel, ll_channel0, ll_channel1, ll_channel2, ll_channel3, ll_channel4, ll_channel5, ll_channel6, ll_channel7;

    private ImageView iv_channel0, iv_channel1, iv_channel2, iv_channel3, iv_channel4, iv_channel5, iv_channel6, iv_channel7, iv_Channel;

    private int SearchChannel = 0;
    private boolean openSearchChannel = true;

    private TextView textChannel;

    private TextView tvDeviceId;
    private boolean isCloudFileList = false;

    private String mAccesstoken;
    private int mUserId;
    private String mEcsIP;
    private int mEcsPort;
    private String mEcsIP2;
    private int mEcsPort2;

    Map<Integer, RecordVideoDownloader> mRecFileDownloaderMap;
    private List<Integer> mRecFileDownloaderList;

    private RecordVideoDownloader mRecFileDownloader;
    private int mDLFileListPosition = -1; // 锟斤拷前锟斤拷锟截碉拷锟斤拷锟斤拷锟斤拷FileList锟叫碉拷位锟矫ｏ拷锟斤拷始锟斤拷为-1
    private String mDLFilePath; // 锟斤拷前锟斤拷锟截碉拷锟斤拷锟斤拷木锟斤拷路锟斤拷

    private boolean isSearchCloudRec = false;

    private LinearLayout ll_SearchType_Non_CN, ll_SearchType_CN = null;

    boolean isZh = true;

    private int nSearchFailCount = 0;

    // /
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isInit) {
            Calendar calendar = Calendar.getInstance();
            // 锟斤拷锟斤拷锟斤拷锟斤拷锟秸ｏ拷锟斤拷锟节ｏ拷
            nYear = (short) calendar.get(Calendar.YEAR);
            nMonth = (short) calendar.get(Calendar.MONTH);
            nDay = (short) calendar.get(Calendar.DAY_OF_MONTH);

            nYear_Cloud = (short) calendar.get(Calendar.YEAR);
            nMonth_Cloud = (short) calendar.get(Calendar.MONTH);
            nDay_Cloud = (short) calendar.get(Calendar.DAY_OF_MONTH);

            // 锟斤拷锟斤拷锟斤拷始时锟斤拷锟诫（时锟戒）
            nStartHour = 0;
            nStartMin = 0;
            nStartSec = 0;

            nStartHour_Cloud = 0;
            nStartMin_Cloud = 0;
            nStartSec_Cloud = 0;

            // 锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷锟诫（时锟戒）
            nEndHour = 23;
            nEndMin = 59;
            nEndSec = 0;

            nEndHour_Cloud = 23;
            nEndMin_Cloud = 59;
            nEndSec_Cloud = 0;
            isInit = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_nvplayer_playback, container, false);
        if (relateAtivity == null)
            relateAtivity = getActivity();
        contentView = v;

        mAccesstoken = getActivity().getSharedPreferences("SaveSign", Activity.MODE_PRIVATE).getString("saveToken", "");

        mUserId = getActivity().getSharedPreferences("SaveSign", Activity.MODE_PRIVATE).getInt("saveServeruserid", -101);
        mEcsIP = getActivity().getSharedPreferences("SaveSign", Activity.MODE_PRIVATE).getString("saveloginEcsIp", "");
        mEcsPort = getActivity().getSharedPreferences("SaveSign", Activity.MODE_PRIVATE).getInt("saveloginEcsport", 0);
        mEcsIP2 = getActivity().getSharedPreferences("SaveSign", Activity.MODE_PRIVATE).getString("saveloginEcsIp2", "");

        mEcsPort2 = getActivity().getSharedPreferences("SaveSign", Activity.MODE_PRIVATE).getInt("saveloginEcsport2", 0);
        InitSubView(); // 锟斤拷始锟斤拷锟斤拷锟斤拷
        createDialogs();
        createDialogsCloud();
        presentShowcaseSequence();// add by lin 20160129
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
        bIsRecFileSearching = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onDestroy() {

        stopCurrentDownloadTask(); // 锟斤拷锟斤拷 2016锟斤拷8锟斤拷18锟斤拷
        if (llPlayBackTitle == null) {
        }
        System.gc();
        // end add by mai 2015-7-9
        m_nThreadID++;
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    private void InitSubView() { // 锟斤拷始锟斤拷锟截硷拷
        llPlayBackTitle = (LinearLayout) contentView.findViewById(R.id.llPlayBackTitle);

        createLoadingDialog();

        ll_SearchType_Non_CN = (LinearLayout) contentView.findViewById(R.id.ll_SearchType_Non_CN);
        ll_SearchType_CN = (LinearLayout) contentView.findViewById(R.id.ll_SearchType_CN);
        llSearchType = (LinearLayout) contentView.findViewById(R.id.llSearchType);
        llSearchType.setOnClickListener(this);
        llPlayerBackType_non_CN = (LinearLayout) contentView.findViewById(R.id.llPlayerBackType_non_CN);

        isZh = LocalDefines.isZh(getActivity());
        if (isZh) {
            ll_SearchType_CN.setVisibility(View.VISIBLE);
            ll_SearchType_Non_CN.setVisibility(View.GONE);
        } else {
            ll_SearchType_CN.setVisibility(View.GONE);
            ll_SearchType_Non_CN.setVisibility(View.VISIBLE);
        }

        tvCloudVideo = (TextView) contentView.findViewById(R.id.txt_Cloud_video);
        tvTFVideo = (TextView) contentView.findViewById(R.id.txt_TF_Video);

        ll_cloud_record = (LinearLayout) contentView.findViewById(R.id.ll_cloud_record);

        ll_search_date = (LinearLayout) contentView.findViewById(R.id.ll_search_date);
        ll_search_date.setOnClickListener(this);

        ll_search_start_time = (LinearLayout) contentView.findViewById(R.id.ll_search_start_time);
        ll_search_start_time.setOnClickListener(this);

        ll_search_end_time = (LinearLayout) contentView.findViewById(R.id.ll_search_end_time);
        ll_search_end_time.setOnClickListener(this);

        ivNvplayerBack = (ImageView) contentView.findViewById(R.id.ivNvplayerBack);
        ivNvplayerBack.setOnClickListener(this);

        ivPlayerBackType = (ImageView) contentView.findViewById(R.id.ivPlayer_back_type);
        ivPlayerBackType2 = (ImageView) contentView.findViewById(R.id.ivPlayer_back_type2);
        llAll = (LinearLayout) contentView.findViewById(R.id.llAll);

        LinearLayout view = (LinearLayout) View.inflate(getActivity(), R.layout.popup_window_listview2, null);
        serverlistView = (ListView) view.findViewById(R.id.lvPlayer_back);

        popupListView = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupListView.setFocusable(true);
        popupListView.setOutsideTouchable(true);
        popupListView.setAnimationStyle(R.style.popupwindow_device_list);
        popupListView.setBackgroundDrawable(new BitmapDrawable());
        popupListView.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // add 2016锟斤拷8锟斤拷17锟斤拷 锟睫革拷bug,锟斤拷bug锟斤拷锟斤拷
                // 为选锟斤拷锟借备锟斤拷锟斤拷popupwindow锟斤拷失锟斤拷btnListVisible未锟斤拷示
                btnListVisible.setVisibility(View.VISIBLE);
            }
        });

        btnSearch = (Button) contentView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        btnDeviceSelectBack_Cloud = (ImageView) contentView.findViewById(R.id.btnDeviceSelectBack_Cloud);
        btnDeviceSelectBack_Cloud.setOnClickListener(this);

        llPlayerBackType = (LinearLayout) contentView.findViewById(R.id.llPlayerBackType);
        llCloudRec = (LinearLayout) contentView.findViewById(R.id.llCloudRec);
        llCloudRec.setOnClickListener(this);
        llTFCardRec = (LinearLayout) contentView.findViewById(R.id.llTFCardRec);
        llTFCardRec.setOnClickListener(this);
        tvPlayerBackType = (TextView) contentView.findViewById(R.id.txt_TF_Video);
        tvPlayerBackType2 = (TextView) contentView.findViewById(R.id.tvPlayer_back_type);
        // ===end add by mai 2015-1-23=======================

        layoutSearchParam = (RelativeLayout) contentView.findViewById(R.id.layoutSearchParam);
        layoutRecFileList = (LinearLayout) contentView.findViewById(R.id.layoutRecFileList);
        btnStartSearch = (Button) contentView.findViewById(R.id.btnStartSearch);
        btnStartSearch.setOnClickListener(this);

        btnListVisible = (Button) contentView.findViewById(R.id.btnListVisible);
        btnListVisible.setOnClickListener(this);

        btnDeviceSelectBack = (ImageView) contentView.findViewById(R.id.btnDeviceSelectBack);
        btnDeviceSelectBack.setOnClickListener(this);

        recFileListView = (ListView) contentView.findViewById(R.id.recfile_list);
        recFileListView.setOnItemClickListener(this);
        setListViewOnScrollListener();

        layoutDevice = (LinearLayout) contentView.findViewById(R.id.layoutDevice);
        layoutDevice.setOnClickListener(this);
        layoutSearchDate = (LinearLayout) contentView.findViewById(R.id.layoutSearchDate);
        layoutSearchDate.setOnClickListener(this);
        layoutSearchEndTime = (LinearLayout) contentView.findViewById(R.id.layoutSearchEndTime);
        layoutSearchEndTime.setOnClickListener(this);
        layoutSearchStartTime = (LinearLayout) contentView.findViewById(R.id.layoutSearchStartTime);
        layoutSearchStartTime.setOnClickListener(this);

        textViewDevice = (TextView) contentView.findViewById(R.id.textViewDevice);
        textViewDate = (TextView) contentView.findViewById(R.id.textViewDate);
        textViewStartTime = (TextView) contentView.findViewById(R.id.textViewStartTime);
        textViewEndTime = (TextView) contentView.findViewById(R.id.textViewEndTime);

        tvDate = (TextView) contentView.findViewById(R.id.tvDate);
        tvStartTime = (TextView) contentView.findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) contentView.findViewById(R.id.tvEndTime);
        llChannels = (LinearLayout) contentView.findViewById(R.id.llChannels);
        llChannel = (LinearLayout) contentView.findViewById(R.id.llChannel);
        // 取锟斤拷锟较次诧拷锟脚伙拷锟斤拷选锟斤拷锟斤拷锟矫碉拷锟借备
        if (LocalDefines._severInfoListData != null && LocalDefines._severInfoListData.size() > 0) {
            if (LocalDefines._PlaybackListviewSelectedPosition >= 0 && LocalDefines._PlaybackListviewSelectedPosition < LocalDefines._severInfoListData.size()) {

            } else {
                LocalDefines._PlaybackListviewSelectedPosition = 0;
            }

            DeviceInfo info = LocalDefines._severInfoListData.get(LocalDefines._PlaybackListviewSelectedPosition);
            if (info != null && textViewDevice != null) {
                if (Functions.isNVRDevice("" + info.getnDevID())) {
                    llChannel.setVisibility(View.VISIBLE);
                } else {
                    llChannel.setVisibility(View.GONE);
                }
                if (info.getStrName() != null && info.getStrName().length() > 0) {
                    textViewDevice.setText(info.getStrName());
                } else {
                    textViewDevice.setText("" + info.getnDevID());
                }
                deviceInfo = info;
            }
        } else {
            textViewDevice.setText(getString(R.string.noDevice));
        }

        if (isListVisible) {
            layoutSearchParam.setVisibility(View.GONE);
            layoutRecFileList.setVisibility(View.VISIBLE);
            if (isLoadFromDatabase) {// 锟斤拷要锟斤拷锟斤拷菘锟斤拷锟斤拷锟斤拷
                GetRecFileListFromDatabase();
            } else {
                refreshRecFileList();
            }

        } else {
            layoutSearchParam.setVisibility(View.VISIBLE);
            layoutRecFileList.setVisibility(View.GONE);
            if (isLoadFromDatabase) {// 锟斤拷要锟斤拷锟斤拷菘锟斤拷锟斤拷锟斤拷
                GetRecFileListFromDatabase();
            }
        }

        if (nMonth < 9 && nDay < 10) {
            textViewDate.setText("" + nYear + "-0" + (nMonth + 1) + "-0" + nDay);
        } else if (nMonth >= 9 && nDay < 10) {
            textViewDate.setText("" + nYear + "-" + (nMonth + 1) + "-0" + nDay);
        } else if (nMonth < 9 && nDay >= 10) {
            textViewDate.setText("" + nYear + "-0" + (nMonth + 1) + "-" + nDay);
        } else {
            textViewDate.setText("" + nYear + "-" + (nMonth + 1) + "-" + nDay);
        }

        if (nStartHour <= 9 && nStartMin <= 9) {
            textViewStartTime.setText("0" + nStartHour + ":0" + nStartMin);
        } else if (nStartHour <= 9 && nStartMin > 9) {
            textViewStartTime.setText("0" + nStartHour + ":" + nStartMin);
        } else if (nStartHour > 9 && nStartMin <= 9) {
            textViewStartTime.setText("" + nStartHour + ":0" + nStartMin);
        } else {
            textViewStartTime.setText("" + nStartHour + ":" + nStartMin);
        }

        if (nEndHour <= 9 && nStartMin <= 9) {
            textViewEndTime.setText("0" + nEndHour + ":0" + nEndMin);
        } else if (nEndHour <= 9 && nStartMin > 9) {
            textViewEndTime.setText("0" + nEndHour + ":" + nEndMin);
        } else if (nEndHour > 9 && nEndMin <= 9) {
            textViewEndTime.setText("" + nEndHour + ":0" + nEndMin);
        } else {
            textViewEndTime.setText("" + nEndHour + ":" + nEndMin);
        }

        if (nMonth_Cloud < 9 && nDay_Cloud < 10) {
            tvDate.setText("" + nYear_Cloud + "-0" + (nMonth_Cloud + 1) + "-0" + nDay_Cloud);
        } else if (nMonth_Cloud >= 9 && nDay < 10) {
            tvDate.setText("" + nYear_Cloud + "-" + (nMonth_Cloud + 1) + "-0" + nDay_Cloud);
        } else if (nMonth_Cloud < 9 && nDay_Cloud >= 10) {
            tvDate.setText("" + nYear_Cloud + "-0" + (nMonth_Cloud + 1) + "-" + nDay_Cloud);
        } else {
            tvDate.setText("" + nYear_Cloud + "-" + (nMonth_Cloud + 1) + "-" + nDay_Cloud);
        }

        if (nStartHour_Cloud <= 9 && nStartMin_Cloud <= 9) {
            tvStartTime.setText("0" + nStartHour_Cloud + ":0" + nStartMin_Cloud);
        } else if (nStartHour_Cloud <= 9 && nStartMin_Cloud > 9) {
            tvStartTime.setText("0" + nStartHour_Cloud + ":" + nStartMin_Cloud);
        } else if (nStartHour_Cloud > 9 && nStartMin_Cloud <= 9) {
            tvStartTime.setText("" + nStartHour_Cloud + ":0" + nStartMin_Cloud);
        } else {
            tvStartTime.setText("" + nStartHour_Cloud + ":" + nStartMin_Cloud);
        }

        if (nEndHour_Cloud <= 9 && nStartMin_Cloud <= 9) {
            tvEndTime.setText("0" + nEndHour_Cloud + ":0" + nEndMin_Cloud);
        } else if (nEndHour_Cloud <= 9 && nStartMin_Cloud > 9) {
            tvEndTime.setText("0" + nEndHour_Cloud + ":" + nEndMin_Cloud);
        } else if (nEndHour_Cloud > 9 && nEndMin_Cloud <= 9) {
            tvEndTime.setText("" + nEndHour_Cloud + ":0" + nEndMin_Cloud);
        } else {
            tvEndTime.setText("" + nEndHour_Cloud + ":" + nEndMin_Cloud);
        }

        tvDeviceId = (TextView) contentView.findViewById(R.id.tv_device_id);
        String strRecordFileHintText = getString(R.string.str_cloud_record_search);
        if (deviceInfo != null) {
            tvDeviceId.setText(String.valueOf(strRecordFileHintText + deviceInfo.getnDevID()));
        }
        llTypeAll = (LinearLayout) contentView.findViewById(R.id.ll_type_all);
        llTypeAuto = (LinearLayout) contentView.findViewById(R.id.ll_type_auto);
        llTypeAlarm = (LinearLayout) contentView.findViewById(R.id.ll_type_alarm);
        llTypeAuto.setOnClickListener(this);
        llTypeAll.setOnClickListener(this);
        llTypeAlarm.setOnClickListener(this);

        llTypeAll_non_CN = (LinearLayout) contentView.findViewById(R.id.ll_type_all_non_CN);
        llTypeAuto_non_CN = (LinearLayout) contentView.findViewById(R.id.ll_type_auto_non_CN);
        llTypeAlarm_non_CN = (LinearLayout) contentView.findViewById(R.id.ll_type_alarm_non_CN);

        llTypeAll_non_CN.setOnClickListener(this);
        llTypeAuto_non_CN.setOnClickListener(this);
        llTypeAlarm_non_CN.setOnClickListener(this);

        ivTypeAll = (ImageView) contentView.findViewById(R.id.iv_type_all);
        ivTypeAuto = (ImageView) contentView.findViewById(R.id.iv_type_auto);
        ivTypeAlarm = (ImageView) contentView.findViewById(R.id.iv_type_alarm);

        ivTypeAll_non_CN = (ImageView) contentView.findViewById(R.id.iv_type_all_non_CN);
        ivTypeAuto_non_CN = (ImageView) contentView.findViewById(R.id.iv_type_auto_non_CN);
        ivTypeAlarm_non_CN = (ImageView) contentView.findViewById(R.id.iv_type_alarm_non_CN);


        ll_choose1_channel = (LinearLayout) contentView.findViewById(R.id.ll_choose1_channel);
        ll_choose2_channel = (LinearLayout) contentView.findViewById(R.id.ll_choose2_channel);
        ll_channel0 = (LinearLayout) contentView.findViewById(R.id.ll_channel0);
        ll_channel1 = (LinearLayout) contentView.findViewById(R.id.ll_channel1);
        ll_channel2 = (LinearLayout) contentView.findViewById(R.id.ll_channel2);
        ll_channel3 = (LinearLayout) contentView.findViewById(R.id.ll_channel3);
        ll_channel4 = (LinearLayout) contentView.findViewById(R.id.ll_channel4);
        ll_channel5 = (LinearLayout) contentView.findViewById(R.id.ll_channel5);
        ll_channel6 = (LinearLayout) contentView.findViewById(R.id.ll_channel6);
        ll_channel7 = (LinearLayout) contentView.findViewById(R.id.ll_channel7);
        llChannels.setOnClickListener(this);
        llTypeAuto.setOnClickListener(this);
        llTypeAll.setOnClickListener(this);
        llTypeAlarm.setOnClickListener(this);
        ll_channel0.setOnClickListener(this);
        ll_channel1.setOnClickListener(this);
        ll_channel2.setOnClickListener(this);
        ll_channel3.setOnClickListener(this);
        ll_channel4.setOnClickListener(this);
        ll_channel5.setOnClickListener(this);
        ll_channel6.setOnClickListener(this);
        ll_channel7.setOnClickListener(this);
        ivTypeAll = (ImageView) contentView.findViewById(R.id.iv_type_all);
        ivTypeAuto = (ImageView) contentView.findViewById(R.id.iv_type_auto);
        ivTypeAlarm = (ImageView) contentView.findViewById(R.id.iv_type_alarm);
        iv_channel0 = (ImageView) contentView.findViewById(R.id.iv_channel0);
        iv_channel1 = (ImageView) contentView.findViewById(R.id.iv_channel1);
        iv_channel2 = (ImageView) contentView.findViewById(R.id.iv_channel2);
        iv_channel3 = (ImageView) contentView.findViewById(R.id.iv_channel3);
        iv_channel4 = (ImageView) contentView.findViewById(R.id.iv_channel4);
        iv_channel5 = (ImageView) contentView.findViewById(R.id.iv_channel5);
        iv_channel6 = (ImageView) contentView.findViewById(R.id.iv_channel6);
        iv_channel7 = (ImageView) contentView.findViewById(R.id.iv_channel7);
        iv_Channel = (ImageView) contentView.findViewById(R.id.iv_Channel);
        textChannel = (TextView) contentView.findViewById(R.id.textChannel);
        textChannel.setText("" + 1);
        //
        switch (SearchChannel) {
            case 0:
                iv_channel0.setImageResource(R.drawable.radio_select_check);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case 1:
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_check);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case 2:
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_check);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case 3:
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_check);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case 4:
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_check);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case 5:
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_check);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case 6:
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_check);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case 7:
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_check);
                break;
        }
        //
        switch (nSearchType) {
            case Defines.FILE_TYPE_ALL: // 锟斤拷锟斤拷录锟斤拷锟侥硷拷
                ivTypeAll.setImageResource(R.drawable.radio_select_check);
                ivTypeAuto.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAlarm.setImageResource(R.drawable.radio_select_uncheck);

                ivTypeAll_non_CN.setImageResource(R.drawable.radio_select_check);
                ivTypeAuto_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAlarm_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case Defines.FILE_TYPE_NORMAL: // 锟皆讹拷录锟斤拷锟斤拷募锟�
                ivTypeAll.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAuto.setImageResource(R.drawable.radio_select_check);
                ivTypeAlarm.setImageResource(R.drawable.radio_select_uncheck);

                ivTypeAll_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAuto_non_CN.setImageResource(R.drawable.radio_select_check);
                ivTypeAlarm_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                break;
            case Defines.FILE_TYPE_ALARM: // 锟斤拷锟斤拷录锟斤拷
                ivTypeAll.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAuto.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAlarm.setImageResource(R.drawable.radio_select_check);

                ivTypeAll_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAuto_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAlarm_non_CN.setImageResource(R.drawable.radio_select_check);
                break;
        }
    }

    // 锟斤拷示时锟斤拷锟斤拷锟斤拷
    private void createDialogs() { // 锟斤拷锟斤拷锟斤拷陆时 锟斤拷锟斤拷锟斤拷锟斤拷曰锟斤拷锟�

        deviceSelectConctentView = LayoutInflater.from(relateAtivity).inflate(R.layout.devices_select_dialog, null);
        deviceSelectDialog = new Dialog(relateAtivity, R.style.selectorDialog);
        deviceSelectDialog.setContentView(deviceSelectConctentView);
        deviceSelectDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub
                btnDeviceSelectCancel = (Button) deviceSelectConctentView.findViewById(R.id.btnDeviceSelectCancel);

                btnDeviceSelectCancel.setOnClickListener(PlaybackFragment.this);

            }
        });

        deviceSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }

        });

        //
        datetimeSelectConctentView = LayoutInflater.from(relateAtivity).inflate(R.layout.datetime_select_dialog, null);
        datetimeSelectDialog = new Dialog(relateAtivity, R.style.dialog_bg_transparent);
        datetimeSelectDialog.setContentView(datetimeSelectConctentView);
        datetimeSelectDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub

                tvDateTimeTitle = (TextView) datetimeSelectConctentView.findViewById(R.id.tvDateTimeTitle);
                tvDateTimeCurrent = (TextView) datetimeSelectConctentView.findViewById(R.id.tvDateTimeCurrent);

                mSelectDatePicker = (DatePicker) datetimeSelectConctentView.findViewById(R.id.mSelectDatePicker);
                mSelectTimePicker = (TimePicker) datetimeSelectConctentView.findViewById(R.id.mSelectTimePicker);
                layoutDatePicker = (LinearLayout) datetimeSelectConctentView.findViewById(R.id.layoutDatePicker);
                layoutTimePicker = (LinearLayout) datetimeSelectConctentView.findViewById(R.id.layoutTimePicker);

                btnDatetimeSelectCancel = (Button) datetimeSelectConctentView.findViewById(R.id.btnDatetimeSelectCancel);
                btnDatetimeSelectOK = (Button) datetimeSelectConctentView.findViewById(R.id.btnDatetimeSelectOK);

                btnDatetimeSelectOK.setOnClickListener(PlaybackFragment.this);
                btnDatetimeSelectCancel.setOnClickListener(PlaybackFragment.this);

                if (nDatetimeMode == DATETIME_MODE_DATE) {
                    tvDateTimeTitle.setText(R.string.lblDate2);
                    layoutDatePicker.setVisibility(View.VISIBLE);
                    layoutTimePicker.setVisibility(View.GONE);

                    mSelectDatePicker.init(nYear, nMonth, nDay, new DatePicker.OnDateChangedListener() {

                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            if ((mSelectDatePicker.getMonth() + 1) < 10 && mSelectDatePicker.getDayOfMonth() < 10) {
                                tvDateTimeCurrent.setText("" + mSelectDatePicker.getYear() + "-0" + (mSelectDatePicker.getMonth() + 1) + "-0" + mSelectDatePicker.getDayOfMonth());
                            } else if ((mSelectDatePicker.getMonth() + 1) >= 10 && mSelectDatePicker.getDayOfMonth() < 10) {
                                tvDateTimeCurrent.setText("" + mSelectDatePicker.getYear() + "-" + (mSelectDatePicker.getMonth() + 1) + "-0" + mSelectDatePicker.getDayOfMonth());
                            } else if ((mSelectDatePicker.getMonth() + 1) < 10 && mSelectDatePicker.getDayOfMonth() >= 10) {
                                tvDateTimeCurrent.setText("" + mSelectDatePicker.getYear() + "-0" + (mSelectDatePicker.getMonth() + 1) + "-" + mSelectDatePicker.getDayOfMonth());
                            } else {
                                tvDateTimeCurrent.setText("" + mSelectDatePicker.getYear() + "-" + (mSelectDatePicker.getMonth() + 1) + "-" + mSelectDatePicker.getDayOfMonth());
                            }

                        }
                    });

                    if ((mSelectDatePicker.getMonth() + 1) < 10 && mSelectDatePicker.getDayOfMonth() < 10) {
                        tvDateTimeCurrent.setText("" + mSelectDatePicker.getYear() + "-0" + (mSelectDatePicker.getMonth() + 1) + "-0" + mSelectDatePicker.getDayOfMonth());
                    } else if ((mSelectDatePicker.getMonth() + 1) >= 10 && mSelectDatePicker.getDayOfMonth() < 10) {
                        tvDateTimeCurrent.setText("" + mSelectDatePicker.getYear() + "-" + (mSelectDatePicker.getMonth() + 1) + "-0" + mSelectDatePicker.getDayOfMonth());
                    } else if ((mSelectDatePicker.getMonth() + 1) < 10 && mSelectDatePicker.getDayOfMonth() >= 10) {
                        tvDateTimeCurrent.setText("" + mSelectDatePicker.getYear() + "-0" + (mSelectDatePicker.getMonth() + 1) + "-" + mSelectDatePicker.getDayOfMonth());
                    } else {
                        tvDateTimeCurrent.setText("" + mSelectDatePicker.getYear() + "-" + (mSelectDatePicker.getMonth() + 1) + "-" + mSelectDatePicker.getDayOfMonth());
                    }

                } else if (nDatetimeMode == DATETIME_MODE_STARTTIME) {
                    tvDateTimeTitle.setText(R.string.lblStartTime2);
                    layoutDatePicker.setVisibility(View.GONE);
                    layoutTimePicker.setVisibility(View.VISIBLE);

                    mSelectTimePicker.setIs24HourView(true);
                    mSelectTimePicker.setCurrentHour((int) nStartHour);// 锟斤拷锟斤拷timePicker小时锟斤拷
                    mSelectTimePicker.setCurrentMinute((int) nStartMin); // 锟斤拷锟斤拷timePicker锟斤拷锟斤拷锟斤拷
                    mSelectTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            if (hourOfDay < 10 && minute < 10) {
                                tvDateTimeCurrent.setText("0" + hourOfDay + ":0" + minute);
                            } else if (hourOfDay >= 10 && minute < 10) {
                                tvDateTimeCurrent.setText("" + hourOfDay + ":0" + minute);
                            } else if (hourOfDay < 10 && minute >= 10) {
                                tvDateTimeCurrent.setText("0" + hourOfDay + ":" + minute);
                            } else {
                                tvDateTimeCurrent.setText("" + hourOfDay + ":" + minute);
                            }
                        }

                    });

                    if (mSelectTimePicker.getCurrentHour() < 10 && mSelectTimePicker.getCurrentMinute() < 10) {
                        tvDateTimeCurrent.setText("0" + mSelectTimePicker.getCurrentHour() + ":0" + mSelectTimePicker.getCurrentMinute());
                    } else if (mSelectTimePicker.getCurrentHour() >= 10 && mSelectTimePicker.getCurrentMinute() < 10) {
                        tvDateTimeCurrent.setText("" + mSelectTimePicker.getCurrentHour() + ":0" + mSelectTimePicker.getCurrentMinute());
                    } else if (mSelectTimePicker.getCurrentHour() < 10 && mSelectTimePicker.getCurrentMinute() >= 10) {
                        tvDateTimeCurrent.setText("0" + mSelectTimePicker.getCurrentHour() + ":" + mSelectTimePicker.getCurrentMinute());
                    } else {
                        tvDateTimeCurrent.setText("" + mSelectTimePicker.getCurrentHour() + ":" + mSelectTimePicker.getCurrentMinute());
                    }

                } else if (nDatetimeMode == DATETIME_MODE_ENDTIME) {
                    tvDateTimeTitle.setText(R.string.lblEndTime2);
                    layoutDatePicker.setVisibility(View.GONE);
                    layoutTimePicker.setVisibility(View.VISIBLE);

                    // @@System.out.println();// add for test
                    mSelectTimePicker.setIs24HourView(true);
                    mSelectTimePicker.setCurrentHour((int) nEndHour);// 锟斤拷锟斤拷timePicker小时锟斤拷
                    mSelectTimePicker.setCurrentMinute((int) nEndMin); // 锟斤拷锟斤拷timePicker锟斤拷锟斤拷锟斤拷
                    mSelectTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            // TODO Auto-generated method stub
                            if (hourOfDay < 10 && minute < 10) {
                                tvDateTimeCurrent.setText("0" + hourOfDay + ":0" + minute);
                            } else if (hourOfDay >= 10 && minute < 10) {
                                tvDateTimeCurrent.setText("" + hourOfDay + ":0" + minute);
                            } else if (hourOfDay < 10 && minute >= 10) {
                                tvDateTimeCurrent.setText("0" + hourOfDay + ":" + minute);
                            } else {
                                tvDateTimeCurrent.setText("" + hourOfDay + ":" + minute);
                            }
                        }

                    });

                    if (mSelectTimePicker.getCurrentHour() < 10 && mSelectTimePicker.getCurrentMinute() < 10) {
                        tvDateTimeCurrent.setText("0" + mSelectTimePicker.getCurrentHour() + ":0" + mSelectTimePicker.getCurrentMinute());
                    } else if (mSelectTimePicker.getCurrentHour() >= 10 && mSelectTimePicker.getCurrentMinute() < 10) {
                        tvDateTimeCurrent.setText("" + mSelectTimePicker.getCurrentHour() + ":0" + mSelectTimePicker.getCurrentMinute());
                    } else if (mSelectTimePicker.getCurrentHour() < 10 && mSelectTimePicker.getCurrentMinute() >= 10) {
                        tvDateTimeCurrent.setText("0" + mSelectTimePicker.getCurrentHour() + ":" + mSelectTimePicker.getCurrentMinute());
                    } else {
                        tvDateTimeCurrent.setText("" + mSelectTimePicker.getCurrentHour() + ":" + mSelectTimePicker.getCurrentMinute());
                    }
                }

            }

        });

        datetimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
            }

        });

    }

    private void createDialogsCloud() { // 锟斤拷锟斤拷锟斤拷陆时 锟斤拷锟斤拷锟斤拷锟斤拷曰锟斤拷锟�

        deviceSelectConctentView = LayoutInflater.from(relateAtivity).inflate(R.layout.devices_select_dialog, null);
        deviceSelectDialog = new Dialog(relateAtivity, R.style.selectorDialog);
        deviceSelectDialog.setContentView(deviceSelectConctentView);
        deviceSelectDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub
                btnDeviceSelectCancel = (Button) deviceSelectConctentView.findViewById(R.id.btnDeviceSelectCancel);

                btnDeviceSelectCancel.setOnClickListener(PlaybackFragment.this);

            }
        });

        deviceSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }

        });

        //
        datetimeSelectConctentViewCloud = LayoutInflater.from(relateAtivity).inflate(R.layout.datetime_cloud_select_dialog, null);
        datetimeSelectDialogCloud = new Dialog(relateAtivity, R.style.dialog_bg_transparent);
        datetimeSelectDialogCloud.setContentView(datetimeSelectConctentViewCloud);
        datetimeSelectDialogCloud.setOnShowListener(new DialogInterface.OnShowListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub

                tvDateTimeTitle = (TextView) datetimeSelectConctentViewCloud.findViewById(R.id.tvDateTimeTitleCloud);
                tvDateTimeCurrent = (TextView) datetimeSelectConctentViewCloud.findViewById(R.id.tvDateTimeCurrentCloud);

                mSelectDatePickerCloud = (DatePicker) datetimeSelectConctentViewCloud.findViewById(R.id.mSelectDatePickerCloud);
                mSelectTimePickerCloud = (TimePicker) datetimeSelectConctentViewCloud.findViewById(R.id.mSelectTimePickerCloud);
                layoutDatePickerCloud = (LinearLayout) datetimeSelectConctentViewCloud.findViewById(R.id.layoutDatePickerCloud);
                layoutTimePickerCloud = (LinearLayout) datetimeSelectConctentViewCloud.findViewById(R.id.layoutTimePickerCloud);

                btnDatetimeSelectCancel = (Button) datetimeSelectConctentViewCloud.findViewById(R.id.btnDatetimeSelectCancelCloud);
                btnDatetimeSelectOK = (Button) datetimeSelectConctentViewCloud.findViewById(R.id.btnDatetimeSelectOKCloud);

                btnDatetimeSelectOK.setOnClickListener(PlaybackFragment.this);
                btnDatetimeSelectCancel.setOnClickListener(PlaybackFragment.this);

                if (nDatetimeMode == DATETIME_MODE_DATE) {
                    tvDateTimeTitle.setText(R.string.lblDate2);
                    layoutDatePickerCloud.setVisibility(View.VISIBLE);
                    layoutTimePickerCloud.setVisibility(View.GONE);

                    mSelectDatePickerCloud.init(nYear_Cloud, nMonth_Cloud, nDay_Cloud, new DatePicker.OnDateChangedListener() {

                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            if ((mSelectDatePickerCloud.getMonth() + 1) < 10 && mSelectDatePickerCloud.getDayOfMonth() < 10) {
                                tvDateTimeCurrent.setText("" + mSelectDatePickerCloud.getYear() + "-0" + (mSelectDatePickerCloud.getMonth() + 1) + "-0" + mSelectDatePickerCloud.getDayOfMonth());
                            } else if ((mSelectDatePickerCloud.getMonth() + 1) >= 10 && mSelectDatePickerCloud.getDayOfMonth() < 10) {
                                tvDateTimeCurrent.setText("" + mSelectDatePickerCloud.getYear() + "-" + (mSelectDatePickerCloud.getMonth() + 1) + "-0" + mSelectDatePickerCloud.getDayOfMonth());
                            } else if ((mSelectDatePickerCloud.getMonth() + 1) < 10 && mSelectDatePickerCloud.getDayOfMonth() >= 10) {
                                tvDateTimeCurrent.setText("" + mSelectDatePickerCloud.getYear() + "-0" + (mSelectDatePickerCloud.getMonth() + 1) + "-" + mSelectDatePickerCloud.getDayOfMonth());
                            } else {
                                tvDateTimeCurrent.setText("" + mSelectDatePickerCloud.getYear() + "-" + (mSelectDatePickerCloud.getMonth() + 1) + "-" + mSelectDatePickerCloud.getDayOfMonth());
                            }

                        }
                    });

                    if ((mSelectDatePickerCloud.getMonth() + 1) < 10 && mSelectDatePickerCloud.getDayOfMonth() < 10) {
                        tvDateTimeCurrent.setText("" + mSelectDatePickerCloud.getYear() + "-0" + (mSelectDatePickerCloud.getMonth() + 1) + "-0" + mSelectDatePickerCloud.getDayOfMonth());
                    } else if ((mSelectDatePickerCloud.getMonth() + 1) >= 10 && mSelectDatePickerCloud.getDayOfMonth() < 10) {
                        tvDateTimeCurrent.setText("" + mSelectDatePickerCloud.getYear() + "-" + (mSelectDatePickerCloud.getMonth() + 1) + "-0" + mSelectDatePickerCloud.getDayOfMonth());
                    } else if ((mSelectDatePickerCloud.getMonth() + 1) < 10 && mSelectDatePickerCloud.getDayOfMonth() >= 10) {
                        tvDateTimeCurrent.setText("" + mSelectDatePickerCloud.getYear() + "-0" + (mSelectDatePickerCloud.getMonth() + 1) + "-" + mSelectDatePickerCloud.getDayOfMonth());
                    } else {
                        tvDateTimeCurrent.setText("" + mSelectDatePickerCloud.getYear() + "-" + (mSelectDatePickerCloud.getMonth() + 1) + "-" + mSelectDatePickerCloud.getDayOfMonth());
                    }

                } else if (nDatetimeMode == DATETIME_MODE_STARTTIME) {
                    tvDateTimeTitle.setText(R.string.lblStartTime2);
                    layoutDatePickerCloud.setVisibility(View.GONE);
                    layoutTimePickerCloud.setVisibility(View.VISIBLE);

                    mSelectTimePickerCloud.setIs24HourView(true);
                    mSelectTimePickerCloud.setCurrentHour((int) nStartHour);// 锟斤拷锟斤拷timePicker小时锟斤拷
                    mSelectTimePickerCloud.setCurrentMinute((int) nStartMin); // 锟斤拷锟斤拷timePicker锟斤拷锟斤拷锟斤拷
                    mSelectTimePickerCloud.setOnTimeChangedListener(new OnTimeChangedListener() {

                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            // TODO Auto-generated method stub
                            if (hourOfDay < 10 && minute < 10) {
                                tvDateTimeCurrent.setText("0" + hourOfDay + ":0" + minute);
                            } else if (hourOfDay >= 10 && minute < 10) {
                                tvDateTimeCurrent.setText("" + hourOfDay + ":0" + minute);
                            } else if (hourOfDay < 10 && minute >= 10) {
                                tvDateTimeCurrent.setText("0" + hourOfDay + ":" + minute);
                            } else {
                                tvDateTimeCurrent.setText("" + hourOfDay + ":" + minute);
                            }
                        }

                    });

                    if (mSelectTimePickerCloud.getCurrentHour() < 10 && mSelectTimePickerCloud.getCurrentMinute() < 10) {
                        tvDateTimeCurrent.setText("0" + mSelectTimePickerCloud.getCurrentHour() + ":0" + mSelectTimePickerCloud.getCurrentMinute());
                    } else if (mSelectTimePickerCloud.getCurrentHour() >= 10 && mSelectTimePickerCloud.getCurrentMinute() < 10) {
                        tvDateTimeCurrent.setText("" + mSelectTimePickerCloud.getCurrentHour() + ":0" + mSelectTimePickerCloud.getCurrentMinute());
                    } else if (mSelectTimePickerCloud.getCurrentHour() < 10 && mSelectTimePickerCloud.getCurrentMinute() >= 10) {
                        tvDateTimeCurrent.setText("0" + mSelectTimePickerCloud.getCurrentHour() + ":" + mSelectTimePickerCloud.getCurrentMinute());
                    } else {
                        tvDateTimeCurrent.setText("" + mSelectTimePickerCloud.getCurrentHour() + ":" + mSelectTimePickerCloud.getCurrentMinute());
                    }

                } else if (nDatetimeMode == DATETIME_MODE_ENDTIME) {
                    tvDateTimeTitle.setText(R.string.lblEndTime2);
                    layoutDatePickerCloud.setVisibility(View.GONE);
                    layoutTimePickerCloud.setVisibility(View.VISIBLE);

                    // @@System.out.println();// add for test
                    mSelectTimePickerCloud.setIs24HourView(true);
                    mSelectTimePickerCloud.setCurrentHour((int) nEndHour);// 锟斤拷锟斤拷timePicker小时锟斤拷
                    mSelectTimePickerCloud.setCurrentMinute((int) nEndMin); // 锟斤拷锟斤拷timePicker锟斤拷锟斤拷锟斤拷
                    mSelectTimePickerCloud.setOnTimeChangedListener(new OnTimeChangedListener() {

                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            // TODO Auto-generated method stub
                            if (hourOfDay < 10 && minute < 10) {
                                tvDateTimeCurrent.setText("0" + hourOfDay + ":0" + minute);
                            } else if (hourOfDay >= 10 && minute < 10) {
                                tvDateTimeCurrent.setText("" + hourOfDay + ":0" + minute);
                            } else if (hourOfDay < 10 && minute >= 10) {
                                tvDateTimeCurrent.setText("0" + hourOfDay + ":" + minute);
                            } else {
                                tvDateTimeCurrent.setText("" + hourOfDay + ":" + minute);
                            }
                        }

                    });

                    if (mSelectTimePickerCloud.getCurrentHour() < 10 && mSelectTimePickerCloud.getCurrentMinute() < 10) {
                        tvDateTimeCurrent.setText("0" + mSelectTimePickerCloud.getCurrentHour() + ":0" + mSelectTimePickerCloud.getCurrentMinute());
                    } else if (mSelectTimePickerCloud.getCurrentHour() >= 10 && mSelectTimePickerCloud.getCurrentMinute() < 10) {
                        tvDateTimeCurrent.setText("" + mSelectTimePickerCloud.getCurrentHour() + ":0" + mSelectTimePickerCloud.getCurrentMinute());
                    } else if (mSelectTimePickerCloud.getCurrentHour() < 10 && mSelectTimePickerCloud.getCurrentMinute() >= 10) {
                        tvDateTimeCurrent.setText("0" + mSelectTimePickerCloud.getCurrentHour() + ":" + mSelectTimePickerCloud.getCurrentMinute());
                    } else {
                        tvDateTimeCurrent.setText("" + mSelectTimePickerCloud.getCurrentHour() + ":" + mSelectTimePickerCloud.getCurrentMinute());
                    }
                }

            }

        });

        datetimeSelectDialogCloud.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }

        });

    }

    private Handler handler = new Handler() {

        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {

            // 录像搜索登录
            if (msg.arg1 == HANDLE_MSG_CODE_LOGIN_FOR_SEARCH_RECORD_FILES) {
                if (msg.arg2 == ResultCode.RESULT_CODE_SUCCESS) {
                    GetRecFileList();
                }
            }


            if (msg.arg1 == REC_FILE_FAIL) {
                bSearchType = false;
                llPlayerBackType.setVisibility(View.VISIBLE);
            }


            // add by lin 20151210
            if (msg.arg1 == Defines.HANDLE_MSG_CODE_RECORD_FILES_RECV) {
                bIsRecFileSearching = false;

                if (msg.arg2 == 0) {

                    if (recFileListView.getAdapter() != null) {
                        return;
                    }

                    isListVisible = false;
                    layoutSearchParam.setVisibility(View.VISIBLE);
                    layoutRecFileList.setVisibility(View.GONE);
                    LocalDefines.bIsBackPlay = false;

                    Toast toast = null;
                    toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticRecOKNOFiles), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    loadingDialog.dismiss();

                    Bundle data = msg.getData();
                    if (data == null) {
                        return;
                    }
                    SaveRecFileListToDatabase();
                } else {
                    nSearchFailCount = 0;
                    Bundle data = msg.getData();
                    if (data == null) {
                        return;
                    }
                    if (data.getParcelableArrayList(Defines.RECORD_FILE_RETURN_MESSAGE) != null) {
                        fileList = data.getParcelableArrayList(Defines.RECORD_FILE_RETURN_MESSAGE);
                    }

                    if (!isCloudFileList) {
                        if (fileList != null && fileList.size() > 0) {
                            SaveRecFileListToDatabase();
                        }
                        if (isActive) {
                            try {
                                refreshRecFileList();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (fileList != null && fileList.size() > 0) {
                            SaveRecFileListToDatabase();
                        }
                        refreshCloudRecFileList();
                    }

                    loadingDialog.dismiss();
                }
            }


            // 录像搜索结果
            if (msg.arg1 == Defines.HANDLE_MSG_CODE_GET_RECORD_FILES_END) {
                bIsRecFileSearching = false;

                loadingDialog.dismiss();

                Toast toast = null;

                switch (msg.arg2) {
                    case ResultCode.RESULT_CODE_SUCCESS:

                        if (fileList.size() <= 0) {
                            toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticRecOKNOFiles), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            if (isActive) {
                                try {
                                    refreshRecFileList();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ResultCode.RESULT_CODE_FAIL_SERVER_CONNECT_FAIL:
                        if (isActive) {
                            try {
                                toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticRecConnectFail), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    case LocalDefines.REC_FILE_SEARCH_RESULT_CODE_FAIL_VERIFY_FAILED:
                        if (isActive) {
                            try {
                                toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.notice_Result_VerifyFailed), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    case LocalDefines.REC_FILE_SEARCH_RESULT_CODE_FAIL_USER_NOEXIST:
                        if (isActive) {
                            try {
                                toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.notice_Result_UserNoExist), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    case ResultCode.RESULT_CODE_FAIL_PWD_ERROR:
                        if (isActive) {
                            try {
                                toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.notice_Result_PWDError), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    case LocalDefines.REC_FILE_SEARCH_RESULT_CODE_FAIL_NET_DOWN:

                        if (isActive) {
                            try {
                                toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticRecConnectFail), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    case LocalDefines.REC_FILE_SEARCH_RESULT_CODE_FAIL_NET_POOL:

                        if (isActive) {
                            try {
                                toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticRecConnectFail), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    case ResultCode.RESULT_CODE_SERVER_CONNECT:

                        if (isActive) {
                            try {
                                toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticRecConnectFail), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    default:
                        if (isActive) {
                            try {
                                toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticRecConnectFail), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }

                if (fileList.size() <= 0) {
                    isListVisible = false;
                    layoutSearchParam.setVisibility(View.VISIBLE);
                    layoutRecFileList.setVisibility(View.GONE);
                    LocalDefines.bIsBackPlay = false;

                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    };


    public void backPlayBack() {

        if (mRecFileDownloader != null && mRecFileDownloader.isDownloading() && mDLFileListPosition != -1 && mDLFilePath != null) {
            // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷氐锟斤拷锟斤拷锟斤拷虻锟饺凤拷隙曰锟斤拷锟�
            View viewDialog = View.inflate(getActivity(), R.layout.show_alert_dialog, null);
            TextView tv_title = (TextView) viewDialog.findViewById(R.id.tv_title);
            tv_title.setText(getString(R.string.str_rec_file_cancle1));
            TextView tv_content = (TextView) viewDialog.findViewById(R.id.tv_content);
            tv_content.setVisibility(View.GONE);
            new AlertDialog.Builder(getActivity()).setView(viewDialog).setNegativeButton(getString(R.string.alert_btn_NO), null).setPositiveButton(getString(R.string.alert_btn_YES), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    //boolean result = mRecFileDownloader.StopDownloadRecFile();
                    boolean result = mRecFileDownloader.stopDownloadRecordVideo();


                    final File file1 = new File(mDLFilePath);
                    if (file1.exists()) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                boolean deleteResult = file1.delete();
                            }
                        }).start();
                    }

                    mDLFileListPosition = -1;
                    mDLFilePath = null;

                    layoutSearchParam.setVisibility(View.VISIBLE);
                    layoutRecFileList.setVisibility(View.GONE);
                    LocalDefines.bIsBackPlay = false;

                }

            }).show();

        } else {
            layoutSearchParam.setVisibility(View.VISIBLE);
            layoutRecFileList.setVisibility(View.GONE);
            LocalDefines.bIsBackPlay = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.llSearchType:
                isSearchCloudRec = false;
                if (bSearchType) {
                    bSearchType = false;
                    llPlayerBackType_non_CN.setVisibility(View.VISIBLE);

                    ivPlayerBackType2.setImageResource(R.drawable.play_back_video_back_1);

                } else {

                    bSearchType = true;
                    llPlayerBackType_non_CN.setVisibility(View.GONE);
                    ivPlayerBackType2.setImageResource(R.drawable.play_back_video_back_2);

                }
                break;

            case R.id.btnSearch:
                break;

            case R.id.btnDeviceSelectBack_Cloud:
                layoutSearchParam.setVisibility(View.VISIBLE);
                ll_cloud_record.setVisibility(View.GONE);
                break;

            case R.id.ll_search_date:
                ShowDateSelectViewCloud();
                break;

            case R.id.ll_search_start_time:
                ShowStartTimeSelectViewCloud();
                break;

            case R.id.ll_search_end_time: // alter by mai 2015-1-26===
                ShowEndTimeSelectViewCloud();
                break;

            case R.id.ll_type_all:
                nSearchType = Defines.FILE_TYPE_ALL;
                isZh = LocalDefines.isZh(getActivity());

                ivTypeAll.setImageResource(R.drawable.radio_select_check);
                ivTypeAuto.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAlarm.setImageResource(R.drawable.radio_select_uncheck);
                break;

            case R.id.ll_type_all_non_CN:
                nSearchType = Defines.FILE_TYPE_ALL;
                tvPlayerBackType2.setText(getString(R.string.AllPlayBack));
                ivTypeAll_non_CN.setImageResource(R.drawable.radio_select_check);
                ivTypeAuto_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAlarm_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                break;

            case R.id.ll_type_auto:
                nSearchType = Defines.FILE_TYPE_NORMAL;
                isZh = LocalDefines.isZh(getActivity());

                ivTypeAll.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAuto.setImageResource(R.drawable.radio_select_check);
                ivTypeAlarm.setImageResource(R.drawable.radio_select_uncheck);
                break;

            case R.id.ll_type_auto_non_CN:
                nSearchType = Defines.FILE_TYPE_NORMAL;
                tvPlayerBackType2.setText(getString(R.string.record_auto_record_title));
                ivTypeAll_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAuto_non_CN.setImageResource(R.drawable.radio_select_check);
                ivTypeAlarm_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                break;

            case R.id.ll_type_alarm:
                nSearchType = Defines.FILE_TYPE_ALARM;
                isZh = LocalDefines.isZh(getActivity());

                ivTypeAll.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAuto.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAlarm.setImageResource(R.drawable.radio_select_check);
                break;

            case R.id.ll_type_alarm_non_CN:
                nSearchType = Defines.FILE_TYPE_ALARM;

                tvPlayerBackType2.setText(getString(R.string.strAlarm));

                ivTypeAll_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAuto_non_CN.setImageResource(R.drawable.radio_select_uncheck);
                ivTypeAlarm_non_CN.setImageResource(R.drawable.radio_select_check);
                break;

            case R.id.llChannels:
                if (openSearchChannel == true) {
                    ll_choose1_channel.setVisibility(View.VISIBLE);
                    ll_choose2_channel.setVisibility(View.VISIBLE);
                    iv_Channel.setImageResource(R.drawable.play_back_video_back_1);
                    openSearchChannel = false;
                } else {
                    ll_choose1_channel.setVisibility(View.GONE);
                    ll_choose2_channel.setVisibility(View.GONE);
                    iv_Channel.setImageResource(R.drawable.play_back_video_back_2);
                    openSearchChannel = true;
                }
                break;

            case R.id.ll_channel0:
                SearchChannel = 0;
                iv_channel0.setImageResource(R.drawable.radio_select_check);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                textChannel.setText("" + 1);
                break;

            case R.id.ll_channel1:
                SearchChannel = 1;
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_check);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                textChannel.setText("" + 2);
                break;

            case R.id.ll_channel2:
                SearchChannel = 2;
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_check);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                textChannel.setText("" + 3);
                break;

            case R.id.ll_channel3:
                SearchChannel = 3;
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_check);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                textChannel.setText("" + 4);
                break;

            case R.id.ll_channel4:
                SearchChannel = 4;
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_check);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                textChannel.setText("" + 5);
                break;

            case R.id.ll_channel5:
                SearchChannel = 5;
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_check);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                textChannel.setText("" + 6);
                break;

            case R.id.ll_channel6:
                SearchChannel = 6;
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_check);
                iv_channel7.setImageResource(R.drawable.radio_select_uncheck);
                textChannel.setText("" + 7);
                break;

            case R.id.ll_channel7:
                SearchChannel = 7;
                iv_channel0.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel1.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel2.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel3.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel4.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel5.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel6.setImageResource(R.drawable.radio_select_uncheck);
                iv_channel7.setImageResource(R.drawable.radio_select_check);
                textChannel.setText("" + 8);
                break;

            case R.id.btnDatetimeSelectOK: // 锟斤拷锟绞憋拷锟斤拷锟斤拷锟�

                switch (nDatetimeMode) {
                    case DATETIME_MODE_DATE:
                        nYear = (short) mSelectDatePicker.getYear();
                        nMonth = (short) mSelectDatePicker.getMonth();
                        nDay = (short) mSelectDatePicker.getDayOfMonth();

                        if (nMonth < 9 && nDay < 10) {
                            textViewDate.setText("" + nYear + "-0" + (nMonth + 1) + "-0" + nDay);
                        } else if (nMonth >= 9 && nDay < 10) {
                            textViewDate.setText("" + nYear + "-" + (nMonth + 1) + "-0" + nDay);
                        } else if (nMonth < 9 && nDay >= 10) {
                            textViewDate.setText("" + nYear + "-0" + (nMonth + 1) + "-" + nDay);
                        } else {
                            textViewDate.setText("" + nYear + "-" + (nMonth + 1) + "-" + nDay);
                        }
                        break;
                    case DATETIME_MODE_STARTTIME:

                        if ((mSelectTimePicker.getCurrentHour() > nEndHour) || (mSelectTimePicker.getCurrentHour() == nEndHour && mSelectTimePicker.getCurrentMinute() > nEndMin)) {

                            Toast toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticStartlargeThanEnd), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            nStartHour = (short) (int) (mSelectTimePicker.getCurrentHour());
                            nStartMin = (short) (int) (mSelectTimePicker.getCurrentMinute());
                            nStartSec = 0;

                            String strHour = "";
                            String strMinute = "";

                            if (nStartHour < 10) {
                                strHour = "0" + nStartHour;
                            } else {
                                strHour = "" + nStartHour;
                            }

                            if (nStartMin < 10) {
                                strMinute = "0" + nStartMin;
                            } else {
                                strMinute = "" + nStartMin;
                            }

                            textViewStartTime.setText(strHour + ":" + strMinute);
                        }

                        break;

                    case DATETIME_MODE_ENDTIME:

                        if ((mSelectTimePicker.getCurrentHour() < nStartHour) || (mSelectTimePicker.getCurrentHour() == nStartHour && mSelectTimePicker.getCurrentMinute() < nStartMin)) {

                            Toast toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticEndLessThanStart), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {

                            nEndHour = (short) (int) (mSelectTimePicker.getCurrentHour());
                            nEndMin = (short) (int) (mSelectTimePicker.getCurrentMinute());
                            nEndSec = 0;

                            String strHour = "";
                            String strMinute = "";

                            if (nEndHour < 10) {
                                strHour = "0" + nEndHour;
                            } else {
                                strHour = "" + nEndHour;
                            }

                            if (nEndMin < 10) {
                                strMinute = "0" + nEndMin;
                            } else {
                                strMinute = "" + nEndMin;
                            }
                            textViewEndTime.setText(strHour + ":" + strMinute);
                        }

                        break;

                }

                nDatetimeMode = DATETIME_MODE_UNDEFINE;
                if (datetimeSelectDialog != null) {
                    if (datetimeSelectDialog.isShowing()) {
                        datetimeSelectDialog.dismiss();
                    }
                }
                break;

            case R.id.btnDatetimeSelectCancel: // 时锟斤拷锟斤拷锟节凤拷锟斤拷

                nDatetimeMode = DATETIME_MODE_UNDEFINE;
                if (datetimeSelectDialog != null) {
                    if (datetimeSelectDialog.isShowing()) {
                        datetimeSelectDialog.dismiss();
                    }
                }
                break;

            case R.id.btnDeviceSelectCancel: // 锟借备锟斤拷锟斤拷
                if (deviceSelectDialog != null) {
                    if (deviceSelectDialog.isShowing()) {
                        deviceSelectDialog.dismiss();
                    }
                }
                break;

            case R.id.btnListVisible: // 锟斤拷示锟借备锟叫憋拷

                if (LocalDefines._severInfoListData != null && LocalDefines._severInfoListData.size() > 0) {
                    isListVisible = true;
                    layoutSearchParam.setVisibility(View.GONE);
                    layoutRecFileList.setVisibility(View.VISIBLE);

                    //((HomePageActivity) relateAtivity).setGuideBarVisible(false);
                    GetRecFileListFromDatabase(); // 锟斤拷取锟斤拷菘锟斤拷锟斤拷锟斤拷募锟斤拷锟斤拷
                } else {
                    Toast.makeText(getActivity(), getString(R.string.noDevice), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnDeviceSelectBack:
                isListVisible = false;
                backPlayBack();
                break;


            // 搜索文件
            case R.id.btnStartSearch:

                if (!isSearchCloudRec) {
                    isCloudFileList = false;

                    fileList.clear();

                    if (LocalDefines._severInfoListData != null && LocalDefines._severInfoListData.size() > 0) {

                        if (LocalDefines._PlaybackListviewSelectedPosition >= 0 && LocalDefines._PlaybackListviewSelectedPosition < LocalDefines._severInfoListData.size()) {
                            isListVisible = true;
                            layoutSearchParam.setVisibility(View.GONE);
                            layoutRecFileList.setVisibility(View.VISIBLE);

                            bIsRecFileSearching = true;

                            fileList.clear();
                            recFileListView.setAdapter(null);

                            mLoadType = TYPE_LOADING;
                            loadingDialog.show();

                            recordSearchLogin();
                        } else {
                            Toast toast = Toast.makeText(relateAtivity.getApplicationContext(), getString(R.string.noticNoDeviceSelect), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.noDevice), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    isCloudFileList = true;
                    isListVisible = true;
                    layoutSearchParam.setVisibility(View.GONE);
                    layoutRecFileList.setVisibility(View.VISIBLE);

                    GetCloudRecFileList();
                }
                break;

            case R.id.layoutDevice:

                if (LocalDefines._severInfoListData != null && LocalDefines._severInfoListData.size() > 0) {
                    bSearchType = true;

                    popupListView.showAsDropDown(layoutDevice, 0, -10);

                    btnListVisible.setVisibility(View.INVISIBLE);

                    updateServerListView();
                } else {

                    Toast.makeText(getActivity(), getString(R.string.noDevice), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.llCloudRec:
                bSearchType = true;
                llPlayerBackType.setVisibility(View.GONE);
                ivPlayerBackType.setImageResource(R.drawable.play_back_video_back_2);

                isZh = LocalDefines.isZh(getActivity());
                if (isZh) {
//                    if (HomePageActivity.AppMode == 1) {
//
//                        if (deviceInfo == null) {
//                            return;
//                        }
//                        if (deviceInfo.getnProductId() > 0) {
//                            isSearchCloudRec = true;
//                            tvTFVideo.setTextColor(getResources().getColor(R.color.font_color_gray));
//                            tvCloudVideo.setTextColor(getResources().getColor(R.color.font_color_sky_blue2));
//                        } else if (deviceInfo.getnProductId() == 0) {
//                            Toast.makeText(getActivity(), getString(R.string.str_bind_to_use), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getActivity(), getString(R.string.str_bind_to_use), Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), getString(R.string.str_please_login_to_use), Toast.LENGTH_SHORT).show();
//                    }
                }
                break;

            case R.id.llTFCardRec:
                isSearchCloudRec = false;
                tvTFVideo.setTextColor(getResources().getColor(R.color.font_color_sky_blue2));
                tvCloudVideo.setTextColor(getResources().getColor(R.color.font_color_gray));
                if (bSearchType) {
                    bSearchType = false;
                    llPlayerBackType.setVisibility(View.VISIBLE);
                    ivPlayerBackType.setImageResource(R.drawable.play_back_video_back_1);
                } else {
                    bSearchType = true;
                    llPlayerBackType.setVisibility(View.GONE);
                    ivPlayerBackType.setImageResource(R.drawable.play_back_video_back_2);
                }
                break;

            case R.id.ivNvplayerBack: // 锟斤拷锟脚凤拷锟斤拷

                if (popupListView != null && popupListView.isShowing()) {
                    popupListView.dismiss();
                    bSearchType = true;
                    btnListVisible.setVisibility(View.VISIBLE);
                } else {
                    getActivity().finish();
                }

                break;
            // =====end add by mai 2015-1-26========
            case R.id.layoutSearchDate:
                ShowDateSelectView();
                break;
            case R.id.layoutSearchEndTime: // alter by mai 2015-1-26=====
                ShowEndTimeSelectView();
                break;
            case R.id.layoutSearchStartTime: // alter by mai 2015-1-26===
                ShowStartTimeSelectView();
                break;
        }

    }

    /**
     * 录像搜索登录
     */
    private void recordSearchLogin() {
        LoginParam loginParam = new LoginParam();
        loginParam.setDeviceInfo(LocalDefines._severInfoListData.get(LocalDefines._PlaybackListviewSelectedPosition));
        loginParam.setConnectType(Defines.LOGIN_FOR_PLAYBACK);

        int loginResult = LoginHelper.loginDevice(getContext(), loginParam, new ILoginDeviceCallback() {
            @Override
            public void onLogin(LoginHandle loginHandle) {
                if (loginHandle != null) {
                    if (loginHandle.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                        // 登录成功
                        _deviceParam = loginHandle;

                        Message msg = handler.obtainMessage();
                        msg = handler.obtainMessage();
                        msg.arg1 = HANDLE_MSG_CODE_LOGIN_FOR_SEARCH_RECORD_FILES;
                        msg.arg2 = ResultCode.RESULT_CODE_SUCCESS;
                        handler.sendMessage(msg);
                    } else {
                        // 登录失败，查看具体错误码
                        Message msg = handler.obtainMessage();
                        msg = handler.obtainMessage();
                        msg.arg1 = Defines.HANDLE_MSG_CODE_GET_RECORD_FILES_END;
                        msg.arg2 = loginHandle.getnResult();
                        handler.sendMessage(msg);
                    }
                } else {
                    // 登录失败
                    Message msg = handler.obtainMessage();
                    msg = handler.obtainMessage();
                    msg.arg1 = Defines.HANDLE_MSG_CODE_GET_RECORD_FILES_END;
                    msg.arg2 = ResultCode.RESULT_CODE_FAIL_COMMUNICATE_FAIL;
                    handler.sendMessage(msg);
                }
            }
        });

        if (loginResult != 0) {
            Message msg = handler.obtainMessage();
            msg = handler.obtainMessage();
            msg.arg1 = Defines.HANDLE_MSG_CODE_GET_RECORD_FILES_END;
            msg.arg2 = ResultCode.RESULT_CODE_FAIL_COMMUNICATE_FAIL;
            handler.sendMessage(msg);
        }
    }


    // 录锟斤拷锟叫憋拷
    public void ShowRecFileList() {
        if (layoutSearchParam != null) {
            layoutSearchParam.setVisibility(View.GONE);
        }

        if (layoutRecFileList != null) {
            layoutRecFileList.setVisibility(View.VISIBLE);
        }

    }

    public void HideRecFileList() {

        if (layoutSearchParam != null) {
            layoutSearchParam.setVisibility(View.VISIBLE);
        }

        if (layoutRecFileList != null) {
            layoutRecFileList.setVisibility(View.GONE);
        }
    }

    public boolean isListVisible() {
        return isListVisible;
    }

    public void setListVisible(boolean isListVisible) {
        this.isListVisible = isListVisible;
        try {
            if (this.isListVisible) {
                ShowRecFileList();
            } else {
                HideRecFileList();
            }
        } catch (Exception e) {

        }

    }

    // 锟斤拷示锟斤拷锟斤拷选锟斤拷
    private void ShowDateSelectView() {
        nDatetimeMode = DATETIME_MODE_DATE;
        datetimeSelectDialog.show();
    }

    private void ShowDateSelectViewCloud() {
        nDatetimeMode = DATETIME_MODE_DATE;
        datetimeSelectDialogCloud.show();
    }

    // 锟斤拷示锟斤拷始时锟斤拷选锟斤拷
    private void ShowStartTimeSelectView() {
        nDatetimeMode = DATETIME_MODE_STARTTIME;
        datetimeSelectDialog.show();
    }

    private void ShowStartTimeSelectViewCloud() {
        nDatetimeMode = DATETIME_MODE_STARTTIME;
        datetimeSelectDialogCloud.show();
    }

    // 锟斤拷锟斤拷时锟斤拷选锟斤拷
    private void ShowEndTimeSelectView() {
        nDatetimeMode = DATETIME_MODE_ENDTIME;
        datetimeSelectDialog.show();
    }

    private void ShowEndTimeSelectViewCloud() {
        nDatetimeMode = DATETIME_MODE_ENDTIME;
        datetimeSelectDialogCloud.show();
    }

    /**
     * 录像列表排序
     *
     * @param list
     */
    private void sortRecFile(List<RecordVideoInfo> list) {
        Collections.sort(list, new Comparator<RecordVideoInfo>() {

            @Override
            public int compare(RecordVideoInfo lhs, RecordVideoInfo rhs) {
                int result = lhs.getnFileID() - rhs.getnFileID(); // 利用FileID进行排序
                if (result < 0) {
                    return -1;
                } else if (result > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * 刷新录像文件列表
     */
    public void refreshRecFileList() {

        int nFileSize = 0;
        double fFileSize = 0.0;
        double nTimeLen = 0;

        String strInfo = "";
        String strSize = null;
        String strStartTime = null;
        String strTimeLen = null;

        if (fileList.size() > 0) {
            sortRecFile(fileList);
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

            boolean isNewDevice = false;
            if (_deviceParam.getVersion() >= 3) {
                isNewDevice = true;
            }

            RecordVideoInfo fileInfo = null;
            for (int i = 0; i < fileList.size(); i++) {

                if (isNewDevice) {
                    fileList.get(i).setuStartHour(fileList.get(i).getRecSegment().getNvtStartTime().getuHour());
                    fileList.get(i).setuStartMin(fileList.get(i).getRecSegment().getNvtStartTime().getuMin());
                    fileList.get(i).setuStartSec(fileList.get(i).getRecSegment().getNvtStartTime().getuSec());
                    fileList.get(i).setuEndHour(fileList.get(i).getRecSegment().getNvtEndTime().getuHour());
                    fileList.get(i).setuEndMin(fileList.get(i).getRecSegment().getNvtEndTime().getuMin());
                    fileList.get(i).setuEndSec(fileList.get(i).getRecSegment().getNvtEndTime().getuSec());
                    fileList.get(i).setuFileTimeLen(fileList.get(i).getRecSegment().getnEndTime() - fileList.get(i).getRecSegment().getnStartTime());
                }


                fileInfo = fileList.get(i);

                Log.i("demo_test", "nFileID = " + fileInfo.getnFileID());
                Log.i("demo_test", "fileName = " + fileInfo.getStrFileName());
                Log.i("demo_test", "FileSize = " + fileInfo.getnFileSize());
                Log.i("demo_test", "nFileState = " + fileInfo.getnFileState());
                Log.i("demo_test", "nFileDownloadProgress = " + fileInfo.getnFileDownloadProgress());
                Log.i("demo_test", "SegmentID = " + fileInfo.getRecSegment().getnSegmentID());
                Log.i("demo_test", "nType = " + fileInfo.getRecSegment().getnType());
                Log.i("demo_test", "NvtStartTime = " + fileInfo.getRecSegment().getNvtStartTime());
                Log.i("demo_test", "NvtEndTime = " + fileInfo.getRecSegment().getNvtEndTime());
                Log.i("demo_test", "nStartTime = " + fileInfo.getRecSegment().getnStartTime());
                Log.i("demo_test", "nEndTime = " + fileInfo.getRecSegment().getnEndTime());


                HashMap<String, Object> map = new HashMap<String, Object>();

                map.put("ItemTitleName", R.id.ItemFileName);
                map.put("ItemTitleInfo", R.id.ItemFileInfo);

                nFileSize = fileInfo.getnFileSize();

                strSize = getString(R.string.strFileSize);
                if (nFileSize > 1024000) {
                    fFileSize = nFileSize / 1048576.0;
                    if (fFileSize >= 100) {
                        strSize = strSize + String.format("%.0f", fFileSize) + " MB";
                    } else if (fFileSize >= 1) {
                        strSize = strSize + String.format("%.1f", fFileSize) + " MB";
                    } else {
                        strSize = strSize + String.format("%.2f", fFileSize) + " MB";
                    }

                } else if (nFileSize > 1024) {
                    fFileSize = nFileSize / 1024.0;
                    strSize = strSize + String.format("%.0f", fFileSize) + " KB";
                } else {
                    strSize = strSize + nFileSize + " B";
                }

                strStartTime = getString(R.string.strStartTime);

                if (fileInfo.getuStartHour() >= 10) {
                    strStartTime = strStartTime + fileInfo.getuStartHour();
                } else {
                    strStartTime = strStartTime + "0" + fileInfo.getuStartHour();
                }

                if (fileInfo.getuStartMin() >= 10) {
                    strStartTime = strStartTime + ":" + fileInfo.getuStartMin();
                } else {
                    strStartTime = strStartTime + ":0" + fileInfo.getuStartMin();
                }

                if (fileInfo.getuStartSec() >= 10) {
                    strStartTime = strStartTime + ":" + fileInfo.getuStartSec();
                } else {
                    strStartTime = strStartTime + ":0" + fileInfo.getuStartSec();
                }

                strTimeLen = getString(R.string.strTimeLen);
                if (fileInfo.getuFileTimeLen() >= 3600) {
                    nTimeLen = fileInfo.getuFileTimeLen() / 3600.0;
                    strTimeLen = strTimeLen + String.format("%.1f", nTimeLen) + getString(R.string.strHour);
                } else if (fileInfo.getuFileTimeLen() >= 60) {
                    nTimeLen = fileInfo.getuFileTimeLen() / 60.0;
                    strTimeLen = strTimeLen + String.format("%.1f", nTimeLen) + getString(R.string.strMin);
                } else {
                    nTimeLen = fileInfo.getuFileTimeLen();
                    strTimeLen = strTimeLen + String.format("%.0f", nTimeLen) + getString(R.string.strSec);
                }

                strInfo = strStartTime + " " + strTimeLen + " " + strSize;

                map.put("FileInfo", strInfo);
                map.put("FileStartTime", strStartTime);
                map.put("FileSize", strSize);
                map.put("FileTimeLen", strTimeLen);
                map.put("FileName", fileInfo.getStrFileName());
                map.put("FileDownloadState", fileInfo.getnFileState());
                map.put("FileDownloadProgress", fileInfo.getnFileDownloadProgress());

                listItem.add(map);
            }

            RecFileListViewAdapter recFileListItemAdapter = new RecFileListViewAdapter(relateAtivity, listItem, R.layout.recfile_list_item,
                    new String[]{"ItemTitleName", "ItemTitleInfo", "ItemSize", "ItemTimeLen", "ItemDownload", "ItemDownloadState", "ItemDownloadPro", "ItemDownloadBar"},
                    new int[]{R.id.ItemFileName, R.id.ItemFileInfo, R.id.tvSize, R.id.tvTimeLen, R.id.download_layout, R.id.recording_download_txt, R.id.tv_progress, R.id.pb_download});

            if (recFileListView == null) {
                recFileListView = (ListView) contentView.findViewById(R.id.recfile_list);
            }
            recFileListView.setAdapter(recFileListItemAdapter);
            recFileListView.setSelection(LocalDefines._PlaybackRecListviewFirstPosition);

        } else {
            recFileListView.setAdapter(null);
        }

    }


    /**
     * 录像文件列表适配器
     */
    private class RecFileListViewAdapter extends BaseAdapter {

        private class ItemViewHolder {
            TextView tvName;
            TextView tvInfo;
            TextView tvSize; // add by mai 2015-1-26
            TextView tvTimeLen; // add by mai 2015-1-26
            RelativeLayout rlDownload;

            TextView tvDownloadState;
            TextView tvDownloadPro;
            ProgressBar pbDownloadBar;
        }

        private ArrayList<HashMap<String, Object>> mAppList;
        private LayoutInflater mInflater;
        private Context mContext;
        private String[] keyString;
        private int[] valueViewID;
        private ItemViewHolder holder;

        public RecFileListViewAdapter(Context context, ArrayList<HashMap<String, Object>> appList, int resource, String[] from, int[] to) {
            mContext = context;
            mAppList = appList;
            keyString = new String[from.length];
            valueViewID = new int[to.length];

            System.arraycopy(from, 0, keyString, 0, from.length);
            System.arraycopy(to, 0, valueViewID, 0, to.length);

            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private void downloadFile(final int position) {

            // 普通状态或下载出错
            String strSDCardPath = Functions.GetSDPath();

            if (strSDCardPath == null) {
                Toast.makeText(getActivity(), getString(R.string.noticeSDCardNotExist), Toast.LENGTH_SHORT).show();
                return;
            }

            RecordVideoInfo recFile = fileList.get(position);

            StatFs sf = new StatFs(strSDCardPath);
            int fileSize = recFile.getnFileSize();

            boolean isStorageEnough = sf.getAvailableBytes() - fileSize > 0;

            if (isStorageEnough) {
                String strSavePath = strSDCardPath + File.separator + LocalDefines.SDCardPath;
                File fileDir = new File(strSavePath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                String fileName = recFile.getStrFileName();
                //String strFilePath = strSavePath + File.separator + deviceInfo.getnDevID() + "_" + fileName.substring(0, fileName.length() - 4) + ".mp4";
                String strFilePath = strSavePath + File.separator + deviceInfo.getnDevID() + "_" + fileName + ".mp4";
                final File file = new File(strFilePath);
                if (file.exists()) {
                    Toast.makeText(getActivity(), getString(R.string.str_rec_file_exist), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mRecFileDownloader != null && mRecFileDownloader.isDownloading() && mDLFileListPosition != -1 && mDLFilePath != null) {
                    mRecFileDownloader.stopDownloadRecordVideo();
                    mAppList.get(mDLFileListPosition).put("FileDownloadState", FILE_DOWNLOAD_STATE_NORMAL);
                    mAppList.get(mDLFileListPosition).put("FileDownloadProgress", 0);
                    final File file1 = new File(mDLFilePath);
                    if (file1.exists()) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                file1.delete();
                            }
                        }).start();
                    }
                    mDLFileListPosition = -1;
                    mDLFilePath = null;
                }

                final RecordVideoDownloader recFileDownloader = new RecordVideoDownloader();


                boolean startDownloadResult = recFileDownloader.startDownloadRecordVideo(LocalDefines.Device_LoginHandle, recFile, strFilePath, new IDownloadCallback() {

                    @Override
                    public void onDownloadProcess(Object o, int state, int progress) {

                        mAppList.get(position).put("FileDownloadState", state);
                        mAppList.get(position).put("FileDownloadProgress", progress);

                        if (state == FILE_DOWNLOAD_STATE_SUCCESS || progress == 100) {

                            boolean stopResult = recFileDownloader.stopDownloadRecordVideo();

                            mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_SUCCESS);
                            mAppList.get(position).put("FileDownloadProgress", 0);

                        } else if (state == FILE_DOWNLOAD_STATE_ERROR) {
                            boolean stopResult = recFileDownloader.stopDownloadRecordVideo();

                            mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_ERROR);
                            mAppList.get(position).put("FileDownloadProgress", 0);

                            if (file.exists()) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        file.delete();
                                    }
                                }).start();
                            }
                        }

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (recFileListView.getAdapter() != null) {
                                    ((RecFileListViewAdapter) (recFileListView.getAdapter())).notifyDataSetChanged();
                                }
                            }

                        });

                    }
                });


//                boolean startDownloadResult = recFileDownloader.StartDownloadRecFile(new IDownloadCallback() {
//
//                    @Override
//                    public void onDownloadProcess(Object arg0, final int state, final int progress) {
//
//                        mAppList.get(position).put("FileDownloadState", state);
//                        mAppList.get(position).put("FileDownloadProgress", progress);
//
//                        if (state == FILE_DOWNLOAD_STATE_SUCCESS || progress == 100) {
//                            boolean stopResult = recFileDownloader.StopDownloadRecFile();
//
//                            mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_SUCCESS);
//                            mAppList.get(position).put("FileDownloadProgress", 0);
//
//                        } else if (state == FILE_DOWNLOAD_STATE_ERROR) {
//                            boolean stopResult = recFileDownloader.StopDownloadRecFile();
//
//                            mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_ERROR);
//                            mAppList.get(position).put("FileDownloadProgress", 0);
//
//                            if (file.exists()) {
//                                new Thread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        file.delete();
//                                    }
//                                }).start();
//                            }
//                        }
//
//                        getActivity().runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                if (recFileListView.getAdapter() != null) {
//                                    ((RecFileListViewAdapter) (recFileListView.getAdapter())).notifyDataSetChanged();
//                                }
//                            }
//
//                        });
//
//                    }
//                }, strFilePath, LocalDefines.Device_LoginHandle, recFile);

                if (startDownloadResult) {
                    mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_CONNECTING); // 连接中
                    mDLFileListPosition = position;
                    mDLFilePath = strFilePath;
                    mRecFileDownloader = recFileDownloader;
                    if (recFileListView.getAdapter() != null) {
                        ((RecFileListViewAdapter) (recFileListView.getAdapter())).notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.downFail), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.str_storage_not_enough), Toast.LENGTH_SHORT).show();
                return;
            }

        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView != null) {
                holder = (ItemViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.recfile_list_item, null);
                holder = new ItemViewHolder();
                convertView.setTag(holder);
            }

            holder.tvName = (TextView) convertView.findViewById(valueViewID[0]);
            holder.tvInfo = (TextView) convertView.findViewById(valueViewID[1]);
            holder.tvSize = (TextView) convertView.findViewById(valueViewID[2]);
            holder.tvTimeLen = (TextView) convertView.findViewById(valueViewID[3]);
            holder.rlDownload = (RelativeLayout) convertView.findViewById(valueViewID[4]);
            holder.tvDownloadState = (TextView) convertView.findViewById(valueViewID[5]);
            holder.tvDownloadPro = (TextView) convertView.findViewById(valueViewID[6]);
            holder.pbDownloadBar = (ProgressBar) convertView.findViewById(valueViewID[7]);

            HashMap<String, Object> map = mAppList.get(position);

            if (map != null) {
                String startTime = (String) map.get("FileStartTime");
                String strTimeLen = (String) map.get("FileTimeLen");
                String strSize = (String) map.get("FileSize");
                String info = (String) map.get("FileName");
                int fileDownloadState = (Integer) map.get("FileDownloadState");
                int fileDownloadProgress = (Integer) map.get("FileDownloadProgress");

                holder.tvName.setText(startTime);
                holder.tvTimeLen.setText(strTimeLen);
                holder.tvSize.setText(strSize);
                holder.tvInfo.setText(info);

                if (fileDownloadState == FILE_DOWNLOAD_STATE_NORMAL) {
                    // 2 普通状态下载
                    holder.tvDownloadState.setText(getString(R.string.showDown));
                    holder.tvDownloadPro.setVisibility(View.INVISIBLE);
                    holder.pbDownloadBar.setVisibility(View.INVISIBLE);
                } else if (fileDownloadState == FILE_DOWNLOAD_STATE_SUCCESS) {
                    // 1 下载完成
                    holder.tvDownloadState.setText(getString(R.string.str_rec_file_download_finish));
                    holder.tvDownloadPro.setVisibility(View.VISIBLE);
                    holder.pbDownloadBar.setVisibility(View.VISIBLE);
                    holder.tvDownloadPro.setText(100 + "%");
                    holder.pbDownloadBar.setProgress(100);
                } else if (fileDownloadState == FILE_DOWNLOAD_STATE_DOWNLOADING) {
                    // 0 正在下载
                    holder.tvDownloadState.setText(R.string.str_rec_file_stop_download);
                    holder.tvDownloadPro.setVisibility(View.VISIBLE);
                    holder.pbDownloadBar.setVisibility(View.VISIBLE);
                    holder.tvDownloadPro.setText(fileDownloadProgress + "%");
                    holder.pbDownloadBar.setProgress(fileDownloadProgress);
                } else if (fileDownloadState == FILE_DOWNLOAD_STATE_ERROR) {
                    // -1 下载出错重新下载
                    holder.tvDownloadState.setText(getString(R.string.str_rec_file_redownload));
                    holder.tvDownloadPro.setVisibility(View.VISIBLE);
                    holder.pbDownloadBar.setVisibility(View.VISIBLE);
                    holder.tvDownloadPro.setText(fileDownloadProgress + "%");
                    holder.pbDownloadBar.setProgress(fileDownloadProgress);
                } else if (fileDownloadState == FILE_DOWNLOAD_STATE_CONNECTING) {
                    // -2  连接中
                    holder.tvDownloadState.setText(getString(R.string.str_rec_file_download_connecting));
                    holder.tvDownloadPro.setVisibility(View.INVISIBLE);
                    holder.pbDownloadBar.setVisibility(View.INVISIBLE);
                }

            }

            holder.rlDownload.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {

                    int fileDownloadState = (Integer) mAppList.get(position).get("FileDownloadState");

                    if (fileDownloadState == FILE_DOWNLOAD_STATE_NORMAL
                            || fileDownloadState == FILE_DOWNLOAD_STATE_ERROR
                            || fileDownloadState == FILE_DOWNLOAD_STATE_SUCCESS) {

                        downloadFile(position);

                    } else if (fileDownloadState == FILE_DOWNLOAD_STATE_DOWNLOADING
                            || fileDownloadState == FILE_DOWNLOAD_STATE_CONNECTING) {

                        View viewDialog = View.inflate(getActivity(), R.layout.show_alert_dialog, null);

                        TextView tv_title = (TextView) viewDialog.findViewById(R.id.tv_title);
                        TextView tv_content = (TextView) viewDialog.findViewById(R.id.tv_content);

                        tv_title.setText(getString(R.string.str_rec_file_cancle1));

                        tv_content.setVisibility(View.GONE);

                        new AlertDialog.Builder(getActivity())
                                .setView(viewDialog)
                                .setNegativeButton(getString(R.string.alert_btn_NO), null)
                                .setPositiveButton(getString(R.string.alert_btn_YES),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                mRecFileDownloader.stopDownloadRecordVideo();

                                                mAppList.get(position).put("FileDownloadState", 2);
                                                mAppList.get(position).put("FileDownloadProgress", 0);

                                                if (recFileListView.getAdapter() != null) {
                                                    ((RecFileListViewAdapter) (recFileListView.getAdapter())).notifyDataSetChanged();
                                                }

                                                final File file = new File(mDLFilePath);

                                                if (file.exists()) {
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            file.delete();
                                                        }
                                                    }).start();
                                                }
                                            }

                                        })
                                .show();
                    }

                }

            });

            return convertView;
        }

    }


    private int nThreadID = 0;
    private int m_nThreadID = 0;

    /**
     * 录像文件搜索线程
     */
    public class RecFileSearcher extends Thread {

        private int m_nSearchID = 0;
        private int nThreadID = 0;
        private DeviceInfo info = null;// add by lin 2015-12-10

        public RecFileSearcher(DeviceInfo info, int nSearchID, int nThreadID) {
            this.m_nSearchID = nSearchID;
            this.info = info;
            this.nThreadID = nThreadID;
        }

        public void run() {

            m_nLoginExID++;

            LocalDefines.Device_LoginHandle = _deviceParam;

            LocalDefines._nDeviceID = info.getnDevID();
            LocalDefines._strUserName = info.getStrUsername();
            LocalDefines._strUserName = info.getStrPassword();
            LocalDefines._isMRMode = _deviceParam.isMRMode();
            LocalDefines._lHandle = _deviceParam.getlHandle();

            // add by lin 20151210
            if (m_nThreadID == nThreadID) {

                RecordFileParam recordFileParam = new RecordFileParam();
                recordFileParam.setSearchChn(SearchChannel);
                recordFileParam.setSearchType(nSearchType);
                recordFileParam.setSearchYear(nYear);
                recordFileParam.setSearchMonth(nMonth);
                recordFileParam.setSearchDay(nDay);
                recordFileParam.setSearchStartHour(nStartHour);
                recordFileParam.setSearchStartMin(nStartMin);
                recordFileParam.setSearchStartSec(nStartSec);
                recordFileParam.setSearchEndHour(nEndHour);
                recordFileParam.setSearchEndMin(nEndMin);
                recordFileParam.setSearchEndSec(nEndSec);

                int recordFileResult = RecordFileHelper.getRecordVideoInTFCard(_deviceParam, recordFileParam, new IRecFileCallback() {
                    @Override
                    public void onReceiveFile(int totalFileCount, int fileCount, ArrayList<RecordVideoInfo> arrayList) {
                        if (arrayList != null) {
                            Log.i("demo_test", "recordFile size = " + arrayList.size());

                            if (m_nThreadID == nThreadID) {
                                if (_deviceParam.getVersion() >= 3) {
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        arrayList.get(i).setuStartHour(arrayList.get(i).getRecSegment().getNvtStartTime().getuHour());
                                        arrayList.get(i).setuStartMin(arrayList.get(i).getRecSegment().getNvtStartTime().getuMin());
                                        arrayList.get(i).setuStartSec(arrayList.get(i).getRecSegment().getNvtStartTime().getuSec());
                                        arrayList.get(i).setuEndHour(arrayList.get(i).getRecSegment().getNvtEndTime().getuHour());
                                        arrayList.get(i).setuEndMin(arrayList.get(i).getRecSegment().getNvtEndTime().getuMin());
                                        arrayList.get(i).setuEndSec(arrayList.get(i).getRecSegment().getNvtEndTime().getuSec());
                                        arrayList.get(i).setuFileTimeLen(arrayList.get(i).getRecSegment().getnEndTime() - arrayList.get(i).getRecSegment().getnStartTime());
                                    }
                                }
                                fileList.addAll(arrayList);
                            }
                        }
                    }
                });

                Message msg = handler.obtainMessage();
                msg = handler.obtainMessage();
                msg.arg1 = Defines.HANDLE_MSG_CODE_GET_RECORD_FILES_END;
                msg.arg2 = recordFileResult;
                handler.sendMessage(msg);

            }
        }
    }

    public void GetRecFileListFromDatabase() {
        LocalDefines.bIsBackPlay = true;
        fileList.clear();

        if (!isCloudFileList) {
            refreshRecFileList();
        } else {
            refreshCloudRecFileList();
        }
    }

    public void SaveRecFileListToDatabase() {

//        DatabaseManager.ClearRecInfos();
//
//        if (fileList != null && fileList.size() > 0) {
//            DatabaseManager.SaveRecInfos(fileList);
//        }
    }

    public boolean isLoadFromDatabase() {
        if (fileList != null && fileList.size() > 0) {
            isLoadFromDatabase = true;
        } else {
            isLoadFromDatabase = false;
        }
        return isLoadFromDatabase;
    }

    public void setLoadFromDatabase(boolean isLoadFromDatabase) {
        this.isLoadFromDatabase = isLoadFromDatabase;
    }

    public boolean isbIsRecFileSearching() {
        return bIsRecFileSearching;
    }

    public void setbIsRecFileSearching(boolean bIsRecFileSearching) {
        this.bIsRecFileSearching = bIsRecFileSearching;
    }

    public int getM_nLoginExID() {
        return m_nLoginExID;
    }

    public void setM_nLoginExID(int m_nLoginExID) {
        this.m_nLoginExID = m_nLoginExID;
    }

    public int getnSearchChn() {
        return nSearchChn;
    }

    public void setnSearchChn(int nSearchChn) {
        this.nSearchChn = nSearchChn;
    }

    public int getnSearchType() {
        return nSearchType;
    }

    public void setnSearchType(int nSearchType) {
        this.nSearchType = nSearchType;
    }


    /**
     * 搜索录像文件
     */
    public void GetRecFileList() {

        LocalDefines.bIsBackPlay = true;

        if (LocalDefines._PlaybackListviewSelectedPosition >= 0 && LocalDefines._PlaybackListviewSelectedPosition < LocalDefines._severInfoListData.size()) {

            DeviceInfo info = LocalDefines._severInfoListData.get(LocalDefines._PlaybackListviewSelectedPosition);

            if (info != null) {

                LocalDefines._nSearchDevID = info.getnDevID();
                LocalDefines._strSearchIP = info.getStrIP();
                LocalDefines._nSearchPort = info.getnPort();
                LocalDefines._strSearchDomain = info.getStrDomain();
                LocalDefines._PlaybackRecListviewFirstPosition = 0;

                m_nThreadID++;
                nThreadID = m_nThreadID;
                new RecFileSearcher(info, nSearchType, m_nThreadID).start();
            }
        }
    }

    /**
     * 更新设备选择列表
     */
    private void updateServerListView() {

        int devID = 0;
        String strDevID = textViewDevice.getText().toString();

        if (strDevID.length() > 0 && deviceInfo != null) {
            devID = deviceInfo.getnDevID();
        }

        if (LocalDefines._severInfoListData != null && LocalDefines._severInfoListData.size() > 0) {

            DeviceInfo info = null;
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

            for (int i = 0; i < LocalDefines._severInfoListData.size(); i++) {

                HashMap<String, Object> map = new HashMap<String, Object>();

                info = LocalDefines._severInfoListData.get(i);

                map.put("ItemBtnFace", R.id.item_face);

                if (info.getStrName() != null && info.getStrName().length() > 0) {
                    map.put("ItemTitleName", info.getStrName());
                } else {
                    map.put("ItemTitleName", "" + info.getnDevID());
                }

                if (devID == info.getnDevID()) {
                    optOf = i;
                }

                if (info.getnDevID() <= 0) {
                    map.put("ItemTitleID", getString(R.string.server) + info.getStrIP());
                } else {
                    map.put("ItemTitleID", getString(R.string.strIDNumber) + "" + info.getnDevID() + " " + getString(R.string.server) + info.getStrIP());
                }

                map.put("SID", (Integer) info.getnID());
                map.put("SaveType", (Integer) info.getnSaveType());

                listItem.add(map);

            }

            DeviceListViewAdapter deviceListItemAdapter = new DeviceListViewAdapter(relateAtivity, listItem, R.layout.player_back_device_select_item,

                    new String[]{"ItemTvDeviceSelect", "ItemIvDeviceSelect"},

                    new int[]{R.id.tvDeviceSelect, R.id.ivDeviceSelect});

            if (serverlistView == null) {
                LinearLayout view = (LinearLayout) View.inflate(getActivity(), R.layout.popup_window_listview, null);
                serverlistView = (ListView) view.findViewById(R.id.lvPlayer_back);
            }

            if (serverlistView != null) {
                serverlistView.setCacheColorHint(Color.TRANSPARENT);

                serverlistView.setAdapter(deviceListItemAdapter);

                serverlistView.setOnItemClickListener(this);
            }
        }
    }

    /**
     * 设备选择列表适配器
     */
    private class DeviceListViewAdapter extends BaseAdapter {

        private class ItemViewHolder {
            TextView tvName;
            ImageView ivDeviceSelect;
        }

        private ArrayList<HashMap<String, Object>> mAppList;
        private LayoutInflater mInflater;
        private Context mContext;
        private String[] keyString;
        private int[] valueViewID;
        private ItemViewHolder holder;

        public DeviceListViewAdapter(Context c, ArrayList<HashMap<String, Object>> appList, int resource, String[] from, int[] to) {
            mAppList = appList;
            mContext = c;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            keyString = new String[from.length];
            valueViewID = new int[to.length];
            System.arraycopy(from, 0, keyString, 0, from.length);
            System.arraycopy(to, 0, valueViewID, 0, to.length);
        }

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView != null) {
                holder = (ItemViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.player_back_device_select_item, null);
                holder = new ItemViewHolder();
                holder.tvName = (TextView) convertView.findViewById(valueViewID[0]);
                holder.ivDeviceSelect = (ImageView) convertView.findViewById(valueViewID[1]);

                convertView.setTag(holder);
            }

            HashMap<String, Object> map = mAppList.get(position);
            if (map != null) {

                if (position == optOf) {
                    holder.tvName.setTextColor(Color.BLUE);
                    holder.ivDeviceSelect.setImageResource(R.drawable.play_back_choose_2);
                } else {
                    holder.tvName.setTextColor(Color.parseColor("#666666"));
                    holder.ivDeviceSelect.setImageResource(R.drawable.play_back_choose_1);
                }

                String name = (String) map.get("ItemTitleName");
                holder.tvName.setText(name);

            }
            return convertView;
        }

    }

    private void StartPlayFile(int nIndex) {
        isLoadFromDatabase = true;
        int camType = LocalDefines.Device_LoginHandle.getCamType();
        Intent intent = new Intent();
        Bundle data = new Bundle();
        if (camType == LocalDefines.CAMTYPE_WALL || camType == LocalDefines.CAMTYPE_CEIL) {
            intent.setClass(relateAtivity, NVPlayerPlaybackFishEyeActivity.class);
            data.putBoolean("isPlayFishEyeFromCloud", false);
        } else {
            intent.setClass(relateAtivity, NVPlayerPlaybackActivity.class);
        }

        data.putParcelable("login_handle", LocalDefines.Device_LoginHandle);
        data.putInt("play_index", nIndex);
        data.putInt("cam_type", camType);

        intent.putExtras(data);
        relateAtivity.startActivity(intent);

        this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

    }

    private void overridePendingTransition(int zoomIn, int zoomOut) {


    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int nSelectIndex, long arg3) {
        // TODO Auto-generated method stub

        LocalDefines.B_INTENT_ACTIVITY = true; // add by mai 2015-5-15
        if (arg0.getId() == R.id.lvPlayer_back) {

            if (nSelectIndex >= 0 && nSelectIndex < LocalDefines._severInfoListData.size()) {

                LocalDefines._PlaybackListviewSelectedPosition = nSelectIndex;
                DeviceInfo info = LocalDefines._severInfoListData.get(LocalDefines._PlaybackListviewSelectedPosition);
                if (info != null && textViewDevice != null) {
                    boolean isZh = LocalDefines.isZh(getActivity());
                    if (isZh) {
//                        if (HomePageActivity.AppMode == 1) {
//
//                            if (info.getnProductId() > 0) {
//                            } else if (info.getnProductId() == 0) {
//                                isSearchCloudRec = false;
//                                tvTFVideo.setTextColor(getResources().getColor(R.color.font_color_sky_blue2));
//                                tvCloudVideo.setTextColor(getResources().getColor(R.color.font_color_gray));
//                            } else {
//                                isSearchCloudRec = false;
//                                tvTFVideo.setTextColor(getResources().getColor(R.color.font_color_sky_blue2));
//                                tvCloudVideo.setTextColor(getResources().getColor(R.color.font_color_gray));
//                            }
//
//                        } else {
//                        }
                    }
                    if (Functions.isNVRDevice("" + info.getnDevID())) {
                        llChannel.setVisibility(View.VISIBLE);
                    } else {
                        llChannel.setVisibility(View.GONE);
                    }
                    deviceInfo = info;
                    if (info.getStrName() != null && info.getStrName().length() > 0) {
                        textViewDevice.setText(info.getStrName());
                    } else {
                        textViewDevice.setText("" + info.getnDevID());
                    }
                    bSearchType = true;
                    ivPlayerBackType.setImageResource(R.drawable.play_back_video_back_2);

                    btnListVisible.setVisibility(View.VISIBLE);

                    popupListView.dismiss();
                }

            }

        } else if (arg0.getId() == R.id.recfile_list) {
            if (nSelectIndex >= 0 && nSelectIndex < fileList.size()) {

                if (mRecFileDownloader != null && mRecFileDownloader.isDownloading()) {
                    // 锟斤拷锟斤拷锟斤拷锟斤拷锟截碉拷录锟斤拷锟侥硷拷
                    Toast.makeText(getActivity(), getString(R.string.str_rec_file_cancle2), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isCloudFileList) {
                    // Log.i("TAG", "本地录像");
                    if (fileList != null && fileList.size() > 0) {
                        SaveRecFileListToDatabase();
                    }
                    LocalDefines.listMapPlayerBackFile = fileList;
                    StartPlayFile(nSelectIndex);

                } else {
                    // Log.i("TAG", "云录像");
                    if (fileList != null && fileList.size() > 0) {
                        SaveRecFileListToDatabase();
                    }
                    LocalDefines.cloudRecordFileList = fileList;
                    if (mRecFileDownloader != null && mRecFileDownloader.isDownloading()) {
                        // 锟斤拷锟斤拷锟斤拷锟斤拷锟截碉拷录锟斤拷锟侥硷拷
                        Toast.makeText(getActivity(), getString(R.string.str_rec_file_cancle2), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startPlayCloudRecordFile(nSelectIndex);
                }

            }
        }

    }

    private void setListViewOnScrollListener() {// add by luo 20141023
        // 锟斤拷锟斤拷锟叫憋拷锟斤拷锟斤拷录锟斤拷锟斤拷锟斤拷锟�

        if (recFileListView == null)
            return;

        recFileListView.setOnScrollListener(new OnScrollListener() {

            /**
             * 锟斤拷锟斤拷状态锟侥憋拷时锟斤拷锟斤拷
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 锟斤拷锟斤拷锟斤拷时锟斤拷锟芥当前锟斤拷锟斤拷锟斤拷锟斤拷位锟斤拷
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {

                    LocalDefines._PlaybackRecListviewFirstPosition = recFileListView.getFirstVisiblePosition();
                    // end add by luo 20141010
                }

            }

            /**
             * 锟斤拷锟斤拷时锟斤拷锟斤拷
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void presentShowcaseSequence() {
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(0);
//
//        MaterialShowcaseView showcaseView = new MaterialShowcaseView.Builder(getActivity()).setContentText(getResources().getString(R.string.str_showcase_fra_configdevice1)).setTarget(contentView.findViewById(R.id.llPlayBackTitle)).withRectangleShape().setDismissOnTouch(true)
//                .singleUse("PlayBackFragment").build();
//        showcaseView.setContentTextGravity(Gravity.CENTER);
//        showcaseView.setTopImage(R.drawable.guide_t, Gravity.CENTER);
//        showcaseView.show(getActivity());
    }

    private View loadingView;
    private Dialog loadingDialog;
    private int mLoadType = TYPE_LOADING;
    private static final int TYPE_LOADING = 1;
    private static final int TYPE_SAVING = 2;

    private void createLoadingDialog() {
        loadingView = LayoutInflater.from(relateAtivity).inflate(R.layout.logindialog, null);
        loadingView.setAlpha((float) 0.775);
        loadingDialog = new Dialog(relateAtivity, R.style.selectorDialog);
        loadingDialog.setContentView(loadingView);
        loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                TextView tv = (TextView) loadingView.findViewById(R.id.loginText);

                tv.setText(getString(R.string.loading));

            }
        });

        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

                if (bIsRecFileSearching) {
                    bIsRecFileSearching = false;
                    isListVisible = false;
                    layoutSearchParam.setVisibility(View.VISIBLE);
                    layoutRecFileList.setVisibility(View.GONE);
                    LocalDefines.bIsBackPlay = false;
                    m_nThreadID++;
                }
            }
        });
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);
    }

    private void stopCurrentDownloadTask() {

        if (mRecFileDownloader != null && mRecFileDownloader.isDownloading() && mDLFileListPosition != -1 && mDLFilePath != null) {
            mRecFileDownloader.stopDownloadRecordVideo();

            final File file1 = new File(mDLFilePath);
            if (file1.exists()) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        file1.delete();
                    }
                }).start();
            }

            mDLFileListPosition = -1;
            mDLFilePath = null;
        }
    }

    private void showRecordFileListLayout() {
        ll_cloud_record.setVisibility(View.GONE);
        recFileListView.setVisibility(View.VISIBLE);
    }

    private void showScrollViewLayout() {
        ll_cloud_record.setVisibility(View.VISIBLE);
        recFileListView.setVisibility(View.GONE);
    }

    private int mGetRecFileId = 0;

    public class RecFileSearcherCloud extends Thread {

        private int nSearchType = 0; // searchtype
        private int nThreadID = 0;
        private DeviceInfo info = null;// add by lin 2015-12-10

        public RecFileSearcherCloud(DeviceInfo info, int nSearchType, int nThreadID) {
            this.nSearchType = nSearchType;
            this.info = info;
            this.nThreadID = nThreadID;
        }

        public void run() {

            long lHandle = deviceInfo.getnDevID();

            LocalDefines.Device_LoginHandle = LoginHelper.getDeviceParamRecordFileEX(info, Defines.LOGIN_FOR_PLAYBACK);

            int nResult = 0;
            if (nSearchFailCount == 0) {
                nResult = CloudServiceHelper.Cloud_getRecordFileServer(0, mAccesstoken, mUserId, handler, /*锟借备id*/lHandle,
                        mEcsIP, mEcsPort, 0, nSearchType, nYear, nMonth, nDay, nStartHour,
                        nStartMin, nStartSec, nEndHour, nEndMin, nEndSec);
            } else {
                nResult = CloudServiceHelper.Cloud_getRecordFileServer(0, mAccesstoken, mUserId, handler, /*锟借备id*/lHandle,
                        mEcsIP2, mEcsPort, 0, nSearchType, nYear, nMonth, nDay, nStartHour,
                        nStartMin, nStartSec, nEndHour, nEndMin, nEndSec);
            }
            if (nResult != ResultCode.RESULT_CODE_SUCCESS && m_nThreadID == mGetRecFileId) {
                Message msg = handler.obtainMessage();
                msg.arg1 = Defines.HANDLE_MSG_CODE_GET_RECORD_FILES_END;
                msg.arg2 = nResult;
                handler.sendMessage(msg);
            }
        }
    }

    public void GetCloudRecFileList() {
        LocalDefines.bIsBackPlay = true;
        fileList.clear();
        recFileListView.setAdapter(null);
        //((HomePageActivity) relateAtivity).setGuideBarVisible(false);
        // 锟斤拷示锟斤拷锟截斤拷瓤锟�
        mLoadType = TYPE_LOADING;
        loadingDialog.show();
        m_nThreadID++;
        mGetRecFileId = m_nThreadID; // add 2016锟斤拷8锟斤拷9锟斤拷
        new RecFileSearcherCloud(deviceInfo, nSearchType, m_nThreadID).start();

    }

    public void refreshCloudRecFileList() {

        int nFileSize = 0;
        double fFileSize = 0.0;
        double nTimeLen = 0;

        String strInfo = "";
        String strSize = null;
        String strStartTime = null;
        String strTimeLen = null;

        if (fileList.size() > 0) { // 锟矫碉拷锟斤拷锟�
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

            RecordVideoInfo fileInfo = null;
            for (int i = 0; i < fileList.size(); i++) {
                fileInfo = fileList.get(i);

                HashMap<String, Object> map = new HashMap<String, Object>();

                map.put("ItemTitleName", R.id.ItemFileName);
                map.put("ItemTitleInfo", R.id.ItemFileInfo);

                map.put("FileName", fileInfo.getStrFileName());

                nFileSize = fileInfo.getnFileSize();

                strSize = getString(R.string.strFileSize);
                if (nFileSize > 1024000) {
                    fFileSize = nFileSize / 1048576.0;
                    if (fFileSize >= 100) {
                        strSize = strSize + String.format("%.0f", fFileSize) + " MB";
                    } else if (fFileSize >= 1) {
                        strSize = strSize + String.format("%.1f", fFileSize) + " MB";
                    } else {
                        strSize = strSize + String.format("%.2f", fFileSize) + " MB";
                    }

                } else if (nFileSize > 1024) {
                    fFileSize = nFileSize / 1024.0;
                    strSize = strSize + String.format("%.0f", fFileSize) + " KB";
                } else {
                    strSize = strSize + nFileSize + " B";
                }

                strStartTime = getString(R.string.strStartTime);

                if (fileInfo.getuStartHour() >= 10) {
                    strStartTime = strStartTime + fileInfo.getuStartHour();
                } else {
                    strStartTime = strStartTime + "0" + fileInfo.getuStartHour();
                }

                if (fileInfo.getuStartMin() >= 10) {
                    strStartTime = strStartTime + ":" + fileInfo.getuStartMin();
                } else {
                    strStartTime = strStartTime + ":0" + fileInfo.getuStartMin();
                }

                if (fileInfo.getuStartSec() >= 10) {
                    strStartTime = strStartTime + ":" + fileInfo.getuStartSec();
                } else {
                    strStartTime = strStartTime + ":0" + fileInfo.getuStartSec();
                }

                strTimeLen = getString(R.string.strTimeLen);
                if (fileInfo.getuFileTimeLen() >= 3600) {
                    nTimeLen = fileInfo.getuFileTimeLen() / 3600.0;
                    strTimeLen = strTimeLen + String.format("%.1f", nTimeLen) + getString(R.string.strHour);
                } else if (fileInfo.getuFileTimeLen() >= 60) {
                    nTimeLen = fileInfo.getuFileTimeLen() / 60.0;
                    strTimeLen = strTimeLen + String.format("%.1f", nTimeLen) + getString(R.string.strMin);
                } else {
                    nTimeLen = fileInfo.getuFileTimeLen();
                    strTimeLen = strTimeLen + String.format("%.0f", nTimeLen) + getString(R.string.strSec);
                }

                strInfo = strStartTime + " " + strTimeLen + " " + strSize;
                map.put("FileInfo", strInfo);
                // ===add by mai 2015-1-26=======
                map.put("FileSize", strSize);
                map.put("FileStartTime", strStartTime);
                map.put("FileTimeLen", strTimeLen);
                map.put("FileDownloadState", fileInfo.getnFileState()); // 锟斤拷锟斤拷
                // 2016锟斤拷8锟斤拷16锟斤拷
                map.put("FileDownloadProgress", fileInfo.getnFileDownloadProgress()); // 锟斤拷锟斤拷
                // ===end add by mai 2015-1-26====
                listItem.add(map);

            }


            RecFileListViewAdapterCloud recFileListItemAdapter = new RecFileListViewAdapterCloud(getActivity(), listItem, R.layout.recfile_list_item, // ListItem锟斤拷XML实锟斤拷

                    new String[]{"ItemTitleName", "ItemTitleInfo", "ItemSize", "ItemTimeLen", "ItemDownload", "ItemDownloadState", "ItemDownloadPro", "ItemDownloadBar"},
                    new int[]{R.id.ItemFileName, R.id.ItemFileInfo, R.id.tvSize, R.id.tvTimeLen, R.id.download_layout, R.id.recording_download_txt, R.id.tv_progress, R.id.pb_download});


            if (recFileListView == null) {
                recFileListView = (ListView) contentView.findViewById(R.id.recfile_list);
            }
            recFileListView.setAdapter(recFileListItemAdapter);
            recFileListView.setSelection(LocalDefines._PlaybackRecListviewFirstPosition);

        } else {
            recFileListView.setAdapter(null);
        }

    }

    private class RecFileListViewAdapterCloud extends BaseAdapter {

        private class ItemViewHolder {
            TextView tvName;
            TextView tvInfo;
            TextView tvSize; // add by mai 2015-1-26
            TextView tvTimeLen; // add by mai 2015-1-26

            RelativeLayout rlDownload;

            TextView tvDownloadState;
            TextView tvDownloadPro;
            ProgressBar pbDownloadBar;

        }

        private ArrayList<HashMap<String, Object>> mAppList;
        private LayoutInflater mInflater;
        private Context mContext;
        private String[] keyString;
        private int[] valueViewID;
        private ItemViewHolder holder;

        public RecFileListViewAdapterCloud(Context context, ArrayList<HashMap<String, Object>> appList, int resource, String[] from, int[] to) {
            mAppList = appList;
            mContext = context;
            keyString = new String[from.length];
            valueViewID = new int[to.length];

            System.arraycopy(from, 0, keyString, 0, from.length);
            System.arraycopy(to, 0, valueViewID, 0, to.length);
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private void downloadFile(final int position) {

            // 普通状态或下载出错
            String strSDcardPath = Functions.GetSDPath();
            if (strSDcardPath == null) {
                Toast.makeText(getActivity(), getString(R.string.noticeSDCardNotExist), Toast.LENGTH_SHORT).show();
                return;
            }
            StatFs sf = new StatFs(strSDcardPath);
            RecordVideoInfo recFile = fileList.get(position);
            int fileSize = recFile.getnFileSize();
            boolean isStorageEnough = sf.getAvailableBytes() - fileSize > 0 ? true : false;
            if (isStorageEnough) {
                String strSavePath = strSDcardPath + File.separator + LocalDefines.SDCardPath;
                File fileDiir = new File(strSavePath);
                if (!fileDiir.exists()) {
                    fileDiir.mkdir();
                }

                String fileName = recFile.getStrFileName();
                String strFilePath = strSavePath + File.separator + deviceInfo.getnDevID() + "_" + fileName + ".mp4";
                final File file = new File(strFilePath);
                if (file.exists()) {
                    Toast.makeText(getActivity(), getString(R.string.str_rec_file_exist), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mRecFileDownloader != null && mRecFileDownloader.isDownloading() && mDLFileListPosition != -1 && mDLFilePath != null) {
                    //mRecFileDownloader.StopDownloadRecFile();
                    mRecFileDownloader.stopDownloadRecordVideo();
                    mAppList.get(mDLFileListPosition).put("FileDownloadState", FILE_DOWNLOAD_STATE_NORMAL);
                    mAppList.get(mDLFileListPosition).put("FileDownloadProgress", 0);
                    final File file1 = new File(mDLFilePath);
                    if (file1.exists()) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                file1.delete();
                            }
                        }).start();
                    }
                    mDLFileListPosition = -1;
                    mDLFilePath = null;
                }

                final RecordVideoDownloader recFileDownloader = new RecordVideoDownloader();
//                boolean startDownloadResult = recFileDownloader.StartDownloadCloudRecFile(new IDownloadCallback() {
//
//                    @Override
//                    public void onDownloadProcess(Object arg0, final int state, final int progress) {
//
//                        mAppList.get(position).put("FileDownloadState", state);
//                        mAppList.get(position).put("FileDownloadProgress", progress);
//
//                        if (state == FILE_DOWNLOAD_STATE_SUCCESS || progress == 100) {
//                            boolean stopResult = recFileDownloader.StopDownloadRecFile();
//
//                            mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_SUCCESS);
//                            mAppList.get(position).put("FileDownloadProgress", 0);
//
//                        } else if (state == FILE_DOWNLOAD_STATE_ERROR) {
//                            boolean stopResult = recFileDownloader.StopDownloadRecFile();
//
//                            mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_ERROR);
//                            mAppList.get(position).put("FileDownloadProgress", 0);
//
//                            if (file.exists()) {
//                                new Thread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        file.delete();
//                                    }
//                                }).start();
//                            }
//                        }
//
//                        getActivity().runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                if (recFileListView.getAdapter() != null) {
//                                    ((RecFileListViewAdapterCloud) (recFileListView.getAdapter())).notifyDataSetChanged();
//                                }
//                            }
//
//                        });
//
//                    }
//                }, strFilePath, mUserId, deviceInfo.getnDevID(), "123", mAccesstoken, mEcsIP, mEcsPort, recFile);
//                if (startDownloadResult) {
//                    mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_CONNECTING); // 连接中
//                    mDLFileListPosition = position;
//                    mDLFilePath = strFilePath;
//                    mRecFileDownloader = recFileDownloader;
//                    if (recFileListView.getAdapter() != null) {
//                        ((RecFileListViewAdapterCloud) (recFileListView.getAdapter())).notifyDataSetChanged();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), getString(R.string.downFail), Toast.LENGTH_SHORT).show();
//                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.str_storage_not_enough), Toast.LENGTH_SHORT).show();
                return;
            }

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView != null) {
                holder = (ItemViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.recfile_list_item_cloud, null);
                holder = new ItemViewHolder();
                convertView.setTag(holder);
            }

            holder.tvName = (TextView) convertView.findViewById(valueViewID[0]);
            holder.tvInfo = (TextView) convertView.findViewById(valueViewID[1]);
            holder.tvSize = (TextView) convertView.findViewById(valueViewID[2]);
            holder.tvTimeLen = (TextView) convertView.findViewById(valueViewID[3]);
            holder.rlDownload = (RelativeLayout) convertView.findViewById(valueViewID[4]);
            holder.tvDownloadState = (TextView) convertView.findViewById(valueViewID[5]);
            holder.tvDownloadPro = (TextView) convertView.findViewById(valueViewID[6]);
            holder.pbDownloadBar = (ProgressBar) convertView.findViewById(valueViewID[7]);

            HashMap<String, Object> map = mAppList.get(position);

            if (map != null) {

                String startTime = (String) map.get("FileStartTime");
                String strSize = (String) map.get("FileSize");
                String strTimeLen = (String) map.get("FileTimeLen");
                String info = (String) map.get("FileName");
                int fileDownloadState = (Integer) map.get("FileDownloadState");
                int fileDownloadProgress = (Integer) map.get("FileDownloadProgress");

                holder.tvName.setText(startTime);
                holder.tvTimeLen.setText(strTimeLen);
                holder.tvSize.setText(strSize);
                holder.tvInfo.setText(info);

                if (fileDownloadState == FILE_DOWNLOAD_STATE_NORMAL) {
                    // 2 普通状态下载
                    holder.tvDownloadState.setText(getString(R.string.showDown));
                    holder.tvDownloadPro.setVisibility(View.INVISIBLE);
                    holder.pbDownloadBar.setVisibility(View.INVISIBLE);
                } else if (fileDownloadState == FILE_DOWNLOAD_STATE_SUCCESS) {
                    // 1 下载完成
                    holder.tvDownloadState.setText(getString(R.string.str_rec_file_download_finish));
                    holder.tvDownloadPro.setVisibility(View.VISIBLE);
                    holder.pbDownloadBar.setVisibility(View.VISIBLE);
                    holder.tvDownloadPro.setText(100 + "%");
                    holder.pbDownloadBar.setProgress(100);
                } else if (fileDownloadState == FILE_DOWNLOAD_STATE_DOWNLOADING) {
                    // 0 正在下载
                    holder.tvDownloadState.setText(R.string.str_rec_file_stop_download);
                    // holder.tvDownloadProgress.setText(getString(R.string.str_rec_file_downloading));
                    holder.tvDownloadPro.setVisibility(View.VISIBLE);
                    holder.pbDownloadBar.setVisibility(View.VISIBLE);
                    holder.tvDownloadPro.setText(fileDownloadProgress + "%");
                    holder.pbDownloadBar.setProgress(fileDownloadProgress);
                } else if (fileDownloadState == FILE_DOWNLOAD_STATE_ERROR) {
                    // -1 下载出错重新下载
                    holder.tvDownloadState.setText(getString(R.string.str_rec_file_redownload));
                    holder.tvDownloadPro.setVisibility(View.INVISIBLE);
                    holder.pbDownloadBar.setVisibility(View.INVISIBLE);
                    holder.tvDownloadPro.setText(fileDownloadProgress + "%");
                    holder.pbDownloadBar.setProgress(fileDownloadProgress);
                } else if (fileDownloadState == FILE_DOWNLOAD_STATE_CONNECTING) {
                    // -2  连接中
                    holder.tvDownloadState.setText(getString(R.string.str_rec_file_download_connecting));
                    holder.tvDownloadPro.setVisibility(View.INVISIBLE);
                    holder.pbDownloadBar.setVisibility(View.INVISIBLE);
                }

            }

            holder.rlDownload.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {

                    int fileDownloadState = (Integer) mAppList.get(position).get("FileDownloadState");

                    if (fileDownloadState == FILE_DOWNLOAD_STATE_NORMAL
                            || fileDownloadState == FILE_DOWNLOAD_STATE_ERROR
                            || fileDownloadState == FILE_DOWNLOAD_STATE_SUCCESS) {

                        //downloadFile(position);

                    } else if (fileDownloadState == FILE_DOWNLOAD_STATE_DOWNLOADING
                            || fileDownloadState == FILE_DOWNLOAD_STATE_CONNECTING) {

                        View viewDialog = View.inflate(getActivity(), R.layout.show_alert_dialog, null);

                        TextView tv_title = (TextView) viewDialog.findViewById(R.id.tv_title);
                        TextView tv_content = (TextView) viewDialog.findViewById(R.id.tv_content);

                        tv_title.setText(getString(R.string.str_rec_file_cancle1));
                        tv_content.setVisibility(View.GONE);

                        new AlertDialog.Builder(getActivity())
                                .setView(viewDialog)
                                .setNegativeButton(getString(R.string.alert_btn_NO), null)
                                .setPositiveButton(getString(R.string.alert_btn_YES), new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        mRecFileDownloader.stopDownloadRecordVideo();

                                        mAppList.get(position).put("FileDownloadState", FILE_DOWNLOAD_STATE_NORMAL);
                                        mAppList.get(position).put("FileDownloadProgress", 0);

                                        if (recFileListView.getAdapter() != null) {
                                            ((RecFileListViewAdapterCloud) (recFileListView.getAdapter())).notifyDataSetChanged();
                                        }

                                        final File file = new File(mDLFilePath);

                                        if (file.exists()) {
                                            new Thread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    file.delete();
                                                }
                                            }).start();
                                        }
                                    }

                                }).show();
                    }
                }

            });

            return convertView;
        }

    }

    private void startPlayCloudRecordFile(int position) {
        int camType = LocalDefines.Device_LoginHandle.getCamType();
        Intent intent = new Intent();
        Bundle data = new Bundle();

        if (camType == LocalDefines.CAMTYPE_WALL || camType == LocalDefines.CAMTYPE_CEIL) {
//            intent.setClass(getActivity(), NVPlayerPlaybackFishEyeActivity.class);
//            data.putParcelable(Defines.CLOUD_RECORD_FILE_RETURN_MESSAGE, LocalDefines.Device_LoginHandle);
//            data.putBoolean("isPlayFishEyeFromCloud", true);
        } else {
//            intent.setClass(getActivity(), NVPlayerPlaybackCloudRecordActivity.class);
        }

        data.putInt("play_index", position);
        data.putInt("deviceId", deviceInfo.getnDevID());
        data.putString("accesstoken", mAccesstoken);
        data.putInt("user_id", mUserId);
        data.putString("ecs_ip", mEcsIP);
        data.putString("ecs_ip2", mEcsIP2);
        data.putInt("ecs_port", mEcsPort);
        data.putInt("ecs_port2", mEcsPort2);
        intent.putExtras(data);
        startActivity(intent);
    }

}
