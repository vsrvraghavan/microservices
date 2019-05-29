/**
 * 
 */
package com.onehuddle.leaderboard.pojo;

import java.util.Date;

import org.springframework.http.HttpStatus;

/**
 * @author ragha
 *
 */
public class ExceptionResponse {
	private Date timestamp;
	private String message;
	private Integer errorcode;
	private HttpStatus status;
	
	
	public ExceptionResponse(Date timestamp, String message, Integer errorcode, HttpStatus status) {
		this.timestamp =  timestamp;
		this.message = message;
		this.errorcode =  errorcode;
		this.status = status;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
