package login.comblueant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blueant.Application.ActivityManagerApplication;
import com.login.User;

import login.comblueant.teamwork.R;

public class LoginActivity extends Activity {

    private TextView tVUserAccount;
    private TextView tVUserPassword;
   /* private TextView tVAutoLogin;
    private TextView tVRememberPassword;*/
    private EditText eDAccount;
    private EditText eDPassword;

    private CheckBox cBAutoLogin;
    private CheckBox cBRememberPassword;
    private ImageButton btn_login;
    private ImageButton btn_register;

    Thread thread;
    static String getAccount;
    static String getPassword ;
    static String getConfirmPassword;
    static String getContact ;
    static String getEmail ;
    private static SharedPreferences sp;
    public static SharedPreferences getSp() {

        return sp;
    }

    private String userNameValue,passwordValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_login);
        ActivityManagerApplication.addDestoryActivity(LoginActivity.this);

        initView();
        init();
    }
    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("result");
            Toast.makeText(LoginActivity.this, val, Toast.LENGTH_SHORT).show();
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };
    Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int val = data.getInt("result");
            if(val==-1)
                Toast.makeText(LoginActivity.this,"用户名或密码错误", Toast.LENGTH_LONG).show();
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };
    void initView(){
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//????
        tVUserAccount = (TextView)findViewById(R.id.tv_useraccount);
        tVUserPassword = (TextView)findViewById(R.id.tv_userpassword);
        eDAccount = (EditText) findViewById(R.id.et_useraccount);
        eDPassword = (EditText) findViewById(R.id.et_userpassword);

        cBAutoLogin = (CheckBox) findViewById(R.id.cb_autologin);
        cBRememberPassword = (CheckBox) findViewById(R.id.cb_rememberpassword);
        btn_login = (ImageButton)findViewById(R.id.btn_login);
        btn_register = (ImageButton)findViewById(R.id.btn_register);


    }
    void init(){
        login();
        register();
    }
    Runnable networkRegister = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String result;
            Log.d("123", "进入");
            if (getPassword.equals(getConfirmPassword))
                if (User.register(getAccount, getPassword, getEmail, getContact) > 0) {
                    result = "注册成功";
                    Log.d("123","注册成功");
                }
                else result = "注册失败";
            else result = "两次密码不一致";
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("result", result);
            msg.setData(data);
            Log.d("123", "handler");
            mHandler1.sendMessage(msg);

        }
    };
    Runnable networkAutoLogin = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            User user = new User(sp.getString("USER_NAME", ""),sp.getString("PASSWORD", ""));
            user.login();
            Log.d("123", user.login() + "测试结果");
            Intent intent = new Intent(LoginActivity.this,LoginingActivity.class);
            Bundle userBundle = new Bundle();
            userBundle.putSerializable("user", user);
            intent.putExtras(userBundle);
            LoginActivity.this.startActivity(intent);
            finish();
            /*Message msg = new Message();
            Bundle data = new Bundle();
            *//*data.putString("result", result);
            msg.setData(data);
            Log.d("123", "handler");
            mHandler.sendMessage(msg);*/

        }
    };
    Runnable networkLogin = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            userNameValue = eDAccount.getText().toString();
            passwordValue = eDPassword.getText().toString();
            User user = new User(userNameValue,passwordValue);
            if(user.login())
            {
                Log.d("123","登录成功");
                            /*Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();*/
                //登录成功和记住密码框为选中状态才保存用户信息
                if(cBRememberPassword.isChecked())
                {
                    //记住用户名、密码、
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_NAME", userNameValue);
                    editor.putString("PASSWORD", passwordValue);
                    editor.commit();

                }

                Intent intent = new Intent(LoginActivity.this,LoginingActivity.class);
                Bundle userBundle = new Bundle();
                userBundle.putSerializable("user", user);
                intent.putExtras(userBundle);
                LoginActivity.this.startActivity(intent);
                finish();

            }else{
                int result = -1;
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("result", result);
                msg.setData(data);
                Log.d("123", "handler");
                mHandler2.sendMessage(msg);
            }


        }
    };

    void register(){
        btn_register.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请填写注册信息：");
                // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(LoginActivity.this).inflate(
                        R.layout.dialog_register, null);
                // 设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                final EditText writeAccount = (EditText) view.findViewById(R.id.write_account);
                final EditText writePassword = (EditText) view.findViewById(R.id.write_password);
                final EditText confirmPassword = (EditText) view.findViewById(R.id.confirm_password);
                final EditText contact = (EditText) view.findViewById(R.id.contact_text);
                final EditText email = (EditText) view.findViewById(R.id.email_text);

                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                getAccount = writeAccount.getText().toString().trim();
                                getPassword = writePassword.getText().toString().trim();
                                getConfirmPassword = confirmPassword.getText().toString().trim();
                                getContact = contact.getText().toString();
                                getEmail = email.getText().toString();
                                thread = new Thread(networkRegister);
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

    void login(){
        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            cBRememberPassword.setChecked(true);
            eDAccount.setText(sp.getString("USER_NAME", ""));
            eDPassword.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动登录状态̬
                cBAutoLogin.setChecked(true);
                thread = new Thread(networkAutoLogin);
                thread.start();

            }
        }



        // 登录监听事件  现在默认为用户名为：ba 密码：123
        btn_login.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                thread = new Thread(networkLogin);
                thread.start();

            }
        });

        //监听记住密码多选框按钮事件
        cBRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (cBRememberPassword.isChecked()) {

                    System.out.println("��ס������ѡ��");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("��ס����û��ѡ��");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //监听自动登录多选框事件
        cBAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cBAutoLogin.isChecked()) {
                    System.out.println("�Զ���¼��ѡ��");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("�Զ���¼û��ѡ��");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

    }


}
