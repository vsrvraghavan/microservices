/**
 * 
 */
package com.onehuddle.leaderboard;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onehuddle.commons.pojo.ContestData;
import com.onehuddle.commons.pojo.LeaderData;
import com.onehuddle.commons.pojo.LocationsAndDepartments;
import com.onehuddle.commons.pojo.PlayersAndPoint;
import com.onehuddle.commons.pojo.RegisteredPlayer;
import com.onehuddle.leaderboard.pojo.GameData;
import com.onehuddle.leaderboard.pojo.GameScoreData;

import com.onehuddle.leaderboard.pojo.MyLeaderBoardData;
import com.onehuddle.leaderboard.redis.CompanyLeaderboard;
import com.onehuddle.leaderboard.redis.Leaderboard;
import com.onehuddle.leaderboard.util.GameRankLogger;
import com.onehuddle.leaderboard.util.HttpUtils;
import com.onehuddle.leaderboard.util.StringUtil;

@RestController
@EnableAutoConfiguration
public class LeaderBoard {

	
	@RequestMapping(value="/leaderboard/{itemid}", method = RequestMethod.GET)
    String home(@RequestParam("data") String data, @PathVariable("itemid") String itemid) {
        return "Hello World! itemid : "+ itemid + " data : " + data;
    }
	
	
	
	@RequestMapping(value="/gameleaderboard/{company_id}/{game_id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<MyLeaderBoardData> getGameLeaderboard(@PathVariable("company_id") String company_id, @PathVariable("game_id") String game_id, @RequestHeader(value="user_id", defaultValue="0") String user_id, @RequestHeader(value="max_records", defaultValue="25") String max_records) {
        //return "Hello World from Tomcat Embedded with Jersey! getCompanyLeaderboard \n company_id : "+ company_id;        
        Leaderboard lb = new Leaderboard("company_"+company_id+"_game_"+game_id+"_leaderboard");
        
        Hashtable<String, Object> member_data = lb.scoreAndRankFor(user_id, false);        
        List<LeaderData>  leaderlist = lb.leadersIn(1, false, Integer.valueOf(max_records));
        
        MyLeaderBoardData mylbdata = new MyLeaderBoardData(member_data, leaderlist);
        
        return new ResponseEntity<MyLeaderBoardData>(mylbdata, HttpStatus.OK);
        
        
    }
	
	
	@RequestMapping(value="/companyleaderboard/{company_id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<MyLeaderBoardData> getCompanyLeaderboard(@PathVariable("company_id") String company_id, @RequestHeader(value="user_id", defaultValue="0") String user_id, @RequestHeader(value="max_records", defaultValue="25") String max_records) {
        
        CompanyLeaderboard lb = new CompanyLeaderboard("company_"+company_id+"_leaderboard");
                        
        List<LeaderData> leaderlist = lb.mergeScoresIn(company_id, 0, false, 25);
                
        	Hashtable<String, Object> member_data = lb.scoreAndRankFor(user_id, false);
        		               
        MyLeaderBoardData mylbdata = new MyLeaderBoardData(member_data, leaderlist);
        
        return new ResponseEntity<MyLeaderBoardData>(mylbdata, HttpStatus.OK);
        
        
    }
    
	
	
	@RequestMapping(value="/departmentleaderboard/{company_id}/{department_id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<MyLeaderBoardData> getDepartmentLeaderboard(@PathVariable("company_id") String company_id, @PathVariable("department_id") String department_id, @RequestHeader(value="user_id", defaultValue="0") String user_id, @RequestHeader(value="max_records", defaultValue="25") String max_records) {
        
        CompanyLeaderboard lb = new CompanyLeaderboard("company_"+company_id+"_department_"+department_id+"_leaderboard");                
        List<LeaderData>  leaderlist = lb.departmentScoresIn(company_id, department_id, 1, false, 25);        
        Hashtable<String, Object> member_data = lb.scoreAndRankFor(user_id, false);
        MyLeaderBoardData mylbdata = new MyLeaderBoardData(member_data, leaderlist);        
        return new ResponseEntity<MyLeaderBoardData>(mylbdata, HttpStatus.OK);
    }
	
	
	@RequestMapping(value="/groupleaderboard/{company_id}/{group_id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<MyLeaderBoardData> getGroupLeaderboard(@PathVariable("company_id") String company_id, @PathVariable("group_id") String group_id, @RequestHeader(value="user_id", defaultValue="0") String user_id, @RequestHeader(value="max_records", defaultValue="25") String max_records) {
		
		CompanyLeaderboard lb = new CompanyLeaderboard("company_"+company_id+"_group_"+group_id+"_leaderboard");                
        List<LeaderData>  leaderlist = lb.groupScoresIn(company_id, group_id, 1, false, 25);        
        Hashtable<String, Object> member_data = lb.scoreAndRankFor(user_id, false);
        MyLeaderBoardData mylbdata = new MyLeaderBoardData(member_data, leaderlist);        
        return new ResponseEntity<MyLeaderBoardData>(mylbdata, HttpStatus.OK);

		
    }
	
	
	@RequestMapping(value="/sampleGameScoreData", method = RequestMethod.GET)
    public GameScoreData getSampleGameScoreData(@RequestParam("company_id") String company_id, @RequestParam("game_id") String game_id,  @RequestParam("department_id") String department_id, @RequestParam("group_id") String group_id, @RequestParam("user_id") String user_id, @RequestParam("score") String score) {
        
		GameScoreData gamescoredata = new GameScoreData();
		
		gamescoredata.setCompanyID(company_id);
		gamescoredata.setDepartmentID(department_id);
		gamescoredata.setGroupID(group_id);
		gamescoredata.setGameID(game_id);
		gamescoredata.setPlayerID(user_id);
		gamescoredata.setScore(Double.valueOf(score));
		
		return gamescoredata;
    }
	
	
	@RequestMapping(value="/leaderBoard", method = RequestMethod.PUT)
    public String setLeaderboard(@RequestBody GameScoreData gameData) { 
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			System.out.println("In PUT leaderBoard");
			System.out.println(mapper.writeValueAsString(gameData));
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}
		
