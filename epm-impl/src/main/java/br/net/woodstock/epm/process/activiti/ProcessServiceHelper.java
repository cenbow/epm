package br.net.woodstock.epm.process.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;

import br.net.woodstock.epm.process.api.Activity;
import br.net.woodstock.epm.process.api.ProcessDefinition;
import br.net.woodstock.epm.process.api.ProcessInstance;
import br.net.woodstock.epm.process.api.TaskInstance;
import br.net.woodstock.rockframework.utils.CollectionUtils;
import br.net.woodstock.rockframework.utils.ConditionUtils;

abstract class ProcessServiceHelper {

	private ProcessServiceHelper() {
		//
	}

	public static ProcessInstance getProcessInstance(final ProcessEngine engine, final String id, final String key) {
		ProcessInstanceQuery query = engine.getRuntimeService().createProcessInstanceQuery();

		if (!ConditionUtils.isEmpty(id)) {
			query.processInstanceId(id);
		} else {
			query.processInstanceBusinessKey(key);
		}

		org.activiti.engine.runtime.ProcessInstance instance = query.singleResult();

		if (instance != null) {
			ProcessInstance pi = ConverterHelper.toProcessInstance(instance);

			ProcessServiceHelper.completeProcessInstance(engine, pi, instance.getProcessDefinitionId(), null, new HashMap<String, ProcessInstance>());

			return pi;
		}

		HistoricProcessInstanceQuery hQuery = engine.getHistoryService().createHistoricProcessInstanceQuery();

		if (!ConditionUtils.isEmpty(id)) {
			hQuery.processInstanceId(id);
		} else {
			hQuery.processInstanceBusinessKey(key);
		}

		List<HistoricProcessInstance> list = hQuery.list();
		if (list.size() > 0) {
			HistoricProcessInstance hpi = null;
			for (HistoricProcessInstance tmp : list) {
				String businessKey = tmp.getBusinessKey();
				if (ConditionUtils.isNotEmpty(businessKey)) {
					if (tmp.getBusinessKey().equals(key)) {
						hpi = tmp;
						break;
					}
				}
			}

			if (hpi != null) {
				ProcessInstance pi = ConverterHelper.toProcessInstance(hpi);

				ProcessServiceHelper.completeProcessInstance(engine, pi, hpi.getProcessDefinitionId(), hpi.getSuperProcessInstanceId(), new HashMap<String, ProcessInstance>());

				return pi;
			}
		}

		return null;
	}

	public static TaskInstance getTaskInstance(final ProcessEngine engine, final String id) {
		TaskQuery query = engine.getTaskService().createTaskQuery();
		query.taskId(id);
		org.activiti.engine.task.Task t = query.singleResult();
		if (t != null) {
			TaskInstance instance = ConverterHelper.toTask(t);
			instance.setProcessInstance(ProcessServiceHelper.getProcessInstance(engine, t.getProcessInstanceId(), null));

			if (ConditionUtils.isNotEmpty(t.getAssignee())) {
				UserQuery userQuery = engine.getIdentityService().createUserQuery();
				userQuery.userId(t.getAssignee());
				org.activiti.engine.identity.User user = userQuery.singleResult();
				instance.setUser(ConverterHelper.toUser(user));
			}
			if (ConditionUtils.isNotEmpty(t.getOwner())) {
				UserQuery userQuery = engine.getIdentityService().createUserQuery();
				userQuery.userId(t.getOwner());
				org.activiti.engine.identity.User user = userQuery.singleResult();
				instance.setOwner(ConverterHelper.toUser(user));
			}
			return instance;
		}
		return null;
	}

	private static void completeProcessInstance(final ProcessEngine engine, final ProcessInstance instance, final String processDefinitionId, final String parentProcessInstanceId, final Map<String, ProcessInstance> cache) {
		cache.put(instance.getId(), instance);
		ProcessServiceHelper.addParentProcessInstance(engine, instance, parentProcessInstanceId, cache);
		ProcessServiceHelper.addSubProcessInstance(engine, instance, cache);

		// Definition
		ProcessDefinitionQuery processDefinitionQuery = engine.getRepositoryService().createProcessDefinitionQuery();
		processDefinitionQuery.processDefinitionId(processDefinitionId);
		org.activiti.engine.repository.ProcessDefinition pd = processDefinitionQuery.singleResult();

		ProcessDefinition processDefinition = new ProcessDefinition();
		processDefinition.setId(pd.getId());
		processDefinition.setName(pd.getName());
		processDefinition.setVersion(Integer.toString(pd.getVersion()));

		instance.setProcessDefinition(processDefinition);

		HistoricActivityInstanceQuery activityInstanceQuery = engine.getHistoryService().createHistoricActivityInstanceQuery();
		activityInstanceQuery.processInstanceId(instance.getId());
		List<HistoricActivityInstance> activityList = activityInstanceQuery.list();

		List<Activity> histories = new ArrayList<Activity>();
		List<Activity> currents = new ArrayList<Activity>();

		if (ConditionUtils.isNotEmpty(activityList)) {
			for (HistoricActivityInstance activityInstance : activityList) {
				Activity a = ConverterHelper.toActivity(activityInstance);

				// if (ConditionUtils.isNotEmpty(activityInstance.getExecutionId())) {
				// ExecutionQuery executionQuery = engine.getRuntimeService().createExecutionQuery();
				// executionQuery.executionId(activityInstance.getExecutionId());
				// Execution execution = executionQuery.singleResult();
				// }

				if (ConditionUtils.isNotEmpty(activityInstance.getAssignee())) {
					UserQuery userQuery = engine.getIdentityService().createUserQuery();
					userQuery.userId(activityInstance.getAssignee());
					org.activiti.engine.identity.User user = userQuery.singleResult();
					a.setUser(ConverterHelper.toUser(user));
				}

				if (activityInstance.getEndTime() != null) {
					histories.add(a);
				} else {
					currents.add(a);
				}
			}
		}

		instance.setHistory(CollectionUtils.toArray(histories, Activity.class));
		instance.setCurrent(CollectionUtils.toArray(currents, Activity.class));
	}

