package br.net.woodstock.epm.web.security.departmentSkell;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import br.net.woodstock.epm.web.AbstractForm;
import br.net.woodstock.epm.web.tree.TreeItem;

public class DepartmentSkellForm extends AbstractForm {

	private static final long	serialVersionUID	= -1976299756804938146L;

	private Integer				id;

	private String				name;

	private Integer				parentId;

	private String				parentName;

	private TreeNode			selectedNode;

	public DepartmentSkellForm() {
		super();
	}

	@Override
	public void reset() {
		this.setId(null);
		this.setName(null);
		this.setParentId(null);
		this.setParentName(null);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(final Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return this.parentName;
	}

	public void setParentName(final String parentName) {
		this.parentName = parentName;
	}

	public TreeNode getSelectedNode() {
		return this.selectedNode;
	}

	public void setSelectedNode(final TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	// Aux
	public void onNodeSelect(final NodeSelectEvent e) {
		TreeNode node = e.getTreeNode();
		TreeItem item = (TreeItem) node.getData();
		this.parentId = item.getId();
		this.parentName = item.getName();
	}

}
