package login.comblueant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.blueant.Application.ActivityManagerApplication;
import com.blueant.Fragment.MemberManageFragment;
import com.blueant.Fragment.ProjectManageFragment;
import com.login.User;
import com.team.Team;

import login.comblueant.teamwork.R;

public class TeamManageFragmentActivity extends FragmentActivity {
    private Fragment[] mFragments;
    private RadioGroup bottomRg;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton rbOne, rbTwo, rbThree, rbFour;
    private Intent intent_project;
    private Intent intent_member;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManagerApplication.addDestoryActivity(TeamManageFragmentActivity.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_team_manage_fragment);
        mFragments = new Fragment[2];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragement_search);
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]);
        fragmentTransaction.show(mFragments[0]).commit();
        setFragmentIndicator();
        initIntent();
    }
    void initIntent() {
        user = (User)this.getIntent().getSerializableExtra("user");
        intent_project = new Intent(this, ProjectManageFragment.getThisActivity().getClass());
        intent_project .putExtras(getIntent());
        System.out.println(((User) getIntent().getSerializableExtra("user")).userName);
        System.out.println(((Team) getIntent().getSerializableExtra("team")).teamName);
        Bundle bundle = new Bundle();
        bundle.putSerializable("team",((Team) getIntent().getSerializableExtra("team")));
        bundle.putSerializable("user",user);
        intent_member = new Intent(this, MemberManageFragment.getThisActivity().getClass());
        //intent_member.putExtra("teamID", ((Team) getIntent().getSerializableExtra("team")).teamID);

        intent_member.putExtras(bundle);
    }
    private void setFragmentIndicator() {

        bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
        rbOne = (RadioButton) findViewById(R.id.rbOne);
        rbTwo = (RadioButton) findViewById(R.id.rbTwo);

        bottomRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction()
                        .hide(mFragments[0]).hide(mFragments[1])
                ;
                switch (checkedId) {
                    case R.id.rbOne:
                        fragmentTransaction.show(mFragments[0]).commit();
                        break;

                    case R.id.rbTwo:
                        fragmentTransaction.show(mFragments[1]).commit();
                        break;

                    default:
                        break;
                }
            }
        });
    }
    public void doBack(View view) {
        onBackPressed();
    }

}