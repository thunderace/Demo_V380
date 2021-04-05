package com.macrovideo.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.macrovideo.animate.RadarView;
import com.macrovideo.sdk.objects.DeviceInfo;
import com.macrovideo.sdk.tools.DeviceScanner;
import com.macrovideo.sdk.tools.Functions;

import org.json.JSONException;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 用于声波配置
 *
 * @author Administrator
 */
public class SmartLinkQuickWifiConfigActivity extends Activity implements
        OnClickListener, OnItemClickListener {

    // //WIFI
    private WiFiAdnim mWiFiAdnim;
    private WifiManager mWiFiManager; // 定义一个WifiManager
    private WifiReceiver mwReceiver; // 定义一个WifiReceiver
    private android.net.wifi.WifiInfo mWifiInfo; // 定义一个wifiInfo
    private List<ScanResult> locaWifiDeiviceList = new ArrayList<ScanResult>();

    private LinearLayout lLayoutWifiInputPage; // 显示或隐藏准备配置，开始配置
    private ImageView btnSLBack, ivSLPwdVisible; // 返回按钮，开始声波配置按钮，现实隐藏wifi密码按钮
    private Button btnSLSearchBack = null;
    private Button btnSLStartConfig; // 下一步;
    private Button btnWifiQuikConfig;// AP配置
    private EditText etSLWifiSSID, etSLWifiPassword; // wifi名称，wifi密码

    private View soundWaveConfigConctentView = null;// ,
    // soundWaveConfigDemoState
    // = null;
    // //用于加载listView的View
    private Dialog soundWaveConfigDialog = null;// ,
    // soundWaveConfigDemoStateDialog
    // = null; //
    // 用于现实ListView的DiaLog
    private ImageView ivSoundWaveConfigWifiListViewBack; // wifi列表返回按钮
    private ListView lvSoundWaveConfigWifi; // 声波配置的wifi列表

    private ProgressDialog progressDialog; // 用于确定wifi列表是否出现
    private boolean bWifiPassword = true; // 用于判断是否隐藏密码

    private LinearLayout llayoutSLSearchingPage; // 显示或隐藏准备配置，开始配置
    private FrameLayout flayoutSLSearchingAnimate; // 显示搜索界面
    private RadarView searchAminatView;
    private String strConnSSID; // 当前手机连接的wifi用户名
    private MediaPlayer soundPlayer = null;// 声音播放
    private MediaPlayer soundPlayerHint = null; //

    private static final int WIFI_CONNECT = 0x11;
    private static final int WIFI_CONNECT2 = 0x12;
    private static final int WIFI_NOT_CONNECT = 0x14;
    private static final int SEEK_DEVICE_OVERTIME = 0x13;
    private static final int MY_PERMISSION_REQUEST_LOCATION = 0;
    private boolean bWifiOpen = false;
    private AlertDialog.Builder wifiNoticeDialog = null;
    private boolean bIsNoticeShow = false; //
    private boolean bIsConfiging = false; //

    private int nTimeoutDetectID = 0;

    private TextView tvTimeLeft = null;

    private ArrayList<DeviceInfo> deviceList = null;

    private int nConfigID = 0;
    private int mWifiEnrcrypt;

    private int n_BindDeviceThreadID = 0;
    private int bindDevice_result;

    private DeviceInfo Editinfo;

    boolean bHasUpdate = false;
    boolean bNewDevFound = false;

    static final int DEVICE_IS_EXISTANCE = 10004; // 设备已存在，添加失败

    private LinearLayout llWifiQuikConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // add 2016年8月25日 设置屏幕常亮，避免Smartlink的时候息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题 
        setContentView(R.layout.activity_smartlink_wifi_config);
        initView(); // 初始化界面控件

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        initWifi(); // 初始化wifi 参数 并开启wifi广播
        wifiChooseWindow(); // 初始化现实wifi列表的Dialog
    }


    @Override
    public void onStop() {
        if (soundWaveConfigDialog != null && soundWaveConfigDialog.isShowing()) {
            soundWaveConfigDialog.dismiss();
        }
        try {
            if (soundPlayerHint != null) {
                soundPlayerHint.stop(); // 关闭提示音
            }

            if (soundPlayer != null) {
                soundPlayer.stop(); // 关闭声音
            }
        } catch (Exception e) {

        }
        if (bIsConfiging) {

            DeviceScanner.stopSmartConnection();
            showInputPage();
            bIsConfiging = false;
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        soundPlayer = null;// 声音播放
        soundPlayerHint = null; //

        locaWifiDeiviceList = null;
        mWifiInfo = null;
        this.unregisterReceiver(mwReceiver);

        if (searchAminatView != null) {
            searchAminatView.recycleBitmap();
            System.gc();
        }
        super.onDestroy();

    }

    /**
     * 初始化控件
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        llWifiQuikConfig = (LinearLayout) findViewById(R.id.llWifiQuikConfig);
        lLayoutWifiInputPage = (LinearLayout) findViewById(R.id.lLayoutWifiInputPage);

        btnSLBack = (ImageView) findViewById(R.id.btnSLBack);
        btnSLBack.setOnClickListener(this);

        ivSLPwdVisible = (ImageView) findViewById(R.id.ivSLPwdVisible);
        ivSLPwdVisible.setOnClickListener(this);

        btnSLStartConfig = (Button) findViewById(R.id.btnSLStartConfig);
        btnSLStartConfig.setOnClickListener(this);


        btnWifiQuikConfig = (Button) findViewById(R.id.btnWifiQuikConfig);// add
        // by
        // lin
        // 20160123
        boolean isZh = LocalDefines.isZh(this);
        //llWifiQuikConfig.setVisibility(isZh ? View.VISIBLE : View.GONE);
        btnWifiQuikConfig.setOnClickListener(this);

        etSLWifiSSID = (EditText) findViewById(R.id.etSLWifiSSID);
        etSLWifiSSID.setInputType(InputType.TYPE_NULL);
        etSLWifiSSID.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    etSLWifiSSID.setEnabled(false);
                } else {
                    etSLWifiSSID.setEnabled(true);
                }
            }
        });
        etSLWifiPassword = (EditText) findViewById(R.id.etSLWifiPassword);

        lLayoutWifiInputPage.setVisibility(View.VISIBLE);
        etSLWifiPassword
                .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); // 默认显示密码

        llayoutSLSearchingPage = (LinearLayout) findViewById(R.id.llayoutSLSearchingPage);
        flayoutSLSearchingAnimate = (FrameLayout) findViewById(R.id.flayoutSLSearchingAnimate);
        flayoutSLSearchingAnimate.setBackgroundColor(Color
                .parseColor("#f9f9f9"));
        llayoutSLSearchingPage.setVisibility(View.GONE);

        btnSLSearchBack = (Button) findViewById(R.id.btnSLSearchBack);
        btnSLSearchBack.setOnClickListener(this);
        // gif图
        searchAminatView = (RadarView) findViewById(R.id.searchAminatView);
        searchAminatView.setWillNotDraw(false); //
        tvTimeLeft = (TextView) findViewById(R.id.tvTimeLeft);
    }

    // 信息提示框
    public void ShowAlert(String title, String msg) {

        if (hasWindowFocus()) {
            View view = View.inflate(this, R.layout.show_alert_dialog, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_title.setText(title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_content.setText(msg);
            new AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton(getString(R.string.alert_btn_OK), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            setResult(android.app.Activity.RESULT_OK);
                        }

                    }).show();
        }
    }

    /**
     * 初始化wifi
     */
    private void initWifi() {
        mWiFiAdnim = new WiFiAdnim(this);
        mWiFiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWiFiManager.getConnectionInfo();

        // 如果手机有连接wifi 则
        if (mWiFiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED
                && mWifiInfo != null) { // 如果wifi是打开状态
            strConnSSID = mWifiInfo.getSSID();
            // System.out.println("GetSSID= "+strConnSSID+", "+strConnSSID.equalsIgnoreCase("0x"));//

            if (strConnSSID != null && strConnSSID.length() > 0
                    && !strConnSSID.equalsIgnoreCase("0x") && !strConnSSID.equalsIgnoreCase("<unknown ssid>")) {
                // System.out.println("GetSSID= "+strConnSSID.substring(0,
                // 1)+", "+strConnSSID.substring(strConnSSID.length()-1,
                // strConnSSID.length()));//add for test

                if (strConnSSID.substring(0, 1).equals("\"")
                        && strConnSSID.substring(strConnSSID.length() - 1,
                        strConnSSID.length()).equals("\"")) {
                    strConnSSID = strConnSSID.substring(1,
                            (strConnSSID.length() - 1)); // 得到当前连接的用户铭，去掉前后双引号
                }

                etSLWifiSSID.setText(strConnSSID);
            } else {
                etSLWifiSSID.setText("");
            }
            // btnSLStartConfig.setEnabled(true);
            if (lLayoutWifiInputPage.getVisibility() == View.VISIBLE) {
                if (isLanguage()) {
                    soundPlayerHint = MediaPlayer.create(this,
                            R.raw.input_wifi_pwd);
                    soundPlayerHint.setLooping(false);
                    soundPlayerHint.start();
                }
            }

        } else { // wifi没有开启
            if (!bIsNoticeShow) {

                View view = View
                        .inflate(this, R.layout.show_alert_dialog, null);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setText(getString(R.string.wifiConnect));
                TextView tv_content = (TextView) view
                        .findViewById(R.id.tv_content);
                tv_content.setText(getString(R.string.wifi_start_bt));

                wifiNoticeDialog = new AlertDialog.Builder(this);
                wifiNoticeDialog.setView(view);
                wifiNoticeDialog.setPositiveButton(getString(R.string.wifi_is),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mWiFiAdnim.openWifi(); // 开启wifi
                                bIsNoticeShow = false;
                                progressDialog = new ProgressDialog(
                                        SmartLinkQuickWifiConfigActivity.this);
                                progressDialog
                                        .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog
                                        .setMessage(getString(R.string.wifi_start));
                                progressDialog.show(); // 显示进度条

                            }
                        });
                wifiNoticeDialog.setNegativeButton(getString(R.string.wifi_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                bIsNoticeShow = false;
                            }
                        });
                wifiNoticeDialog.show();
                bIsNoticeShow = true;
            }

        }
        // /注册wifi广播接收器
        mwReceiver = new WifiReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); // 网络状态改变
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); // wifi 状态改变
        registerReceiver(mwReceiver, intentFilter);
        // //
        mWiFiAdnim.startScan(); // 开始扫描网络

    }

    // 判断wifi是否是打开的
    public boolean isWiFiActive() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressWarnings("deprecation")
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.getTypeName().equals("WIFI") && ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * wifi选择窗口
     */
    private void wifiChooseWindow() {
        soundWaveConfigConctentView = LayoutInflater.from(getApplication())
                .inflate(R.layout.sound_wave_config_window, null);
        soundWaveConfigDialog = new Dialog(this, R.style.dialog_bg_transparent);
        soundWaveConfigDialog.setContentView(soundWaveConfigConctentView);
        soundWaveConfigDialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub
                onSoundWaveConfigListViewDialogShow();
            }

        });
    }

    /**
     * 显示wifi列表
     */
    private void onSoundWaveConfigListViewDialogShow() {

        ivSoundWaveConfigWifiListViewBack = (ImageView) soundWaveConfigConctentView
                .findViewById(R.id.ivSoundWaveConfigBack);
        lvSoundWaveConfigWifi = (ListView) soundWaveConfigConctentView
                .findViewById(R.id.lvSoundWaveConfig);
        ivSoundWaveConfigWifiListViewBack.setOnClickListener(this);
        lvSoundWaveConfigWifi.setOnItemClickListener(this);

        // listView数据加载并显示
        if (locaWifiDeiviceList != null && locaWifiDeiviceList.size() > 0) {
            DeviceSoundWaveConfigAdapter deviceSoundWaveConfigAdapter = new DeviceSoundWaveConfigAdapter(
                    SmartLinkQuickWifiConfigActivity.this, locaWifiDeiviceList,
                    R.layout.time_zone_item, new String[]{"item_list"},
                    new int[]{R.id.tvTimeZone});
            if (lvSoundWaveConfigWifi != null) {
                lvSoundWaveConfigWifi.setAdapter(deviceSoundWaveConfigAdapter); //
            } else {
                lvSoundWaveConfigWifi = (ListView) soundWaveConfigConctentView
                        .findViewById(R.id.lvSoundWaveConfig);
                lvSoundWaveConfigWifi.setAdapter(deviceSoundWaveConfigAdapter); //
            }
        }

    }

    /**
     * 判断当前语言
     *
     * @return
     */
    public boolean isLanguage() {
        boolean bisLanguage = false;

        String locale = Locale.getDefault().getLanguage();
        if (locale.equals("zh")) // 如果是zhongwen
        {
            bisLanguage = true;
        }
        return bisLanguage;
    }

    // 列表
    public class DeviceSoundWaveConfigAdapter extends BaseAdapter {

        private class ItemViewHolder {
            TextView tvTimeZone;
        }

        private List<ScanResult> mAppList;
        private LayoutInflater mInflater;
        private Context mContext;
        private String[] keyString;
        private int[] valueViewID;
        private ItemViewHolder holder;

        public DeviceSoundWaveConfigAdapter(Context c,
                                            List<ScanResult> appList, int resource, String[] from, int[] to) {
            mAppList = appList;
            mContext = c;
            mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            keyString = new String[from.length];
            valueViewID = new int[to.length];
        }

        @Override
        public int getCount() {

            return mAppList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                holder = (ItemViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.time_zone_item, null);
                holder = new ItemViewHolder();
                holder.tvTimeZone = (TextView) convertView
                        .findViewById(R.id.tvTimeZone);
                convertView.setTag(holder);
            }

            if (mAppList != null && mAppList.size() > 0) {
                holder.tvTimeZone.setText(mAppList.get(position).SSID);
            }

            return convertView;
        }

    }

    /**
     * 用于判断当前搜索是否超时
     *
     * @author Administrator
     */
    public class TimeoutDetectThread extends Thread {

        private int nThreadID = 0;

        public TimeoutDetectThread(int nThreadID) {
            this.nThreadID = nThreadID;
        }

        @Override
        public void run() {

            int nCount = 83;
            while (nTimeoutDetectID == nThreadID) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {

                }
                nCount--;
                if (nCount <= 0) {
                    break;
                }
            }

            if (nThreadID == nTimeoutDetectID) { //

                Message msg = handler.obtainMessage();
                msg.arg1 = SEEK_DEVICE_OVERTIME;
                handler.sendMessage(msg);
            }

        }

    }

    private void showInputPage() {

        lLayoutWifiInputPage.setVisibility(View.VISIBLE);
        llayoutSLSearchingPage.setVisibility(View.GONE);
    }

    private void showSearchingPage() {
        lLayoutWifiInputPage.setVisibility(View.GONE);
        llayoutSLSearchingPage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.btnSLSearchBack: // 点击返回按钮
                searchAminatView.stopAnimate();
                try {
                    if (isLanguage() && soundPlayerHint != null) {
                        soundPlayerHint.stop(); // 关闭提示音
                    }

                    if (soundPlayer != null) {
                        soundPlayer.stop(); // 关闭声音
                    }
                } catch (Exception e) {

                }

                bIsConfiging = false;

                DeviceScanner.stopSmartConnection();
                showInputPage();
                nTimeoutDetectID++;

                break;
            case R.id.btnSLBack: // 点击返回按钮

                bIsConfiging = false;
                DeviceScanner.stopSmartConnection();

                try {
                    if (isLanguage() && soundPlayerHint != null) {
                        soundPlayerHint.stop(); // 关闭提示音
                    }

                    if (soundPlayer != null) {
                        soundPlayer.stop(); // 关闭声音
                    }
                } catch (Exception e) {

                }

                //Intent intent = new Intent(SmartLinkQuickWifiConfigActivity.this, MainActivity.class);
                //startActivity(intent);

                SmartLinkQuickWifiConfigActivity.this.finish();

                break;

            case R.id.btnSLStartConfig: // 点击下一步按钮

                // 重新加载声音

                if (isLanguage()) {
                    if (soundPlayerHint != null) {
                        soundPlayerHint.stop();
                    }

                    soundPlayerHint = MediaPlayer
                            .create(this, R.raw.device_perpare);
                    soundPlayerHint.setLooping(false);
                    soundPlayerHint.start();
                }

                String strSSID = etSLWifiSSID.getText().toString();
                String strPassword = etSLWifiPassword.getText().toString();
                nConfigID = LocalDefines.getConfigID();
                bIsConfiging = true;

                // 获得当前已连接的SSID的字符串
                String currentConnectedSSIDName = LocalDefines.getCurrentConnectedWifiSSIDName(mWiFiManager);
                if (!(currentConnectedSSIDName.equals(strSSID)) && strSSID != null
                        && strSSID.length() > 0) {
                    connectToSpecifiedWifi(strSSID, strPassword, mWifiEnrcrypt);
                }
                //
                DeviceScanner.startSmartConnection(nConfigID, strSSID, strPassword); // 开始发送smarkLink

                searchAminatView.startAnimate();
                showSearchingPage();

                StartSearchDevice(); // 开始搜索设备
                if (isLanguage() && soundPlayerHint != null) {
                    soundPlayerHint.stop(); // 关闭声音
                }
                soundPlayer = MediaPlayer.create(this, R.raw.seekmusic); // 加入声音
                soundPlayer.setLooping(true);
                soundPlayer.start();

                tvTimeLeft.setText("" + 80);
                nTimeoutDetectID++;

                CountDownTimer timer = new CountDownTimer(80000, 1000) {
                    int nThreadID = nTimeoutDetectID;
                    int nCount = 80;

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (nThreadID != nTimeoutDetectID) {

                            return;
                        }
                        if (tvTimeLeft != null) {
                            nCount--;
                            if (nCount < 0)
                                nCount = 0;
                            try {
                                tvTimeLeft.setText("" + nCount);
                            } catch (Exception e) {

                            }

                        }
                    }

                    @Override
                    public void onFinish() {
                        if (nThreadID == nTimeoutDetectID) { //

                            Message msg = handler.obtainMessage();
                            msg.arg1 = SEEK_DEVICE_OVERTIME;
                            handler.sendMessage(msg);
                        }
                    }
                };
                timer.start();
                break;
            case R.id.ivSoundWaveConfigBack: // wifi列表窗口返回按钮

                if (soundWaveConfigDialog != null) {
                    soundWaveConfigDialog.dismiss();
                }

                break;

            case R.id.ivSLPwdVisible: // 点击了显示隐藏密码按钮

                if (bWifiPassword) {
                    bWifiPassword = false;
                    etSLWifiPassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivSLPwdVisible.setImageResource(R.drawable.netword_hide);
                    Editable etable = etSLWifiPassword.getText();
                    Selection.setSelection(etable, etable.length());
                } else {

                    bWifiPassword = true;
                    etSLWifiPassword
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivSLPwdVisible
                            .setImageResource(R.drawable.netword_show_password);
                    Editable etable = etSLWifiPassword.getText();
                    Selection.setSelection(etable, etable.length());
                }

                break;

            case R.id.btnWifiQuikConfig:// add by lin 20160123 AP配置
