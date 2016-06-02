package com.team;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.service.ServiceClient;
/**
 * @author xiao
 * @version 1.0
 * 
 * 点开一个团队时，调用Team类中的proList()方法，返回Project的实例对象数组
 */
public class Project implements Serializable{
	public int userID;
	public String projectName;
	public int projectID;
	public int teamID;
	public String projectDetails;
	public Date projectPublishTime;
	public Date projectFinishTime;
	public int projectState;
	public Project(){}
	public Project(int userID, int teamID, String projectName, String projectDetails, Date projectFinishTime,Date projectPublishTime) {
		// 
		this.userID=userID;
		this.teamID=teamID;
		this.projectName=projectName;
		this.projectDetails=projectDetails;
		this.projectFinishTime=projectFinishTime;
		this.projectPublishTime=projectPublishTime;
	}
	
	/**
	 * 获得该项目的所有分工任务。
	 * @return List
	 */
	public List<TeamTask> showTeamTask(){
		return ServiceClient.getTeamTask(projectID);
	}
	/**
	 * 获得给定项目projectID的所有任务。
	 * @param projectID
	 * @return List
	 */
	public static List<TeamTask> showTeamTask(int projectID){
		return ServiceClient.getTeamTask(projectID);
	}
	

}
