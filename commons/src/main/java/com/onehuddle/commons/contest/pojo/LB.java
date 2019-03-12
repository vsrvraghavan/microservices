package com.onehuddle.commons.contest.pojo;

public class LB {
    private String name;
    private Double score;
    private Long rank;
    
    public LB() {
    	
    }
    public LB(String name, Double score, Long rank) {
    	
    	this.name = name;
    	this.score = score;
    	this.rank = rank;
    }

    public String getName() { return this.name; }
    public void setName(String value) { this.name = value; }

    public Double getScore() { return this.score; }
    public void setScore(Double value) { this.score = value; }

    public Long getRank() { return this.rank; }
    public void setRank(Long value) { this.rank = value; }
}
