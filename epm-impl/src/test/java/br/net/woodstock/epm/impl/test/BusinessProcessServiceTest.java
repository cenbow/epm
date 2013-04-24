package br.net.woodstock.epm.impl.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.orm.Process;
import br.net.woodstock.epm.orm.DeploymentType;
import br.net.woodstock.epm.process.api.ProcessService;
import br.net.woodstock.rockframework.core.utils.IO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class BusinessProcessServiceTest {

	@Autowired(required = true)
	private ProcessService	service;

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

		Process businessProcess = new Process();
		businessProcess.setActive(Boolean.TRUE);
		businessProcess.setBin(bytes);
		businessProcess.setDescription("EPM Process");
		businessProcess.setName("epm-process");
		businessProcess.setType(DeploymentType.XML);

		this.service.save(businessProcess);
		System.out.println(businessProcess);
	}

	@Test
	public void testAddFile() throws Exception {
		File file = new File("/home/lourival/workspaces/workspace-activiti/activiti/src/main/resources/diagrams/MyProcess.bpmn");
		byte[] bytes = IO.toByteArray(file);

		Process businessProcess = new Process();
		businessProcess.setActive(Boolean.TRUE);
		businessProcess.setBin(bytes);
		businessProcess.setDescription("My Process");
		businessProcess.setName("MyProcess");
		businessProcess.setType(DeploymentType.XML);

		this.service.save(businessProcess);
		System.out.println(businessProcess);
	}

	// @Test
	public void testImage() throws Exception {
		byte[] bytes = this.service.getBusinessProcessImage(Integer.valueOf(2));
		FileOutputStream outputStream = new FileOutputStream("/tmp/epm-process.png");
		outputStream.write(bytes);
		outputStream.close();
	}

}
