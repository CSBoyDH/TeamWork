package com.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;

import com.login.User;
import com.selftask.Task;
import com.share.Share;
import com.team.Project;
import com.team.Team;
import com.team.TeamTask;

public class ConnectTest {

	public static void main(String[] args)throws Exception{
/*		System.out.println(ServiceClient.checkUser("dh", "123"));
		System.out.println(ServiceClient.register("xiaochurong2", "123", "123213@sad.com", new Date(), "12312412"));
		User user = new User("dh","123");
		System.out.println(user.login());*/
		/*Task task = new Task("ceshi",new Date(),new Date(),"ceshigongneng",1,1);
		System.out.println(ServiceClient.uploadSelfTask(task));*/
		getTeamTask();
		//deleteProjectTest();
		//getUserNameTest();
		//getTeam();
		//getSelfTask();
		//getMembers();
		//getTeamTask();
		//submitTask();
		//getProject();
//		ServiceClient.getMembers(1);
		List<Task> listTask = ServiceClient.getSelfTask(1);
		List<Share> listShare = ServiceClient.getShare(3);
		List<Project> listProject = ServiceClient.getTeamProjects(1);
		List<Team> listTeam = ServiceClient.getTeam(1);
		List<TeamTask> listTeamTask = ServiceClient.getTeamTask(1);
		List<User> listmember = ServiceClient.getMembers(1);
		System.out.println();
/*	String path="http://115.28.89.176/TeamWork/?action=getTeamTask&userID=1&projectID=1";

		URL url = new URL(path);
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.connect();
//		FileOutputStream file = new FileOutputStream("abc");
		InputStream is = http.getInputStream();
		String result;
		
		while((result=br.readLine())!=null)file.write(result.getBytes());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom;
		InputSource isrc = new InputSource(new InputStreamReader(is,"utf-8"));
		//dom = db.parse(is);
//		dom = db.parse(new File("abc"));
		dom=db.parse(isrc);
		Element ele = dom.getDocumentElement();
		System.out.print(ele.getChildNodes().item(1).getChildNodes().item(1).getTextContent());*/

//		BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(),"utf-8"));
//		SAXBuilder sb = new SAXBuilder();
//		CharBuffer cb = CharBuffer.allocate(1024*1024);
//		br.read(cb);
//		String result = cb.toString();
//	    if(null != result && !"".equals(result)){  
//	        if(result.indexOf("<") != -1 && result.lastIndexOf(">") != -1 && result.lastIndexOf(">") > result.indexOf("<"))  
//	            result = result.substring(result.indexOf("<"), result.lastIndexOf(">") + 1);  
//	    }  
	}
	private static void getSelfTask() {
		// TODO Auto-generated method stub
		System.out.print(ServiceClient.getSelfTask(1));
	}
	private static void getProject() {
		// TODO Auto-generated method stub
		System.out.print(ServiceClient.getTeamProjects(1));
	}
	private static void submitTask() {
		// TODO Auto-generated method stub
		System.out.print(ServiceClient.submitTask(0, State.IN_TIME_FINISHED, 1));
	}
	private static void getMembers() {
		// TODO Auto-generated method stub
		System.out.print(ServiceClient.getMembers(1));
	}
	private static void deleteProjectTest() throws Exception{
		URL url = new URL("http://115.28.89.176/TeamWork?action=deleteProject&projectID=1");
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.connect();
		InputStream inputStream = httpURLConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
        StringBuilder sb =new StringBuilder();
        BufferedReader br=new BufferedReader(inputStreamReader);
        char []buffer = new char[1024];
        
        int len;
        while((len = br.read(buffer))!=-1)
        {
            sb.append(new String(buffer, 0, len));
        }    
        System.out.print(sb);
	}
	public static void getTeam(){
		System.out.print(ServiceClient.getTeam(1));
	}
	public static void getUserNameTest(){
		
		System.out.print(ServiceClient.getUserName(3));
	}
	public static void getTeamTask(){
		//System.out.print(ServiceClient.getTeamTask(1).toString());
		List<TeamTask> list = ServiceClient.getTeamTask(1);
		System.out.println(list.get(0).taskDetails);
		System.out.println(list.get(0).accepterID);
		System.out.println(list.get(0).taskID);
		System.out.println(list.get(1).taskID);

	}

}
