package atguigu.com.mediaplayer321.Pager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import atguigu.com.mediaplayer321.Adapter.NetAudioAdapter;
import atguigu.com.mediaplayer321.Media.ShowTimeActivity;
import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.NetAudioInfo;
import atguigu.com.mediaplayer321.fragment.BaseFragment;
import io.vov.vitamio.utils.Log;

/**
 * Created by sun on 2017/5/19.
 */

public class NetAudioPager extends BaseFragment {
    private ListView lv_net_audio_pager;
    private TextView tv_dataerror;
    private ProgressBar audio_progressbar;
    private NetAudioAdapter adapter;
    private MaterialRefreshLayout materialRefreshLayout;
    private boolean isloadMore = false;
    private List<NetAudioInfo.ListBean> list;
    private SharedPreferences sp;
    private static final String uri="http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8\";";

    @Override
    public View initview() {
        View view = View.inflate(context, R.layout.fragment_net_audio_pager, null);
        lv_net_audio_pager = (ListView) view.findViewById(R.id.lv_net_audio_pager);
        tv_dataerror = (TextView) view.findViewById(R.id.tv_dataerror);
        audio_progressbar = (ProgressBar) view.findViewById(R.id.audio_progressbar);
        sp=context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);

        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.jj_refresh);

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

        lv_net_audio_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NetAudioInfo.ListBean listBean = list.get(position);
                if(listBean!=null){
                    Intent intent=new Intent(context,ShowTimeActivity.class);
                    if(listBean.getType().equals("image")){
                        String url = listBean.getImage().getBig().get(0);
                        intent.putExtra("url",url);
                        context.startActivity(intent);
                    }else if(listBean.getType().equals("gif")){
                        String url = listBean.getGif().getImages().get(0);
                        intent.putExtra("url",url);
                        context.startActivity(intent);
                    }
                }

            }
        });

        return view;
    }

    @Override
    public void initdata() {
        super.initdata();
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
                audio_progressbar.setVisibility(View.GONE);
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
        NetAudioInfo netAudioInfo = new Gson().fromJson(json, NetAudioInfo.class);

        if(!isloadMore){
            list = netAudioInfo.getList();

            Log.e("TAG","list"+list);

            if (list != null && list.size() > 0) {

                adapter = new NetAudioAdapter(context, list);
                lv_net_audio_pager.setAdapter(adapter);

            } else {
                tv_dataerror.setVisibility(View.VISIBLE);
            }
        }else {
            List<NetAudioInfo.ListBean> datas = netAudioInfo.getList();
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
