package atguigu.com.media001;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sun on 2017/5/19.
 */

public class LocalVideoPager extends BaseFragment {
    private ListView lv_local_video_pager;
    private TextView tv_nodata;
    private ArrayList<MediaItems> mediaItem;
    private LocalVideoPagerAdapter adapter;
    @Override
    protected View initview() {
        View view=View.inflate(context,R.layout.fragment_local_video_pager,null);
        lv_local_video_pager= (ListView) view.findViewById(R.id.lv_local_video_pager);
        tv_nodata= (TextView) view.findViewById(R.id.tv_nodata);
        lv_local_video_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,SystemVideoView.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("videoList",mediaItem);
                intent.putExtra("position",position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }
    
    
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItem!=null && mediaItem.size()>0){
                tv_nodata.setVisibility(View.GONE);
                adapter=new LocalVideoPagerAdapter(context,mediaItem);
                lv_local_video_pager.setAdapter(adapter);
            }else {
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void initdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaItem = new ArrayList<>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA
                };
                Cursor cursor = resolver.query(uri,objs,null,null,null);
                if(cursor!=null){
                    while (cursor.moveToNext()){
                       String name= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                       long   duration= cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                       long   size= cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                       String data= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        mediaItem.add(new MediaItems(name, duration,size,data));
                        handler.sendEmptyMessage(0);
                    }
                }
                

            }
        }).start();

    }
}
