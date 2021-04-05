package com.macrovideo.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyfisheyepano.GLFisheyeView;
//import com.macrovideo.pull.lib.CheckSwitchDecodeButton;
//import com.macrovideo.pull.lib.CheckSwitchDecodeCsButton;
import com.macrovideo.sdk.media.IPlaybackCallback;
import com.macrovideo.sdk.objects.RecordVideoInfo;
import com.macrovideo.sdk.defines.Defines;
import com.macrovideo.sdk.media.ITimeTextCallback;
import com.macrovideo.sdk.media.LibContext;
import com.macrovideo.sdk.media.LoginHandle;
import com.macrovideo.sdk.media.NVPanoPlayer;
import com.macrovideo.sdk.media.Player;
import com.macrovideo.sdk.media.VideoDecoder;
import com.macrovideo.sdk.tools.Functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tencent.android.tpush.TypeStr.msg;

@SuppressLint("NewApi")
public class NVPlayerPlaybackFishEyeActivity extends Activity implements View.OnClickListener, OnSeekBarChangeListener, OnTouchListener {

    static final short SHOWCODE_LOADING = 1001;// 正在加载
    static final short SHOWCODE_NEW_IMAGE = 1002;// 新图片
    static final short SHOWCODE_VIDEO = 1004;// 提示
    static final short SHOWCODE_STOP = 2001;// 停止播放
    static final short SHOWCODE_HAS_DATA = 3001;// 有数据

    static final short STAT_CONNECTING = 2001;// 正在连接服务器
    static final short STAT_LOADING = 2002;// 正在加载视频
    static final short STAT_DECODE = 2003;// 解码
    static final short STAT_STOP = 2004;// 停止
    static final short STAT_DISCONNECT = 2005;// 连接断开
    static final short STAT_RESTART = 2006;// 重新连接
    static final short STAT_MR_BUSY = 2007;// 重新连接
    static final short STAT_MR_DISCONNECT = 2008;// 重新连接

    private String m_strFileName = "";

    private String m_strIP = "127.0.0.1";
    private String m_strLanIP = "127.0.0.1";

    private int m_nPort = 5050;
    private String m_strUsername = "admin";
    private String m_strPassword = "";

    private String m_strMRServer = "";
    private int m_nMRPort = 80;
    private boolean mPlaySound = true;
    private ImageView mBtnBack, mBtnBackHorizontal; // alter by mai 2015-4-29
    private Button mBtnStopAndPlay, mBtnStopAndPlayHorizontal;// alter by mai
    // 2015-4-29
    private Button mBtnSound, mBtnSoundHorizontal; // alter by mai 2015-4-29

    private TextView mTVTopServer = null;
    private LinearLayout layoutTopBar = null;
    private boolean mIsPlaying = false;
    private boolean mIsToPlay = false;
    private RelativeLayout layoutBottomBar;
    private LinearLayout layoutCrossScreenMode;
    private LinearLayout LayoutSeekBar;
    private LinearLayout layoutCenter = null;
    private boolean mIsOnDropUp = true;
    private boolean mIsFinish = false;

    private boolean m_bFinish = false;
    int mScreenWidth = 0;// 屏幕宽
    int mScreenHeight = 0;// 屏幕高

    // ====add by mai 2015-1-30=====
    private TextView tvStartTime, tvStopTime, tvStartTimeHorizontal, tvStopTimeHorizontal;
    private Button btnLastFile, btnNextFile, btnCatpure;
    private Button btnLastFileHorizontal, btnNextFileHorizontal, btnCatpureHorizontal; // add
    // by
    // mai
    // 2015-4-29
    private int listID = 0;
    private int nPlayerFileTime;
    private int nPlayerTime = 0;
    // ====end add by mai 2019-1-30===
    // add by mai 2015-3-16
    private View iamgeViewConctentView = null;
    private Dialog screenshotDialog = null;
    Bitmap bm = null;
    // end add by mai 2015-3-16

    // NVMediaPlayer[] glViews = { null, null, null, null };// add by lin
    // 20151211

    LinearLayout container = null;
    private int nScreenOrientation = Configuration.ORIENTATION_PORTRAIT;

    private Button btnRepeat = null;
    private SeekBar seekBarPlayProgress = null, seekBarPlayProgressHorizontal = null; // alter
    private NVPanoPlayer mvMediaPlayer = null;
    private TextView mTvRealTimeDisplay = null;
    private LoginHandle deviceParam = null;// add 2015-12-1

    // add by mai 2015-4-29
    private LinearLayout bottomButtonHorizontal;
    private RelativeLayout bottomButton, RlPlayerDevice;
    private boolean bAnyway = true;
    private LinearLayout llVideoPalyBakc, llVideoPalyBakcHorizontal;

    // add by xie 2016-6-7
    private TextView Time_display_view;

    // add by xie 2016-6-8
    static String endTimetxt, startTimetxt;
    static int time_display, totaltime;
    // end add by mai 2015-8-4-29

    private PopupWindow mPopupExpandMode, mPopupExpandMode2, mPopupDeviceMode;
    private int mPlayMode = 0;

    // 模式选择
    private final int PlayerMode1 = 0; // 原始图
    private final int PlayerMode2 = 3; // 筒形图
    private final int PlayerMode3 = 7; // 4分割视角图
    private final int PlayerMode4 = 11; // 1个原图+2个视角图+1个360经纬图
    private final int PlayerMode5 = 6; // 上下180度经纬图
    private final int PlayerModeLand = 12; // 360经纬图
    private final int PlayerModeViewAngel = 4; // 视角图

    private Button mBtnExpandMode, mBtnDeviceMode;
    private Button imgModeViewAngle;
    private Button imgWallOriginal;
    // private Button btnDownLoad;
    private int camType;

    private Button imgCSModeOriginal;
    private Button imgCSMode5;

    private LinearLayout llLandscapeDecode;

    private long mTime;
    private long mLastTime;
    private long mRecTimeLast = 0;
    private boolean mIsSeekBarTouch = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果是返回键,直接返回到桌面
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果点击的是返回按钮

