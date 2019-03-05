package com.onehuddle.commons.contest.pojo;

import java.util.*;

public class GameLB {
    private String gameID;
    private List<LB> lb;

    public String getGameID() { return gameID; }
    public void setGameID(String value) { this.gameID = value; }

    public List<LB> getLB() { return lb; }
    public void setLB(List<LB> value) { this.lb = value; }
}
