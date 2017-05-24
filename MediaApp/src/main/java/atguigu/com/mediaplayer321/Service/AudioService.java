package atguigu.com.mediaplayer321.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import atguigu.com.mediaplayer321.IAudioService;
import atguigu.com.mediaplayer321.Media.SystemAudioView;
import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.MediaItem;

public class AudioService extends Service {


    private IAudioService.Stub stub= new IAudioService.Stub() {

       AudioService service=AudioService.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);

        }

        @Override
        public void start() throws RemoteException {
            service.start();

        }

        @Override
        public void pause() throws RemoteException {
            service.pause();

        }

        @Override
        public String getArtistName() throws RemoteException {
            return service.getArtistName();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);

        }

        @Override
        public void next() throws RemoteException {
            service.next();

        }

        @Override
        public void pre() throws RemoteException {
            service.pre();

        }

       @Override
       public boolean isPlaying() throws RemoteException {
           return mediaPlayer.isPlaying();
       }

       @Override
       public int getPlaymode() throws RemoteException {
           return service.getPlaymode();
       }

       @Override
       public void setPlaymode(int playmode) throws RemoteException {
           service.setPlaymode(playmode);

       }
   };


    private ArrayList<MediaItem> mediaItems;
    private MediaPlayer  mediaPlayer;
    private MediaItem mediaItem;
    private int position;
    //播放模式
    private int playmode = REPEAT_NORMAL;

    //顺序播放
    public static final int REPEAT_NORMAL = 1;
    //单曲循环
    public static final int REPEAT_SINGLE = 2;
    //无限循环
    public static final int REPEAT_ALL = 3;

    public static final String OPEN_COMPLETE = "com.atguigu.mobileplayer.OPEN_COMPLETE";
    private NotificationManager nm;

    //true为正常播放  false为手动下一个
    private boolean isCompletion=false;


    private SharedPreferences sp;


    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("atguigu",MODE_PRIVATE);
        playmode = sp.getInt("playmode",getPlaymode());
        getData();
    }


    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();
                Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST
                };
                Cursor cursor=resolver.query(uri,objs,null,null,null);
                if(cursor!=null){
                    while (cursor.moveToNext()){
                        String name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        long   duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long   size=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        String data=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                        if(duration>10*1000){
                            mediaItems.add(new MediaItem(name,duration,size,data,artist));
                        }



                    }
                    cursor.close();
                }
            }
        }).start();

    }



    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
    //根据位置打开音频
    private void openAudio(int position){
        this.position=position;
        if(mediaItems!=null && mediaItems.size()>0){

            if(position<mediaItems.size()){
                mediaItem = mediaItems.get(position);

                //如果播放器不为空,释放空间并置为空
                if(mediaPlayer!=null){
                    mediaPlayer.reset();
                    mediaPlayer=null;
                }

                try {
                    mediaPlayer=new MediaPlayer();
                    //设置播放地址
                    mediaPlayer.setDataSource(mediaItem.getData());

                    //设置基本的三个监听:准备好监听,错误监听,播放完成监听
                    mediaPlayer.setOnPreparedListener(new MyPrepared());
                    mediaPlayer.setOnErrorListener(new MyError());
                    mediaPlayer.setOnCompletionListener(new MyCompletion());

                    mediaPlayer.prepareAsync();

                    if(playmode==AudioService.REPEAT_SINGLE){
                        isCompletion=false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else {
            Toast.makeText(AudioService.this, "音频还没有加载完成", Toast.LENGTH_SHORT).show();
        }
    }

    //播放
    private void start(){
        mediaPlayer.start();
        nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent=new Intent(this, SystemAudioView.class);
        intent.putExtra("notification",true);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification=new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("321音乐")
                .setContentText("正在播放"+getAudioName())
                .setContentIntent(pi)
                .build();
        nm.notify(1,notification);
    }

    //暂停
    private void pause(){
        mediaPlayer.pause();
        nm.cancel(1);
    }

    //得到名字
    private String getArtistName(){
        return mediaItem.getArtist();
    }

    //得到歌名
    private String getAudioName(){
        return mediaItem.getName();
    }

    //得到路径
    private String getAudioPath(){
        return "";
    }

    //得到总时长
    private int getDuration(){
        return mediaPlayer.getDuration();
    }

    //得到当前进度
    private int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    //音频拖动
    private void seekTo(int position){
        mediaPlayer.seekTo(position);
    }

    //下一个
    private void next(){
        //根据不同的模式设置不同的下标
        setNextPosition();
        //根据不同的下标播放不同的音频  边界处理
        openNextPosition();
    }

    private void openNextPosition() {
        int playmode=getPlaymode();
        if(playmode==AudioService.REPEAT_NORMAL){
            if(position < mediaItems.size()){
                openAudio(position);
            }else {
                position=mediaItems.size()-1;
            }
        }else if(playmode==AudioService.REPEAT_SINGLE){
            if(position< mediaItems.size()){
                openAudio(position);
            }else {
                position=mediaItems.size()-1;
            }
        }else if(playmode==AudioService.REPEAT_ALL){
            openAudio(position);
        }
    }

    private void setNextPosition() {
        int playmode = getPlaymode();
        if(playmode==AudioService.REPEAT_NORMAL){
            position++;
        }else if(playmode==AudioService.REPEAT_SINGLE){
            if(!isCompletion){
                position++;
            }
        }else if(playmode==AudioService.REPEAT_ALL){
            position++;
            if(position>mediaItems.size()-1){
                position=0;
            }
        }

    }

    //上一个
    private void pre(){
        //根据不同的模式设置不同的下标
        setPrePosition();
        //根据不同的下标播放不同的音频  边界处理
        openPrePosition();
    }

    private void openPrePosition() {
        int playmode=getPlaymode();
        if(playmode==AudioService.REPEAT_NORMAL){
            if(position >= 0){
                openAudio(position);
            }else {
                position=0;
            }
        }else if(playmode==AudioService.REPEAT_SINGLE){
            if(position>=0){
                openAudio(position);
            }else {
                position=0;
            }
        }else if(playmode==AudioService.REPEAT_ALL){
            openAudio(position);
        }
    }

    private void setPrePosition() {
        int playmode = getPlaymode();
        if(playmode==AudioService.REPEAT_NORMAL){
            position--;
        }else if(playmode==AudioService.REPEAT_SINGLE){
            if(!isCompletion){
                position--;
            }
        }else if(playmode==AudioService.REPEAT_ALL){
            position--;
            if(position<0){
                position=mediaItems.size()-1;
            }
        }


    }

    //得到播放模式
    public int getPlaymode(){
        return playmode;
    }

    //设置播放模式
    public void setPlaymode(int playmode){
        this.playmode=playmode;
        sp.edit().putInt("playmode",playmode).commit();
    }

   //准备好监听
    private class MyPrepared implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {

            notifyChange(OPEN_COMPLETE);
            start();

        }
    }

    private void notifyChange(String action) {
        Intent intent=new Intent(action);
        sendBroadcast(intent);
    }

    //错误监听
    private class MyError implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }
    //播放完成监听
    private class MyCompletion implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            isCompletion=true;
            next();

        }
    }
}
