package login.comblueant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.blueant.Application.ActivityManagerApplication;

import login.comblueant.teamwork.R;

public class LoginingActivity extends Activity {

    private ProgressBar progressBar;
    private Button backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logining);
        ActivityManagerApplication.addDestoryActivity(LoginingActivity.this);
        init();
    }
    void init(){
        progressBar = (ProgressBar) findViewById(R.id.pgBar);
        backButton = (Button) findViewById(R.id.btn_back);

        Intent intent = new Intent(this, MainFragmentActivity.class);
        intent.putExtras(getIntent());
        LoginingActivity.this.startActivity(intent);
        finish();
        backButton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                finish();

            }
        });
    }


}
