package login.comblueant.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.login.User;

import java.text.SimpleDateFormat;

import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/6.
 */
public class DetailsShareActivity extends Activity {
    private TextView tvSharePublisher;
    private TextView sharePublisher;
    private TextView tvShareDetails;
    private TextView shareDetails;
    private TextView title;
    private TextView right_text;
    private TextView shareTime;
    String share_publisher;
    String share_details;
    String share_time;
    String share_publisherName;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.details_share);
        getData();
        init();
        initView();
    }
    void init(){
        tvSharePublisher = (TextView)findViewById(R.id.tv_details_share_publisher);
        tvShareDetails= (TextView)findViewById(R.id.tv_details_share_details);
        sharePublisher= (TextView)findViewById(R.id.details_share_publisher);
        shareDetails= (TextView)findViewById(R.id.details_share_details);
        title = (TextView)findViewById(R.id.title);
        right_text = (TextView)findViewById(R.id.right_text);

        shareTime = (TextView) findViewById(R.id.details_share_time);

    }
    void initView(){
        title.setText("分享详情");
        right_text.setText("");
        sharePublisher.setText(share_publisherName);
        shareDetails.setText(share_details);
        shareTime.setText(share_time);

    }
    void getData(){
        share_publisherName = getIntent().getStringExtra("sharePublisher");
        share_details= getIntent().getStringExtra("shareDetails");
        share_time = getIntent().getStringExtra("shareTime");



    }
    public void doBack(View view) {
        onBackPressed();
    }
}
