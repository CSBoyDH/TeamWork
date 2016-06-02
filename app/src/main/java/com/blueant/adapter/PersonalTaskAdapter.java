package com.blueant.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.login.User;
import com.selftask.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/24.
 */
public class PersonalTaskAdapter extends BaseAdapter{
    private ArrayList<HashMap<Object,Object>> data=new ArrayList<HashMap<Object, Object>>();
    Activity activity;
    private List<Task> tasks ;
    private User user;
    LayoutInflater mInflater;
    boolean isInTime;
    public PersonalTaskAdapter(Context context,ArrayList<HashMap<Object,Object>> data,Activity activity,List<Task> tasks,User user){
        this.mInflater = LayoutInflater.from(context);
        this.activity = activity;
        this.data = data;
        this.user = user;
        this.tasks = tasks;
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

            convertView = mInflater.inflate(R.layout.list_item_personal, null);
            holder.taskName=(TextView)convertView.findViewById(R.id.personaltask_name);
            holder.time=(TextView)convertView.findViewById(R.id.personaltask_time);
            holder.content=(TextView)convertView.findViewById(R.id.personaltask_content);
            /*holder.state=(ImageButton)convertView.findViewById(R.id.personaltask_state);*/
            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }
        holder.taskName.setText(data.get(position).get("taskName").toString());
        holder.time.setText(data.get(position).get("time").toString());
        holder.content.setText(data.get(position).get("content").toString());
       /* holder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ImageButton)view).setImageDrawable(activity.getResources().getDrawable(R.drawable.btn_taskfinish_pressed));
                isInTime=true;
                if(tasks.get(position).taskFinishTime.compareTo(Calendar.getInstance().getTime())<0)isInTime = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        user.submitTask(tasks.get(position).taskID, isInTime);

                    }
                }).start();
            }
        });*/
        return convertView;
    }

    private static class ViewHolder{
        private TextView taskName;
        private TextView time;
        private TextView content;
       /* private ImageButton state;*/
    }
}
