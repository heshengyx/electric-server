package com.grgbanking.electric.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.grgbanking.electric.server.WorkNioServer;

public class WorkListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkListener.class);

	private WorkNioServer server;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (server != null)
			server.shutdown();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 读取配置信息
		String serverPort = sce.getServletContext().getInitParameter(
				"serverPort");
		server = new WorkNioServer(Integer.parseInt(serverPort));

		try {
			LOGGER.info("服务正在启动中...");
			// 以下代码会阻塞
			server.run();
		} catch (Exception e) {
			LOGGER.error("服务启动失败", e);
		}
	}
}
