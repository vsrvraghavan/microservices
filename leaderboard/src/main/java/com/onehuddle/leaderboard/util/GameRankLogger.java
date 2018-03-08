/**
 * 
 */
package com.onehuddle.leaderboard.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onehuddle.leaderboard.pojo.GameScoreData;

/**
 * @author ragha
 *
 */
public class GameRankLogger {
	private static final Logger logger = LoggerFactory.getLogger(GameRankLogger.class);
	/**
	 * 
	 */
	public GameRankLogger() {
		// TODO Auto-generated constructor stub
	}
	
	public void log(GameScoreData gameData, Long rank) {
		log(gameData, rank, gameData.getScore());
	}
	
	public void log(GameScoreData gameData, Long rank, Double score) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		System.out.println("Logging Game Data");
		logger.debug("Snapshot  taken at : "+dateFormat.format(cal.getTime())+" | Game ID : "+gameData.getGameID()
				+" | Time Zone Applicable : "+gameData.getTimezoneApplicable()
				+" | Company ID : "+ gameData.getCompanyID() 
				+" | Department ID : "+ gameData.getDepartmentID() 
				+" | PlayerID : "+ gameData.getPlayerID() 
				+" | Game ID : "+	gameData.getGameID()
				+" | Game Session ID : "+ gameData.getGameSessionUUID()
				+" | Group ID : "+ gameData.getGroupID() 
				+" | Rank : "+ rank 
				+" | Score : "+ score
 				);		
	}

}
