package atguigu.com.mediaplayer001.Media;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

import atguigu.com.mediaplayer001.R;
import atguigu.com.mediaplayer001.Utils.Utils;
import atguigu.com.mediaplayer001.domain.MediaItem;

public class SystemNetVideoMedia extends AppCompatActivity implements View.OnClickListener {
    private static final int PROGRESS = 0;
    private VideoView vv;

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



    private Uri uri;
    private ArrayList<MediaItem> mediaItem;
    private int position;
    private boolean isNetUri=true;
    private Utils utils;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-25 16:19:09 by Android Layout Finder
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
     * Auto-created on 2017-05-25 16:19:09 by Android Layout Finder
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
            startPre();
        } else if ( v == btnStartPause ) {
            setStartOrPause();
        } else if ( v == btnNext ) {
            startNext();
        } else if ( v == btnSwitchScreen ) {
            // Handle clicks for btnSwitchScreen
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_net_video_media);
        vv = (VideoView)findViewById(R.id.vv);

        initdata();

        findViews();
        getData();
        setData();

        setListener();



    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case  PROGRESS:
                    int currentPosition = vv.getCurrentPosition();
                    seekbarVideo.setProgress(currentPosition);
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    break;
            }
        }
    };


    private void setStartOrPause() {
        if(vv.isPlaying()){
            vv.pause();
            btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
        }else {
            vv.start();
            btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
        }
    }
    private void initdata() {
        utils=new Utils();

    }


    private void getData() {
        uri = getIntent().getData();
        mediaItem = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videoList");
        Log.e("TAG","dianying "+mediaItem);
        position=getIntent().getIntExtra("position",0);
        Log.e("TAG","zuobiao "+position);
    }

    private void setData() {
        if(mediaItem!=null && mediaItem.size()>0){
            MediaItem mediaItems = mediaItem.get(position);
            Log.e("TAG","duixiang "+mediaItems);
            isNetUri=utils.isNetUri(mediaItems.getData());
            tvName.setText(mediaItems.getName());
            vv.setVideoPath(mediaItems.getData());
        }else if(uri!=null){
            vv.setVideoURI(uri);
            isNetUri=utils.isNetUri(uri.toString());
            tvName.setText(uri.toString());
        }

    }



    private void startNext() {
        position++;
        if(position < mediaItem.size()){
            MediaItem mediaItem = this.mediaItem.get(position);
            vv.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
        }else {
            Toast.makeText(SystemNetVideoMedia.this, "退出播放器", Toast.LENGTH_SHORT).show();
        }

    }

    private void startPre() {
        position--;
        if(position>=0){
            MediaItem mediaItem = this.mediaItem.get(position);
            vv.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
        }

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
                return false;
            }
        });


        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vv.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
