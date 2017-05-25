package atguigu.com.mediaplayer001.Pager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import atguigu.com.mediaplayer001.BaseFragment;

/**
 * Created by sun on 2017/5/25.
 */

public class LocalAudioPager extends BaseFragment {
    private TextView textView;
    @Override
    public View initView() {
       textView = new TextView(context);
        textView.setTextSize(40);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        textView.setText("本地音频内容");

    }
}
