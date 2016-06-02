package login.comblueant.activity;

import java.util.Calendar;

import java.util.Date;
import java.util.logging.LogRecord;


import login.comblueant.teamwork.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.login.User;


public class NullClassActivity extends Activity implements  OnMenuItemClickListener{

    private WebView webView;
    Handler mhandler;
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            // 向后（back）
            case 0:
                webView.goBack();
                break;
            // 向前（Forward）
            case 1:
                webView.goForward();
                break;
        }

        return false;
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem miBack = menu.add(0, 0, 0, "向后（back）");
        MenuItem miForward = menu.add(0, 1, 1, "向前（Forward）");
        miBack.setOnMenuItemClickListener(this);
        miForward.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nullclass_webview);

        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User("dh","123");
                Calendar date = Calendar.getInstance();
                Date time = date.getTime();

                System.out.println();
                /*Log.d("123", user.login() + "测试结果");*/
                Log.d("123",user.createSelfTask("sda",time,"asd").taskID+"测试结果");

            }
        }).start();


        final Calendar c = Calendar.getInstance();
        if(c.get(Calendar.DAY_OF_WEEK)==6||c.get(Calendar.DAY_OF_WEEK)==7)
        {
            Toast.makeText(this, "TAT今天是周末，不能查空教室-。-",Toast.LENGTH_LONG).show();
        }
        webView = (WebView) findViewById(R.id.webview);
        String url = "http://www.charmwithtime.com/vlife/?form=timeline";
        webView.getSettings().setJavaScriptEnabled(true);
		/*String url = "http://www.hao123.com";*/
        if (URLUtil.isNetworkUrl(url))
            webView.loadUrl(url);
        else
            Toast.makeText(this, "输入的网址不正确.", Toast.LENGTH_LONG).show();
		/*etAddress = (EditText) findViewById(R.id.etAddress);*/
		/*etAddress.setText("");*/
		/*ImageButton ibBrowse = (ImageButton) findViewById(R.id.ibBrowse);
		ibBrowse.setOnClickListener(this);*/
        //设置Web视图
        webView.setWebViewClient(new HelloWebViewClient ());

    }
    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}