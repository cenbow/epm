package br.net.woodstock.epm.web.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import br.net.woodstock.epm.util.EPMLog;

public class EPMAccessDecisionManager extends UnanimousBased {

	@SuppressWarnings("rawtypes")
	public EPMAccessDecisionManager(final List<AccessDecisionVoter> decisionVoters) {
		super(decisionVoters);
	}

	@Override
	public void decide(final Authentication authentication, final Object object, final Collection<ConfigAttribute> attributes) {
		try {
			super.decide(authentication, object, attributes);
		} catch (AccessDeniedException e) {
			FilterInvocation filterInvocation = (FilterInvocation) object;
			String url = filterInvocation.getRequestUrl();
			EPMLog.getLogger().warn("Access denied to " + url);
			EPMLog.getLogger().info(e.getMessage(), e);
			throw e;
		}
	}

}
