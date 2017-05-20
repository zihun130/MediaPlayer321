package atguigu.com.mediaplayer321.Media;

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
import java.util.ArrayList;
import java.util.Date;

import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.MediaItem;
import atguigu.com.mediaplayer321.utils.Utils;

public class SystemView extends AppCompatActivity implements View.OnClickListener {

    private VideoView vv;
    private Uri uri;
    private Utils utils;
    private MyBroadCastReceiver receiver;
    private ArrayList<MediaItem> mediaItems;

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

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-20 11:49:44 by Android Layout Finder
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
     * Auto-created on 2017-05-20 11:49:44 by Android Layout Finder
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
            setPrevideo();
        } else if ( v == btnStartPause ) {
            if(vv.isPlaying()){
                vv.pause();
                btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
            }else {
                vv.start();
                btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
            }
        } else if ( v == btnNext ) {
           setNextvideo();
        } else if ( v == btnSwitchScreen ) {
            // Handle clicks for btnSwitchScreen
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_view);
        vv = (VideoView)findViewById(R.id.vv);


        initData();
        findViews();

        getData();
        setData();

        setListener();


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
        }else if(uri!=null){
            vv.setVideoURI(uri);
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
            setButtonStatus();
        }else {
            Toast.makeText(SystemView.this, "退出播放器", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setPrevideo() {
        position--;
        if(position >0){
            MediaItem mediaItem = mediaItems.get(position);
            vv.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
            setButtonStatus();
        }
    }


    private void initData() {
        utils=new Utils();
        receiver = new MyBroadCastReceiver();
        IntentFilter intentFilter=new IntentFilter();
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
                    int currentPosition = vv.getCurrentPosition();
                    seekbarVideo.setProgress(currentPosition);

                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    tvSystemTime.setText(getSystemTime());
                    sendEmptyMessageDelayed(PROGRESS,1000);

                    break;
            }

        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    private void setListener() {
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = vv.getDuration();
                seekbarVideo.setMax(duration);
                tvDuration.setText(utils.stringForTime(duration));
                vv.start();
                handler.sendEmptyMessage(PROGRESS);
            }
        });

        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemView.this, "播放出错了！！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               setNextvideo();
            }
        });

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
}
