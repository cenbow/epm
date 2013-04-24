package br.net.woodstock.epm.process.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Component
public class TakeEventExecutionListener extends AbstractExecutionListener {

	private static final long	serialVersionUID	= -8074913423442511938L;

	public TakeEventExecutionListener() {
		super();
	}

	@Override
	public void notify(final DelegateExecution execution) throws Exception {
		if (this.isStartEvent(execution)) {
			System.out.println("Process take " + execution.getProcessInstanceId());
		}
	}

}
