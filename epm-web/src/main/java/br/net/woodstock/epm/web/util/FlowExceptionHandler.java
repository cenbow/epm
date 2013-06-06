package br.net.woodstock.epm.web.util;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageResolver;
import org.springframework.stereotype.Component;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.execution.FlowExecutionException;

import br.net.woodstock.epm.util.EPMLog;

@Component
public class FlowExceptionHandler implements FlowExecutionExceptionHandler {

	public FlowExceptionHandler() {
		super();
	}

	@Override
	public boolean canHandle(final FlowExecutionException exception) {
		return true;
	}

	@Override
	public void handle(final FlowExecutionException exception, final RequestControlContext context) {
		EPMLog.getLogger().error(exception.getMessage(), exception);

		MessageBuilder messageBuilder = new MessageBuilder();
		messageBuilder.error().defaultText(exception.getMessage());

		MessageResolver messageResolver = messageBuilder.build();
		context.getMessageContext().addMessage(messageResolver);

		context.getExternalContext().requestExternalRedirect("/spring/error");
	}
}
