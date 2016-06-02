package com.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import com.login.LeaderUser;
import com.login.MemberUser;
import com.login.User;

/**
 * Created by lenovo on 2015/10/14.
 */
public class Utils {
    public static void fatherToChild(User father,LeaderUser child) throws Exception{
    	child.contactWay=father.contactWay;
    	child.createTime=father.createTime;
    	child.hasTeam=father.hasTeam;
    	child.userEmail=father.userEmail;
    	child.userID=father.userID;
    	child.userName=father.userName;
    	child.userPassword=father.userPassword;
    }
    public static void fatherToChild(User father,MemberUser child) throws Exception{
    	child.contactWay=father.contactWay;
    	child.createTime=father.createTime;
    	child.hasTeam=father.hasTeam;
    	child.userEmail=father.userEmail;
    	child.userID=father.userID;
    	child.userName=father.userName;
    	child.userPassword=father.userPassword;
    }
    public static String getStateDescription(int state){
        String result ;
        switch (state){
            case State.NONIN_TIME_NONFINISHED:
                result = "超时未完成";
                break;
            case State.NONIN_TIME_FINISHED:
                result="超时完成";
                break;
            case State.IN_TIME_FINISHED:
                result="按时完成";
                break;
            case State.IN_TIME_NONFINISHED:
                result="未超时进行中";
                break;
            case State.IN_TIME_CHECK:
                result="按时审查";
                break;
            case State.NONIN_TIME_CHECK:
                result="超时审查中";
                break;
            default:result="未知状态";
        }
        return result;
    }
}
