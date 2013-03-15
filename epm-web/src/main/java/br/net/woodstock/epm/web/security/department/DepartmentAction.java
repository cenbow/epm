package br.net.woodstock.epm.web.security.department;

import java.util.Collection;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.Department;
import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.LocaleService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.epm.web.tree.TreeItem;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.web.faces.EntityRepository;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityDataModel;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentAction extends AbstractAction {

	private static final long	serialVersionUID	= -3462021569529103583L;

	@Autowired(required = true)
	private LocaleService		localeService;

	public DepartmentAction() {
		super();
	}

	public boolean edit(final Department department, final DepartmentForm form) {
		if (department != null) {
			form.setAbbreviation(department.getAbbreviation());
			form.setActive(department.getActive());
			form.setId(department.getId());
			form.setName(department.getName());
			if (department.getParent() != null) {
				form.setParentId(department.getParent().getId());
				form.setParentName(department.getParent().getName());
			}
			if (department.getSkell() != null) {
				form.setSkellId(department.getSkell().getId());
				form.setSkellName(department.getSkell().getName());
			}
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

		if (form.getParentId() != null) {
			department.setParent(new Department(form.getParentId()));
		}

		if (form.getSkellId() != null) {
			department.setSkell(new DepartmentSkell(form.getSkellId()));
		}

		if (department.getId() != null) {
			this.localeService.updateDepartment(department);
		} else {
			this.localeService.saveDepartment(department);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public EntityDataModel<User> search(final DepartmentSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public ORMResult getResult(final Page page) {
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

	public TreeNode getTree() {
		TreeNode root = new DefaultTreeNode("root", null);
		Collection<Department> collection = this.localeService.listRootDepartments();
		for (Department d : collection) {
			this.addNode(root, d);
		}
		return root;
	}

	private void addNode(final TreeNode parent, final Department department) {
		TreeItem item = new TreeItem(department.getId(), department.getName(), department.getFullName());
		TreeNode node = new DefaultTreeNode(item, parent);
		if (department.getChilds() != null) {
			for (Department d : department.getChilds()) {
				this.addNode(node, d);
			}
		}
	}

	public LocaleService getLocaleService() {
		return this.localeService;
	}

}
