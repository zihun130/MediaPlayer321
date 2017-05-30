package atguigu.com.mediarecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.x;

import java.util.List;

import atguigu.com.mediarecyclerview.R;
import atguigu.com.mediarecyclerview.domain.RecyclerViewInfo;

import static android.media.tv.TvTrackInfo.TYPE_VIDEO;

/**
 * Created by sun on 2017/5/30.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private static final int TYPE_RECYCLER_VIDEO = 0;
    private static final int TYPE_RECYCLER_IMAGE = 1;
    private static final int TYPE_RECYCLER_TEXT = 2;
    private static final int TYPE_RECYCLER_GIF = 3;
    private static final int TYPE_RECYCLER_HTML = 4;
    private Context context;
    private List<RecyclerViewInfo.ListBean> list;
    public RecyclerViewAdapter(Context context, List<RecyclerViewInfo.ListBean> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return initViewHolder(viewType);
    }

    private RecyclerView.ViewHolder initViewHolder(int itemviewType) {
        RecyclerView.ViewHolder   viewHolder=null;
        View convertView = null;
        switch (itemviewType) {
            case TYPE_RECYCLER_VIDEO :
                convertView = View.inflate(context, R.layout.videoholder_item, null);
                viewHolder = new VideoHoder(convertView);

                break;
            case TYPE_RECYCLER_IMAGE :
                convertView = View.inflate(context, R.layout.imageholder_item, null);
                viewHolder = new ImageHolder(convertView);

                break;
            case TYPE_RECYCLER_TEXT :
                convertView = View.inflate(context, R.layout.textholder_item, null);
                viewHolder = new TextHolder(convertView);

                break;
            case TYPE_RECYCLER_GIF :
                convertView = View.inflate(context, R.layout.gifholder_item, null);
                viewHolder = new GifHolder(convertView);

                break;
            case TYPE_RECYCLER_HTML :
                convertView = View.inflate(context, R.layout.htmlholder_item, null);
                viewHolder = new HtmlHolder(convertView);

                break;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_RECYCLER_VIDEO) {
            VideoHoder videoHoder = (VideoHoder) holder;
            videoHoder.setData(datas.get(position));
        } else if (getItemViewType(position) == TYPE_RECYCLER_IMAGE) {
            ImageHolder imageHolder = (ImageHolder) holder;
            imageHolder.setData(datas.get(position));
        } else if (getItemViewType(position) == TYPE_RECYCLER_TEXT) {
            TextHolder textHolder = (TextHolder) holder;
            textHolder.setData(datas.get(position));
        } else if (getItemViewType(position) == TYPE_RECYCLER_GIF) {
            GifHolder gifHolder = (GifHolder) holder;
            gifHolder.setData(datas.get(position));
        } else {
            ADHolder adHolder = (ADHolder) holder;
            adHolder.setData(datas.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    class BaseViewHolder extends RecyclerView.ViewHolder{
        ImageView ivHeadpic;
        TextView tvName;
        TextView tvTimeRefresh;
        ImageView ivRightMore;
        ImageView ivVideoKind;
        TextView tvVideoKindText;
        TextView tvShenheDingNumber;
        TextView tvShenheCaiNumber;
        TextView tvPostsNumber;
        LinearLayout llDownload;

        public BaseViewHolder(View itemView) {
            super(itemView);

            ivHeadpic = (ImageView) itemView.findViewById(R.id.iv_headpic);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTimeRefresh = (TextView) itemView.findViewById(R.id.tv_time_refresh);
            ivRightMore = (ImageView) itemView.findViewById(R.id.iv_right_more);

            ivVideoKind = (ImageView) itemView.findViewById(R.id.iv_video_kind);
            tvVideoKindText = (TextView) itemView.findViewById(R.id.tv_video_kind_text);
            tvShenheDingNumber = (TextView) itemView.findViewById(R.id.tv_shenhe_ding_number);
            tvShenheCaiNumber = (TextView) itemView.findViewById(R.id.tv_shenhe_cai_number);
            tvPostsNumber = (TextView) itemView.findViewById(R.id.tv_posts_number);
            llDownload = (LinearLayout) itemView.findViewById(R.id.ll_download);
        }
        public void setData(RecyclerViewInfo.ListBean mediaItem) {
            if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
                x.image().bind(ivHeadpic, mediaItem.getU().getHeader().get(0));
            }
            if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
                tvName.setText(mediaItem.getU().getName() + "");
            }

            tvTimeRefresh.setText(mediaItem.getPasstime());

            List<RecyclerViewInfo.ListBean.TagsBean> tagsEntities = mediaItem.getTags();
            if (tagsEntities != null && tagsEntities.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < tagsEntities.size(); i++) {
                    buffer.append(tagsEntities.get(i).getName() + "");
                }
                tvVideoKindText.setText(buffer.toString());
            }

            tvShenheDingNumber.setText(mediaItem.getUp());
            tvShenheCaiNumber.setText(mediaItem.getDown() + "");
            tvPostsNumber.setText(mediaItem.getForward() + "");

        }

    }




}
