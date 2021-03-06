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
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.web.faces.DataRepository;
import br.net.woodstock.rockframework.web.faces.primefaces.PrimeFacesDataModel;

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
			DepartmentSkell ds = this.localeService.getDepartmentSkellById(department.getId());
			form.setId(ds.getId());
			form.setName(ds.getName());
			if (ds.getParent() != null) {
				form.setParentId(ds.getParent().getId());
				form.setParentName(ds.getParent().getName());
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

	public PrimeFacesDataModel<User> search(final DepartmentSkellSearch search) {
		DataRepository repository = new DataRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public ORMResult getResult(final Page page) {
				return DepartmentSkellAction.this.getLocaleService().listDepartmentSkellsByName(search.getName(), page);
			}

			@Override
			public Object getObject(final Object id) {
				return DepartmentSkellAction.this.getLocaleService().getDepartmentById((Integer) id);
			}

		};
		PrimeFacesDataModel<User> users = new PrimeFacesDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public TreeNode getTree() {
		TreeNode root = new DefaultTreeNode("root", null);
		Collection<DepartmentSkell> collection = this.localeService.listRootDepartmentSkells(null).getItems();
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
