package com.grgbanking.electric.worker;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grgbanking.electric.entity.Result;
import com.grgbanking.electric.enums.OptEnum;
import com.grgbanking.electric.server.WorkServer;
import com.grgbanking.electric.service.IRecognitionService;

/**
 * 注册指静脉
 */
@Service
public class RegisterFingerVeinWorker extends WorkServer {
	
	@Autowired
	private IRecognitionService recognitionService;
	
	@PostConstruct
	public void init() {
		this.req = OptEnum.CreateFgVeinReq.name().toUpperCase();
		this.res = OptEnum.CreateFgVeinResp.name();
	}
	
	@Override
	protected Result process(String json, String ipaddr) {
		return recognitionService.registerFingerVein(json, ipaddr);
	}
}
