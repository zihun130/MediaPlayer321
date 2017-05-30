package atguigu.com.mediarecyclerview.Pager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import atguigu.com.mediarecyclerview.Fragment.BaseFragment;
import atguigu.com.mediarecyclerview.R;

/**
 * Created by sun on 2017/5/30.
 */

public class RecyclerViewPager extends BaseFragment {
    private ListView lv_recycler_pager;
    private TextView recycler_dataerror;
    private ProgressBar recycler_progressbar;
    private SharedPreferences sp;
    private static final String uri="http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_recycler_pager, null);
        lv_recycler_pager = (ListView) view.findViewById(R.id.lv_recycler_pager);
        recycler_dataerror = (TextView) view.findViewById(R.id.recycler_dataerror);
        recycler_progressbar = (ProgressBar) view.findViewById(R.id.recycler_progressbar);
        sp=context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);

        return view;
    }

    @Override
    protected void initData() {
        String string = sp.getString(uri, "");
        if(!TextUtils.isEmpty(string)){
            processedata(string);
        }

        getData();
    }

    private void getData() {
        final RequestParams request = new RequestParams(uri);
        x.http().get(request, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                recycler_progressbar.setVisibility(View.GONE);
                Log.e("TAG","String"+s);
                processedata(s);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(uri,s);
                edit.commit();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Log.e("TAG","开那个网失败");

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processedata(String json) {

    }

}
