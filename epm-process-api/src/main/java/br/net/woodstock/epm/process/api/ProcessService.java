package br.net.woodstock.epm.process.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface ProcessService extends Serializable {

	// Deploy
	String deploy(final String processName, final String deploymentName, final byte[] data, boolean zip);

	// Task
	void assignTask(final String taskId, final String userId);

	void completeTask(final String taskId);

	void delegateTask(final String taskId, final String userId);
	
	// Execution
	void signalExecution(final String executionId);

	// Form
	Form getForm(final String taskId);

	void submitForm(final String taskId, final Form form);

	// Instance
	String startProccess(final String processId, final String key, final Map<String, Object> parameters);

	ProcessInstance getProccessInstanceById(final String id);

	ProcessInstance getProccessInstanceByKey(final String key);

	byte[] getProcessInstanceImageById(final String id);

	byte[] getProcessInstanceImageByKey(final String key);

	// Process
	ProcessDefinition getProcessById(final String id);
	
	ProcessDefinition getProcessByKey(final String key);

	// Query
	Collection<String> listDeploymentByName(final String name);

	Collection<ProcessDefinition> listProcessByName(final String name);
	
	Collection<ProcessDefinition> listProcessByStartableUser(final String user);

	Collection<Task> listTasksByUser(final String user);

	Collection<Task> listTasksByCandidateUser(final String user);

	Collection<Task> listTasksByCandidateGroup(final String group);

	Collection<Task> listTasksByProcessInstanceId(final String id);

	Collection<Task> listTasksByProcessInstanceKey(final String key);

}