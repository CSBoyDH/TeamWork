package com.blueant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/24.
 */
public class ShareAdapter extends BaseAdapter {
     ArrayList<HashMap<Object,Object>> data=new ArrayList<HashMap<Object, Object>>();
    LayoutInflater mInflater;
    public ShareAdapter(Context context,ArrayList<HashMap<Object,Object>> data){
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position-1);
    }//-1是因为有XListViewHeader

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            holder=new ViewHolder();

            convertView = mInflater.inflate(R.layout.list_item_share, null);
            holder.publisherName=(TextView)convertView.findViewById(R.id.share_publisher_name);
            holder.time=(TextView)convertView.findViewById(R.id.share_time);
            holder.title=(TextView)convertView.findViewById(R.id.share_title);

            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }
        holder.publisherName.setText(data.get(position).get("publisherName").toString());
        holder.time.setText(data.get(position).get("time").toString());
        holder.title.setText(data.get(position).get("title").toString());

        return convertView;
    }
    private static class ViewHolder{
        private TextView publisherName;
        private TextView time;
        private TextView title;

    }
}
