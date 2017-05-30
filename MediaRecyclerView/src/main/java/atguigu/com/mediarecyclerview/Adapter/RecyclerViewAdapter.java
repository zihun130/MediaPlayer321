package atguigu.com.mediarecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import atguigu.com.mediarecyclerview.R;
import atguigu.com.mediarecyclerview.domain.RecyclerViewInfo;
import atguigu.com.mediarecyclerview.utils.Utils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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
                viewHolder = new VideoHolder(convertView);

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
            VideoHolder videoHoder = (VideoHolder) viewHolder;
            videoHoder.setData(list.get(position));
        } else if (getItemViewType(position) == TYPE_RECYCLER_IMAGE) {
            ImageHolder imageHolder = (ImageHolder) viewHolder;
            imageHolder.setData(list.get(position));
        } else if (getItemViewType(position) == TYPE_RECYCLER_TEXT) {
            TextHolder textHolder = (TextHolder) viewHolder;
            textHolder.setData(list.get(position));
        } else if (getItemViewType(position) == TYPE_RECYCLER_GIF) {
            GifHolder gifHolder = (GifHolder) viewHolder;
            gifHolder.setData(list.get(position));
        } else {
            HtmlHolder htmlHolder = (HtmlHolder) viewHolder;
            htmlHolder.setData(list.get(position));
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


    class VideoHolder extends BaseViewHolder {
        Utils utils;
        TextView tvContext;
        JCVideoPlayerStandard jcvVideoplayer;
        TextView tvPlayNums;
        TextView tvVideoDuration;
        ImageView ivCommant;
        TextView tvCommantContext;


        public VideoHolder(View itemView) {
            super(itemView);

            tvContext = (TextView) itemView.findViewById(R.id.tv_context);
            utils = new Utils();
            tvPlayNums = (TextView) itemView.findViewById(R.id.tv_play_nums);
            tvVideoDuration = (TextView) itemView.findViewById(R.id.tv_video_duration);
            ivCommant = (ImageView) itemView.findViewById(R.id.iv_commant);
            tvCommantContext = (TextView) itemView.findViewById(R.id.tv_commant_context);
            jcvVideoplayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.jcv_videoplayer);

        }

        public void setData(RecyclerViewInfo.ListBean mediaItem) {
            super.setData(mediaItem);

            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
            //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
            boolean setUp = jcvVideoplayer.setUp(
                    mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    "");

            if (setUp) {
            Glide.with(context).load(mediaItem.getVideo().getThumbnail().get(0)).into(jcvVideoplayer.thumbImageView);
            }
            tvPlayNums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
            tvVideoDuration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");

        }
    }

    class ImageHolder extends BaseViewHolder {
        TextView tvContext;
        ImageView ivImageIcon;

        public ImageHolder(View itemView) {
            super(itemView);
            tvContext = (TextView) itemView.findViewById(R.id.tv_context);
            ivImageIcon = (ImageView) itemView.findViewById(R.id.iv_image_icon);
        }

        public void setData(RecyclerViewInfo.ListBean mediaItem) {
            super.setData(mediaItem);

            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
           //图片特有的
            ivImageIcon.setImageResource(R.drawable.bg_item);
            if (mediaItem.getImage() != null && mediaItem.getImage() != null && mediaItem.getImage().getSmall() != null) {
                Glide.with(context).load(mediaItem.getImage().getDownload_url().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImageIcon);
            }

        }
    }


    class TextHolder extends BaseViewHolder {
        TextView tvContext;

        public TextHolder(View itemView) {
            super(itemView);
            tvContext = (TextView) itemView.findViewById(R.id.tv_context);
        }

        public void setData(RecyclerViewInfo.ListBean mediaItem) {
            super.setData(mediaItem);
//设置文本-所有的都有
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
        }

    }


    class GifHolder extends BaseViewHolder {
        TextView tvContext;
        ImageView ivImageGif;
        private ImageOptions imageOptions;

        public GifHolder(View itemView) {
            super(itemView);
            tvContext = (TextView) itemView.findViewById(R.id.tv_context);
            imageOptions = new ImageOptions.Builder()
                    .setSize(ViewGroup.LayoutParams.WRAP_CONTENT, -2)
//设置圆角
                    .setRadius(DensityUtil.dip2px(5))
                    .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.video_default)
                    .setFailureDrawableId(R.drawable.video_default)
                    .build();

        }

        public void setData(RecyclerViewInfo.ListBean mediaItem) {
            super.setData(mediaItem);

            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
            if (mediaItem.getGif() != null && mediaItem.getGif() != null && mediaItem.getGif().getImages() != null) {
//                Glide.with(context).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivImageGif);
                x.image().bind(ivImageGif, mediaItem.getGif().getImages().get(0), imageOptions);
            }

        }
    }

    class HtmlHolder extends BaseViewHolder {
        TextView tvContext;
        ImageView ivImageIcon;
        Button btnInstall;

        public HtmlHolder(View itemView) {
            super(itemView);

            tvContext = (TextView) itemView.findViewById(R.id.tv_context);
            btnInstall = (Button) itemView.findViewById(R.id.btn_install);
            ivImageIcon = (ImageView) itemView.findViewById(R.id.iv_image_icon);

        }
    }

}
