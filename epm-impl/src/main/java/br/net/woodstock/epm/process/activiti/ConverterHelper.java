package br.net.woodstock.epm.process.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;

import br.net.woodstock.epm.acl.api.User;
import br.net.woodstock.epm.process.api.Activity;
import br.net.woodstock.epm.process.api.DateFormField;
import br.net.woodstock.epm.process.api.EnumFormField;
import br.net.woodstock.epm.process.api.Form;
import br.net.woodstock.epm.process.api.FormField;
import br.net.woodstock.epm.process.api.Group;
import br.net.woodstock.epm.process.api.ProcessDefinition;
import br.net.woodstock.epm.process.api.ProcessInstance;
import br.net.woodstock.epm.process.api.TaskInstance;
import br.net.woodstock.rockframework.utils.CollectionUtils;

abstract class ConverterHelper {

	private static final String	DATE_PATTERN_INFORMATION	= "datePattern";

	private static final String	ENUM_VALUES_INFORMATION		= "values";

	private ConverterHelper() {
		//
	}

	public static Activity toActivity(final HistoricActivityInstance activity) {
		if (activity == null) {
			return null;
		}
		Activity a = new Activity();

		a.setEnd(activity.getEndTime());
		a.setId(activity.getId());
		a.setName(activity.getActivityName());
		a.setType(activity.getActivityType());
		a.setStart(activity.getStartTime());

		return a;
	}

	@SuppressWarnings("unchecked")
	public static Form toForm(final TaskFormData formData) {
		if (formData == null) {
			return null;
		}
		Form f = new Form();
		f.setId(formData.getFormKey());
		List<FormField> list = new ArrayList<FormField>();
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

			list.add(ff);
		}
		f.setFields(CollectionUtils.toArray(list, FormField.class));
		return f;
	}

	public static Group toGroup(final org.activiti.engine.identity.Group group) {
		if (group == null) {
			return null;
		}
		Group g = new Group();
		g.setId(group.getId());
		g.setName(group.getName());
		return g;
	}

	public static ProcessInstance toProcessInstance(final org.activiti.engine.runtime.ProcessInstance instance) {
		if (instance == null) {
			return null;
		}
		ProcessInstance p = new ProcessInstance();
		p.setFinished(instance.isEnded());
		p.setId(instance.getId());
		p.setKey(instance.getBusinessKey());
		p.setSuspended(instance.isSuspended());
		return p;
	}

	public static ProcessInstance toProcessInstance(final HistoricProcessInstance instance) {
		if (instance == null) {
			return null;
		}
		ProcessInstance p = new ProcessInstance();
		p.setEnd(instance.getEndTime());
		p.setFinished(true);
		p.setId(instance.getId());
		p.setKey(instance.getBusinessKey());
		p.setStart(instance.getStartTime());
		p.setSuspended(false);
		return p;
	}

	public static ProcessDefinition toProcessDefinition(final org.activiti.engine.repository.ProcessDefinition definition) {
		if (definition == null) {
			return null;
		}
		ProcessDefinition p = new ProcessDefinition();
		p.setId(definition.getId());
		p.setKey(definition.getKey());
		p.setName(definition.getName());
		p.setVersion(Integer.toString(definition.getVersion()));
		return p;
	}

	public static TaskInstance toTask(final org.activiti.engine.task.Task task) {
		if (task == null) {
			return null;
		}
		TaskInstance t = new TaskInstance();
		t.setDescription(task.getDescription());
		t.setId(task.getId());
		t.setName(task.getName());
		t.setStatus(ConverterHelper.toString(task.getDelegationState()));
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
		for (FormField field : form.getFields()) {
			map.put(field.getId(), field.getValue());
		}
		return map;
	}

}
