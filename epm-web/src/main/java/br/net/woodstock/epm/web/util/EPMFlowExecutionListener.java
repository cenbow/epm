package br.net.woodstock.epm.web.util;

import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;

import br.net.woodstock.epm.util.EPMLog;

public class EPMFlowExecutionListener extends FlowExecutionListenerAdapter {

	public EPMFlowExecutionListener() {
		super();
	}

	@Override
	public void sessionStarting(final RequestContext context, final FlowSession session, final MutableAttributeMap input) {
		EPMLog.getLogger().debug("Starting flow " + context.getActiveFlow().getId());
	}

	@Override
	public void sessionEnding(final RequestContext context, final FlowSession session, final String outcome, final MutableAttributeMap output) {
		EPMLog.getLogger().debug("Ending flow " + context.getActiveFlow().getId());
	}

	@Override
	public void transitionExecuting(final RequestContext context, final TransitionDefinition transition) {
		EPMLog.getLogger().debug("Transition on flow " + context.getActiveFlow().getId() + ", " + transition.getId() + " => " + transition.getTargetStateId());
	}

}
