package com.grgbanking.electric.worker;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grgbanking.electric.entity.Result;
import com.grgbanking.electric.enums.OptEnum;
import com.grgbanking.electric.server.WorkServer;
import com.grgbanking.electric.service.IRecognitionService;

/**
 * 时间同步
 */
@Service
public class SyncTimeWorker extends WorkServer {

	@Autowired
	private IRecognitionService recognitionService;
	
	@PostConstruct
	public void init() {
		this.req = OptEnum.SyncTime.name().toUpperCase();
		this.res = OptEnum.SyncTime.name();
	}
	
	@Override
	protected Result process(String json, String ipaddr) {
		return recognitionService.syncTime(json, ipaddr);
	}

}
