package com.blueant.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blueant.Application.ActivityManagerApplication;
import com.blueant.adapter.ProjectManageAdapter;
import com.blueant.view.QQListView;
import com.login.LeaderUser;
import com.login.MemberUser;
import com.login.User;
import com.service.Utils;
import com.team.Project;
import com.team.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import login.comblueant.activity.DetailsProjectActivity;
import login.comblueant.teamwork.R;

public class ProjectManageFragment extends Fragment {
    private TextView tv;
    ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
    private QQListView mListView;
    ProjectManageAdapter adapter;
    private TextView title;
    private TextView right_text;
    private User user;
    private LeaderUser leader;
    private MemberUser member;
    private boolean isLeader;
    private Team team;
    private List<Project> projects;
    private static Activity activity;
    Thread thread;
    Calendar projectFinishTimeCalendar;
    Calendar projectStartTimeCalendar ;
    Date projectFinishTime;
    Date projectStartTime;
    String getProjectName;
    String getProjectContent;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    public static Activity getThisActivity() {
        return activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_team_project_manage, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActivityManagerApplication.addDestoryActivity(activity);
        team = (Team)activity.getIntent().getSerializableExtra("team");
        user = (User)activity.getIntent().getSerializableExtra("user");
        isLeader = activity.getIntent().getBooleanExtra("isLeader",false);
        Log.d("projectManage", "团队和用户名" + team.teamName + "  " + user.userName);
        if(isLeader)leader = (LeaderUser)user;
        else member = (MemberUser)user;
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    if(projects == null){
                        projects = new ArrayList<Project>();
                    }
                } catch (NullPointerException e){}


            }
        }).start();*/
        AsyncTask<Void,Void,List<Project>> ATgetProject = new AsyncTask<Void, Void, List<Project>>() {
            @Override
            protected List<Project> doInBackground(Void... voids) {

                try{
                    projects = team.proList();
                    if(projects==null){
                        projects = new ArrayList<Project>();
                    }
                } catch (NullPointerException e){}
                return projects;
            }
            protected void onPostExecute(List<Project> projects){
                getData();
                init();
                initView();
            }
        };
        ATgetProject.execute();



    }
    public void onAttach(Activity activity) {

        this.activity = activity;
        super.onAttach(activity);
    }
    void init() {
        title = (TextView) getView().findViewById(R.id.title);
        right_text = (TextView) getView().findViewById(R.id.right_text);

        mListView = (QQListView) getView().findViewById(R.id.team_project_listView);
        adapter = new ProjectManageAdapter(activity,data,activity);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, DetailsProjectActivity.class);
                Bundle projectBundle = new Bundle();
                projectBundle.putSerializable("project", projects.get(i));
                intent.putExtra("isLeader", isLeader);
                if (isLeader) projectBundle.putSerializable("user", leader);
                else projectBundle.putSerializable("user", member);

                intent.putExtras(projectBundle);
                startActivity(intent);
            }
        });
        if(isLeader)mListView.setDelButtonClickListener(new QQListView.DelButtonClickListener() {

            public void clickHappend(final int position) {
                /*Toast.makeText(DetailsProjectActivity.this, position + " : " + adapter.getItem(position), Toast.LENGTH_LONG).show();*/
                /*adapter.remove(adapter.getItem(position));*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        leader.deleteTeamTask(projects.get(position).projectID);
                    }
                }).start();
                data.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundledata = msg.getData();
            String result = bundledata.getString("result");
            Boolean val  = bundledata.getBoolean("val");
            if(val){
                adapter.notifyDataSetChanged();
                Log.d("123","创建项目成功");
            }
            else {
                Log.d("123","创建项目 失败");
            }
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
            Log.i("123", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };
    Runnable networkProjectManage = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作

            Log.d("1",getProjectName+"  "+getProjectContent+"  "+projectFinishTime.toString()+"  "+projectStartTime.toString());

            Project pro = leader.createPro(getProjectName, getProjectContent, projectFinishTime.toString(), projectStartTime.toString());
            String result;
            Boolean val;
            if(pro.projectID>0){
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("projectName", getProjectName);
                item.put("projectContent", getProjectContent);
                item.put("projectStartTime", projectStartTime.getTime());
                item.put("projectFinishTime", projectFinishTime.getTime());
                item.put("projectState",Utils.getStateDescription(1));
                data.add(item);
               /* adapter.notifyDataSetChanged();*/
                result = "创建项目成功";
                projects.add(pro);
                val = true;
            }else {
                result="创建项目失败";
                val = false;
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("result", result);
            data.putBoolean("val", val);
            msg.setData(data);
            Log.d("123", "handler");
            mHandler1.sendMessage(msg);

        }
    };
    void initView(){
        title.setText("项目管理");
        right_text.setText("添加");
        right_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        activity);
                builder.setIcon(R.drawable.ic_addproject);
                builder.setTitle("请填写项目信息：");
                // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_add_project, null);
                // 设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                final EditText projectName = (EditText) view.findViewById(R.id.add_project_name);
                final EditText projectContent = (EditText) view.findViewById(R.id.add_project_content);
                //final EditText projectState = (EditText) view.findViewById(R.id.add_project_state);
                final DatePicker FinishDate = (DatePicker) view.findViewById(R.id.add_project_finish_date);
                final TimePicker FinishTime = (TimePicker) view.findViewById(R.id.add_project_finish_time);
                final DatePicker StartDate = (DatePicker) view.findViewById(R.id.add_project_start_date);
                final TimePicker StartTime = (TimePicker) view.findViewById(R.id.add_project_start_time);
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                getProjectName = projectName.getText().toString().trim();
                                getProjectContent = projectContent.getText().toString().trim();
                                projectFinishTimeCalendar = Calendar.getInstance();
                                projectStartTimeCalendar = Calendar.getInstance();
                                projectFinishTimeCalendar.set(FinishDate.getYear(), FinishDate.getMonth(), FinishDate.getDayOfMonth(), FinishTime.getCurrentHour(), FinishTime.getCurrentMinute());
                                projectStartTimeCalendar.set(StartDate.getYear(), StartDate.getMonth(),StartDate.getDayOfMonth(), StartTime.getCurrentHour(),StartTime.getCurrentMinute());
                                projectFinishTime= projectFinishTimeCalendar.getTime();
                                projectStartTime= projectStartTimeCalendar.getTime();

                                thread = new Thread(networkProjectManage);
                                thread.start();
                            }

                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                builder.show();
            }
        });
    }
    void getData(){
        ListIterator<Project> iterator=null;
        if(projects!=null)iterator = projects.listIterator();
        while(iterator!=null&&iterator.hasNext()){
            Project project = iterator.next();
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("projectName", project.projectName);
            item.put("projectContent", project.projectDetails);
            item.put("projectState", Utils.getStateDescription(project.projectState));
            item.put("projectStartTime", formatter.format(project.projectPublishTime));
            item.put("projectFinishTime",formatter.format(project.projectFinishTime));
            data.add(item);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }
    public void doBack(View view) {
        activity.onBackPressed();
    }

}