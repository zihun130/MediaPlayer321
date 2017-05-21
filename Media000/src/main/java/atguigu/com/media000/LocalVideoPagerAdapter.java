package atguigu.com.media000;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sun on 2017/5/21.
 */

public class LocalVideoPagerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MediaItems> mediaItem;
    private Utils utils;
    public LocalVideoPagerAdapter(Context context, ArrayList<MediaItems> mediaItem) {
        this.context=context;
        this.mediaItem=mediaItem;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return mediaItem==null ? 0 : mediaItem.size();
    }

    @Override
    public MediaItems getItem(int position) {
        return mediaItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.localvideo_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_duration= (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.tv_size= (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        MediaItems mediaItems = mediaItem.get(position);

        viewHolder.tv_name.setText(mediaItems.getName());
        viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItems.getSize()));
        viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItems.getDuration()));

        return convertView;
    }

    static class ViewHolder{
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;

    }
}
