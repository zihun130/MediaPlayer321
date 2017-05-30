package atguigu.com.mediarecyclerview.Pager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import atguigu.com.mediarecyclerview.Adapter.RecyclerViewAdapter;
import atguigu.com.mediarecyclerview.Fragment.BaseFragment;
import atguigu.com.mediarecyclerview.R;
import atguigu.com.mediarecyclerview.domain.RecyclerViewInfo;

/**
 * Created by sun on 2017/5/30.
 */

public class RecyclerViewPager extends BaseFragment {
    private RecyclerView lv_recycler_pager;
    private TextView recycler_dataerror;
    private ProgressBar recycler_progressbar;
    private RecyclerViewAdapter adapter;
    private MaterialRefreshLayout materialRefreshLayout;
    private boolean isloadMore = false;
    private List<RecyclerViewInfo.ListBean> list;
    private SharedPreferences sp;
    private static final String uri="http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_recycler_pager, null);
        lv_recycler_pager = (RecyclerView) view.findViewById(R.id.lv_recycler_pager);
        recycler_dataerror = (TextView) view.findViewById(R.id.recycler_dataerror);
        recycler_progressbar = (ProgressBar) view.findViewById(R.id.recycler_progressbar);
        sp=context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);

        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.recycler_refresh);

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                isloadMore = false;
                getData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                isloadMore = true;
                getLoadMore();

            }
        });

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
                materialRefreshLayout.finishRefresh();
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
        RecyclerViewInfo netAudioInfo = new Gson().fromJson(json, RecyclerViewInfo.class);

        if(!isloadMore){
            list = netAudioInfo.getList();

            Log.e("TAG","list"+list);

            if (list != null && list.size() > 0) {

                adapter = new RecyclerViewAdapter(context, list);
                lv_recycler_pager.setAdapter(adapter);

            } else {
                recycler_dataerror.setVisibility(View.VISIBLE);
            }
        }else {
            List<RecyclerViewInfo.ListBean> datas = netAudioInfo.getList();
            if (datas != null && datas.size() > 0) {

                list.addAll(datas);
                adapter.notifyDataSetChanged();
            }

        }

    }


    private void getLoadMore() {
        final RequestParams request = new RequestParams(uri);
        x.http().get(request, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                processedata(s);
                materialRefreshLayout.finishRefreshLoadMore();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });

    }



}
