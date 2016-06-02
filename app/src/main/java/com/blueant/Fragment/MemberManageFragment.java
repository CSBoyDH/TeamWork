package com.blueant.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blueant.Application.ActivityManagerApplication;
import com.login.User;
import com.selftask.Task;
import com.team.Team;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import login.comblueant.activity.AddMemberActivity;
import login.comblueant.teamwork.R;

public class MemberManageFragment extends Fragment {
    private TextView tv;
    List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
    private ListView mListView;
    private TextView title;
    private TextView right_text;
    private TextView back;
    private List<User> members;
    private User user;
    private int teamID;
    private Team team;
   SimpleAdapter adapter;



    private static Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_team_member_mamage, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActivityManagerApplication.addDestoryActivity(activity);
        user = (User)activity.getIntent().getSerializableExtra("user");
        teamID = ((Team)activity.getIntent().getSerializableExtra("team")).teamID;
        team = (Team)activity.getIntent().getSerializableExtra("team");
        AsyncTask<Void,Void,List<User>> ATAddMember = new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... voids) {
                String state;

                try{
                    members = Team.getMembers(teamID);
                    if(members==null){
                        members = new ArrayList<User>();
                    }
                } catch (NullPointerException e){}

                return members;
            }
            protected void onPostExecute(List<User> state){
                getData();
                init();
                initView();
            }
        };
        ATAddMember.execute();


    }
    public void onAttach(Activity activity) {

        this.activity = activity;
        super.onAttach(activity);
    }

    public static Activity getThisActivity() {
        return activity;
    }


    void init(){
        back = (TextView)getView().findViewById(R.id.back);
        title = (TextView)getView().findViewById(R.id.title);
        right_text = (TextView)getView().findViewById(R.id.right_text);
        mListView = (ListView)getView().findViewById(R.id.team_member_listView);
        adapter = new SimpleAdapter(activity, data, R.layout.list_item_member,
                new String[]{"memberName", "memberContact",},
                new int[]{R.id.member_name, R.id.member_contact});
        mListView.setAdapter(adapter);
    }
    void initView(){

        title.setText("成员管理");
        right_text.setText("添加");

        right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.userID==team.leaderID){
                    Intent intent = new Intent(activity, AddMemberActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",user);
                    bundle.putInt("teamID",teamID);
                    // intent.putExtra("teamID",teamID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(activity,"您不是队长",Toast.LENGTH_LONG).show();
                }

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                if(user.userID==team.leaderID){
                    final int index = arg2;
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            activity);
                    builder.setIcon(R.drawable.ic_launcher);
                    builder.setTitle("删除成员！");
                    // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                    View view = LayoutInflater.from(activity).inflate(
                            R.layout.dialog_delete_member, null);
                    // 设置我们自己定义的布局文件作为弹出框的Content
                    builder.setView(view);
                    final TextView deleteMember = (TextView) view.findViewById(R.id.tv_delete_member);
                    deleteMember.setText("确定删除此成员？");
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    data.remove(index);
                                    adapter.notifyDataSetChanged();
                                }

                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    builder.show();
                }else{
                    Toast.makeText(activity,"您不是队长",Toast.LENGTH_LONG).show();
                }
                // TODO Auto-generated method stub


                return false;
            }
        });

    }
    void getData(){
        ListIterator<User> iterator=null;
        if(members!=null)iterator=members.listIterator();
        while(iterator!=null&&iterator.hasNext()){
            User member = iterator.next();
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("memberName", member.userName);
            item.put("memberContact", member.userEmail);

            data.add(item);
        }
    }


    public void doBack(View view) {
        activity.onBackPressed();
    }
}