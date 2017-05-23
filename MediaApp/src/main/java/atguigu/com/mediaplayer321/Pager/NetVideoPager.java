package atguigu.com.mediaplayer321.Pager;

import android.content.Intent;
import android.os.Bundle;
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

import atguigu.com.mediaplayer321.Adapter.NetVideoAdapter;
import atguigu.com.mediaplayer321.Media.VitamioSystemView;
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
    @Override
    public View initview() {
        View view=View.inflate(context, R.layout.fragment_net_video_pager,null);
        lv_net_video_pager = (ListView) view.findViewById(R.id.lv_net_video_pager);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        lv_net_video_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //MoiveInfo.TrailersBean item = adapter.getItem(position);

                Intent intent=new Intent(context, VitamioSystemView.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("videoList",mediaItems);
                intent.putExtra("position",position);
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
        getData();
    }

    private void getData() {
       final RequestParams request = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(request,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String s) {
                processedata(s);
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

    private void processedata(String json) {
        MoiveInfo moiveInfo = new Gson().fromJson(json, MoiveInfo.class);
        List<MoiveInfo.TrailersBean> trailers = moiveInfo.getTrailers();

        if(trailers!=null && trailers.size()>0){
            tv_nodata.setVisibility(View.GONE);

            mediaItems = new ArrayList<MediaItem>();
            for(int i = 0; i < trailers.size(); i++) {
                MoiveInfo.TrailersBean bean = trailers.get(i);

                mediaItems.add(new MediaItem(bean.getMovieName(),bean.getVideoLength(),0,bean.getUrl()));
            }

            adapter=new NetVideoAdapter(context,trailers);
            lv_net_video_pager.setAdapter(adapter);

        }else {
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }
}
