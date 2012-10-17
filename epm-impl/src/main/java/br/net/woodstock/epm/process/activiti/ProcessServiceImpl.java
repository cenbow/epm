package br.net.woodstock.epm.process.activiti;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;

import br.net.woodstock.epm.process.api.DeploymentType;
import br.net.woodstock.epm.process.api.Form;
import br.net.woodstock.epm.process.api.ProcessDefinition;
import br.net.woodstock.epm.process.api.ProcessInstance;
import br.net.woodstock.epm.process.api.ProcessService;
import br.net.woodstock.epm.process.api.Task;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;
import br.net.woodstock.rockframework.utils.ConditionUtils;
import br.net.woodstock.rockframework.utils.IOUtils;

public class ProcessServiceImpl implements ProcessService, Service {

	private static final long	serialVersionUID	= -5930737454047853423L;

	private static final String	ALL_PATTERN			= "%";

	private static final String	IMAGE_EXTENSION		= "png";

	private static final String	XML_SUFFIX			= ".bpmn20.xml";

	private ProcessEngine		engine;

	public ProcessServiceImpl(final ProcessEngine engine) {
		super();
		this.engine = engine;
	}

	// Deploy
	@Override
	public String deploy(final String processName, final byte[] data, final DeploymentType type) {
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			DeploymentBuilder builder = this.engine.getRepositoryService().createDeployment();
			builder.name(processName);

			if (type.equals(DeploymentType.ZIP)) {
				ZipInputStream zipInputStream = new ZipInputStream(inputStream);
				builder.addZipInputStream(zipInputStream);
			} else if (type.equals(DeploymentType.XML)) {
				builder.addInputStream(processName + ProcessServiceImpl.XML_SUFFIX, inputStream);
			}

			Deployment deployment = builder.deploy();
			return deployment.getId();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Task
	@Override
	public void assignTask(final String taskId, final String userId) {
		try {
			this.engine.getTaskService().claim(taskId, userId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void completeTask(final String taskId) {
		try {
			this.engine.getTaskService().complete(taskId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void delegateTask(final String taskId, final String userId) {
		try {
			this.engine.getTaskService().delegateTask(taskId, userId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Execution
	@Override
	public void signalExecution(final String executionId) {
		try {
			this.engine.getRuntimeService().signal(executionId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Form
	@Override
	public Form getForm(final String taskId) {
		try {
			TaskFormData formData = this.engine.getFormService().getTaskFormData(taskId);
			Form form = ConverterHelper.toForm(formData);
			return form;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void submitForm(final String taskId, final Form form) {
		try {
			this.engine.getFormService().submitTaskFormData(taskId, ConverterHelper.toMap(form));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Instance
	@Override
	public String startProccessById(final String processId, final String instanceKey, final Map<String, Object> parameters) {
		try {
			org.activiti.engine.runtime.ProcessInstance instance = this.engine.getRuntimeService().startProcessInstanceById(processId, instanceKey, parameters);
			return instance.getId();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ProcessInstance getProccessInstanceById(final String id) {
		try {
			ProcessInstance instance = ProcessServiceHelper.getProcessInstance(this.engine, id, null);
			return instance;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ProcessInstance getProccessInstanceByKey(final String key) {
		try {
			ProcessInstance instance = ProcessServiceHelper.getProcessInstance(this.engine, null, key);
			return instance;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] getProcessInstanceImageById(final String id) {
		try {
			ProcessInstanceQuery processInstanceQuery = this.engine.getRuntimeService().createProcessInstanceQuery();
			processInstanceQuery.processInstanceId(id);
			org.activiti.engine.runtime.ProcessInstance instance = processInstanceQuery.singleResult();
			if (instance != null) {
				RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) this.engine.getRepositoryService();
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryServiceImpl.getDeployedProcessDefinition(instance.getProcessDefinitionId());

				List<String> activeActivities = this.engine.getRuntimeService().getActiveActivityIds(id);

				InputStream inputStream = ProcessDiagramGenerator.generateDiagram(processDefinition, ProcessServiceImpl.IMAGE_EXTENSION, activeActivities);
				byte[] image = IOUtils.toByteArray(inputStream);
				return image;
			}

			HistoricProcessInstanceQuery historicProcessInstanceQuery = this.engine.getHistoryService().createHistoricProcessInstanceQuery();
			historicProcessInstanceQuery.processInstanceId(id);
			HistoricProcessInstance historicInstance = historicProcessInstanceQuery.singleResult();
			if (historicInstance != null) {
				RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) this.engine.getRepositoryService();
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryServiceImpl.getDeployedProcessDefinition(historicInstance.getProcessDefinitionId());

				List<String> activeActivities = Collections.emptyList();

				InputStream inputStream = ProcessDiagramGenerator.generateDiagram(processDefinition, ProcessServiceImpl.IMAGE_EXTENSION, activeActivities);
				byte[] image = IOUtils.toByteArray(inputStream);
				return image;
			}

			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] getProcessInstanceImageByKey(final String key) {
		try {
			ProcessDefinitionEntity entity = new ProcessDefinitionEntity();
			entity.setKey(key);

			InputStream inputStream = ProcessDiagramGenerator.generatePngDiagram(entity);
			byte[] image = IOUtils.toByteArray(inputStream);
			return image;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void setVariableByProcessInstanceId(final String processInstanceId, final String name, final String value) {
		try {
			ExecutionQuery executionQuery = this.engine.getRuntimeService().createExecutionQuery();
			executionQuery.processInstanceId(processInstanceId);
			Execution execution = executionQuery.singleResult();
			if (execution != null) {
				this.engine.getRuntimeService().setVariable(execution.getId(), name, value);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void setVariableByProcessInstanceKey(final String processInstanceKey, final String name, final String value) {
		try {
			ExecutionQuery executionQuery = this.engine.getRuntimeService().createExecutionQuery();
			executionQuery.processInstanceBusinessKey(processInstanceKey);
			Execution execution = executionQuery.singleResult();
			if (execution != null) {
				this.engine.getRuntimeService().setVariable(execution.getId(), name, value);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Process
	@Override
	public ProcessDefinition getProcessDefinitionById(final String id) {
		try {
			ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery();
			query.processDefinitionId(id);
			org.activiti.engine.repository.ProcessDefinition pd = query.singleResult();
			if (pd != null) {
				return ConverterHelper.toProcessDefinition(pd);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ProcessDefinition getProcessDefinitionByKey(final String key) {
		try {
			ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery();
			query.processDefinitionKey(key);
			query.latestVersion();
			org.activiti.engine.repository.ProcessDefinition pd = query.singleResult();
			if (pd != null) {
				return ConverterHelper.toProcessDefinition(pd);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Query
	@Override
	public String[] listDeploymentByName(final String name) {
		try {

			DeploymentQuery query = this.engine.getRepositoryService().createDeploymentQuery();
			if (ConditionUtils.isNotEmpty(name)) {
				query.deploymentNameLike(name);
			}
			List<Deployment> list = query.list();
			String[] array = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				array[i] = list.get(i).getId();
			}
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ProcessDefinition[] listProcessByName(final String name) {
		try {
			ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery();
			if (ConditionUtils.isNotEmpty(name)) {
				String s = ProcessServiceImpl.ALL_PATTERN + name + ProcessServiceImpl.ALL_PATTERN;
				query.processDefinitionNameLike(s);
			}
			List<org.activiti.engine.repository.ProcessDefinition> list = query.list();
			ProcessDefinition[] array = new ProcessDefinition[list.size()];
			for (int i = 0; i < list.size(); i++) {
				ProcessDefinition p = ConverterHelper.toProcessDefinition(list.get(i));
				array[i] = p;
			}
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ProcessDefinition[] listProcessByStartableUser(final String user) {
		try {
			ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery();
			query.startableByUser(user);
			List<org.activiti.engine.repository.ProcessDefinition> list = query.list();
			ProcessDefinition[] array = new ProcessDefinition[list.size()];
			for (int i = 0; i < list.size(); i++) {
				ProcessDefinition p = ConverterHelper.toProcessDefinition(list.get(i));
				array[i] = p;
			}
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Task[] listTasksByUser(final String user) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.taskAssignee(user);
			List<org.activiti.engine.task.Task> list = query.list();
			Task[] array = new Task[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Task t = ConverterHelper.toTask(list.get(i));
				ProcessServiceHelper.completeTask(this.engine, t);
				array[i] = t;
			}
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Task[] listTasksByCandidateUser(final String user) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.taskCandidateUser(user);
			List<org.activiti.engine.task.Task> list = query.list();
			Task[] array = new Task[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Task t = ConverterHelper.toTask(list.get(i));
				ProcessServiceHelper.completeTask(this.engine, t);
				array[i] = t;
			}
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Task[] listTasksByCandidateGroup(final String group) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.taskCandidateGroup(group);
			List<org.activiti.engine.task.Task> list = query.list();
			Task[] array = new Task[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Task t = ConverterHelper.toTask(list.get(i));
				ProcessServiceHelper.completeTask(this.engine, t);
				array[i] = t;
			}
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Task[] listTasksByProcessInstanceId(final String id) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.processInstanceId(id);
			List<org.activiti.engine.task.Task> list = query.list();
			Task[] array = new Task[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Task t = ConverterHelper.toTask(list.get(i));
				ProcessServiceHelper.completeTask(this.engine, t);
				array[i] = t;
			}
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Task[] listTasksByProcessInstanceKey(final String key) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.processInstanceBusinessKey(key);
			List<org.activiti.engine.task.Task> list = query.list();
			Task[] array = new Task[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Task t = ConverterHelper.toTask(list.get(i));
				ProcessServiceHelper.completeTask(this.engine, t);
				array[i] = t;
			}
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
