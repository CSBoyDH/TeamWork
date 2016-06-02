package com.service;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.login.User;
import com.selftask.Task;
import com.share.Share;
import com.team.Invitation;
import com.team.Project;
import com.team.Team;
import com.team.TeamTask;

/**
 * 该类负责处理与服务器的交互。<br>
 * 类中所有方法皆为静态方法。
 * @author xiao
 * @version 1.0
 */
public class ServiceClient {
	private final static String MAIN_IP="http://115.28.89.176/";
	private final static String MAIN_PORT="";
	private final static String SHARE_IP="http://115.28.89.176/";
	private final static String SHARE_PORT="";
	private final static String SELFMANAGE_IP="http://115.28.89.176/";
	private final static String SELFMANAGE_PORT="";
	private final static String TEAMWORK_IP="http://115.28.89.176/";
	private final static String TEAMWORK_PORT="";
	private final static int ANSWERTIME=10;
	private final static String WEBAPP="TeamWork/";
	public final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ServiceClient(){}//禁用构造方法
	/**
	 * 发出HTTP请求，并以XML文档格式解析返回实体内容。
	 * @param path
	 * @param type
	 * @return 返回XML文档中的return节点下的节点列表。
	 * @throws Exception
	 */
	private static NodeList getNodeList(String path,String type) throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom;
		URL url = new URL(path);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.connect();
		InputStream is = httpURLConnection.getInputStream();
		InputSource isrc = new InputSource(new InputStreamReader(is,"utf-8"));
//		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
		if(httpURLConnection.getResponseCode()==200){
			dom = db.parse(isrc);
			NodeList nodeList = dom.getElementsByTagName("rar");
			nodeList = nodeList.item(0).getChildNodes();
			if(nodeList.item(0)==null)return null;
			NodeList methodList = nodeList.item(0).getChildNodes();//Method下的节点列表
			if(!methodList.item(0).getTextContent().equals(type))return null;//初步验证
			httpURLConnection.disconnect();
			return methodList.item(2).getChildNodes();//返回Return下的节点列表
		}

