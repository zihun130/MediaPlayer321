package atguigu.com.mediaplayer321.Adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.MediaItem;
import atguigu.com.mediaplayer321.utils.Utils;

/**
 * Created by sun on 2017/5/19.
 */

public class LocalVideoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MediaItem> mediaItems;
    private Utils  utils;
    private boolean isVideo;
    public LocalVideoAdapter(Context context, ArrayList<MediaItem> mediaItems,boolean b) {
        this.context=context;
        this.mediaItems=mediaItems;
        this.isVideo=b;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return mediaItems==null ? 0 : mediaItems.size();
    }

    @Override
    public MediaItem getItem(int position) {
        return mediaItems.get(position);
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

            viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_duration= (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.tv_size= (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
        viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));

        if(!isVideo){
            viewHolder.iv_icon.setImageResource(R.drawable.music_default_bg);

        }

        return convertView;
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;

    }
}
