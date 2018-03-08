/**
 * 
 */
package com.onehuddle.leaderboard.pojo;

import java.util.Hashtable;
import java.util.List;

import com.onehuddle.commons.pojo.LeaderData;

/**
 * @author ragha
 *
 */
public class MyLeaderBoardData {

	/**
	 * 
	 */
	
	private Hashtable<String, Object> _memberdata;
	private List<LeaderData> _leaderdata;
	
	
	public MyLeaderBoardData() {
		// TODO Auto-generated constructor stub
	}

	
	public MyLeaderBoardData(Hashtable<String, Object> memberdata, List<LeaderData> leaderdata) {
		_memberdata = memberdata;
		_leaderdata = leaderdata;
		
	}


	public Hashtable<String, Object> getMemberdata() {
		return _memberdata;
	}


	public void setMemberdata(Hashtable<String, Object> _memberdata) {
		this._memberdata = _memberdata;
	}


	public List<LeaderData> getLeaderdata() {
		return _leaderdata;
	}


	public void setLeaderdata(List<LeaderData> _leaderdata) {
		this._leaderdata = _leaderdata;
	}
	
}
