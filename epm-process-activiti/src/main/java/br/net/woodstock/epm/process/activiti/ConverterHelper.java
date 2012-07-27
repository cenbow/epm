package br.net.woodstock.epm.process.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

import br.net.woodstock.epm.api.User;
import br.net.woodstock.epm.process.api.Activity;
import br.net.woodstock.epm.process.api.DateFormField;
import br.net.woodstock.epm.process.api.EnumFormField;
import br.net.woodstock.epm.process.api.Form;
import br.net.woodstock.epm.process.api.FormField;
import br.net.woodstock.epm.process.api.Process;
import br.net.woodstock.epm.process.api.ProcessInstance;
import br.net.woodstock.epm.process.api.Task;
import br.net.woodstock.rockframework.utils.ConditionUtils;

abstract class ConverterHelper {

	private static final String	DATE_PATTERN_INFORMATION	= "datePattern";

	private static final String	ENUM_VALUES_INFORMATION		= "values";

	private ConverterHelper() {
		//
	}

	public static Form toForm(final TaskFormData formData) {
		if (formData == null) {
			return null;
		}
		Form f = new Form();
		f.setId(formData.getFormKey());
		for (FormProperty property : formData.getFormProperties()) {
			FormType ft = property.getType();
			String type = ft.getName();
			FormField ff = null;

			if (FieldType.DATE.getType().equals(type)) {
				String datePattern = (String) ft.getInformation(ConverterHelper.DATE_PATTERN_INFORMATION);
				ff = new DateFormField();
				((DateFormField) ff).setPattern(datePattern);
			} else if (FieldType.ENUM.getType().equals(type)) {
				Map<String, String> values = (Map<String, String>) ft.getInformation(ConverterHelper.ENUM_VALUES_INFORMATION);
				ff = new EnumFormField();
				((EnumFormField) ff).setValues(values);
			} else {
				ff = new FormField();
			}

			ff.setId(property.getId());
			ff.setName(property.getName());
			ff.setReadable(property.isReadable());
			ff.setRequired(property.isRequired());
			ff.setValue(property.getValue());
			ff.setWriteable(property.isWritable());

			ff.setType(type);

			f.getFields().put(ff.getId(), ff);
		}
		return f;
	}

	public static void completeProcessInstance(final ProcessEngine engine, final ProcessInstance instance, final String processDefinitionId) {
		if (instance == null) {
			return;
		}

		ProcessDefinitionQuery processQuery = engine.getRepositoryService().createProcessDefinitionQuery();
		processQuery.processDefinitionId(processDefinitionId);
		ProcessDefinition processDefinition = processQuery.singleResult();

		Process process = new Process();
		process.setId(processDefinition.getId());
		process.setName(processDefinition.getName());
		process.setVersion(Integer.toString(processDefinition.getVersion()));

		instance.setProcess(process);

		HistoricActivityInstanceQuery activityInstanceQuery = engine.getHistoryService().createHistoricActivityInstanceQuery();
		activityInstanceQuery.processInstanceId(instance.getId());
		List<HistoricActivityInstance> activityList = activityInstanceQuery.list();

		if (ConditionUtils.isNotEmpty(activityList)) {
			for (HistoricActivityInstance activityInstance : activityList) {

				Activity a = new Activity();

				a.setEnd(activityInstance.getEndTime());
				a.setId(activityInstance.getId());
				a.setName(activityInstance.getActivityName());
				a.setType(activityInstance.getActivityType());
				a.setStart(activityInstance.getStartTime());

				if (ConditionUtils.isNotEmpty(activityInstance.getExecutionId())) {
					engine.getRuntimeService().createExecutionQuery();
				}

				if (ConditionUtils.isNotEmpty(activityInstance.getAssignee())) {
					UserQuery userQuery = engine.getIdentityService().createUserQuery();
					userQuery.userId(activityInstance.getAssignee());
					org.activiti.engine.identity.User user = userQuery.singleResult();
					a.setUser(ConverterHelper.toUser(user));
				}

				if (activityInstance.getEndTime() != null) {
					instance.getHistory().add(a);
				} else {
					instance.getCurrent().add(a);
				}
			}
		}
	}

	public static Process toProcess(final ProcessDefinition definition) {
		if (definition == null) {
			return null;
		}
		Process p = new Process();
		p.setId(definition.getId());
		p.setName(definition.getName());
		p.setVersion(Integer.toString(definition.getVersion()));
		return p;
	}

	public static Task toTask(final ProcessEngine engine, final org.activiti.engine.task.Task task) {
		if (task == null) {
			return null;
		}
		Task t = new Task();
		t.setDescription(task.getDescription());
		t.setId(task.getId());
		t.setName(task.getName());
		t.setStatus(ConverterHelper.toString(task.getDelegationState()));

		if (ConditionUtils.isNotEmpty(task.getAssignee())) {
			UserQuery userQuery = engine.getIdentityService().createUserQuery();
			userQuery.userId(task.getAssignee());
			org.activiti.engine.identity.User user = userQuery.singleResult();
			t.setUser(ConverterHelper.toUser(user));
		}
		if (ConditionUtils.isNotEmpty(task.getOwner())) {
			UserQuery userQuery = engine.getIdentityService().createUserQuery();
			userQuery.userId(task.getOwner());
			org.activiti.engine.identity.User user = userQuery.singleResult();
			t.setOwner(ConverterHelper.toUser(user));
		}
		return t;
	}

	public static User toUser(final org.activiti.engine.identity.User user) {
		if (user == null) {
			return null;
		}
		User u = new User();
		u.setEmail(user.getEmail());
		u.setId(user.getId());
		u.setName((user.getFirstName() + " " + user.getLastName()).trim());
		return u;
	}

	@SuppressWarnings("rawtypes")
	public static String toString(final Enum e) {
		if (e != null) {
			return e.name();
		}
		return null;
	}

	public static Map<String, String> toMap(final Form form) {
		if (form == null) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<String, FormField> entry : form.getFields().entrySet()) {
			map.put(entry.getKey(), entry.getValue().getValue());
		}
		return map;
	}

}
