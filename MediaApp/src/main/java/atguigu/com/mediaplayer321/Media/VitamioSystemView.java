package atguigu.com.mediaplayer321.Media;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
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

import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.View.VitamioVideoView;
import atguigu.com.mediaplayer321.domain.MediaItem;
import atguigu.com.mediaplayer321.utils.Utils;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;


public class VitamioSystemView extends AppCompatActivity implements View.OnClickListener {

    private static final int HIDEMEDIACONTROLLER = 1;
    private VitamioVideoView vv;
    private Uri uri;
    private Utils utils;
    private MyBroadCastReceiver receiver;
    private ArrayList<MediaItem> mediaItems;
    private GestureDetector detector;
    private boolean isShowMediaController=false;
    //是否全屏
    private boolean isFullSrceen=false;
    //屏幕的高与宽；
    private int screenHeight;
    private int screenWidth;
    //原生的高和宽
    private int videoHeight;
    private int videoWidth;
    //判断是否有网络资源
    private boolean isNetUri;
    private LinearLayout ll_buffering;
    private TextView     tv_net_speed;
    private int precurrentPosition;





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
    private int position;
    private static final int  DEFAULT_SCREEN=0;
    private static final int FULL_SCREEN=1;
    private int maxVoice;
    private int currentVoice;
    private AudioManager am;
    private boolean isMute=false;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-20 11:49:44 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        //初始化解码器
        Vitamio.isInitialized(getApplicationContext());
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

        ll_buffering = (LinearLayout)findViewById(R.id.ll_buffering);
        tv_net_speed = (TextView)findViewById(R.id.tv_net_speed);

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnPre.setOnClickListener( this );
        btnStartPause.setOnClickListener( this );
        btnNext.setOnClickListener( this );
        btnSwitchScreen.setOnClickListener( this );
        //设置最大声音（0-15）
        seekbarVoice.setMax(maxVoice);
        seekbarVoice.setProgress(currentVoice);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-20 11:49:44 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            isMute=!isMute;

