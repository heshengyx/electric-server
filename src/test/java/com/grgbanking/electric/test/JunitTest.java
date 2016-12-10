package com.grgbanking.electric.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.grgbanking.electric.entity.Organization;
import com.grgbanking.electric.entity.Terminal;
import com.grgbanking.electric.page.IPage;
import com.grgbanking.electric.param.OrganizationQueryParam;
import com.grgbanking.electric.service.IOrganizationService;
import com.grgbanking.electric.service.ITerminalService;
import com.grgbanking.electric.service.IUserService;

public class JunitTest extends BaseJunitTest {

	@Autowired
	private ITerminalService terminalService;
	
	@Test
	public void testGetTerminal() {
		Terminal t = terminalService.getById("0791D39DC62043A784F492A4CB399B49");
		System.out.println(t.getName());
	}
	
	public static void main(String[] args) {
		String ip = "10.1.53.84";
		String code = ip.replaceAll("[.]", "");
		System.out.println(code);
	}
}
