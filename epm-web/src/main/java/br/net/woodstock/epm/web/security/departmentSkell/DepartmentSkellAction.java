package br.net.woodstock.epm.web.security.departmentSkell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.LocaleService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityDataModel;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityRepository;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentSkellAction extends AbstractAction {

	private static final long	serialVersionUID	= -6705247715553054517L;

	@Autowired(required = true)
	private LocaleService		securityService;

	public DepartmentSkellAction() {
		super();
	}

	public boolean edit(final DepartmentSkell department, final DepartmentSkellForm form) {
		if (department != null) {
			form.setId(department.getId());
			form.setName(department.getName());
			form.setParent(department.getParent());
			return true;
		}
		return false;
	}

	public void save(final DepartmentSkellForm form) {
		DepartmentSkell department = new DepartmentSkell();
		department.setId(form.getId());
		department.setName(form.getName());
		department.setParent(form.getParent());

		if (department.getId() != null) {
			this.securityService.updateDepartmentSkell(department);
		} else {
			this.securityService.saveDepartmentSkell(department);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public EntityDataModel<User> search(final DepartmentSkellSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public QueryResult getResult(final Page page) {
				return DepartmentSkellAction.this.getLocaleService().listDepartmentsByName(search.getName(), page);
			}

			@Override
			public Object getEntity(final Object id) {
				return DepartmentSkellAction.this.getLocaleService().getDepartmentById((Integer) id);
			}

		};
		EntityDataModel<User> users = new EntityDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public LocaleService getLocaleService() {
		return this.securityService;
	}

}