		return null;
	}
	
	/**
	 * 发出HTTP请求，并以String类型解析返回实体内容。
	 * @param path
	 * @return 返回服务器response的实体内容。
	 */
	private static String request(String path){
		URL url = null;
		try {
			url = new URL(path);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
        try {
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.connect();
			if(httpURLConnection.getResponseCode() == 200)
            {
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                BufferedReader br=new BufferedReader(inputStreamReader);
                char []buffer = new char[1024];
                
                int len;
                while((len = br.read(buffer))!=-1)
                {
                    sb.append(new String(buffer, 0, len));
                }
    			httpURLConnection.disconnect();
            }
		} catch (IOException e) {
			sb.setLength(0);
		}
        return sb.toString();
	}
	
//	public static boolean checkUser(String userName) {
//		return false;
//	}

	/**
	 * 注册用户。
	 * @param userName
	 * @param password
	 * @param email
	 * @param date
	 * @param contact
	 * @return 服务器返回的userID
	 */
	public static int register(String userName, String password, String email, Date date, String contact) {
		String path=MAIN_IP + WEBAPP +"?action=register" +"&userName=" + userName+"&password="+ password
				+ "&email="+ email+ /*"&regTime="+ formatter.format(date)+ */"&tel="+ contact ;
		String result=request(path);
		if(result.equals(""))return -2;
		return Integer.parseInt(result);
	}
	
	public static int checkUser(String userName, String password) {
		// TODO Auto-generated method stub
		
        String path = MAIN_IP + WEBAPP +"?action=checkUser"+"&userName=" + userName + "&password=" + password;
        String result = request(path);
        if(result.equals(""))return -2;//-2表示数据丢失。
        return Integer.parseInt(result);
	}

	public static boolean checkLeader(int userID,int teamID) {
        String path = MAIN_IP + WEBAPP +"?action=checkLeader" +"&userID=" + userID +"&teamID="+teamID;
        String result = request(path);
        if(result.equals(""));//当数据为空时，数据有可能丢失
		return result.compareToIgnoreCase("true")==0;
	}

	public static boolean submitTeamTask(int taskID,int state){
        String path = TEAMWORK_IP + WEBAPP +"?action=submitTeamTask&taskID="+taskID+"&changeToState="+state;
        String result = request(path);
        if(result.equals(""));
		return result.compareToIgnoreCase("true")==0;
	}
	public static boolean checkMember(int userID) {
        String path = MAIN_IP + WEBAPP +"?action=checkMember" +"&userID=" + userID ;
        String result = request(path);
        if(result.equals(""));
        //存在缺陷.出现其他问题后，仍然回复FALSE，即“不是队长”
		return result.compareToIgnoreCase("true")==0;
	}

	public static int uploadShare(Share share) {
        String path = SHARE_IP + WEBAPP +"?action=uploadShare" +"&userID=" + share.publisherID 
        		+"&shareTitle="+share.shareTitle+"&shareDetails="+share.shareDetails
        		+"&shareTime="+formatter.format(share.shareTime).replace(" ","%20");
        String result = request(path);
        if(result.equals(""))return -2;
        return Integer.parseInt(result);
	}

	public static int uploadProject(Project pro) {
		String path = TEAMWORK_IP+WEBAPP+"?action=uploadProject&userID="+pro.userID
				+"&projectName="+pro.projectName+"&teamID="+pro.teamID
				+"&projectDetails="+pro.projectDetails+"&projectCurrentTime="+formatter.format(pro.projectPublishTime).replace(" ","%20")
				+"&projectFinishTime="+formatter.format(pro.projectFinishTime).replace(" ","%20")+"&projectState="+pro.projectState;
		String result = request(path);
        if(result.equals(""))return -2;
		return Integer.parseInt(result);
	}

	public static int uploadTeam(Team team) {
		String path = TEAMWORK_IP+WEBAPP+"?action=uploadTeam&teamName="+team.teamName
				+"&leaderID="+team.leaderID+"&createTime="+formatter.format(team.createTime).replace(" ","%20");
		String result = request(path);
        if(result.equals(""))return -2;
		return Integer.parseInt(result);
	}

	public static int uploadSelfTask(Task task) {
		String path = SELFMANAGE_IP+WEBAPP+"?action=uploadSelfTask&taskName="+task.taskName
				+"&taskPublishTime="+formatter.format(task.taskPublishTime).replace(" ","%20")+"&taskFinishTime="+formatter.format(task.taskFinishTime).replace(" ","%20")
				+"&taskDetails="+task.taskDetails+"&taskState="+task.taskState+"&userID="+task.userID;
		String result = request(path);
        if(result.equals(""))return -2;
		return Integer.parseInt(result);
	}


	
	public static int uploadTeamTask(TeamTask task) {
		String path = TEAMWORK_IP+WEBAPP+"?action=uploadTeamTask&projectID="+task.projectID+"&taskDetails="+task.taskDetails
				+"&accepterID="+task.accepterID+"&teamID="+task.teamID+"&distributeTime="+formatter.format(task.distributeTime).replace(" ","%20")
				+"&taskFinishTime="+formatter.format(task.taskFinishTime).replace(" ","%20")+"&taskState="+task.taskState+"&taskName="+task.taskName;
		String result = request(path);
        if(result.equals(""))return -2;
		return Integer.parseInt(result);
	}

	public static boolean addMember(int userID, int teamID) {
		String path = TEAMWORK_IP+WEBAPP+"?action=addMember&userID="+userID+"&teamID="+teamID;
		String result = request(path);
		if(result.equals(""));
		return result.compareToIgnoreCase("true")==0;
	}
	
	public static boolean inviteMember(String userName,int teamID,int leaderID){
		String path = TEAMWORK_IP+WEBAPP+"?action=inviteMember&userName="+userName+"&teamID="+teamID
				+"&userID="+leaderID;
		String result = request(path);
		if(result.equals(""));
		Log.d("invite","inviteMember"+result);
		return result.compareToIgnoreCase("true")==0;
	}
	
	public static List<Share> getShare(int userID) {
			
		String path = SELFMANAGE_IP + WEBAPP +"?action=getShare&userID="+userID;
		NodeList returnList =null;
		try {
			returnList = getNodeList(path,"Share[]");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(returnList==null)return null;
		
		List<Share> list = new ArrayList<Share>();
		Element sharesNode = (Element)returnList.item(0);
		int len = Integer.parseInt(sharesNode.getAttribute("num"));
		NodeList sharesList = sharesNode.getChildNodes();//Node.hasChildNodes()
		for(int i = 0;i < len ;i++){
			Share share = new Share();
			Element shareNode = (Element)sharesList.item(i);
			NodeList shareList = shareNode.getChildNodes();
			share.shareID=Integer.parseInt(shareList.item(0).getTextContent());
			share.publisherID=Integer.parseInt(shareList.item(1).getTextContent());
			share.shareTitle=shareList.item(2).getTextContent();
			share.shareDetails=shareList.item(3).getTextContent();
			share.publisherName=shareList.item(5).getTextContent();
			try {
				share.shareTime=formatter.parse(shareList.item(4).getTextContent());
			} catch (ParseException e) {
				share.shareTime=null;
			}
			list.add(share);
		}
		return list;
	}

	public static List<Task> getSelfTask(int userID) {
		String path = SELFMANAGE_IP+WEBAPP+"?action=getSelfTask&userID="+userID;
		NodeList returnList =null;
		try {
			returnList = getNodeList(path,"Task[]");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(returnList==null)return null;
		
		List<Task> list = new ArrayList<Task>();
		Element tasksNode = (Element)returnList.item(0);
		int len = Integer.parseInt(tasksNode.getAttribute("num"));
		NodeList tasksList = tasksNode.getChildNodes();//tasksNode.hasChildNodes()
		for(int i = 0;i < len ;i++){
			Task task = new Task();

			Element taskNode = (Element)tasksList.item(i);
			NodeList taskList = taskNode.getChildNodes();
			task.taskName = taskList.item(1).getTextContent();
			task.taskDetails= taskList.item(4).getTextContent();
			task.taskState=Integer.parseInt( taskList.item(5).getTextContent());
			task.userID=Integer.parseInt(taskList.item(6).getTextContent());
			task.taskID = Integer.parseInt(taskList.item(0).getTextContent());
			task.userName=taskList.item(7).getTextContent();
			try {
				task.taskPublishTime=formatter.parse( taskList.item(2).getTextContent());
			} catch (ParseException e1) {
				task.taskPublishTime=null;
			}
			try {
				task.taskFinishTime=formatter.parse( taskList.item(3).getTextContent());
			}  catch (ParseException e) {
				task.taskFinishTime=null;
			}
			list.add(task);
		}
		return list;
		
	}

	public static List<TeamTask> getTeamTask(int projectID) {
		String path = TEAMWORK_IP+WEBAPP+"?action=getTeamTask&projectID="+projectID;
		NodeList nodeList = null;
		try {
			nodeList = getNodeList(path,"TeamTask[]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			//COMPLETE
		}
		if(nodeList == null)return null;		
		
		
		
		
		List<TeamTask> list = new ArrayList<TeamTask>();
		Element projectsNode = (Element)nodeList.item(0);
		int len = Integer.parseInt(projectsNode.getAttribute("num"));
		NodeList projectsList = projectsNode.getChildNodes();//projectsNode.hasChildNodes()
		
		for(int i = 0;i<len;i++){
			Element projectNode = (Element)projectsList.item(i);
			NodeList projectList = projectNode.getChildNodes();
			Element teamTasksNode = (Element)projectList.item(0);
			
			int len2 = Integer.parseInt(teamTasksNode.getAttribute("num"));//teamTasks子节点数量，即teamTask数量
			NodeList teamTasksList = teamTasksNode.getChildNodes();//teamTasksNode.hasChildNodes()
			for(int k = 0; k<len2; k++){
				TeamTask tt = new TeamTask();

				Element teamTaskNode = (Element)teamTasksList.item(k);
				NodeList teamTaskList = teamTaskNode.getChildNodes();
				tt.taskName = teamTaskList.item(1).getTextContent();
				tt.taskID = Integer.parseInt(teamTaskList.item(0).getTextContent());
				tt.taskDetails = teamTaskList.item(2).getTextContent();
				tt.projectID = Integer.parseInt(teamTaskList.item(3).getTextContent());
				try {
					tt.distributeTime = formatter.parse(teamTaskList.item(4).getTextContent());
				}catch (ParseException e) {
					tt.distributeTime = null;
					e.printStackTrace();
				}
				try {
					tt.taskFinishTime = formatter.parse(teamTaskList.item(5).getTextContent());
				} catch (ParseException e) {
					tt.taskFinishTime = null;
					e.printStackTrace();
				}
				try{
					tt.taskState = Integer.parseInt(teamTaskList.item(6).getTextContent());
				}catch(NumberFormatException e){
					e.printStackTrace();
					tt.taskState=0;
				}
				try{
					tt.accepterID = Integer.parseInt(teamTaskList.item(7).getTextContent());
				}catch(NumberFormatException e){
					e.printStackTrace();
					tt.accepterID=0;
				}
				try{
					tt.teamID = Integer.parseInt(teamTaskList.item(8).getTextContent());
				}catch(NumberFormatException e){
					e.printStackTrace();
					tt.teamID=0;
				}
				
				
				list.add(tt);
			}
		}
		return list;

	}


	public static List<Project> getTeamProjects(int teamID) {
		String path = TEAMWORK_IP+WEBAPP+"?action=getTeamProjects&teamID="+teamID;
		NodeList nodeList =null;
		try {
			nodeList = getNodeList(path,"Project[]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(nodeList == null) return null;
		
		List<Project> list = new  ArrayList<Project>();
		Element projectsNode = (Element)nodeList.item(0);
		int len = Integer.parseInt(projectsNode.getAttribute("num"));
		NodeList projectsList = projectsNode.getChildNodes();//projectsNode.getChildNodes()
		
		for(int i = 0; i<len; i++)
		{
			Element projectNode = (Element)projectsList.item(i);
			NodeList projectList = projectNode.getChildNodes();
			Project project = new Project();

			project.projectName = projectList.item(2).getTextContent();
			project.projectDetails = projectList.item(4).getTextContent();
			project.projectID = Integer.parseInt(projectList.item(0).getTextContent());
			project.userID = Integer.parseInt(projectList.item(1).getTextContent());
			project.teamID = Integer.parseInt(projectList.item(3).getTextContent());
			project.projectState = Integer.parseInt(projectList.item(8).getTextContent());
			try{
				project.projectPublishTime = formatter.parse(projectList.item(5).getTextContent());	
			}catch(ParseException e){
				project.projectPublishTime = null;
			}
			try{
				project.projectFinishTime = formatter.parse(projectList.item(6).getTextContent());	
			}catch(ParseException e){
				project.projectFinishTime = null;
			}
			list.add(project);
		}
		return list;
	}

	public static List<Team> getTeam(int userID) {
		String path = TEAMWORK_IP+WEBAPP+"?action=getTeam&userID="+userID;
		NodeList nodeList =null;
		try {
			nodeList = getNodeList(path,"Team[]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(nodeList == null) return null;
		
		List<Team>list = new ArrayList<Team>();
		Element teamsNode = (Element)nodeList.item(0);
		int len = Integer.parseInt(teamsNode.getAttribute("num"));
		NodeList teamsList = teamsNode.getChildNodes();//teamsNode.getChildNodes()
		
		for(int i = 0; i<len; i++){
			Element teamNode = (Element)teamsList.item(i);
			NodeList teamList = teamNode.getChildNodes();
			Team team = new Team();

			team.teamName = teamList.item(1).getTextContent();
			team.teamID = Integer.parseInt(teamList.item(0).getTextContent());
			team.leaderID = Integer.parseInt(teamList.item(2).getTextContent());
			try{
				team.createTime = formatter.parse(teamList.item(3).getTextContent());	
			}catch(ParseException e){
				team.createTime = null;
			}			
			list.add(team);		
		}
		return list;
	}

	public static int getUserID(String userName) {
		String path = MAIN_IP + WEBAPP +"?action=getUserID&userName=" + userName;;
		String result = request(path);
		if(result.equals(""))return -2;
		return Integer.parseInt(result);
	}

	public static boolean submitTask(int taskID, int state,int userID) {
		String path = SELFMANAGE_IP+WEBAPP+"?action=submitTask&taskID="+taskID+"&taskState="+state
				+"&userID="+userID;
		String result = request(path);
		if(result.equals(""));
		return result.compareToIgnoreCase("true")==0;
	}	
	
	public static List<User> getMembers(int teamID){
		String path = TEAMWORK_IP+WEBAPP+"?action=getMembers&teamID="+teamID;
		NodeList nodeList =null;
		try {
			nodeList = getNodeList(path,"Member[]");
		} catch (Exception e) {
			return null;
		}
		List<User>list = new ArrayList<User>();
		Element teamsNode = (Element)nodeList.item(0);
		NodeList teamsList = teamsNode.getChildNodes();//teamsNode.getChildNodes()
		Element teamNode = (Element)teamsList.item(0);
		NodeList membersList = teamNode.getChildNodes();
		Element membersNode = (Element)membersList.item(0);
		int len = Integer.parseInt(membersNode.getAttribute("num"));
		NodeList memberList = membersNode.getChildNodes();

		for(int i=0;i<len;i++){
			Element memberNode = (Element)memberList.item(i);
			NodeList member = memberNode.getChildNodes();
			User user = new User();
			user.contactWay = member.item(2).getTextContent();
			user.userEmail = member.item(3).getTextContent();
			user.userName = member.item(0).getTextContent();
			user.userID = Integer.parseInt(member.item(1).getTextContent());
			list.add(user);
		}
		
		return list;
		
	}
	public static boolean deleteProject(int projectID){
		String path = TEAMWORK_IP+WEBAPP+"?action=deleteProject&projectID="+projectID;
		String result = request(path);
		if(result.equals(""));
		return result.compareToIgnoreCase("true")==0;
	}
	public static String getUserName(int userID){
		String path = MAIN_IP+WEBAPP+"?action=getUserName&userID="+userID;
		return request(path);
	}
	public static boolean deleteTeamTask(int teamTaskID){
		String path = TEAMWORK_IP+WEBAPP+"?action=deleteTeamTask&teamTaskID="+teamTaskID;
		String result = request(path);
		if(result.equals(""));
		return result.compareToIgnoreCase("true")==0;
	}
	public static List<Invitation> getInvite(int userID){
		String path = TEAMWORK_IP+WEBAPP+"?action=getInvitation&userID="+userID;
		NodeList nodeList =null;
		try {
			nodeList = getNodeList(path,"Invitation[]");
		} catch (Exception e) {
			return null;
		}
		List<Invitation> list = new ArrayList<Invitation>();
		Element invitationsNode = (Element)nodeList.item(0);
		int len = Integer.parseInt(invitationsNode.getAttribute("num"));
		NodeList invitationList = invitationsNode.getChildNodes();
		
		for(int i=0;i<len;i++){
			Element invitationNode=(Element) invitationList.item(i);
			Invitation invite = new Invitation();
			invite.leaderName=invitationNode.getChildNodes().item(0).getTextContent();
			invite.teamID=Integer.parseInt(invitationNode.getChildNodes().item(2).getTextContent());
			invite.teamName=invitationNode.getChildNodes().item(1).getTextContent();
			try {
				invite.inviteTime=formatter.parse(invitationNode.getChildNodes().item(3).getTextContent());
			} catch (Exception e) {
				invite.inviteTime=new Date();
			}
			list.add(invite);
		}
		return list;
	}
}