            updataVoice(isMute);
        } else if ( v == btnSwitchPlayer ) {
            switchPlayer();
        } else if ( v == btnExit ) {
           finish();
        } else if ( v == btnPre ) {
            setPrevideo();
        } else if ( v == btnStartPause ) {
            setStartOrPause();
        } else if ( v == btnNext ) {
           setNextvideo();
        } else if ( v == btnSwitchScreen ) {
            if(isFullSrceen){
                //默认
                setVideoType(DEFAULT_SCREEN);
            }else {
                //全屏
                setVideoType(FULL_SCREEN);
            }
        }

        handler.removeMessages(HIDEMEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,4000);
    }

    private void switchPlayer() {
        new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("如果当前为万能播放器播放，当播放有色块，播放质量不好，请切换到系统播放器播放")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSystemView();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();

    }

    private void startSystemView() {
        if(vv!=null){
            vv.stopPlayback();
        }
        Intent intent=new Intent(this,SystemView.class);
        if(mediaItems!=null && mediaItems.size()>0){
            Bundle bundle=new Bundle();
            bundle.putSerializable("videoList",mediaItems);
            intent.putExtra("position",position);
            intent.putExtras(bundle);

        }else if(uri!=null){
            intent.setData(uri);
        }

        startActivity(intent);
        finish();
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

    //设置视频的全屏与默认
    private void setVideoType(int videoType) {
        switch (videoType) {
            case FULL_SCREEN :
                isFullSrceen=true;
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_default_selector);
                vv.setVideoSize(screenWidth,screenHeight);

                break;
            case DEFAULT_SCREEN :
                isFullSrceen=false;

                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_full_selector);
                 //视频原生高与宽
                int mVideoWidth=videoWidth;
                int mVideoHeight=videoHeight;
                //计算出的显示高与宽
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



    private void setStartOrPause() {
        if(vv.isPlaying()){
            vv.pause();
            btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
        }else {
            vv.start();
            btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_view);
        vv = (VitamioVideoView)findViewById(R.id.vv);


        initData();
        findViews();

        getData();
        setData();

        setListener();

    }

    private void initData() {
        utils=new Utils();
        receiver = new MyBroadCastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,intentFilter);

        //实例化手势识别器
        detector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            //长按
            public void onLongPress(MotionEvent e) {
                setStartOrPause();
                super.onLongPress(e);
            }

            @Override
            //双击
            public boolean onDoubleTap(MotionEvent e) {
                if(isFullSrceen){
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
                    handler.sendEmptyMessage(HIDEMEDIACONTROLLER);
                }else {
                    showMediaController();
                    handler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,4000);
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
    //坐标
    private float downY;
    //滑动的初始声音
    private int   mVoice;
    //滑动的区域
    private float touchS;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downY=event.getY();
                mVoice=am.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchS=Math.min(screenWidth,screenHeight);
                handler.removeMessages(HIDEMEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE :
                float endY=event.getY();
                float distanceY=downY-endY;
                float changeY=(distanceY/touchS)*mVoice;
                if(changeY!=0){
                    int finalVoice= (int) Math.min(Math.max(mVoice+changeY,0),maxVoice);
                    updataVoiceProgress(finalVoice);
                }

                break;
            case MotionEvent.ACTION_UP :
                handler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,4000);

                break;
        }
        return super.onTouchEvent(event);
    }

    private void hideMediaController(){
        llBottom.setVisibility(View.INVISIBLE);
        llTop.setVisibility(View.GONE);
        isShowMediaController=false;
    }

    private void showMediaController(){
        llBottom.setVisibility(View.VISIBLE);
        llTop.setVisibility(View.VISIBLE);
        isShowMediaController=true;

    }

    private void getData() {
        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videoList");
        position=getIntent().getIntExtra("position",0);
    }

    private void setData() {
        if(mediaItems!=null && mediaItems.size()>0){
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());
            vv.setVideoPath(mediaItem.getData());
            //有网时获取网络地址
            isNetUri=utils.isNetUri(mediaItem.getData());
        }else if(uri!=null){
            vv.setVideoURI(uri);
            tvName.setText(uri.toString());
            //有网时获取网络地址
            isNetUri=utils.isNetUri(uri.toString());
        }
       setButtonStatus();

    }

    private void setButtonStatus() {
        if(mediaItems!=null && mediaItems.size()>0){
            setEnable(true);
            if(position==0){
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);
            }
            if(position==mediaItems.size()-1){
                btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnNext.setEnabled(false);
            }
        }else if(uri!=null){
            setEnable(false);
        }
    }

    private void setEnable(boolean b) {
        if(b){
            btnPre.setBackgroundResource(R.drawable.btn_pre_selector);
            btnNext.setBackgroundResource(R.drawable.btn_next_selector);
        }else{
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnNext.setBackgroundResource(R.drawable.btn_next_gray);
        }

        btnPre.setEnabled(b);
        btnNext.setEnabled(b);
    }


    private void setNextvideo() {
        position++;
        if(position < mediaItems.size()){
            MediaItem mediaItem = mediaItems.get(position);
            vv.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
            //有网时获取网络地址
            isNetUri=utils.isNetUri(mediaItem.getData());
            setButtonStatus();
        }else {
            Toast.makeText(VitamioSystemView.this, "退出播放器", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setPrevideo() {
        position--;
        //列表是从0开始的。所以要有等于0。
        if(position >=0){
            MediaItem mediaItem = mediaItems.get(position);
            vv.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
            //有网时获取网络地址
            isNetUri=utils.isNetUri(mediaItem.getData());
            setButtonStatus();
        }
    }


    class MyBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level=intent.getIntExtra("level",0);
            setBatteryView(level);
        }
    }

    private void setBatteryView(int level) {
        if(level <=0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level <= 10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if(level <=20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if(level <=40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if(level <=60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if(level <=80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if(level <=100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    int currentPosition = (int) vv.getCurrentPosition();
                    seekbarVideo.setProgress(currentPosition);

                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    tvSystemTime.setText(getSystemTime());
                    //设置视频缓存效果
                    if(isNetUri){
                        int bufferPercentage = vv.getBufferPercentage();//0~100;
                        int totalBuffer = bufferPercentage*seekbarVideo.getMax();
                        int secondaryProgress =totalBuffer/100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    }else{
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    //设置缓存指示图
                    if(isNetUri && vv.isPlaying()){
                        int duration=currentPosition-precurrentPosition;
                        if(duration<500){
                            ll_buffering.setVisibility(View.VISIBLE);
                        }else {
                            ll_buffering.setVisibility(View.GONE);
                        }
                        precurrentPosition=currentPosition;
                    }
                    sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
                case HIDEMEDIACONTROLLER:
                    hideMediaController();
                    break;
            }

        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    private void setListener() {
        //准备好监听
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoWidth=mp.getVideoWidth();
                videoHeight=mp.getVideoHeight();

                int duration = (int) vv.getDuration();
                seekbarVideo.setMax(duration);
                tvDuration.setText(utils.stringForTime(duration));
                vv.start();
                handler.sendEmptyMessage(PROGRESS);

                hideMediaController();
                setVideoType(DEFAULT_SCREEN);
            }
        });
        //播放错误监听
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showErrorDialog();
                return false;
            }
        });
        //播放完成监听
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               setNextvideo();
            }
        });
        //设置视频变化的监听
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    vv.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDEMEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,4000);

            }
        });

        //设置声音拖动监听
        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    updataVoiceProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDEMEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,4000);
            }
        });

    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前视频不可播放，请检查网络或者视频文件是否有损坏！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
    }

    //声音滑动的变化
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
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler=null;
        }

        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVoice--;
            updataVoiceProgress(currentVoice);
            handler.removeMessages(HIDEMEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,4000);
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            currentVoice++;
            updataVoiceProgress(currentVoice);
            handler.removeMessages(HIDEMEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,4000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
