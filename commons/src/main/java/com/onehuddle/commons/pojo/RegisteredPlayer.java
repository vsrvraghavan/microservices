package com.onehuddle.commons.pojo;

/**
 * @author ragha
 *
 */
public class RegisteredPlayer {
    private String companyID;
    private String gameID;
    private String locationID;
    private String departmentID;
    private String playerID;


    public String getCompanyID() { return companyID; }

    public void setCompanyID(String value) { this.companyID = value; }


    public String getGameID() { return gameID; }
    
    public void setGameID(String value) { this.gameID = value; }

    
    public String getLocationID() { return locationID; }
    
    public void setLocationID(String value) { this.locationID = value; }

    
    public String getDepartmentID() { return departmentID; }
    
    public void setDepartmentID(String value) { this.departmentID = value; }

    
    public String getPlayerID() { return playerID; }
    
    public void setPlayerID(String value) { this.playerID = value; }
}
