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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blueant.Application.ActivityManagerApplication;
import com.blueant.adapter.TeamAdapter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.login.LeaderUser;
import com.login.MemberUser;
import com.login.User;
import com.service.ServiceClient;
import com.service.Utils;
import com.team.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import login.comblueant.activity.LoginActivity;
import login.comblueant.activity.NoticeActivity;
import login.comblueant.activity.TeamManageFragmentActivity;
import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/14.
 */
public class TeamManageFragment extends Fragment{
    private ListView team_mListView;

    private ArrayList<HashMap<Object,Object>> data=new ArrayList<HashMap<Object, Object>>();

    private int start = 0;
    private static int refreshCnt = 0;
    TeamAdapter adapter = null;
    private ImageButton team_setting;
    private ImageButton team_addTeam;
    private ImageButton team_notice;
    protected SlidingMenu team_side_drawer;
    private RelativeLayout changeAccount_btn;
    private User user;
    private List<Team> teams ;
    int teamID;
    Bundle teamBundle ;
    Intent intent;
    private static Activity activity;
    String getName ;
    Thread thread;
    Thread thread2;
    public static Activity getThisActivity() {
        return activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_team_manage, container, false);
    }
    public void onAttach(Activity activity) {

        this.activity = activity;
        super.onAttach(activity);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActivityManagerApplication.addDestoryActivity(activity);
        user = (User)activity.getIntent().getSerializableExtra("user");

      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                teams = Team.showTeams(user.userID);
            }
        }).start();*/
        AsyncTask<Void,Void,List<Team>> ATgetTeam = new AsyncTask<Void, Void,List<Team>>() {
            @Override
            protected List<Team> doInBackground(Void... voids) {
                try{

                    teams = Team.showTeams(user.userID);
                    if(teams==null){
                        teams = new ArrayList<Team>();
                    }
                } catch (NullPointerException e){}
                geneItems();
                return teams;
            }
            protected void onPostExecute(List<Team> teams){
               /* new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();*/
                init();
                initRefresh();

                initSlidingMenu();
            }
        };
        ATgetTeam.execute();


    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundledata = msg.getData();

            if(msg.what==1) {
                String result = bundledata.getString("result");
                Boolean isTeam = bundledata.getBoolean("isTeam");
                Log.d("teamManage","执行Handler");
                if (isTeam) {
                    HashMap<Object, Object> item = new HashMap<Object, Object>();
                    item.put("teamName", getName);
                    item.put("leader", user.userName);
                    item.put("character", "leader");
                    data.add(item);
                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(activity, "团队名:" + getName + " , " + result, Toast.LENGTH_LONG).show();

                Log.i("123", "请求结果为-->" + isTeam);
            }else if(msg.what == 2){
                LeaderUser leader;
                MemberUser member;
                Log.d("teamManage","执行leaderhandle");
                Boolean isleader = bundledata.getBoolean("isleader");
                Log.d("teamManage", isleader.toString());
                try{
                    Log.d("teamManage","try catch");

                    intent = new Intent(activity,TeamManageFragmentActivity.class);
                    if(isleader){
                        leader = new LeaderUser();
                        Utils.fatherToChild(user, leader);
                        leader.teamID = teamID;
                        teamBundle.putSerializable("user", leader);
                        intent.putExtra("isLeader",true);
                    } else {
                        Log.d("teamManage","try catch else");
                        member = new MemberUser();
                        Utils.fatherToChild(user,member);
                        member.teamID = teamID;
                        teamBundle.putSerializable("user",member);
                        intent.putExtra("isLeader",false);
                    }

                }catch (Exception e){
                    e.printStackTrace();

                }

                intent.putExtras(teamBundle);
                Log.d("teamManage", intent.getExtras().getSerializable("user").toString());
                startActivity(intent);

            }
            // TODO
            // UI界面的更新等相关操作
        }
    };
    Runnable networkTeam = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String result;
            Boolean isTeam;
            Team team = user.createTeam(getName);
            if(team!=null&&team.teamID>0){
                result="创建团队成功";
                Log.d("123", "创建团队成功");
                System.out.println(team.teamID + team.teamName + team.leaderID + team.createTime + team.memberNames);
                isTeam = true;
                /*teams = new ArrayList<Team>();*/
                teams.add(team);
            }
            else{
                result = "创建团队失败";
                isTeam = false;
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("result", result);
            data.putBoolean("isTeam", isTeam);
            msg.setData(data);
            msg.what=1;
            Log.d("123", "handler");
            mHandler.sendMessage(msg);

        }
    };

    Runnable networkisLeader= new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String result;
            Boolean isleader;
            if(user.isLeader(teamID)){
                isleader = true;
                result = "队长";
            }else{
                isleader = false;
                result = "成员";
            }
            Log.d("teamManage","判断是不是队长");
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putBoolean("isleader", isleader);
            msg.setData(data);
            msg.what=2;
            Log.d("123", result);
            mHandler.sendMessage(msg);
            Log.d("teamManage", "判断是不是队长");

        }
    };

    void init(){
        team_mListView = (ListView) getView().findViewById(R.id.team_ListView);
        team_notice = (ImageButton)getView().findViewById(R.id.btn_notice);
        team_setting = (ImageButton)getView().findViewById(R.id.setting);
        team_addTeam = (ImageButton)getView().findViewById(R.id.btn_add);
        team_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,NoticeActivity.class);
                startActivity(intent);
            }
        });
        team_addTeam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        activity);
                builder.setIcon(R.drawable.ic_addteam);
                builder.setTitle("请填写团队信息：");
                // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_add_team, null);
                // 设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                final EditText teamName = (EditText) view.findViewById(R.id.add_teamname);

                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                getName = teamName.getText().toString().trim();
                                thread = new Thread(networkTeam);
                                thread.start();

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

    }
    protected void initSlidingMenu() {
        final SlidingMenu menu = new SlidingMenu(activity);
        /*personal_side_drawer = new DrawerView(this).initSlidingMenu();*/
        team_setting.setOnClickListener(new View.OnClickListener() {
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
        adapter = new TeamAdapter(activity,data);
        team_mListView.setAdapter(adapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
        //    team_mListView.setXListViewListener(this);
        team_mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.e("teamManage","teamiDs");
                System.out.println("新建团队的ID为==========》" + teams.get(position).teamID);
                teamID = teams.get(position).teamID;
                teamBundle = new Bundle();
                teamBundle.putSerializable("team",teams.get(position));
                teamBundle.putSerializable("user",user);
                Log.e("teamManage", "teamiDf");
                thread = new Thread(networkisLeader);
                thread.start();
                Log.d("teamManage", "子线程执行完");
            }

        });
    }
    private void geneItems(){
        ListIterator<Team> iterator = null;
        if(teams!=null)iterator = teams.listIterator();
        while(iterator!=null&&iterator.hasNext()){
            Team team = iterator.next();
            HashMap<Object,Object> itemData=new HashMap<Object, Object>();
            itemData.put("teamName", team.teamName);
            itemData.put("leader", ServiceClient.getUserName(team.leaderID));
            String character;
            if(user.isLeader(team.teamID))character = "leader";
            else character = "member";
            itemData.put("character",character);
            data.add(itemData);
            Log.d("teammanage", data.get(0).toString());

        }
    }

}
