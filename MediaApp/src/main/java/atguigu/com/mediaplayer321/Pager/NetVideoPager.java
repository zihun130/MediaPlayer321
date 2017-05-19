package atguigu.com.mediaplayer321.Pager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import atguigu.com.mediaplayer321.fragment.BaseFragment;

/**
 * Created by sun on 2017/5/19.
 */

public class NetVideoPager extends BaseFragment {
    private TextView textView;
    @Override
    public View initview() {
        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initdata() {
        super.initdata();
        textView.setText("网络视频的内容");
    }
}
