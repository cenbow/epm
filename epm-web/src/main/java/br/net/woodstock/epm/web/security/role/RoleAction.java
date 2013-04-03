package br.net.woodstock.epm.web.security.role;

import java.util.Collection;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.epm.web.tree.TreeItem;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.web.faces.EntityRepository;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityDataModel;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleAction extends AbstractAction {

	private static final long	serialVersionUID	= 8763250019756637129L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public RoleAction() {
		super();
	}

	public boolean edit(final Role role, final RoleForm form) {
		if (role != null) {
			form.setActive(role.getActive());
			form.setId(role.getId());
			form.setName(role.getName());
			if (role.getParent() != null) {
				form.setParentId(role.getParent().getId());
				form.setParentName(role.getParent().getName());
			}
			return true;
		}
		return false;
	}

	public void save(final RoleForm form) {
		Role role = new Role();
		role.setActive(form.getActive());
		role.setId(form.getId());
		role.setName(form.getName());

		if (form.getParentId() != null) {
			role.setParent(new Role(form.getParentId()));
		}

		if (role.getId() != null) {
			this.securityService.updateRole(role);
		} else {
			this.securityService.saveRole(role);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public EntityDataModel<User> search(final RoleSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public ORMResult getResult(final Page page) {
				return RoleAction.this.getSecurityService().listRolesByName(search.getName(), page);
			}

			@Override
			public Object getEntity(final Object id) {
				return RoleAction.this.getSecurityService().getRoleById((Integer) id);
			}

		};
		EntityDataModel<User> users = new EntityDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	public TreeNode getTree() {
		TreeNode root = new DefaultTreeNode("root", null);
		Collection<Role> collection = this.securityService.listRootRoles();
		for (Role r : collection) {
			this.addNode(root, r);
		}
		return root;
	}

	private void addNode(final TreeNode parent, final Role role) {
		TreeItem item = new TreeItem(role.getId(), role.getName(), role.getFullName());
		TreeNode node = new DefaultTreeNode(item, parent);
		if (role.getChilds() != null) {
			for (Role r : role.getChilds()) {
				this.addNode(node, r);
			}
		}
	}

	public SecurityService getSecurityService() {
		return this.securityService;
	}

}
