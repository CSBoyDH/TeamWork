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
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blueant.Application.ActivityManagerApplication;
import com.blueant.adapter.ShareAdapter;
import com.blueant.view.XListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.login.User;
import com.service.ServiceClient;
import com.share.Share;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import login.comblueant.activity.DetailsShareActivity;
import login.comblueant.activity.LoginActivity;
import login.comblueant.activity.NoticeActivity;
import login.comblueant.teamwork.R;

/**
 * Created by DH on 2015/10/14.
 */
public class ShareFragment extends Fragment implements XListView.IXListViewListener{
    private XListView share_mListView;

    private ArrayList<HashMap<Object,Object>> data=new ArrayList<HashMap<Object, Object>>();
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    ShareAdapter adapter = null;
    private ImageButton share_setting;
    private ImageButton share_addShare;
    private ImageButton share_notice;
    private RelativeLayout changeAccount_btn;
    private User user;
    private List<Share> shares;
    private static Activity activity;
    Thread thread;
    String getDetails;
    String getTitle ;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    public static Activity getThisActivity() {
        return activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.share, container, false);
    }
    @Override

    public void onAttach(Activity activity) {

        this.activity = activity;
        super.onAttach(activity);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = (User)activity.getIntent().getSerializableExtra("user");
        ActivityManagerApplication.addDestoryActivity(activity);
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                shares = Share.showShare(user.userID);
            }
        }).start();*/
        AsyncTask<Void,Void,List<Share>> ATgetShare = new AsyncTask<Void, Void, List<Share>>() {
            @Override
            protected List<Share> doInBackground(Void... voids) {

                try{
                    shares = Share.showShare(user.userID);
                    if(shares==null){
                        shares = new ArrayList<Share>();
                    }
                } catch (NullPointerException e){}

                return shares;
            }
            protected void onPostExecute(List<Share> Shares){

                geneItems();
                init();
                initRefresh();
                initSlidingMenu();

            }
        };

        ATgetShare.execute();

        Log.d("123", "finsihshowtask");
      /*  geneItems();
        initRefresh();
        init();
        initSlidingMenu();*/

    }

    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundledata = msg.getData();
            int val = bundledata.getInt("result");

            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String shareTime = formatter.format(curDate);
            if(val==1){
                HashMap<Object,Object> item=new HashMap<Object, Object>();
                item.put("publisherName",user.userName);
                item.put("time",shareTime);
                item.put("title",getTitle);
                Log.d("123", user.userName + shareTime + getTitle);
                data.add(item);
                Log.d("123","adapter.noS");
                adapter.notifyDataSetChanged();
                Log.d("123", "adapter.noF");
                Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(activity,"分享失败", Toast.LENGTH_SHORT).show();

            Log.i("123", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };
    Runnable networkShare = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作

            int result;
            Share share = user.createShare(getDetails, getTitle);
            if(share.shareID>0) {
                /*shares = new ArrayList<Share>();*/
                shares.add(share);
                result =1;
            }
            else result = 0;
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putInt("result", result);
            msg.setData(data);
            Log.d("123", "handler");
            mHandler1.sendMessage(msg);

        }
    };
    void init(){
        share_notice = (ImageButton)getView().findViewById(R.id.btn_notice);
        share_setting = (ImageButton)getView().findViewById(R.id.setting);
        share_addShare = (ImageButton)getView().findViewById(R.id.btn_add);
        share_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,NoticeActivity.class);
                startActivity(intent);
            }
        });
        share_addShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        activity);
                builder.setIcon(R.drawable.ic_addshare);
                builder.setTitle("请写入分享内容：");
                // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_add_share, null);
                // 设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                final EditText shareTitle = (EditText) view.findViewById(R.id.add_share_title);
                final MultiAutoCompleteTextView shareContent= (MultiAutoCompleteTextView)view.findViewById(R.id.add_share_content);

                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {


                                getDetails = shareContent.getText().toString().trim();
                                getTitle = shareTitle.getText().toString().trim();
                                thread = new Thread(networkShare);
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
    protected void initSlidingMenu() {
        final SlidingMenu menu = new SlidingMenu(activity);
        /*personal_side_drawer = new DrawerView(this).initSlidingMenu();*/
        share_setting.setOnClickListener(new View.OnClickListener() {
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


        share_mListView = (XListView) getView().findViewById(R.id.share_xListView);
        share_mListView.setPullLoadEnable(true);
        adapter = new ShareAdapter(activity,data);

        share_mListView.setAdapter(adapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
        share_mListView.setXListViewListener(this);
        share_mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               final Intent intent = new Intent(activity, DetailsShareActivity.class);
                System.out.println("position===================》" + position);
                final Share share = shares.get(position-1);//XListViewHeader ,所以-1
                AsyncTask<Void,Void,String> ATGetuserName = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        String name;
                        name =  ServiceClient.getUserName(share.publisherID);
                        if(name.equals("")){
                            name = "出现未知错误";
                        }
                        return name;
                    }
                    protected void onPostExecute(String name){
                        intent.putExtra("sharePublisher",(name));
                        intent.putExtra("shareDetails", share.shareDetails);
                        intent.putExtra("shareTime",formatter.format(share.shareTime));
                        Log.d("share",share.publisherID+ share.shareDetails+share.shareTime);
                        startActivity(intent);
                    }
                };
                ATGetuserName.execute();


            }
        });
        mHandler = new Handler();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_btn:
                this.startActivity(new Intent(activity, LoginActivity.class));
                /*this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                break;

            default:
                break;
        }
    }
    private void geneItems(){
        ListIterator<Share> iterator = null;
       if(shares!=null)iterator = shares.listIterator();
        while(iterator!=null&&iterator.hasNext()){
            Share share = iterator.next();
            HashMap<Object,Object> itemData=new HashMap<Object, Object>();
            itemData.put("publisherName",share.publisherName);
            itemData.put("time",formatter.format(share.shareTime));
            itemData.put("title",share.shareTitle);
            data.add(itemData);
        }
    }

    private void onLoad() {
        share_mListView.stopRefresh();
        share_mListView.stopLoadMore();
        share_mListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                data.clear();
                geneItems();//网络访问
                // mAdapter.notifyDataSetChanged();
                adapter = new ShareAdapter(activity,data);
                share_mListView.setAdapter(adapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();//显示剩余数据
                adapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }
}
