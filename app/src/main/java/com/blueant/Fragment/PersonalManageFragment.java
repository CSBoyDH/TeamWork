package com.blueant.Fragment;

import android.app.Activity;
import android.app.AlertDialog;

import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blueant.Application.ActivityManagerApplication;
import com.blueant.adapter.PersonalTaskAdapter;
import com.blueant.view.QQListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.login.User;
import com.selftask.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import login.comblueant.activity.DetailsPersonalTaskActivity;
import login.comblueant.activity.LoginActivity;

import login.comblueant.activity.NoticeActivity;
import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/14.
 */
public class PersonalManageFragment extends Fragment {
    private QQListView personal_mListView;

    private ArrayList<HashMap<Object,Object>> data=new ArrayList<HashMap<Object, Object>>();
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    PersonalTaskAdapter adapter = null;
    private ImageButton personal_setting;
    private ImageButton personal_addTask;
    private ImageButton personal_notice;
    private RelativeLayout changeAccount_btn;
    private static List<Task> tasks ;
   public  static User user;
    String getName ;
    String getContent;
    Calendar taskFinishTimeCalendar ;
    Date taskFinishTime;
    Thread thread;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//这里没有秒

    private static Activity activity;
    public static Activity getThisActivity() {
        return activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_personal_manage, container, false);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActivityManagerApplication.addDestoryActivity(activity);
        user = (User)activity.getIntent().getSerializableExtra("user");
        Log.d("123", "showtask");

        AsyncTask<Void,Void,List<Task>> ATgetTask = new AsyncTask<Void, Void, List<Task>>() {
            @Override
            protected List<Task> doInBackground(Void... voids) {

                try{
                    tasks = Task.showTask(user.userID);
                    if(tasks==null){
                        tasks = new ArrayList<Task>();
                    }
                } catch (NullPointerException e){}
                return tasks;
            }
            protected void onPostExecute(List<Task> tasks){
                geneItems();
                init();
                initRefresh();
                initSlidingMenu();
            }
        };
        ATgetTask.execute();
        Log.d("123", "finsihshowtask");


    }
    public void onAttach(Activity activity) {

        this.activity = activity;
        super.onAttach(activity);
    }
    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundledata = msg.getData();
            String val = bundledata.getString("result");
            ArrayList list = bundledata.getParcelableArrayList("tasks");
            tasks = (List<Task>) list.get(0);
            adapter.notifyDataSetChanged();
            Toast.makeText(activity, val, Toast.LENGTH_SHORT).show();
            Log.i("123", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };
    Runnable networkPersonalTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String result;
            Task task = user.createSelfTask(getName, taskFinishTime, getContent);
            if(task.taskID>0){
                result = "创建任务成功";

                tasks.add(task);

                String time = formatter.format(taskFinishTime);
                HashMap<Object, Object> item = new HashMap<Object, Object>();
                item.put("taskName", getName);
                item.put("time",time);
                item.put("content",getContent);
                data.add(item);
            }
            else result = "创建任务失败";
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("result", result);
            ArrayList list = new ArrayList();
            list.add(tasks);
            data.putParcelableArrayList("tasks",list);
            msg.setData(data);
            Log.d("123", "handler");
            mHandler1.sendMessage(msg);

        }
    };
    void init(){
        personal_mListView = (QQListView) getView().findViewById(R.id.personal_xListView);
        personal_setting = (ImageButton)getView().findViewById(R.id.setting);
        personal_addTask = (ImageButton)getView().findViewById(R.id.btn_add);
        personal_notice = (ImageButton)getView().findViewById(R.id.btn_notice);
        personal_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,NoticeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        personal_addTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                       activity);
                builder.setIcon(R.drawable.ic_addpersonaltask);
                builder.setTitle("请填写任务信息：");
                // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_add_personal_task, null);
                // 设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                final EditText taskName = (EditText) view.findViewById(R.id.add_personaltask_name);
                final EditText taskContent = (EditText) view.findViewById(R.id.add_personaltask_content);
                final DatePicker finishDate = (DatePicker) view.findViewById(R.id.add_personal_finish_Date);
                final TimePicker finishTime = (TimePicker) view.findViewById(R.id.add_personal_finish_time);
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                getName = taskName.getText().toString().trim();
                                getContent = taskContent.getText().toString().trim();
                                taskFinishTimeCalendar = Calendar.getInstance();
                                taskFinishTimeCalendar.set(finishDate.getYear(), finishDate.getMonth(), finishDate.getDayOfMonth(), finishTime.getCurrentHour(), finishTime.getCurrentMinute());
                                taskFinishTime = taskFinishTimeCalendar.getTime();
                                thread = new Thread(networkPersonalTask);
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
    protected void initSlidingMenu() {//***************侧拉BUG**************

        final SlidingMenu menu = new SlidingMenu(activity);
        personal_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setBehindWidth(500);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.left_drawer_fragment);
        changeAccount_btn =(RelativeLayout)menu.findViewById(R.id.account_btn);
        changeAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.account_btn:

                        //activity.startActivity(new Intent(activity, LoginActivity.class));
                        LoginActivity.getSp().edit().clear().commit();
                       // activity.finish();
                        ActivityManagerApplication.destoryActivity();
                        Intent i = activity.getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                /*this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                        break;

                    default:
                        break;
                }
            }
        });

    }

    void initRefresh(){



       /* personal_mListView.setPullLoadEnable(true);*/
        adapter = new PersonalTaskAdapter(activity,data,activity,tasks,user);

        personal_mListView.setAdapter(adapter);

//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
        /*personal_mListView.setXListViewListener(this);*/
        personal_mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(activity, DetailsPersonalTaskActivity.class);
                Task task = tasks.get(position);//列表中很多task,需要改。不对应
                intent.putExtra("taskName", task.taskName);
                intent.putExtra("taskDetails", task.taskDetails);
                intent.putExtra("taskStartTime", formatter.format(task.taskPublishTime));
                intent.putExtra("taskFinishTime", formatter.format(task.taskFinishTime));
                intent.putExtra("taskState", task.taskState);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putSerializable("task", task);
                intent.putExtras(bundle);

                Log.d("personalManage",task.taskName+task.taskDetails+ task.taskPublishTime.toString()+task.taskFinishTime.toString());
                startActivity(intent);

            }
        });
        personal_mListView.setDelButtonClickListener(new QQListView.DelButtonClickListener() {

            public void clickHappend(final int position) {
                /*Toast.makeText(DetailsProjectActivity.this, position + " : " + adapter.getItem(position), Toast.LENGTH_LONG).show();*/
                /*adapter.remove(adapter.getItem(position));*/
               /* leader.deleteTeamTask(teamTasks.get(position).taskID);*/
                tasks.remove(position);
                data.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        mHandler = new Handler();
    }


    private void geneItems(){
        Iterator<Task> iterator=null;
        if(tasks!=null)iterator = tasks.listIterator();
        while(iterator!=null&&iterator.hasNext()){
            Task task = iterator.next();
            HashMap<Object,Object> itemData=new HashMap<Object, Object>();
            itemData.put("taskName",task.taskName);
            itemData.put("time",formatter.format(task.taskFinishTime));
            itemData.put("content", task.taskDetails);
            itemData.put("state",task.taskState);
            data.add(itemData);
        }
    }

    public void onPause() {
        super.onPause();
    }

}
