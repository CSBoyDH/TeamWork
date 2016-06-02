package com.login;

import com.service.ServiceClient;
import com.service.State;

import java.util.Date;

/**
 * @author xiao
 * @version 1.0
 * 
 * 团队成员类。
 */
public class MemberUser extends User{
	public int teamID;

	/**
	 * 该功能未实现。
	 */
	public MemberUser(){}
	public MemberUser(int userID){
		this.userID = userID;

	}
	public void submitProgress(){
		
	}
	/**
	 * @param taskID
	 * @param isInTime
	 * @return true表示提交成功，false表示提交失败
	 */
	public boolean submitTeamTask(int taskID,boolean isInTime){
		int state=isInTime?State.IN_TIME_CHECK:State.NONIN_TIME_CHECK;
		return ServiceClient.submitTeamTask(taskID,state);
	}
}
