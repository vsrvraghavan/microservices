/**
 * 
 */
package com.onehuddle.commons.pojo;

import com.onehuddle.commons.contest.pojo.*;


/**
 * @author ragha
 *
 */
public class ContestLeaderboardMessage {
    private LBMessageType type;
    private DashboardData content;
    private String messageFor;;

    public enum LBMessageType {
        DATA,
        JOIN,
        LEAVE
    }

    public LBMessageType getType() {
        return type;
    }

    public void setType(LBMessageType type) {
        this.type = type;
    }

  

	public DashboardData getContent() {
		return content;
	}

	public void setContent(DashboardData content) {
		this.content = content;
	}

	/**
	 * @return the messageFor
	 */
	public String getMessageFor() {
		return messageFor;
	}

	/**
	 * @param messageFor the messageFor to set
	 */
	public void setMessageFor(String messageFor) {
		this.messageFor = messageFor;
	}

}
