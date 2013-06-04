package br.net.woodstock.epm.web.security.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.rockframework.core.utils.Collections;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleResourceAction extends AbstractAction {

	private static final long	serialVersionUID	= -3154468176965938956L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public RoleResourceAction() {
		super();
	}

	public boolean edit(final Role role, final RoleResourceForm form) {
		if (role != null) {
			form.setRole(role);

			Collection<Resource> cTarget = this.securityService.listResourcesByRole(role.getId(), null).getItems();
			List<Resource> lTarget = Collections.toList(cTarget);

			Collection<Resource> cSource = this.securityService.listResourcesByName(null, null).getItems();
			List<Resource> lSource = new ArrayList<Resource>();

			for (Resource r : cSource) {
				if (!cTarget.contains(r)) {
					lSource.add(r);
				}
			}

			DualListModel<Resource> resources = new DualListModel<Resource>(lSource, lTarget);
			form.setResources(resources);
			return true;
		}
		return false;
	}

	public void save(final RoleResourceForm form) {
		this.securityService.setRoleResources(form.getRole(), Collections.toArray(form.getResources().getTarget(), Resource.class));

		this.addFacesMessage(this.getMessageOK());
	}

}
