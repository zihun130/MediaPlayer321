package atguigu.com.media001;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sun on 2017/5/19.
 */

public class LocalAudioPager extends BaseFragment {
    private TextView textView;
    @Override
    protected View initview() {
        textView=new TextView(context);
        textView.setTextSize(40);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    protected void initdata() {
        textView.setText("本地音频内容");
    }
}
