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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemMediaPlayer extends AppCompatActivity implements View.OnClickListener {
    private VideoView vv;
    private Uri uri;
    private Utils utils;
    private  MyBroadCastReceiver receiver;


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
            // Handle clicks for btnExit
        } else if ( v == btnPre ) {
            // Handle clicks for btnPre
        } else if ( v == btnStartPause ) {
            if(vv.isPlaying()){
                vv.pause();
                btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
            }else {
                vv.start();
                btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
            }
        } else if ( v == btnNext ) {
            // Handle clicks for btnNext
        } else if ( v == btnSwitchScreen ) {
            // Handle clicks for btnSwitchScreen
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

        uri = getIntent().getData();

        //设置播放地址
        vv.setVideoURI(uri);

    }

    private void initData() {
        utils=new Utils();
        //监视电量变化的广播
         receiver = new MyBroadCastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        //电量的变化
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,intentFilter);
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
                finish();
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

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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