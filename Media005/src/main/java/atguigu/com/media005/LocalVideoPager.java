package atguigu.com.media005;

import android.content.ContentResolver;
import android.content.Intent;
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

/**
 * Created by sun on 2017/5/20.
 */

public class LocalVideoPager extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView  lv_local_video_pager;
    private TextView  tv_nodata;
    private ArrayList<MedioItems> medioitem;
    private LocalVideoPagerAdapter adapter;

    @Override
    protected View initView() {
        View view=View.inflate(context,R.layout.fragment_local_video_pager,null);
        lv_local_video_pager= (ListView) view.findViewById(R.id.lv_local_video_pager);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        lv_local_video_pager.setOnItemClickListener(this);
        return view;
    }

    @Override
    protected void initData() {
        getData();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(medioitem!=null && medioitem.size()>0){

                tv_nodata.setVisibility(View.GONE);

                adapter = new LocalVideoPagerAdapter(context,medioitem);
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
                 medioitem = new ArrayList<>();
                ContentResolver resolver = context.getContentResolver();

                Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA
                };
                Cursor cursor=resolver.query(uri,objs,null,null,null);
                while (cursor.moveToNext()){
                    String name=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    long   duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    long   size=cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                    String data=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                    medioitem.add(new MedioItems(name,duration,size,data));
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MedioItems item = (MedioItems) adapter.getItem(position);
        Intent intent=new Intent(context,SystemView.class);
        intent.setDataAndType(Uri.parse(item.getData()),"video/*");
        startActivity(intent);
    }

}
