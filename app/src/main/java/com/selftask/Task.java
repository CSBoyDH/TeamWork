package com.selftask;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.service.ServiceClient;
/**
 * @author xiao
 * @version 1.0
 */
public class Task implements Serializable{
	public int taskID;
	public String taskName;
	public Date taskPublishTime;
	public Date taskFinishTime;
	public String taskDetails;
	public int taskState;
	public int userID;
	public String userName;
	public Task(){}
	public Task(String taskName, Date taskPublishTime, Date taskFinishTime, String taskDetails, int taskState,
			int userID) {
		
		this.taskName = taskName;
		this.taskPublishTime=taskPublishTime;
		this.taskFinishTime=taskFinishTime;
		this.taskDetails=taskDetails;
		this.taskState=taskState;
		this.userID=userID;
	}

	/**
	 * 获得该用户所有个人任务。
	 * @param userID
	 * @return List
	 * */
	public static List<Task> showTask(int userID){
		return ServiceClient.getSelfTask(userID);
	}
}
