package com.onehuddle.commons.contest.pojo;

import java.util.*;

public class DepartmentLB {
    private String departmentID;
    private List<LB> lb;
    private List<GameLB> gameLB;

    public String getDepartmentID() { return departmentID; }
    public void setDepartmentID(String value) { this.departmentID = value; }

    public List<LB> getLB() { return lb; }
    public void setLB(List<LB> value) { this.lb = value; }

    public List<GameLB> getGameLB() { return gameLB; }
    public void setGameLB(List<GameLB> value) { this.gameLB = value; }
}
