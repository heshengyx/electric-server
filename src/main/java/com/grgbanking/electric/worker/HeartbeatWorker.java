package com.grgbanking.electric.worker;

import java.util.Date;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grgbanking.electric.entity.Terminal;
import com.grgbanking.electric.enums.OptEnum;
import com.grgbanking.electric.enums.StatusEnum;
import com.grgbanking.electric.json.Result;
import com.grgbanking.electric.server.WorkServer;
import com.grgbanking.electric.service.ITerminalService;

@Service
public class HeartbeatWorker extends WorkServer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HeartbeatWorker.class);

	@Autowired
	private ITerminalService terminalService;

	@PostConstruct
	public void init() {
		this.opt = OptEnum.HeartBeat.name().toUpperCase();
	}

	@Override
	protected Result process(String json, String ip) {
		JSONObject jsonObject = JSONObject.fromObject(json);

		String code = ip.replaceAll("[.]", "");
		Terminal terminal = new Terminal();
		terminal.setCode(code);
		terminal = terminalService.getData(terminal);
		if (terminal == null) {
			terminal = new Terminal();
			terminal.setName("指静脉终端");
			terminal.setCode(code);
			terminal.setHeartbeat(new Date()); // 当前心跳时间
			terminal.setStatus(String.valueOf(StatusEnum.SUCCESS.getValue())); // 1在线,0断线
			terminal.setCreateBy("system");
			terminalService.save(terminal);
		} else {
			terminal.setHeartbeat(new Date());
			terminal.setStatus(String.valueOf(StatusEnum.SUCCESS.getValue()));
			terminalService.update(terminal);
		}
		return null;
	}

}
