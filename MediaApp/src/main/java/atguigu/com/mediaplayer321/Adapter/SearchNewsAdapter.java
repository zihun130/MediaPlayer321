package atguigu.com.mediaplayer321.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xutils.image.ImageOptions;

import java.util.List;

import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.SearchBean;

/**
 * Created by sun on 2017/5/22.
 */

public class SearchNewsAdapter extends BaseAdapter {
    private Context context;
    private List<SearchBean.ItemsBean> datas;
    private ImageOptions imageOptions;
    public SearchNewsAdapter(Context context, List<SearchBean.ItemsBean> datas) {
        this.context=context;
        this.datas=datas;
        imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setFailureDrawableId(R.drawable.video_default)
                .setLoadingDrawableId(R.drawable.video_default)
                .build();

    }


    @Override
    public int getCount() {
        return datas==null ? 0 : datas.size();
    }

    @Override
    public SearchBean.ItemsBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.netvideo_item,null);
            viewHolder=new ViewHolder();
            viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_net_icon);
            viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_duration= (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.tv_size= (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        SearchBean.ItemsBean trailersBean = datas.get(position);
        viewHolder.tv_name.setText(trailersBean.getItemTitle());
        viewHolder.tv_size.setText(trailersBean.getDatecheck());
        viewHolder.tv_duration.setText(trailersBean.getKeywords());
        //x.image().bind(viewHolder.iv_icon,trailersBean.getCoverImg(),imageOptions);
        Picasso.with(context)
                .load(trailersBean.getItemImage().getImgUrl1())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.iv_icon);


        return convertView;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
    }
}
