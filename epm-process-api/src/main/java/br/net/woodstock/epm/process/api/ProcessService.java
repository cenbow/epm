package br.net.woodstock.epm.process.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface ProcessService extends Serializable {

	// Deploy
	String deploy(String processName, byte[] data, DeploymentType type);

	// Task
	void assignTask(String taskId, String userId);

	void completeTask(String taskId);

	void delegateTask(String taskId, String userId);

	// Execution
	void signalExecution(String executionId);

	// Form
	Form getForm(String taskId);

	void submitForm(String taskId, Form form);

	// Instance
	String startProccessById(String processId, String instanceKey, Map<String, Object> parameters);

	ProcessInstance getProccessInstanceById(String id);

	ProcessInstance getProccessInstanceByKey(String key);

	byte[] getProcessInstanceImageById(String id);

	byte[] getProcessInstanceImageByKey(String key);

	// Process
	ProcessDefinition getProcessDefinitionById(String id);

	ProcessDefinition getProcessDefinitionByKey(String key);

	// Query
	Collection<String> listDeploymentByName(String name);

	Collection<ProcessDefinition> listProcessByName(String name);

	Collection<ProcessDefinition> listProcessByStartableUser(String user);

	Collection<Task> listTasksByUser(String user);

	Collection<Task> listTasksByCandidateUser(String user);

	Collection<Task> listTasksByCandidateGroup(String group);

	Collection<Task> listTasksByProcessInstanceId(String id);

	Collection<Task> listTasksByProcessInstanceKey(String key);

}