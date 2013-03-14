package br.net.woodstock.epm.web.security.departmentSkell;

import java.util.Collection;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.LocaleService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.epm.web.tree.TreeItem;
import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityDataModel;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityRepository;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentSkellAction extends AbstractAction {

	private static final long	serialVersionUID	= -6705247715553054517L;

	@Autowired(required = true)
	private LocaleService		localeService;

	public DepartmentSkellAction() {
		super();
	}

	public boolean edit(final DepartmentSkell department, final DepartmentSkellForm form) {
		if (department != null) {
			form.setId(department.getId());
			form.setName(department.getName());
			if (department.getParent() != null) {
				form.setParentId(department.getParent().getId());
				form.setParentName(department.getParent().getName());
			}
			return true;
		}
		return false;
	}

	public void save(final DepartmentSkellForm form) {
		DepartmentSkell department = new DepartmentSkell();
		department.setId(form.getId());
		department.setName(form.getName());

		if (form.getParentId() != null) {
			department.setParent(new DepartmentSkell(form.getParentId()));
		}

		if (department.getId() != null) {
			this.localeService.updateDepartmentSkell(department);
		} else {
			this.localeService.saveDepartmentSkell(department);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public EntityDataModel<User> search(final DepartmentSkellSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public QueryResult getResult(final Page page) {
				return DepartmentSkellAction.this.getLocaleService().listDepartmentSkellsByName(search.getName(), page);
			}

			@Override
			public Object getEntity(final Object id) {
				return DepartmentSkellAction.this.getLocaleService().getDepartmentById((Integer) id);
			}

		};
		EntityDataModel<User> users = new EntityDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public TreeNode getTree() {
		TreeNode root = new DefaultTreeNode("root", null);
		Collection<DepartmentSkell> collection = this.localeService.listRootDepartmentSkells();
		for (DepartmentSkell ds : collection) {
			this.addNode(root, ds);
		}
		return root;
	}

	private void addNode(final TreeNode parent, final DepartmentSkell department) {
		TreeItem item = new TreeItem(department.getId(), department.getName(), department.getFullName());
		TreeNode node = new DefaultTreeNode(item, parent);
		if (department.getChilds() != null) {
			for (DepartmentSkell ds : department.getChilds()) {
				this.addNode(node, ds);
			}
		}
	}

	public LocaleService getLocaleService() {
		return this.localeService;
	}

}
