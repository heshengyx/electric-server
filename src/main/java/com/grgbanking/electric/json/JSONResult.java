package com.grgbanking.electric.json;

import java.io.Serializable;

public class JSONResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private String type;
	private String code;
	private String status;
	private String message;

	public JSONResult() {}
	
	public JSONResult(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toJson() {
		StringBuilder sb = new StringBuilder("");
		sb.append("{'type':'").append(type);
		sb.append("','code':'").append(code);
		sb.append("','message':'").append(message);
		sb.append("'}");
		return sb.toString();
	}
}
