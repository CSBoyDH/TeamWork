package login.comblueant.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blueant.adapter.NoticeAdapter;
import com.login.User;
import com.selftask.Task;
import com.share.Share;
import com.team.Invitation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/24.
 */
public class NoticeActivity extends Activity {
    private ListView noticeListView;
    private TextView title;
    private TextView right_text;
    private NoticeAdapter adapter;
    private List<Invitation> invitations;
    private ArrayList<HashMap<Object,Object>> data=new ArrayList<HashMap<Object, Object>>();
    private User user;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        user = (User)this.getIntent().getSerializableExtra("user");
        AsyncTask<Void,Void,List<Invitation>> ATgetInvitions = new AsyncTask<Void, Void, List<Invitation>>() {
            @Override
            protected List<Invitation> doInBackground(Void... voids) {

                try{
                    invitations = Invitation.getInv(user.userID);
                    if(invitations==null){
                        invitations = new ArrayList<Invitation>();
                    }
                } catch (NullPointerException e){}

                return invitations;
            }
            protected void onPostExecute(List<Invitation> invitations){
                geneItems();
                init();
                initView();
            }
        };
        ATgetInvitions.execute();


    }
    void init(){

        title = (TextView)findViewById(R.id.title);
        right_text = (TextView)findViewById(R.id.right_text);
        noticeListView = (ListView) findViewById(R.id.notice_listView);
        adapter = new NoticeAdapter(this,data,NoticeActivity.this,user);
    }
    void initView(){
        title.setText("通知");
        right_text.setText("清空");
        right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.removeAll(data);
                adapter.notifyDataSetChanged();
            }
        });
        noticeListView.setAdapter(adapter);
    }
  /*  private void geneItems(){
        for (int i =0;i<10;i++) {
            HashMap<Object, Object> itemData = new HashMap<Object, Object>();
            itemData.put("teamName", "blueAnt");
            itemData.put("teamLeader", "Churong Xiao");
            data.add(itemData);
        }
    }*/
  private void geneItems(){
      ListIterator<Invitation> iterator = null;
      if(invitations!=null)iterator = invitations.listIterator();
      while(iterator!=null&&iterator.hasNext()){
          Invitation invitation = iterator.next();
          HashMap<Object,Object> itemData=new HashMap<Object, Object>();
          itemData.put("teamName",invitation.teamName);
          itemData.put("leaderName",invitation.leaderName);
          itemData.put("inviteTime",invitation.inviteTime);
          itemData.put("teamID",invitation.teamID);
          data.add(itemData);
      }
  }
    public void doBack(View view) {
        onBackPressed();
    }

}
