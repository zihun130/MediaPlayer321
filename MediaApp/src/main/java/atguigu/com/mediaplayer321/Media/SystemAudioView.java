package atguigu.com.mediaplayer321.Media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import atguigu.com.mediaplayer321.IAudioService;
import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.Service.AudioService;
import atguigu.com.mediaplayer321.View.BaseVisualizerView;
import atguigu.com.mediaplayer321.View.ScrollContentView;
import atguigu.com.mediaplayer321.domain.ContentInfo;
import atguigu.com.mediaplayer321.domain.MediaItem;
import atguigu.com.mediaplayer321.utils.LyricsUtils;
import atguigu.com.mediaplayer321.utils.Utils;

public class SystemAudioView extends AppCompatActivity implements View.OnClickListener {

    private static final int SHOW_CONTENT = 1;
    private RelativeLayout rlTop;
    private ImageView ivAudioIcon;
    private TextView tvArtist;
    private TextView tvAudioname;
    private LinearLayout llAudioBottom;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnPlaymode;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnLyric;


    private ScrollContentView scv_content;

    private IAudioService  service;
    private int position;
    private Myreceiver receiver;
    private Utils utils;

    private BaseVisualizerView visualizerview;
    private Visualizer mVisualizer;

    private final static int PROGRESS=0;

    private boolean notification;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case SHOW_CONTENT:
                    try {
                        int currentPosition = service.getCurrentPosition();

                        scv_content.setNextContent(currentPosition);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    removeMessages(SHOW_CONTENT);
                    sendEmptyMessage(SHOW_CONTENT);


                case  PROGRESS:

                    try {

                        int currentPosition = service.getCurrentPosition();
                        seekbarAudio.setProgress(currentPosition);

                        tvTime.setText(utils.stringForTime(currentPosition)+"/"+utils.stringForTime(service.getDuration()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS,1000);

                    break;
            }
        }
    };

    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder IBinder) {
            service=IAudioService.Stub.asInterface(IBinder);
            if(service!=null){

                    try {
                        if(notification){
                            setViewData(null);
                        }else {

                            service.openAudio(position);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-24 20:38:25 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        rlTop = (RelativeLayout)findViewById( R.id.rl_top );
        ivAudioIcon = (ImageView)findViewById( R.id.iv_audio_icon );
        tvArtist = (TextView)findViewById( R.id.tv_artist );
        tvAudioname = (TextView)findViewById( R.id.tv_audioname );
        llAudioBottom = (LinearLayout)findViewById( R.id.ll_audio_bottom );
        tvTime = (TextView)findViewById( R.id.tv_time );
        btnPlaymode = (Button)findViewById( R.id.btn_playmode );
        btnPre = (Button)findViewById( R.id.btn_pre );
        btnStartPause = (Button)findViewById( R.id.btn_start_pause );
        btnNext = (Button)findViewById( R.id.btn_next );
        btnLyric = (Button)findViewById( R.id.btn_lyric );
        seekbarAudio = (SeekBar)findViewById(R.id.seekbarAudio);

        visualizerview = (BaseVisualizerView)findViewById(R.id.visualizerview);


        scv_content = (ScrollContentView)findViewById(R.id.scv_content);

        btnPlaymode.setOnClickListener( this );
        btnPre.setOnClickListener( this );
        btnStartPause.setOnClickListener( this );
        btnNext.setOnClickListener( this );
        btnLyric.setOnClickListener( this );
        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    try {
                        service.seekTo(progress);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
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

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-24 20:38:25 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnPlaymode ) {
            setPlaymode();
        } else if ( v == btnPre ) {
            try {
                service.pre();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ( v == btnStartPause ) {
            try {
                if(service.isPlaying()){
                    service.pause();
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                }else {
                    service.start();
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ( v == btnNext ) {
            try {
                service.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ( v == btnLyric ) {
            // Handle clicks for btnLyric
        }
    }

    private void setPlaymode() {
        try {
            int playmode=service.getPlaymode();
            if(playmode==AudioService.REPEAT_NORMAL){
                playmode=AudioService.REPEAT_SINGLE;
            }else if(playmode==AudioService.REPEAT_SINGLE){
                playmode=AudioService.REPEAT_ALL;
            }else if(playmode==AudioService.REPEAT_ALL){
                playmode=AudioService.REPEAT_NORMAL;
            }

            service.setPlaymode(playmode);

            setBottonImage();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void setBottonImage() {
        try {
            int playmode=service.getPlaymode();
            if(playmode==AudioService.REPEAT_NORMAL){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_normal_selector);
            }else if(playmode==AudioService.REPEAT_SINGLE){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_single_selector);
            }else if(playmode==AudioService.REPEAT_ALL){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_all_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_audio_view);

        initData();

        findViews();
        getData();

        startAndBindService();
    }


    private void initData() {
        receiver=new Myreceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(AudioService.OPEN_COMPLETE);
        registerReceiver(receiver,intentFilter);

        utils=new Utils();


        EventBus.getDefault().register(this);
    }

    private class Myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //setViewData(null);
        }
    }


    private void getData() {
        notification=getIntent().getBooleanExtra("notification",false);
        if(!notification){
            position=getIntent().getIntExtra("position",0);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setViewData(MediaItem mediaItem) {

        try {
            setBottonImage();
            tvArtist.setText(service.getArtistName());
            tvAudioname.setText(service.getAudioName());
            int duration=service.getDuration();
            seekbarAudio.setMax(duration);



            //解析歌词
            //1.得到歌词所在路径
            String audioPath = service.getAudioPath();//mnt/sdcard/audio/beijingbeijing.mp3

            String lyricPath = audioPath.substring(0,audioPath.lastIndexOf("."));//mnt/sdcard/audio/beijingbeijing
            File file = new File(lyricPath+".lrc");
            if(!file.exists()){
                file = new File(lyricPath+".txt");
            }
            LyricsUtils lyricsUtils = new LyricsUtils();
            lyricsUtils.readFile(file);

            //2.传入解析歌词的工具类
            ArrayList<ContentInfo> lyrics = lyricsUtils.getLyrics();
            scv_content.setLyrics(lyrics);

            //3.如果有歌词，就歌词同步
            if(lyricsUtils.isLyric()){
                handler.sendEmptyMessage(SHOW_CONTENT);
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }

        handler.sendEmptyMessage(PROGRESS);
       //显示音乐频谱
        setupVisualizerFxAndUi();

    }

    private void setupVisualizerFxAndUi() {
        int audioSessionid = 0;
        try {
            audioSessionid = service.getAudioSessionId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("audioSessionid==" + audioSessionid);
        mVisualizer = new Visualizer(audioSessionid);
        // 参数内必须是2的位数
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // 设置允许波形表示，并且捕获它
        visualizerview.setVisualizer(mVisualizer);
        mVisualizer.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            mVisualizer.release();
        }
    }

    @Override
    protected void onDestroy() {

        if(conn!=null){
            unbindService(conn);
            conn=null;
        }

        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }

        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }


        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void startAndBindService(){
        Intent intent=new Intent(this,AudioService.class);
        //绑定-得到服务的操作对象
        bindService(intent,conn,Context.BIND_AUTO_CREATE);
        //防止多次实例化Service
        startService(intent);

    }
}