	private static void addParentProcessInstance(final ProcessEngine engine, final ProcessInstance instance, final String parentProcessInstanceId, final Map<String, ProcessInstance> cache) {
		// Parent Process
		if ((ConditionUtils.isNotEmpty(parentProcessInstanceId)) && (cache.containsKey(parentProcessInstanceId))) {
			instance.setParentProcessInstance(cache.get(parentProcessInstanceId));
			return;
		}

		ProcessInstanceQuery processInstanceQuery = engine.getRuntimeService().createProcessInstanceQuery();
		processInstanceQuery.subProcessInstanceId(instance.getId());
		org.activiti.engine.runtime.ProcessInstance pi = processInstanceQuery.singleResult();

		if (pi != null) {
			if (!cache.containsKey(pi.getId())) {
				ProcessInstance parentProcessInstance = ConverterHelper.toProcessInstance(pi);
				instance.setParentProcessInstance(parentProcessInstance);
				cache.put(pi.getId(), parentProcessInstance);
				ProcessServiceHelper.completeProcessInstance(engine, parentProcessInstance, pi.getProcessDefinitionId(), parentProcessInstanceId, cache);
			} else {
				instance.setParentProcessInstance(cache.get(pi.getId()));
			}
		} else if (ConditionUtils.isNotEmpty(parentProcessInstanceId)) {
			HistoricProcessInstanceQuery historicProcessInstanceQuery = engine.getHistoryService().createHistoricProcessInstanceQuery();
			historicProcessInstanceQuery.processInstanceId(parentProcessInstanceId);
			HistoricProcessInstance hpi = historicProcessInstanceQuery.singleResult();

			if (hpi != null) {
				if (!cache.containsKey(hpi.getId())) {
					ProcessInstance parentProcessInstance = ConverterHelper.toProcessInstance(hpi);
					instance.setParentProcessInstance(parentProcessInstance);
					cache.put(hpi.getId(), parentProcessInstance);
					ProcessServiceHelper.completeProcessInstance(engine, parentProcessInstance, hpi.getProcessDefinitionId(), parentProcessInstanceId, cache);
				} else {
					instance.setParentProcessInstance(cache.get(hpi.getId()));
				}
			}
		}
	}

	private static void addSubProcessInstance(final ProcessEngine engine, final ProcessInstance instance, final Map<String, ProcessInstance> cache) {
		// Child Process
		List<ProcessInstance> list = new ArrayList<ProcessInstance>();
		if (!instance.isFinished()) {
			ProcessInstanceQuery processInstanceQuery = engine.getRuntimeService().createProcessInstanceQuery();
			processInstanceQuery.superProcessInstanceId(instance.getId());
			List<org.activiti.engine.runtime.ProcessInstance> processInstancelist = processInstanceQuery.list();
			if (ConditionUtils.isNotEmpty(processInstancelist)) {
				for (org.activiti.engine.runtime.ProcessInstance tmpPi : processInstancelist) {
					if (!cache.containsKey(tmpPi.getId())) {
						ProcessInstance subProcessInstance = ConverterHelper.toProcessInstance(tmpPi);
						list.add(subProcessInstance);
						cache.put(tmpPi.getId(), subProcessInstance);
						ProcessServiceHelper.completeProcessInstance(engine, subProcessInstance, tmpPi.getProcessDefinitionId(), instance.getId(), cache);
					} else {
						list.add(cache.get(tmpPi.getId()));
					}
				}
			}
		}

		HistoricProcessInstanceQuery historicProcessInstanceQuery = engine.getHistoryService().createHistoricProcessInstanceQuery();
		historicProcessInstanceQuery.superProcessInstanceId(instance.getId());
		List<HistoricProcessInstance> historicProcessInstancelist = historicProcessInstanceQuery.list();
		if (ConditionUtils.isNotEmpty(historicProcessInstancelist)) {
			for (HistoricProcessInstance hpi : historicProcessInstancelist) {
				if (!cache.containsKey(hpi.getId())) {
					ProcessInstance subProcessInstance = ConverterHelper.toProcessInstance(hpi);
					list.add(subProcessInstance);
					cache.put(hpi.getId(), subProcessInstance);
					ProcessServiceHelper.completeProcessInstance(engine, subProcessInstance, hpi.getProcessDefinitionId(), hpi.getSuperProcessInstanceId(), cache);
				} else {
					list.add(cache.get(hpi.getId()));
				}
			}
		}
		instance.setSubProcess(CollectionUtils.toArray(list, ProcessInstance.class));
	}

}
