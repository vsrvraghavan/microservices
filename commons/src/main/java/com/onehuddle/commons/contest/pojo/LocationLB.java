package com.onehuddle.commons.contest.pojo;

import java.util.*;

public class LocationLB {
    private String locationID;
    private List<LB> lb;
    private List<GameLB> gameLB;
    private List<DepartmentLB> departmentLB;

    public String getLocationID() { return locationID; }
    public void setLocationID(String value) { this.locationID = value; }

    public List<LB> getLB() { return lb; }
    public void setLB(List<LB> value) { this.lb = value; }

    public List<GameLB> getGameLB() { return gameLB; }
    public void setGameLB(List<GameLB> value) { this.gameLB = value; }

    public List<DepartmentLB> getDepartmentLB() { return departmentLB; }
    public void setDepartmentLB(List<DepartmentLB> value) { this.departmentLB = value; }
}