            exitCurrentActivity();
        }
        return false;

    }

    // 设置是否播放声音
    public boolean writeSystemParam() {
        SharedPreferences ap = getSharedPreferences(Defines._fileName, MODE_PRIVATE);
        SharedPreferences.Editor editer = ap.edit();
        editer.putBoolean("sound", mPlaySound);
        editer.commit();
        return true;
    }

    // 提示框显示
    // msg：提示的消息
    private void ShowAlert(String title, String msg) {
        try {
            new AlertDialog.Builder(NVPlayerPlaybackFishEyeActivity.this).setTitle(title).setMessage(msg)
                    .setIcon(R.drawable.icon).setPositiveButton(getString(R.string.alert_btn_OK), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    setResult(RESULT_OK);
                }
            }).show();

        } catch (Exception e) {

        }

    }

    @Override
    public void onPause() {
        if (mIsPlaying) {
            PausePlay();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        nToolsViewShowTickCount = 8;
        timerThreadID++;
        new TimerThread(timerThreadID).start();

        if (!mIsFinish) {
            if (mIsPlaying) {
                ResumePlay();
            } else {
                if (mIsToPlay) {
                    startPlay();
                } else {
                    stopPlay(true);
                }
            }
        }

        // OnPlayersResume(); // 重新播放
        m_bFinish = false;
        NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiManager.cancelAll();

    }

    @Override
    public void onDestroy() {
        // @@System.out.println("onDestroy");//add for test
        mvMediaPlayer = null;
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStop() {
        timerThreadID++;
        if (!m_bFinish) {// 如果是按下了home键导致的停止，就保存当期的数据
            NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.app_notice))
                    .setTicker(getString(R.string.app_name))
                    .setWhen(System.currentTimeMillis());
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;

            if (Build.VERSION.SDK_INT >= 23) {
                builder.setSmallIcon(R.drawable.my_device_3);
                builder.setLargeIcon(Functions.readBitMap(this, R.drawable.icon));
            } else {
                builder.setSmallIcon(R.drawable.icon_1);
            }

            Intent intent = new Intent(this, NVPlayerPlaybackFishEyeActivity.class);
            Bundle data = new Bundle();
            if (isPlayFishEyeFromCloud) {
                data.putParcelable("login_handle", mLoginHandle);
                data.putInt("play_index", mListIndex);

            } else {
                data.putParcelable("login_handle", deviceParam);
                data.putInt("play_index", listID);

            }
            intent.putExtras(data);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            notiManager.notify(LocalDefines.notificationID, builder.build());

            LibContext.stopAll();

        } else {
            LibContext.stopAll();// add by lin 2015-12-11
            LibContext.ClearContext();
        }
        m_bFinish = true;
        super.onStop();
    }

    // 横屏
    private void ShowLandscapeView() {
        seekBarPlayProgress.setVisibility(View.GONE);
        tvStartTime.setVisibility(View.GONE);
        tvStopTime.setVisibility(View.GONE);
        // 是否显示硬软解切换开关
//        if (mCbHWDecode.getVisibility() == View.VISIBLE) {
//            // 只有手机支持硬解才显示此选项
//
//        } else {
//            // 手机不支持硬解则不显示选项
//            llLandscapeDecode.setVisibility(View.GONE);
//        }
        synchronized (this) {
            if (mPopupExpandMode != null && mPopupExpandMode.isShowing()) {
                mPopupExpandMode.dismiss();
            }

            nToolsViewShowTickCount = 5;
            bAnyway = false; // add by mai 2015-4-29
            hideToolsViews();

            int nWidth = mScreenWidth;
            int nHeight = mScreenHeight;
            double dWidth = nHeight * 1.7777777;
            if (dWidth < nWidth)
                nWidth = (int) dWidth;

            if (layoutCenter != null) {
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(nWidth, nHeight);
                rlp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                layoutCenter.setLayoutParams(rlp);
                layoutCenter.setPadding(0, 0, 0, 0);

            }

            container.setPadding(0, 0, 0, 0);

            nScreenOrientation = Configuration.ORIENTATION_LANDSCAPE;
            mvMediaPlayer.onOreintationChange(nScreenOrientation);
            mvMediaPlayer.getGLFisheyeView().setMode(PlayerModeLand);
        }
    }

    // 竖屏
    private void ShowPortrailView() {

        // 重新获取屏幕高度，针对虚拟键可以隐藏的手机
        //seekBarPlayProgress.setVisibility(View.VISIBLE);
        llLandscapeDecode.setVisibility(View.GONE);
        synchronized (this) {
            // dip转化为px
            int padding_in_dp = 45; // add by xie 2016-6-1
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

            bAnyway = true; // add by mai 2015-4-29
            showToolsViews();

            int nWidth = mScreenWidth;

            int statusBarHeight = LocalDefines.getStatusBarHeight(this);
            int nHeight = (int) (mScreenHeight - (185 * scale + 0.5f) - statusBarHeight);
            if (layoutCenter != null) {
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(nWidth, nHeight);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                layoutCenter.setLayoutParams(rlp);
                layoutCenter.setPadding(0, padding_in_px, 0, 0);
            }

            int padding = (int) ((nHeight - padding_in_px - nWidth * 1.1) / 2);
            if (padding > 0) {
                container.setPadding(0, padding, 0, padding);
            }

            nScreenOrientation = Configuration.ORIENTATION_PORTRAIT;
            mvMediaPlayer.onOreintationChange(nScreenOrientation);
            mvMediaPlayer.getGLFisheyeView().setMode(mPlayMode);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {

        super.onConfigurationChanged(config);//

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;// 屏幕宽
        mScreenHeight = dm.heightPixels;// 屏幕高
        LocalDefines.loadResource(getResources());// 加载图片资源

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /*
             * If the screen is switched from portait mode to landscape mode
             */
            if (camType == LocalDefines.CAMTYPE_CEIL) {
                layoutCrossScreenMode.setVisibility(View.GONE);

            } else if (camType == LocalDefines.CAMTYPE_WALL) {
                layoutCrossScreenMode.setVisibility(View.VISIBLE);

            }
            ShowLandscapeView();

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            /*
             * If the screen is switched from landscape mode to portrait mode
             */
            layoutCrossScreenMode.setVisibility(View.GONE);
            ShowPortrailView();
        }
        // @@System.out.println("onConfigurationChanged end");//add for test
    }

    // 线程改变UI时调用
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Log.i("demo_test", "msg = " + msg.arg1 + " " + msg.arg2);
            if (msg.arg1 == Defines.MSG_INIT_VIDEO_DECODER_FAIL
                /*&& mCbHWDecode != null*/) {
                //mCbHWDecode.setVisibility(View.INVISIBLE);
                llLandscapeDecode.setVisibility(View.GONE);
                mvMediaPlayer.setHWDecodeStatus(false, false);
                mvMediaPlayer.restartPlayVideo();
                Toast.makeText(NVPlayerPlaybackFishEyeActivity.this, getString(R.string.str_use_hw_fail), Toast.LENGTH_SHORT).show();
                return;
            }

            // add by mai 20151316
            if (msg.arg1 == Defines.MSG_SCREENSHOT) {
                return;
            }
            // end add by mai 2015316

            // @@System.out.println("handleMessage");//add for test
            if (msg.arg1 == Defines.MSG_HIDE_TOOLVIEW) {
                if (nScreenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    hideToolsViews();
                }

            } else if (msg.arg1 == Defines.MSG_SET_SEEKBAR_VALUE) {
                mIsOnDropUp = false;
                seekBarPlayProgress.setProgress(msg.arg2);
                seekBarPlayProgressHorizontal.setProgress(msg.arg2); // add by
                nPlayerTime++;
                String strPlayer = null;
                if (nPlayerTime >= 60) {
                    strPlayer = (nPlayerTime / 60) + ":" + (nPlayerTime % 60);

                } else {
                    strPlayer = "00:" + nPlayerTime;
                }

                startTimetxt = strPlayer;
                tvStartTime.setText(strPlayer);
                tvStartTimeHorizontal.setText(strPlayer); // add by mai
            }

        }
    };
    private GestureDetector mGestureDetector;

    //
    static final int SIZE_CMDPACKET = 128;
    static final int SEND_BUFFER_SIZE = 512;
    static final int SESSION_FRAME_BUFFER_SIZE = 65536;
    static final int SEND_BUFFER_DATA_SIZE = 504; // 512 - 8 (header)
    static final int SEND_BUFFER_HEADER_SIZE = 8;
    static final int SP_DATA = 0x7f;

    static final int CMD_REQUEST = 0x9101;
    static final int CMD_AFFIRM = 0x9102;
    static final int CMD_EXIT = 0x9103;
    static final int CMD_ACCEPT = 0x9104;
    static final int CMD_CONNECTINFO = 0x9105;
    static final int CMD_STREAMHEAD = 0x9106;
    static final int CMD_UDPSHAKE = 0x9107;
    static final int CMD_ASKFORCNLNUM = 0x9108;
    static final int CMD_CHECKPSW = 0x9109;

    private boolean isPlayFishEyeFromCloud;
    private int mDeviceId;
    private String mAccessToken;
    private int mListIndex;
    private int mUserId;
    private String mEcsIP;
    private String mEcsIP2;
    private int mEcsPort;
    private int mEcsPort2;
    private LoginHandle mLoginHandle;

