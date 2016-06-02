package login.comblueant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.blueant.receiver.MyReceiver;
import com.login.User;
import com.team.Team;
import com.team.TeamTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/7.
 */
public class ProjectTaskAddMemberActivity extends Activity {
    List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();

    public static List<String> getMemberList() {
        return memberList;
    }

    private static ArrayList<String> memberList = new ArrayList<String>();
    private ListView mListView;
    private TextView title;
    private TextView right_text;
    private int teamID;
    private List<User> members;
    private MyAdapter adapter;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projecttask_addmember);
        teamID = getIntent().getIntExtra("teamID",0);
        AsyncTask<Void,Void,List<User>> ATgetTeamTask = new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... voids) {

                try{
                    members = Team.getMembers(teamID);
                    if(members==null){
                        members = new ArrayList<User>();
                    }
                } catch (NullPointerException e){}
                return members;
            }
            protected void onPostExecute(List<User> members){
                getData();
                init();
                initView();
            }
        };
        ATgetTeamTask.execute();



    }

    private void init() {
        memberList.removeAll(memberList);
        title = (TextView)findViewById(R.id.title);
        right_text = (TextView)findViewById(R.id.right_text);
        mListView = (ListView)findViewById(R.id.project_task_add_member_listView);
        adapter = new MyAdapter(this);
        mListView.setAdapter(adapter);
    }

    private void initView() {//  发送广播
        title.setText("任务成员添加");
        right_text.setText("完成");
        right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishMemberList();//向memberList中写入数据
                Intent intent = new Intent("android.intent.action.MY_BROADCAST");
                intent.putStringArrayListExtra("memberList",memberList);
                sendBroadcast(intent);
                //DetailsProjectActivity.memberList = MyReceiver.getMemberList();
               // doBack(right_text);
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0;i<memberList.size();i++){
                    stringBuffer.append(memberList.get(i)+"   ");
                }
                DetailsProjectActivity.projectTaskMemberName.setText(stringBuffer);
                finish();
            }
        });
    }
    private void finishMemberList(){
        for(int i =0;i<data.size();i++){
            if (data.get(i).get("isCheck").equals(true)){
                memberList.add(data.get(i).get("memberName").toString());
            }
        }
    }
    private void getData() {
        ListIterator<User> iterator=null;
        if(members!=null)iterator = members.listIterator();
        while(iterator!=null&&iterator.hasNext()){
            User member = iterator.next();
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("memberName", member.userName);
            item.put("memberEmail",member.userEmail);
            item.put("memberContact",member.contactWay);//同时需要显示联系方式和Email 请及时修改视图。
            item.put("isCheck",false);
            data.add(item);
        }
    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater mInflater;
        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {//此处Object应换为Task，等待后台提供ArrayList<tasklist>数据
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.list_item_add_member, null);
                holder.memberName=(TextView)convertView.findViewById(R.id.add_member_name);
                holder.email=(TextView)convertView.findViewById(R.id.add_member_email);
                holder.contact=(TextView)convertView.findViewById(R.id.add_member_email);
                holder.isCheck=(CheckBox)convertView.findViewById(R.id.add_member_check);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }
            holder.memberName.setText(data.get(position).get("memberName").toString());
            holder.email.setText(data.get(position).get("memberEmail").toString());
            holder.contact.setText(data.get(position).get("memberContact").toString());
            holder.isCheck.setChecked(data.get(position).get("isCheck").equals(true));
            String name = data.get(position).get("memberName").toString();
            holder.isCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean state = (Boolean)data.get(position).get("isCheck");
                    state = !state;
                    data.get(position).put("isCheck", state);

                }
            });

            return convertView;
        }
    }

    private static class ViewHolder{
        private TextView memberName;
        private TextView email;
        private TextView contact;
        private CheckBox isCheck;
    }
    public void doBack(View view) {
        onBackPressed();
    }
}
