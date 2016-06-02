package com.blueant.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.login.User;
import com.selftask.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/25.
 */
public class NoticeAdapter extends BaseAdapter {
    private ArrayList<HashMap<Object,Object>> data=new ArrayList<HashMap<Object, Object>>();
    Activity activity;
    private User user;
    LayoutInflater mInflater;

    public NoticeAdapter(Context context,ArrayList<HashMap<Object,Object>> data,Activity activity,User user){
        this.mInflater = LayoutInflater.from(context);
        this.activity = activity;
        this.data = data;
        this.user =user;

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

            convertView = mInflater.inflate(R.layout.list_item_notice, null);
            holder.teamLeader=(TextView)convertView.findViewById(R.id.notice_teamleader);
            holder.teamName=(TextView)convertView.findViewById(R.id.notice_teamname);
            holder.accepter= (Button) convertView.findViewById(R.id.notice_accept);
            holder.refuse= (Button) convertView.findViewById(R.id.notice_refuse);
            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }
        final int teamID = (int)data.get(position).get("teamID");
        holder.teamName.setText(data.get(position).get("teamName").toString());
       // holder.teamLeader.setText(data.get(position).get("teamLeader").toString());
        holder.accepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//TODO
                AsyncTask<Void,Void,String> ATAddMember = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        String state;
                        try{
                            user.acceptInvite(teamID);
                            state = "邀请已接受";
                        } catch (Exception e){
                            state ="接受邀请失败";
                        }
                        return state;
                    }
                    protected void onPostExecute(String state){
                        Toast.makeText(activity, state, Toast.LENGTH_LONG).show();

                        data.remove(position);
                        notifyDataSetChanged();
                    }
                };
                ATAddMember.execute();

            }
        });
        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(position);
                notifyDataSetChanged();
/*
                ((ImageButton)view).setImageDrawable(activity.getResources().getDrawable(R.drawable.btn_taskfinish_pressed));
*/
            }
        });
        return convertView;
    }
    private static class ViewHolder{
        private TextView teamName;
        private TextView teamLeader;
        private Button accepter;
        private Button refuse;
    }
}
