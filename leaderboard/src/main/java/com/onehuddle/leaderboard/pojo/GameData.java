/**
 * 
 */
package com.onehuddle.leaderboard.pojo;

import java.util.Date;

/**
 * @author ragha
 *
 */
public class GameData {

	private int gameID;
	private String gameName;
	private int companyID;
	private String companyName;
	private Date validFrom;
	private Date validTill;
	private String gameType;
	
	
	/**
	 * 
	 */
	public GameData() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the gameID
	 */
	public int getGameID() {
		return gameID;
	}


	/**
	 * @param gameID the gameID to set
	 */
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}


	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}


	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}


	/**
	 * @return the companyID
	 */
	public int getCompanyID() {
		return companyID;
	}


	/**
	 * @param companyID the companyID to set
	 */
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}


	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}


	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	/**
	 * @return the validFrom
	 */
	public Date getValidFrom() {
		return validFrom;
	}


	/**
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}


	/**
	 * @return the validTill
	 */
	public Date getValidTill() {
		return validTill;
	}


	/**
	 * @param validTill the validTill to set
	 */
	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}


	/**
	 * @return the gameType
	 */
	public String getGameType() {
		return gameType;
	}


	/**
	 * @param gameType the gameType to set
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

}
