package br.net.woodstock.epm.impl.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class ActivitiTest {

	@Autowired(required = true)
	private ProcessEngine	engine;

	public ActivitiTest() {
		super();
	}

	// @Test
	public void testListDeployment() throws Exception {
		DeploymentQuery query = this.engine.getRepositoryService().createDeploymentQuery();
		List<Deployment> list = query.list();
		for (Deployment deployment : list) {
			System.out.println(deployment.getId() + " - " + deployment.getName());
		}
	}

	@Test
	public void testDeploy() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("test.bar");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);

		DeploymentBuilder builder = this.engine.getRepositoryService().createDeployment();
		builder.name("test2");
		builder.addZipInputStream(zipInputStream);
		builder.deploy();
	}

	// @Test
	public void testListProccess() throws Exception {
		ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery();
		List<ProcessDefinition> list = query.list();
		for (ProcessDefinition process : list) {
			System.out.println("DI     : " + process.getDeploymentId());
			System.out.println("ID     : " + process.getId());
			System.out.println("Key    : " + process.getKey());
			System.out.println("Name   : " + process.getName());
			System.out.println("Version: " + process.getVersion());
		}
	}

	// @Test
	public void testStart() throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String id = "Test2:1:1616";
		String key = "test2-1";
		parameters.put("key1", "value1");
		parameters.put("key2", "value2");
		parameters.put("key3", "value3");
		parameters.put("key4", new int[] { 1, 2, 3, 4, 5 });
		ProcessInstance instance = this.engine.getRuntimeService().startProcessInstanceById(id, key, parameters);
		System.out.println("ID : " + instance.getId());
		System.out.println("PDI: " + instance.getProcessDefinitionId());
		System.out.println("PII: " + instance.getProcessInstanceId());
		System.out.println("BK : " + instance.getBusinessKey());
	}

	// @Test
	public void testView() throws Exception {
		String id = "310";
		HistoricActivityInstanceQuery query = this.engine.getHistoryService().createHistoricActivityInstanceQuery();
		query.processInstanceId(id);
		List<HistoricActivityInstance> list = query.list();
		for (HistoricActivityInstance instance : list) {
			System.out.println("Assignee: " + instance.getAssignee());
			System.out.println("Assignee: " + instance.getExecutionId());
			System.out.println("Act ID  : " + instance.getActivityId());
			System.out.println("Act Name: " + instance.getActivityName());
			System.out.println("Act Type: " + instance.getActivityType());
			System.out.println("Start   : " + instance.getStartTime());
			System.out.println("End     : " + instance.getEndTime());

			// ProcessDefinitionEntity entity = new ProcessDefinitionEntity();
			// entity.setId(instance.getProcessInstanceId());

			// InputStream png = ProcessDiagramGenerator.generatePngDiagram(entity);
			// FileOutputStream outputStream = new FileOutputStream("/tmp/310.png");
			// IOUtils.copy(png, outputStream);
			// outputStream.close();
		}
	}

	// @Test
	public void testListTask() throws Exception {
		String id = "1710";
		TaskQuery query = this.engine.getTaskService().createTaskQuery();
		query.processInstanceId(id);
		List<Task> list = query.list();
		for (Task task : list) {
			System.out.println("Assignee: " + task.getAssignee());
			System.out.println("Name    : " + task.getId());
			System.out.println("Name    : " + task.getName());
			System.out.println("Desc    : " + task.getDescription());
			System.out.println("Owner   : " + task.getOwner());
			System.out.println("State   : " + task.getDelegationState());
		}
	}

	// @Test
	public void testAssignTask() {
		String taskId = "1717";
		String userId = "kermit";
		this.engine.getTaskService().delegateTask(taskId, userId);
	}

	// @Test
	public void testCompleteTask() throws Exception {
		String id = "1717";
		this.engine.getTaskService().complete(id);
	}

	// @Test
	public void testFormTask() throws Exception {
		String id = "1717";
		Object form = this.engine.getFormService().getRenderedTaskForm(id);
		System.out.println(form);
		System.out.println(form.getClass());

		TaskFormData data = this.engine.getFormService().getTaskFormData(id);
		for (FormProperty property : data.getFormProperties()) {
			System.out.println(property.getName() + " => " + property.getValue());
		}
	}

	// @Test
	public void testExecuteTask() {
		String taskId = "1";
		String userId = "1";
		this.engine.getTaskService().delegateTask(taskId, userId);
		this.engine.getTaskService().claim(taskId, userId);
		this.engine.getTaskService().complete(taskId);
	}

	public void testSearch() throws Exception {
		String id = "310";
		HistoricActivityInstanceQuery query = this.engine.getHistoryService().createHistoricActivityInstanceQuery();
		query.processInstanceId(id);
		List<HistoricActivityInstance> list = query.list();
		for (HistoricActivityInstance instance : list) {
			System.out.println("Assignee: " + instance.getAssignee());
			System.out.println("Assignee: " + instance.getExecutionId());
			System.out.println("Act ID  : " + instance.getActivityId());
			System.out.println("Act Name: " + instance.getActivityName());
			System.out.println("Act Type: " + instance.getActivityType());
			System.out.println("Start   : " + instance.getStartTime());
			System.out.println("End     : " + instance.getEndTime());

			// ProcessDefinitionEntity entity = new ProcessDefinitionEntity();
			// entity.setId(instance.getProcessInstanceId());

			// InputStream png = ProcessDiagramGenerator.generatePngDiagram(entity);
			// FileOutputStream outputStream = new FileOutputStream("/tmp/310.png");
			// IOUtils.copy(png, outputStream);
			// outputStream.close();
		}
	}
}
