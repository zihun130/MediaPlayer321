package atguigu.com.mediaplayer321.Pager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import atguigu.com.mediaplayer321.Adapter.NetVideoAdapter;
import atguigu.com.mediaplayer321.Media.SystemView;
import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.MediaItem;
import atguigu.com.mediaplayer321.domain.MoiveInfo;
import atguigu.com.mediaplayer321.fragment.BaseFragment;

/**
 * Created by sun on 2017/5/19.
 */

public class NetVideoPager extends BaseFragment {
    private ListView lv_net_video_pager;
    private TextView tv_nodata;
    private NetVideoAdapter adapter;
    private ArrayList<MediaItem> mediaItems;
    private MaterialRefreshLayout materialRefreshLayout;
    private boolean isloadMore = false;
    private List<MoiveInfo.TrailersBean> trailers;
    private SharedPreferences  sp;
    private static final String uri="http://api.m.mtime.cn/PageSubArea/TrailerList.api";

    @Override
    public View initview() {
        View view = View.inflate(context, R.layout.fragment_net_video_pager, null);
        lv_net_video_pager = (ListView) view.findViewById(R.id.lv_net_video_pager);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        sp=context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);


        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

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

        lv_net_video_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //MoiveInfo.TrailersBean item = adapter.getItem(position);

                Intent intent = new Intent(context, SystemView.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoList", mediaItems);
                intent.putExtra("position", position);
                intent.putExtras(bundle);
                //intent.setDataAndType(Uri.parse(item.getUrl()),"video/*");
                startActivity(intent);

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
                processedata(s);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(uri,s);
                edit.commit();
                materialRefreshLayout.finishRefresh();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                sp.getString("Json",null);


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
        MoiveInfo moiveInfo = new Gson().fromJson(json, MoiveInfo.class);

        if(!isloadMore){
            trailers = moiveInfo.getTrailers();
            if (trailers != null && trailers.size() > 0) {
                tv_nodata.setVisibility(View.GONE);

                mediaItems = new ArrayList<MediaItem>();
                for (int i = 0; i < trailers.size(); i++) {
                    MoiveInfo.TrailersBean bean = trailers.get(i);

                    mediaItems.add(new MediaItem(bean.getMovieName(), bean.getVideoLength(), 0, bean.getUrl()));
                }

                adapter = new NetVideoAdapter(context, trailers);
                lv_net_video_pager.setAdapter(adapter);

            } else {
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }else {
            List<MoiveInfo.TrailersBean> datas = moiveInfo.getTrailers();
            if (datas != null && datas.size() > 0) {
                tv_nodata.setVisibility(View.GONE);

                mediaItems = new ArrayList<MediaItem>();
                for (int i = 0; i < datas.size(); i++) {
                    MoiveInfo.TrailersBean bean = datas.get(i);

                    mediaItems.add(new MediaItem(bean.getMovieName(), bean.getVideoLength(), 0, bean.getUrl()));
                }
                trailers.addAll(datas);
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
