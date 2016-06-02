package com.blueant.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.login.User;
import com.selftask.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/11/2.
 */
public class ProjectManageAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,Object>> data=new ArrayList<HashMap<String, Object>>();
    Activity activity;

    LayoutInflater mInflater;
    public ProjectManageAdapter(Context context,ArrayList<HashMap<String,Object>> data,Activity activity){
        this.mInflater = LayoutInflater.from(context);
        this.activity = activity;
        this.data = data;
    }
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {//此处Object应换为Task，等待后台提供ArrayList<tasklist>数据
        return data.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            holder=new ViewHolder();

            convertView = mInflater.inflate(R.layout.list_item_project, null);
            holder.projectName=(TextView)convertView.findViewById(R.id.project_name);
            holder.projectContent=(TextView)convertView.findViewById(R.id.project_content);
            holder.projectState=(TextView)convertView.findViewById(R.id.project_state);
            holder.projectStartTime=(TextView)convertView.findViewById(R.id.project_start_time);
            holder.projectFinishTime=(TextView)convertView.findViewById(R.id.project_finish_time);

            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }
        holder.projectName.setText(data.get(position).get("projectName").toString());
        holder.projectContent.setText(data.get(position).get("projectContent").toString());
        holder.projectState.setText(data.get(position).get("projectState").toString());
        holder.projectStartTime.setText(data.get(position).get("projectStartTime").toString());
        holder.projectFinishTime.setText(data.get(position).get("projectFinishTime").toString());


        return convertView;
    }
    private static class ViewHolder{
        private TextView projectName;
        private TextView projectContent;
        private TextView projectState;
        private TextView projectStartTime;
        private TextView projectFinishTime;
    }
}
