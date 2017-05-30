package atguigu.com.mediarecyclerview.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import atguigu.com.mediarecyclerview.domain.RecyclerViewInfo;

/**
 * Created by sun on 2017/5/30.
 */

public class RecyclerViewAdapter extends BaseAdapter {

    public RecyclerViewAdapter(Context context, List<RecyclerViewInfo.ListBean> list) {

    }

    @Override
    public int getCount() {
        return 0;
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
        return null;
    }
}
