package atguigu.com.media002;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sun on 2017/5/19.
 */

public class LocalVideoPager extends BaseFragment{
    private TextView textView;
    @Override
    protected View initView() {
        textView=new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(40);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    protected void initData() {
        textView.setText("本地视频内容");

    }
}
