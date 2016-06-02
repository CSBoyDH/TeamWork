package com.team;

import java.io.Serializable;
import java.util.Date;

import com.service.ServiceClient;
/**
 * @author xiao
 * @version 1.0
 * 
 * 用户点开一条团队任务后，调用类Project中的showTeamTask()方法，返回TeamTask类的实例对象数组。
 */
public class TeamTask implements Serializable{
	public TeamTask(String taskName,int teamID, int projectID, String taskDetails, String accepterName, int taskState, Date taskDTime, Date taskFinishTime) {
		// TODO Auto-generated constructor stub
		this.taskName = taskName;
		this.teamID=teamID;
		this.projectID=projectID;
		this.taskDetails = taskDetails;
		this.accepterID = ServiceClient.getUserID(accepterName);
		this.distributeTime=taskDTime;
		this.taskState = taskState;
		this.taskFinishTime=taskFinishTime;
	}
	public TeamTask(){}
	public String taskName;
	public int projectID;
	public int taskID;
	public String taskDetails;
	public int accepterID;
	public int teamID;
	public Date distributeTime;
	public Date taskFinishTime;
	public int taskState;
	
	/**
	 * 团队任务的具体内容
	 * 
	 * @return taskDetails
	 */
	public String showTeamTaskDetails(){
		return taskDetails;
	}
	
	
}
