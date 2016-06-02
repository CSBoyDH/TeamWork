package com.team;

import java.util.Date;
import java.util.List;

import com.service.ServiceClient;
/**
 * @author xiao
 * @version 1.0
 */
public class Invitation {
	public String teamName;
	public int teamID;
	public String leaderName;
	public Date inviteTime;
	/**
	 * 该方法需要详细斟酌。
	 */
	public static List<Invitation> getInv(int userID){
		//
		return ServiceClient.getInvite(userID);
	}
}
