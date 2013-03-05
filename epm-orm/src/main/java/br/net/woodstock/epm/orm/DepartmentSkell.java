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

import br.net.woodstock.rockframework.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_department_skell")
@Indexed
public class DepartmentSkell extends AbstractIntegerEntity {

	private static final long		serialVersionUID	= -4698111874290305730L;

	private static final String		SEPARATOR			= "/";

	@Id
	@Column(name = "department_skell_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer					id;

	@Column(name = "department_skell_name", length = 100, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 100)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String					name;

	@ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "department_skell_parent_id", referencedColumnName = "department_skell_id", nullable = true)
	private DepartmentSkell			parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<DepartmentSkell>	childs;

	@OneToMany(mappedBy = "skell", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<Department>			departments;

	public DepartmentSkell() {
		super();
	}

	public DepartmentSkell(final Integer id) {
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

	public DepartmentSkell getParent() {
		return this.parent;
	}

	public void setParent(final DepartmentSkell parent) {
		this.parent = parent;
	}

	public Set<DepartmentSkell> getChilds() {
		return this.childs;
	}

	public void setChilds(final Set<DepartmentSkell> childs) {
		this.childs = childs;
	}

	public Set<Department> getDepartments() {
		return this.departments;
	}

	public void setDepartments(final Set<Department> departments) {
		this.departments = departments;
	}

	// Aux
	public String getParentName() {
		DepartmentSkell parent = this.getParent();
		StringBuilder builder = new StringBuilder();
		if (parent != null) {
			if (parent.getParent() != null) {
				builder.append(parent.getParentName());
				builder.append(DepartmentSkell.SEPARATOR);
			}
			builder.append(parent.getName());
		}
		return builder.toString();
	}

	public String getFullName() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getParentName());
		builder.append(DepartmentSkell.SEPARATOR);
		builder.append(this.getName());
		return builder.toString();
	}

}
