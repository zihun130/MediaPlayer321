package atguigu.com.media005;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sun on 2017/5/20.
 */

public class NetVideoPager extends BaseFragment {
    private TextView textView;
    @Override
    protected View initView() {
        textView=new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(40);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    protected void initData() {
        textView.setText("网络视频内容");
    }
}
