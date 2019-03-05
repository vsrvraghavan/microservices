/**
 * 
 */
package com.onehuddle.commons.pojo;


/**
 * @author ragha
 *
 */

public class PlayersAndPoint {
	
    private RegisteredPlayer registeredPlayer;
    private String contestTicket;
    private long pointsEarned;

    
    public RegisteredPlayer getRegisteredPlayer() { return registeredPlayer; }
    
    public void setRegisteredPlayer(RegisteredPlayer value) { this.registeredPlayer = value; }

    
    public String getContestTicket() { return contestTicket; }
    
    public void setContestTicket(String value) { this.contestTicket = value; }

    
    public long getPointsEarned() { return pointsEarned; }
    
    public void setPointsEarned(long value) { this.pointsEarned = value; }
}
