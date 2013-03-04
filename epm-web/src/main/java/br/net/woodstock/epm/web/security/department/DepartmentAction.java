package br.net.woodstock.epm.web.security.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.Department;
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
public class DepartmentAction extends AbstractAction {

	private static final long	serialVersionUID	= -3462021569529103583L;

	@Autowired(required = true)
	private LocaleService		securityService;

	public DepartmentAction() {
		super();
	}

	public boolean edit(final Department department, final DepartmentForm form) {
		if (department != null) {
			form.setAbbreviation(department.getAbbreviation());
			form.setActive(department.getActive());
			form.setId(department.getId());
			form.setName(department.getName());
			form.setParent(department.getParent());
			form.setSkell(department.getSkell());
			return true;
		}
		return false;
	}

	public void save(final DepartmentForm form) {
		Department department = new Department();
		department.setAbbreviation(form.getAbbreviation());
		department.setActive(form.getActive());
		department.setId(form.getId());
		department.setName(form.getName());
		department.setParent(form.getParent());
		department.setSkell(form.getSkell());

		if (department.getId() != null) {
			this.securityService.updateDepartment(department);
		} else {
			this.securityService.saveDepartment(department);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public EntityDataModel<User> search(final DepartmentSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public QueryResult getResult(final Page page) {
				return DepartmentAction.this.getLocaleService().listDepartmentsByName(search.getName(), page);
			}

			@Override
			public Object getEntity(final Object id) {
				return DepartmentAction.this.getLocaleService().getDepartmentById((Integer) id);
			}

		};
		EntityDataModel<User> users = new EntityDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public LocaleService getLocaleService() {
		return this.securityService;
	}

}
