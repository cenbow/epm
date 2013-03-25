package br.net.woodstock.epm.process.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.epm.orm.BusinessProcess;
import br.net.woodstock.epm.orm.BusinessProcessBinType;
import br.net.woodstock.epm.orm.BusinessGroup;
import br.net.woodstock.epm.process.api.BusinessProcessService;
import br.net.woodstock.epm.repository.util.ORMRepositoryHelper;
import br.net.woodstock.rockframework.core.utils.Collections;
import br.net.woodstock.rockframework.core.utils.IO;
import br.net.woodstock.rockframework.domain.ServiceException;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMFilter;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMRepository;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class BusinessProcessServiceImpl implements BusinessProcessService {

	private static final long	serialVersionUID			= -5930737454047853423L;

	private static final String	JPQL_GET_PROCESS_BY_NAME	= "SELECT b FROM BusinessProcess AS b WHERE b.name = :name";

	private static final String	ALL_PATTERN					= "%";

	private static final String	IMAGE_EXTENSION				= "png";

	private static final String	XML_SUFFIX					= ".bpmn20.xml";

	@Autowired(required = true)
	private ProcessEngine		engine;

	@Autowired(required = true)
	private ORMRepository		repository;

	public BusinessProcessServiceImpl() {
		super();
	}

	@Override
	public BusinessProcess getBusinessProcessById(final Integer id) {
		try {
			return this.repository.get(BusinessProcess.class, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public BusinessProcess getBusinessProcessByName(final String name) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", name);
			ORMFilter filter = ORMRepositoryHelper.toORMFilter(BusinessProcessServiceImpl.JPQL_GET_PROCESS_BY_NAME, parameters);
			BusinessProcess businessProcess = this.repository.getSingle(filter);
			return businessProcess;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(final BusinessProcess businessProcess) {
		try {
			InputStream inputStream = new ByteArrayInputStream(businessProcess.getBin());
			DeploymentBuilder builder = this.engine.getRepositoryService().createDeployment();
			builder.name(businessProcess.getName());

			if (businessProcess.getType().equals(BusinessProcessBinType.ZIP)) {
				ZipInputStream zipInputStream = new ZipInputStream(inputStream);
				builder.addZipInputStream(zipInputStream);
			} else {
				builder.addInputStream(businessProcess.getName() + BusinessProcessServiceImpl.XML_SUFFIX, inputStream);
			}

			Deployment deployment = builder.deploy();

			ProcessDefinitionQuery query = this.engine.getRepositoryService().createProcessDefinitionQuery().deploymentId(deployment.getId());
			ProcessDefinition processDefinition = query.singleResult();
			ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.engine.getRepositoryService()).getDeployedProcessDefinition(processDefinition.getId());
			Map<String, TaskDefinition> taskMap = processDefinitionEntity.getTaskDefinitions();

			businessProcess.setProcessDefinition(processDefinition.getId());
			businessProcess.setActive(Boolean.TRUE);

			this.repository.save(businessProcess);

			Set<String> swimLanes = new HashSet<String>();
			for (Entry<String, TaskDefinition> entry : taskMap.entrySet()) {
				TaskDefinition taskDefinition = entry.getValue();
				for (Expression e : taskDefinition.getCandidateGroupIdExpressions()) {
					String swimLane = e.getExpressionText();
					swimLanes.add(swimLane);
				}
			}

			if (swimLanes.size() > 0) {
				for (String swimLane : swimLanes) {
					BusinessGroup sl = new BusinessGroup();
					sl.setBusinessProcess(businessProcess);
					sl.setName(swimLane);

					this.repository.save(sl);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public byte[] getBusinessProcessImage(final Integer id) {
		try {
			BusinessProcess businessProcess = this.repository.get(BusinessProcess.class, id);
			if (businessProcess != null) {
				RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) this.engine.getRepositoryService();
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryServiceImpl.getDeployedProcessDefinition(businessProcess.getProcessDefinition());

				List<String> activeActivities = Collections.emptyList();

				InputStream inputStream = ProcessDiagramGenerator.generateDiagram(processDefinition, BusinessProcessServiceImpl.IMAGE_EXTENSION, activeActivities);
				byte[] image = IO.toByteArray(inputStream);
				return image;
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
