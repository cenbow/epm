package br.net.woodstock.epm.web.security;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.faces.model.OneSelectionTrackingListDataModel;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;
import br.net.woodstock.rockframework.utils.CollectionUtils;

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

	public OneSelectionTrackingListDataModel search(final UserSearch search) {
		QueryResult result = this.securityService.listUsersByName(search.getName(), null);
		Collection<User> users = result.getResult();
		OneSelectionTrackingListDataModel dataModel = new OneSelectionTrackingListDataModel(CollectionUtils.toList(users));
		return dataModel;
	}

}
