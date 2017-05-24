package atguigu.com.mediaplayer321.Media;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import atguigu.com.mediaplayer321.R;

public class SystemAudioView extends AppCompatActivity {
   private ImageView iv_audio_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_audio_view);
        iv_audio_icon = (ImageView)findViewById(R.id.iv_audio_icon);
        iv_audio_icon.setBackgroundResource(R.drawable.animation_bg);
        AnimationDrawable background = (AnimationDrawable) iv_audio_icon.getBackground();
        background.start();
    }
}
