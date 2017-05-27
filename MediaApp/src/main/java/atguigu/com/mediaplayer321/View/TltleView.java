package atguigu.com.mediaplayer321.View;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import atguigu.com.mediaplayer321.Media.SpeechVoiceActivity;
import atguigu.com.mediaplayer321.R;

/**
 * Created by sun on 2017/5/19.
 */

public class TltleView extends LinearLayout implements View.OnClickListener {
    private Context context;
    private TextView tv_sousuo;
    private RelativeLayout rl_game;
    private ImageView iv_record;

    public TltleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_sousuo= (TextView) getChildAt(1);
        rl_game= (RelativeLayout) getChildAt(2);
        iv_record= (ImageView) getChildAt(3);

        tv_sousuo.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sousuo :
                Intent  intent=new Intent(context,SpeechVoiceActivity.class);
                context.startActivity(intent);

                break;

            case R.id.rl_game :
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();

                break;

            case R.id.iv_record :
                Toast.makeText(context, "记录", Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
