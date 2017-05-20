package atguigu.com.media005;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sun on 2017/5/20.
 */

public class LocalVideoPagerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MedioItems> medioitem;
    private Utils  utils;
    public LocalVideoPagerAdapter(Context context, ArrayList<MedioItems> medioitem) {
        this.context=context;
        this.medioitem=medioitem;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return medioitem==null ? 0 : medioitem.size();
    }

    @Override
    public MedioItems getItem(int position) {
        return medioitem.get(position);
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

        MedioItems medioItems = medioitem.get(position);
        viewHolder.tv_name.setText(medioItems.getName());
        viewHolder.tv_size.setText(Formatter.formatFileSize(context,medioItems.getSize()));
        viewHolder.tv_duration.setText(utils.stringForTime((int) medioItems.getDuration()));

        return convertView;
    }

    static class ViewHolder{
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;

    }
}
