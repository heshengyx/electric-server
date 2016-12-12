package com.grgbanking.electric.server;

import org.apache.log4j.Logger;

import com.grgbanking.electric.entity.Result;
import com.grgbanking.electric.enums.StatusEnum;
import com.grgbanking.electric.json.JSONResult;


/**
 * 模板
 * @author
 *
 */
public abstract class WorkServer {

	private static final Logger LOGGER = Logger.getLogger(WorkServer.class);

	protected String req; //请求业务类型
	protected String res; //应答业务类型

	public final String getReq() {
		return this.req;
	}
	
	public final String getRes() {
		return this.res;
	}

	/**
	 * 模板方法
	 * @param json
	 * @return
	 */
	public final String handler(String json, String ipaddr) {
		JSONResult jsonResult = null;
		try {
			Result result = process(json, ipaddr);
			if (result != null) {
				String message = result.getMessage();
				if (message.contains("=")) {
					String[] msg = message.split("[=]");
					jsonResult = new JSONResult(getRes(), result.getCode(), msg[0]);
					jsonResult.setCode(msg[1]);
					jsonResult.setNum(msg[2]);
					jsonResult.setSimilarity(msg[3]);
					if (msg.length > 4)
						jsonResult.setDeptName(msg[4]);
				} else {
					jsonResult = new JSONResult(getRes(), result.getCode(),
							result.getMessage());
				}
			} else {
				throw new IllegalArgumentException("业务异常");
			}
		} catch (IllegalArgumentException e) {
			//业务异常
			jsonResult = new JSONResult(getRes(), String.valueOf(StatusEnum.FAIL
					.getValue()), e.getMessage());
		} catch (Exception e) {
			//系统异常
			LOGGER.error("系统异常", e);
			jsonResult = new JSONResult(getRes(), String.valueOf(StatusEnum.FAIL
					.getValue()), "系统异常");
		}
		return jsonResult.toJson();
	}
	
	/**
	 * 业务处理
	 * @param json
	 * @return
	 */
	protected abstract Result process(String json, String ipaddr);
}
