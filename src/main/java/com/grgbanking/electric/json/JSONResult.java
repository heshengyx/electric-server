package com.grgbanking.electric.json;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class JSONResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type; // 消息类型
	private String retcode; // 状态码
	private String name; // 消息
	private String code; // 证件号
	private String num; // 手指序号
	private String similarity; // 相似度
	private String deptName; // 部门名称

	public JSONResult() {
	}

	public JSONResult(String type, String retcode, String name) {
		this.type = type;
		this.retcode = retcode;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getSimilarity() {
		return similarity;
	}

	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toJson() {
		StringBuilder sb = new StringBuilder("");
		sb.append("{'type':'").append(type);
		sb.append("','retcode':'").append(retcode);
		sb.append("','name':'").append(name);
		sb.append("','identityID':'").append(code);
		sb.append("','fingerID':'").append(num);
		sb.append("','Similarity':'").append(similarity);
		if (!StringUtils.isEmpty(deptName)) {
			sb.append("','department':'").append(deptName);
		}
		sb.append("'}");
		return sb.toString();
	}

	public String toTimeJson() {
		StringBuilder sb = new StringBuilder("");
		sb.append("{'type':'").append(type);
		sb.append("','retcode':'").append(retcode);
		sb.append("','srvTime':'").append(name);
		sb.append("'}");
		return sb.toString();
	}
}
