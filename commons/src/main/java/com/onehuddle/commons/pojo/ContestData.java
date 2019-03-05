/**
 * 
 */
package com.onehuddle.commons.pojo;

import java.util.List;

/**
 * @author ragha
 *
 */
public class ContestData {
    private String companyName;
    private String contestID;
    private String contestStatus;
    private List<String> gamesAllowed;
    private List<LocationsAndDepartments> locationsAndDepartments;
    private List<PlayersAndPoint> playersAndPoints;


    public String getCompanyName() { return companyName; }
    
    public void setCompanyName(String value) { this.companyName = value; }

    
    public String getContestID() { return contestID; }
    
    public void setContestID(String value) { this.contestID = value; }

    
    public String getContestStatus() { return contestStatus; }
    
    public void setContestStatus(String value) { this.contestStatus = value; }

    
    public List<String> getGamesAllowed() {
		return gamesAllowed;
	}

	public void setGamesAllowed(List<String> gamesAllowed) {
		this.gamesAllowed = gamesAllowed;
	}

	public List<LocationsAndDepartments> getLocationsAndDepartments() { return locationsAndDepartments; }

	public void setLocationsAndDepartments(List<LocationsAndDepartments> value) { this.locationsAndDepartments = value; }

	public List<PlayersAndPoint> getPlayersAndPoints() { return playersAndPoints; }
    
    public void setPlayersAndPoints(List<PlayersAndPoint> value) { this.playersAndPoints = value; }
}

