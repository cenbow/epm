package br.net.woodstock.epm.process.activiti.client;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public abstract class JavaDelegateTask implements JavaDelegate {

	@Override
	public final void execute(final DelegateExecution delegate) throws Exception {
		this.execute(new Execution(delegate));
	}

	protected abstract void execute(final Execution execution) throws Exception;

}
