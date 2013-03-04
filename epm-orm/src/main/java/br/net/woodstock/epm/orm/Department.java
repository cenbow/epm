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
@Table(name = "epm_department")
@Indexed
public class Department extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= -5897363506398215712L;

	private static final String	SEPARATOR			= "/";

	@Id
	@Column(name = "department_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "department_abbreviation", length = 50, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 50)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String				abbreviation;

	@Column(name = "department_name", length = 100, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 100)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String				name;

	@Column(name = "department_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean				active;

	@ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "department_parent_id", referencedColumnName = "department_id", nullable = true)
	@NotNull
	private Department			parent;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "department_skell_id", referencedColumnName = "department_skell_id", nullable = false)
	@NotNull
	private DepartmentSkell		skell;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<Department>		childs;

	public Department() {
		super();
	}

	public Department(final Integer id) {
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

	public Department getParent() {
		return this.parent;
	}

	public void setParent(final Department parent) {
		this.parent = parent;
	}

	public DepartmentSkell getSkell() {
		return this.skell;
	}

	public void setSkell(final DepartmentSkell skell) {
		this.skell = skell;
	}

	public Set<Department> getChilds() {
		return this.childs;
	}

	public void setChilds(final Set<Department> childs) {
		this.childs = childs;
	}

	// Aux
	public String getFullName() {
		Department parent = this.getParent();
		StringBuilder builder = new StringBuilder();
		if (parent != null) {
			builder.append(parent.getFullName());
			builder.append(Department.SEPARATOR);
		}
		builder.append(this.getName());
		return builder.toString();
	}

	public String getFullAbbreviation() {
		Department parent = this.getParent();
		StringBuilder builder = new StringBuilder();
		if (parent != null) {
			builder.append(parent.getFullAbbreviation());
			builder.append(Department.SEPARATOR);
		}
		builder.append(this.getAbbreviation());
		return builder.toString();
	}

}
