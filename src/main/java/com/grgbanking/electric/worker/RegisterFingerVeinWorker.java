package com.grgbanking.electric.worker;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.grgbanking.electric.entity.Employee;
import com.grgbanking.electric.entity.FingerVein;
import com.grgbanking.electric.enums.OptEnum;
import com.grgbanking.electric.enums.StatusEnum;
import com.grgbanking.electric.json.Result;
import com.grgbanking.electric.server.WorkServer;
import com.grgbanking.electric.service.IEmployeeService;
import com.grgbanking.electric.service.IFingerVeinService;
import com.grgbanking.electric.util.BASE64Util;

import net.sf.json.JSONObject;

/**
 * 注册指静脉
 */
@Service
public class RegisterFingerVeinWorker extends WorkServer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RegisterFingerVeinWorker.class);
	
	@Autowired
	private IFingerVeinService fingerVeinService;
	
	@Autowired
	private IEmployeeService employeeService;
	
	@PostConstruct
	public void init() {
		this.req = OptEnum.CreateFgVeinReq.name().toUpperCase();
		this.res = OptEnum.CreateFgVeinResp.name();
	}
	
	@Override
	protected Result process(String json, String ip) {
		Result result = null;
		JSONObject jsonObject = JSONObject.fromObject(json);
		String code = jsonObject.optString("id");
		if (StringUtils.isEmpty(code)) {
			throw new IllegalArgumentException("工号不能为空");
		}
		String feature = jsonObject.optString("feature");
		if (StringUtils.isEmpty(feature)) {
			throw new IllegalArgumentException("特征值不能为空");
		}
		Employee employee = new Employee();
		employee.setCode(code);
		employee = employeeService.getData(employee);
		if (employee == null) {
			//工号不存在
			LOGGER.info("工号：" + code + "不存在");
			throw new IllegalArgumentException("工号不存在");
		} else {
			result = new Result();
			FingerVein fingerVein = new FingerVein();
			fingerVein.setSeq("01");
			fingerVein.setEmployeeId(employee.getId());
			fingerVein.setStatus(String.valueOf(StatusEnum.SUCCESS.getValue()));
			try {
				fingerVein.setFeature(BASE64Util.decoder(feature));
			} catch (IOException e) {
			}
			fingerVeinService.register(fingerVein);
			result.setCode(String.valueOf(StatusEnum.SUCCESS.getValue()));
			result.setMessage(employee.getName());
		}
		return result;
	}
}
