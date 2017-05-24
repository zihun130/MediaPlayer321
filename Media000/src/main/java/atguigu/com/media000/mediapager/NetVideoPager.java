package atguigu.com.media000.mediapager;

import android.content.Intent;
import android.net.Uri;
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

import atguigu.com.media000.BaseFragment;
import atguigu.com.media000.MediaItems;
import atguigu.com.media000.MoiveInfo;
import atguigu.com.media000.NetVideoAdapter;
import atguigu.com.media000.R;
import atguigu.com.media000.SystemMediaPlayer;

/**
 * Created by sun on 2017/5/19.
 */

public class NetVideoPager extends BaseFragment {
    private ListView lv_local_video_pager;
    private TextView tv_nodata;
    private NetVideoAdapter adapter;
    private MaterialRefreshLayout materialRefreshLayout;
    private boolean isloadingmore=false;
    private List<MoiveInfo.TrailersBean> trailers;
    private ArrayList<MediaItems> mediaItem;

    @Override
    public View initview() {
        View view=View.inflate(context, R.layout.fragment_net_video_pager,null);
        lv_local_video_pager = (ListView) view.findViewById(R.id.lv_local_video_pager);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                isloadingmore=false;
                getData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                isloadingmore=true;
                getloadmore();

            }
        });


        lv_local_video_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoiveInfo.TrailersBean item = adapter.getItem(position);
                Intent intent=new Intent(context, SystemMediaPlayer.class);
                intent.setDataAndType(Uri.parse(item.getUrl()),"Video/*");
                startActivity(intent);

            }
        });
        return view;
    }



    @Override
    public void initdata() {
        getData();
    }

    private void getData() {
        RequestParams params = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(params,new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String s) {
                processeddata(s);
                materialRefreshLayout.finishRefresh();
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


    private void getloadmore() {
        RequestParams params = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(params,new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String s) {
                processeddata(s);
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

    private void processeddata(String json) {
        MoiveInfo moiveInfo = new Gson().fromJson(json, MoiveInfo.class);

        if(!isloadingmore){
            trailers = moiveInfo.getTrailers();
            if(trailers!=null && trailers.size()>0){
                tv_nodata.setVisibility(View.GONE);

                 mediaItem = new ArrayList<>();
                for(int i = 0; i < trailers.size(); i++) {
                    MoiveInfo.TrailersBean trailersBean = trailers.get(i);
                }

                adapter=new NetVideoAdapter(context,trailers);
                lv_local_video_pager.setAdapter(adapter);

            }else {
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }else {
            List<MoiveInfo.TrailersBean> bean = moiveInfo.getTrailers();
            if(bean!=null && bean.size()>0){

            }

        }


    }
}
