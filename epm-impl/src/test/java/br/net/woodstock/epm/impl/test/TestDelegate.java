package br.net.woodstock.epm.impl.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class TestDelegate implements JavaDelegate {

	public TestDelegate() {
		super();
	}

	@Override
	public void execute(final DelegateExecution execution) throws Exception {
		System.out.println(execution.getEventName());
		System.out.println(execution.getVariables());
	}

}
