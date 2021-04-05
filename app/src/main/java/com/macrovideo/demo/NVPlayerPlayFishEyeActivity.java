package com.macrovideo.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyfisheyepano.GLFisheyeView;
import com.macrovideo.sdk.defines.Defines;
import com.macrovideo.sdk.defines.ResultCode;
import com.macrovideo.sdk.media.ITimeTextCallback;
import com.macrovideo.sdk.media.LibContext;
import com.macrovideo.sdk.media.LoginHandle;
import com.macrovideo.sdk.media.NVPanoPlayer;
import com.macrovideo.sdk.media.Player;
import com.macrovideo.sdk.objects.PTZXPoint;
import com.macrovideo.sdk.tools.Functions;
import com.macrovideo.sdk.tools.SpSaveList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class NVPlayerPlayFishEyeActivity extends Activity implements View.OnClickListener, OnTouchListener, OnItemClickListener {

    static final short SHOWCODE_LOADING = 1001;
    static final short SHOWCODE_NEW_IMAGE = 1002;

    static final short SHOWCODE_NOTICE = 1003;

    static final short SHOWCODE_VIDEO = 1004;
    static final short SHOWCODE_STOP = 2001;
    static final short SHOWCODE_HAS_DATA = 3001;

    static final short STAT_CONNECTING = 2001;
    static final short STAT_LOADING = 2002;
    static final short STAT_DECODE = 2003;
    static final short STAT_STOP = 2004;
    static final short STAT_DISCONNECT = 2005;
    static final short STAT_RESTART = 2006;
    static final short STAT_MR_BUSY = 2007;

    static final short STAT_MR_DISCONNECT = 2008;

    private int m_nID = 0;
    private int m_nDeviceID = 0;
    private String m_strName = "IPC";
    private int m_nAddType = 0;

    private String m_strUsername = "admin";
    private String m_strPassword = "";

    private boolean m_bSpeak = false;
    private boolean m_bPTZ = false;
    private boolean m_bPTZX = false;
    private int m_nPTZXCount = 0;

    private boolean m_bReversePRI = true;
    private int mPlayingChn = -1;
    private boolean mPlaySound = false;

    private int m_lightStatus = 1;
    // private Button mBtnStopAndPlay;
    private Button mBtnSound;
    private Button mBtnSound2; // add by mai 2015-3-25

    private LinearLayout layoutTopBar = null;
    private boolean mIsPlaying = false;
    private RelativeLayout layoutBottomBar;
    private LinearLayout layoutCrossScreenMode;

    private boolean bIsLeftPressed = false, bIsRightPressed = false, bIsUpPressed = false, bIsDownPressed = false;


    private ImageView mBtnBack;


    private boolean mIsSpeaking = false;

    private boolean m_bFinish = false;
    int mScreenWidth = 0;// 锟斤拷幕锟斤拷
    int mScreenHeight = 0;// 锟斤拷幕锟斤拷

    private boolean mQLHD = true;// 锟角凤拷支锟街革拷锟斤拷
    private int mStreamType = 0;// 锟斤拷前锟斤拷锟脚碉拷锟斤拷锟斤拷

    private Button mBtnMic = null, mBtnScreenShot = null;
    private Button mBtnMic2 = null, mBtnReverse2 = null, mBtnScreenShot2 = null; // add


    private LinearLayout llVertical;

    private RelativeLayout llLandscape;

    NVPanoPlayer mNVPanoPlayer = null;
    TextView mTvDisplayRealTime = null;

    LinearLayout container = null;

    private int nScreenOrientation = Configuration.ORIENTATION_PORTRAIT;

    private GestureDetector mGestureDetector = null;
    private ScaleGestureDetector mScaleGestureDetector = null;
    private boolean mIsReverse = false;// add by luo 20141124

    // add by luo 20141217
    private Dialog iamgeViewDialog = null;
    private View iamgeViewConctentView = null;
    private Button btnCancelImageView = null;
    // end add by luo 20141217

    private TextView tvPlayDeviceID; // add by mai 2015-3-13
    private LinearLayout llPlayTalkback; // add by mai 2015-3-14

    // mai 2015-3-14
    private boolean bAnyway = true;
    private Dialog screenshotDialog = null;

    private int FLING_MIN_DISTANCE = 10;
    private int FLING_MIN_VELOCITY = 80;
    private int FLING_MAX_DISTANCE = FLING_MIN_DISTANCE;

    private static int BTN_SCREENSHOT = 10010; // add by mai 2015-6-25

    private ListView lvPreset; // add by mai 2015-7-30
    private PopupWindow popupWindowMore; // add by mai 2015-7-30
    private ImageView btnPresetConfig; // add by mai 2015-7-30

    private PTZXPiontAdapter ptzxAdapter = null;// add by luo 20150803

    private static final int PTZX_RESULT_OK = 1111; // add by mai 2015-7-31

    private static final int PTZX_RESULT_FAIL = 1112; // add by mai 2015-7-31


    private ProgressBar progressBarPresetConfig; // add by mai 2015-7-31

    // add by mai 20151210
    private LoginHandle deviceParam = null;
    // end add by mai 20121210

    private Button mBtnExpandMode, mBtnDeviceMode;

    private PopupWindow mPopupExpandMode, mPopupExpandMode2, mPopupDeviceMode;

    private int mPlayMode = 0;

    // 模式
    private final int PlayerMode0 = 0; // 原始图
    private final int PlayerMode3 = 3; // 筒形图
    private final int PlayerMode7 = 7; // 4画面图
    private final int PlayerMode11 = 11; // 1个原始图 + 2个画面图 + 1个360度经纬图
    private final int PlayerMode6 = 6; // 180度经纬图
    private final int PlayerMode12 = 12; // 360度经纬图
    private final int PlayerMode1 = 4; // 展开图

    private static final int MY_PERMISSIONS_REQUEST_MICROPHONE = 2;
    private static final int MY_PERMISSION_REQUEST_STORAGE = 3;

    private Button imgOriginal;
    private Button imgCylindric;
    private Button imgQuad;
    private Button imgModeLongLatUD;
    private Button imgModeInversionCeil;
    private Button imgMode4;
    private Button imgModeViewAngle;
    private Button imgWallOriginal;

    private Button imgModeInversionWall;

    private Button imgCSModeOriginal;
    private Button imgCSMode5;
    private Button imgCSModeInversion;

    private ArrayList<Integer> SelectAreaList; // 自定义选择报警区域集合
    private Map<Integer, Integer> LocalAreaSelectmap;
    private ArrayList<Integer> ALL_area_alarmlist; // 默认报警区域集合
    private HashMap<Integer, ArrayList<Integer>> map_Select_area;
    private boolean isSelectArea = false;
    private boolean isAllArea = false;
    private ArrayList<String> Viewlist;
    private SelectAreaAdapter adapter;

    private Button btn_saveSelectArea = null;
    private LinearLayout ll_alarmArea_explain = null;
    private Button btnSaveAlarmArea = null;
    private LinearLayout RlVertical;
    private ProgressBar spinner_0 = null;
    private Button btnSelectAllArea, btnCanelAllArea;
    private LinearLayout ll_light_controler, ll_light_switch, ll_back_to_lightcontroler, ll_open_light, ll_close_light, ll_auto_light;
    private boolean isShowLightSwitch = false;

    private void createDialogs() {

        iamgeViewConctentView = LayoutInflater.from(this).inflate(R.layout.screenshotdialog, null);
        screenshotDialog = new Dialog(this, R.style.progressDialog);
        screenshotDialog.setContentView(iamgeViewConctentView);
        screenshotDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Message msg = handler.obtainMessage();
                msg.arg1 = Defines.MSG_SCREENSHOT;
                handler.sendMessage(msg);
            }

        });

        screenshotDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isShowLightSwitch) {
                ll_light_switch.setVisibility(View.GONE);
                ll_light_controler.setVisibility(View.VISIBLE);
                isShowLightSwitch = false;
                return false;
            }

            if (isSelectArea == true) {
                NVPlayerPlayFishEyeActivity.this.finish();
            } else {
                exitPlayFisheye();
            }

        }

        return false;
    }

    // 锟斤拷取锟角否播凤拷锟斤拷频锟斤拷锟斤拷锟斤拷
    private void readSystemParam() {

        SharedPreferences ap = getSharedPreferences(Defines._fileName, MODE_PRIVATE);
        mPlaySound = ap.getBoolean("sounds", true);

    }

    // 锟斤拷锟斤拷锟斤拷频锟角凤拷乇锟�
    public boolean writeSystemParam() {
        SharedPreferences ap = getSharedPreferences(Defines._fileName, MODE_PRIVATE);
        SharedPreferences.Editor editer = ap.edit();
        editer.putBoolean("sounds", mPlaySound);
        editer.commit();

        return true;
    }

    // 锟斤拷示锟斤拷锟斤拷示
    // msg锟斤拷锟斤拷示锟斤拷锟斤拷息
    private void ShowAlert(String title, String msg) {
        try {
            new AlertDialog.Builder(NVPlayerPlayFishEyeActivity.this).setTitle(title).setMessage(msg)
                    // .setTitle(getString(R.string.Alert_Title)).setMessage(msg)
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
        OnPlayersPuase();
        super.onPause();
    }

    @Override
    public void onResume() {

        OnPlayersResume(); // 锟斤拷锟铰匡拷始锟斤拷锟斤拷
        nToolsViewShowTickCount = 8;
        timerThreadID++;
        new TimerThread(timerThreadID).start();
        if (mIsPlaying) {
            startPlay();
        } else {
            stopPlay(true);
        }

        m_bFinish = false;
        NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiManager.cancel(LocalDefines.notificationID);
        nScreenOrientation = this.getResources().getConfiguration().orientation;
        super.onResume();
    }

    // 锟斤拷锟铰匡拷始锟斤拷锟斤拷
    public void OnPlayersResume() {

        if (mNVPanoPlayer != null) {
            mNVPanoPlayer.getGLFisheyeView().onResume();
        }
    }

    @Override
    public void onDestroy() {

        if (mNVPanoPlayer != null) {
            mNVPanoPlayer.release();
            mNVPanoPlayer = null;
        }

        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStop() {

        timerThreadID++;
        m_nThreadID++; // 锟接迟革拷锟铰斤拷图锟竭筹拷使锟斤拷
        if (!m_bFinish) {// 锟斤拷锟斤拷前锟斤拷锟斤拷锟絟ome锟斤拷锟铰碉拷停止锟斤拷锟酵憋拷锟芥当锟节碉拷锟斤拷锟�

            NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = null;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.app_notice)).setTicker(getString(R.string.app_name)) // 閫氱煡閿熼樁娆＄鎷烽敓鏂ゆ嫹閿熸枻鎷烽�氱煡閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋晥閿熸枻鎷烽敓锟�
                    .setWhen(System.currentTimeMillis());
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;

            if (Build.VERSION.SDK_INT >= 23) {
                builder.setSmallIcon(R.drawable.my_device_3);
                builder.setLargeIcon(Functions.readBitMap(this, R.drawable.icon));
            } else {
                builder.setSmallIcon(R.drawable.icon_1);
            }

            Intent intent = new Intent(this, NVPlayerPlayFishEyeActivity.class);
            Bundle data = new Bundle();
            // 锟斤拷锟斤拷锟矫伙拷锟斤拷锟斤拷锟较�
            data.putString("name", m_strName);
            if (mIsPlaying) {// 锟斤拷锟斤拷
                data.putBoolean("isplaying", true);// 锟斤拷锟酵拷锟斤拷锟�
                data.putInt("playing_chn", mPlayingChn);// 锟斤拷锟酵拷锟斤拷锟�
            }
            data.putParcelable("", deviceParam);
            intent.putExtras(data);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(this, LocalDefines.notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notiManager.notify(LocalDefines.notificationID, builder.build());
            LibContext.stopAll();// add by luo 20141219

        } else {

            LibContext.stopAll();// add by luo 20141219
            LibContext.ClearContext();
        }

        m_bFinish = true;
        super.onStop();
    }

    // 妯睆
    private void ShowLandscapeView() {

        synchronized (this) {
            ll_light_controler.setVisibility(View.GONE);
            ll_light_switch.setVisibility(View.GONE);
//			mGridView.setVisibility(View.GONE);
            // btnSelectAllArea.setVisibility(View.GONE);
            llVertical.setVisibility(View.GONE);
            if (mPopupExpandMode != null && mPopupExpandMode.isShowing()) {
                mPopupExpandMode.dismiss();
            }

            if (mPopupExpandMode2 != null && mPopupExpandMode2.isShowing()) {
                mPopupExpandMode2.dismiss();
            }

            if (mPopupDeviceMode != null && mPopupDeviceMode.isShowing()) {
                mPopupDeviceMode.dismiss();
            }
            bAnyway = false; // add by mai 2015-3-23
            nToolsViewShowTickCount = 5;

            // int nWidth = mScreenWidth;
            // int nHeight = mScreenHeight;
            // double dWidth = nHeight*1.7777777;
            // if(dWidth<nWidth)nWidth=(int) dWidth;

            /*
             * if(controlPanel!=null) { controlPanel.setVisibility(View.GONE); }
             */

            if (popupWindowMore != null) {
                popupWindowMore.dismiss();
            }

            hideToolsViews();

            /*
             * if(layoutCenter!=null){ RelativeLayout.LayoutParams rlp=new
             * RelativeLayout.LayoutParams(nWidth, nHeight);
             * rlp.addRule(RelativeLayout.CENTER_IN_PARENT,
             * RelativeLayout.TRUE); layoutCenter.setLayoutParams(rlp);
             * layoutCenter.setPadding(0, 0, 0, 0); }
             */

            nScreenOrientation = Configuration.ORIENTATION_LANDSCAPE;
            /*
             * for(int i = 0;i < Player.PLAYER_NUM;i++) { if(glViews[i]!=null){
             * glViews[i].onOreintationChange(nScreenOrientation); }
             *
             * }
             */
            if (mNVPanoPlayer != null) {
                mNVPanoPlayer.onOreintationChange(nScreenOrientation);
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode12);
            }
            // containers[Player.CurrentSelPlayer()].setPadding(0, 0, 0, 0);
            container.setPadding(0, 0, 0, 0);
            // glViews[Player.CurrentSelPlayer()].getGLFisheyeView().setMode(PlayerMode12);
        }

    }

    // 绔栧睆
    private void ShowPortrailView() {

        synchronized (this) {
            if (!isSelectArea) {
                ll_light_controler.setVisibility(View.VISIBLE);
            }

            if (m_lightStatus == 0) {
                ll_light_controler.setVisibility(View.GONE);
            } else if (m_lightStatus == Defines.NV_IPC_ACTION_LIGHT_ON) {
                ll_open_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
                ll_open_light.getBackground().setAlpha(180);
                ll_close_light.setBackgroundColor(Color.BLACK);
                ll_close_light.getBackground().setAlpha(5);
                ll_auto_light.setBackgroundColor(Color.BLACK);
                ll_auto_light.getBackground().setAlpha(5);
            } else if (m_lightStatus == Defines.NV_IPC_ACTION_LIGHT_OFF) {
                ll_open_light.setBackgroundColor(Color.BLACK);
                ll_open_light.getBackground().setAlpha(5);
                ll_close_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
                ll_close_light.getBackground().setAlpha(180);
                ll_auto_light.setBackgroundColor(Color.BLACK);
                ll_auto_light.getBackground().setAlpha(5);
            } else if (m_lightStatus == Defines.NV_IPC_ACTION_LIGHT_AUTO) {
                ll_open_light.setBackgroundColor(Color.BLACK);
                ll_open_light.getBackground().setAlpha(5);
                ll_close_light.setBackgroundColor(Color.BLACK);
                ll_close_light.getBackground().setAlpha(5);
                ll_auto_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
                ll_auto_light.getBackground().setAlpha(180);
            }
            if (mScreenWidth > mScreenHeight) {
                ShowLandscapeView();
            } else {
                // dip转锟斤拷为px
                int padding_in_dp_top = 45; // 6 dps
                int padding_in_dp_bottom = 75; // 6 dps
                final float scale = getResources().getDisplayMetrics().density;
                int padding_in_px_top = (int) (padding_in_dp_top * scale + 0.5f);
                int padding_in_px = (int) (40 * scale + 0.5f);
                int padding_in_px_progress = (int) (80 * scale + 0.5f);
                int padding_in_px_bottom = (int) (padding_in_dp_bottom * scale + 0.5f);
                bAnyway = true; // add by mai 2015-3-23
                showToolsViews();

                int nWidth = mScreenWidth;
                int statusBarHeight = LocalDefines.getStatusBarHeight(this);
                // int nHeight = (int)(mScreenHeight - (75 * scale + 0.5f) -
                // statusBarHeight); // 70 锟斤拷xml锟斤拷锟斤拷锟叫的高讹拷
                int nHeight = 0;
                if (isSelectArea == true) {
                    nHeight = (int) ((mScreenHeight - statusBarHeight) * 0.6 - padding_in_px);
                } else {
                    nHeight = (int) (mScreenHeight - padding_in_px_top - padding_in_px_bottom - statusBarHeight);
                }

                nScreenOrientation = Configuration.ORIENTATION_PORTRAIT;
                if (mNVPanoPlayer != null) {
                    mNVPanoPlayer.onOreintationChange(nScreenOrientation);
                    mNVPanoPlayer.getGLFisheyeView().setMode(mPlayMode);
                }

                if (RlVertical != null) {
                    // LinearLayout.LayoutParams params = new
                    // LinearLayout.LayoutParams((int) (mScreenWidth), (int)
                    // ((mScreenHeight - statusBarHeight) * 0.4));
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams((int) (mScreenWidth), (int) ((mScreenHeight - statusBarHeight) * 0.4));
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    RlVertical.setLayoutParams(rlp);

                    // rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                    // RelativeLayout.TRUE);
                    // RlVertical.setPadding(0, (int) ((mScreenHeight -
                    // statusBarHeight) * 0.6), 0, 0);

                }
                // int padding = 0;
                int padding = (int) (30 * getResources().getDisplayMetrics().density + 0.5f);
                if (isSelectArea == false) {
                    padding = (int) ((nHeight - nWidth * 1.1) / 2);
                    if (padding > 0) {
                        // containers[Player.CurrentSelPlayer()].setPadding(0,
                        // padding, 0, padding);
                        container.setPadding(0, padding, 0, padding);
                    }
                } else {
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(nWidth, nHeight);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    container.setLayoutParams(rlp);
                    // container.setPadding(0, 45, 0, 0);

                    RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams((int) (padding_in_px_progress), (int) (padding_in_px_progress));
                    rlp2.setMargins((int) (mScreenWidth / 2 - padding_in_px_progress / 2), (int) ((mScreenHeight - statusBarHeight) * 0.2), 0, 0);
                    spinner_0.setLayoutParams(rlp2);
                }
                // glViews[Player.CurrentSelPlayer()].getGLFisheyeView().setMode(mPlayMode);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {

        super.onConfigurationChanged(config);//

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;// 锟斤拷幕锟斤拷
        mScreenHeight = dm.heightPixels;// 锟斤拷幕锟斤拷
        LocalDefines.loadResource(getResources());// 锟斤拷锟斤拷图片锟斤拷源

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /*
             * If the screen is switched from portait mode to landscape mode
             */
            if (camType == LocalDefines.CAMTYPE_CEIL) {
                // mBtnDeviceMode.setBackgroundResource(R.drawable.device_ceiling_btn);
                layoutCrossScreenMode.setVisibility(View.GONE);

            } else if (camType == LocalDefines.CAMTYPE_WALL) {
                // mBtnDeviceMode.setBackgroundResource(R.drawable.device_wall_btn);
                layoutCrossScreenMode.setVisibility(View.VISIBLE);
                if (m_bReversePRI) {

                    imgCSModeInversion.setVisibility(View.VISIBLE);
                    if (mIsReverse) {
                        imgCSModeInversion.setBackgroundResource(R.drawable.mode_inversion_bg_click);
                    } else {
                        imgCSModeInversion.setBackgroundResource(R.drawable.mode_inversion_transparent);
                    }
                } else {

                    imgCSModeInversion.setVisibility(View.GONE);
                }
            }
            ShowLandscapeView();

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutCrossScreenMode.setVisibility(View.GONE);
            /*
             * If the screen is switched from landscape mode to portrait mode
             */
            ShowPortrailView();
        }
        // System.out.println("onConfigurationChanged end");//add for test
    }

    /**
     * 锟斤拷取锟斤拷锟斤拷锟叫碉拷锟斤拷锟斤拷ID
     */
    public void InitGLContainers() {

        container = (LinearLayout) findViewById(R.id.playContainer1);
        mTvDisplayRealTime = (TextView) findViewById(R.id.tv_display_real_time1);
    }


    public void InitGLViewPlayer() {
        if (mNVPanoPlayer == null) {
            mNVPanoPlayer = new NVPanoPlayer(this, false);
            if (camType == LocalDefines.CAMTYPE_WALL) {
                mNVPanoPlayer.setFixType(LocalDefines.FIXTYPE_WALL);
            } else if (camType == LocalDefines.CAMTYPE_CEIL) {
                mNVPanoPlayer.setFixType(LocalDefines.FIXTYPE_CEIL);
            }

            mNVPanoPlayer.setTvTimeOSD(new ITimeTextCallback() {

                @Override
                public void setTimeText(final String strTime) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mTvDisplayRealTime.setText(strTime);
                        }
                    });
                }
            });

            GLFisheyeView fisheyeView = new GLFisheyeView(getApplication());
            fisheyeView.setOnTouchListener(this);
            fisheyeView.setMode(mPlayMode);
            mNVPanoPlayer.setGlFishView(fisheyeView);
        }
    }


    // 停止``````````
    public void OnPlayersPuase() {

        if (mNVPanoPlayer != null) {
            mNVPanoPlayer.getGLFisheyeView().onPause();
        }
    }


    public void ConnectGLViewToPlayer() {
        container.addView(mNVPanoPlayer.getGLFisheyeView());
    }


    public void InitGLViewProgressbar() {
        Player.GetProgressBars((ProgressBar) findViewById(R.id.spinner_0), Player.WND_ID_0);
    }

    public void SetGLViewPlayerMessageHandler() {
        if (mNVPanoPlayer != null && handler != null) {
            mNVPanoPlayer.GetHandler(handler);
        }

    }

    private Handler handler = new Handler() {
        // @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {

            if (msg.arg1 == BTN_SCREENSHOT) {
                mBtnScreenShot.setEnabled(true);
                mBtnScreenShot2.setEnabled(true);
                return;
            }

            if (msg.arg1 == PTZX_RESULT_OK) {
                progressBarPresetConfig.setVisibility(View.GONE);
                int nPTZXID = msg.arg2;
                boolean isSetOK = false;

                if (nPTZXID >= 0 && nPTZXID < 9) {
                    Bitmap image = getPTZXImage();
                    isSetOK = LocalDefines.updatePTZXPoints(m_nDeviceID, nPTZXID, image);
                }

                Toast.makeText(NVPlayerPlayFishEyeActivity.this, getString(R.string.presetOK), Toast.LENGTH_SHORT).show();

                if (ptzxAdapter != null) {
                    ptzxAdapter.notifyDataSetChanged();
                }

                return;

            } else if (msg.arg1 == PTZX_RESULT_FAIL) {
                progressBarPresetConfig.setVisibility(View.GONE);
                Toast.makeText(NVPlayerPlayFishEyeActivity.this, getString(R.string.presetFail), Toast.LENGTH_SHORT).show();
                return;
            }

            if (msg.arg1 == Defines.MSG_SCREENSHOT) {
                ScreenShot();

                return;
            }

            if (msg.arg1 == Defines.MSG_HIDE_TOOLVIEW) {
                if (!mIsSpeaking && nScreenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (popupWindowMore != null && popupWindowMore.isShowing()) {
                        nToolsViewShowTickCount = 5;
                        return;
                    }
                    hideToolsViews();
                }

                return;
            }

            switch (msg.arg2) {
                case Player.WND_ID_0:
                    if (msg.arg1 == 1) {
                        Player.ShowProgressBar(Player.WND_ID_0, true);
                    } else {
                        Player.ShowProgressBar(Player.WND_ID_0, false);
                    }
                    break;

                default:
                    break;
            }
        }
    };

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

    Bitmap bm = null;
    String folderName = "iCamSeeImages";

    private int m_nThreadID = 0;

    /**
     * 锟斤拷头锟斤拷锟斤拷
     */
    private int camType; // add 2016锟斤拷7锟斤拷12锟斤拷

    private int panoX; // add 2016锟斤拷9锟斤拷23锟斤拷
    private int panoY; // add by 2016锟斤拷9锟斤拷23锟斤拷
    private int panoRad; // add  2016锟斤拷9锟斤拷23锟斤拷

    boolean isCheck = false;

    private String screenShotDate;

    private boolean mIsHWChangedFromPortrait = false;
    private boolean mIsHWChangedFromLandscape = false;
    private SharedPreferences mHWConfigPrefer;

    private class ThreadBtnScreenShot extends Thread {

        private int nThreadID = 0;

        public ThreadBtnScreenShot(int nThreadID) {

            this.nThreadID = nThreadID;
        }

        @Override
        public void run() {
            super.run();

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (m_nThreadID == nThreadID) {
                Message msg = handler.obtainMessage();
                msg.arg1 = BTN_SCREENSHOT;
                handler.sendMessage(msg);
            }

        }

    }


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
            Paint paint2 = new Paint();
            paint2.setColor(Color.WHITE);
            paint2.setStrokeWidth(4);
            paint2.setTextSize(33);
            paint2.setStyle(Paint.Style.FILL);
            Canvas canvas = new Canvas(image);
            canvas.drawText(screenShotDate, (float) (image.getWidth() / 1.55), (float) (image.getHeight() / 19), paint2);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            bResult = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bResult;
    }


    private Bitmap getPTZXImage() {

        // TODO: new sdk 截图
        bm = mNVPanoPlayer.screenShot();// 锟斤拷图

        float deltaW = Defines._PTZXWidth / Defines._capWidth;
        float deltaH = Defines._PTZXHeight / Defines._capHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(deltaW, deltaH);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }


    private void ScreenShot() {

        m_nThreadID++;
        mBtnScreenShot.setEnabled(false); // add by mai 2015-6-25
        mBtnScreenShot2.setEnabled(false); // add by mai 2015-6-25
        new ThreadBtnScreenShot(m_nThreadID).start(); // add by mai 2015-6-25
        String strSavePath = Functions.GetSDPath();
        if (strSavePath == null) {
            screenshotDialog.dismiss();
            Toast.makeText(getApplicationContext(), getString(R.string.noticeSDCardNotExist), Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: new sdk 截图
        bm = mNVPanoPlayer.screenShot();// 锟斤拷图

        if (bm != null) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(new Date());
            screenShotDate = dateString;
            screenShotDate = formatter2.format(new Date());
            String strFileName = dateString + "(" + m_strName + ")" + ".jpg";

            if (saveToSDCard(bm, strFileName)) {
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
        } else {// 锟斤拷锟斤拷失锟斤拷
            screenshotDialog.dismiss();

            Toast.makeText(getApplicationContext(), getString(R.string.noticeScreenShotFail), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("deprecation")
    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保持屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiManager.cancel(LocalDefines.notificationID);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_nvplayer_fisheye_playview);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        // 注册广播接收器，接收关闭当前Activity的广播
        registerReceiver(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        LocalDefines.loadResource(getResources());

        FLING_MAX_DISTANCE = (int) (mScreenWidth / 3);

        tvPlayDeviceID = (TextView) findViewById(R.id.tvPlayDeviceID);

        llPlayTalkback = (LinearLayout) findViewById(R.id.llPlayTalkback); // add

        Viewlist = new ArrayList<String>();
        SelectAreaList = new ArrayList<Integer>();
        map_Select_area = new HashMap<>();
        ALL_area_alarmlist = new ArrayList<Integer>();

        Bundle data = this.getIntent().getExtras();

        if (data != null) {
            deviceParam = data.getParcelable("device_param");
            isSelectArea = data.getBoolean("isSelectArea");

            m_bReversePRI = deviceParam.isbReversePRI();
            m_bPTZ = deviceParam.isbPTZ();
            m_bPTZX = deviceParam.isbPTZX();
            m_nPTZXCount = deviceParam.getnPTZXCount();
            m_nDeviceID = deviceParam.getnDeviceID();
            m_strUsername = deviceParam.getStrUsername();
            m_strPassword = deviceParam.getStrPassword();
            m_lightStatus = deviceParam.getLightStatus();
            m_bSpeak = deviceParam.isbSpeak();
            camType = deviceParam.getCamType();
            panoX = deviceParam.getPanoX();
            panoY = deviceParam.getPanoY();
            panoRad = deviceParam.getPanoRad();
            m_strName = String.valueOf(deviceParam.getnDeviceID());
        }

        RlVertical = (LinearLayout) findViewById(R.id.RlVertical);
        spinner_0 = (ProgressBar) findViewById(R.id.spinner_0);

        if (isSelectArea) {
            RlVertical.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            RlVertical.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }

        mGestureDetector = new GestureDetector(this, new PTZGestureListener(this));

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());
        tvPlayDeviceID.setText(m_strName);

        layoutTopBar = (LinearLayout) findViewById(R.id.linearLayoutTopBar);
        // layoutCenter = (LinearLayout)findViewById(R.id.playContainer);
        layoutBottomBar = (RelativeLayout) findViewById(R.id.linearLayoutBottomBar);
        layoutCrossScreenMode = (LinearLayout) findViewById(R.id.cross_screen_mode);

        imgCSModeOriginal = (Button) findViewById(R.id.iv_cross_screen_expand_normal);
        imgCSModeOriginal.setOnClickListener(this);
        imgCSMode5 = (Button) findViewById(R.id.iv_cross_screen_expand_mode5);
        imgCSMode5.setOnClickListener(this);
        imgCSModeInversion = (Button) findViewById(R.id.iv_cross_screen_expand_inversion);
        imgCSModeInversion.setOnClickListener(this);

        llLandscape = (RelativeLayout) findViewById(R.id.llLandscape);
        llLandscape.getBackground().setAlpha(95);
        llVertical = (LinearLayout) findViewById(R.id.llVertical);
        btn_saveSelectArea = (Button) findViewById(R.id.btn_saveSelectArea);
        btn_saveSelectArea.setOnClickListener(this);
        btnSelectAllArea = (Button) findViewById(R.id.btnSelectAllArea);
        btnSelectAllArea.setOnClickListener(this);
        btnCanelAllArea = (Button) findViewById(R.id.btnCanelAllArea);
        btnCanelAllArea.setOnClickListener(this);

        ll_light_controler = (LinearLayout) findViewById(R.id.ll_light_controler);
        ll_light_controler.setOnClickListener(this);

        if (isSelectArea) {
            ll_light_controler.setVisibility(View.GONE);
        }

        ll_light_switch = (LinearLayout) findViewById(R.id.ll_light_switch);
        ll_light_switch.setOnClickListener(this);

        ll_back_to_lightcontroler = (LinearLayout) findViewById(R.id.ll_back_to_lightcontroler);
        ll_back_to_lightcontroler.setOnClickListener(this);

        ll_open_light = (LinearLayout) findViewById(R.id.ll_open_light);
        ll_open_light.setOnClickListener(this);

        ll_close_light = (LinearLayout) findViewById(R.id.ll_close_light);
        ll_close_light.setOnClickListener(this);

        ll_auto_light = (LinearLayout) findViewById(R.id.ll_auto_light);
        ll_auto_light.setOnClickListener(this);

        if (m_lightStatus == 0) {
            ll_light_controler.setVisibility(View.GONE);
        } else if (m_lightStatus == Defines.NV_IPC_ACTION_LIGHT_ON) {
            ll_open_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
            ll_open_light.getBackground().setAlpha(180);
            ll_close_light.setBackgroundColor(Color.BLACK);
            ll_close_light.getBackground().setAlpha(5);
            ll_auto_light.setBackgroundColor(Color.BLACK);
            ll_auto_light.getBackground().setAlpha(5);
        } else if (m_lightStatus == Defines.NV_IPC_ACTION_LIGHT_OFF) {
            ll_open_light.setBackgroundColor(Color.BLACK);
            ll_open_light.getBackground().setAlpha(5);
            ll_close_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
            ll_close_light.getBackground().setAlpha(180);
            ll_auto_light.setBackgroundColor(Color.BLACK);
            ll_auto_light.getBackground().setAlpha(5);
        } else if (m_lightStatus == Defines.NV_IPC_ACTION_LIGHT_AUTO) {
            ll_open_light.setBackgroundColor(Color.BLACK);
            ll_open_light.getBackground().setAlpha(5);
            ll_close_light.setBackgroundColor(Color.BLACK);
            ll_close_light.getBackground().setAlpha(5);
            ll_auto_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
            ll_auto_light.getBackground().setAlpha(180);
        }


        InitGLContainers();
        InitGLViewProgressbar();

        InitGLViewPlayer();

        ConnectGLViewToPlayer();
        // CreateHandler();
        SetGLViewPlayerMessageHandler();

        LibContext.SetContext(mNVPanoPlayer, null, null, null);

        Player.SelectWindow(Player.WND_ID_0);


        ShowPortrailView();

        mBtnBack = (ImageView) findViewById(R.id.buttonBackToLogin);
        mBtnBack.setOnClickListener(this);

        mBtnExpandMode = (Button) findViewById(R.id.btn_mode);
        mBtnExpandMode.setOnClickListener(this);

        mBtnDeviceMode = (Button) findViewById(R.id.btn_devicemode);
        mBtnDeviceMode.setOnClickListener(this);

        switch (mPlayMode) {
            case PlayerMode0:
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode1_btn);
                break;
            case PlayerMode3:
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode2_btn);
                break;
            case PlayerMode7:
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode3_btn);
                break;
            case PlayerMode11:
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode4_btn);
                break;
            case PlayerMode6:
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode5_btn);
                break;
            case PlayerMode1:
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode6_btn);
                break;
            default:
                break;
        }

        mBtnSound = (Button) findViewById(R.id.buttonSound);
        mBtnSound.setOnClickListener(this);

        mBtnMic = (Button) findViewById(R.id.buttonMic);
        mBtnMic.setOnTouchListener(this);

        mBtnScreenShot = (Button) findViewById(R.id.buttonScreenShot);
        mBtnScreenShot.setOnClickListener(this);

        // add by mai 2015-3-25
        mBtnScreenShot2 = (Button) findViewById(R.id.buttonScreenShot2);
        mBtnScreenShot2.setOnClickListener(this);

        mBtnMic2 = (Button) findViewById(R.id.buttonMic2);
        mBtnMic2.setOnTouchListener(this);

        mBtnReverse2 = (Button) findViewById(R.id.buttonReverse2);
        mBtnReverse2.setOnClickListener(this);

        mBtnSound2 = (Button) findViewById(R.id.buttonSound2);
        mBtnSound2.setOnClickListener(this);

        // add by luo 20141014
        if (m_bSpeak) {
            findViewById(R.id.layoutMicBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutMicBtn2).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layoutMicBtn).setVisibility(View.GONE);
            findViewById(R.id.layoutMicBtn2).setVisibility(View.GONE);

        }

        int nWidth = mScreenWidth;
        int statusBarHeight = LocalDefines.getStatusBarHeight(this);
        int nHeight = 0;
        if (isSelectArea == true) {
            nHeight = (int) ((mScreenHeight - statusBarHeight) * 0.6);
        }

        for (int i = 0; i < LocalDefines.alarmcolumns * LocalDefines.alarmrows; i++) {
            Viewlist.add("");
            // 报警区域默认为1选中
            ALL_area_alarmlist.add(i, 1);
        }

        LocalAreaSelectmap = new HashMap<Integer, Integer>();

        readSystemParam();
        onSoundChange();

        mPlayingChn = 1;
        mIsPlaying = true;
        createDialogs();

        if (isSelectArea == true) {

            btn_saveSelectArea.setVisibility(View.VISIBLE);
            llVertical.setVisibility(View.GONE);
            mNVPanoPlayer.setHWDecodeStatus(false, false);
        } else {
            llVertical.setVisibility(View.VISIBLE);
            btn_saveSelectArea.setVisibility(View.GONE);
        }

        mNVPanoPlayer.setHWDecodeStatus(false, false);
    }


    private class PTZXPiontAdapter extends BaseAdapter {

        private class ItemViewHolder {
            ImageView ivPresetImage;
            RoundCornerImageView btnPTZXSet;
            TextView tvPresetText;
            TextView tvIDText;
        }

        private ArrayList<Bitmap> mAppList;
        private LayoutInflater mInflater;
        private Context mContext;
        private String[] keyString;
        private int[] valueViewID;
        private ItemViewHolder holder;

        public PTZXPiontAdapter(Context c, ArrayList<Bitmap> appList, int resource, String[] from, int[] to) {
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
                convertView = mInflater.inflate(R.layout.preset_count_item, null);
                holder = new ItemViewHolder();
                holder.ivPresetImage = (ImageView) convertView.findViewById(valueViewID[0]);
                holder.btnPTZXSet = (RoundCornerImageView) convertView.findViewById(valueViewID[1]);
                holder.tvPresetText = (TextView) convertView.findViewById(valueViewID[2]);
                holder.tvIDText = (TextView) convertView.findViewById(valueViewID[3]);

                convertView.setTag(holder);
            }

            PTZXPoint ptzxPoint = null;
            Bitmap image = null;
            if (LocalDefines._PTZXPointDevID == m_nDeviceID) {

                ptzxPoint = LocalDefines._ptzxPointList.get(position);
            }

            if (ptzxPoint != null) {
                image = ptzxPoint.getFaceImage();

            }

            if (image != null) {
                holder.ivPresetImage.setImageBitmap(image);
                holder.tvPresetText.setVisibility(View.GONE);
            } else {
                holder.tvPresetText.setVisibility(View.VISIBLE);
                holder.ivPresetImage.setImageBitmap(null);
            }
            ListViewButtonListener listener = new ListViewButtonListener(position);

            holder.tvIDText.setText("" + (position + 1));
            holder.ivPresetImage.setOnClickListener(listener);
            holder.btnPTZXSet.setOnClickListener(listener);

            return convertView;
        }

        class ListViewButtonListener implements OnClickListener {
            private int position;

            ListViewButtonListener(int pos) {
                position = pos;
            }

            @Override
            public void onClick(View v) {

                if (v.getId() == holder.ivPresetImage.getId()) {

                    locationPTZXPoint(position);
                    if (popupWindowMore != null) {
                        popupWindowMore.dismiss();
                    }
                } else if (v.getId() == holder.btnPTZXSet.getId()) {
                    progressBarPresetConfig.setVisibility(View.VISIBLE);

                    startPTZXConfig(deviceParam, position, Defines.NV_PRESET_ACTION_RESET, deviceParam.getnDeviceID());
                }

            }
        }
    }

    private void presetList() {

        if (m_nPTZXCount > 0) {
            ArrayList<Bitmap> listItem = new ArrayList<Bitmap>();
            for (int i = 0; i < m_nPTZXCount; i++) {
                listItem.add(Functions.readBitMap(this, R.drawable.alarm_icon));

            }

            ptzxAdapter = new PTZXPiontAdapter(NVPlayerPlayFishEyeActivity.this, listItem, R.layout.preset_count_item,// ListItem锟斤拷XML实锟斤拷

                    new String[]{"ItemPresetImage", "ItemPresetSetImage", "ItemPresetText", "ItemIDText"},

                    new int[]{R.id.ivPresetImage, R.id.btnPTZXSet, R.id.tvPresetText, R.id.tvPTZXID});

            if (lvPreset != null) {
                lvPreset.setCacheColorHint(Color.TRANSPARENT);

                lvPreset.setAdapter(ptzxAdapter);

                lvPreset.setOnItemClickListener(this);

            }

        } else {

        }

    }


    private void stopPlay(boolean bFlag) {

        ptzTimerThreadID++;
        // add by luo 20141007
        if (m_nAddType != Defines.SERVER_SAVE_TYPE_DEMO) {


            // TODO: new sdk 截图
            bm = mNVPanoPlayer.screenShot();// 锟斤拷图
            if (bm != null) {
                Bitmap image = Functions.zoomBitmap(bm, 320, 240);
            }

        }

        // end add by luo 20141007

        mIsPlaying = false;
        tvPlayDeviceID.setText(m_strName);
        mPlayingChn = -1;

        // TODO: new sdk 停止实时预览
        mNVPanoPlayer.stopPlay();// add by luo 20141008

        Player.ShowProgressBar(Player.WND_ID_0, false);

    }

    // 锟斤拷始锟斤拷锟斤拷
    private void startPlay() {
        if (m_strName != null && m_strName.trim().length() > 0) {
            tvPlayDeviceID.setText(getString(R.string.Notification_Playing_Chn) + " " + m_strName);
        }

        mNVPanoPlayer.setnServerID(deviceParam.getnDeviceID());

        if (Player.CurrentSelPlayer() < 4) {
            Player.setPlaying(Player.CurrentSelPlayer(), true);

            mNVPanoPlayer.EnableRender();
            Defines._capWidth = 0;
            Defines._capHeight = 0;

            // TODO: new sdk 开始实时预览
            mNVPanoPlayer.startPlay(0, 0, 1, mPlaySound, deviceParam);
            mNVPanoPlayer.setReverse(mIsReverse);
            mNVPanoPlayer.playAudio();
        }

        ptzTimerThreadID++;
        if (m_bPTZ) {
            new PTZTimerThread(ptzTimerThreadID).start();
        }

        mIsPlaying = true;

    }

    // add by luo 20150820
    private void onStreamTypeChange(int nType) {
        if (mStreamType == nType)
            return;

        mStreamType = nType;

        if (mIsPlaying) {
            stopPlay(false);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startPlay();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSoundChange() {

        if (mPlaySound) {
            mBtnSound.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn));
            mBtnSound2.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn_horziontal));
        } else {
            mBtnSound.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_3));
            mBtnSound2.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_back_sound_btn_horziontal2));
        }

        // TODO: new sdk 声音开关
        mNVPanoPlayer.enableAudio(mPlaySound);

        writeSystemParam();
    }

    // add by xie 2016-9-3
    private void showPopWinPlayerMode(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View playerModeView = layoutInflater.inflate(R.layout.popup_expand_mode_layout, null);
        //
        imgOriginal = (Button) playerModeView.findViewById(R.id.iv_expand_normal);
        imgCylindric = (Button) playerModeView.findViewById(R.id.iv_expand_mode1);
        imgQuad = (Button) playerModeView.findViewById(R.id.iv_expand_mode2);
        imgMode4 = (Button) playerModeView.findViewById(R.id.iv_expand_mode3);
        imgModeLongLatUD = (Button) playerModeView.findViewById(R.id.iv_expand_mode4);
        // 倒置功能，2016-10-31
        imgModeInversionCeil = (Button) playerModeView.findViewById(R.id.iv_expand_inversion);

        int PopWinWidth = 0;
        if (m_bReversePRI) {
            PopWinWidth = 240;
            imgModeInversionCeil.setVisibility(View.VISIBLE);
            if (mIsReverse) {
                imgModeInversionCeil.setBackgroundResource(R.drawable.mode_inversion_bg_click);
            } else {
                imgModeInversionCeil.setBackgroundResource(R.drawable.mode_inversion_transparent);
            }
        } else {
            PopWinWidth = 200;
            imgModeInversionCeil.setVisibility(View.GONE);
        }

        switch (mPlayMode) {
            case PlayerMode0:
                imgOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                // imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode3:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_bg_click);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                // imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode7:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_bg_click);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                // imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode11:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_bg_click);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                // imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode6:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_bg_click);
                // imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                break;
            case PlayerMode1:
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                break;
            default:
                break;
        }
        imgOriginal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                imgOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                // imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                mPopupExpandMode.dismiss();
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode0);
                mPlayMode = PlayerMode0;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode1_btn);
            }
        });

        imgCylindric.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_bg_click);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                mPopupExpandMode.dismiss();
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode3);
                mPlayMode = PlayerMode3;

                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode2_btn);
            }
        });

        imgQuad.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_bg_click);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);
                mPopupExpandMode.dismiss();
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode7);
                mPlayMode = PlayerMode7;

                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode3_btn);
            }
        });

        imgMode4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_bg_click);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_transparent);

                mPopupExpandMode.dismiss();
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode11);
                mPlayMode = PlayerMode11;

                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode4_btn);
            }
        });

        imgModeLongLatUD.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                imgOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCylindric.setBackgroundResource(R.drawable.mode2_transparent);
                imgQuad.setBackgroundResource(R.drawable.mode3_transparent);
                imgMode4.setBackgroundResource(R.drawable.mode4_transparent);
                imgModeLongLatUD.setBackgroundResource(R.drawable.mode5_bg_click);

                mPopupExpandMode.dismiss();
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode6);
                mPlayMode = PlayerMode6;

                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode5_btn);
            }
        });

        // 倒置
        imgModeInversionCeil.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                 * 点击倒置功能
                 */
                if (m_bReversePRI) {
                    if (mIsReverse) {
                        // 已经倒置
                        imgModeInversionCeil.setBackgroundResource(R.drawable.mode_inversion_transparent);
                    } else {
                        // 未倒置
                        imgModeInversionCeil.setBackgroundResource(R.drawable.mode_inversion_bg_click);
                    }
                    mIsReverse = !mIsReverse;
                    // TODO: new sdk 倒置
                    mNVPanoPlayer.setCamImageOrientation(Defines.NV_IPC_ORIENTATION_REVERSE);
                    mPopupExpandMode.dismiss();
                }
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
        int PopWinWidth = 0;
        imgModeViewAngle = (Button) playerModeView.findViewById(R.id.iv_wall_expand_mode5);
        imgWallOriginal = (Button) playerModeView.findViewById(R.id.iv_wall_expand_normal);

        imgModeInversionWall = (Button) playerModeView.findViewById(R.id.iv_wall_expand_inversion);
        if (m_bReversePRI) {

            PopWinWidth = 120;
            imgModeInversionWall.setVisibility(View.VISIBLE);
            if (mIsReverse) {
                imgModeInversionWall.setBackgroundResource(R.drawable.mode_inversion_bg_click);
            } else {
                imgModeInversionWall.setBackgroundResource(R.drawable.mode_inversion_transparent);
            }
        } else {

            PopWinWidth = 80;
            imgModeInversionWall.setVisibility(View.GONE);
        }

        switch (mPlayMode) {
            case PlayerMode0:
                imgWallOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);
                // imgModeInversionWall.setBackgroundResource(R.drawable.mode_inversion_transparent);
                break;
            case PlayerMode1:
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
                // TODO Auto-generated method stub
                imgWallOriginal.setBackgroundResource(R.drawable.mode1_bg_click);

                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_transparent);

                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_transparent);
                mPopupExpandMode2.dismiss();
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode0);
                mPlayMode = PlayerMode0;


                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode1_btn);
            }
        });

        imgModeViewAngle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imgWallOriginal.setBackgroundResource(R.drawable.mode1_transparent);

                imgModeViewAngle.setBackgroundResource(R.drawable.mode6_bg_click);
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_bg_click);
                mPopupExpandMode2.dismiss();
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode1);
                mPlayMode = PlayerMode1;

                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode6_btn);
            }
        });

        imgModeInversionWall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * 点击倒置功能
                 */
                if (m_bReversePRI) {
                    if (mIsReverse) {
                        // 已经倒置
                        imgModeInversionWall.setBackgroundResource(R.drawable.mode_inversion_transparent);

                    } else {
                        // 未倒置
                        imgModeInversionWall.setBackgroundResource(R.drawable.mode_inversion_bg_click);
                    }
                    mIsReverse = !mIsReverse;
                    // TODO: new sdk 倒置
                    mNVPanoPlayer.setCamImageOrientation(Defines.NV_IPC_ORIENTATION_REVERSE);
                    mPopupExpandMode2.dismiss();
                }
            }
        });

        float density = getResources().getDisplayMetrics().density;
        mPopupExpandMode2 = new PopupWindow(playerModeView, (int) (PopWinWidth * density + 0.5f), (int) (50 * density + 0.5f));
        mPopupExpandMode2.setTouchable(true);
        mPopupExpandMode2.setFocusable(true);
        mPopupExpandMode2.setBackgroundDrawable(new BitmapDrawable());
        mPopupExpandMode2.setAnimationStyle(R.style.popupwindow_expand_mode);
        ;

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mPopupExpandMode2.showAtLocation(v, Gravity.NO_GRAVITY, (int) (location[0] + mPopupExpandMode2.getWidth() * density + 0.5f), (int) (location[1] - mPopupExpandMode2.getHeight() - 25 * density + 0.5f));
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
                mNVPanoPlayer.setFixType(LocalDefines.FIXTYPE_CEIL);
                btnCeiling.setBackgroundResource(R.drawable.ceiling_bg_click);
                btnWall.setBackgroundResource(R.drawable.wall_transparent);
                mBtnDeviceMode.setBackgroundResource(R.drawable.device_ceiling_btn);
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_transparent);
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode0);
                mPlayMode = PlayerMode0;
                mBtnExpandMode.setBackgroundResource(R.drawable.play_mode1_btn);
                mPopupDeviceMode.dismiss();
            }
        });

        btnWall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                camType = LocalDefines.CAMTYPE_WALL;
                mNVPanoPlayer.setFixType(LocalDefines.FIXTYPE_WALL);
                btnCeiling.setBackgroundResource(R.drawable.ceiling_transparent);
                btnWall.setBackgroundResource(R.drawable.wall_bg_click);
                mBtnDeviceMode.setBackgroundResource(R.drawable.device_wall_btn);
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_transparent);
                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode0);
                mPlayMode = PlayerMode0;
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
        mPopupDeviceMode.showAtLocation(v, Gravity.NO_GRAVITY, (int) (mScreenWidth * 0.6), (int) (location[1] - mPopupDeviceMode.getHeight() - 25 * density + 0.5f));
    }


    @Override
    public void onClick(View v) {

        nToolsViewShowTickCount = 5;

        if (v == ll_light_controler) {
            isShowLightSwitch = true;
            ll_light_switch.setVisibility(View.VISIBLE);
            ll_light_controler.setVisibility(View.GONE);
        }

        if (v == ll_back_to_lightcontroler) {
            isShowLightSwitch = false;
            ll_light_switch.setVisibility(View.GONE);
            ll_light_controler.setVisibility(View.VISIBLE);
        }

        if (v == ll_open_light) {
            ll_open_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
            ll_open_light.getBackground().setAlpha(180);
            ll_close_light.setBackgroundColor(Color.BLACK);
            ll_close_light.getBackground().setAlpha(5);
            ll_auto_light.setBackgroundColor(Color.BLACK);
            ll_auto_light.getBackground().setAlpha(5);
            mNVPanoPlayer.setLightParam(Defines.NV_IPC_ACTION_LIGHT_ON, 0, true);
            m_lightStatus = Defines.NV_IPC_ACTION_LIGHT_ON;
        }

        if (v == ll_close_light) {
            ll_open_light.setBackgroundColor(Color.BLACK);
            ll_open_light.getBackground().setAlpha(5);
            ll_close_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
            ll_close_light.getBackground().setAlpha(180);
            ll_auto_light.setBackgroundColor(Color.BLACK);
            ll_auto_light.getBackground().setAlpha(5);
            mNVPanoPlayer.setLightParam(Defines.NV_IPC_ACTION_LIGHT_OFF, 0, true);
            m_lightStatus = Defines.NV_IPC_ACTION_LIGHT_OFF;
        }

        if (v == ll_auto_light) {
            ll_open_light.setBackgroundColor(Color.BLACK);
            ll_open_light.getBackground().setAlpha(5);
            ll_close_light.setBackgroundColor(Color.BLACK);
            ll_close_light.getBackground().setAlpha(5);
            ll_auto_light.setBackgroundColor((getResources().getColor(R.color.font_color_blue)));
            ll_auto_light.getBackground().setAlpha(180);
            mNVPanoPlayer.setLightParam(Defines.NV_IPC_ACTION_LIGHT_AUTO, 0, true);
            m_lightStatus = Defines.NV_IPC_ACTION_LIGHT_AUTO;
        }

        if (v == btnSelectAllArea) {

            SelectAreaList.clear();
            for (int i = 0; i < LocalDefines.alarmcolumns * LocalDefines.alarmrows; i++) {
                SelectAreaList.add(i, 1);
            }

            isAllArea = true;

        }

        if (v == btnCanelAllArea) {
            SelectAreaList.clear();
            for (int i = 0; i < LocalDefines.alarmcolumns * LocalDefines.alarmrows; i++) {
                SelectAreaList.add(i, 0);
            }

            isAllArea = false;

        }

        if (v == btn_saveSelectArea) {
            boolean isEffectiveArea = false;
            for (int i = 0; i < SelectAreaList.size(); i++) {
                int n = SelectAreaList.get(i);
                if (n == 1) {
                    isEffectiveArea = true;
                }

            }
            if (!isEffectiveArea) {
                Toast.makeText(getApplicationContext(), getString(R.string.str_alarm_area_tip), Toast.LENGTH_SHORT).show();
                return;
            }

            LocalDefines.shouldUpdateSelectArea = true;
            // 设备是否需要更新选择区域标识
            LocalDefines.Localmap_Update_area.put(deviceParam.getnDeviceID(), LocalDefines.shouldUpdateSelectArea);
            LocalDefines.LocalAlarmAreaList = SelectAreaList;

            List splist = new ArrayList<Integer>();
            SharedPreferences mySharedPreferences = getSharedPreferences("scenelist", Context.MODE_PRIVATE);
            Editor edit = mySharedPreferences.edit();
            try {
                String liststr = SpSaveList.SceneList2String(LocalDefines.LocalAlarmAreaList);
                edit.putString("spArea_list_for" + deviceParam.getnDeviceID(), liststr);
                edit.commit();
                String liststr2 = mySharedPreferences.getString("spArea_list_for" + deviceParam.getnDeviceID(), "");
                splist = SpSaveList.String2SceneList(liststr2);
            } catch (IOException e) {

                e.printStackTrace();
            } catch (ClassNotFoundException e) {

                e.printStackTrace();
            }

            // 根据id寻找出设备的选择区域
            LocalDefines.Localmap_Select_area.put(deviceParam.getnDeviceID(), LocalDefines.LocalAlarmAreaList);

            if (LocalDefines.Localmap_Select_area != null) {

                LocalDefines.LocalAlarmAreaList = LocalDefines.Localmap_Select_area.get(deviceParam.getnDeviceID());

            } else {

            }
            NVPlayerPlayFishEyeActivity.this.finish();
        }

        if (v == mBtnExpandMode) {

            if (camType == LocalDefines.CAMTYPE_CEIL) {
                showPopWinPlayerMode(v);
            } else if (camType == LocalDefines.CAMTYPE_WALL) {
                showPopWinWallPlayerMode(v);
            }

        }

        if (v == mBtnDeviceMode) {
            showPopWinDeviceMode(v);
        }

        if (v == btnCancelImageView) {
            iamgeViewDialog.dismiss();
            return;
        }


        if (v == btnPresetConfig) {


        }

        // end add by mai 2015-7-30
        if (v == mBtnScreenShot) {
            if (mIsPlaying) {

                if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
                    return;
                }
                screenshotDialog.show();
            }
            return;
        } else if (v == mBtnScreenShot2) { // add by mai 2015-3-25
            if (mIsPlaying) {

                if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
                    return;
                }
                screenshotDialog.show();

            }
            return;
        } else // add by mai 2015-3-25
            if (v == mBtnSound) {

                v.setEnabled(false);

                if (mPlaySound) {
                    mPlaySound = false;
                } else {
                    mPlaySound = true;
                }
                onSoundChange();
                v.setEnabled(true);
            } else if (v == mBtnSound2) { // add by mai 2015-3-25
                v.setEnabled(false);
                if (mPlaySound) {
                    mPlaySound = false;
                } else {
                    mPlaySound = true;
                }
                onSoundChange();
                v.setEnabled(true);

            } else if (v == mBtnBack) {
                if (isSelectArea == true) {
                    NVPlayerPlayFishEyeActivity.this.finish();
                } else {
                    exitPlayFisheye();
                }

            } else if (v == imgCSModeOriginal) {
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_bg_click);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_transparent);
                mBtnExpandMode.setBackgroundResource(R.drawable.mode1_bg_click);

                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode0);
                mPlayMode = PlayerMode0;

            } else if (v == imgCSMode5) {
                imgCSModeOriginal.setBackgroundResource(R.drawable.mode1_transparent);
                imgCSMode5.setBackgroundResource(R.drawable.mode6_bg_click);
                mBtnExpandMode.setBackgroundResource(R.drawable.mode6_bg_click);

                mNVPanoPlayer.getGLFisheyeView().setMode(PlayerMode1);
                mPlayMode = PlayerMode1;
            } else if (v == imgCSModeInversion) {
                if (m_bReversePRI) {
                    if (mIsReverse) {
                        // 已经倒置
                        imgCSModeInversion.setBackgroundResource(R.drawable.mode_inversion_transparent);

                    } else {
                        // 未倒置
                        imgCSModeInversion.setBackgroundResource(R.drawable.mode_inversion_bg_click);
                    }
                    mIsReverse = !mIsReverse;
                    // TODO: new sdk 倒置
                    mNVPanoPlayer.setCamImageOrientation(Defines.NV_IPC_ORIENTATION_REVERSE);
                    // mPopupExpandMode2.dismiss();
                }
            }


    }


    public void ShowNotic(String title, String msg) {// add by luo 20141010

        Toast toast = Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean onTouch(View v, MotionEvent event) {//

        if (v == this.mNVPanoPlayer.getGLFisheyeView()) {
            mScaleGestureDetector.onTouchEvent(event);
            if (System.currentTimeMillis() - lScaleTime > 500) {
                mGestureDetector.onTouchEvent(event);
            }
            return false;

        } else if (v == this.mBtnMic) {

            return intercom(event, false);

        } else if (v == this.mBtnMic2) {

            return intercom(event, true);
        }

        return false;
    }

    private boolean intercom(MotionEvent event, boolean isBtn2) {

        if (!mIsPlaying) {
            return true;
        }

        int btnMicBgId = -1;
        Button btn = isBtn2 ? mBtnMic2 : mBtnMic;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (shouldRemind())
                    break;
                btnMicBgId = isBtn2 ? R.drawable.cross_screen_btn_talk_click : R.drawable.btn_talk_click;
                setIntercomViewValue(btn, btnMicBgId, true, View.VISIBLE, true);
                // TODO: new sdk 开始对讲
                mNVPanoPlayer.startSpeak();
                break;

            case MotionEvent.ACTION_CANCEL:
                btnMicBgId = isBtn2 ? R.drawable.cross_screen_btn_talk : R.drawable.btn_talk;
                setIntercomViewValue(btn, btnMicBgId, false, View.GONE, true);
                // TODO: new sdk 结束对讲
                mNVPanoPlayer.stopSpeak();
                break;

            case MotionEvent.ACTION_MOVE:
                btnMicBgId = isBtn2 ? R.drawable.cross_screen_btn_talk_click : R.drawable.btn_talk_click;
                setIntercomViewValue(btn, btnMicBgId, true, View.VISIBLE, false);
                break;

            case MotionEvent.ACTION_UP:
                btnMicBgId = isBtn2 ? R.drawable.cross_screen_btn_talk : R.drawable.btn_talk;
                setIntercomViewValue(btn, btnMicBgId, false, View.GONE, true);
                // TODO: new sdk 结束对讲
                mNVPanoPlayer.stopSpeak();
                break;

            default:
                break;
        }

        return true;
    }

    private boolean shouldRemind() {
        if (!m_bSpeak) {
            Toast.makeText(this, getString(R.string.str_do_not_support_mic), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(NVPlayerPlayFishEyeActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(NVPlayerPlayFishEyeActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_MICROPHONE);
            return true;
        }
        return false;
    }

    private void setIntercomViewValue(Button btn, final int btnMicBgId, boolean isSpeaking, final int llPlayTalkbackVisible, boolean isSleep) {
        btn.setBackgroundResource(btnMicBgId);
        llPlayTalkback.setVisibility(llPlayTalkbackVisible);
        mIsSpeaking = isSpeaking;
        if (!isSleep)
            return;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showToolsViews() {
        if (popupWindowMore != null) {
            popupWindowMore.dismiss();
        }
        nToolsViewShowTickCount = 5;
        if (bAnyway) {
            layoutTopBar.setVisibility(View.VISIBLE);
            llVertical.setVisibility(View.VISIBLE);
            llLandscape.setVisibility(View.GONE);

        } else {
            layoutBottomBar.setVisibility(View.VISIBLE);
            if (camType == LocalDefines.CAMTYPE_WALL) {
                layoutCrossScreenMode.setVisibility(View.VISIBLE);
            }
            layoutTopBar.setVisibility(View.GONE);
            llVertical.setVisibility(View.GONE);

            llLandscape.setVisibility(View.VISIBLE);


            if (m_bPTZX) {

            } else {

            }
        }


    }

    private void hideToolsViews() {

        nToolsViewShowTickCount = 0;
        layoutBottomBar.setVisibility(View.GONE);

        layoutCrossScreenMode.setVisibility(View.GONE);

        layoutTopBar.setVisibility(View.GONE);

        llLandscape.setVisibility(View.GONE);

        llVertical.setVisibility(View.GONE);
    }


    private int nToolsViewShowTickCount = 8;

    private int timerThreadID = 0;

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

                    e.printStackTrace();
                }
            }

        }
    }

    private float fScaleSize = 1.0f;
    private long lScaleTime = 0;

    class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {// 锟斤拷锟接放达拷锟斤拷锟斤拷

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (detector.getScaleFactor() > 1) {
                fScaleSize = fScaleSize - 0.008f;
                if (fScaleSize < 0.2) {
                    fScaleSize = 0.2f;
                } else {
                    // TODO: remove scale

                }

            } else if (detector.getScaleFactor() < 1) {
                fScaleSize = fScaleSize + 0.05f;
                if (fScaleSize > 1) {
                    fScaleSize = 1.0f;
                } else {
                    // TODO: remove scale

                }
            }

            lScaleTime = System.currentTimeMillis();
            //
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }

    }

    private int ptzTimerThreadID = 0;

    class PTZTimerThread extends Thread {
        int mThreadID = 0;

        public PTZTimerThread(int nThreadID) {
            mThreadID = nThreadID;
        }

        public void run() {
            boolean bLeft = false, bRight = false, bUp = false, bDown = false;
            while (mThreadID == ptzTimerThreadID) {
                bLeft = bIsLeftPressed;
                bRight = bIsRightPressed;
                bUp = bIsUpPressed;
                bDown = bIsDownPressed;

                if (bLeft && bRight) {
                    bLeft = false;
                    bRight = false;
                }

                if (bUp && bDown) {
                    bUp = false;
                    bDown = false;
                }

                if (bLeft || bRight || bUp || bDown) {
                    // TODO: new sdk 云台控制
                    mNVPanoPlayer.setPTZAction(bLeft, bRight, bUp, bDown, 0);
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {

                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    class PTZGestureListener extends SimpleOnGestureListener {

        PTZGestureListener(Context context) {
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (layoutBottomBar.getVisibility() == View.VISIBLE && layoutCrossScreenMode.getVisibility() == View.VISIBLE) {
                if (nScreenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    hideToolsViews();
                }
            } else {
                showToolsViews();
            }

            return false;
        }

    }


    private void startPTZXConfig(LoginHandle lhandle, int nPTZXID, int nPTZXAction, int nDeviceId) {
        m_nPTZXID++;
        new PTZXThread(handler, m_nPTZXID, lhandle, nPTZXID, nPTZXAction, nDeviceId).start();
    }

    private void locationPTZXPoint(int nPTZXID) {// 预锟斤拷位位锟斤拷锟借定

        mNVPanoPlayer.CallPTZXLocationID(nPTZXID, deviceParam);
    }

    /**
     * add by mai 2015-7-30
     *
     * @author Administrator
     */

    private int m_nPTZXID = 0;

    private class PTZXThread extends Thread {

        private Handler handler;
        private int m_ThreadConfigID = 0;
        LoginHandle lhandle = null;

        private int m_ThreadPTZXID = 0;
        private int m_ThreadPTZXAction = 0;
        private int m_nDeviceId = 0;


        public PTZXThread(Handler handler, int configID, LoginHandle lhandle, int nPTZXID, int nPTZXAction, int nDeviceId) {
            m_ThreadConfigID = configID;
            this.handler = handler;
            this.lhandle = lhandle;
            this.m_ThreadPTZXID = nPTZXID;
            this.m_ThreadPTZXAction = nPTZXAction;
            this.m_nDeviceId = nDeviceId;
        }

        public void run() {

            // TODO: new sdk 设置预置位
            int nConfigResult = mNVPanoPlayer.setPTZXLocation(m_ThreadPTZXID, lhandle, m_nDeviceId);
            if (m_nPTZXID == m_ThreadConfigID && nConfigResult == ResultCode.RESULT_CODE_SUCCESS) {
                Message msg = handler.obtainMessage();
                msg.arg1 = PTZX_RESULT_OK;
                msg.arg2 = m_ThreadPTZXID;

                handler.sendMessage(msg);
            } else if (m_nPTZXID == m_ThreadConfigID) {
                Message msg = handler.obtainMessage();
                msg.arg1 = PTZX_RESULT_FAIL;
                msg.arg2 = nConfigResult;
                handler.sendMessage(msg);
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
        // TODO Auto-generated method stub

        // 用来判断设备所选报警区域

        int list = SelectAreaList.get(position);

        if (list == 0) {
            // 未选中背景变化及值变化

            view.setBackgroundColor(Color.parseColor("#ff0000"));
            SelectAreaList.remove(position);
            SelectAreaList.add(position, 1);
            LocalAreaSelectmap.clear();
            view.getBackground().setAlpha(51);

        } else {
            // 选中背景变化及值变化
            view.setBackgroundColor(Color.parseColor("#000000"));
            view.getBackground().setAlpha(20);
            SelectAreaList.remove(position);
            SelectAreaList.add(position, 0);
            LocalAreaSelectmap.clear();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_MICROPHONE) {
            if (permissions[0].equals(Manifest.permission.RECORD_AUDIO) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {

                new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.str_permission_request)).setMessage(getResources().getString(R.string.str_permission_microphone2)).setNegativeButton(getResources().getString(R.string.str_permission_neglect), null)
                        .setPositiveButton(getResources().getString(R.string.str_permission_setting2), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                Uri uri = Uri.fromParts("package", NVPlayerPlayFishEyeActivity.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);

                            }
                        }).show();
            }
        }
        if (requestCode == MY_PERMISSION_REQUEST_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {

                new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.str_permission_request)).setMessage(getResources().getString(R.string.str_permission_storage1)).setNegativeButton(getResources().getString(R.string.str_permission_neglect), null)
                        .setPositiveButton(getResources().getString(R.string.str_permission_setting2), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                Uri uri = Uri.fromParts("package", NVPlayerPlayFishEyeActivity.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);

                            }
                        }).show();
            }
        }
    }

    /**
     * 退出全景播放
     */
    private void exitPlayFisheye() {
        if (mIsPlaying == false) {
            stopPlay(false);

            m_bFinish = true;

            LocalDefines.B_UPDATE_LISTVIEW = true;
            unRegisterReceiver();
            NVPlayerPlayFishEyeActivity.this.finish();

            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            return;
        }

        View view = View.inflate(this, R.layout.show_alert_dialog, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.alert_stop_play));
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.GONE);
        new AlertDialog.Builder(this)//
                .setView(view).setNegativeButton(getString(R.string.alert_btn_NO), null).setPositiveButton(getString(R.string.alert_btn_YES), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                try {
                    stopPlay(false);
                } catch (Exception e) {

                }

                setResult(RESULT_OK);

                mIsPlaying = false;

                m_bFinish = true;

                LocalDefines.B_UPDATE_LISTVIEW = true;
                unRegisterReceiver();
                NVPlayerPlayFishEyeActivity.this.finish();

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
            String action = LocalDefines.getReceiverActionString(NVPlayerPlayFishEyeActivity.this);
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
            LocalDefines.B_UPDATE_LISTVIEW = true;
            unRegisterReceiver();
            NVPlayerPlayFishEyeActivity.this.finish();
        } else {
            setResult(RESULT_OK);
            stopPlay(false);
            m_bFinish = true;
            LocalDefines.B_UPDATE_LISTVIEW = true; // add by mai 2015-6-29刷新界面
            unRegisterReceiver();
            NVPlayerPlayFishEyeActivity.this.finish();
        }
    }

    public class SelectAreaAdapter extends BaseAdapter {

        GridView mGv;
        Context context;
        public int ROW_NUMBER = 4;
        ArrayList<Integer> listDate;

        public SelectAreaAdapter(GridView gv, Context mContext, ArrayList<Integer> list) {
            this.context = mContext;
            this.mGv = gv;
            listDate = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.draggable_grid_item, null);

            if (listDate != null) {

                for (int i = 0; i < listDate.size(); i++) {
                    int isSelect = listDate.get(i);
                    if (position == i) {
                        if (isSelect == 1) {


                            convertView.setBackgroundColor(Color.parseColor("#ff0000"));
                            convertView.getBackground().setAlpha(51);
                        } else {
                            convertView.setBackgroundColor(Color.parseColor("#000000"));
                            convertView.getBackground().setAlpha(20);
                        }
                    }
                }
            }

            int statusBarHeight = LocalDefines.getStatusBarHeight(NVPlayerPlayFishEyeActivity.this);

            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (42 * scale + 0.5f);
            int nHeight = (int) ((mScreenHeight - statusBarHeight) * 0.6 - padding_in_px);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, nHeight / LocalDefines.alarmrows);
            convertView.setLayoutParams(param);

            return convertView;
        }

        @Override
        public int getCount() {

            return listDate.size();
        }

        @Override
        public Object getItem(int position) {


            return listDate.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }
    }

}
