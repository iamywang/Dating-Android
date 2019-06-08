package cn.iamywang.mapchats.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.iamywang.mapchats.R;

import java.util.LinkedList;

public class HistoryPathItemAdapter extends BaseAdapter {
    private LinkedList<HistoryPathItem> mData;
    private Context mContext;

    public HistoryPathItemAdapter(LinkedList<HistoryPathItem> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_history_path_list, parent, false);
        TextView path_item_time = (TextView) convertView.findViewById(R.id.path_item_time);
        TextView path_item_tag = (TextView) convertView.findViewById(R.id.path_item_tag);
        TextView path_item_loc = (TextView) convertView.findViewById(R.id.path_item_loc);
        path_item_time.setText(mData.get(position).getStart_time());
        path_item_tag.setText(mData.get(position).getPath_id());
        path_item_loc.setText(mData.get(position).getStart_loc());
        return convertView;
    }


}
