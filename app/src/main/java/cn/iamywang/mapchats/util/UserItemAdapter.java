package cn.iamywang.mapchats.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.iamywang.mapchats.R;

import java.util.LinkedList;

public class UserItemAdapter extends BaseAdapter {
    private LinkedList<UserListItem> mData;
    private Context mContext;

    public UserItemAdapter(LinkedList<UserListItem> mData, Context mContext) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_friends_list, parent, false);
        TextView user_item_name = (TextView) convertView.findViewById(R.id.user_item_name);
        TextView user_item_msg = (TextView) convertView.findViewById(R.id.user_item_msg);
        TextView user_item_time = (TextView) convertView.findViewById(R.id.user_item_time);
        TextView user_item_num = (TextView) convertView.findViewById(R.id.user_item_num);
        user_item_name.setText(mData.get(position).getUser_name());
        user_item_msg.setText(mData.get(position).getUser_msg());
        user_item_time.setText(mData.get(position).getUser_time());
        if (Integer.parseInt(mData.get(position).getUser_num()) == 0) {
            user_item_num.setVisibility(View.INVISIBLE);
        } else {
            user_item_num.setText(mData.get(position).getUser_num());

        }
        return convertView;
    }
}
