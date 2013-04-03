package br.net.woodstock.epm.orm;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_role")
@Indexed
public class Role extends AbstractIntegerEntity {

	private static final long		serialVersionUID	= -1932408409262519409L;

	private static final String		SEPARATOR			= "/";

	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer					id;

	@Column(name = "role_name", length = 100, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 100)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String					name;

	@Column(name = "role_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean					active;

	@ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "parent_id", referencedColumnName = "role_id", nullable = true)
	private Role					parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<Role>				childs;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "epm_resource_role", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"), inverseJoinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "resource_id"))
	private Set<Resource>			resources;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<UserRole>			users;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<BusinessGroupItem>	swimlaneItems;

	public Role() {
		super();
	}

	public Role(final Integer id) {
		super();
		this.id = id;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
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

	public Role getParent() {
		return this.parent;
	}

	public void setParent(final Role parent) {
		this.parent = parent;
	}

	public Set<Role> getChilds() {
		return this.childs;
	}

	public void setChilds(final Set<Role> childs) {
		this.childs = childs;
	}

	public Set<Resource> getResources() {
		return this.resources;
	}

	public void setResources(final Set<Resource> resources) {
		this.resources = resources;
	}

	public Set<UserRole> getUsers() {
		return this.users;
	}

	public void setUsers(final Set<UserRole> users) {
		this.users = users;
	}

	public Set<BusinessGroupItem> getSwimlaneItems() {
		return this.swimlaneItems;
	}

	public void setSwimlaneItems(final Set<BusinessGroupItem> swimlaneItems) {
		this.swimlaneItems = swimlaneItems;
	}

	// Aux
	public String getParentName() {
		Role parent = this.getParent();
		StringBuilder builder = new StringBuilder();
		if (parent != null) {
			if (parent.getParent() != null) {
				builder.append(parent.getParentName());
				builder.append(Role.SEPARATOR);
			}
			builder.append(parent.getName());
		}
		return builder.toString();
	}

	public String getFullName() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getParentName());
		builder.append(Role.SEPARATOR);
		builder.append(this.getName());
		return builder.toString();
	}

}
