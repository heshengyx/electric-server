package com.grgbanking.electric.worker;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.grgbanking.electric.entity.FingerVein;
import com.grgbanking.electric.enums.OptEnum;
import com.grgbanking.electric.enums.StatusEnum;
import com.grgbanking.electric.json.Result;
import com.grgbanking.electric.server.WorkServer;
import com.grgbanking.electric.service.IFingerVeinService;
import com.grgbanking.electric.util.BASE64Util;
import com.grgbanking.electric.util.DateUtil;
import com.grgbanking.electric.util.FileUtil;

import net.sf.json.JSONObject;

/**
 * 识别指静脉
 */
@Service
public class RecognitionFingerVeinWorker extends WorkServer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RecognitionFingerVeinWorker.class);
	
	@Autowired
	private IFingerVeinService fingerVeinService;
	
	@Value("${fingervein.folder}")
	private String folder;
	
	@PostConstruct
	public void init() {
		this.req = OptEnum.MatchFgVeinReq.name().toUpperCase();
		this.res = OptEnum.MatchFgVeinResp.name();
	}
	
	@Override
	protected Result process(String json, String ip) {
		Result result = null;
		JSONObject jsonObject = JSONObject.fromObject(json);
		String sample = jsonObject.getString("sample");
		if (StringUtils.isEmpty(sample)) {
			throw new IllegalArgumentException("特征值不能为空");
		}
		FingerVein fingerVein = new FingerVein();
		try {
			fingerVein.setFeature(BASE64Util.decoder(sample));
		} catch (IOException e) {
		}
		String message = fingerVeinService.recognition(fingerVein, ip);
		try {
			writeFile(message.toString(), sample, ip);
		} catch (Exception e) {
			LOGGER.error("文件写入失败", e);
		}
		result = new Result();
		result.setCode(String.valueOf(StatusEnum.SUCCESS.getValue()));
		result.setMessage(message);
		return result;
	}

	private void writeFile(String result, String sample, String ip) {
		String folder = this.folder;
		String[] message = result.split("[=]");
		if ("1".equals(message[4])) {
			folder += File.separator + "accept";
		} else {
			folder += File.separator + "reject";
		}
		
		//创建文件目录
		File file = FileUtil.mkdirs(folder, ip);
		String fileName = message[1] + "_" + message[2] + "_" + message[0];
		if (message.length > 5) {
			fileName += "_" + message[5];
		}
		String date = DateUtil.getDateTime(new Date());
		fileName += "_" + message[3] + "_" + date.replaceAll(" ", "_").replaceAll(":", "-");
		FileUtil.writeFile(file.getPath() + File.separator + fileName + ".bin", sample);
	}
}