		if(StringUtil.isNull(gameData.getPlayerID())) {
			gameData.setPlayerID("Unknown");
		}
		
		CompanyLeaderboard lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_game_"+gameData.getGameID()+"_leaderboard");		
		lb.putCompanyGamesIn(gameData.getCompanyID(), gameData.getGameID());			
		lb.putCompanyDepartmentMembersIn(gameData.getCompanyID(), gameData.getDepartmentID(), gameData.getPlayerID());
		lb.putCompanyGroupMembersIn(gameData.getCompanyID(), gameData.getGroupID(), gameData.getPlayerID());		
		Long user_rank = lb.rankMember(gameData.getPlayerID(), gameData.getScore());		
		
		GameRankLogger gameLogger = new GameRankLogger();
		gameLogger.log(gameData, user_rank);
		gameLogger = null;
		
		lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_game_"+gameData.getGameID()+"_leaderboard");                      
        List<LeaderData>  leaderlist = lb.leadersInGame(1, false, Integer.valueOf(3), gameData.getGameID());
        HttpUtils utils = new HttpUtils();
        	utils.updateAdminPanel(leaderlist, gameData.getGameID());
        
		return String.valueOf(user_rank);		
    }
	
	
	
	@RequestMapping(value="/leaderBoard", method = RequestMethod.POST)
    public String updateLeaderboard(@RequestBody GameScoreData gameData) { 
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			System.out.println("In POST leaderBoard");
			System.out.println(mapper.writeValueAsString(gameData));
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}

		if(StringUtil.isNull(gameData.getPlayerID())) {
			gameData.setPlayerID("Unknown");
		}
		
		CompanyLeaderboard lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_game_"+gameData.getGameID()+"_leaderboard");		
		lb.putCompanyGamesIn(gameData.getCompanyID(), gameData.getGameID());			
		lb.putCompanyDepartmentMembersIn(gameData.getCompanyID(), gameData.getDepartmentID(), gameData.getPlayerID());
		lb.putCompanyGroupMembersIn(gameData.getCompanyID(), gameData.getGroupID(), gameData.getPlayerID());		
		//long user_rank = lb.rankMember(gameData.getPlayerID(), gameData.getScore());		
		Double user_score = lb.changeScoreFor(gameData.getPlayerID(), gameData.getScore());
		
		Long user_rank = lb.rankForIn("company_"+gameData.getCompanyID()+"_game_"+gameData.getGameID()+"_leaderboard", gameData.getPlayerID(), false);
		
		GameRankLogger gameLogger = new GameRankLogger();
		gameLogger.log(gameData, user_rank, user_score);
		gameLogger = null;
		//lb = new CompanyLeaderboard("company_"+gameData.getCompanyID()+"_game_"+gameData.getGameID()+"_leaderboard");                      
        //List<LeaderData>  leaderlist = lb.leadersInGame(1, false, Integer.valueOf(3), gameData.getGameID());
        HttpUtils utils = new HttpUtils();
        	//utils.updateAdminPanel(leaderlist, gameData.getGameID());
        utils.updateAdminPanel(gameData);
        	
        
		return String.valueOf(user_rank);		
    }
	
	
	
	public ObjectNode updateScore(ContestData contestData) {
		ObjectMapper mapper = new ObjectMapper();
		
		ObjectNode node = mapper.createObjectNode();		
		ObjectNode company_node = mapper.createObjectNode();		
		ObjectNode conates_node = mapper.createObjectNode();		
		ObjectNode game_node = mapper.createObjectNode();		
		ObjectNode department_node = mapper.createObjectNode();
		
		RegisteredPlayer regplayer = contestData.getPlayersAndPoints().get(0).getRegisteredPlayer();
		PlayersAndPoint player_point = contestData.getPlayersAndPoints().get(0);
		
		String companyId = contestData.getCompanyName();
		String contestId = contestData.getContestID();
		String gameId = regplayer.getGameID();
		String departmentId = regplayer.getDepartmentID();
		String memberId = regplayer.getPlayerID();				
		String locationId = regplayer.getLocationID();
		
		Long points_earned = player_point.getPointsEarned();
		
		
		CompanyLeaderboard company_contest_leaderboard = new CompanyLeaderboard("company_"+companyId+"_contest_"+contestId+"_leaderboard");
			
		company_contest_leaderboard.putCompanyContestDepartmentMembersIn(companyId, contestId, departmentId, memberId);
		
		//Double user_score = company_contest_leaderboard.changeScoreFor(memberId, points_earned);				
			
		List<Object> response = company_contest_leaderboard.updateContestScoreForMemberIn(companyId, contestId, locationId, departmentId, gameId, memberId, points_earned);
		
		try {
			String jsonInString = mapper.writeValueAsString(response);
			System.out.println(jsonInString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
		return null;
	}
	
	
	
	
	
	
	
	
	@RequestMapping(value="/contestleaderBoard", method = RequestMethod.POST)
    public ObjectNode updateContestLeaderboard(@RequestBody ContestData contestData) { 
		
		ObjectMapper mapper = new ObjectMapper();
		
		ObjectNode node = mapper.createObjectNode();
		
		ObjectNode company_node = mapper.createObjectNode();
		
		ObjectNode conates_node = mapper.createObjectNode();
		
		ObjectNode game_node = mapper.createObjectNode();
		
		ObjectNode department_node = mapper.createObjectNode();
		
		Long user_rank = 0L;
		
		switch(contestData.getContestStatus()) {
				case "STARTED" :
					String leader_board_name = "company_"+contestData.getCompanyName()+"_contest_"+contestData.getContestID(); 
					CompanyLeaderboard contest_started_lb = new CompanyLeaderboard(leader_board_name);					
					contest_started_lb.putCompanyContestsIn(contestData.getCompanyName(), contestData.getContestID());
					
					for (LocationsAndDepartments location_department : contestData.getLocationsAndDepartments()) { 			             
						String locationId = location_department.getLocation();
						contest_started_lb.putCompanyContestLocationsIn(contestData.getCompanyName(), contestData.getContestID(), locationId);
						for(String departmentId: location_department.getDepartment()) {							
							contest_started_lb.putCompanyContestDepartmentsIn(contestData.getCompanyName(), contestData.getContestID(), departmentId);
							contest_started_lb.putCompanyContestLocationDepartmentsIn(contestData.getCompanyName(), contestData.getContestID(), locationId, departmentId);
						}																		
			        } 
					
					for(String gameId: contestData.getGamesAllowed()) {
						contest_started_lb.putCompanyContestGamesIn(contestData.getCompanyName(), contestData.getContestID(), gameId);
					}
			
					/*
					for(int i=0;i<contestData.getLocationsAndDepartments().size();i++) {
						String locationId = contestData.getLocationsAndDepartments().get(i).getLocation();
						contest_started_lb.putCompanyContestLocationsIn(contestData.getCompanyName(), contestData.getContestID(), locationId);
						for(int j=0;j<contestData.getLocationsAndDepartments().get(i).getDepartment().size();i++) {
							String departmentId = contestData.getLocationsAndDepartments().get(i).getDepartment().get(j);							
							contest_started_lb.putCompanyContestDepartmentsIn(contestData.getCompanyName(), contestData.getContestID(), departmentId);
							contest_started_lb.putCompanyContestLocationDepartmentsIn(contestData.getCompanyName(), contestData.getContestID(), locationId, departmentId);							
						}						
					}
					
					for(int i=0;i<contestData.getGamesAllowed().size();i++) {
						String gameId = contestData.getGamesAllowed().get(i);
						contest_started_lb.putCompanyContestGamesIn(contestData.getCompanyName(), contestData.getContestID(), gameId);						
					}
					*/					
					
					break;
								
				case "CONTINUING" :
					
					updateScore(contestData);
					
					break;
				case "CONTINUING_bak" :
					
					break;
					
					
					
				case "STOPPED" :
	
					break;	

				case "ENDED" :
					
					break;	
				
				default :
					
					break;
		
		}
		
		return node;
	}
	
	
	
	
	@RequestMapping(value="/game", method = RequestMethod.PUT)
    public String setGame(@RequestBody GameData gameData) { 		
		System.out.println("In PUT game");
		
		return "null";
		
    }
	
	
	
	
	
		
	

}