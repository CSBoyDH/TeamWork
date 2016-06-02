package login.comblueant.activity;

import android.app.Activity;
import android.bluetooth.le.AdvertiseData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.login.LeaderUser;
import com.login.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/7.
 */
public class AddMemberActivity extends Activity {
    List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();

    private EditText et;
    private TextView title;
    private TextView right_text;
    private final String[] mStrings = {"Job","balls","hai","joker","NA"};
    private LeaderUser user;
    private int teamID;
    String memberName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);
        /*getData();*/
        init();
        initView();

    }

    /*private void getData() {
        for(int i = 0;i<7;i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("memberName", "Xpeke");
            item.put("isCheck",false);
            data.add(item);
        }
    }*/

    private void init() {
        teamID = this.getIntent().getIntExtra("teamID",0);
        user = (LeaderUser)this.getIntent().getSerializableExtra("user");
        et = (EditText)findViewById(R.id.sv_addmember);
        title = (TextView)findViewById(R.id.title);
        right_text = (TextView)findViewById(R.id.right_text);


    }
    private void initView(){
        title.setText("成员添加");
        right_text.setText("邀请");
        right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO**********************************
                memberName = et.getText().toString();
                AsyncTask<Void,Void,String> ATAddMember = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        String state;
                        try{
                            if(user.inviteUser(memberName))
                            {
                                state = "邀请发送成功";
                            }else{
                                state ="邀请发送失败";
                            }
                        } catch (Exception e){
                            state ="邀请发送失败";
                        }
                        return state;
                    }
                    protected void onPostExecute(String state){
                        Toast.makeText(AddMemberActivity.this,state,Toast.LENGTH_LONG).show();
                    }
                };
                ATAddMember.execute();
               /* new Thread(new Runnable() {
                    @Override
                    public void run() {//网络访问，修改为异步任务冒气泡
                        user.inviteUser(memberName);
                    }
                }).start();*/

               /* Intent intent = new Intent(AddMemberActivity.this,

                TeamMemberActivity.class);

                startActivity(intent);*/

                finish();
            }
        });

    }

    public void doBack(View view) {
        onBackPressed();
    }
}
