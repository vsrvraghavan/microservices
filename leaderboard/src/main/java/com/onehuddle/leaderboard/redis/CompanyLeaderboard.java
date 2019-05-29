package com.onehuddle.leaderboard.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onehuddle.commons.contest.pojo.ContestLB;
import com.onehuddle.commons.contest.pojo.DashboardData;
import com.onehuddle.commons.contest.pojo.DepartmentLB;
import com.onehuddle.commons.contest.pojo.GameLB;
import com.onehuddle.commons.contest.pojo.LB;
import com.onehuddle.commons.contest.pojo.LocationLB;
import com.onehuddle.commons.pojo.LeaderData;
import com.onehuddle.leaderboard.pojo.GameScoreData;
import com.onehuddle.leaderboard.util.GameRankLogger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

public class CompanyLeaderboard extends Leaderboard {

	
	public CompanyLeaderboard(String leaderboardName) {
		super(leaderboardName);
		// TODO Auto-generated constructor stub
	}

	public CompanyLeaderboard(String leaderboardName, String host, int port, int pageSize) {
		super(leaderboardName, host, port, pageSize);
		// TODO Auto-generated constructor stub
	}

	public CompanyLeaderboard(String leaderboardName, int pageSize, Jedis redisConnection) {
		super(leaderboardName, pageSize, redisConnection);
		// TODO Auto-generated constructor stub
		
	}
	
	
	public boolean contestMemberExists(String companyId, String contestId, String memberId) {
		
		boolean retval = false;						   
		String  key = "company_"+companyId+"_contest_"+contestId+"_player_details";		
		retval = _jedis.hexists(key, memberId);
						
		return retval;		
	}
	
