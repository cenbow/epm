package br.net.woodstock.epm.process.activiti.client;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

public class Execution {

	private DelegateExecution	delegate;

	public Execution(final DelegateExecution delegate) {
		super();
		this.delegate = delegate;
	}

	public void createVariableLocal(final String name, final Object value) {
		this.delegate.createVariableLocal(name, value);
	}

	public String getCurrentActivityId() {
		return this.delegate.getCurrentActivityId();
	}

	public String getCurrentActivityName() {
		return this.delegate.getCurrentActivityName();
	}

	public String getEventName() {
		return this.delegate.getEventName();
	}

	public String getId() {
		return this.delegate.getId();
	}

	public String getProcessInstanceId() {
		return this.delegate.getProcessInstanceId();
	}

	public Object getVariable(final String name) {
		return this.delegate.getVariable(name);
	}

	public Map<String, Object> getVariables() {
		return this.delegate.getVariables();
	}

	public void removeVariable(final String name) {
		this.delegate.removeVariable(name);
	}

	public void setVariable(final String name, final Object value) {
		this.delegate.setVariable(name, value);
	}

}
