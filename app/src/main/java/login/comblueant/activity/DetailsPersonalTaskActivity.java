package login.comblueant.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.login.User;
import com.selftask.Task;
import com.service.State;
import com.service.Utils;

import java.util.Calendar;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/6.
 */
public class DetailsPersonalTaskActivity extends Activity {
    private TextView tvTaskName;
    private TextView taskName;
    private TextView tvStartTime;
    private TextView startTime;
    private TextView tvFinishTime;
    private TextView finishTime;
    private TextView tvState;
    private TextView state;
    private TextView title;
    private TextView right_text;
    private TextView tvDetails;
    private TextView details;
    String task_name;
    String task_startTime;
    String task_finishTime;
    String task_state;
    int stateInt;
    String task_details;
    private User user;
    private Task task;
    boolean isInTime;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_personaltask);
        getData();
        init();
        initView();
    }
    void init(){
        title = (TextView)findViewById(R.id.title);
        right_text = (TextView)findViewById(R.id.right_text);
        tvTaskName = (TextView)findViewById(R.id.tv_details_personaltask_taskname);
        taskName= (TextView)findViewById(R.id.details_personaltask_taskname);
        tvStartTime= (TextView)findViewById(R.id.tv_details_personaltask_starttime);
        startTime= (TextView)findViewById(R.id.details_personaltask_starttime);
        tvFinishTime= (TextView)findViewById(R.id.tv_details_personaltask_finishtime);
        finishTime= (TextView)findViewById(R.id.details_personaltask_finishtime);
        tvState= (TextView)findViewById(R.id.tv_details_personaltask_state);
        state= (TextView)findViewById(R.id.details_personaltask_state);
        tvDetails= (TextView)findViewById(R.id.tv_details_personaltask_details);
        details= (TextView)findViewById(R.id.details_personaltask_details);


    }
    void initView(){
        title.setText("任务详情");
        right_text.setText("提交");
        right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task.taskFinishTime.compareTo(Calendar.getInstance().getTime())<0)isInTime = false;
                AsyncTask<Void,Void,String> ATSubmitTask = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        String state;

                        if(user.submitTask(task.taskID, isInTime)){
                            state = "任务提交成功";
                        }else{
                            state = "任务提交失败";
                        }
                        return state;
                    }
                    protected void onPostExecute(String state){
                        Toast.makeText(DetailsPersonalTaskActivity.this,state,Toast.LENGTH_LONG).show();
                    }
                };
                ATSubmitTask.execute();

            }
        });
        taskName.setText(task_name);
        startTime.setText(task_startTime);
        finishTime.setText(task_finishTime);
        state.setText(task_state);
        details.setText(task_details);
    }
    void getData(){
        task_name = getIntent().getStringExtra("taskName");
        task_startTime = getIntent().getStringExtra("taskStartTime");
        task_finishTime = getIntent().getStringExtra("taskFinishTime");
        stateInt = getIntent().getIntExtra("taskState", -1);
        task_state = Utils.getStateDescription(stateInt);
        task_details = getIntent().getStringExtra("taskDetails");
        user = (User)getIntent().getSerializableExtra("user");
        task = (Task)getIntent().getSerializableExtra("task");
        Log.d("personalManage",task_name + task_details+task_startTime+ task_finishTime );
    }
    public void doBack(View view) {
        onBackPressed();
    }

}
