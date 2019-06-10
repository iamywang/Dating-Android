package cn.iamywang.mapchats.util.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);
        ImageView head_image = (ImageView) convertView.findViewById(R.id.user_item_sex);
        TextView user_item_name = (TextView) convertView.findViewById(R.id.user_item_name);
        TextView user_item_msg = (TextView) convertView.findViewById(R.id.user_item_msg);
        TextView user_item_time = (TextView) convertView.findViewById(R.id.user_item_time);
        TextView user_item_num = (TextView) convertView.findViewById(R.id.user_item_num);

        String name_and_id = mData.get(position).getUser_name() + "（ID：" + mData.get(position).getUser_id() + "）";
        user_item_name.setText(name_and_id);
        user_item_msg.setText(mData.get(position).getUser_msg());
        user_item_time.setText(mData.get(position).getUser_time());
        if (mData.get(position).getUser_id().equals("1")) {
            head_image.setImageResource(R.drawable.ic_user_admin);
        } else if (mData.get(position).getUser_sex().equals("男")) {
            head_image.setImageResource(R.drawable.ic_user_color);
        } else if (mData.get(position).getUser_sex().equals("女")) {
            head_image.setImageResource(R.drawable.ic_user_color_2);
        } else if (mData.get(position).getUser_sex().equals("room")) {
            head_image.setImageResource(R.drawable.ic_user_color_3);
        }
        if (Integer.parseInt(mData.get(position).getUser_num()) == 0) {
            user_item_num.setVisibility(View.INVISIBLE);
        } else {
            user_item_num.setText(mData.get(position).getUser_num());
        }
        return convertView;
    }
}
