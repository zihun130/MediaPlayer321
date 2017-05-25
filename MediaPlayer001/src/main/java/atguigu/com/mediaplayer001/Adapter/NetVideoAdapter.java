package atguigu.com.mediaplayer001.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import atguigu.com.mediaplayer001.R;
import atguigu.com.mediaplayer001.domain.MoiveInfo;

/**
 * Created by sun on 2017/5/25.
 */

public class NetVideoAdapter extends BaseAdapter{
    private Context context;
    private List<MoiveInfo.TrailersBean> trailers;
    public NetVideoAdapter(Context context, List<MoiveInfo.TrailersBean> trailers) {
        this.context=context;
        this.trailers=trailers;
    }

    @Override
    public int getCount() {
        return trailers==null ? 0 : trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=View.inflate(context, R.layout.net_video_item,null);
            viewHolder.net_icon= (ImageView) convertView.findViewById(R.id.net_icon);
            viewHolder.tv_videoname= (TextView) convertView.findViewById(R.id.tv_videoname);
            viewHolder.tv_videotitle= (TextView) convertView.findViewById(R.id.tv_videotitle);
            viewHolder.tv_videotime= (TextView) convertView.findViewById(R.id.tv_videotime);

            convertView.setTag(viewHolder);

        }else {

            viewHolder= (ViewHolder) convertView.getTag();
        }

        MoiveInfo.TrailersBean trailersBean = trailers.get(position);

        viewHolder.tv_videoname.setText(trailersBean.getMovieName());
        viewHolder.tv_videotitle.setText(trailersBean.getVideoTitle());
        viewHolder.tv_videotime.setText(trailersBean.getVideoLength()+"秒");

        Picasso.with(context)
                .load(trailersBean.getCoverImg())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.net_icon);

        return convertView;
    }

    static class ViewHolder{
        ImageView net_icon;
        TextView tv_videoname;
        TextView tv_videotitle;
        TextView tv_videotime;
    }
}