	public Long putContestMemberDetails(String companyId, String contestId, String memberId, String locationId, String departmentId) {
		
		ObjectMapper mapper = new ObjectMapper();		
		ObjectNode node = mapper.createObjectNode();
		
		node.put("locationId", locationId);
		node.put("departmentId", departmentId);
		
		Long retval = 0L;
		try {
							   
			System.out.println("company_"+companyId+"_contest_"+contestId+"_player_details");
			retval = _jedis.hset("company_"+companyId+"_contest_"+contestId+"_player_details", memberId, mapper.writeValueAsString(node));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return retval;		
	}
	
	
	public ObjectNode getContestMemberDetails(String companyId, String contestId, String memberId) {
		ObjectMapper mapper = new ObjectMapper();		
		ObjectNode node = mapper.createObjectNode();
				
		try {			
			System.out.println("member :  "+ memberId);
			System.out.println("company_"+companyId+"_contest_"+contestId+"_player_details" + "    ---     "+ memberId);
			
			System.out.println(_jedis.hget("company_Swanspeed_contest_Contest-01_player_details", "ANDY_AT_Swanspeed.com"));
			
			String members = _jedis.hget("company_"+companyId+"_contest_"+contestId+"_player_details", memberId);			
			
			System.out.println("members  : "+ members);
			
			JsonNode jsonnode = mapper.readTree(members);
			
			
			
			System.out.println(mapper.writeValueAsString(jsonnode));
			
			node.putPOJO("member_details", jsonnode);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return node;
	}
	
	public List<String> getContestGames(String companyId, String contestId) {
				
		return this.getMembersIn("company_"+companyId+"_contest_"+contestId+"_games");
	}
	
	/**
	 * Change the score for a member by a certain delta in the named leaderboard
	 *
	 * @param leaderboardName Leaderboard
	 * @param member Member
	 * @param delta Score delta
	 * @return Updated score
	 */
	
	/*
	public ObjectNode getContestScoreForMemberIn(String companyId, String contestId, String member) {
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("member :  "+ member);
		ObjectNode member_details_data = this.getContestMemberDetails(companyId, contestId, member);
		
		
		
		
		
		
		List<String> games = getContestGames(companyId, contestId);
		
				
		ObjectNode contest_node = mapper.createObjectNode();
		
		
		
		
		try {
			ObjectNode member_details = (ObjectNode) mapper.readTree(mapper.writeValueAsString(member_details_data.get("member_details")));
			
			String locationId =  member_details.get("locationId").asText();
			String departmentId = member_details.get("departmentId").asText();			
			String leader_board = "company_"+companyId+"_contest_"+contestId+"_leaderboard";		
			Double score = this.scoreForIn(leader_board, member);	
			
			Long contest_rank = this.rankForIn(leader_board, member, false);
							
			contest_node.put("contest_name", contestId);
			contest_node.put("rank", contest_rank);
			contest_node.put("score", score);
			
			System.out.println(mapper.writeValueAsString(contest_node));
			ArrayNode gamesScores = mapper.createArrayNode();
			for(String gameId : games) {
				leader_board = "company_"+companyId+"_contest_"+contestId+"_game_"+gameId+"_leaderboard";		
				//transaction.zincrby(leader_board, delta, member);
				score = this.scoreForIn(leader_board, member);
				Long contest_game_rank = this.rankForIn(leader_board, member, false);
				ObjectNode contest_game_node = mapper.createObjectNode();				
				contest_game_node.put("game_name", gameId);
				contest_game_node.put("rank", contest_game_rank);
				contest_game_node.put("score", score);				
				gamesScores.add(contest_game_node);
				
			}
			
			contest_node.putPOJO("game", gamesScores);
			
			System.out.println(mapper.writeValueAsString(contest_node));
			
			
			leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_leaderboard";		
			//transaction.zincrby(leader_board, delta, member);
			score = this.scoreForIn(leader_board, member);
			Long contest_location_rank = this.rankForIn(leader_board, member, false);
			ObjectNode contest_location_node = mapper.createObjectNode();
			
			contest_location_node.put("location_name", locationId);
			contest_location_node.put("rank", contest_location_rank);
			contest_location_node.put("score", score);
			
			
			gamesScores = mapper.createArrayNode();
			for(String gameId : games) {			
				leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_game_"+gameId+"_leaderboard";		
				//transaction.zincrby(leader_board, delta, member);
				score = this.scoreForIn(leader_board, member);
				Long contest_location_game_rank = this.rankForIn(leader_board, member, false);
				ObjectNode contest_location_game_node = mapper.createObjectNode();
				contest_location_game_node.put("game_name", gameId);
				contest_location_game_node.put("rank", contest_location_game_rank);
				contest_location_game_node.put("score", score);
				gamesScores.add(contest_location_game_node);
				
			}
			contest_location_node.putPOJO("game", gamesScores);
			
			
			leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_department_"+departmentId+"_leaderboard";		
			//transaction.zincrby(leader_board, delta, member);
			score = this.scoreForIn(leader_board, member);
			Long contest_location_department_rank = this.rankForIn(leader_board, member, false);
			ObjectNode contest_location_department_node = mapper.createObjectNode();
			contest_location_department_node.put("department_name", departmentId);
			contest_location_department_node.put("rank", contest_location_department_rank);
			contest_location_department_node.put("score", score);
		
			
			gamesScores = mapper.createArrayNode();
			for(String gameId : games) {	
				leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_department_"+departmentId+"_game_"+gameId+"_leaderboard";		
				//transaction.zincrby(leader_board, delta, member);
				score = this.scoreForIn(leader_board, member);
				Long contest_location_department_game_rank = this.rankForIn(leader_board, member, false);
				ObjectNode contest_location_department_game_node = mapper.createObjectNode();
				contest_location_department_game_node.put("game_name", gameId);
				contest_location_department_game_node.put("rank", contest_location_department_game_rank);
				contest_location_department_game_node.put("score", score);
				gamesScores.add(contest_location_department_game_node);
			}
			
			
			
			contest_location_department_node.putPOJO("game", gamesScores);		
			
			contest_location_node.putPOJO("department", contest_location_department_node);
			contest_node.putPOJO("location", contest_location_node);
			
			
			System.out.println(mapper.writeValueAsString(contest_node));
			
			
			leader_board = "company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_leaderboard";		
			//transaction.zincrby(leader_board, delta, member);
			score = this.scoreForIn(leader_board, member);
			Long contest_department_rank = this.rankForIn(leader_board, member, false);
			ObjectNode contest_department_node = mapper.createObjectNode();
			contest_department_node.put("department_name", departmentId);
			contest_department_node.put("rank", contest_department_rank);
			contest_department_node.put("score", score);
			
			//node.put("department_rank", contest_department_rank);
			
			gamesScores = mapper.createArrayNode();
			for(String gameId : games) {
				leader_board = "company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_game_"+gameId+"_leaderboard";		
				//transaction.zincrby(leader_board, delta, member);
				score = this.scoreForIn(leader_board, member);
				Long contest_department_game_rank = this.rankForIn(leader_board, member, false);
				ObjectNode contest_department_game_node = mapper.createObjectNode();
				contest_department_game_node.put("game_name", gameId);
				contest_department_game_node.put("rank", contest_department_game_rank);
				contest_department_game_node.put("score", score);
				gamesScores.add(contest_department_game_node);
			}
			
			contest_department_node.putPOJO("game", gamesScores);
			contest_node.putPOJO("department", contest_department_node);
						
			System.out.println(mapper.writeValueAsString(contest_node));
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return contest_node;//transaction.exec();
		
		
		
	}
	
	*/
	
	
	/**
	 * Change the score for a member by a certain delta in the named leaderboard
	 *
	 * @param leaderboardName Leaderboard
	 * @param member Member
	 * @param delta Score delta
	 * @return Updated score
	 */
	//public ObjectNode getContestScoreForMemberIn(String companyId, String contestId, String member) {
	public DashboardData getContestScoreForMemberIn(String companyId, String contestId, String member) {
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("member :  "+ member);
		ObjectNode member_details_data = this.getContestMemberDetails(companyId, contestId, member);
		List<String> games = getContestGames(companyId, contestId);		
		ObjectNode contest_node = mapper.createObjectNode();
		
		ContestLB contestlb = new ContestLB();
		DashboardData dashData = new  DashboardData();
		List<LB> listLB = new ArrayList<LB>();
		LB lb = new LB();
		try {
			ObjectNode member_details = (ObjectNode) mapper.readTree(mapper.writeValueAsString(member_details_data.get("member_details")));
			
			String locationId =  member_details.get("locationId").asText();
			String departmentId = member_details.get("departmentId").asText();			
			String leader_board = "company_"+companyId+"_contest_"+contestId+"_leaderboard";		
			Double score = this.scoreForIn(leader_board, member);	
			
			Long contest_rank = this.rankForIn(leader_board, member, false);
							
			contest_node.put("contest_name", contestId);
			contest_node.put("rank", contest_rank);
			contest_node.put("score", score);
			
			dashData.setContestName(contestId);
			
			
			
			lb.setName(contestId);
			lb.setRank(contest_rank);
			lb.setScore(score);						
			
			listLB.add(lb);
			contestlb.setLB(listLB);
			
			
			System.out.println(mapper.writeValueAsString(contest_node));
			ArrayNode gamesScores = mapper.createArrayNode();
			GameLB glb = new GameLB();
			List<GameLB> glbList = new  ArrayList<GameLB>();
			for(String gameId : games) {
				leader_board = "company_"+companyId+"_contest_"+contestId+"_game_"+gameId+"_leaderboard";		
				//transaction.zincrby(leader_board, delta, member);
				score = this.scoreForIn(leader_board, member);
				Long contest_game_rank = this.rankForIn(leader_board, member, false);
				ObjectNode contest_game_node = mapper.createObjectNode();				
				contest_game_node.put("game_name", gameId);
				contest_game_node.put("rank", contest_game_rank);
				contest_game_node.put("score", score);				
				gamesScores.add(contest_game_node);
				
				listLB = new ArrayList<LB>();
				lb.setName(contestId);
				lb.setRank(contest_rank);
				lb.setScore(score);						
				
				listLB.add(lb);
				glb.setLB(listLB);
				glbList.add(glb);
			}
			
			contest_node.putPOJO("game", gamesScores);
			
			contestlb.setGameLB(glbList);
			
			
			System.out.println(mapper.writeValueAsString(contest_node));
			
			
			leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_leaderboard";		
			//transaction.zincrby(leader_board, delta, member);
			score = this.scoreForIn(leader_board, member);
			Long contest_location_rank = this.rankForIn(leader_board, member, false);
			ObjectNode contest_location_node = mapper.createObjectNode();
			
			contest_location_node.put("location_name", locationId);
			contest_location_node.put("rank", contest_location_rank);
			contest_location_node.put("score", score);
						
			LocationLB locLB = new  LocationLB();
			
			List<LocationLB> locLBList = new  ArrayList<LocationLB>();
			
			
			gamesScores = mapper.createArrayNode();
			for(String gameId : games) {			
				leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_game_"+gameId+"_leaderboard";		
				//transaction.zincrby(leader_board, delta, member);
				score = this.scoreForIn(leader_board, member);
				Long contest_location_game_rank = this.rankForIn(leader_board, member, false);
				ObjectNode contest_location_game_node = mapper.createObjectNode();
				contest_location_game_node.put("game_name", gameId);
				contest_location_game_node.put("rank", contest_location_game_rank);
				contest_location_game_node.put("score", score);
				gamesScores.add(contest_location_game_node);
				
				listLB = new ArrayList<LB>();
				lb.setName(contestId);
				lb.setRank(contest_rank);
				lb.setScore(score);						
				
				listLB.add(lb);
				locLB.setLB(listLB);
				locLBList.add(locLB);
				
				
				
			}
			contest_location_node.putPOJO("game", gamesScores);
			
			contestlb.setLocationLB(locLBList);
			
			leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_department_"+departmentId+"_leaderboard";		
			//transaction.zincrby(leader_board, delta, member);
			score = this.scoreForIn(leader_board, member);
			Long contest_location_department_rank = this.rankForIn(leader_board, member, false);
			ObjectNode contest_location_department_node = mapper.createObjectNode();
			contest_location_department_node.put("department_name", departmentId);
			contest_location_department_node.put("rank", contest_location_department_rank);
			contest_location_department_node.put("score", score);
		
			
			gamesScores = mapper.createArrayNode();
			for(String gameId : games) {	
				leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_department_"+departmentId+"_game_"+gameId+"_leaderboard";		
				//transaction.zincrby(leader_board, delta, member);
				score = this.scoreForIn(leader_board, member);
				Long contest_location_department_game_rank = this.rankForIn(leader_board, member, false);
				ObjectNode contest_location_department_game_node = mapper.createObjectNode();
				contest_location_department_game_node.put("game_name", gameId);
				contest_location_department_game_node.put("rank", contest_location_department_game_rank);
				contest_location_department_game_node.put("score", score);
				gamesScores.add(contest_location_department_game_node);
			}
			
			
			
			contest_location_department_node.putPOJO("game", gamesScores);		
			
			contest_location_node.putPOJO("department", contest_location_department_node);
			contest_node.putPOJO("location", contest_location_node);
			
			
			System.out.println(mapper.writeValueAsString(contest_node));
			
			
			leader_board = "company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_leaderboard";		
			//transaction.zincrby(leader_board, delta, member);
			score = this.scoreForIn(leader_board, member);
			Long contest_department_rank = this.rankForIn(leader_board, member, false);
			ObjectNode contest_department_node = mapper.createObjectNode();
			contest_department_node.put("department_name", departmentId);
			contest_department_node.put("rank", contest_department_rank);
			contest_department_node.put("score", score);
			
			//node.put("department_rank", contest_department_rank);
			
			DepartmentLB deptLB = new  DepartmentLB();			
			List<DepartmentLB> deptLBList = new  ArrayList<DepartmentLB>();
						
			gamesScores = mapper.createArrayNode();
			for(String gameId : games) {
				leader_board = "company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_game_"+gameId+"_leaderboard";		
				//transaction.zincrby(leader_board, delta, member);
				score = this.scoreForIn(leader_board, member);
				Long contest_department_game_rank = this.rankForIn(leader_board, member, false);
				ObjectNode contest_department_game_node = mapper.createObjectNode();
				contest_department_game_node.put("game_name", gameId);
				contest_department_game_node.put("rank", contest_department_game_rank);
				contest_department_game_node.put("score", score);
				gamesScores.add(contest_department_game_node);
				
				listLB = new ArrayList<LB>();
				lb.setName(contestId);
				lb.setRank(contest_rank);
				lb.setScore(score);						
				
				listLB.add(lb);
				deptLB.setLB(listLB);
				deptLBList.add(deptLB);
				
			}
			
			contest_department_node.putPOJO("game", gamesScores);
			contest_node.putPOJO("department", contest_department_node);
			
			contestlb.setDepartmentLB(deptLBList);
			
			System.out.println(mapper.writeValueAsString(contest_node));
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dashData.setContestLB(contestlb);
		//return contest_node;//transaction.exec();
		return dashData;
		
		
		
	}
	
	
	/**
	 * Change the score for a member by a certain delta in the named leaderboard
	 *
	 * @param leaderboardName Leaderboard
	 * @param member Member
	 * @param delta Score delta
	 * @return Updated score
	 */
	public ObjectNode updateContestScoreForMemberIn(String companyId, String contestId, String locationId, String departmentId, String gameId, String member, double delta) {
		
		
		ObjectMapper mapper = new ObjectMapper();		

		/*
		GameRankLogger gameLogger = new GameRankLogger();		
		GameScoreData gameData = new GameScoreData();				
		gameLogger.log(gameData, user_rank, user_score);
		gameLogger = null;
		*/

		//Transaction transaction = _jedis.multi();
		
		String leader_board = "company_"+companyId+"_contest_"+contestId+"_leaderboard";		
		//transaction.zincrby(leader_board, delta, member);
		Double score = changeScoreForMemberIn(leader_board, member, delta);		
		Long contest_rank = this.rankForIn(leader_board, member, false);
		ObjectNode contest_node = mapper.createObjectNode();				
		contest_node.put("contest_name", contestId);
		contest_node.put("rank", contest_rank);
		contest_node.put("score", score);
		
		
		
		leader_board = "company_"+companyId+"_contest_"+contestId+"_game_"+gameId+"_leaderboard";		
		//transaction.zincrby(leader_board, delta, member);
		score = changeScoreForMemberIn(leader_board, member, delta);
		Long contest_game_rank = this.rankForIn(leader_board, member, false);
		ObjectNode contest_game_node = mapper.createObjectNode();				
		contest_game_node.put("game_name", gameId);
		contest_game_node.put("rank", contest_game_rank);
		contest_game_node.put("score", score);
		contest_node.putPOJO("game", contest_game_node);
		
		
		leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_leaderboard";		
		//transaction.zincrby(leader_board, delta, member);
		score = changeScoreForMemberIn(leader_board, member, delta);
		Long contest_location_rank = this.rankForIn(leader_board, member, false);
		ObjectNode contest_location_node = mapper.createObjectNode();
		contest_location_node.put("location_name", locationId);
		contest_location_node.put("rank", contest_location_rank);
		contest_location_node.put("score", score);
		
		
		leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_game_"+gameId+"_leaderboard";		
		//transaction.zincrby(leader_board, delta, member);
		score = changeScoreForMemberIn(leader_board, member, delta);
		Long contest_location_game_rank = this.rankForIn(leader_board, member, false);
		ObjectNode contest_location_game_node = mapper.createObjectNode();
		contest_location_game_node.put("game_name", gameId);
		contest_location_game_node.put("rank", contest_location_game_rank);
		contest_location_game_node.put("score", score);
		contest_location_node.putPOJO("game", contest_location_game_node);
		
		
		leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_department_"+departmentId+"_leaderboard";		
		//transaction.zincrby(leader_board, delta, member);
		score = changeScoreForMemberIn(leader_board, member, delta);
		Long contest_location_department_rank = this.rankForIn(leader_board, member, false);
		ObjectNode contest_location_department_node = mapper.createObjectNode();
		contest_location_department_node.put("department_name", departmentId);
		contest_location_department_node.put("rank", contest_location_department_rank);
		contest_location_department_node.put("score", score);
		
		
		
		leader_board = "company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_department_"+departmentId+"_game_"+gameId+"_leaderboard";		
		//transaction.zincrby(leader_board, delta, member);
		score = changeScoreForMemberIn(leader_board, member, delta);
		Long contest_location_department_game_rank = this.rankForIn(leader_board, member, false);
		ObjectNode contest_location_department_game_node = mapper.createObjectNode();
		contest_location_department_game_node.put("game_name", gameId);
		contest_location_department_game_node.put("rank", contest_location_department_game_rank);
		contest_location_department_game_node.put("score", score);
		contest_location_department_node.putPOJO("game", contest_location_department_game_node);		
		contest_location_node.putPOJO("department", contest_location_department_node);
		contest_node.putPOJO("location", contest_location_node);
		
		
		leader_board = "company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_leaderboard";		
		//transaction.zincrby(leader_board, delta, member);
		score = changeScoreForMemberIn(leader_board, member, delta);
		Long contest_department_rank = this.rankForIn(leader_board, member, false);
		ObjectNode contest_department_node = mapper.createObjectNode();
		contest_department_node.put("department_name", departmentId);
		contest_department_node.put("rank", contest_department_rank);
		contest_department_node.put("score", score);
		
		//node.put("department_rank", contest_department_rank);
		
		
		leader_board = "company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_game_"+gameId+"_leaderboard";		
		//transaction.zincrby(leader_board, delta, member);
		score = changeScoreForMemberIn(leader_board, member, delta);
		Long contest_department_game_rank = this.rankForIn(leader_board, member, false);
		ObjectNode contest_department_game_node = mapper.createObjectNode();
		contest_department_game_node.put("game_name", gameId);
		contest_department_game_node.put("rank", contest_department_game_rank);
		contest_department_game_node.put("score", score);
		contest_department_node.putPOJO("game", contest_department_game_node);
		contest_node.putPOJO("department", contest_department_node);
		
		try {
			System.out.println(mapper.writeValueAsString(contest_node));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return contest_node;//transaction.exec();
		
	}
	

	public DashboardData getContestDashboard(String companyId, String contestId) {	
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
		
		return dashbordData;
	}
	
	
	
	
	
	/**
	 * Retrieve a page of leaders as a list of LeaderData in the named company
	 *
	 * @param companyName CompanyName
	 * @param currentPage Page
	 * @param useZeroIndexForRank Use zero-based index for rank
	 * @param pageSize Page size
	 * @return Page of leaders as a list of LeaderData in the named leaderboard
	 */
	public List<LeaderData> mergeScoresIn(String companyId, int currentPage, boolean useZeroIndexForRank, int pageSize){
				
		List<LeaderData> leaderList = new ArrayList<LeaderData>();		
		currentPage = (currentPage < 1) ? 1 : currentPage;		
		pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
		String[] company_games_leaderboards = getCompanyGamesLeaderBoardIn(companyId);			
		if(company_games_leaderboards.length == 0) {
			return leaderList;
		}else {
			Long no_of_merged_records = _jedis.zunionstore(this._leaderboardName, company_games_leaderboards);			
			currentPage = (currentPage > totalPagesIn(this._leaderboardName, pageSize)) ? totalPagesIn(this._leaderboardName, pageSize) : currentPage;			
			int indexForRedis = currentPage - 1;
			int startingOffset = indexForRedis * pageSize;			
			startingOffset = (startingOffset < 0) ? 0 : startingOffset;			
			int endingOffset = (startingOffset + pageSize) - 1;
			Set<Tuple> rawLeaderData = _jedis.zrevrangeWithScores(this._leaderboardName, startingOffset, endingOffset);
			leaderList = massageLeaderData(this._leaderboardName, rawLeaderData, useZeroIndexForRank);
		}		
		return leaderList;		
	}

	
	
	/**
	 * Retrieve a page of leaders as a list of LeaderData in the named company
	 *
	 * @param companyName CompanyName
	 * @param currentPage Page
	 * @param useZeroIndexForRank Use zero-based index for rank
	 * @param pageSize Page size
	 * @return Page of leaders as a list of LeaderData in the named leaderboard
	 */
	public List<LeaderData> mergeScoresIn(String companyId, String contestId, int currentPage, boolean useZeroIndexForRank, int pageSize){
				
		System.out.println("Lb Name :  "+ this._leaderboardName);
		List<LeaderData> leaderList = new ArrayList<LeaderData>();		
		currentPage = (currentPage < 1) ? 1 : currentPage;		
		pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
		//String[] company_games_leaderboards = getCompanyGamesLeaderBoardIn(companyId);		
		
		String[] company_contest_games_leaderboards = getCompanyContestGamesLeaderBoardIn(companyId, contestId);
		
		if(company_contest_games_leaderboards.length == 0) {
			return leaderList;
		}else {
			Long no_of_merged_records = _jedis.zunionstore(this._leaderboardName, company_contest_games_leaderboards);			
			currentPage = (currentPage > totalPagesIn(this._leaderboardName, pageSize)) ? totalPagesIn(this._leaderboardName, pageSize) : currentPage;			
			int indexForRedis = currentPage - 1;
			int startingOffset = indexForRedis * pageSize;			
			startingOffset = (startingOffset < 0) ? 0 : startingOffset;			
			int endingOffset = (startingOffset + pageSize) - 1;
			Set<Tuple> rawLeaderData = _jedis.zrevrangeWithScores(this._leaderboardName, startingOffset, endingOffset);
			leaderList = massageLeaderData(this._leaderboardName, rawLeaderData, useZeroIndexForRank);
		}		
		return leaderList;		
	}
	
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public Long putCompanyGamesIn(String companyId, String gameId) {
		
		Long memberid = _jedis.sadd("company_"+companyId+"_games", gameId);		
		return memberid;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public String[] getCompanyGamesIn(String companyId) {
		
		Set<String> games = _jedis.smembers("company_"+companyId+"_games");
		String[] company_games =  games.stream().toArray(String[]::new);
		return company_games;
	}
	
	
	
	
	/**
	 * Retrieve list of contests in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of contests in the named company
	 */
	
	public boolean companyContestsExists(String companyId, String contestId) {
		
		Set<String> contests = _jedis.smembers("company_"+companyId+"_contests");		
		return _jedis.sismember("company_"+companyId+"_contests", contestId);
	}
	
	
	/**
	 * Retrieve list of contests in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of contests in the named company
	 */
	
	public String[] getCompanyContestsIn(String companyId) {
		
		Set<String> contests = _jedis.smembers("company_"+companyId+"_contests");
		String[] company_contests =  contests.stream().toArray(String[]::new);
		return company_contests;
	}
	
	
	
	
	/**
	 * Retrieve list of contests in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of contests in the named company
	 */
	
	public Long putCompanyContestsIn(String companyId, String contestId) {
		
		Long memberid = _jedis.sadd("company_"+companyId+"_contests", contestId);		
		return memberid;
	}
	
	
	
	
	
	
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param CompanyId CompanyId
	 * @param ContestId contestId
	 * @param GameId gameId
	 * @return list of games in the named company and contest
	 */
	
	public Long putCompanyContestGamesIn(String companyId, String contestId, String gameId) {
		
		Long memberid = _jedis.sadd("company_"+companyId+"_contest_"+contestId+"_games", gameId);		
		return memberid;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param CompanyId CompanyId
	 * @param ContestId contestId
	 * @return list of games in the named company and contest
	 */
	
	public String[] getCompanyContestLocationsIn(String companyId, String contestId) {
		
		Set<String> games = _jedis.smembers("company_"+companyId+"_contest_"+contestId+"_locations");
		String[] company_games =  games.stream().toArray(String[]::new);
		return company_games;
	}
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param CompanyId CompanyId
	 * @param ContestId contestId
	 * @param GameId gameId
	 * @return list of games in the named company and contest
	 */
	
	public Long putCompanyContestLocationsIn(String companyId, String contestId, String locationId) {
		
		Long memberid = _jedis.sadd("company_"+companyId+"_contest_"+contestId+"_locations", locationId);		
		return memberid;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param CompanyId CompanyId
	 * @param ContestId contestId
	 * @return list of games in the named company and contest
	 */
	
	public String[] getCompanyContestLocationDepartmentsIn(String companyId, String contestId, String locationId) {
		
		Set<String> games = _jedis.smembers("company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_departments");
		String[] company_games =  games.stream().toArray(String[]::new);
		return company_games;
	}
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param CompanyId CompanyId
	 * @param ContestId contestId
	 * @param GameId gameId
	 * @return list of games in the named company and contest
	 */
	
	public Long putCompanyContestLocationDepartmentsIn(String companyId, String contestId, String locationId, String departmentId) {
		
		Long memberid = _jedis.sadd("company_"+companyId+"_contest_"+contestId+"_location_"+locationId+"_departments", departmentId);		
		return memberid;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param CompanyId CompanyId
	 * @param ContestId contestId
	 * @return list of games in the named company and contest
	 */
	
	public String[] getCompanyContestDepartmentsIn(String companyId, String contestId) {
		
		Set<String> games = _jedis.smembers("company_"+companyId+"_contest_"+contestId+"_departments");
		String[] company_games =  games.stream().toArray(String[]::new);
		return company_games;
	}
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param CompanyId CompanyId
	 * @param ContestId contestId
	 * @param GameId gameId
	 * @return list of games in the named company and contest
	 */
	
	public Long putCompanyContestDepartmentsIn(String companyId, String contestId, String departmentId) {
		
		Long memberid = _jedis.sadd("company_"+companyId+"_contest_"+contestId+"_departments", departmentId);		
		return memberid;
	}
	
	
	
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param CompanyId CompanyId
	 * @param ContestId contestId
	 * @return list of games in the named company and contest
	 */
	
	public String[] getCompanyContestGamesIn(String companyId, String contestId) {
		
		Set<String> games = _jedis.smembers("company_"+companyId+"_contest_"+contestId+"_games");
		String[] company_games =  games.stream().toArray(String[]::new);
		return company_games;
	}
	
	
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public String[] getCompanyGamesLeaderBoardIn(String companyId) {
		
		List<String> games = _jedis.smembers("company_"+companyId+"_games").stream()
	    .map(s -> "company_"+companyId+"_game_"+ s + "_leaderboard")
	    .collect(Collectors.toList());		
		String[] company_games_leaderboard =  games.stream().toArray(String[]::new); //games.toArray(new String[0]);//games.stream().toArray(String[]::new);
		System.out.println(Arrays.toString(company_games_leaderboard));		
		return company_games_leaderboard;
	}
	
	
	
	
	/**
	 * Retrieve list of games in the named company and contest
	 *
	 * @param companyId CompanyName
	 * @param contestId ContestName
	 * @return list of games in the named company and contest
	 */
	
	public String[] getCompanyContestGamesLeaderBoardIn(String companyId, String contestId) {
	
		List<String> games = _jedis.smembers("company_"+companyId+"_contest_"+contestId+"_games").stream()
	    //.map(s -> "company_"+companyId+"_contest_"+contestId+"_game_"+ s + "_leaderboard")
		.map(s -> "company_"+companyId+"_game_"+ s + "_leaderboard")
	    .collect(Collectors.toList());		
		String[] company_contest_games_leaderboard =  games.stream().toArray(String[]::new); //games.toArray(new String[0]);//games.stream().toArray(String[]::new);
		System.out.println(Arrays.toString(company_contest_games_leaderboard));		
		return company_contest_games_leaderboard;
	}
	
	
	
	
	
	/**
	 * put department in the named company
	 *
	 * @param companyId CompanyName
	 * @param departmentId DepartmentName
	 * @return list no of departments in the named company
	 */
	
	public Long putCompanyDepartmentsIn(String companyId, String departmentId) {
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		if(companyId != null &&  departmentId != null) {			
			inserted_quantity = _jedis.sadd("company_"+companyId+"_departments", departmentId);
		}		
		return inserted_quantity;
	}
	
	
	/**
	 * Retrieve list of departments in the named company
	 *
	 * @param companyId CompanyName
	 * @return list of departments in the named company
	 */
	
	public String[] getCompanyDepartmentsIn(String companyId) {
		
		Set<String> departments = _jedis.smembers("company_"+companyId+"_departments");		
		String[] company_departments = departments.stream().toArray(String[]::new);		
		return company_departments;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @param memberId Player ID
	 * @return inserted members quantity
	 */
	
	public Long putCompanyDepartmentMembersIn(String companyId, String departmentId, String memberId) {
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		putCompanyDepartmentsIn(companyId, departmentId);
		inserted_quantity = _jedis.sadd("company_"+companyId+"_department_"+departmentId+"_members", memberId);				
		return inserted_quantity;
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @return list of members in the named company and department
	 */
	
	public String[] getCompanyDepartmentMembersIn(String companyId, String departmentId) {
		
		Set<String> department_members = _jedis.smembers("company_"+companyId+"_department_"+departmentId+"_members");		
		String[] company_department_members = department_members.stream().toArray(String[]::new);		
		return company_department_members;
	}
	
	
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @param memberId Player ID
	 * @return inserted members quantity
	 */
	
	public Long putCompanyContestDepartmentMembersIn(String companyId, String contestId, String departmentId, String memberId) {
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		putCompanyDepartmentsIn(companyId, departmentId);
		inserted_quantity = _jedis.sadd("company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_members", memberId);				
		return inserted_quantity;
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @return list of members in the named company and department
	 */
	
	public String[] getCompanyContestDepartmentMembersIn(String companyId, String contestId, String departmentId) {
		
		Set<String> department_members = _jedis.smembers("company_"+companyId+"_contest_"+contestId+"_department_"+departmentId+"_members");		
		String[] company_department_members = department_members.stream().toArray(String[]::new);		
		return company_department_members;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public Long putCompanyGroupsIn(String companyId, String groupId) {
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		if(companyId != null &&  groupId != null) {
			inserted_quantity = _jedis.sadd("company_"+companyId+"_groups", groupId);
		}		
		return inserted_quantity;
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public String[] getCompanyGroupsIn(String companyId) {
		
		Set<String> groups = _jedis.smembers("company_"+companyId+"_groups");		
		String[] company_groups = groups.stream().toArray(String[]::new);		
		return company_groups;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @param memberId Player ID
	 * @return inserted members quantity
	 */
	
	public Long putCompanyGroupMembersIn(String companyId, String groupId, String memberId) {	
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		putCompanyGroupsIn(companyId, groupId);
		inserted_quantity = _jedis.sadd("company_"+companyId+"_group_"+groupId+"_members", memberId);		
		return inserted_quantity;		
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @return list of members in the named company and department
	 */
	
	public String[] getCompanyGroupMembersIn(String companyId, String groupId) {
		
		Set<String> group_members = _jedis.smembers("company_"+companyId+"_group_"+groupId+"_members");		
		String[] company_group_members = group_members.stream().toArray(String[]::new);		
		return company_group_members;
	}
	
	
	
	
	
	/**
	 * Retrieve a page of leaders as a list of LeaderData in the named company
	 *
	 * @param companyName CompanyName
	 * @param currentPage Page
	 * @param useZeroIndexForRank Use zero-based index for rank
	 * @param pageSize Page size
	 * @return Page of leaders as a list of LeaderData in the named leaderboard
	 */
	public List<LeaderData> departmentScoresIn(String companyId, String departmentId, int currentPage, boolean useZeroIndexForRank, int pageSize){
				
		List<LeaderData> leaderList = new ArrayList<LeaderData>();	
		String[] company_games_leaderboards = getCompanyGamesLeaderBoardIn(companyId);			
		if(company_games_leaderboards.length != 0) {
			_jedis.zunionstore("company_"+companyId+"_leaderboard", company_games_leaderboards);
		}
		ZParams params = new ZParams();
		params.weightsByDouble(0,1);
		_jedis.zinterstore("company_"+companyId+"_department_"+departmentId+"_leaderboard", params, "company_"+companyId+"_department_"+departmentId+"_members", "company_"+companyId+"_leaderboard");
		currentPage = (currentPage < 1) ? 1 : currentPage;		
		pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
		currentPage = (currentPage > totalPagesIn(this._leaderboardName, pageSize)) ? totalPagesIn(this._leaderboardName, pageSize) : currentPage;			
		int indexForRedis = currentPage - 1;
		int startingOffset = indexForRedis * pageSize;			
		startingOffset = (startingOffset < 0) ? 0 : startingOffset;			
		int endingOffset = (startingOffset + pageSize) - 1;					
		Set<Tuple> rawLeaderData = _jedis.zrevrangeWithScores("company_"+companyId+"_department_"+departmentId+"_leaderboard", startingOffset, endingOffset);			
		leaderList = massageLeaderData(this._leaderboardName, rawLeaderData, useZeroIndexForRank);		
		return leaderList;		
	}
	
	
	
	/**
	 * Retrieve a page of leaders as a list of LeaderData in the named company
	 *
	 * @param companyName CompanyName
	 * @param currentPage Page
	 * @param useZeroIndexForRank Use zero-based index for rank
	 * @param pageSize Page size
	 * @return Page of leaders as a list of LeaderData in the named leaderboard
	 */
	public List<LeaderData> groupScoresIn(String companyId, String groupId, int currentPage, boolean useZeroIndexForRank, int pageSize){
				
		List<LeaderData> leaderList = new ArrayList<LeaderData>();		
		String[] company_games_leaderboards = getCompanyGamesLeaderBoardIn(companyId);			
		if(company_games_leaderboards.length != 0) {
			_jedis.zunionstore("company_"+companyId+"_leaderboard", company_games_leaderboards);
		}
		ZParams params = new ZParams();
		params.weightsByDouble(0,1);
		_jedis.zinterstore("company_"+companyId+"_group_"+groupId+"_leaderboard", params, "company_"+companyId+"_group_"+groupId+"_members", "company_"+companyId+"_leaderboard");			
		currentPage = (currentPage < 1) ? 1 : currentPage;		
		pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;					
		currentPage = (currentPage > totalPagesIn("company_"+companyId+"_group_"+groupId+"_leaderboard", pageSize)) ? totalPagesIn(this._leaderboardName, pageSize) : currentPage;			
		int indexForRedis = currentPage - 1;
		int startingOffset = indexForRedis * pageSize;			
		startingOffset = (startingOffset < 0) ? 0 : startingOffset;			
		int endingOffset = (startingOffset + pageSize) - 1;								
		Set<Tuple> rawLeaderData = _jedis.zrevrangeWithScores("company_"+companyId+"_group_"+groupId+"_leaderboard", startingOffset, endingOffset);			
		leaderList = massageLeaderData(this._leaderboardName, rawLeaderData, useZeroIndexForRank);	
		return leaderList;		
	}
	
	
	
	

}
