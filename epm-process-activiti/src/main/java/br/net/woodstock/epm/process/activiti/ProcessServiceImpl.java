package br.net.woodstock.epm.process.activiti;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;

import br.net.woodstock.epm.process.api.Form;
import br.net.woodstock.epm.process.api.ProcessDefinition;
import br.net.woodstock.epm.process.api.ProcessInstance;
import br.net.woodstock.epm.process.api.ProcessService;
import br.net.woodstock.epm.process.api.Task;
import br.net.woodstock.rockframework.domain.jee.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;
import br.net.woodstock.rockframework.utils.ConditionUtils;
import br.net.woodstock.rockframework.utils.IOUtils;

@Service
public class ProcessServiceImpl implements ProcessService {

	private static final long	serialVersionUID	= -5930737454047853423L;

	@Autowired(required = true)
	private ProcessEngine		engine;

	// Deploy
	@Override
	public String deploy(final String processName, final String deploymentName, final byte[] data, final boolean zip) {
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			DeploymentBuilder builder = this.engine.getRepositoryService().createDeployment();
			builder.name(processName);

			if (zip) {
				ZipInputStream zipInputStream = new ZipInputStream(inputStream);
				builder.addZipInputStream(zipInputStream);
			} else {
				builder.addInputStream(deploymentName, inputStream);
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
	public String startProccess(final String processId, final String key, final Map<String, Object> parameters) {
		try {
			org.activiti.engine.runtime.ProcessInstance instance = this.engine.getRuntimeService().startProcessInstanceById(processId, key, parameters);
			return instance.getId();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ProcessInstance getProccessInstanceById(final String id) {
		try {
			ProcessInstanceQuery query = this.engine.getRuntimeService().createProcessInstanceQuery();
			query.processInstanceId(id);
			org.activiti.engine.runtime.ProcessInstance instance = query.singleResult();
			if (instance != null) {
				ProcessInstance pi = ConverterHelper.toProcessInstance(instance);

				ConverterHelper.completeProcessInstance(this.engine, pi, instance.getProcessDefinitionId());

				return pi;
			}

			HistoricProcessInstanceQuery hQuery = this.engine.getHistoryService().createHistoricProcessInstanceQuery();
			query.processInstanceId(id);
			List<HistoricProcessInstance> list = hQuery.list();
			if (list.size() > 0) {
				HistoricProcessInstance hpi = null;
				for (HistoricProcessInstance tmp : list) {
					if (tmp.getId().equals(id)) {
						hpi = tmp;
						break;
					}
				}

				if (hpi != null) {
					ProcessInstance pi = ConverterHelper.toProcessInstance(hpi);

					ConverterHelper.completeProcessInstance(this.engine, pi, hpi.getProcessDefinitionId());

					return pi;
				}
			}

			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ProcessInstance getProccessInstanceByKey(final String key) {
		try {
			ProcessInstanceQuery query = this.engine.getRuntimeService().createProcessInstanceQuery();
			query.processInstanceBusinessKey(key);
			org.activiti.engine.runtime.ProcessInstance instance = query.singleResult();
			if (instance != null) {
				ProcessInstance pi = new ProcessInstance();
				pi.setFinished(instance.isEnded());
				pi.setId(instance.getId());
				pi.setKey(instance.getBusinessKey());
				pi.setSuspended(instance.isSuspended());

				ConverterHelper.completeProcessInstance(this.engine, pi, instance.getProcessDefinitionId());

				return pi;
			}

			HistoricProcessInstanceQuery hQuery = this.engine.getHistoryService().createHistoricProcessInstanceQuery();
			query.processInstanceBusinessKey(key);
			List<HistoricProcessInstance> list = hQuery.list();
			if (list.size() > 0) {
				HistoricProcessInstance hpi = null;
				for (HistoricProcessInstance tmp : list) {
					if (tmp.getBusinessKey().equals(key)) {
						hpi = tmp;
						break;
					}
				}

				if (hpi != null) {
					ProcessInstance pi = new ProcessInstance();
					pi.setEnd(hpi.getEndTime());
					pi.setFinished(true);
					pi.setId(hpi.getId());
					pi.setKey(hpi.getBusinessKey());
					pi.setStart(hpi.getStartTime());
					pi.setSuspended(false);

					ConverterHelper.completeProcessInstance(this.engine, pi, hpi.getProcessDefinitionId());

					return pi;
				}
			}

			return null;
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

				InputStream inputStream = ProcessDiagramGenerator.generateDiagram(processDefinition, "png", activeActivities);
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

				InputStream inputStream = ProcessDiagramGenerator.generateDiagram(processDefinition, "png", activeActivities);
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

	// Process
	@Override
	public ProcessDefinition getProcessById(final String id) {
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
	public ProcessDefinition getProcessByKey(final String key) {
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
	public Collection<String> listDeploymentByName(final String name) {
		try {

			DeploymentQuery query = this.engine.getRepositoryService().createDeploymentQuery();
			if (ConditionUtils.isNotEmpty(name)) {
				query.deploymentNameLike(name);
			}
			List<Deployment> list = query.list();
			List<String> result = new ArrayList<String>();
			for (Deployment deployment : list) {
				result.add(deployment.getId());
			}
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<ProcessDefinition> listProcessByName(final String name) {
		try {
			ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery();
			if (ConditionUtils.isNotEmpty(name)) {
				String s = "%" + name + "%";
				query.processDefinitionNameLike(s);
			}
			List<org.activiti.engine.repository.ProcessDefinition> list = query.list();
			List<ProcessDefinition> result = new ArrayList<ProcessDefinition>();
			for (org.activiti.engine.repository.ProcessDefinition definition : list) {
				result.add(ConverterHelper.toProcessDefinition(definition));
			}
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<ProcessDefinition> listProcessByStartableUser(final String user) {
		try {
			ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery();
			query.startableByUser(user);
			List<org.activiti.engine.repository.ProcessDefinition> list = query.list();
			List<ProcessDefinition> result = new ArrayList<ProcessDefinition>();
			for (org.activiti.engine.repository.ProcessDefinition definition : list) {
				result.add(ConverterHelper.toProcessDefinition(definition));
			}
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<Task> listTasksByUser(final String user) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.taskAssignee(user);
			List<org.activiti.engine.task.Task> list = query.list();
			List<Task> result = new ArrayList<Task>();
			for (org.activiti.engine.task.Task task : list) {
				result.add(ConverterHelper.toTask(this.engine, task));
			}
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<Task> listTasksByCandidateUser(final String user) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.taskCandidateUser(user);
			List<org.activiti.engine.task.Task> list = query.list();
			List<Task> result = new ArrayList<Task>();
			for (org.activiti.engine.task.Task task : list) {
				result.add(ConverterHelper.toTask(this.engine, task));
			}
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<Task> listTasksByCandidateGroup(final String group) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.taskCandidateGroup(group);
			List<org.activiti.engine.task.Task> list = query.list();
			List<Task> result = new ArrayList<Task>();
			for (org.activiti.engine.task.Task task : list) {
				result.add(ConverterHelper.toTask(this.engine, task));
			}
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<Task> listTasksByProcessInstanceId(final String id) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.processInstanceId(id);
			List<org.activiti.engine.task.Task> list = query.list();
			List<Task> result = new ArrayList<Task>();
			for (org.activiti.engine.task.Task task : list) {
				result.add(ConverterHelper.toTask(this.engine, task));
			}
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<Task> listTasksByProcessInstanceKey(final String key) {
		try {
			TaskQuery query = this.engine.getTaskService().createTaskQuery();
			query.processInstanceId(key);
			List<org.activiti.engine.task.Task> list = query.list();
			List<Task> result = new ArrayList<Task>();
			for (org.activiti.engine.task.Task task : list) {
				result.add(ConverterHelper.toTask(this.engine, task));
			}
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
