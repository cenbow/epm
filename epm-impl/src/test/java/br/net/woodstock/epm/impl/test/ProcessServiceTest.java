package br.net.woodstock.epm.impl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.process.api.Activity;
import br.net.woodstock.epm.process.api.DeploymentType;
import br.net.woodstock.epm.process.api.Form;
import br.net.woodstock.epm.process.api.FormField;
import br.net.woodstock.epm.process.api.ProcessDefinition;
import br.net.woodstock.epm.process.api.ProcessInstance;
import br.net.woodstock.epm.process.api.ProcessService;
import br.net.woodstock.epm.process.api.Task;
import br.net.woodstock.rockframework.utils.ConditionUtils;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class ProcessServiceTest {

	@Autowired(required = true)
	private ProcessService	service;

	public ProcessServiceTest() {
		super();
	}

	@Test
	public void testConfig() {
		System.out.println("Service OK: " + this.service);
	}

	@Test
	public void testListProcess() throws Exception {
		ProcessDefinition[] array = this.service.listProcessByName(null);
		for (ProcessDefinition process : array) {
			System.out.println(process.getId() + " - " + process.getName() + "(" + process.getVersion() + ")");
		}
	}

	// @Test
	public void testAdd() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("epm-process.bpmn");
		byte[] bytes = IOUtils.toByteArray(inputStream);
		String id = this.service.deploy("epm-process", bytes, DeploymentType.XML);
		System.out.println("Deploy ID: " + id); // 110
	}

	// @Test
	public void testAddSub() throws Exception {
		// String[] files = new String[] { "/tmp/sub-process.bpmn", "/tmp/main-process.bpmn" };
		// String[] files = new String[] { "/tmp/sub-process.bpmn" };
		String[] files = new String[] { "/home/lourival/workspaces/projects-woodstock/epm/epm-process-activiti/src/test/resources/test-process.bpmn" };
		for (String file : files) {
			File f = new File(file);
			String name = f.getName().substring(0, f.getName().indexOf("."));
			InputStream inputStream = new FileInputStream(f);
			byte[] bytes = IOUtils.toByteArray(inputStream);
			String id = this.service.deploy(name, bytes, DeploymentType.XML);
			System.out.println("Deploy ID: " + id); // 110
			inputStream.close();
		}
	}

	// @Test
	public void testListProcessByName() throws Exception {
		ProcessDefinition[] array = this.service.listProcessByName(null);
		for (ProcessDefinition process : array) {
			System.out.println(process.getId() + " - " + process.getName() + "(" + process.getVersion() + ")");
		}
		/*
		 * collection = this.service.listProcessByName("proc"); for (ProcessDefinition process : collection) {
		 * System.out.println(process.getId() + " - " + process.getName() + "(" + process.getVersion() + ")");
		 * }
		 */
	}

	// @Test
	public void testProcessDefinition() throws Exception {
		ProcessDefinition processDefinition = this.service.getProcessDefinitionByKey("test-process");
		if (processDefinition != null) {
			System.out.println(processDefinition.getId());
			System.out.println(processDefinition.getKey());
			System.out.println(processDefinition.getName());
			System.out.println(processDefinition.getVersion());
		} else {
			System.out.println("Processo nao encontrado");
		}
	}

	// @Test
	public void testStartProcess() throws Exception {
		ProcessDefinition processDefinition = this.service.getProcessDefinitionByKey("test-process");
		if (processDefinition != null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("userId", "lourival.junior");
			String id = this.service.startProccessById(processDefinition.getId(), "Test Lourival Sabino 4", parameters);
			System.out.println(id);
		} else {
			System.out.println("Processo nao encontrado");
		}
	}

	// @Test
	public void testInfoProcess() throws Exception {
		ProcessInstance pi = this.service.getProccessInstanceByKey("Test Lourival Sabino 3");
		this.print(pi, "");
	}

	// @Test
	public void testListTasks() throws Exception {
		Task[] tasks = this.service.listTasksByProcessInstanceKey("Test Lourival Sabino 2");
		System.out.println("====== TASKS =====");
		for (Task task : tasks) {
			System.out.println(task.getDescription());
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getStatus());
			System.out.println(task.getOwner());
			System.out.println(task.getUser());
			Form form = this.service.getForm(task.getId());
			if (form != null) {
				System.out.println(form.getId());
				for (Entry<String, FormField> entry : form.getFields().entrySet()) {
					System.out.println("\t" + entry.getKey() + " => " + entry.getValue().getId() + " => " + entry.getValue().getType());
				}
			}
		}
	}

	// @Test
	public void testImageProcess() throws Exception {
		String[] keys = new String[] { "Test Lourival Sabino 3", "Test Lourival Sabino" };
		for (String key : keys) {
			ProcessInstance pi = this.service.getProccessInstanceByKey(key);
			if (pi != null) {
				FileOutputStream fileOutputStream = new FileOutputStream("/tmp/" + pi.getKey() + ".png");
				byte[] image = this.service.getProcessInstanceImageById(pi.getId());
				fileOutputStream.write(image);
				fileOutputStream.close();
			}
		}
	}

	// @Test
	public void testSetUserTask() throws Exception {
		Task[] tasks = this.service.listTasksByProcessInstanceKey("Test Lourival Sabino 2");
		for (Task task : tasks) {
			this.service.assignTask(task.getId(), "kermit");
		}
	}

	// @Test
	public void testUserTask() throws Exception {
		Task[] tasks = this.service.listTasksByProcessInstanceId("310");
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
	public void testSetUserTaskForm() throws Exception {
		Task[] tasks = this.service.listTasksByProcessInstanceKey("Test Lourival Sabino 3");
		Task task = tasks[0];
		Form form = this.service.getForm(task.getId());
		form.getField("telefone").setValue("1");
		form.getField("nome").setValue("1");
		form.getField("endereco").setValue("123456");
		System.out.println("Definindo o Form");
		this.service.submitForm(task.getId(), form);
	}

	// @Test
	public void testGetUserTaskForm() throws Exception {
		Task[] tasks = this.service.listTasksByProcessInstanceKey("Test Lourival Sabino 2");
		Task task = tasks[0];
		Form form = this.service.getForm(task.getId());
		System.out.println(form.getField("telefone").getValue());
		System.out.println(form.getField("nome").getValue());
		System.out.println(form.getField("endereco").getValue());
	}

	private void print(final ProcessInstance pi, final String tabs) throws Exception {
		if (pi != null) {
			System.out.println(tabs + "\t" + pi.getId());
			System.out.println(tabs + "\t" + pi.getKey());
			System.out.println(tabs + "\t" + pi.isFinished());
			System.out.println(tabs + "\t" + pi.isSuspended());
			if (pi.getProcessDefinition() != null) {
				System.out.println(tabs + "\tDefinition...");
				System.out.println(tabs + "\t\t" + pi.getProcessDefinition().getId());
				System.out.println(tabs + "\t\t" + pi.getProcessDefinition().getName());
				System.out.println(tabs + "\t\t" + pi.getProcessDefinition().getVersion());
			}
			if ((tabs.length() == 0) && (pi.getParentProcessInstance() != null)) {
				System.out.println(tabs + "\tParent...");
				this.print(pi.getParentProcessInstance(), tabs + "\t");
			}
			System.out.println(tabs + "\tHistory");
			for (Activity a : pi.getHistory()) {
				System.out.println(tabs + "\t\tActivity...");
				System.out.println(tabs + "\t\t\t" + a.getId());
				System.out.println(tabs + "\t\t\t" + a.getName());
				System.out.println(tabs + "\t\t\t" + a.getType());
				System.out.println(tabs + "\t\t\t" + a.getStart());
				System.out.println(tabs + "\t\t\t" + a.getEnd());
				System.out.println(tabs + "\t\t\t" + a.getUser());

				if (a.getUser() != null) {
					System.out.println(tabs + "\t\tUser...");
					System.out.println(tabs + "\t\t\t\t" + a.getUser().getEmail());
					System.out.println(tabs + "\t\t\t\t" + a.getUser().getId());
					System.out.println(tabs + "\t\t\t\t" + a.getUser().getName());
				}
			}
			System.out.println(tabs + "\tCurrent");
			for (Activity a : pi.getCurrent()) {
				System.out.println(tabs + "\t\tActivity...");
				System.out.println(tabs + "\t\t\t" + a.getId());
				System.out.println(tabs + "\t\t\t" + a.getName());
				System.out.println(tabs + "\t\t\t" + a.getType());
				System.out.println(tabs + "\t\t\t" + a.getStart());
				System.out.println(tabs + "\t\t\t" + a.getEnd());
				System.out.println(tabs + "\t\t\t" + a.getUser());

				if (a.getUser() != null) {
					System.out.println(tabs + "\t\t\tUser...");
					System.out.println(tabs + "\t\t\t\t" + a.getUser().getEmail());
					System.out.println(tabs + "\t\t\t\t" + a.getUser().getId());
					System.out.println(tabs + "\t\t\t\t" + a.getUser().getName());
				}
			}
			if (ConditionUtils.isNotEmpty(pi.getSubProcess())) {
				System.out.println(tabs + "\tSubProcess...");
				for (ProcessInstance sub : pi.getSubProcess()) {
					this.print(sub, tabs + "\t");
				}
			}
		}
	}

}
