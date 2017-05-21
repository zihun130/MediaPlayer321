package atguigu.com.media000;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemMediaPlayer extends AppCompatActivity implements View.OnClickListener {
    private static final int HIDE_MEDIACONTROLLER = 1;
    private VideoView vv;
    private Uri uri;
    private Utils utils;
    private  MyBroadCastReceiver receiver;
    private ArrayList<MediaItems> mediaItem;
    private int position;
    private GestureDetector detector;
    private boolean isShowMediaController = false;
    private boolean isFullScreen=false;


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

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnPre.setOnClickListener( this );
        btnStartPause.setOnClickListener( this );
        btnNext.setOnClickListener( this );
        btnSwitchScreen.setOnClickListener( this );
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
            // Handle clicks for btnVoice
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
        } else if ( v == btnExit ) {
            finish();
        } else if ( v == btnPre ) {
           setPreVideo();
        } else if ( v == btnStartPause ) {
            setStartOrPause();
        } else if ( v == btnNext ) {
            setNextVideo();
        } else if ( v == btnSwitchScreen ) {


        }

        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
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
            tvName.setText(mediaItems.getName());
            vv.setVideoPath(mediaItems.getData());

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
            tvName.setText(mediaItems.getName());
            vv.setVideoPath(mediaItems.getData());

            setButtonStatus();
        }
    }

    private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case PROGRESS :
                //得到当前进度
                int currentPosition = vv.getCurrentPosition();
                //更新进度
                seekbarVideo.setProgress(currentPosition);
                //设置当前时间
                tvCurrentTime.setText(utils.stringForTime(currentPosition));
                //设置系统时间
                tvSystemTime.setText(getSystemTime());

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
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        return dateFormat.format(new Date());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_media_player);
        vv = (VideoView)findViewById(R.id.vv);

        findViews();
        initData();

        setListener();

        setData();
        getData();



    }

    private void setData() {
        if(mediaItem!=null && mediaItem.size()>0){
            MediaItems mediaItems = mediaItem.get(position);
            tvName.setText(mediaItems.getName());
            vv.setVideoPath(mediaItems.getData());
        }else if(uri!=null){
            vv.setVideoURI(uri);
        }
    }

    private void getData() {
        uri=getIntent().getData();
        mediaItem = (ArrayList<MediaItems>) getIntent().getSerializableExtra("videoList");
        position=getIntent().getIntExtra("position",0);
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
                super.onLongPress(e);
            }

            @Override
            //双击
            public boolean onDoubleTap(MotionEvent e) {
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
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
                //文本总时间
                int duration=vv.getDuration();
                seekbarVideo.setMax(duration);
                //设置文本时间
                tvDuration.setText(utils.stringForTime(duration));
                vv.seekTo(100);
                vv.start();

                handler.sendEmptyMessage(PROGRESS);

                hideMediaController();
            }
        });
        //播放错误监听
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemMediaPlayer.this, "播放出错了", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
       //播放完成监听
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SystemMediaPlayer.this, "播放已到尽头", Toast.LENGTH_SHORT).show();
                finish();
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
}
