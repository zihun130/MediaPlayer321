// IAudioService.aidl
package atguigu.com.mediaplayer321;

// Declare any non-default types here with import statements

interface IAudioService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    //根据位置打开音频
        void openAudio(int position);

        //播放
        void start();

        //暂停
        void pause();

        //得到名字
        String getArtistName();

        //得到歌名
        String getAudioName();

        //得到路径
        String getAudioPath();

        //得到总时长
        int getDuration();

        //得到当前进度
        int getCurrentPosition();

        //音频拖动
        void seekTo(int position);

        //下一个
        void next();

        //上一个
        void pre();

        boolean isPlaying();

        int getPlaymode();
        void setPlaymode(int playmode);
}
