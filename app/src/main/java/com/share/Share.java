package com.share;

import java.util.Date;
import java.util.List;

import com.service.ServiceClient;
/**
 * @author xiao
 * @version 1.0
 */
public class Share {
	public int shareID;
	public int publisherID;
	public String publisherName;
	public String shareTitle;
	public String shareDetails;
	public Date shareTime;

	public Share(){}
	public Share(int userID, String shareDetails, String shareTitle, Date time) {
		// TODO Auto-generated constructor stub
		this.publisherID=userID;
		this.shareDetails=shareDetails;
		this.shareTitle=shareTitle;
		this.shareTime=time;
	}

	/**
	 * 获得用户能够看到的所有动态。
	 * @param userID
	 * @return List
	 * */
	public static List<Share> showShare(int userID){
		return ServiceClient.getShare(userID);
	}
}
