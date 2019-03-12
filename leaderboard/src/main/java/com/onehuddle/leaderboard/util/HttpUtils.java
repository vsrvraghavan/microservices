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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.onehuddle.commons.contest.pojo.*;
import com.onehuddle.commons.pojo.AdminPanelMessage;
import com.onehuddle.commons.pojo.AdminPanelMessage.AdminPanelMessageType;
import com.onehuddle.commons.pojo.AdminPanelMessageData;
import com.onehuddle.commons.pojo.ContestData;
import com.onehuddle.commons.pojo.ContestLeaderboardMessage;
import com.onehuddle.commons.pojo.ContestLeaderboardMessage.LBMessageType;
import com.onehuddle.commons.pojo.LeaderData;
import com.onehuddle.commons.pojo.PlayersAndPoint;
import com.onehuddle.commons.pojo.RegisteredPlayer;
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
	
	public void updateAdminPanel(ContestData contestData) {
		
		Integer leader_list_limit = 3;
		
		OneHuddleProperties props = OneHuddleProperties.getInstance();
		String adminPanelServer = props.getProperty("admin_panel_server", "172.18.0.3");
		String adminPanelServerPort = props.getProperty("admin_panel_server_port", "9000");
		
		RegisteredPlayer regplayer = contestData.getPlayersAndPoints().get(0).getRegisteredPlayer();
		PlayersAndPoint player_point = contestData.getPlayersAndPoints().get(0);
		CompanyLeaderboard contest_game_lb = new CompanyLeaderboard("company_"+contestData.getCompanyName()+"_contest_"+contestData.getContestID()+ "_game_"+regplayer.getGameID()+"_leaderboard");
						
		List<Map<String, List<LeaderData>>> leaderBoardRankDataList = new ArrayList<Map<String, List<LeaderData>>>();				
		Map<String, List<LeaderData>> leaderBoardRankData = new HashMap<String, List<LeaderData>>();		
		
		
		List<LeaderData>  game_leaderlist = contest_game_lb.leadersInGame(1, false, leader_list_limit, regplayer.getGameID());
		leaderBoardRankData.put("game_scoreboard_"+regplayer.getGameID(), game_leaderlist);		
						
		List<LeaderData>  company_contest_leaderlist = contest_game_lb.mergeScoresIn(contestData.getCompanyName(), contestData.getContestID(), 1, false, leader_list_limit);		
		leaderBoardRankData.put("contest_scoreboard_"+contestData.getContestID(), company_contest_leaderlist);
				
		leaderBoardRankDataList.add(leaderBoardRankData);
		
	}
	
	public void publishContestDashboard(String companyId, DashboardData dahsBoardData) {
		OneHuddleProperties props = OneHuddleProperties.getInstance();
		String adminPanelServer = props.getProperty("admin_panel_server", "172.18.0.3");
		String adminPanelServerPort = props.getProperty("admin_panel_server_port", "9000");
		
		
				
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			URL url = new URL("http://"+adminPanelServer+":"+adminPanelServerPort+"/adminpanel/"+companyId+"/"+dahsBoardData.getContestName());
			
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			Gson gson = new Gson();
			//String input1 = "{\"type\":\"DATA\",\"content\":{\"gameSessionsLaunched\":1,\"gameSessionsFinishedByPlayer\":2,\"gameSessionsFinishedByManager\":4,\"gameSessionsFinishedByTimeout\":3,\"lb1\":[{\"member\":\"Raga\",\"score\":1000.0,\"rank\":3,\"gameId\":\"GAME1\",\"department\":null,\"group\":null},{\"member\":\"Nirmalya\",\"score\":1003.0,\"rank\":2,\"gameId\":\"GAME1\",\"department\":null,\"group\":null},{\"member\":\"Andy\",\"score\":1010.0,\"rank\":1,\"gameId\":\"GAME1\",\"department\":null,\"group\":null}],\"lb2\":null},\"messageFor\":\"all\"}";//mapper.writeValueAsString(apm);
			
			
			ContestLeaderboardMessage lbmessage = new ContestLeaderboardMessage();
			
			lbmessage.setType(LBMessageType.DATA);
			lbmessage.setMessageFor("all");
			lbmessage.setContent(dahsBoardData);
			
			
			String input = mapper.writeValueAsString(lbmessage);
			System.out.println("lbmessage : ");
			System.out.println(input);
			
			
			//String input =  gson.toJson(lbmessage);
			
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
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//public void updateContestDashboard(String companyId, String contestId, String gameId, String locationId, String departmentId) {
	public void updateContestDashboard(String companyId, String contestId) {	
		DashboardData dahsBoardData = new DashboardData();
		
		// Contest Leaderboard
		String leader_boardname = "company_"+companyId+"_contest_"+contestId+"_leaderboard";		
		CompanyLeaderboard lb = new CompanyLeaderboard(leader_boardname);						
		ContestLB contestLb = new ContestLB();		
		contestLb.setLB(lb.contestLeadersIn(1, false));
		
		// Contest game leaderboard
								
		List<String> game_members = lb.getMembersIn("company_"+companyId+"_contest_"+contestId+"_games");	
		
		List<GameLB> gameLBList = new ArrayList<GameLB>();				
		
		for(String gameId : game_members) {
			GameLB game_lb = new GameLB();
			leader_boardname = "company_"+companyId+"_contest_"+contestId+"_game_"+gameId+"_leaderboard";
			lb = new CompanyLeaderboard(leader_boardname);
			game_lb.setGameID(gameId);
			game_lb.setLB(lb.contestLeadersIn(1, false));
			gameLBList.add(game_lb);			
		}
		contestLb.setGameLB(gameLBList);
		
				
		// Contest departments leaderboard
		List<String>  dept_members = lb.getMembersIn("company_"+companyId+"_contest_"+contestId+"_departments");
		List<DepartmentLB> departmentLBList = new ArrayList<DepartmentLB>();	
		for(String departmentId : dept_members) {
			DepartmentLB dept_lb = new DepartmentLB();
			leader_boardname = "company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_leaderboard";
			lb = new CompanyLeaderboard(leader_boardname);
			dept_lb.setDepartmentID(departmentId);			
			dept_lb.setLB(lb.contestLeadersIn(1, false));
			List<GameLB> deptGameLBList = new ArrayList<GameLB>();
			for(String gameId : game_members) {
				GameLB game_lb = new GameLB();
				leader_boardname = "company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_game_"+gameId+"_leaderboard";
				lb = new CompanyLeaderboard(leader_boardname);
				game_lb.setGameID(gameId);
				game_lb.setLB(lb.contestLeadersIn(1, false));
				deptGameLBList.add(game_lb);
			}
			dept_lb.setGameLB(deptGameLBList);
			departmentLBList.add(dept_lb);
		}
		contestLb.setDepartmentLB(departmentLBList);
		
		// Contest locations leaderboard
		List<String>  location_members = lb.getMembersIn("company_"+companyId+"_contest_"+contestId+"_locations");
		List<LocationLB> locationLBList = new ArrayList<LocationLB>();
		for(String locationId : location_members) {
			LocationLB location_lb = new LocationLB();
			leader_boardname = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_leaderboard";
			lb = new CompanyLeaderboard(leader_boardname);
			location_lb.setLocationID(locationId);
			location_lb.setLB(lb.contestLeadersIn(1, false));
			List<GameLB> locationGameLBList = new ArrayList<GameLB>();
			for(String gameId : game_members) {
				GameLB game_lb = new GameLB();
				leader_boardname = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_game_"+gameId+"_leaderboard";
				lb = new CompanyLeaderboard(leader_boardname);
				game_lb.setGameID(gameId);
				game_lb.setLB(lb.contestLeadersIn(1, false));
				locationGameLBList.add(game_lb);
			}
			location_lb.setGameLB(locationGameLBList);
			
			
			List<String>  location_dept_members = lb.getMembersIn("company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_departments");
			List<DepartmentLB> locationDepartmentLBList = new ArrayList<DepartmentLB>();	
			for(String departmentId : location_dept_members) {
				DepartmentLB dept_lb = new DepartmentLB();
				leader_boardname = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_department_"+departmentId+"_leaderboard";
				lb = new CompanyLeaderboard(leader_boardname);
				dept_lb.setDepartmentID(departmentId);			
				dept_lb.setLB(lb.contestLeadersIn(1, false));
				List<GameLB> locationDeptGameLBList = new ArrayList<GameLB>();
				for(String gameId : game_members) {
					GameLB dept_game_lb = new GameLB();
					leader_boardname = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_department_"+departmentId+"_game_"+gameId+"_leaderboard";
					lb = new CompanyLeaderboard(leader_boardname);
					dept_game_lb.setGameID(gameId);
					dept_game_lb.setLB(lb.contestLeadersIn(1, false));
					locationDeptGameLBList.add(dept_game_lb);
				}
				dept_lb.setGameLB(locationDeptGameLBList);
				locationDepartmentLBList.add(dept_lb);
			}
			location_lb.setDepartmentLB(locationDepartmentLBList);
			
			locationLBList.add(location_lb);
		}
		
		contestLb.setLocationLB(locationLBList);
		
		DashboardData dashbordData = new DashboardData();
		
		dashbordData.setContestName(contestId);
		
		dashbordData.setContestLB(contestLb);
		
		
		OneHuddleProperties props = OneHuddleProperties.getInstance();
		String adminPanelServer = props.getProperty("admin_panel_server", "172.18.0.3");
		String adminPanelServerPort = props.getProperty("admin_panel_server_port", "9000");
		
		
		
		
		
				
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			URL url = new URL("http://"+adminPanelServer+":"+adminPanelServerPort+"/adminpanel/"+companyId+"/"+contestId);
			
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			Gson gson = new Gson();
			//String input1 = "{\"type\":\"DATA\",\"content\":{\"gameSessionsLaunched\":1,\"gameSessionsFinishedByPlayer\":2,\"gameSessionsFinishedByManager\":4,\"gameSessionsFinishedByTimeout\":3,\"lb1\":[{\"member\":\"Raga\",\"score\":1000.0,\"rank\":3,\"gameId\":\"GAME1\",\"department\":null,\"group\":null},{\"member\":\"Nirmalya\",\"score\":1003.0,\"rank\":2,\"gameId\":\"GAME1\",\"department\":null,\"group\":null},{\"member\":\"Andy\",\"score\":1010.0,\"rank\":1,\"gameId\":\"GAME1\",\"department\":null,\"group\":null}],\"lb2\":null},\"messageFor\":\"all\"}";//mapper.writeValueAsString(apm);
			
			
			ContestLeaderboardMessage lbmessage = new ContestLeaderboardMessage();
			
			lbmessage.setType(LBMessageType.DATA);
			lbmessage.setMessageFor("all");
			lbmessage.setContent(dashbordData);
			
			
			String input = mapper.writeValueAsString(lbmessage);
			System.out.println("lbmessage : ");
			System.out.println(input);
			
			
			//String input =  gson.toJson(lbmessage);
			
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
			
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	public void updateAdminPanel(GameScoreData gameData) {
		
		Integer leader_list_limit = 3;
		
		OneHuddleProperties props = OneHuddleProperties.getInstance();
		
		
		System.out.println("Company Name In Property File : "+props.getProperty("company_name", "ABC"));
		System.out.println("Company Name In Data  : "+gameData.getCompanyID());
		System.out.println("Department Name In Property File : "+props.getProperty("department_name", "4"));
		System.out.println("Department Name In Data  : "+gameData.getDepartmentID());
		
		System.out.println("Game1 Name In Property File : "+props.getProperty("game_panel_1_name", "GAME1"));
		System.out.println("Game2 Name In Property File : "+props.getProperty("game_panel_2_name", "GAME2"));
		System.out.println("Game Name In Data  : "+gameData.getGameID());
		
		/*
		if(		
				(props.getProperty("company_name").equalsIgnoreCase(gameData.getCompanyID()))
				||
				(props.getProperty("game_panel_1_name") != null && props.getProperty("game_panel_1_name").equalsIgnoreCase(gameData.getGameID())) 
				|| 
				(props.getProperty("game_panel_2_name") != null && props.getProperty("game_panel_2_name").equalsIgnoreCase(gameData.getGameID()))				
			){
			
			*/
			
			try {
				
				CompanyLeaderboard lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_game_"+gameData.getGameID()+"_leaderboard");                      
		        List<LeaderData>  game_leaderlist = lb.leadersInGame(1, false, leader_list_limit, gameData.getGameID());
				
				Gson gson = new Gson();
				AdminPanelMessage apm = new AdminPanelMessage();
				AdminPanelMessageData apmd = new AdminPanelMessageData();
				String adminPanelServer = props.getProperty("admin_panel_server", "172.18.0.3");
				String adminPanelServerPort = props.getProperty("admin_panel_server_port", "9000");
				
				/*
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
				
				*/
				
				
				List<Map<String, List<LeaderData>>> leaderBoardRankDataList = new ArrayList<Map<String, List<LeaderData>>>();
				
				
				Map<String, List<LeaderData>> leaderBoardRankData = new HashMap<String, List<LeaderData>>();
				
				
				leaderBoardRankData.put("game_scoreboard_"+gameData.getGameID(), game_leaderlist);
				
				
				if(props.getProperty("game_panel_1_name").equalsIgnoreCase(gameData.getGameID())) {					
					apmd.setLb1(game_leaderlist);
				}else if(props.getProperty("game_panel_2_name").equalsIgnoreCase(gameData.getGameID())) {
					apmd.setLb2(game_leaderlist);
				}
				
				
				leaderBoardRankDataList.add(leaderBoardRankData);
					
					//apmd.setLb1(game_leaderlist);

					//apmd.setLb2(game_leaderlist);
				apmd.setLeaderBoardRankData(leaderBoardRankDataList);
				
				System.out.println("Company Name In Property File : "+props.getProperty("company_name"));
				System.out.println("Company Name In Data  : "+gameData.getCompanyID());
				
				
					
					lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_leaderboard");                      
					List<LeaderData>  company_leaderlist = lb.mergeScoresIn(gameData.getCompanyID(), 1, false, leader_list_limit);
					apmd.setLbC(company_leaderlist);

				
				System.out.println("Department Name In Property File : "+props.getProperty("department_name"));
				System.out.println("Department Name In Data  : "+gameData.getDepartmentID());
				
				
					lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_department_"+gameData.getDepartmentID()+"_leaderboard");                      			        
					List<LeaderData>  department_leaderlist = lb.departmentScoresIn(gameData.getCompanyID(), gameData.getDepartmentID(), 1, false, leader_list_limit);
			        apmd.setLbD(department_leaderlist);

				
				
				apm.setType(AdminPanelMessageType.DATA);
				apm.setContent(apmd);
				apm.setMessageFor("all");
				
				ObjectMapper mapper = new ObjectMapper();
				
				System.out.println(mapper.writeValueAsString(apm));
				
				
				
				URL url = new URL("http://"+adminPanelServer+":"+adminPanelServerPort+"/adminpanel/"+ gameData.getCompanyID());
				//URL url = new URL("http://172.18.0.3:9000/adminpanel");
				
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
			
		/*	
		}
		*/
		
		
	}

}
