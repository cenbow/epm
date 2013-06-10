package br.net.woodstock.epm.web.util;

import org.springframework.stereotype.Component;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.execution.FlowExecutionException;

import br.net.woodstock.epm.util.EPMLog;

@Component("epmFlowExecutionExceptionHandler")
public class EPMFlowExecutionExceptionHandler implements FlowExecutionExceptionHandler {

	public EPMFlowExecutionExceptionHandler() {
		super();
	}

	@Override
	public boolean canHandle(final FlowExecutionException exception) {
		return true;
	}

	@Override
	public void handle(final FlowExecutionException exception, final RequestControlContext context) {
		EPMLog.getLogger().error(exception.getMessage(), exception);
		
		context.endActiveFlowSession("error", context.getAttributes());
		
		context.getExternalContext().getSessionMap().put("exception", exception.getCause());
		context.getExternalContext().requestFlowDefinitionRedirect("error", context.getAttributes());
	}
}
