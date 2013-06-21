package br.net.woodstock.epm.impl.test;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.orm.DeploymentType;
import br.net.woodstock.epm.orm.Process;
import br.net.woodstock.epm.process.api.ProcessService;
import br.net.woodstock.rockframework.core.utils.IO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class LoopProcessServiceTest {

	@Autowired(required = true)
	private ProcessService	service;

	public LoopProcessServiceTest() {
		super();
	}

	@Test
	public void testAdd() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("loop-process.bpmn");
		byte[] bytes = IO.toByteArray(inputStream);

		Process businessProcess = new Process();
		businessProcess.setActive(Boolean.TRUE);
		businessProcess.setBin(bytes);
		businessProcess.setDescription("Loop Process");
		businessProcess.setName("loop-process");
		businessProcess.setType(DeploymentType.XML);

		this.service.save(businessProcess);
		System.out.println(businessProcess);
	}

	// @Test
	public void testImage() throws Exception {
		byte[] bytes = this.service.getProcessImageById(Integer.valueOf(9));
		FileOutputStream outputStream = new FileOutputStream("/tmp/loop-process.png");
		outputStream.write(bytes);
		outputStream.close();
	}

	// @Test
	public void testCreateSimpleProcess() throws Exception {
		Integer id = this.service.createSimpleProcess("1", Integer.valueOf(9));
		System.out.println(id);
	}

}
