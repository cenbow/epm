package br.net.woodstock.epm.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.epm.web.AbstractAction;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class LogonAction extends AbstractAction {

	private static final long	serialVersionUID	= -3154468176965938956L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public LogonAction() {
		super();
	}

	public boolean logon(final LogonForm form) {
		User user = this.securityService.getUserByLoginPassword(form.getLogin(), form.getPassword());
		if (user != null) {
			SecurityContextHolder.getContext().setAuthentication(EPMAuthenticationHelper.toAuthentication(user));
			return true;
		}
		return false;
	}

	public boolean logoff() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return true;
	}

}
