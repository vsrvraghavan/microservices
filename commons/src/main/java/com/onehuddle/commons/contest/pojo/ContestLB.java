package com.onehuddle.commons.contest.pojo;

import java.util.*;

public class ContestLB {
    private List<LB> lb;
    private List<GameLB> gameLB;
    private List<DepartmentLB> departmentLB;
    private List<LocationLB> locationLB;

    public List<LB> getLB() { return lb; }
    public void setLB(List<LB> value) { this.lb = value; }

    public List<GameLB> getGameLB() { return gameLB; }
    public void setGameLB(List<GameLB> value) { this.gameLB = value; }

    public List<DepartmentLB> getDepartmentLB() { return departmentLB; }
    public void setDepartmentLB(List<DepartmentLB> value) { this.departmentLB = value; }

    public List<LocationLB> getLocationLB() { return locationLB; }
    public void setLocationLB(List<LocationLB> value) { this.locationLB = value; }
}
