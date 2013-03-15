package br.net.woodstock.epm.web.security.department;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import br.net.woodstock.epm.web.AbstractForm;
import br.net.woodstock.epm.web.tree.TreeItem;

public class DepartmentForm extends AbstractForm {

	private static final long	serialVersionUID	= 6243763629739870386L;

	private Integer				id;

	private String				abbreviation;

	private String				name;

	private Boolean				active;

	private Integer				parentId;

	private String				parentName;

	private Integer				skellId;

	private String				skellName;

	private TreeNode			selectedParent;

	private TreeNode			selectedSkell;

	public DepartmentForm() {
		super();
	}

	@Override
	public void reset() {
		this.setAbbreviation(null);
		this.setActive(null);
		this.setId(null);
		this.setName(null);
		this.setParentId(null);
		this.setParentName(null);
		this.setSkellId(null);
		this.setSkellName(null);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public void setAbbreviation(final String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
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

	public Integer getSkellId() {
		return this.skellId;
	}

	public void setSkellId(final Integer skellId) {
		this.skellId = skellId;
	}

	public String getSkellName() {
		return this.skellName;
	}

	public void setSkellName(final String skellName) {
		this.skellName = skellName;
	}

	public TreeNode getSelectedParent() {
		return this.selectedParent;
	}

	public void setSelectedParent(final TreeNode selectedParent) {
		this.selectedParent = selectedParent;
	}

	public TreeNode getSelectedSkell() {
		return this.selectedSkell;
	}

	public void setSelectedSkell(final TreeNode selectedSkell) {
		this.selectedSkell = selectedSkell;
	}

	// Aux
	public void onSelectParent(final NodeSelectEvent e) {
		TreeNode node = e.getTreeNode();
		TreeItem item = (TreeItem) node.getData();
		this.parentId = item.getId();
		this.parentName = item.getName();
	}

	// Aux
	public void onSelectSkell(final NodeSelectEvent e) {
		TreeNode node = e.getTreeNode();
		TreeItem item = (TreeItem) node.getData();
		this.skellId = item.getId();
		this.skellName = item.getName();
	}

}