//                if (bIsSearching) {
//                    StopSearchDevice(); // 停止刷新
//                }
//
//                if (Build.VERSION.SDK_INT >= 23) {
//                    initGPS();
//                } else {
//                    Intent intentSeekFine = new Intent(this, DeviceQuickConfigActivity.class);
//                    this.startActivity(intentSeekFine);
//                    this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                    this.finish();
//                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION:
                if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent intentSeekFine = new Intent(
//                            SmartLinkQuickWifiConfigActivity.this,
//                            DeviceQuickConfigActivity.class);
//                    startActivity(intentSeekFine);
//                    this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                    this.finish();
                } else {
                    View view = View
                            .inflate(this, R.layout.show_alert_dialog, null);
                    TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                    tv_title.setText(getString(R.string.str_permission_request));
                    TextView tv_content = (TextView) view
                            .findViewById(R.id.tv_content);
                    tv_content.setText(getString(R.string.str_permission_location));
                    new AlertDialog.Builder(this)
                            .setView(view)
                            .setNegativeButton(getResources().getString(R.string.str_permission_neglect), null)
                            .setPositiveButton(
                                    getResources().getString(R.string.str_permission_setting2),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent();
                                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            Uri uri = Uri.fromParts("package",
                                                    SmartLinkQuickWifiConfigActivity.this.getPackageName(), null);
                                            intent.setData(uri);

                                            startActivity(intent);
                                        }
                                    }).show();
                }
                break;
        }

    }

    /**
     * listView点击事件
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        // wifi列表点击事件

        lvSoundWaveConfigWifi.setSelection(arg2);

        if (locaWifiDeiviceList != null && locaWifiDeiviceList.size() > 0) {
            String WifiSSID = locaWifiDeiviceList.get(arg2).SSID;
            mWifiEnrcrypt = encryCodeOfCapabilities(locaWifiDeiviceList
                    .get(arg2).capabilities);
            if (WifiSSID != null && WifiSSID.length() > 0) {
                etSLWifiSSID.setText(WifiSSID);
                etSLWifiPassword.setText("");
                Editable etable = etSLWifiSSID.getText();
                Selection.setSelection(etable, etable.length());
                if (soundWaveConfigDialog != null) {
                    soundWaveConfigDialog.dismiss();
                }
            }

        }

    }

    /**
     * 创建一个内部类 进行广播扫描出来的热点信息
     *
     * @author Administrator
     */
    public class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {

            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                // 网络状态改变
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    etSLWifiSSID.setText("");
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {

                    mWifiInfo = mWiFiManager.getConnectionInfo();

                    // 获取当前wifi名称
                    strConnSSID = mWifiInfo.getSSID();
                    if (strConnSSID != null && strConnSSID.length() > 0 && !strConnSSID.equalsIgnoreCase("0x") && !strConnSSID.equalsIgnoreCase("<unknown ssid>")) {

                        if (strConnSSID.substring(0, 1).equals("\"")
                                && strConnSSID.substring(strConnSSID.length() - 1, strConnSSID.length()).equals("\"")) {
                            strConnSSID = strConnSSID.substring(1, (strConnSSID.length() - 1)); // 得到当前连接的用户名，去掉前后双引号
                        }

                        etSLWifiSSID.setText(strConnSSID);
                    } else {
                        etSLWifiSSID.setText("");
                    }
                }

            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi打开与否
                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

                if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                    btnSLStartConfig.setEnabled(false);
                } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                    btnSLStartConfig.setEnabled(true);
                }
            }
            locaWifiDeiviceList = mWiFiManager.getScanResults(); // 存放所有热点信息

            // 根据信号的强度对locaWifiDeiviceList进行排序
            if (!locaWifiDeiviceList.isEmpty()) {
                Collections.sort(locaWifiDeiviceList,
                        new Comparator<ScanResult>() {
                            @Override
                            public int compare(ScanResult object1,
                                               ScanResult object2) {
                                int i = Math.abs(object1.level)
                                        + "".compareTo(Math.abs(object2.level)
                                        + "");
                                return i;
                            }
                        });
            }

            if (locaWifiDeiviceList != null && locaWifiDeiviceList.size() > 0
                    && progressDialog != null) {
                progressDialog.dismiss();
                bWifiOpen = true;
            }
        }

    }

    /**
     * 加密类型
     *
     * @param capabilities
     * @return
     */
    private int wifiEncrye(String capabilities) {
        int encrye = 1;

        if (capabilities.indexOf("WPA2") != -1) {
            encrye = 3;
        } else if (capabilities.indexOf("[WPA-PSK-TKIP+CCMP]") != -1
                || capabilities.indexOf("[WPA2-PSK-TKIP+CCMP]") != -1) {
            encrye = 3;
        } else if (capabilities.indexOf("[WEP]") != -1
                && capabilities.indexOf("[IBSS]") != -1) {
            encrye = 2;

        } else if (capabilities.indexOf("[WEP]") != -1) {
            encrye = 2;

        } else if (capabilities.indexOf("[WPA-PSK-CCMP]") != -1
                || capabilities.indexOf("[WPA-PSK-TKIP+CCMP]") != -1) {

            encrye = 3;

        } else if (capabilities.indexOf("[WPA2-PSK-CCMP]") != -1
                || capabilities.indexOf("[WPA2-PSK-TKIP+CCMP]") != -1) {
            encrye = 3;

        } else if (capabilities.indexOf("[ESS]") != -1) {
            encrye = 1;
        }

        return encrye;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果是返回键,直接返回到桌面
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            bIsConfiging = false;

            DeviceScanner.stopSmartConnection();
            try {
                if (soundPlayerHint != null) {
                    soundPlayerHint.stop(); // 关闭提示音
                }

                if (soundPlayer != null) {
                    soundPlayer.stop(); // 关闭声音
                }
            } catch (Exception e) {

            }

            if (lLayoutWifiInputPage.getVisibility() == View.VISIBLE) {
                //Intent intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                finish();

            } else if (llayoutSLSearchingPage.getVisibility() == View.VISIBLE) {

                DeviceScanner.stopSmartConnection();
                showInputPage();
                StopSearchDevice();
            }
        }
        return false;
    }

    // ///////////////////////////////////add by luo 20150407
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {

            if (msg.arg1 == WIFI_CONNECT2) {
                if (!bWifiOpen) {
                    if (progressDialog != null) { // 如果wifi列表没刷新出来
                        progressDialog.dismiss();
                        Toast.makeText(SmartLinkQuickWifiConfigActivity.this,
                                getString(R.string.wifiListingFail),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }

            // 设备收索超时
            if (msg.arg1 == SEEK_DEVICE_OVERTIME) {
                // 超时处理
                nTimeoutDetectID++;
                searchAminatView.stopAnimate();
                try {
                    if (isLanguage() && soundPlayerHint != null) {
                        soundPlayerHint.stop(); //
                    }

                    if (soundPlayer != null) {
                        soundPlayer.stop(); //
                    }
                } catch (Exception e) {

                }

                bIsConfiging = false;
                DeviceScanner.stopSmartConnection();
                showInputPage();

                ShowAlert(getString(R.string.snartLinkFailTitle),
                        getString(R.string.snartLinkFailHint));

            }

            // wifi连接成功
            if (msg.arg1 == WIFI_CONNECT) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            // wifi连接失败
            if (msg.arg1 == WIFI_NOT_CONNECT) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                Toast.makeText(SmartLinkQuickWifiConfigActivity.this,
                        getString(R.string.connect_wifi_off),
                        Toast.LENGTH_SHORT).show();
            }

            // 搜索设备成功
            if (msg.arg1 == LocalDefines.DEVICE_SEARCH_RESULT) {
                nTimeoutDetectID++;
                bIsConfiging = false;

                DeviceScanner.stopSmartConnection();
                StopSearchDevice();
                DeviceInfo info = null;
                switch (msg.arg2) {
                    case LocalDefines.DEVICE_SEARCH_RESULT_OK:

                        if (deviceList != null && deviceList.size() > 0) {

                            // 成功搜索到设备
                            nTimeoutDetectID++;
                            if (soundPlayer != null) {
                                soundPlayer.stop();
                            }

                            lLayoutWifiInputPage.setVisibility(View.VISIBLE);
                            llayoutSLSearchingPage.setVisibility(View.GONE);
                            StopSearchDevice(); // 停止搜索设备

                            Toast toast = Toast.makeText(SmartLinkQuickWifiConfigActivity.this,
                                    "配置完成", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            MainActivity.deviceInfo = deviceList.get(0);

                            //Intent intent = new Intent(SmartLinkQuickWifiConfigActivity.this, MainActivity.class);
                            //startActivity(intent);

                            finish();

                        } else {
                            Toast toast = Toast.makeText(
                                    SmartLinkQuickWifiConfigActivity.this,
                                    getString(R.string.no_dev_found),
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            StartSearchDevice(); // 开始搜索设备
                        }

                        break;
                    case LocalDefines.DEVICE_SEARCH_RESULT_FAIL:

                        Toast toast = Toast.makeText(
                                SmartLinkQuickWifiConfigActivity.this,
                                getString(R.string.no_dev_found),
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                }

            } else if (msg.arg1 == LocalDefines.BIND_DEVICE_RESULT_CODE) {
                String searchResultMsg = getString(R.string.add_device);
                Bundle bundle = msg.getData();
                DeviceInfo info2 = (DeviceInfo) bundle
                        .getParcelable("Bind_info");
                int index = bundle.getInt("Bind_index");
                if (msg.arg2 == 0) {

//                    if (!DatabaseManager.IsInfoExist(info2)) {
//                        bNewDevFound = true;
//                        if (DatabaseManager.AddServerInfo(info2)) {
//
//                            if (index == 0) {
//                                searchResultMsg = searchResultMsg
//                                        + info2.getStrName();
//                            } else {
//                                searchResultMsg = searchResultMsg + ", "
//                                        + info2.getStrName();
//                            }
//                        }
//                    } else {// 如果设备已存在，则更新一下ip地址和端口信息
//                        if (info2.getIsAlarmOn() == 0) {
//                            DatabaseManager.UpdateServerInfoState(info2);
//                        } else {
//                            DatabaseManager
//                                    .UpdateServerInfoStateWithAlarmState(info2);
//                        }
//                        bHasUpdate = true;
//
//                    }
                    if (deviceList.size() > 0) {

                        if (bNewDevFound) {
                            // add by luo 20150714
//                            LocalDefines.reloadDeviceInfoList();
//                            LocalDefines.isDeviceListSet = false;
//                            LocalDefines.nClientDeviceSettingThreadID++;
//                            new RegistClientWithDeviceArrayToServer(this,
//                                    LocalDefines.nClientDeviceSettingThreadID)
//                                    .start();
//
//                            LocalDefines.isAlibabaDeviceListSet = false;
//                            LocalDefines.nAlibabaClientDeviceSettingThreadID++;
//                            new AlibabaRegistClientWithDeviceArrayToServer(this, LocalDefines.nAlibabaClientDeviceSettingThreadID).start();
                            // end add by luo 20150714

                            // 成功搜索到设备
                            nTimeoutDetectID++;
                            if (soundPlayer != null) {
                                soundPlayer.stop();
                            }

                            lLayoutWifiInputPage.setVisibility(View.VISIBLE);
                            llayoutSLSearchingPage.setVisibility(View.GONE);
                            StopSearchDevice(); // 停止搜索设备
                            //Intent intent = new Intent(SmartLinkQuickWifiConfigActivity.this, MainActivity.class);
                            //startActivity(intent);

                            finish();

                        } else {
                            Toast toast_1 = Toast.makeText(
                                    SmartLinkQuickWifiConfigActivity.this,
                                    getString(R.string.search_finish),
                                    Toast.LENGTH_SHORT);
                            toast_1.setGravity(Gravity.CENTER, 0, 0);
                            toast_1.show();

                            StartSearchDevice(); // 开始搜索设备
                        }
                    }
                    // 已存在

                } else if (msg.arg2 == DEVICE_IS_EXISTANCE) {
//                    if (info2.getIsAlarmOn() == 0) {
//                        DatabaseManager.UpdateServerInfoState(info2);
//                    } else {
//                        DatabaseManager
//                                .UpdateServerInfoStateWithAlarmState(info2);
//                    }

                }
//                else if (msg.arg2 == HttpUtils.RESULT_CODE_ERROR_IDENTITY) {
//                    httpResult401();
//                }
//                else if (msg.arg2 == HttpUtils.RESULT_CODE_SERVER_ERROR) {
//                    Toast.makeText(SmartLinkQuickWifiConfigActivity.this,
//                            getString(R.string.str_server_error),
//                            Toast.LENGTH_SHORT).show();
//                }
//                else if (msg.arg2 == HttpUtils.NEWERROR) {
//                    Toast.makeText(SmartLinkQuickWifiConfigActivity.this, R.string.Network_Error, Toast.LENGTH_SHORT).show();
//                }
                else {
                    Toast.makeText(SmartLinkQuickWifiConfigActivity.this, R.string.str_bind_device_error, Toast.LENGTH_SHORT).show();
                }
            }

        }

    };

    // ////////add by mai 2015-4-10 用于搜索设备\

    private int m_nSearchID = 0;//
    private boolean bIsSearching = false;
    private boolean mIsSearchingMode = false;
    DatagramSocket ipuStationudpSocket = null;
    DatagramSocket ipuAPudpSocket = null;

    // 开始设备搜索
    public boolean StartSearchDevice() {

        try {
            if (!Functions.isNetworkAvailable(this.getApplicationContext())) {// 网络不可用

                Toast toast = Toast.makeText(this.getApplicationContext(),
                        getString(R.string.toast_network_unreachable),
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        startBroadCastUdpThread();

        return true;

    }

    // 停止设备搜索
    public void StopSearchDevice() {

        bIsSearching = false;
        m_nSearchID++;
        mIsSearchingMode = false;
    }

    public void startBroadCastUdpThread() {
        m_nSearchID++;
        bIsSearching = true;
        new BroadCastUdp(m_nSearchID).start();
    }

    // 设备搜索线程
    public class BroadCastUdp extends Thread {

        private int nTreadSearchID = 0;

        public BroadCastUdp(int nSearchID) {
            nTreadSearchID = nSearchID;

        }

        public void run() {

            while (bIsSearching && nTreadSearchID == m_nSearchID) {

                deviceList = DeviceScanner.getDeviceListFromLan(nConfigID);

                if (deviceList != null && deviceList.size() > 0) {

                    Message msg = handler.obtainMessage();
                    msg.arg1 = LocalDefines.DEVICE_SEARCH_RESULT;
                    msg.arg2 = LocalDefines.DEVICE_SEARCH_RESULT_OK;
                    handler.sendMessage(msg);
                }

            }

        }

    }

    // //end add by mai 2015-4-10

    /**
     * 判断是否打开GPS位置信息 add 2016年5月26日
     */
    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            // Toast.makeText(this, "请打开GPS", Toast.LENGTH_SHORT).show();

            View view = View.inflate(this, R.layout.show_alert_dialog, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_title.setText(getString(R.string.str_permission_request));
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_content.setText(getString(R.string.str_hotspot));
            new AlertDialog.Builder(this)
                    //
                    .setView(view)
                    .setNegativeButton(
                            getResources().getString(
                                    R.string.str_permission_neglect), null)
                    .setPositiveButton(
                            getResources().getString(
                                    R.string.str_permission_setting2),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // 转到手机设置界面，用户设置GPS
                                    Intent intent = new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    // startActivityForResult(intent, 0); //
                                    // 设置完成后返回到原来的界面
                                    startActivity(intent);
                                }
                            }).show();
        } else {
//            Intent intentSeekFine = new Intent(this, DeviceQuickConfigActivity.class);
//            this.startActivity(intentSeekFine);
//            this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//            this.finish();
            ;
        }
    }

    /**
     * // 连接指定wifi add  2016年6月21日
     *
     * @param ssid
     * @param psw
     * @param type
     */
    private void connectToSpecifiedWifi(String ssid, String psw, int type) {
        mWiFiAdnim.addNetWork(mWiFiAdnim.CreateWifiInfo(ssid, psw, type)); // 连接wifi
    }

    /**
     * 判断加密方式 add 2016年6月21日
     *
     * @param capabilities
     * @return
     */
    private int encryCodeOfCapabilities(String capabilities) {
        int nResult = 0;
        if (capabilities == null) {
            return 1;
        }
        if (capabilities.indexOf("WPA2") != -1) {
            nResult = 3;
        } else if (capabilities.indexOf("[WPA-PSK-TKIP+CCMP]") != -1
                || capabilities.indexOf("[WPA2-PSK-TKIP+CCMP]") != -1) {
            nResult = 3;
        } else if (capabilities.indexOf("[WEP]") != -1
                && capabilities.indexOf("[IBSS]") != -1) {
            nResult = 2;

        } else if (capabilities.indexOf("[WEP]") != -1) {
            nResult = 2;

        } else if (capabilities.indexOf("[WPA-PSK-CCMP]") != -1
                || capabilities.indexOf("[WPA-PSK-TKIP+CCMP]") != -1) {

            nResult = 3;

        } else if (capabilities.indexOf("[WPA2-PSK-CCMP]") != -1
                || capabilities.indexOf("[WPA2-PSK-TKIP+CCMP]") != -1) {
            nResult = 3;

        } else if (capabilities.indexOf("[ESS]") != -1) {
            nResult = 1;
        }
        return nResult;
    }


    public void StartBindDeviceThread(int DeviceId, String DeviceName,
                                      String DevicePassword, DeviceInfo info, int infoIndex) {
        n_BindDeviceThreadID++;
        new BindDeviceThread(n_BindDeviceThreadID, DeviceName, DevicePassword,
                handler, DeviceId, info, infoIndex).start();
    }

    class BindDeviceThread extends Thread {
        private int m_BindDeviceThreadID;
        private Handler handler;
        private int m_DeviceId;
        private String m_DeviceName;
        private String m_DevicePassword;
        private DeviceInfo info;
        private int infoIndex;

        public BindDeviceThread(int BindDeviceThreadID, String DeviceName,
                                String DevicePassword, Handler handler, int DeviceId,
                                DeviceInfo info, int infoIndex) {
            this.m_BindDeviceThreadID = BindDeviceThreadID;
            this.handler = handler;
            this.m_DeviceId = DeviceId;
            this.m_DeviceName = DeviceName;
            this.m_DevicePassword = DevicePassword;
            this.info = info;
            this.infoIndex = infoIndex;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            if (m_BindDeviceThreadID == n_BindDeviceThreadID) {
                try {
                    postBindDeviceData(m_DeviceId, m_DeviceName,
                            m_DevicePassword, info, infoIndex);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Message msg = handler.obtainMessage();
            msg.arg1 = LocalDefines.BIND_DEVICE_RESULT_CODE;
            msg.arg2 = bindDevice_result;
            Bundle data = new Bundle();
            data.putParcelable("Bind_info", info);
            data.putInt("Bind_index", infoIndex);
            msg.setData(data);
            handler.sendMessage(msg);

        }
    }

    public void postBindDeviceData(int DeviceId, String DeviceName,
                                   String DevicePassword, DeviceInfo info, int infoIndex)
            throws JSONException {
//        long time = System.currentTimeMillis();
//
//        String LoginSign = "accesstoken=" + DeviceListViewFragment._Token
//                + "&deviceaccount=" + DeviceName + "&deviceid=" + DeviceId
//                + "&devicepassword=" + DevicePassword + "&timestamp=" + time
//                / 1000 + "hsshop2016";
//        String MDLoginSign = LoginActivity.md5(LoginSign);
//
//        JSONObject json = new JSONObject();
//        json.put("sign", MDLoginSign);
//        json.put("timestamp", time / 1000);
//        json.put("accesstoken", DeviceListViewFragment._Token);
//        json.put("deviceid", DeviceId);
//        json.put("deviceaccount", DeviceName);
//        json.put("devicepassword", DevicePassword);
//        String content = json.toString();
//
//        String strURL = HttpUtils.HTTP_REQUEST_PREFIX + "device/bind";
//        String Recresult = HttpUtils.HttpPostData(strURL, content);
//
//        if (Recresult != null) {
//            JSONObject Bindjson = new JSONObject(Recresult);
//            String bindresult = Bindjson.getString("result");
//            int result = Integer.valueOf(bindresult);
//            bindDevice_result = result;
//            if (result == 0) {
//                int bindTime = Integer.valueOf(Bindjson.getInt("update_timestamp"));
//                DeviceListViewFragment.SaveUpdateDeviceTime(bindTime);
//            }
//        }
    }

    private void httpResult401() {
//        View view = View.inflate(this, R.layout.show_alert_dialog, null);
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
//        tv_title.setText(getString(R.string.str_Notic_Close_APP));
//        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
//        // tv_content.setVisibility(View.GONE);
//        tv_content.setText(getString(R.string.str_401));
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                //
//                .setView(view)
//                .setPositiveButton(getString(R.string.alert_btn_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//                                // 修改登录状态
//                                SharedPreferences shareAppMode = getSharedPreferences(
//                                        "ShareAPPMODE", Activity.MODE_PRIVATE);
//                                Editor modeEditor = shareAppMode.edit();
//                                modeEditor.putInt("GetModeNum", 0);
//                                modeEditor.commit();
//                                HomePageActivity.AppMode = 0;
//
//                                // 修改时间戳为0
//                                SharedPreferences shareLocalTime = getSharedPreferences("SaveTimeTamp",
//                                        Activity.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = shareLocalTime.edit();
//                                editor.putInt("TimeTamp", 0);
//                                editor.commit();
//
//                                // 跳转到登录界面，并结束当前活动
//                                Intent intent = new Intent(
//                                        SmartLinkQuickWifiConfigActivity.this,
//                                        LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//
//                            }
//                        }).create();
//        dialog.setCancelable(false);
//        dialog.show();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }
        return super.onTouchEvent(event);
    }

}
