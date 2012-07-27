package br.net.woodstock.epm.process.activiti.test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.process.api.Activity;
import br.net.woodstock.epm.process.api.DateFormField;
import br.net.woodstock.epm.process.api.EnumFormField;
import br.net.woodstock.epm.process.api.Form;
import br.net.woodstock.epm.process.api.FormField;
import br.net.woodstock.epm.process.api.Process;
import br.net.woodstock.epm.process.api.ProcessInstance;
import br.net.woodstock.epm.process.api.ProcessService;
import br.net.woodstock.epm.process.api.Task;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class ProcessServiceTest {

	@Autowired(required = true)
	private ProcessService	service;

	@Test
	public void testConfig() {
		System.out.println("Service OK: " + this.service);
	}

	// @Test
	public void testListProcess() throws Exception {
		Collection<Process> collection = this.service.listProcessByName(null);
		for (Process process : collection) {
			System.out.println(process.getId() + " - " + process.getName() + "(" + process.getVersion() + ")");
		}
	}

	// @Test
	public void testAdd() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("epm-process.bpmn");
		byte[] bytes = IOUtils.toByteArray(inputStream);
		String id = this.service.deploy("epm-process", "epm-process.bpmn20.xml", bytes, false);
		System.out.println("Deploy ID: " + id); // 110
	}

	// @Test
	public void testListProcessByName() throws Exception {
		Collection<Process> collection = this.service.listProcessByName("EPM Test Process");
		for (Process process : collection) {
			System.out.println(process.getId() + " - " + process.getName() + "(" + process.getVersion() + ")");
		}
		collection = this.service.listProcessByName("EPM");
		for (Process process : collection) {
			System.out.println(process.getId() + " - " + process.getName() + "(" + process.getVersion() + ")");
		}
	}

	// @Test
	public void testStartProcess() throws Exception {
		Process process = this.service.getProcessById("epmprocess:2:213");
		if (process != null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("userId", "lourival.junior");
			String id = this.service.startProccess(process.getId(), "Test Lourival Sabino 2", parameters);
			System.out.println(id);
		} else {
			System.out.println("Processo nao encontrado");
		}
	}

	// @Test
	public void testInfoProcess() throws Exception {
		ProcessInstance pi = this.service.getProccessInstanceByKey("Test Lourival Sabino 2");
		System.out.println(pi.getId());
		System.out.println(pi.getKey());
		System.out.println(pi.isFinished());
		System.out.println(pi.isSuspended());
		if (pi.getProcess() != null) {
			System.out.println("\tProcess...");
			System.out.println("\t\t" + pi.getProcess().getId());
			System.out.println("\t\t" + pi.getProcess().getName());
			System.out.println("\t\t" + pi.getProcess().getVersion());
		}
		System.out.println("\tHistory");
		for (Activity a : pi.getHistory()) {
			System.out.println("\tActivity...");
			System.out.println("\t\t" + a.getId());
			System.out.println("\t\t" + a.getName());
			System.out.println("\t\t" + a.getType());
			System.out.println("\t\t" + a.getStart());
			System.out.println("\t\t" + a.getEnd());
			System.out.println("\t\t" + a.getUser());

			if (a.getUser() != null) {
				System.out.println("\t\tUser...");
				System.out.println("\t\t\t" + a.getUser().getEmail());
				System.out.println("\t\t\t" + a.getUser().getId());
				System.out.println("\t\t\t" + a.getUser().getName());
			}
		}
		System.out.println("\tCurrent");
		for (Activity a : pi.getCurrent()) {
			System.out.println("\tActivity...");
			System.out.println("\t\t" + a.getId());
			System.out.println("\t\t" + a.getName());
			System.out.println("\t\t" + a.getType());
			System.out.println("\t\t" + a.getStart());
			System.out.println("\t\t" + a.getEnd());
			System.out.println("\t\t" + a.getUser());

			if (a.getUser() != null) {
				System.out.println("\t\tUser...");
				System.out.println("\t\t\t" + a.getUser().getEmail());
				System.out.println("\t\t\t" + a.getUser().getId());
				System.out.println("\t\t\t" + a.getUser().getName());
			}
		}
	}

	@Test
	public void testImageProcess() throws Exception {
		String[] keys = new String[] { "Test Lourival Sabino 2", "Test Lourival Sabino" };
		for (String key : keys) {
			ProcessInstance pi = this.service.getProccessInstanceByKey(key);
			FileOutputStream fileOutputStream = new FileOutputStream("/tmp/" + pi.getKey() + ".png");
			byte[] image = this.service.getProcessInstanceImageById(pi.getId());
			fileOutputStream.write(image);
			fileOutputStream.close();
		}
	}

	// @Test
	public void testSetUserTask() throws Exception {
		Collection<Task> tasks = this.service.listTasksByProcessInstanceId("310");
		for (Task task : tasks) {
			this.service.assignTask(task.getId(), "kermit");
		}
	}

	// @Test
	public void testUserTask() throws Exception {
		Collection<Task> tasks = this.service.listTasksByProcessInstanceId("310");
		for (Task task : tasks) {
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getDescription());
			System.out.println(task.getStatus());
			System.out.println(task.getOwner());
			System.out.println(task.getUser());
		}

		tasks = this.service.listTasksByUser("kermit");
		for (Task task : tasks) {
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getDescription());
			System.out.println(task.getStatus());
			System.out.println(task.getOwner());
			System.out.println(task.getUser());
		}
	}

	// @Test
	public void testGetUserTaskForm() throws Exception {
		Form form = this.service.getForm("314");
		if (form != null) {
			System.out.println(form.getId());
			for (Entry<String, FormField> entry : form.getFields().entrySet()) {
				System.out.println("\t" + entry.getKey() + " => " + entry.getValue().getId() + " => " + entry.getValue().getType());
			}
		}
	}

	// @Test
	public void testSetUserTaskForm() throws Exception {
		Form form = this.service.getForm("314");
		form.getField("id").setValue("1");
		form.getField("name").setValue("1");
		form.getField("password").setValue("123456");
		if (form.getField("birthday") instanceof DateFormField) {
			DateFormField dff = (DateFormField) form.getField("birthday");
			System.out.println(dff.getPattern());
			dff.setValue("27/09/1999");
		}
		if (form.getField("status") instanceof EnumFormField) {
			EnumFormField eff = (EnumFormField) form.getField("status");
			System.out.println(eff.getValues());
			form.getField("status").setValue("active");
		}

		System.out.println("Definindo o Form");
		this.service.submitForm("314", form);
	}

}
