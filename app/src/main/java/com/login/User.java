package com.login;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.selftask.Task;
import com.service.ServiceClient;
import com.service.State;
import com.share.Share;
import com.team.Team;
/**
 * User类，用于管理用户信息和实现用户的操作功能。
 * @author xiao
 * @version 1.0
 * 
 * 
 */
public class User implements Serializable{


	public int userID;
	public String userName;
	public String userPassword;
	public String userEmail;
	public Date createTime;
	public String contactWay;
	public boolean hasTeam;//是否属于团队。
	public User(){}
	
	
	/**
	 * 登录时可用该构造方法实例化一个对象，再使用login()方法
	 * @param userName
	 * @param userPassword
	 */
	public User(String userName,String userPassword){
		this.userName=userName;
		this.userPassword=userPassword;
		
	}
	/**
	 * 注册用户时可用该构造方法再使用register()方法;<br>
	 * 或者直接使用静态register(userName,userPassword,userEmail,contactWay)方法。
	 * @param userName
	 * @param userPassword
	 * @param userEmail
	 * @param contactWay
	 */
	public User(String userName,String userPassword,String userEmail,String contactWay){
		this.userName=userName;
		this.userPassword=userPassword;
		this.userEmail = userEmail;
		this.contactWay=contactWay;
	}
//	public void setEmail(String email){
//		userEmail=email;
//	}
//	public String getEmail(){
//		return userEmail;
//	}
//	public void setID(int userID){
//		this.userID=userID;
//	}
	public int getID(){
		return userID;
	}
//	public Date getCreateTime(){
//		return createTime;
//	}
//	public void setContactWay(String contactWay){
//		this.contactWay=contactWay;
//	}
//	public String getContactWay(){
//		return contactWay;
//	}

	
	/**
	 * 是否存在该User对象用户。<br>
	 * 可用于在邀请成员（LearUser类里的inviteMember()方法）前检验输入的用户名是否有效。
	 * @return true 表示该User对象存在。false表示该对象不存在。
	 */
	public boolean isValidate(){
		if(ServiceClient.getUserID(userName)>0)return true;
		return false;
	}
	
	/**
	 * 登陆，登录成功后userID就不会空(0)了。
	 * @return true表示登陆成功.false表示失败，造成原因可能有密码帐户不匹配，账户不存在，网络未连接等原因。
	 */
	public boolean login(){
		if(userName.equals("")||userPassword.equals(""))return false;
		int i=ServiceClient.checkUser(userName,userPassword);
		if(i>0){
			userID=i;
			hasTeam = ServiceClient.checkMember(i);
			return true;
		}
		return false;
	}
	/**
	 * 检验该用户对象是否为一个团队的队长
	 * @param teamID
	 * @return true false
	 */
	public boolean isLeader(int teamID){
		if(ServiceClient.checkLeader(userID,teamID))return true;
		return false;
	}
//	/**
//	 * 是否有团队。<br>
//	 * 提示：可以直接使用Team类的getTeam方法，返回list中长度为零说明没有团队。这样可以减少与服务器通信次数。
//	 * 登陆或注册后该方法有效。
//	 * @return true false
//	 */
//	public boolean hasTeam(){
//		if(hasTeam=ServiceClient.checkMember(userID))return true;
//		return false;
//	}
	/**
	 * 接受邀请。
	 * @param teamID
	 * @return true表示加入团队成功，false表示加入失败
	 */
	public boolean acceptInvite(int teamID){
		return ServiceClient.addMember(userID, teamID);
	}
	
	/**
	 * 创建团队<br>
	 * 创建队长为给定leaderID的团队。
	 * @param teamName
	 * @param leaderID
	 * @return team Team对象
	 */
	public static Team createTeam(String teamName,int leaderID){
		Team team = new Team(teamName,leaderID);
		int id = ServiceClient.uploadTeam(team);
		if(id==-1)return null;
		team.setTeamID(id);
		return team;
	}
	/**
	 * 
	 * 创建团队<br>
	 * 创建该user对象作为leaderID的团队，只需要给出 团队名的参数。
	 * @param teamName
	 * @return team Team对象
	 */
	public Team createTeam(String teamName){
		Team team = new Team(teamName,userID);
		int id = ServiceClient.uploadTeam(team);
		if(id==-1)return null;
		team.setTeamID(id);
		return team;
	}
	

	public Share createShare(String shareDetails,String shareTitle){
		Share share =new Share(userID,shareDetails,shareTitle,Calendar.getInstance().getTime());
		int id = ServiceClient.uploadShare(share);
		if(id==-1)return null;
		share.shareID=id;
		return share;
	}
	
	/**
	 * 创建个人任务。
	 * 
	 * @param taskName
	 * @param taskFinishTime
	 * @param taskDetails
	 * @return task 
	 */
	public Task createSelfTask(String taskName,Date taskFinishTime,String taskDetails){
		Task task = new Task(taskName, Calendar.getInstance().getTime(), taskFinishTime, taskDetails, State.IN_TIME_NONFINISHED, userID);
		int id = ServiceClient.uploadSelfTask(task);
		if(id==-1)return null;
		task.taskID=id;
		return task;
	}
	/**
	 * 提交个人任务（确认完成），需要前端判断是否按时完成。
	 * @param taskID
	 * @param isInTime
	 * @return true 代表提交成功，false代表提交失败。
	 */
	public boolean submitTask(int taskID,boolean isInTime){
		int state=isInTime?State.IN_TIME_FINISHED:State.NONIN_TIME_FINISHED;
		return ServiceClient.submitTask(taskID,state,userID);
	}
	/**
	 * 用户注册，参考构造方法。注册成功后服务端返回的userID会写入属性userID.（即成功后，userID可用）
	 * @return true表示注册成功，false表示注册失败。
	 */
	public boolean register(){
		int i = ServiceClient.register(userName, userPassword, userEmail, new Date(), contactWay);
		
		if(i!=0){
			hasTeam = ServiceClient.checkMember(i);
			return true;
		}
		return false;
	}
	/**
	 * 用户注册，静态方法。如果成功则返回用户的userID，失败则返回0.
	 * @param userName
	 * @param userPassword
	 * @param userEmail
	 * @param contactWay
	 * @return userID注册成功就返回用户的ID
	 */
	public static int register(String userName,String userPassword,String userEmail,String contactWay){
		return ServiceClient.register(userName, userPassword, userEmail, new Date(), contactWay);
	}
	public static String getUserName(int userID){
		return ServiceClient.getUserName(userID);
	}
}
