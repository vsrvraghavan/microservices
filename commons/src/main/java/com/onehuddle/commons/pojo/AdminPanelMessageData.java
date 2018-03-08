/**
 * 
 */
package com.onehuddle.commons.pojo;

import java.util.List;


/**
 * @author ragha
 *
 */
public class AdminPanelMessageData {

	
	
	private String gameSessionsLaunched;
	private String gameSessionsFinishedByPlayer;
	private String gameSessionsFinishedByManager;
	private String gameSessionsFinishedByTimeout;
	private List<LeaderData> lb1;
	private List<LeaderData> lb2;
	
	private List<LeaderData> lbD;
	private List<LeaderData> lbC;
	
	/**
	 * @return the gameSessionsLaunched
	 */
	public String getGameSessionsLaunched() {
		return gameSessionsLaunched;
	}
	/**
	 * @param gameSessionsLaunched the gameSessionsLaunched to set
	 */
	public void setGameSessionsLaunched(String gameSessionsLaunched) {
		this.gameSessionsLaunched = gameSessionsLaunched;
	}
	/**
	 * @return the gameSessionsFinishedByPlayer
	 */
	public String getGameSessionsFinishedByPlayer() {
		return gameSessionsFinishedByPlayer;
	}
	/**
	 * @param gameSessionsFinishedByPlayer the gameSessionsFinishedByPlayer to set
	 */
	public void setGameSessionsFinishedByPlayer(String gameSessionsFinishedByPlayer) {
		this.gameSessionsFinishedByPlayer = gameSessionsFinishedByPlayer;
	}
	/**
	 * @return the gameSessionsFinishedByManager
	 */
	public String getGameSessionsFinishedByManager() {
		return gameSessionsFinishedByManager;
	}
	/**
	 * @param gameSessionsFinishedByManager the gameSessionsFinishedByManager to set
	 */
	public void setGameSessionsFinishedByManager(String gameSessionsFinishedByManager) {
		this.gameSessionsFinishedByManager = gameSessionsFinishedByManager;
	}
	/**
	 * @return the gameSessionsFinishedByTimeout
	 */
	public String getGameSessionsFinishedByTimeout() {
		return gameSessionsFinishedByTimeout;
	}
	/**
	 * @param gameSessionsFinishedByTimeout the gameSessionsFinishedByTimeout to set
	 */
	public void setGameSessionsFinishedByTimeout(String gameSessionsFinishedByTimeout) {
		this.gameSessionsFinishedByTimeout = gameSessionsFinishedByTimeout;
	}
	/**
	 * @return the lb1
	 */
	public List<LeaderData> getLb1() {
		return lb1;
	}
	/**
	 * @param lb1 the lb1 to set
	 */
	public void setLb1(List<LeaderData> lb1) {
		this.lb1 = lb1;
	}
	/**
	 * @return the lb2
	 */
	public List<LeaderData> getLb2() {
		return lb2;
	}
	/**
	 * @param lb2 the lb2 to set
	 */
	public void setLb2(List<LeaderData> lb2) {
		this.lb2 = lb2;
	}
	/**
	 * @return the lbD
	 */
	public List<LeaderData> getLbD() {
		return lbD;
	}
	/**
	 * @param lbD the lbD to set
	 */
	public void setLbD(List<LeaderData> lbD) {
		this.lbD = lbD;
	}
	/**
	 * @return the lbC
	 */
	public List<LeaderData> getLbC() {
		return lbC;
	}
	/**
	 * @param lbC the lbC to set
	 */
	public void setLbC(List<LeaderData> lbC) {
		this.lbC = lbC;
	}

}
