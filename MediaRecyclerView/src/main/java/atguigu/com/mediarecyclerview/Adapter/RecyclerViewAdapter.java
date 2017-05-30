package atguigu.com.mediarecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import atguigu.com.mediarecyclerview.domain.RecyclerViewInfo;

/**
 * Created by sun on 2017/5/30.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private static final int TYPE_RECYCLER_VIDEO = 0;
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
