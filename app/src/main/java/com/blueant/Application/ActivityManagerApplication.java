package com.blueant.Application;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by DH on 2015/11/23.
 */
public class ActivityManagerApplication extends Application{
    private static List<Activity> destoryList = new LinkedList<Activity>();

    private ActivityManagerApplication() {
    }

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */

    public static void addDestoryActivity(Activity activity) {
        destoryList.add(activity);
    }
    /**
     *销毁指定Activity
     */
    public static void destoryActivity() {
        for (Activity activity : destoryList) {
            activity.finish();
        }
        if (destoryList.size() == 0)
            destoryList.clear();
    }
}
