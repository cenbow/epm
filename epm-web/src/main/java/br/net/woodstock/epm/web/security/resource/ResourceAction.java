package br.net.woodstock.epm.web.security.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.web.faces.EntityRepository;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityDataModel;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResourceAction extends AbstractAction {

	private static final long	serialVersionUID	= -3462021569529103583L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public ResourceAction() {
		super();
	}

	public boolean edit(final Resource resource, final ResourceForm form) {
		if (resource != null) {
			form.setActive(resource.getActive());
			form.setId(resource.getId());
			form.setName(resource.getName());
			return true;
		}
		return false;
	}

	public void save(final ResourceForm form) {
		Resource resource = new Resource();
		resource.setActive(form.getActive());
		resource.setId(form.getId());
		resource.setName(form.getName());

		if (resource.getId() != null) {
			this.securityService.updateResource(resource);
		} else {
			this.securityService.saveResource(resource);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public EntityDataModel<User> search(final ResourceSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public ORMResult getResult(final Page page) {
				return ResourceAction.this.getSecurityService().listResourcesByName(search.getName(), page);
			}

			@Override
			public Object getEntity(final Object id) {
				return ResourceAction.this.getSecurityService().getResourceById((Integer) id);
			}

		};
		EntityDataModel<User> users = new EntityDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public SecurityService getSecurityService() {
		return this.securityService;
	}

}
