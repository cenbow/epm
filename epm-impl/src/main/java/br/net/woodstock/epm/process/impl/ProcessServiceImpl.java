package br.net.woodstock.epm.process.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.epm.orm.CandidateGroup;
import br.net.woodstock.epm.orm.DeploymentType;
import br.net.woodstock.epm.orm.Process;
import br.net.woodstock.epm.orm.SimpleProcess;
import br.net.woodstock.epm.orm.Task;
import br.net.woodstock.epm.process.api.ProcessService;
import br.net.woodstock.epm.repository.util.ORMRepositoryHelper;
import br.net.woodstock.rockframework.core.utils.Collections;
import br.net.woodstock.rockframework.core.utils.IO;
import br.net.woodstock.rockframework.domain.ServiceException;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMFilter;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMRepository;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ProcessServiceImpl implements ProcessService {

	private static final long			serialVersionUID			= -5930737454047853423L;

	private static final String			JPQL_GET_PROCESS_BY_NAME	= "SELECT b FROM BusinessProcess AS b WHERE b.name = :name";

	private static final String			JPQL_LIST_PROCESS_BY_NAME	= "SELECT b FROM BusinessProcess AS b WHERE b.name LIKE :name ORDER BY b.name";

	private static final String			JPQL_COUNT_PROCESS_BY_NAME	= "SELECT COUNT(*) FROM BusinessProcess AS b WHERE b.name LIKE :name";

	private static final String			ALL_PATTERN					= "%";

	private static final String			IMAGE_EXTENSION				= "png";

	private static final String			XML_SUFFIX					= ".bpmn20.xml";

	@Autowired(required = true)
	private transient ProcessEngine		engine;

	@Autowired(required = true)
	private ORMRepository				repository;

	@Autowired(required = true)
	private StartEventExecutionListener	startEventExecutionListener;

	@Autowired(required = true)
	private EndEventExecutionListener	endEventExecutionListener;

	@Autowired(required = true)
	private TakeEventExecutionListener	takeEventExecutionListener;

	public ProcessServiceImpl() {
		super();
	}

	// Crud
	@Override
	public Process getBusinessProcessById(final Integer id) {
		try {
			return this.repository.get(Process.class, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Process getBusinessProcessByName(final String name) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", name);
			ORMFilter filter = ORMRepositoryHelper.toORMFilter(ProcessServiceImpl.JPQL_GET_PROCESS_BY_NAME, parameters);
			Process businessProcess = this.repository.getSingle(filter);
			return businessProcess;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(final Process process) {
		try {
			InputStream inputStream = new ByteArrayInputStream(process.getBin());
			DeploymentBuilder builder = this.engine.getRepositoryService().createDeployment();
			builder.name(process.getName());

			if (process.getType().equals(DeploymentType.ZIP)) {
				ZipInputStream zipInputStream = new ZipInputStream(inputStream);
				builder.addZipInputStream(zipInputStream);
			} else {
				builder.addInputStream(process.getName() + ProcessServiceImpl.XML_SUFFIX, inputStream);
			}

			Deployment deployment = builder.deploy();

			ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery().deploymentId(deployment.getId());
			ProcessDefinition processDefinition = query.singleResult();

			ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.engine.getRepositoryService()).getDeployedProcessDefinition(processDefinition.getId());

			processDefinitionEntity.addExecutionListener(ExecutionListener.EVENTNAME_START, this.startEventExecutionListener);
			processDefinitionEntity.addExecutionListener(ExecutionListener.EVENTNAME_END, this.endEventExecutionListener);
			processDefinitionEntity.addExecutionListener(ExecutionListener.EVENTNAME_TAKE, this.takeEventExecutionListener);

			Map<String, TaskDefinition> taskMap = processDefinitionEntity.getTaskDefinitions();

			process.setProcessDefinition(processDefinition.getId());
			process.setActive(Boolean.TRUE);

			this.repository.save(process);

			Map<String, CandidateGroup> groups = new HashMap<String, CandidateGroup>();
			for (Entry<String, TaskDefinition> entry : taskMap.entrySet()) {
				TaskDefinition taskDefinition = entry.getValue();

				Task task = new Task();
				task.setProcess(process);
				task.setName(taskDefinition.getNameExpression().getExpressionText());
				this.repository.save(task);

				for (Expression e : taskDefinition.getCandidateGroupIdExpressions()) {
					String group = e.getExpressionText();
					if (!groups.containsKey(group)) {
						CandidateGroup cg = new CandidateGroup();
						cg.setTask(task);
						cg.setName(group);
						this.repository.save(cg);
						groups.put(group, cg);
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public byte[] getBusinessProcessImage(final Integer id) {
		try {
			Process businessProcess = this.repository.get(Process.class, id);
			if (businessProcess != null) {
				BpmnModel model = this.engine.getRepositoryService().getBpmnModel(businessProcess.getProcessDefinition());
				if (model != null) {
					List<String> activeActivities = Collections.emptyList();
					InputStream inputStream = ProcessDiagramGenerator.generateDiagram(model, ProcessServiceImpl.IMAGE_EXTENSION, activeActivities);
					byte[] image = IO.toByteArray(inputStream);
					return image;
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ORMResult listBusinessProcessByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", ORMRepositoryHelper.getLikeValue(name, false));

			ORMFilter filter = ORMRepositoryHelper.toORMFilter(ProcessServiceImpl.JPQL_LIST_PROCESS_BY_NAME, ProcessServiceImpl.JPQL_COUNT_PROCESS_BY_NAME, page, parameters);
			return this.repository.getCollection(filter);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// BPM
	@Override
	public Integer createSimpleProcess(final String number, final Integer businessProcessId) {
		try {
			Process process = this.repository.get(Process.class, businessProcessId);
			if (process == null) {
				throw new IllegalStateException("Business processs not found");
			}
			if (!process.getActive().booleanValue()) {
				throw new IllegalStateException("Business process is not active");
			}

			ProcessInstance processInstance = this.engine.getRuntimeService().startProcessInstanceById(process.getProcessDefinition());
			String processInstanceId = processInstance.getProcessInstanceId();

			SimpleProcess simpleProcess = new SimpleProcess();
			simpleProcess.setActive(Boolean.TRUE);
			simpleProcess.setNumber(number);

			this.repository.save(simpleProcess);

			br.net.woodstock.epm.orm.ProcessInstance pi = new br.net.woodstock.epm.orm.ProcessInstance();
			pi.setActive(Boolean.TRUE);
			pi.setProcess(process);
			pi.setProcessInstanceId(processInstanceId);
			pi.setSimpleProcess(simpleProcess);

			this.repository.save(simpleProcess);

			return simpleProcess.getId();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void assignTask(final String taskId, final String userId) {
		try {
			this.engine.getTaskService().claim(taskId, userId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void completeTask(final String taskId) {
		try {
			this.engine.getTaskService().complete(taskId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void delegateTask(final String taskId, final String userId) {
		try {
			this.engine.getTaskService().delegateTask(taskId, userId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
