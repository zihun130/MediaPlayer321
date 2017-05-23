package atguigu.com.media000;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemMediaPlayer extends AppCompatActivity implements View.OnClickListener {
    private static final int HIDE_MEDIACONTROLLER = 1;
    private static final int DEFAULT_SCREEN = 0;
    private static final int FULL_SCREEN = 1;
    private static final int SHOW_NET_SPEED = 2;
    private VideoView vv;
    private Uri uri;
    private Utils utils;
    private  MyBroadCastReceiver receiver;
    private ArrayList<MediaItems> mediaItem;
    private int position;
    private GestureDetector detector;
    private boolean isShowMediaController = false;
    //是否全屏
    private boolean isFullScreen=false;
    //判断是否有网络
    private boolean isNetUrl=true;
    private int     preposition;
    private LinearLayout ll_loading;
    private TextView     tv_loading_net_speed;
    private LinearLayout ll_buffering;
    private TextView     tv_net_speed;


    private int screenWidth;
    private int screenHeight;
    private int videoWidth;
    private int videoHeight;

    //设置声音的变量
    private int maxVoice;
    private int currentVoice;
    private AudioManager am;
    private boolean isMute=false;


    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnSwitchScreen;
    private static final int PROGRESS=0;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-21 17:43:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        tvSystemTime = (TextView)findViewById( R.id.tv_system_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (Button)findViewById( R.id.btn_switch_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrentTime = (TextView)findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnPre = (Button)findViewById( R.id.btn_pre );
        btnStartPause = (Button)findViewById( R.id.btn_start_pause );
        btnNext = (Button)findViewById( R.id.btn_next );
        btnSwitchScreen = (Button)findViewById( R.id.btn_switch_screen );


        ll_loading = (LinearLayout)findViewById(R.id.ll_loading);
        tv_loading_net_speed = (TextView)findViewById(R.id.tv_loading_net_speed);
        ll_buffering = (LinearLayout)findViewById(R.id.ll_buffering);
        tv_net_speed = (TextView)findViewById(R.id.tv_net_speed);


        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnPre.setOnClickListener( this );
        btnStartPause.setOnClickListener( this );
        btnNext.setOnClickListener( this );
        btnSwitchScreen.setOnClickListener( this );

        handler.sendEmptyMessage(SHOW_NET_SPEED);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-21 17:43:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            isMute=!isMute;
            updataVoice(isMute);
        } else if ( v == btnSwitchPlayer ) {
            switchMediaPlayer();
        } else if ( v == btnExit ) {
            finish();
        } else if ( v == btnPre ) {
           setPreVideo();
        } else if ( v == btnStartPause ) {
            setStartOrPause();
        } else if ( v == btnNext ) {
            setNextVideo();
        } else if ( v == btnSwitchScreen ) {
           if(isFullScreen){
               setVideoType(DEFAULT_SCREEN);
           }else {
               setVideoType(FULL_SCREEN);
           }

        }

        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
    }

    private void switchMediaPlayer() {
        new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前使用系统播放器播放，当播放有声音没有画面，请切换到万能播放器播放")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startVitamioSystemView();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
    }

    private void updataVoice(boolean isMute) {
        if(isMute){
            //静音
            am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
            seekbarVoice.setProgress(0);
        }else {
            //非静音
            am.setStreamVolume(AudioManager.STREAM_MUSIC,currentVoice,0);
            seekbarVoice.setProgress(currentVoice);
        }

    }

    private void setVideoType(int videoType) {
        switch (videoType) {
            case FULL_SCREEN:
                isFullScreen=true;
                //默认按钮
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_default_selector);
                //设置全屏显示
                vv.setVideoSize(screenWidth,screenHeight);
                break;

            case DEFAULT_SCREEN:
                isFullScreen=false;
                //全屏按钮
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_full_selector);
                //原生宽和高
                int mVideoWidth=videoWidth;
                int mVideoHeight=videoHeight;
                //显示的宽和高
                int width=screenWidth;
                int height=screenHeight;
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }

                vv.setVideoSize(width,height);
                break;
        }

    }

    //开始与暂停设置
    private void setStartOrPause() {
        if(vv.isPlaying()){
            vv.pause();
            btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
        }else {
            vv.start();
            btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
        }
    }
    //按钮状态
    private void setButtonStatus() {
        if(mediaItem!=null && mediaItem.size()>0){
            //有视频播放
            setEnable(true);
            if(position==0){
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                setEnable(false);
            }
            if(position==mediaItem.size()-1){
                btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                setEnable(false);
            }
        }else if(uri!=null){
            setEnable(false);
        }

    }

    private void setEnable(boolean b) {
        if(b){
            //上一个和下一个可以点击
            btnPre.setBackgroundResource(R.drawable.btn_pre_selector);
            btnNext.setBackgroundResource(R.drawable.btn_next_selector);
        }else {
            //上一个和下一个不可以点击
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnNext.setBackgroundResource(R.drawable.btn_next_gray);
        }

        btnPre.setEnabled(b);
        btnNext.setEnabled(b);
    }
    //下一个视频播放设置
    private void setNextVideo() {
        position++;
        if(position < mediaItem.size()){
            MediaItems mediaItems = mediaItem.get(position);
            isNetUrl=utils.isNetUri(mediaItems.getData());

            ll_loading.setVisibility(View.VISIBLE);
            vv.setVideoPath(mediaItems.getData());
            tvName.setText(mediaItems.getName());
            setButtonStatus();
        }else {
            Toast.makeText(SystemMediaPlayer.this, "退出播放器", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    //上一个视频播放设置
    private void setPreVideo() {
        position--;
        if(position >= 0){
            MediaItems mediaItems = mediaItem.get(position);

            isNetUrl=utils.isNetUri(mediaItems.getData());
            ll_loading.setVisibility(View.VISIBLE);

            vv.setVideoPath(mediaItems.getData());
            tvName.setText(mediaItems.getName());

            setButtonStatus();
        }
    }

    private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SHOW_NET_SPEED:
                if(isNetUrl){
                    String netSpeed = utils.getNetSpeed(SystemMediaPlayer.this);
                    tv_loading_net_speed.setText("正在加载中..."+netSpeed);
                    tv_net_speed.setText("正在缓冲..."+netSpeed);
                    handler.sendEmptyMessageDelayed(SHOW_NET_SPEED,1000);

                }
            case PROGRESS :
                //得到当前进度
                int currentPosition = vv.getCurrentPosition();
                //更新进度
                seekbarVideo.setProgress(currentPosition);
                //设置当前时间
                tvCurrentTime.setText(utils.stringForTime(currentPosition));
                //设置系统时间
                tvSystemTime.setText(getSystemTime());

                if(isNetUrl){
                    int bufferPercentage = vv.getBufferPercentage();
                    int totalBuffer=bufferPercentage*seekbarVideo.getMax();
                    int secondaryBuffer=totalBuffer/100;
                    seekbarVideo.setSecondaryProgress(secondaryBuffer);
                }else {
                    seekbarVideo.setSecondaryProgress(0);
                }

                /*if(isNetUrl && vv.isPlaying()){
                    int duration=currentPosition-preposition;
                    if(duration<500){
                        ll_buffering.setVisibility(View.VISIBLE);
                    }else {
                        ll_buffering.setVisibility(View.GONE);
                    }

                    preposition=currentPosition;
                }*/

                sendEmptyMessageDelayed(PROGRESS,1000);
                break;
            case  HIDE_MEDIACONTROLLER:
                hideMediaController();
                break;
        }

    }
};
    //得到系统时间
    private String getSystemTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_media_player);
        vv = (VideoView)findViewById(R.id.vv);

        findViews();
        initData();
        //设置最大音量和当前进度
        seekbarVoice.setMax(maxVoice);
        seekbarVoice.setProgress(currentVoice);


        getData();
        setData();
        setListener();



    }

    private void getData() {
        uri=getIntent().getData();
        mediaItem = (ArrayList<MediaItems>) getIntent().getSerializableExtra("videoList");
        position=getIntent().getIntExtra("position",0);
    }


    private void setData() {
        if(mediaItem!=null && mediaItem.size()>0){
            MediaItems mediaItems = mediaItem.get(position);
            isNetUrl=utils.isNetUri(mediaItems.getData());
            tvName.setText(mediaItems.getName());
            vv.setVideoPath(mediaItems.getData());
        }else if(uri!=null){
            vv.setVideoURI(uri);
            isNetUrl=utils.isNetUri(uri.toString());
        }
        setButtonStatus();
    }


    private void initData() {
        utils=new Utils();
        //监视电量变化的广播
         receiver = new MyBroadCastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        //电量的变化
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,intentFilter);

        //手势识别器的设置
        detector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            //长按
            public void onLongPress(MotionEvent e) {
                setButtonStatus();
                super.onLongPress(e);
            }

            @Override
            //双击
            public boolean onDoubleTap(MotionEvent e) {
                if(isFullScreen){
                    setVideoType(DEFAULT_SCREEN);
                }else {
                    setVideoType(FULL_SCREEN);
                }
                return super.onDoubleTap(e);
            }

            @Override
            //单击
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(isShowMediaController){
                    hideMediaController();
                    handler.sendEmptyMessage(HIDE_MEDIACONTROLLER);
                }else {
                    showMediaController();
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        //初始化声音
        am= (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice=am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
    //显示视频控制
    private void showMediaController() {
        llBottom.setVisibility(View.VISIBLE);
        llTop.setVisibility(View.VISIBLE);
        isShowMediaController=true;

    }
    //隐藏视频控制
    private void hideMediaController() {
        llBottom.setVisibility(View.GONE);
        llTop.setVisibility(View.GONE);
        isShowMediaController=false;

    }

    private float downY;
    private int   mVoice;
    private float touchs;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downY=event.getY();
                mVoice=am.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchs=Math.min(screenWidth,screenHeight);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE :
                float endY=event.getY();
                float diatenceY=downY-endY;
                float changeY=(diatenceY/touchs)*maxVoice;
                if(changeY!=0){
                   int  finalVoice= (int) Math.min(Math.max((mVoice+changeY),0),maxVoice);
                    updataVoiceProgress(finalVoice);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
                break;
        }
        return super.onTouchEvent(event);
    }

    class MyBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level=intent.getIntExtra("level",0);
            setBatteryView(level);
        }
    }

    private void setBatteryView(int level) {
        if(level<=0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level<=10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if(level<=20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if(level<=40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if(level<=60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if(level<=80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if(level<=100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }

    }

    private void setListener() {
        //准备好监听
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                //文本总时间
                int duration=vv.getDuration();
                seekbarVideo.setMax(duration);
                //设置文本时间
                tvDuration.setText(utils.stringForTime(duration));
                vv.start();

                handler.sendEmptyMessage(PROGRESS);

                ll_loading.setVisibility(View.GONE);

                hideMediaController();
                setVideoType(DEFAULT_SCREEN);

                if(vv.isPlaying()){
                    //设置暂停
                    btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
                }else {
                    btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
                }
            }
        });
        //播放错误监听
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                startVitamioSystemView();
                return true;
            }
        });
       //播放完成监听
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                setNextVideo();
            }
        });

        //seekbarVideo状态的变化的监听
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    vv.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);

            }
        });
        //拖动监听声音
        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              if(fromUser){
                  updataVoiceProgress(progress);
              }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
            }
        });

        //设置监听卡顿现象
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            vv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START :
                            ll_buffering.setVisibility(View.VISIBLE);

                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END :
                            ll_buffering.setVisibility(View.GONE);
                            break;
                    }
                    return true;
                }
            });
        }
    }


   private void startVitamioSystemView(){
       if(vv!=null){
           vv.stopPlayback();
       }

       Intent intent=new Intent(this,VitamioSystemMediaPlayer.class);
       if(mediaItem!=null && mediaItem.size()>0){
           Bundle bundle=new Bundle();
           bundle.putSerializable("videoList",mediaItem);
           intent.putExtra("position",position);
           intent.putExtras(bundle);

       }else if(uri!=null){
           intent.setData(uri);
       }
       startActivity(intent);
       finish();
   }

    private void updataVoiceProgress(int progress) {
        currentVoice=progress;
        am.setStreamVolume(AudioManager.STREAM_MUSIC,currentVoice,0);
        seekbarVoice.setProgress(currentVoice);

        if(progress<=0){
            isMute=true;
        }else {
            isMute=false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler=null;
        }
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVoice--;
            updataVoiceProgress(currentVoice);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            currentVoice++;
            updataVoiceProgress(currentVoice);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
