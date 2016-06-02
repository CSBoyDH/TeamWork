package login.comblueant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blueant.receiver.MyReceiver;
import com.blueant.view.QQListView;
import com.blueant.view.QQListView.DelButtonClickListener;
import com.login.LeaderUser;
import com.login.MemberUser;
import com.service.State;
import com.service.Utils;
import com.team.Project;
import com.team.TeamTask;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/7.
 */
public class DetailsProjectActivity extends Activity {
    List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
    private QQListView mListView;
    private TextView title;
    private TextView right_text;
    Thread thread;
    SimpleAdapter adapter;
    private Project project;
    private List<TeamTask> teamTasks;
    private MemberUser member;
    private LeaderUser leader;
    private boolean isLeader;
    String strProjectTaskName ;
    String strProjectTaskContent ;
    String strProjectMemberName ;
    Calendar projectTaskFinishTimeCalendar ;
    Date projectTaskFinishTime ;

    public static TextView projectTaskMemberName = null;
    public static ArrayList<String> memberList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_project);
        project =(Project)getIntent().getSerializableExtra("project");
        isLeader = getIntent().getBooleanExtra("isLeader", false);

        if(isLeader)leader = (LeaderUser)getIntent().getSerializableExtra("user");
        else member = (MemberUser) getIntent().getSerializableExtra("user");


        AsyncTask<Void,Void,List<TeamTask>> ATgetTeamTask = new AsyncTask<Void, Void, List<TeamTask>>() {
            @Override
            protected List<TeamTask> doInBackground(Void... voids) {

                try{
                    teamTasks = project.showTeamTask();
                    if(teamTasks==null){
                        teamTasks = new ArrayList<TeamTask>();
                    }
                } catch (NullPointerException e){}
                return teamTasks;
            }
            protected void onPostExecute(List<TeamTask> teamTasks){
                getData();
                init();
                initView();
            }
        };
        ATgetTeamTask.execute();

    }
    void init(){

        title = (TextView)findViewById(R.id.title);
        right_text = (TextView)findViewById(R.id.right_text);
        mListView = (QQListView)findViewById(R.id.project_task_listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DetailsProjectActivity.this, DetailsProjectTaskActivity.class);
                intent.putExtra("teamID", project.teamID);
                Bundle b = new Bundle();
                b.putSerializable("teamTask", teamTasks.get(i));

                intent.putExtras(b);
                startActivity(intent);
            }
        });

            mListView.setDelButtonClickListener(new DelButtonClickListener() {

                public void clickHappend(final int position) {
                /*Toast.makeText(DetailsProjectActivity.this, position + " : " + adapter.getItem(position), Toast.LENGTH_LONG).show();*/
                /*adapter.remove(adapter.getItem(position));*/
                    AsyncTask<Void,Void,String> ATDeleteTeamTask = new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {
                            String state;
                            if(isLeader){
                                    if(leader.deleteTeamTask(teamTasks.get(position).taskID)){
                                    state = "删除任务成功";
                                }else{
                                    state = "删除任务失败";
                                }
                            }else{
                                state = "权限不够";
                            }
                            return state;
                        }
                        protected void onPostExecute(String state){
                            if(state.equals("删除任务成功")){
                                data.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                            Toast.makeText(DetailsProjectActivity.this,state,Toast.LENGTH_LONG).show();
                        }
                    };
                    ATDeleteTeamTask.execute();
                }
            });
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundledata = msg.getData();
            String val = bundledata.getString("result");
            ArrayList list = bundledata.getParcelableArrayList("teamTasks");
            teamTasks = (List<TeamTask>) list.get(0);
            adapter.notifyDataSetChanged();
            Toast.makeText(DetailsProjectActivity.this, val, Toast.LENGTH_SHORT).show();
            Log.i("123", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };
    Runnable networkProjectDetails = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String result = "分配任务失败";
           memberList = MyReceiver.getMemberList();
            for(int i=0;i<memberList.size();i++){
                TeamTask task = leader.distributeTask(strProjectTaskName, project.projectID, strProjectTaskContent, memberList.get(i), projectTaskFinishTime);//分配的名字，FinishTime呢？
                if(task.taskID>-1){
                    result = memberList.get(i)+"的任务分配成功";
                    teamTasks.add(task);
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("projectTaskName", strProjectTaskName);
                    item.put("projectTaskContent", strProjectTaskContent);
                    item.put("projectTaskState", Utils.getStateDescription(State.IN_TIME_NONFINISHED));
                    item.put("projectTaskFinishTime", projectTaskFinishTime);
                    // item.put("projectTaskMemberName", strProjectMemberName);
                    item.put("projectTaskMemberName", "成员");
                    data.add(item);
                }  else {
                    result = "分配任务失败";
                    Log.d("disTask","分配任务失败");
                }

            }



            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("result", result);
            ArrayList list = new ArrayList();
            list.add(teamTasks);
            data.putParcelableArrayList("teamTasks", list);
            msg.setData(data);
            Log.d("123", "handler");
            mHandler.sendMessage(msg);

        }
    };

    void initView() {
        title.setText("项目任务管理");
        right_text.setText("添加");
        if (isLeader)
            right_text.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            DetailsProjectActivity.this);
                    builder.setIcon(R.drawable.ic_addprojectask);
                    builder.setTitle("请填写项目任务信息：");
                    // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                    View view = LayoutInflater.from(DetailsProjectActivity.this).inflate(
                            R.layout.dialog_add_project_task, null);
                    // 设置我们自己定义的布局文件作为弹出框的Content
                    builder.setView(view);
                    final EditText projectTaskName = (EditText) view.findViewById(R.id.add_project_task_name);
                    final EditText projectTaskContent = (EditText) view.findViewById(R.id.add_project_task_content);
                    final ImageButton projectTaskAddMember = (ImageButton) view.findViewById(R.id.btn_add_project_task_addmember);
                    projectTaskMemberName = (TextView) view.findViewById(R.id.add_project_task_member_name);
                    final DatePicker finishDate = (DatePicker) view.findViewById(R.id.add_project_task_finish_date);
                    final TimePicker finishTime = (TimePicker) view.findViewById(R.id.add_project_task_finish_time);
                    //String a = "ada";

                    projectTaskAddMember.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DetailsProjectActivity.this, ProjectTaskAddMemberActivity.class);
                            intent.putExtra("teamID", leader.teamID);
                            startActivity(intent);
                        }
                    });


                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    strProjectTaskName = projectTaskName.getText().toString();
                                    strProjectTaskContent = projectTaskContent.getText().toString();
                                    strProjectMemberName = projectTaskMemberName.getText().toString();
                                    projectTaskFinishTimeCalendar = Calendar.getInstance();
                                    projectTaskFinishTimeCalendar.set(finishDate.getYear(), finishDate.getMonth(), finishDate.getDayOfMonth(), finishTime.getCurrentHour(), finishTime.getCurrentMinute());
                                    projectTaskFinishTime = projectTaskFinishTimeCalendar.getTime();
                                    thread = new Thread(networkProjectDetails);
                                    thread.start();
                                   //选中成员列表

                                /*Toast.makeText(LoginActivity.this,"注册成功", Toast.LENGTH_SHORT).show();*/

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
        adapter = new SimpleAdapter(this, data, R.layout.list_item_projecttask,
                new String[]{"projectTaskName", "projectTaskContent", "projectTaskState"},
                new int[]{R.id.project_task_name, R.id.project_task_content, R.id.project_task_state});
        mListView.setAdapter(adapter);
    }



    void getData(){
        ListIterator<TeamTask> iterator=null;
        if(teamTasks!=null)iterator = teamTasks.listIterator();
        while(iterator!=null&&iterator.hasNext()){
            TeamTask teamTask = iterator.next();
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("projectTaskName", teamTask.taskName);
            item.put("projectTaskContent", teamTask.taskDetails);
            item.put("projectTaskState", teamTask.taskState);
            data.add(item);
        }
    }

    public void doBack(View view) {
        onBackPressed();
    }



}
