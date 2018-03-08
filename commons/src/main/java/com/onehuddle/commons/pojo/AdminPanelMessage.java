/**
 * 
 */
package com.onehuddle.commons.pojo;


/**
 * @author ragha
 *
 */
public class AdminPanelMessage {
    private AdminPanelMessageType type;
    private AdminPanelMessageData content;
    private String messageFor;

    public enum AdminPanelMessageType {
        DATA,
        JOIN,
        LEAVE
    }

	/**
	 * @return the type
	 */
	public AdminPanelMessageType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AdminPanelMessageType type) {
		this.type = type;
	}

	/**
	 * @return the content
	 */
	public AdminPanelMessageData getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(AdminPanelMessageData content) {
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

