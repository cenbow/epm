package br.net.woodstock.epm.web.security.user;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.UserRole;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.rockframework.core.utils.Collections;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.web.faces.EntityRepository;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityDataModel;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserRoleAction extends AbstractAction {

	private static final long	serialVersionUID	= -3154468176965938956L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public UserRoleAction() {
		super();
	}

	public boolean edit(final UserRole role, final UserRoleForm form) {
		if (role != null) {
			form.setActive(role.getActive());
			form.setDepartment(role.getDepartment());
			form.setId(role.getId());
			form.setRole(role.getRole());
			form.setUser(role.getUser());
			return true;
		}
		return false;
	}

	public void save(final UserRoleForm form) {
		UserRole role = new UserRole();
		role.setActive(form.getActive());
		role.setDepartment(form.getDepartment());
		role.setId(form.getId());
		role.setRole(form.getRole());
		role.setUser(form.getUser());

		if (role.getId() != null) {
			this.securityService.updateUserRole(role);
		} else {
			this.securityService.saveUserRole(role);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public EntityDataModel<UserRole> search(final UserRoleSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -336605554089769356L;

			@Override
			public ORMResult getResult(final Page page) {
				return UserRoleAction.this.getSecurityService().listUserRolesByUser(search.getUserId(), page);
			}

			@Override
			public Object getEntity(final Object id) {
				return UserRoleAction.this.getSecurityService().getUserById((Integer) id);
			}

		};
		EntityDataModel<UserRole> users = new EntityDataModel<UserRole>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public List<Role> listRoles() {
		ORMResult result = this.securityService.listRolesByName(null, null);
		Collection<Role> roles = result.getItems();
		return Collections.toList(roles);
	}

	public SecurityService getSecurityService() {
		return this.securityService;
	}

}
