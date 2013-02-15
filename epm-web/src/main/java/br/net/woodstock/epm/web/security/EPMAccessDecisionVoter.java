package br.net.woodstock.epm.web.security;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

public class EPMAccessDecisionVoter implements AccessDecisionVoter<Object>, Serializable {

	private static final long	serialVersionUID	= 6919229901562509996L;

	public EPMAccessDecisionVoter() {
		super();
	}

	@Override
	public int vote(final Authentication authentication, final Object object, final Collection<ConfigAttribute> attributes) {
		FilterInvocation filterInvocation = (FilterInvocation) object;
		String url = filterInvocation.getRequestUrl();
		int result = AccessDecisionVoter.ACCESS_ABSTAIN;

		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				result = AccessDecisionVoter.ACCESS_DENIED;
				if (authentication instanceof EPMAuthentication) {
					EPMAuthentication wa = (EPMAuthentication) authentication;

					outer: for (EPMGrantedAuthority wga : wa.getAuthorities()) {
						for (String permission : wga.getPermissions()) {
							if (permission.equals(url)) {
								result = AccessDecisionVoter.ACCESS_GRANTED;
								break outer;
							}
						}
					}

				}
			}
		}

		// if (result == AccessDecisionVoter.ACCESS_DENIED) {
		// EPMLog.getLogger().warn("Access Denied!!! User: " + authentication.getName() + " URL: " + url);
		// }

		return result;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		if (FilterInvocation.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean supports(final ConfigAttribute attribute) {
		if (attribute.getAttribute().startsWith(EPMAuthenticationHelper.ROLE_PREFIX)) {
			return true;
		}
		return false;
	}

}
