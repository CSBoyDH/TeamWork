package login.comblueant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blueant.Fragment.PersonalManageFragment;
import com.login.MemberUser;
import com.login.User;
import com.service.ServiceClient;
import com.team.TeamTask;

import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/7.
 */
public class DetailsProjectTaskActivity extends Activity {
    private TextView title;
    private TextView right_text;
    private TextView projectTaskName;
    private TextView projectTaskContent;
    private TextView projectTaskFinishTime;
    private TextView projectTaskMember;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private TeamTask teamTask;
    String accepterName;
    MemberUser user;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_projecttask);
        teamTask = (TeamTask)getIntent().getSerializableExtra("teamTask");
        user = new MemberUser(PersonalManageFragment.user.userID);

        AsyncTask<Void,Void,String> ATGetName = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String state;
                try{

                    accepterName = User.getUserName(teamTask.accepterID);
                    if(!accepterName.equals("")){
                        state ="获取成员姓名成功";
                    }else {
                        state ="获取成员姓名失败";
                    }
                } catch (Exception e){
                    state ="获取成员姓名失败";
                }
                return state;
            }
            protected void onPostExecute(String state){
                Toast.makeText(DetailsProjectTaskActivity.this, state, Toast.LENGTH_LONG).show();
                init();
                initView();
            }
        };
        ATGetName.execute();


    }

    private void init() {

        projectTaskName = (TextView)findViewById(R.id.details_project_task_name);
        projectTaskContent = (TextView)findViewById(R.id.details_project_task_content);
        projectTaskFinishTime = (TextView)findViewById(R.id.details_project_task_finishtime);
        projectTaskMember = (TextView)findViewById(R.id.details_project_task_member);
        title = (TextView)findViewById(R.id.title);
        right_text = (TextView)findViewById(R.id.right_text);


    }
    private void initView() {


        title.setText("项目任务详情");
        right_text.setText("提交");
        projectTaskName.setText(teamTask.taskName);
        projectTaskContent.setText(teamTask.taskDetails);
        projectTaskFinishTime.setText(formatter.format(teamTask.taskFinishTime));
        projectTaskMember.setText(accepterName);
        right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teamTask.accepterID==user.userID) {
                    AsyncTask<Void, Void, String> ATSubmitTask = new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {
                            String state;
                            //isInTime未设置
                            if (user.submitTeamTask(teamTask.taskID,true)) {
                                state = "任务提交成功";
                            } else {
                                state = "任务提交失败";
                            }
                            return state;
                        }

                        protected void onPostExecute(String state) {
                            Toast.makeText(DetailsProjectTaskActivity.this, state, Toast.LENGTH_LONG).show();
                        }
                    };
                    ATSubmitTask.execute();
                }else{
                    Toast.makeText(DetailsProjectTaskActivity.this, "这不是您的任务", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*projectTaskName.setText(item.get("projectTaskName").toString());
        projectTaskContent.setText(item.get("projectTaskContent").toString());
        projectTaskFinishTime.setText(item.get("projectTaskFinishTime").toString());
        projectTaskMember.setText(item.get("projectTaskMember").toString());*/


    }

    public void doBack(View view) {
        onBackPressed();
    }


}
