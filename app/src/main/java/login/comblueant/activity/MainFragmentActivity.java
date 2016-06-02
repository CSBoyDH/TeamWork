package login.comblueant.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.blueant.Application.ActivityManagerApplication;
import com.blueant.Fragment.PersonalManageFragment;
import com.blueant.Fragment.ShareFragment;
import com.blueant.Fragment.TeamManageFragment;

import login.comblueant.teamwork.R;

public class MainFragmentActivity extends FragmentActivity {
    private Fragment[] mFragments;
    private RadioGroup bottomRg;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton rbOne, rbTwo, rbThree, rbFour;
    private Intent intent_personalManage;
    private Intent intent_share;
    private Intent intent_teamManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainfragment);
        ActivityManagerApplication.addDestoryActivity(MainFragmentActivity.this);
        mFragments = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_personal_manage);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_share);
        mFragments[2] = fragmentManager
                .findFragmentById(R.id.fragment_team_manage);
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        fragmentTransaction.show(mFragments[0]).commit();
        setFragmentIndicator();
        initIntent();
    }

    void initIntent() {
        intent_personalManage = new Intent(this, PersonalManageFragment.getThisActivity().getClass());
        intent_personalManage.putExtras(getIntent());
        intent_share = new Intent(this, ShareFragment.getThisActivity().getClass());
        intent_share.putExtras(getIntent());
        intent_teamManage = new Intent(this, TeamManageFragment.getThisActivity().getClass());
        intent_teamManage.putExtras(getIntent());

    }
    private void setFragmentIndicator() {

        bottomRg = (RadioGroup) findViewById(R.id.main_bottomRg);
        rbOne = (RadioButton) findViewById(R.id.rb_personal_manage);
        rbTwo = (RadioButton) findViewById(R.id.rb_share);
        rbThree = (RadioButton) findViewById(R.id.rb_team_manage);

        bottomRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction()
                        .hide(mFragments[0]).hide(mFragments[1])
                        .hide(mFragments[2]);
                switch (checkedId) {
                    case R.id.rb_personal_manage:
                        fragmentTransaction.show(mFragments[0]).commit();
                        break;

                    case R.id.rb_share:
                        fragmentTransaction.show(mFragments[1]).commit();
                        break;

                    case R.id.rb_team_manage:
                        fragmentTransaction.show(mFragments[2]).commit();
                        break;

                    default:
                        break;
                }
            }
        });
    }
    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出",
                        Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }

            return true;
        }
        //拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return onKeyDown(keyCode, event);
    }

}