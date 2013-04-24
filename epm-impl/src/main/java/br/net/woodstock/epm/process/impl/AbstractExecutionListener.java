package br.net.woodstock.epm.process.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public abstract class AbstractExecutionListener implements ExecutionListener {

	private static final long	serialVersionUID	= -6516443594247534772L;

	protected boolean isStartEvent(final DelegateExecution execution) {
		return ExecutionListener.EVENTNAME_START.equals(execution.getEventName());
	}

	protected boolean isEndEvent(final DelegateExecution execution) {
		return ExecutionListener.EVENTNAME_END.equals(execution.getEventName());
	}

	protected boolean isTakeEvent(final DelegateExecution execution) {
		return ExecutionListener.EVENTNAME_TAKE.equals(execution.getEventName());
	}

}
