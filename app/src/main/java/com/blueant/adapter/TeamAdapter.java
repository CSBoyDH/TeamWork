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
public class TeamAdapter extends BaseAdapter {
    ArrayList<HashMap<Object,Object>> data=new ArrayList<HashMap<Object, Object>>();
    LayoutInflater mInflater;
    public TeamAdapter(Context context,ArrayList<HashMap<Object,Object>> data){
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            holder=new ViewHolder();

            convertView = mInflater.inflate(R.layout.list_item_team, null);
            holder.teamName=(TextView)convertView.findViewById(R.id.team_name);
            holder.leader=(TextView)convertView.findViewById(R.id.team_leader);
            holder.character=(TextView)convertView.findViewById(R.id.team_character);

            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }
        holder.teamName.setText(data.get(position).get("teamName").toString());
        holder.leader.setText(data.get(position).get("leader").toString());
        holder.character.setText(data.get(position).get("character").toString());

        return convertView;
    }
    private static class ViewHolder{
        private TextView teamName;
        private TextView leader;
        private TextView character;

    }
}
