package br.net.woodstock.epm.process.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Component
public class StartEventExecutionListener extends AbstractExecutionListener {

	private static final long	serialVersionUID	= 530857808732684674L;

	public StartEventExecutionListener() {
		super();
	}

	@Override
	public void notify(final DelegateExecution execution) throws Exception {
		if (this.isStartEvent(execution)) {
			System.out.println("Process starting " + execution.getProcessInstanceId());
		}
	}

}
