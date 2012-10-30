package br.net.woodstock.epm.process.api;

import java.io.Serializable;
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

	TaskInstance getTaskInstanceById(String id);

	byte[] getProcessInstanceImageById(String id);

	byte[] getProcessInstanceImageByKey(String key);

	void setVariableByProcessInstanceId(String processInstanceId, String name, String value);

	void setVariableByProcessInstanceKey(String processInstanceKey, String name, String value);

	// Definition
	ProcessDefinition getProcessDefinitionById(String id);

	ProcessDefinition getProcessDefinitionByKey(String key);

	// Query
	String[] listDeploymentByName(String name);

	ProcessDefinition[] listProcessByName(String name);

	ProcessDefinition[] listProcessByStartableUser(String user);

	TaskInstance[] listTasksByUser(String user);

	TaskInstance[] listTasksByCandidateUser(String user);

	TaskInstance[] listTasksByCandidateGroup(String group);

	TaskInstance[] listTasksByProcessInstanceId(String id);

	TaskInstance[] listTasksByProcessInstanceKey(String key);

}