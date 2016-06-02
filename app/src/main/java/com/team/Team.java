package com.team;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.login.User;
import com.service.ServiceClient;

/**
 * @author xiao
 * @version 1.0
 * 
 * 为了方便使用，将所有属性设为public型<BR>
 * <p>当用户点所有团队的界面是调用静态方法showTeams(int userID)<br>
 * 当用户点开一个团队的时候实例化该类。<br>
 */
public class Team implements Serializable {
	public int teamID;
	public int leaderID;
	public String teamName;
	/**
	 * 版本1.0 此字段请勿使用
	 */
	public List<String> memberNames;
	public Date createTime;
	public Team(){};
	/**
	 * 创建团队时所用的构造方法，teamName由用户输入，leaderID为该用户userID。
	 * @param teamName
	 * @param leaderID
	 */
	public Team(String teamName, int leaderID) {
		// TODO Auto-generated constructor stub
		this.leaderID=leaderID;
		this.teamName=teamName;
		this.createTime = Calendar.getInstance().getTime();
	}
	
	/**
	 * 设置teamID值
	 * @param teamID
	 */
	public void setTeamID(int teamID) {
		// TODO Auto-generated method stub
		this.teamID=teamID;
	}
	/**
	 * 显示给定teamID团队的所有项目
	 * @param teamID
	 * @return List
	 */
	public static List<Project> proList(int teamID){
		return ServiceClient.getTeamProjects(teamID);
	}
	/**
	 * 显示该团队对象的所有项目，选中点开某一个团队时查看团队的所有项目。
	 * @return List类的对象
	 */
	public List<Project> proList(){
		return ServiceClient.getTeamProjects(teamID);
	}
	/**
	 * 在点开团队页面调用。<br>
	 * 显示给定userID用户的所有团队
	 * @param userID
	 * @return List
	 */
	public static List<Team> showTeams(int userID){
		return ServiceClient.getTeam(userID);
	}
	
	/**
	 * 在点开团队页面调用。<br>
	 * 显示给定teamID团队的所有成员
	 * @param teamID
	 * @return List
	 */
	public static List<User> getMembers(int teamID){
		return ServiceClient.getMembers(teamID);
	}
	/**
	 * 显示该团队对象的所有成员
	 * @return List
	 */
	public List<User> getMembers(){
		return ServiceClient.getMembers(teamID);
	}
}
