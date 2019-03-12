package com.onehuddle.commons.contest.pojo;

public class DashboardData {
    
	private String contestName;
	private ContestLB contestLB;

	
    public String getContestName() {
		return contestName;
	}
	public void setContestName(String contestName) {
		this.contestName = contestName;
	}
	
	public ContestLB getContestLB() { return contestLB; }
    public void setContestLB(ContestLB value) { this.contestLB = value; }
}
