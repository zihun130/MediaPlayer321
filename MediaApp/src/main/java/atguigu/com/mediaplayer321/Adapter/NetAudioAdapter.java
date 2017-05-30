package atguigu.com.mediaplayer321.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.NetAudioInfo;
import atguigu.com.mediaplayer321.utils.DensityUtil;
import atguigu.com.mediaplayer321.utils.Utils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by sun on 2017/5/27.
 */

public class NetAudioAdapter extends BaseAdapter {
    private static final int TYPE_NET_VIDEO = 0;
    private static final int TYPE_NET_IMAGE = 1;
    private static final int TYPE_NET_TEXT = 2;
    private static final int TYPE_NET_GIF = 3;
    private static final int TYPE_NET_HTML = 4;
    private Context context;
    private List<NetAudioInfo.ListBean> list;

    public NetAudioAdapter(Context context, List<NetAudioInfo.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        NetAudioInfo.ListBean listBean = list.get(position);

        String type = listBean.getType();
        if ("video".equals(type)) {
            itemViewType = TYPE_NET_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_NET_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_NET_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_NET_GIF;
        } else if ("html".equals(type)) {
            itemViewType = TYPE_NET_HTML;
        }

        return itemViewType;
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
        convertView = initView(convertView, getItemViewType(position), list.get(position));
        return convertView;
    }

    private View initView(View convertView, int itemViewType, NetAudioInfo.ListBean mediaItem) {
        switch (itemViewType) {
            case TYPE_NET_VIDEO:
                VideoHolder videoHolder;
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.videoholder_item, null);
                    videoHolder = new VideoHolder(convertView);
                    convertView.setTag(videoHolder);
                } else {
                    videoHolder = (VideoHolder) convertView.getTag();
                }

                videoHolder.setData(mediaItem);


                break;
            case TYPE_NET_IMAGE:
                ImageHolder imageHolder;
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.imageholder_item, null);
                    imageHolder = new ImageHolder(convertView);
                    convertView.setTag(imageHolder);
                } else {
                    imageHolder = (ImageHolder) convertView.getTag();
                }

                imageHolder.setData(mediaItem);

                break;
            case TYPE_NET_TEXT:
                TextHolder textHolder;
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.textholder_item, null);
                    textHolder = new TextHolder(convertView);
                    convertView.setTag(textHolder);
                } else {
                    textHolder = (TextHolder) convertView.getTag();
                }

                textHolder.setData(mediaItem);

                break;
            case TYPE_NET_GIF:
                GifHolder gifHolder;
                if (convertView == null) {

                    convertView = View.inflate(context, R.layout.gifholder_item, null);
                    gifHolder = new GifHolder(convertView);
                    convertView.setTag(gifHolder);
                } else {
                    gifHolder = (GifHolder) convertView.getTag();
                }

                gifHolder.setData(mediaItem);


                break;
            case TYPE_NET_HTML:
                HtmlHolder htmlHolder;
                if (convertView == null) {

                    convertView = View.inflate(context, R.layout.htmlholder_item, null);
                    htmlHolder = new HtmlHolder(convertView);
                    convertView.setTag(htmlHolder);
                } else {
                    htmlHolder = (HtmlHolder) convertView.getTag();
                }

                break;
        }
        return convertView;
    }

    class BaseViewHolder {
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

        public BaseViewHolder(View convertView) {
//公共的
            ivHeadpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvTimeRefresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
            ivRightMore = (ImageView) convertView.findViewById(R.id.iv_right_more);
//bottom
            ivVideoKind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
            tvVideoKindText = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
            tvShenheDingNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
            tvShenheCaiNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_number);
            tvPostsNumber = (TextView) convertView.findViewById(R.id.tv_posts_number);
            llDownload = (LinearLayout) convertView.findViewById(R.id.ll_download);
        }

        public void setData(NetAudioInfo.ListBean mediaItem) {
            if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
                x.image().bind(ivHeadpic, mediaItem.getU().getHeader().get(0));
            }
            if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
                tvName.setText(mediaItem.getU().getName() + "");
            }

            tvTimeRefresh.setText(mediaItem.getPasstime());

//设置标签
            List<NetAudioInfo.ListBean.TagsBean> tagsEntities = mediaItem.getTags();
            if (tagsEntities != null && tagsEntities.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < tagsEntities.size(); i++) {
                    buffer.append(tagsEntities.get(i).getName() + "");
                }
                tvVideoKindText.setText(buffer.toString());
            }

//设置点赞，踩,转发

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


        public VideoHolder(View convertView) {
            super(convertView);

            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            utils = new Utils();
            tvPlayNums = (TextView) convertView.findViewById(R.id.tv_play_nums);
            tvVideoDuration = (TextView) convertView.findViewById(R.id.tv_video_duration);
            ivCommant = (ImageView) convertView.findViewById(R.id.iv_commant);
            tvCommantContext = (TextView) convertView.findViewById(R.id.tv_commant_context);
            jcvVideoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.jcv_videoplayer);

        }

        public void setData(NetAudioInfo.ListBean mediaItem) {
            super.setData(mediaItem);

//设置文本-所有的都有,只有广告没有哦
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
            //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
            boolean setUp = jcvVideoplayer.setUp(
                    mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    "");
//加载图片
            if (setUp) {
//                ImageLoader.getInstance().displayImage(mediaItem.getVideo().getThumbnail().get(0),
//                        jcvVideoplayer.thumbImageView);
                Glide.with(context).load(mediaItem.getVideo().getThumbnail().get(0)).into(jcvVideoplayer.thumbImageView);
            }
            tvPlayNums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
            tvVideoDuration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");

        }


    }

    class ImageHolder extends BaseViewHolder {
        TextView tvContext;
        ImageView ivImageIcon;

        public ImageHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            ivImageIcon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
        }

        public void setData(NetAudioInfo.ListBean mediaItem) {
            super.setData(mediaItem);
//设置文本-所有的都有
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

        public TextHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
        }

        public void setData(NetAudioInfo.ListBean mediaItem) {
            super.setData(mediaItem);
//设置文本-所有的都有
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
        }

    }

    class GifHolder extends BaseViewHolder {
        TextView tvContext;
        ImageView ivImageGif;
        private ImageOptions imageOptions;

        public GifHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            ivImageGif = (ImageView) convertView.findViewById(R.id.iv_image_gif);
            imageOptions = new ImageOptions.Builder()
                    .setSize(ViewGroup.LayoutParams.WRAP_CONTENT, -2)
//设置圆角
                    .setRadius(DensityUtil.dip2px(context, 5))
                    .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.video_default)
                    .setFailureDrawableId(R.drawable.video_default)
                    .build();

        }

        public void setData(NetAudioInfo.ListBean mediaItem) {
            super.setData(mediaItem);
//设置文本-所有的都有
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

        public HtmlHolder(View convertView) {
            super(convertView);

            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            btnInstall = (Button) convertView.findViewById(R.id.btn_install);
            ivImageIcon = (ImageView) convertView.findViewById(R.id.iv_image_icon);

        }
    }
}
