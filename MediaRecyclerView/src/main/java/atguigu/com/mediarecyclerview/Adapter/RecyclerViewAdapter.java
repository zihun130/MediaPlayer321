package atguigu.com.mediarecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import atguigu.com.mediarecyclerview.R;
import atguigu.com.mediarecyclerview.domain.RecyclerViewInfo;

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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }
}
