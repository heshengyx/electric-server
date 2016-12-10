package com.grgbanking.electric.server;

import org.apache.log4j.Logger;

import com.grgbanking.electric.enums.StatusEnum;
import com.grgbanking.electric.json.JSONResult;
import com.grgbanking.electric.json.Result;


/**
 * 模板
 * @author
 *
 */
public abstract class WorkServer {

	private static final Logger LOGGER = Logger.getLogger(WorkServer.class);

	protected String opt; //业务类型

	public final String getOpt() {
		return this.opt;
	}

	/**
	 * 模板方法
	 * @param json
	 * @return
	 */
	public final String handler(String json, String ip) {
		JSONResult jsonResult = null;
		try {
			Result result = process(json, ip);
			if (result != null) {
				jsonResult = new JSONResult(result.getCode(),
						result.getMessage());
			} else {
				jsonResult = new JSONResult();
			}
		} catch (IllegalArgumentException e) {
			//业务异常
			jsonResult = new JSONResult(String.valueOf(StatusEnum.ERROR
					.getValue()), e.getMessage());
		} catch (Exception e) {
			//系统异常
			LOGGER.error("系统异常", e);
			jsonResult = new JSONResult(String.valueOf(StatusEnum.ERROR
					.getValue()), "系统异常");
		}
		return jsonResult.toJson();
	}
	
	/**
	 * 业务处理
	 * @param json
	 * @return
	 */
	protected abstract Result process(String json, String ip);
}