//    private CheckSwitchDecodeCsButton mCbHWDecode;
//    private CheckSwitchDecodeButton mCbHWDecodeLand;

    private boolean mIsHWChangedFromPortrait = false;
    private boolean mIsHWChangedFromLandscape = false;
    private SharedPreferences mHWConfigPrefer;

    // 获取图像
    // 获取图像

    /**
     * Called when the activity is first created.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保持屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiManager.cancel(LocalDefines.notificationID);
        setContentView(R.layout.activity_nvplayer_fisheye_playbackview);

        // 注册广播接收器
        registerReceiver(this);

        // 创建新的图片
        DisplayMetrics dm = getResources().getDisplayMetrics();

        mScreenWidth = dm.widthPixels;// 屏幕宽
        mScreenHeight = dm.heightPixels;// 屏幕高

        init();
        Bundle data = this.getIntent().getExtras();
        if (data != null) {
            // ===add by mai 2015-1-30===

            isPlayFishEyeFromCloud = data.getBoolean("isPlayFishEyeFromCloud");
            if (isPlayFishEyeFromCloud) {
                mListIndex = data.getInt("play_index");
                mDeviceId = data.getInt("deviceId");
                mAccessToken = data.getString("accesstoken");

                mUserId = data.getInt("user_id");
                mEcsIP = data.getString("ecs_ip");
                mEcsIP2 = data.getString("ecs_ip2");
                mEcsPort = data.getInt("ecs_port");
                mEcsPort2 = data.getInt("ecs_port2");

                mLoginHandle = data.getParcelable(Defines.CLOUD_RECORD_FILE_RETURN_MESSAGE);
                camType = mLoginHandle.getCamType();
            } else {
                listID = data.getInt("play_index"); // 点击文件列表下标
                deviceParam = data.getParcelable("login_handle"); // 文件获取得到的局部类LoginHandle
                camType = data.getInt("cam_type");
            }

        }

        mTVTopServer = (TextView) findViewById(R.id.tvPlayerDevice);
        mTVTopServer.setText(m_strFileName);

        layoutTopBar = (LinearLayout) findViewById(R.id.linearLayoutTopBar);
        layoutCenter = (LinearLayout) findViewById(R.id.playbackContainer);
        layoutBottomBar = (RelativeLayout) findViewById(R.id.linearLayoutBottomBar);
        layoutCrossScreenMode = (LinearLayout) findViewById(R.id.PB_cross_screen_mode);
        LayoutSeekBar = (LinearLayout) findViewById(R.id.bg_seekbar);
        LayoutSeekBar.getBackground().setAlpha(95);
        container = (LinearLayout) findViewById(R.id.playbackContainer1);
        // 创建播放器
        mvMediaPlayer = new NVPanoPlayer(this, false); // 2017-5-25 edit
        mvMediaPlayer.setTvTimeOSD(new ITimeTextCallback() {

            @Override
            public void setTimeText(final String timeText) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.i("demo_test", "timeText = " + timeText);
                        mTvRealTimeDisplay.setText(timeText);
                    }
                });
            }
        });
        mvMediaPlayer.setTimeCallback(new IPlaybackCallback() {
            @Override
            public void setTime(final long time) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("demo_test", "time = " + time);

                        if (time > 0) {

                            if (mTime == 0) {


                                if (Math.abs(time - mLastTime) < 1000) {
                                    return;
                                }

                                mTime = time;
                                mRecTimeLast = time;

                                seekBarPlayProgress.setProgress(100);
                                seekBarPlayProgressHorizontal.setProgress(100);
                            } else {
                                if (Math.abs(time - mLastTime) > 10000) {
                                    mTime = time;
                                }

                                long mRecStartTime = LocalDefines.listMapPlayerBackFile.get(listID).getRecSegment().getNvtStartTime().getlTime();
                                long mRecEndTime = LocalDefines.listMapPlayerBackFile.get(listID).getRecSegment().getNvtEndTime().getlTime();
                                float mTimeLen = mRecEndTime - mRecStartTime;

                                if (LocalDefines.Device_LoginHandle.getVersion() >= Functions.SETTING_VERSION_UP_ONE) {
                                    if (seekBarPlayProgress != null && seekBarPlayProgressHorizontal != null) {

                                        float currentTime = mTimeLen * seekBarPlayProgress.getProgress() / 100;
                                        long nCurrentTime = (long) currentTime;

                                        long playInedxTime = mRecStartTime + nCurrentTime;
                                        if (Math.abs(mTimeLen - nCurrentTime) <= 1800) {

                                            return;
                                        }
                                    }
                                    float currentTime = time - mTime;
                                    float progress = currentTime / mTimeLen;
                                    int nProgress = (int) (progress * 100);

                                    if (!mIsSeekBarTouch) {
                                        seekBarPlayProgress.setProgress(nProgress);
                                        seekBarPlayProgressHorizontal.setProgress(nProgress);
                                    }
                                }
                            }

                            mLastTime = time;


                            nPlayerTime++;
                            mIsOnDropUp = false;

                            String strPlayer = null;
                            if (nPlayerTime >= 60) {
                                strPlayer = (nPlayerTime / 60) + ":" + (nPlayerTime % 60);

                            } else {
                                strPlayer = "00:" + nPlayerTime;
                            }

                            startTimetxt = strPlayer;
                            tvStartTime.setText(strPlayer);
                            tvStartTimeHorizontal.setText(strPlayer); // add by mai
                        }

                    }
                });

            }

            @Override
            public void onReceiveFinishMSG(int nSegmentID) {
                Log.i("demo_test", "onReceiveFinishMSG " + nSegmentID
                        + " " + LocalDefines.listMapPlayerBackFile.get(listID).getnFileID()
                        + " " + LocalDefines.listMapPlayerBackFile.get(listID).getRecSegment().getnSegmentID());
                if (nSegmentID == LocalDefines.listMapPlayerBackFile.get(listID).getnFileID()
                        || nSegmentID == LocalDefines.listMapPlayerBackFile.get(listID).getRecSegment().getnSegmentID()) {
                    seekBarPlayProgress.setProgress(100);
                    seekBarPlayProgressHorizontal.setProgress(100);
                }

            }
        });

        GLFisheyeView glFisheyeView = new GLFisheyeView(this);
        glFisheyeView.setMode(mPlayMode);

        mvMediaPlayer.setGlFishView(glFisheyeView);
        if (camType == LocalDefines.CAMTYPE_WALL) {
            // 挂壁鱼眼镜头
            mvMediaPlayer.setFixType(LocalDefines.FIXTYPE_WALL);
        } else if (camType == LocalDefines.CAMTYPE_CEIL) {
            // 吸顶鱼眼镜头
            mvMediaPlayer.setFixType(LocalDefines.FIXTYPE_CEIL);
        }
        mvMediaPlayer.GetHandler(handler);
        mGestureDetector = new GestureDetector(NVPlayerPlaybackFishEyeActivity.this, new BottomMenuGestureListener());
        mvMediaPlayer.getGLFisheyeView().setOnTouchListener(this);
        // 添加播放器到容器
        container.addView(mvMediaPlayer.getGLFisheyeView());

        // 设置环境变量
        LibContext.SetContext(mvMediaPlayer, null, null, null);

        Player.SelectWindow(Player.WND_ID_0);
        initDecodeSwitcher();

        btnRepeat = (Button) findViewById(R.id.btnRepeat1);
        btnRepeat.setOnClickListener(this);
        btnRepeat.setVisibility(View.GONE);

        mBtnExpandMode = (Button) findViewById(R.id.btn_mode);
        mBtnExpandMode.setOnClickListener(this);

        mBtnDeviceMode = (Button) findViewById(R.id.btn_device_camera);
        mBtnDeviceMode.setOnClickListener(this);

        if (camType == LocalDefines.CAMTYPE_CEIL) {
            mBtnDeviceMode.setBackgroundResource(R.drawable.device_ceiling_btn);
        } else if (camType == LocalDefines.CAMTYPE_WALL) {
            mBtnDeviceMode.setBackgroundResource(R.drawable.device_wall_btn);
        }

        mBtnBack = (ImageView) findViewById(R.id.btnPBBackToLogin);
        mBtnBack.setOnClickListener(this);

        mBtnBackHorizontal = (ImageView) findViewById(R.id.btnPBBackToLoginHprizontal);
        mBtnBackHorizontal.setOnClickListener(this);

        mBtnStopAndPlay = (Button) findViewById(R.id.btnPBStopAndPlay);
        mBtnStopAndPlay.setOnClickListener(this);
        mBtnStopAndPlayHorizontal = (Button) findViewById(R.id.btnPBStopAndPlayHorizontal);
        mBtnStopAndPlayHorizontal.setOnClickListener(this);

        mBtnSound = (Button) findViewById(R.id.btnPBAudio);
        mBtnSound.setOnClickListener(this);
        mBtnSoundHorizontal = (Button) findViewById(R.id.btnPBAudioHorizontal);
        mBtnSoundHorizontal.setOnClickListener(this);

        imgCSModeOriginal = (Button) findViewById(R.id.PB_iv_cross_screen_expand_normal);
        imgCSModeOriginal.setOnClickListener(this);
        imgCSMode5 = (Button) findViewById(R.id.PB_iv_cross_screen_expand_mode5);
        imgCSMode5.setOnClickListener(this);

        // 判断是否开启音频
        if (mPlaySound) {
            mBtnSound.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn));
            mBtnSoundHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn_horziontal));
        } else {
            mBtnSound.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn2));
            mBtnSoundHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn_horziontal2));
        }

        seekBarPlayProgress = (SeekBar) findViewById(R.id.seekBarPlayProgress);
        seekBarPlayProgress.setOnSeekBarChangeListener(this);

        seekBarPlayProgress.setMax(100);
        seekBarPlayProgress.setProgress(0);

        seekBarPlayProgressHorizontal = (SeekBar) findViewById(R.id.seekBarPlayProgressHorizontal); // add by mai 2015-4-29
        seekBarPlayProgressHorizontal.setOnSeekBarChangeListener(this); // add by mai 2015-4-29
        seekBarPlayProgressHorizontal.setMax(100); // add by mai 2015-4-29
        seekBarPlayProgressHorizontal.setProgress(0); // add by mai 2015-4-29

        mIsPlaying = false;
        mIsToPlay = true;
        ShowPortrailView();
        createDialogs();
    }

    private void initDecodeSwitcher() {
        llLandscapeDecode = (LinearLayout) findViewById(R.id.ll_pano_playback_land_cwb_decode);
        llLandscapeDecode.setVisibility(View.GONE);
        mHWConfigPrefer = PreferenceManager.getDefaultSharedPreferences(this);
        // 设置当前软硬解状态
        mvMediaPlayer.setHWDecodeStatus(false, false);
    }

    private class HWDecodeCheckChangeListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mIsHWChangedFromPortrait && buttonView.getId() == R.id.CWB_Decode) {
                mIsHWChangedFromPortrait = false;
                return;
            }

            if (mIsPlaying) {
                if (isChecked) {
                    // 选中，硬解
                    mvMediaPlayer.setHWDecodeStatus(isChecked, isChecked);
                } else {
                    // 未选中，使用软解
                    mvMediaPlayer.setHWDecodeStatus(isChecked, isChecked);
                }
                mvMediaPlayer.restartPlayVideo();
            } else {
                if (isChecked) {
                    // 选中，硬解
                    mvMediaPlayer.setHWDecodeStatus(isChecked, isChecked);
                } else {
                    // 未选中，使用软解
                    mvMediaPlayer.setHWDecodeStatus(isChecked, isChecked);
                }
                startPlay();
            }
        }
    }

    /**
     * 初始化数据
     */
    private void init() {
        mTvRealTimeDisplay = (TextView) findViewById(R.id.tv_display_real_time1);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvStopTime = (TextView) findViewById(R.id.tvStopTime);
        tvStartTimeHorizontal = (TextView) findViewById(R.id.tvStartTimeHorizontal);
        tvStopTimeHorizontal = (TextView) findViewById(R.id.tvStopTimeHorizontal);
        btnLastFile = (Button) findViewById(R.id.btnLastFlie);
        btnLastFile.setOnClickListener(this);
        btnLastFileHorizontal = (Button) findViewById(R.id.btnLastFlieHorizontal);
        btnLastFileHorizontal.setOnClickListener(this);

        btnNextFile = (Button) findViewById(R.id.btnNextFile);
        btnNextFile.setOnClickListener(this);
        btnNextFileHorizontal = (Button) findViewById(R.id.btnNextFileHorizontal);
        btnNextFileHorizontal.setOnClickListener(this);

        btnCatpure = (Button) findViewById(R.id.btnCatpure);
        btnCatpure.setOnClickListener(this);
        btnCatpureHorizontal = (Button) findViewById(R.id.btnCatpureHorizontal);
        btnCatpureHorizontal.setOnClickListener(this);

        bottomButtonHorizontal = (LinearLayout) findViewById(R.id.bottomButtonHorizontal);
        bottomButtonHorizontal.getBackground().setAlpha(95);
        bottomButton = (RelativeLayout) findViewById(R.id.bottomButton);

        llVideoPalyBakc = (LinearLayout) findViewById(R.id.llVideoPalyBakc);

        llVideoPalyBakcHorizontal = (LinearLayout) findViewById(R.id.llVideoPalyBakcHorizontal);

        RlPlayerDevice = (RelativeLayout) findViewById(R.id.RlPlayerDevice);
        Time_display_view = (TextView) findViewById(R.id.Time_display);
        Time_display_view.getBackground().setAlpha(95);
    }

    @SuppressWarnings("deprecation")
    private void stopPlay(boolean bFlag) {
        if (seekBarPlayProgress != null) {
            seekBarPlayProgress.setEnabled(false);
        }

        if (seekBarPlayProgressHorizontal != null) { // add by mai 2015-4-29
            seekBarPlayProgressHorizontal.setEnabled(false);
        }
        mIsFinish = false;
        mIsPlaying = false;
        mTVTopServer.setText(m_strFileName);
        if (isPlayFishEyeFromCloud) {
            mvMediaPlayer.StopCloudPlayBack();
        } else {
            mvMediaPlayer.stopPlayBack();// add by lin 20151211
        }
        mvMediaPlayer.pauseAudio();// add by lin 20151211
        mBtnStopAndPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_play_btn));
        mBtnStopAndPlayHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_play_btn_horziontal));
        mIsFinish = true;
    }

    @SuppressWarnings("deprecation")
    private void startPlay() { // 开始播放
        mIsFinish = false;
        mBtnBack.setClickable(true);
        if (btnRepeat != null) {
            btnRepeat.setVisibility(View.GONE);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        mvMediaPlayer.pauseAudio();
        mTVTopServer.setText(m_strFileName);

        if (Player.CurrentSelPlayer() < 4) {
            Player.setPlaying(Player.CurrentSelPlayer(), true);
            mvMediaPlayer.EnableRender();
            // ========add by lin 20151211==========
            RecordVideoInfo recFile = null;
            if (isPlayFishEyeFromCloud) {
                // 云存储全景回放
                recFile = LocalDefines.cloudRecordFileList.get(mListIndex); // 得到要进行播放的文件信息
            } else {
                // sd卡全景回放
                recFile = LocalDefines.listMapPlayerBackFile.get(listID); // 得到要进行播放的文件信息
            }

            if (recFile == null) {
                Log.i("demo_test", "recFile == null");
                return;
            }

            m_strFileName = recFile.getStrFileName();
            mTVTopServer.setText(m_strFileName);
            nPlayerFileTime = recFile.getuFileTimeLen();
            String strTime = "";
            nPlayerTime = 0;
            if (nPlayerFileTime >= 60) {
                strTime = (nPlayerFileTime / 60) + ":" + (nPlayerFileTime % 60);
                // add by xie 2016-6-8
                endTimetxt = strTime;
                totaltime = totalSeconds("00:00", endTimetxt);
            } else {
                strTime = "00:" + nPlayerFileTime;
                endTimetxt = strTime;
                totaltime = totalSeconds("00:00", endTimetxt);
            }

            tvStopTime.setText(strTime);
            tvStopTimeHorizontal.setText(strTime); // add by mai 2015-4-29

            // =======add by lin 2015-12-11=====

            mTime = 0L;

            if (isPlayFishEyeFromCloud) {
                if (mvMediaPlayer.StartCloudPlayBack(0, mUserId, mDeviceId, "123", mAccessToken, /* "120.24.168.18" */mEcsIP, /* 4000 */mEcsPort, recFile, mPlaySound, mLoginHandle)) {
                    mvMediaPlayer.playAudio();
                    mBtnStopAndPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_stop_btn));
                    mBtnStopAndPlayHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_stop_btn_horziontal));
                    mIsPlaying = true;
                }
            } else {
                if (mvMediaPlayer.startPlayBack(0, deviceParam, recFile, mPlaySound)) {
                    mvMediaPlayer.playAudio();
                    mBtnStopAndPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_stop_btn));
                    mBtnStopAndPlayHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_stop_btn_horziontal));
                    mIsPlaying = true;
                }
            }

            if (seekBarPlayProgress != null) {
                seekBarPlayProgress.setEnabled(true);
            }

            if (seekBarPlayProgressHorizontal != null) { // add by mai 2015-4-29
                seekBarPlayProgressHorizontal.setEnabled(true);
            }
        }
    }

    private void PausePlay() {
        if (seekBarPlayProgress != null) {
            seekBarPlayProgress.setProgress(0);
            seekBarPlayProgress.setEnabled(false);

        }

        if (seekBarPlayProgressHorizontal != null) { // add by mai 2015-4-29
            seekBarPlayProgressHorizontal.setProgress(0);
            seekBarPlayProgressHorizontal.setEnabled(false);
        }
        mIsFinish = false;
        mIsPlaying = false;
        mTVTopServer.setText(m_strFileName);
        if (isPlayFishEyeFromCloud) {
            mvMediaPlayer.StopCloudPlayBack();
        } else {
            mvMediaPlayer.stopPlayBack();// add by lin 20151211
        }
        mvMediaPlayer.pauseAudio();// add by lin 20151211
        mBtnStopAndPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_play_btn));
        mBtnStopAndPlayHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_play_btn_horziontal));
        mIsFinish = true;
    }

    private void ResumePlay() {

    }

    // add by xie 2016-9-3
    private void showPopWinPlayerMode(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View playerModeView = layoutInflater.inflate(R.layout.popup_expand_mode_layout, null);
        //
        final Button imgOriginal = (Button) playerModeView.findViewById(R.id.iv_expand_normal);
        final Button imgCylindric = (Button) playerModeView.findViewById(R.id.iv_expand_mode1);
        final Button imgQuad = (Button) playerModeView.findViewById(R.id.iv_expand_mode2);
        final Button imgMode4 = (Button) playerModeView.findViewById(R.id.iv_expand_mode3);
        final Button imgModeLongLatUD = (Button) playerModeView.findViewById(R.id.iv_expand_mode4);
        final Button imgModeViewAngle = (Button) playerModeView.findViewById(R.id.iv_expand_mode5);

        int PopWinWidth = 0;

        if (camType == LocalDefines.CAMTYPE_WALL) {
            imgCylindric.setVisibility(View.GONE);
            imgQuad.setVisibility(View.GONE);
            imgMode4.setVisibility(View.GONE);
            imgModeLongLatUD.setVisibility(View.GONE);
            PopWinWidth = 80;
        }
        if (camType == LocalDefines.CAMTYPE_CEIL) {
            imgModeViewAngle.setVisibility(View.GONE);
            PopWinWidth = 200;
        }

        switch (mPlayMode) {
            case PlayerMode1:
                imgOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode2:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_bg_click);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode3:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_bg_click);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode4:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_bg_click);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode5:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_bg_click);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerModeViewAngel:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_bg_click);
                break;
            default:
                break;
        }
        imgOriginal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imgOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                mPopupExpandMode.dismiss();
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode1);
                mPlayMode = PlayerMode1;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode1_btn);
            }
        });

        imgCylindric.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_bg_click);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                mPopupExpandMode.dismiss();
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode2);
                mPlayMode = PlayerMode2;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode2_btn);
            }
        });

        imgQuad.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_bg_click);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                mPopupExpandMode.dismiss();
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode3);
                mPlayMode = PlayerMode3;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode3_btn);
            }
        });

        imgMode4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_bg_click);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                mPopupExpandMode.dismiss();
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode4);
                mPlayMode = PlayerMode4;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode4_btn);
            }
        });

        imgModeLongLatUD.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_bg_click);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                mPopupExpandMode.dismiss();
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode5);
                mPlayMode = PlayerMode5;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode5_btn);
            }
        });

        imgModeViewAngle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_bg_click);
                mPopupExpandMode.dismiss();
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerModeViewAngel);
                mPlayMode = PlayerModeViewAngel;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode6_btn);
            }
        });

        float density = getResources().getDisplayMetrics().density;
        mPopupExpandMode = new PopupWindow(playerModeView, (int) (PopWinWidth * density + 0.5f), (int) (50 * density + 0.5f));
        mPopupExpandMode.setTouchable(true);
        mPopupExpandMode.setFocusable(true);
        mPopupExpandMode.setBackgroundDrawable(new BitmapDrawable());
        mPopupExpandMode.setAnimationStyle(R.style.popupwindow_expand_mode);

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mPopupExpandMode.showAtLocation(v, Gravity.NO_GRAVITY, (int) (location[0] + mPopupExpandMode.getWidth() * density + 0.5f), (int) (location[1] - mPopupExpandMode.getHeight() - 25 * density + 0.5f));

    }

    private void showPopWinWallPlayerMode(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View playerModeView = layoutInflater.inflate(R.layout.popup_wall_expand_mode_layout, null);
        int PopWinWidth = 80;
        imgModeViewAngle = (Button) playerModeView.findViewById(R.id.iv_wall_expand_mode5);
        imgWallOriginal = (Button) playerModeView.findViewById(R.id.iv_wall_expand_normal);

        switch (mPlayMode) {
            case PlayerMode1:
                imgWallOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerModeViewAngel:
                imgWallOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_bg_click);
                break;
            default:
                imgWallOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
        }
        imgWallOriginal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imgWallOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_transparent);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                mPopupExpandMode2.dismiss();
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode1);
                mPlayMode = PlayerMode1;

                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode1_btn);
            }
        });

        imgModeViewAngle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imgWallOriginal.setBackgroundResource(R.drawable.mode1_transparent);

                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_bg_click);
                mPopupExpandMode2.dismiss();
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerModeViewAngel);
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_bg_click);
                mPlayMode = PlayerModeViewAngel;

                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode6_btn);
            }
        });

        float density = getResources().getDisplayMetrics().density;
        mPopupExpandMode2 = new PopupWindow(playerModeView, (int) (PopWinWidth * density + 0.5f), (int) (50 * density + 0.5f));
        mPopupExpandMode2.setTouchable(true);
        mPopupExpandMode2.setFocusable(true);
        mPopupExpandMode2.setBackgroundDrawable(new BitmapDrawable());
        mPopupExpandMode2.setAnimationStyle(R.style.popupwindow_expand_mode);

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mPopupExpandMode2.showAtLocation(v, Gravity.NO_GRAVITY, (int) (mScreenWidth * 0.7), (int) (location[1] - mPopupExpandMode2.getHeight() - 25 * density + 0.5f));
    }

    private void showPopWinDeviceMode(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View devicerModeView = layoutInflater.inflate(R.layout.popup_device_mode_layout, null);

        final Button btnCeiling = (Button) devicerModeView.findViewById(R.id.iv_device_ceiling);
        final Button btnWall = (Button) devicerModeView.findViewById(R.id.iv_device_wall);

        int PopWinWidth = 80;
        switch (camType) {
            case LocalDefines.CAMTYPE_WALL:
                btnCeiling.setBackgroundResource(R.drawable.ceiling_transparent);
                btnWall.setBackgroundResource(R.drawable.wall_bg_click);

                break;

            case LocalDefines.CAMTYPE_CEIL:
                btnCeiling.setBackgroundResource(R.drawable.ceiling_bg_click);
                btnWall.setBackgroundResource(R.drawable.wall_transparent);
                break;
        }

        btnCeiling.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                camType = LocalDefines.CAMTYPE_CEIL;
                mvMediaPlayer.setFixType(LocalDefines.FIXTYPE_CEIL);
                btnCeiling.setBackgroundResource(R.drawable.ceiling_bg_click);
                btnWall.setBackgroundResource(R.drawable.wall_transparent);
                mBtnDeviceMode.setBackgroundResource(R.drawable.device_ceiling_btn);
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_transparent);
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode1);
                mPlayMode = PlayerMode1;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode1_btn);
                mPopupDeviceMode.dismiss();
            }
        });

        btnWall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                camType = LocalDefines.CAMTYPE_WALL;
                mvMediaPlayer.setFixType(LocalDefines.FIXTYPE_WALL);
                btnCeiling.setBackgroundResource(R.drawable.ceiling_transparent);
                btnWall.setBackgroundResource(R.drawable.wall_bg_click);
                mBtnDeviceMode.setBackgroundResource(R.drawable.device_wall_btn);
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_transparent);
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode1);
                mPlayMode = PlayerMode1;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode1_btn);
                mPopupDeviceMode.dismiss();

            }
        });

        float density = getResources().getDisplayMetrics().density;
        mPopupDeviceMode = new PopupWindow(devicerModeView, (int) (PopWinWidth * density + 0.5f), (int) (50 * density + 0.5f));
        mPopupDeviceMode.setTouchable(true);
        mPopupDeviceMode.setFocusable(true);
        mPopupDeviceMode.setBackgroundDrawable(new BitmapDrawable());
        mPopupDeviceMode.setAnimationStyle(R.style.popupwindow_expand_mode);

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mPopupDeviceMode.showAtLocation(v, Gravity.NO_GRAVITY, (int) (mScreenWidth * 0.7), (int) (location[1] - mPopupDeviceMode.getHeight() - 25 * density + 0.5f));
    }

    @SuppressWarnings("deprecation")
    public void onClick(View v) {

        nToolsViewShowTickCount = 5;

        if (v == null)
            return;
        switch (v.getId()) {

            case R.id.btn_mode:
                // 点击切换播放模式
                if (camType == LocalDefines.CAMTYPE_CEIL) {
                    showPopWinPlayerMode(v);
                } else if (camType == LocalDefines.CAMTYPE_WALL) {
                    showPopWinWallPlayerMode(v);
                }
                break;
            case R.id.btn_device_camera:
                showPopWinDeviceMode(v);
                break;

            case R.id.btnRepeat1:// replay
                stopPlay(true);
                startPlay();
                break;
            case R.id.btnPBBackToLogin: // 返回按钮
            case R.id.btnPBBackToLoginHprizontal: // add by mai 2015-4-29

                exitCurrentActivity();

                break;
            case R.id.btnPBStopAndPlay: // 停止或开启播放
            case R.id.btnPBStopAndPlayHorizontal: // add by mai 2015-4-29
                mIsPlaying = !mIsPlaying;
                if (mIsPlaying) {
                    startPlay();

                } else {
                    stopPlay(true);
                }
                break;

            case R.id.btnPBAudio: // 音频
            case R.id.btnPBAudioHorizontal: // add by mai 2015-4-29
                mPlaySound = !mPlaySound;
                if (mPlaySound) {
                    mBtnSound.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn));
                    mBtnSoundHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn_horziontal));
                    mvMediaPlayer.playAudio();
                } else {
                    mBtnSound.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn2));
                    mBtnSoundHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn_horziontal2));
                    mvMediaPlayer.pauseAudio();
                }

                int nChn = Player.CurrentSelPlayer();
                if (nChn >= 0 && nChn < 4) {
                    mvMediaPlayer.enableAudio(mPlaySound);
                }

                writeSystemParam();
                break;

            case R.id.btnLastFlie: // 上一个文件
            case R.id.btnLastFlieHorizontal: // add by mai 2015-4-29
                if (isPlayFishEyeFromCloud) {
                    // 从云存储过来回放全景录像文件
                    if ((mListIndex - 1) >= 0) { // 如果上一个存在
                        mListIndex = mListIndex - 1;
                        stopPlay(false);
                        final Handler han = new Handler();
                        Runnable runn = new Runnable() {

                            @Override
                            public void run() {

                                if (mIsFinish) { // 如果已经停止
                                    startPlay();
                                } else {
                                    han.postDelayed(this, 1000);
                                }
                            }
                        };

                        han.postDelayed(runn, 1000);
                    } else {
                        Toast.makeText(NVPlayerPlaybackFishEyeActivity.this, getString(R.string.FileFirst), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 从SD卡过来回放全景录像文件
                    if ((listID - 1) >= 0) { // 如果上一个存在
                        listID = listID - 1;
                        stopPlay(false);
                        final Handler han = new Handler();
                        Runnable runn = new Runnable() {

                            @Override
                            public void run() {

                                if (mIsFinish) { // 如果已经停止
                                    startPlay();
                                } else {
                                    han.postDelayed(this, 1000);
                                }

                            }
                        };

                        han.postDelayed(runn, 1000);
                    } else {
                        Toast.makeText(NVPlayerPlaybackFishEyeActivity.this, getString(R.string.FileFirst), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.btnNextFile: // 下一个文件
            case R.id.btnNextFileHorizontal: // add by mai 2015-4-29
                if (isPlayFishEyeFromCloud) {
                    if ((mListIndex + 1) < LocalDefines.cloudRecordFileList.size()) { // 如果下一个
                        mListIndex = mListIndex + 1;
                        stopPlay(true);
                        final Handler han = new Handler();
                        Runnable runn = new Runnable() {

                            @Override
                            public void run() {

                                if (mIsFinish) { // 如果已经停止
                                    startPlay();

                                } else {

                                    han.postDelayed(this, 1000);
                                }

                            }
                        };

                        han.postDelayed(runn, 1000);

                    } else {
                        Toast.makeText(NVPlayerPlaybackFishEyeActivity.this, getString(R.string.FileFinally), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if ((listID + 1) < LocalDefines.listMapPlayerBackFile.size()) { // 如果下一个
                        listID = listID + 1;
                        stopPlay(true);
                        final Handler han = new Handler();
                        Runnable runn = new Runnable() {

                            @Override
                            public void run() {

                                if (mIsFinish) { // 如果已经停止
                                    startPlay();

                                } else {

                                    han.postDelayed(this, 1000);
                                }

                            }
                        };

                        han.postDelayed(runn, 1000);

                    } else {
                        Toast.makeText(NVPlayerPlaybackFishEyeActivity.this, getString(R.string.FileFinally), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.btnCatpure: // 截图
            case R.id.btnCatpureHorizontal: // add by mai 2015-4-29
                // 检查 存储控件权限
                if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
                    break;
                }
                screenshotDialog.show();
                ScreenShot();
                break;
            case R.id.PB_iv_cross_screen_expand_normal:
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_bg_click);

                imgCSMode5.setBackgroundResource(R.drawable.mode6_transparent);
                mBtnExpandMode.setBackgroundResource(R.drawable.mode1_bg_click);
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerMode1);
                mPlayMode = PlayerMode1;

                break;
            case R.id.PB_iv_cross_screen_expand_mode5:
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_bg_click);
                mBtnExpandMode.setBackgroundResource(R.drawable.mode6_bg_click);
                mvMediaPlayer.getGLFisheyeView().setMode(PlayerModeViewAngel);
                mPlayMode = PlayerModeViewAngel;
                break;
            default:
                break;
        }

    }

    private static final int MY_PERMISSION_REQUEST_STORAGE = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意使用camera
            } else {
                // 用户不同意，自行处理即可
                new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.str_permission_request))
                        .setMessage(getResources().getString(R.string.str_permission_storage1)).setNegativeButton(getResources().getString(R.string.str_permission_neglect), null).setPositiveButton(getResources().getString(R.string.str_permission_setting2), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        Uri uri = Uri.fromParts("package", NVPlayerPlaybackFishEyeActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }).show();
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String time_display = getCheckTimeBySeconds((totaltime * progress / 100), "00:00");
        Time_display_view.setText(time_display);

        if (!mIsOnDropUp && mIsPlaying && progress >= 100) {
            btnRepeat.setVisibility(View.VISIBLE);
            mIsPlaying = false;
            mIsPlaying = false;
            mTVTopServer.setText(m_strFileName);
            mvMediaPlayer.pauseAudio();

            mBtnStopAndPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_play_btn));
            mBtnStopAndPlayHorizontal.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_play_btn_horziontal));
            mvMediaPlayer.FinishPlayback();
            mIsFinish = true;
            seekBarPlayProgressHorizontal.setEnabled(false);
            seekBarPlayProgress.setEnabled(false);
        }

        if (seekBarPlayProgress != null) {
            if (seekBarPlayProgress.getProgress() == 100) {
                seekBarPlayProgress.setProgress(0);
                Time_display_view.setVisibility(View.GONE);
            }
        }

        if (seekBarPlayProgressHorizontal != null) { // add by mai 2015-4-29
            if (seekBarPlayProgressHorizontal.getProgress() == 100) {
                seekBarPlayProgressHorizontal.setProgress(0);
                Time_display_view.setVisibility(View.GONE);
            }
        }

        mIsOnDropUp = true;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Time_display_view.setVisibility(View.VISIBLE);
        if (LocalDefines.Device_LoginHandle.getVersion() >= Functions.SETTING_VERSION_UP_ONE) {
            mIsSeekBarTouch = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Time_display_view.setVisibility(View.GONE);
        mIsSeekBarTouch = false;
        mTime = 0L;
        int nValue = seekBar.getProgress();
        mvMediaPlayer.setPlayBackProgress(nValue);
    }

    // 横竖屏切换
    private void showToolsViews() {
        nToolsViewShowTickCount = 5;

        layoutTopBar.setVisibility(View.VISIBLE);
        // add by mai 2015-4-29
        if (bAnyway) // 如果是竖屏
        {

            bottomButton.setVisibility(View.VISIBLE);
            bottomButtonHorizontal.setVisibility(View.GONE);
            llVideoPalyBakc.setVisibility(View.VISIBLE);
            llVideoPalyBakcHorizontal.setVisibility(View.GONE);
            RlPlayerDevice.setVisibility(View.VISIBLE);

        } else {
            layoutBottomBar.setVisibility(View.VISIBLE);
            if (camType == LocalDefines.CAMTYPE_WALL) {
                layoutCrossScreenMode.setVisibility(View.VISIBLE);
            }
            bottomButtonHorizontal.setVisibility(View.VISIBLE);
            bottomButton.setVisibility(View.GONE);
            llVideoPalyBakc.setVisibility(View.GONE);
            llVideoPalyBakcHorizontal.setVisibility(View.VISIBLE);
            RlPlayerDevice.setVisibility(View.GONE);
        }
        // end add by mai 2015-4-29

    }

    // 竖屏
    private void hideToolsViews() {

        nToolsViewShowTickCount = 0;
        layoutBottomBar.setVisibility(View.GONE);

        layoutCrossScreenMode.setVisibility(View.GONE);

        layoutTopBar.setVisibility(View.GONE);
        // add by mai 2015-4-29
        bottomButtonHorizontal.setVisibility(View.GONE);
        bottomButton.setVisibility(View.GONE);
        // end add by mai 2015-4-29
    }

    // /timer task
    private int nToolsViewShowTickCount = 8;

    private int timerThreadID = 0;

    // 延迟操作线程
    class TimerThread extends Thread {

        int mThreadID = 0;

        public TimerThread(int nThreadID) {
            mThreadID = nThreadID;
        }

        public void run() {
            while (mThreadID == timerThreadID) {

                nToolsViewShowTickCount -= 1;
                if (nToolsViewShowTickCount <= 0 && mThreadID == timerThreadID) {
                    Message message = new Message();
                    message.arg1 = Defines.MSG_HIDE_TOOLVIEW;
                    handler.sendMessage(message);
                    nToolsViewShowTickCount = 0;
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    private void createDialogs() { // 创建登陆时 ，进度条对话框

        // add by luo 20141226
        iamgeViewConctentView = LayoutInflater.from(this).inflate(R.layout.screenshotdialog, null);
        screenshotDialog = new Dialog(this, R.style.progressDialog);
        screenshotDialog.setContentView(iamgeViewConctentView);
        screenshotDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub

                Message msg = handler.obtainMessage();
                msg.arg1 = Defines.MSG_SCREENSHOT;
                handler.sendMessage(msg);
            }
        });

        screenshotDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }

        });
    }

    // add by luo 20141115 屏幕截图
    @SuppressLint("SimpleDateFormat")
    private void ScreenShot() {

        // 没锟斤拷SDCard
        String strSavePath = Functions.GetSDPath();
        if (strSavePath == null) {

            screenshotDialog.dismiss();// 取消对话框

            Toast.makeText(getApplicationContext(), getString(R.string.noticeSDCardNotExist), Toast.LENGTH_SHORT).show();
            return;
        }

        // 首先保存到相册
        bm = mvMediaPlayer.screenShot();

        if (bm != null) {
            bm.recycle();
            bm = null;
            bm = Bitmap.createBitmap(Defines._capWidth, Defines._capHeight, Config.RGB_565);
            bm.copyPixelsFromBuffer(Defines._capbuffer);
            Defines._capbuffer.position(0);
        }

        if (bm != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date());
            String strFileName = dateString + "(" + m_strFileName + ")" + ".jpg";
            if (saveToSDCard(bm, strFileName)) {// 保存到相册
                screenshotDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.noticeFileSaveToAlbumsOK), Toast.LENGTH_SHORT).show();
            } else {

                strFileName = strSavePath + "/" + strFileName;
                if (saveToSDCard(bm, strFileName)) {
                    screenshotDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.noticeScreenShotOK), Toast.LENGTH_SHORT).show();
                } else {
                    screenshotDialog.dismiss();

                    Toast.makeText(getApplicationContext(), getString(R.string.noticeScreenShotFail), Toast.LENGTH_SHORT).show();
                }
            }
        } else {// 截屏失败
            screenshotDialog.dismiss();

            Toast.makeText(getApplicationContext(), getString(R.string.noticeScreenShotFail), Toast.LENGTH_SHORT).show();
        }

    }

    // end add by luo 20141115

    // 保存到SD卡
    private boolean saveToSDCard(Bitmap image, String strFileName) {
        boolean bResult = false;

        if (image == null)
            return bResult;
        try {

            File file = new File(Functions.GetSDPath() + File.separator + LocalDefines.SDCardPath);
            if (!file.exists())
                file.mkdir();

            File imageFile = new File(file.getAbsolutePath() + File.separator + strFileName);
            FileOutputStream out = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            bResult = true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bResult;
    }

    // end add by luo 20141226

    /**
     * 根据当前选择的秒数还原时间点
     *
     * @param
     */

    // add by xie 2016-6-8
    private static String getCheckTimeBySeconds(int progress, String startTime) {

        String return_h = "", return_m = "", return_s = "";

        String[] st = startTime.split(":");

        int st_m = Integer.valueOf(st[0]);
        int st_s = Integer.valueOf(st[1]);

        int m = (progress % 3600) / 60;

        int s = progress % 60;

        if ((s + st_s) >= 60) {

            int tmpSecond = (s + st_s) % 60;

            m = m + 1;

            if (tmpSecond >= 10) {
                return_s = tmpSecond + "";
            } else {
                return_s = "0" + (tmpSecond);
            }

        } else {
            if ((s + st_s) >= 10) {
                return_s = s + st_s + "";
            } else {
                return_s = "0" + (s + st_s);
            }

        }

        if ((m + st_m) >= 60) {

            int tmpMin = (m + st_m) % 60;

            // h = h + 1;

            if (tmpMin >= 10) {
                return_m = tmpMin + "";
            } else {
                return_m = "0" + (tmpMin);
            }

        } else {
            if ((m + st_m) >= 10) {
                return_m = (m + st_m) + "";
            } else {
                return_m = "0" + (m + st_m);
            }

        }
        return return_m + ":" + return_s;
    }

    /**
     * 计算连个时间之间的秒数
     */
    // add by xie 2016-6-8
    private static int totalSeconds(String startTime, String endTime) {

        String[] st = startTime.split(":");
        String[] et = endTime.split(":");

        int st_m = Integer.valueOf(st[0]);
        int st_s = Integer.valueOf(st[1]);
        int et_m = Integer.valueOf(et[0]);
        int et_s = Integer.valueOf(et[1]);

        int totalSeconds = (et_m - st_m) * 60 + (et_s - st_s);

        return totalSeconds;

    }

    class BottomMenuGestureListener extends SimpleOnGestureListener {// 云台控制手势监听

        /**
         * 这个方法不同于onSingleTapUp，他是在GestureDetector确信用户在第一次触摸屏幕后，没有紧跟着第二次触摸屏幕，
         * 也就是不是“双击”的时候触发
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (layoutBottomBar.getVisibility() == View.VISIBLE && layoutCrossScreenMode.getVisibility() == View.VISIBLE) {

                if (nScreenOrientation == Configuration.ORIENTATION_LANDSCAPE) {


                    hideToolsViews();
                }

            } else {
                // 显示
                showToolsViews();
            }

            return false;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mvMediaPlayer.getGLFisheyeView()) {
            mGestureDetector.onTouchEvent(event);
        }
        return false;
    }

    /**
     * 点击返回按钮或者回退键，退出当前活动
     */
    private void exitCurrentActivity() {
        if (mIsPlaying == false) {
            stopPlay(false);
            m_bFinish = true;
            if (!isPlayFishEyeFromCloud) {
//                Intent intent = new Intent(NVPlayerPlaybackFishEyeActivity.this, MainActivity.class);
//                Bundle data = new Bundle();
//                data.putParcelable("login_handle", deviceParam);
//                data.putInt("play_index", listID);
//                data.putBoolean("is_file_list_visible", true);
//                data.putBoolean("rec_load_from_db", true);
//
//                intent.putExtras(data);
//                startActivity(intent);
                LocalDefines.PLAY_BACK_BACK = true;

            }

            unRegisterReceiver();
            NVPlayerPlaybackFishEyeActivity.this.finish();
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            return;

        }

        View view = View.inflate(this, R.layout.show_alert_dialog, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.alert_stop_play));
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.GONE);
        new AlertDialog.Builder(this)//
                .setView(view).setNegativeButton(getString(R.string.alert_btn_Cancel), null).setPositiveButton(getString(R.string.alert_btn_OK), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                try {
                    stopPlay(false);
                } catch (Exception e) {

                }
                mIsPlaying = false;
                m_bFinish = true;

                if (!isPlayFishEyeFromCloud) {
                    setResult(RESULT_OK);

//                    Intent intent = new Intent(NVPlayerPlaybackFishEyeActivity.this, MainActivity.class);
//                    Bundle data = new Bundle();
//                    data.putParcelable("login_handle", deviceParam);
//                    data.putInt("play_index", listID);
//
//                    data.putBoolean("is_file_list_visible", true);
//                    data.putBoolean("rec_load_from_db", true);
//
//                    intent.putExtras(data);
//                    startActivity(intent);
                    LocalDefines.PLAY_BACK_BACK = true;

                }

                unRegisterReceiver();
                NVPlayerPlaybackFishEyeActivity.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }

        }).show();
    }

    private CloseActivityReceiver mReceiver;

    /**
     * 注册广播接收器，当收到广播时候，关闭当前Activity实例
     *
     * @param context
     */
    private void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocalDefines.getReceiverActionString(context));
        mReceiver = new CloseActivityReceiver();
        registerReceiver(mReceiver, filter);
    }

    /**
     * 取消注册广播接收器
     */
    private void unRegisterReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    /**
     * 广播接收器，用于接收关闭当前Activity的广播
     *
     * @author Administrator
     */
    class CloseActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = LocalDefines.getReceiverActionString(NVPlayerPlaybackFishEyeActivity.this);
            if (intent != null && intent.getAction().equals(action)) {
                stopCurrentActivityFromBroadcast();
            }
        }
    }

    /**
     * 收到广播后，关闭当前活动 add 2016年10月25日
     */
    private void stopCurrentActivityFromBroadcast() {
        if (mIsPlaying == false) {
            stopPlay(false);
            m_bFinish = true;

            if (isPlayFishEyeFromCloud) {
//                if (LocalDefines.sCloudStorageActivity != null) {
//                    LocalDefines.sCloudStorageActivity.closeCurrentActivity();
//                }

            } else {

                LocalDefines.PLAY_BACK_BACK = true;
            }
            unRegisterReceiver();
            NVPlayerPlaybackFishEyeActivity.this.finish();
        } else {
            try {
                stopPlay(false);
            } catch (Exception e) {

            }

            mIsPlaying = false;
            m_bFinish = true;

            if (isPlayFishEyeFromCloud) {
//                if (LocalDefines.sCloudStorageActivity != null) {
//                    LocalDefines.sCloudStorageActivity.closeCurrentActivity();
//                }
            } else {
                setResult(RESULT_OK);
                LocalDefines.PLAY_BACK_BACK = true;
            }
            unRegisterReceiver();
            NVPlayerPlaybackFishEyeActivity.this.finish();
        }
    }
}