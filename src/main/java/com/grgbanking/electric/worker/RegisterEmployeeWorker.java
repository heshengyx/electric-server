package com.grgbanking.electric.worker;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.grgbanking.electric.entity.Employee;
import com.grgbanking.electric.enums.OptEnum;
import com.grgbanking.electric.enums.StatusEnum;
import com.grgbanking.electric.json.Result;
import com.grgbanking.electric.server.WorkServer;
import com.grgbanking.electric.service.IEmployeeService;

import net.sf.json.JSONObject;

/**
 * 注册用户
 */
@Service
public class RegisterEmployeeWorker extends WorkServer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RegisterEmployeeWorker.class);
	
	@Value("${fingervein.password}")
	private String password;
	
	@Autowired
	private IEmployeeService employeeService;
	
	@PostConstruct
	public void init() {
		this.req = OptEnum.CreatePplReq.name().toUpperCase();
		this.res = OptEnum.CreatePplResp.name();
	}
	
	@Override
	protected Result process(String json, String ip) {
		Result result = null;
		JSONObject jsonObject = JSONObject.fromObject(json);
		String code = jsonObject.optString("id");
		if (StringUtils.isEmpty(code)) {
			throw new IllegalArgumentException("工号不能为空");
		} else {
			result = new Result();
			Employee employee = new Employee();
			employee.setCode(code);
			employee = employeeService.getData(employee);
			if (employee == null) {
				//工号不存在
				LOGGER.info("工号：" + code + "不存在");
				employee = new Employee();
				employee.setName(code);
				employee.setCode(code);
				employee.setCreateBy(OptEnum.SYSTEM.name().toLowerCase());
				employeeService.save(employee);
			}
			String password = jsonObject.optString("password");
			if (this.password != null && this.password.equals(password)) {
				result.setCode(String.valueOf(StatusEnum.SUCCESS.getValue()));
				result.setMessage(employee.getName());
			} else {
				LOGGER.info("密码不正确");
				throw new IllegalArgumentException("密码不正确");
			}
		}
		return result;
	}

}
