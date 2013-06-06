package br.net.woodstock.epm.web.util;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;

import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.rockframework.core.utils.Arrays;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FlowRegistryAction extends AbstractAction {

	private static final long		serialVersionUID	= -5731589957885801262L;

	@Autowired(required = true)
	private FlowDefinitionRegistry	registry;

	public FlowRegistryAction() {
		super();
	}

	public Collection<String> getFlowNames() {
		String[] ids = this.registry.getFlowDefinitionIds();
		return Arrays.toList(ids);
	}

}
