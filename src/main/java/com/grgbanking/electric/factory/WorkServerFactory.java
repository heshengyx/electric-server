package com.grgbanking.electric.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.grgbanking.electric.server.WorkServer;

@Component
public class WorkServerFactory {

	private List<WorkServer> servers;
	private Map<String, WorkServer> map;

	public List<WorkServer> getServers() {
		return servers;
	}

	@Autowired
	public void setServers(List<WorkServer> servers) {
		this.servers = servers;
		createMap();
	}

	private void createMap() {
		if (!CollectionUtils.isEmpty(servers)) {
			map = new HashMap<String, WorkServer>();
			for (WorkServer server : servers) {
				map.put(server.getReq(), server);
			}
		}
	}

	public WorkServer getWorkServer(String opt) {
		return (!CollectionUtils.isEmpty(map)) ? map.get(opt) : null;
	}
}
