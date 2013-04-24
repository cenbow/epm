package br.net.woodstock.epm.process.impl;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public abstract class AbstractTaskListener implements TaskListener {

	private static final long	serialVersionUID	= 3566788951041431583L;

	public boolean isCreateEvent(final DelegateTask task) {
		return TaskListener.EVENTNAME_CREATE.equals(task.getEventName());
	}

	public boolean isAssigmentEvent(final DelegateTask task) {
		return TaskListener.EVENTNAME_ASSIGNMENT.equals(task.getEventName());
	}

	public boolean isCompleteEvent(final DelegateTask task) {
		return TaskListener.EVENTNAME_COMPLETE.equals(task.getEventName());
	}

	public boolean isAllEvent(final DelegateTask task) {
		return TaskListener.EVENTNAME_ALL_EVENTS.equals(task.getEventName());
	}

}
