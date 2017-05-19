package atguigu.com.media000;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;



public class LocalVideoPager extends BaseFragment {
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
        textView.setText("本地视频内容");

    }
}
