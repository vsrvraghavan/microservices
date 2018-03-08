/**
 * 
 */
package com.onehuddle.leaderboard.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.onehuddle.commons.pojo.AdminPanelMessage;
import com.onehuddle.commons.pojo.AdminPanelMessage.AdminPanelMessageType;
import com.onehuddle.commons.pojo.AdminPanelMessageData;
import com.onehuddle.commons.pojo.LeaderData;
import com.onehuddle.leaderboard.OneHuddleProperties;

import com.onehuddle.leaderboard.pojo.GameScoreData;

import com.onehuddle.leaderboard.redis.CompanyLeaderboard;

/**
 * @author ragha
 *
 */
public class HttpUtils {

	/**
	 * 
	 */
	public HttpUtils() {
		// TODO Auto-generated constructor stub
	}
	

	public void updateAdminPanel(List<LeaderData>  leaderlist , String gameID) {
		System.out.println("in updateAdminPanel PUT ");
		OneHuddleProperties props = OneHuddleProperties.getInstance();
		if(props.getProperty("game_panel_1_name", "GAME1") != null && (props.getProperty("game_panel_1_name", "GAME1").equalsIgnoreCase(gameID) || props.getProperty("game_panel_2_name", "GAME2").equalsIgnoreCase(gameID)) ){
			
			try {
				System.out.println("in updateAdminPanel inside if condition ");
				
				Gson gson = new Gson();
				AdminPanelMessage apm = new AdminPanelMessage();
				AdminPanelMessageData apmd = new AdminPanelMessageData();
				String adminPanelServer = props.getProperty("admin_panel_server", "172.18.0.22");
				String adminPanelServerPort = props.getProperty("admin_panel_server_port", "9000");
				
				if(props.getProperty("game_panel_1_name", "GAME1").equalsIgnoreCase(gameID)) {					
					apmd.setLb1(leaderlist);
				}else if(props.getProperty("game_panel_2_name", "GAME2").equalsIgnoreCase(gameID)) {
					apmd.setLb2(leaderlist);
				}				
				apm.setType(AdminPanelMessageType.DATA);
				apm.setContent(apmd);
				apm.setMessageFor("all");
				
				ObjectMapper mapper = new ObjectMapper();
				
				System.out.println(mapper.writeValueAsString(apm));
				
				
				
				URL url = new URL("http://"+adminPanelServer+":"+adminPanelServerPort+"/adminpanel");
				
				System.out.println("url : " + url);
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

				
				//String input1 = "{\"type\":\"DATA\",\"content\":{\"gameSessionsLaunched\":1,\"gameSessionsFinishedByPlayer\":2,\"gameSessionsFinishedByManager\":4,\"gameSessionsFinishedByTimeout\":3,\"lb1\":[{\"member\":\"Raga\",\"score\":1000.0,\"rank\":3,\"gameId\":\"GAME1\",\"department\":null,\"group\":null},{\"member\":\"Nirmalya\",\"score\":1003.0,\"rank\":2,\"gameId\":\"GAME1\",\"department\":null,\"group\":null},{\"member\":\"Andy\",\"score\":1010.0,\"rank\":1,\"gameId\":\"GAME1\",\"department\":null,\"group\":null}],\"lb2\":null},\"messageFor\":\"all\"}";//mapper.writeValueAsString(apm);
				String input =  gson.toJson(apm);// mapper.writeValueAsString(apm);
				
				
				System.out.println("input  : " + input);
				
				System.out.println("In adminpanel POST");
				System.out.println(input);
				
				AdminPanelMessage apm1 = gson.fromJson(input, AdminPanelMessage.class);
				System.out.println(apm1.getMessageFor());
				System.out.println(apm1.getType());
				System.out.println(apm1.getMessageFor());
				
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}

				conn.disconnect();
				
			} catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }				
		}				
	}
	
	
	public void updateAdminPanel(GameScoreData gameData) {
		
		Integer leader_list_limit = 10;
		
		OneHuddleProperties props = OneHuddleProperties.getInstance();
		
		System.out.println("Company Name In Property File : "+props.getProperty("company_name", "ABC"));
		System.out.println("Company Name In Data  : "+gameData.getCompanyID());
		System.out.println("Department Name In Property File : "+props.getProperty("department_name", "4"));
		System.out.println("Department Name In Data  : "+gameData.getDepartmentID());
		
		System.out.println("Game1 Name In Property File : "+props.getProperty("game_panel_1_name", "GAME1"));
		System.out.println("Game2 Name In Property File : "+props.getProperty("game_panel_2_name", "GAME2"));
		System.out.println("Game Name In Data  : "+gameData.getGameID());
		
		
		if(		
				(props.getProperty("company_name").equalsIgnoreCase(gameData.getCompanyID()))
				||
				(props.getProperty("game_panel_1_name") != null && props.getProperty("game_panel_1_name").equalsIgnoreCase(gameData.getGameID())) 
				|| 
				(props.getProperty("game_panel_2_name") != null && props.getProperty("game_panel_2_name").equalsIgnoreCase(gameData.getGameID()))				
			){
			
			try {
				
				CompanyLeaderboard lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_game_"+gameData.getGameID()+"_leaderboard");                      
		        List<LeaderData>  game_leaderlist = lb.leadersInGame(1, false, leader_list_limit, gameData.getGameID());
				
				Gson gson = new Gson();
				AdminPanelMessage apm = new AdminPanelMessage();
				AdminPanelMessageData apmd = new AdminPanelMessageData();
				String adminPanelServer = props.getProperty("admin_panel_server", "172.18.0.22");
				String adminPanelServerPort = props.getProperty("admin_panel_server_port", "9000");
				
				if(props.getProperty("game_panel_1_name").equalsIgnoreCase(gameData.getGameID())) {					
					apmd.setLb1(game_leaderlist);
				}else if(props.getProperty("game_panel_2_name").equalsIgnoreCase(gameData.getGameID())) {
					apmd.setLb2(game_leaderlist);
				}
				System.out.println("Company Name In Property File : "+props.getProperty("company_name"));
				System.out.println("Company Name In Data  : "+gameData.getCompanyID());
				
				if(props.getProperty("company_name").equalsIgnoreCase(gameData.getCompanyID())) {
					
					lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_leaderboard");                      
					List<LeaderData>  company_leaderlist = lb.mergeScoresIn(gameData.getCompanyID(), 1, false, leader_list_limit);
					apmd.setLbC(company_leaderlist);
				}
				
				System.out.println("Department Name In Property File : "+props.getProperty("department_name"));
				System.out.println("Department Name In Data  : "+gameData.getDepartmentID());
				
				if(props.getProperty("department_name").equalsIgnoreCase(gameData.getDepartmentID())){
					lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_department_"+gameData.getDepartmentID()+"_leaderboard");                      			        
					List<LeaderData>  department_leaderlist = lb.departmentScoresIn(gameData.getCompanyID(), gameData.getDepartmentID(), 1, false, leader_list_limit);
			        apmd.setLbD(department_leaderlist);
				}
				
				
				
				apm.setType(AdminPanelMessageType.DATA);
				apm.setContent(apmd);
				apm.setMessageFor("all");
				
				ObjectMapper mapper = new ObjectMapper();
				
				System.out.println(mapper.writeValueAsString(apm));
				
				
				
				URL url = new URL("http://"+adminPanelServer+":"+adminPanelServerPort+"/adminpanel");
				
				System.out.println("url : " + url);
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

				
				//String input1 = "{\"type\":\"DATA\",\"content\":{\"gameSessionsLaunched\":1,\"gameSessionsFinishedByPlayer\":2,\"gameSessionsFinishedByManager\":4,\"gameSessionsFinishedByTimeout\":3,\"lb1\":[{\"member\":\"Raga\",\"score\":1000.0,\"rank\":3,\"gameId\":\"GAME1\",\"department\":null,\"group\":null},{\"member\":\"Nirmalya\",\"score\":1003.0,\"rank\":2,\"gameId\":\"GAME1\",\"department\":null,\"group\":null},{\"member\":\"Andy\",\"score\":1010.0,\"rank\":1,\"gameId\":\"GAME1\",\"department\":null,\"group\":null}],\"lb2\":null},\"messageFor\":\"all\"}";//mapper.writeValueAsString(apm);
				String input =  gson.toJson(apm);// mapper.writeValueAsString(apm);
				
				
				System.out.println("input  : " + input);
				
				System.out.println("In adminpanel POST");
				System.out.println(input);
				
				AdminPanelMessage apm1 = gson.fromJson(input, AdminPanelMessage.class);
				System.out.println(apm1.getMessageFor());
				System.out.println(apm1.getType());
				System.out.println(apm1.getMessageFor());
				
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}

				conn.disconnect();
				
			} catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
			
			
		}
		
		
	}

}
