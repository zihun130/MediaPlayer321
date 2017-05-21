package atguigu.com.media000.mediapager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import atguigu.com.media000.BaseFragment;

/**
 * Created by sun on 2017/5/19.
 */

public class NetVideoPager extends BaseFragment {
    private TextView textView;
    @Override
    public View initview() {
        textView=new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(40);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initdata() {
        textView.setText("网络视频内容");
    }
}
