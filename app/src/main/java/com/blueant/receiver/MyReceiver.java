package com.blueant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by DH on 2015/10/24.
 */
public class MyReceiver extends BroadcastReceiver{
    private static final String TAG = "MyReceiver";

    public static ArrayList<String> getMemberList() {
        return memberList;
    }

    private static ArrayList<String> memberList;
    @Override
    public void onReceive(Context context, Intent intent) {

         memberList = intent.getStringArrayListExtra("memberList");

    }


}
