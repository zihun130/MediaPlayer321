package atguigu.com.media000.mediapager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import atguigu.com.media000.BaseFragment;
import atguigu.com.media000.LocalVideoPagerAdapter;
import atguigu.com.media000.MediaItems;
import atguigu.com.media000.R;


public class LocalVideoPager extends BaseFragment {
    private ListView lv_local_video_pager;
    private TextView tv_nodata;
    private ArrayList<MediaItems> mediaItem;
    private LocalVideoPagerAdapter adapter;
    @Override
    public View initview() {
        View view=View.inflate(context, R.layout.fragment_local_video_pager,null);
        lv_local_video_pager = (ListView) view.findViewById(R.id.lv_local_video_pager);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        lv_local_video_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaItems item = adapter.getItem(position);
            }
        });
        return view;
    }

    @Override
    public void initdata() {
        getData();
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

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaItem = new ArrayList<>();
                ContentResolver resolver=context.getContentResolver();
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
                        mediaItem.add(new MediaItems(name,duration,size,data));

                        handler.removeMessages(0);
                    }
                    cursor.close();
                }

            }
        }).start();
    }
}
