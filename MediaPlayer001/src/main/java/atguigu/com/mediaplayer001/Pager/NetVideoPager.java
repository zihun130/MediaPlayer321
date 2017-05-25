package atguigu.com.mediaplayer001.Pager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import atguigu.com.mediaplayer001.Adapter.NetVideoAdapter;
import atguigu.com.mediaplayer001.BaseFragment;
import atguigu.com.mediaplayer001.Media.SystemNetVideoMedia;
import atguigu.com.mediaplayer001.R;
import atguigu.com.mediaplayer001.domain.MediaItem;
import atguigu.com.mediaplayer001.domain.MoiveInfo;

/**
 * Created by sun on 2017/5/25.
 */

public class NetVideoPager extends BaseFragment {
    private ListView lv_net_video;
    private TextView tv_nodatas;
    private List<MoiveInfo.TrailersBean> trailers;
    private NetVideoAdapter adapter;
    private ArrayList<MediaItem> mediaItem;

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.fragment_net_video_pager,null);
        lv_net_video = (ListView) view.findViewById(R.id.lv_net_video);
        tv_nodatas = (TextView) view.findViewById(R.id.tv_nodatas);
        
        lv_net_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, SystemNetVideoMedia.class);
                Bundle  bundle=new Bundle();
                bundle.putSerializable("videoList",mediaItem);
                intent.putExtra("position",position);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void initData() {
         super.initData();
        getData();

    }

    private void getData() {
               final RequestParams request = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
                x.http().get(request,new Callback.CommonCallback<String>(){
                    @Override
                    public void onSuccess(String s) {
                        Log.e("TAG","联网成功="+s);
                        processData(s);
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        Log.e("TAG","联网失败=");
                    }

                    @Override
                    public void onCancelled(CancelledException e) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }

    private void processData(String json) {
        Gson gson = new Gson();
        MoiveInfo moiveInfo = gson.fromJson(json, MoiveInfo.class);
        trailers = moiveInfo.getTrailers();
        if(trailers!=null && trailers.size()>0){
            tv_nodatas.setVisibility(View.GONE);

            mediaItem = new ArrayList<>();
            for(int i = 0; i <trailers.size() ; i++) {
                MoiveInfo.TrailersBean trailersBean = trailers.get(i);
                String name=trailersBean.getMovieName();
                long  duration=trailersBean.getVideoLength();
                String title=trailersBean.getVideoTitle();
                String data=trailersBean.getUrl();

                mediaItem.add(new MediaItem(name,duration,title,data));
            }

            adapter=new NetVideoAdapter(context,trailers);
            lv_net_video.setAdapter(adapter);

        }else {
            tv_nodatas.setVisibility(View.VISIBLE);
        }



    }
}
