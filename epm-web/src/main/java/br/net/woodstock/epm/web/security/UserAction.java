package br.net.woodstock.epm.web.security;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;

@Controller
// @Scope(WebApplicationContext.SCOPE_APPLICATION)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserAction implements Serializable {

	private static final long	serialVersionUID	= -3154468176965938956L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public UserAction() {
		super();
	}

	public void saveUser(final UserForm form) {
		User user = new User();
		user.setActive(form.getActive());
		user.setEmail(form.getEmail());
		user.setId(form.getId());
		user.setLogin(form.getLogin());
		user.setName(form.getName());
		user.setPassword(form.getPassword());

		this.securityService.saveUser(user);
	}

}
