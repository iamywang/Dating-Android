package cn.iamywang.mapchats.util.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.iamywang.mapchats.R;

import java.util.LinkedList;

public class MessageListItemAdapter extends BaseAdapter {
    private LinkedList<MessageListItem> mData;
    private Context mContext;

    public MessageListItemAdapter(LinkedList<MessageListItem> mData, Context mContext) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_msg_list, parent, false);
        ImageView head_image = (ImageView) convertView.findViewById(R.id.msg_item_sex);
        TextView msg_item_name = (TextView) convertView.findViewById(R.id.msg_item_name);
        TextView msg_item_msg = (TextView) convertView.findViewById(R.id.msg_item_msg);
        TextView msg_item_time = (TextView) convertView.findViewById(R.id.msg_item_time);
        TextView msg_item_read = (TextView) convertView.findViewById(R.id.msg_item_read);

        msg_item_name.setText(mData.get(position).getMessage_name());
        msg_item_msg.setText(mData.get(position).getMessage_msg());
        msg_item_time.setText(mData.get(position).getMessage_time());
        if (mData.get(position).getMessage_sex().equals("男")) {
            head_image.setImageResource(R.drawable.ic_user_color);
        } else {
            head_image.setImageResource(R.drawable.ic_user_color_2);
        }
        if (mData.get(position).getMessage_read().equals("yes")) {
            msg_item_read.setText("已读");
        } else if(mData.get(position).getMessage_read().equals("no")) {
            msg_item_read.setText("未读");
            msg_item_read.setTextColor(Color.RED);
        }
        return convertView;
    }
}
