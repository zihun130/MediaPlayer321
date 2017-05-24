package atguigu.com.mediaplayer321.Pager;

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

import atguigu.com.mediaplayer321.Adapter.LocalVideoAdapter;
import atguigu.com.mediaplayer321.Media.SystemView;
import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.MediaItem;
import atguigu.com.mediaplayer321.fragment.BaseFragment;

/**
 * Created by sun on 2017/5/19.
 */

public class LocalVideoPager extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView lv_local_video_pager;
    private TextView tv_nodata;
    private ArrayList<MediaItem>  mediaItems;
    private LocalVideoAdapter adapter;

    @Override
    public View initview() {
       View view= View.inflate(context,R.layout.fragment_local_video_pager,null);
        lv_local_video_pager = (ListView) view.findViewById(R.id.lv_local_video_pager);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        lv_local_video_pager.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void initdata() {
        super.initdata();
        getData();

    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems!=null && mediaItems.size()>0){

                tv_nodata.setVisibility(View.GONE);

                adapter = new LocalVideoAdapter(context,mediaItems,true);
                lv_local_video_pager.setAdapter(adapter);
            }else{
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA
                };
                Cursor cursor=resolver.query(uri,objs,null,null,null);
                if(cursor!=null){
                    while (cursor.moveToNext()){
                        String name=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        long   duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long   size=cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        String data=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                        mediaItems.add(new MediaItem(name,duration,size,data));

                    }
                    cursor.close();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //MediaItem item = (MediaItem) adapter.getItem(position);
        Intent intent=new Intent(context,SystemView.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("videoList",mediaItems);
        intent.putExtra("position",position);
        intent.putExtras(bundle);
        //intent.setDataAndType(Uri.parse(item.getData()),"video/*");
        startActivity(intent);
    }

}
