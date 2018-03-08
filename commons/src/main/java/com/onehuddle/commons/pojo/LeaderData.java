/**
 * 
 */
package com.onehuddle.commons.pojo;

/**
 * @author ragha
 *
 */

public class LeaderData {

	private String member;
	private Double score;
	private Long rank;
	private String gameId;
	private String department;
	private String group;
	
	
	
	public LeaderData() {
		
	}
	/**
	 * Store leader data
	 * 
	 * @param member Name
	 * @param score Score
	 * @param rank Rank
	 */
	public LeaderData(String member, double score, long rank) {
		this.member = member;
		this.score = score;
		this.rank = rank;		
	}
	
	
	/**
	 * Store leader data
	 * 
	 * @param member Name
	 * @param score Score
	 * @param rank Rank
	 */
	public LeaderData(String member, double score, long rank, String gameID) {
		this.member = member;
		this.score = score;
		this.rank = rank;	
		this.gameId = gameID;
	}
	
	/**
	 * Set the member name
	 * 
	 * @param member Member name
	 */
	public void setMember(String member) {
		this.member = member;
	}
	
	/**
	 * Get the member name
	 * 
	 * @return Member name
	 */
	public String getMember() {
		return member;
	}
	
	/**
	 * Set the score
	 * 
	 * @param score Score
	 */
	public void setScore(double score) {
		this.score = score;
	}
	
	/**
	 * Get the score
	 * 
	 * @return Score
	 */
	public double getScore() {
		return score;
	}
	
	/**
	 * Set the rank
	 * 
	 * @param rank Rank
	 */
	public void setRank(long rank) {
		this.rank = rank;
	}
	
	/**
	 * Get the rank
	 * 
	 * @return Rank
	 */
	public long getRank() {
		return rank;
	}
	
	
	/**
	 * @return the gameId
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * Set the Department
	 * 
	 * @param department Department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	
	/**
	 * Get the department
	 * 
	 * @return Department
	 */
	public String getDepartment() {
		return department;
	}
	
	/**
	 * Set the group
	 * 
	 * @param group Group
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * Get the group
	 * 
	 * @return Group
	 */
	public String getGroup() {
		return group;
	}
	
	
}
