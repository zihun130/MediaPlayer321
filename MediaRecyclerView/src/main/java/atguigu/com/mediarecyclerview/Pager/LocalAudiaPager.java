package atguigu.com.mediarecyclerview.Pager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import atguigu.com.mediarecyclerview.Fragment.BaseFragment;

/**
 * Created by sun on 2017/5/20.
 */

public class LocalAudiaPager extends BaseFragment {
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
        textView.setText("本地音频内容");
    }
}
