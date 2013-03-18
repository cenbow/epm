package br.net.woodstock.epm.impl.test;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.orm.BusinessProcess;
import br.net.woodstock.epm.orm.BusinessProcessBinType;
import br.net.woodstock.epm.process.api.BusinessProcessService;
import br.net.woodstock.rockframework.core.utils.IO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class BusinessProcessServiceTest {

	@Autowired(required = true)
	private BusinessProcessService	service;

	public BusinessProcessServiceTest() {
		super();
	}

	// @Test
	// public void testConfig() {
	// System.out.println("Service OK: " + this.service);
	// }

	// @Test
	// public void testListProcess() throws Exception {
	// ProcessDefinition[] array = this.service.listProcessByName(null);
	// for (ProcessDefinition process : array) {
	// System.out.println(process.getId() + " - " + process.getName() + "(" + process.getVersion() + ")");
	// }
	// }

	// @Test
	public void testAdd() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("epm-process.bpmn");
		byte[] bytes = IO.toByteArray(inputStream);

		BusinessProcess businessProcess = new BusinessProcess();
		businessProcess.setActive(Boolean.TRUE);
		businessProcess.setBin(bytes);
		businessProcess.setDescription("EPM Process");
		businessProcess.setName("epm-process");
		businessProcess.setType(BusinessProcessBinType.XML);

		this.service.save(businessProcess);
		System.out.println(businessProcess);
	}

	@Test
	public void testImage() throws Exception {
		byte[] bytes = this.service.getBusinessProcessImage(Integer.valueOf(2));
		FileOutputStream outputStream = new FileOutputStream("/tmp/epm-process.png");
		outputStream.write(bytes);
		outputStream.close();
	}

}