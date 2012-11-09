package br.net.woodstock.epm.web.security;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityDataModel;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityRepository;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserAction implements Serializable {

	private static final long	serialVersionUID	= -3154468176965938956L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public UserAction() {
		super();
	}

	public void save(final UserForm form) {
		User user = new User();
		user.setActive(form.getActive());
		user.setEmail(form.getEmail());
		user.setId(form.getId());
		user.setLogin(form.getLogin());
		user.setName(form.getName());
		user.setPassword(form.getPassword());

		this.securityService.saveUser(user);
	}

	public EntityDataModel<User> search(final UserSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public QueryResult getResult(final Page page) {
				return UserAction.this.getSecurityService().listUsersByName(search.getName(), page);
			}

			@Override
			public Object getEntity(final Object id) {
				return UserAction.this.getSecurityService().getUserById((Integer) id);
			}

		};
		EntityDataModel<User> users = new EntityDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public SecurityService getSecurityService() {
		return this.securityService;
	}

}
