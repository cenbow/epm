package br.net.woodstock.epm.web.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.web.faces.DataRepository;
import br.net.woodstock.rockframework.web.faces.primefaces.PrimeFacesDataModel;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserAction extends AbstractAction {

	private static final long	serialVersionUID	= -3154468176965938956L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public UserAction() {
		super();
	}

	public boolean edit(final User user, final UserForm form) {
		if (user != null) {
			User u = this.securityService.getUserById(user.getId());
			form.setActive(u.getActive());
			form.setEmail(u.getEmail());
			form.setId(u.getId());
			form.setLogin(u.getLogin());
			form.setName(u.getName());
			form.setPassword(u.getPassword());
			return true;
		}
		return false;
	}

	public void save(final UserForm form) {
		User user = new User();
		user.setActive(form.getActive());
		user.setEmail(form.getEmail());
		user.setId(form.getId());
		user.setLogin(form.getLogin());
		user.setName(form.getName());
		user.setPassword(form.getPassword());

		if (user.getId() != null) {
			this.securityService.updateUser(user);
		} else {
			this.securityService.saveUser(user);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public PrimeFacesDataModel<User> search(final UserSearch search) {
		DataRepository repository = new DataRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public ORMResult getResult(final Page page) {
				return UserAction.this.getSecurityService().listUsersByName(search.getName(), page);
			}

			@Override
			public Object getObject(final Object id) {
				return UserAction.this.getSecurityService().getUserById((Integer) id);
			}

		};
		PrimeFacesDataModel<User> users = new PrimeFacesDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public SecurityService getSecurityService() {
		return this.securityService;
	}

}
