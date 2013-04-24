package br.net.woodstock.epm.process.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Component
public class EndEventExecutionListener extends AbstractExecutionListener {

	private static final long	serialVersionUID	= 626682661759333474L;

	public EndEventExecutionListener() {
		super();
	}

	@Override
	public void notify(final DelegateExecution execution) throws Exception {
		if (this.isEndEvent(execution)) {
			System.out.println("Process ending " + execution.getProcessInstanceId());
		}
	}

}
