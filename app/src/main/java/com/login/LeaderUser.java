package com.login;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.service.ServiceClient;
import com.service.State;
import com.team.Project;
import com.team.TeamTask;
/**
 * @author xiao
 * @version 1.0
 * 团队创建人类。在User类中有判断是否为队长，是否为团队成员的方法。
 */
public class LeaderUser extends User{

	public int teamID;

	public TeamTask distributeTask(String taskName,int projectID,String taskDetails,String accepterName,Date taskFinishTime){
		TeamTask task = new TeamTask(taskName,teamID,projectID,taskDetails,accepterName,State.IN_TIME_NONFINISHED,Calendar.getInstance().getTime(),taskFinishTime);
//		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			task.taskFinishTime = sdf.parse(taskFinishTime);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			//������Ҫ����һ�£��д��Ż���
//			e.printStackTrace();
//		}
		int taskId = ServiceClient.uploadTeamTask(task);
		if(taskId==-1)return null;
		return task;
	}
	/**
	 * 
	 * 团队leader能够创建任务<br>创建前需要实例化的对象
	 * @param projectName
	 * @param projectDetails
	 * @param projectFinishTime
	 * @param projectStartTime
	 * @return pro 项目project对象
	 */
	public Project createPro(String projectName,String projectDetails,String projectFinishTime,String projectStartTime){

		Date finish = null;
		Date start = null;
		try {
			finish = ServiceClient.formatter.parse(projectFinishTime);
			start = ServiceClient.formatter.parse(projectStartTime);
		} catch (ParseException e) {
			start = Calendar.getInstance().getTime();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(start);
			calendar.add(Calendar.DAY_OF_YEAR,100);//默认一百天
			finish=calendar.getTime();
			e.printStackTrace();
		}
		Project pro = new Project(userID,teamID,projectName,projectDetails,finish,start);
		pro.projectState=1;
		int id=ServiceClient.uploadProject(pro);
		if(id==-1)return null;
		pro.projectID=id;
		return pro;
	}
	/**
	 * 邀请成员，根据用户名进行邀请。<br>参考User的isValidate()和getUserID()方法。
	 * @param userName
	 * @return true表示成功邀请，false表示邀请失败。
	 */
	public boolean inviteUser(String userName){
		return ServiceClient.inviteMember(userName, teamID, userID);
	}
	public boolean deleteProject(int projectId){
		return ServiceClient.deleteProject(projectId);
	}
	public boolean deleteTeamTask(int teamTaskID){
		return ServiceClient.deleteTeamTask(teamTaskID);
	}
}
