package br.net.woodstock.epm.orm;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Indexed;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_business_group_item")
@Indexed
public class BusinessGroupItem extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= -4698111874290305730L;

	@Id
	@Column(name = "business_group_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "business_group_id", referencedColumnName = "business_group_id", nullable = false)
	@NotNull
	private BusinessGroup		group;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
	@NotNull
	private Role				role;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "department_skell_id", referencedColumnName = "department_skell_id", nullable = false)
	@NotNull
	private DepartmentSkell		departmentSkell;

	public BusinessGroupItem() {
		super();
	}

	public BusinessGroupItem(final Integer id) {
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

	public BusinessGroup getSwimlane() {
		return this.group;
	}

	public void setSwimlane(final BusinessGroup group) {
		this.group = group;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(final Role role) {
		this.role = role;
	}

	public DepartmentSkell getDepartmentSkell() {
		return this.departmentSkell;
	}

	public void setDepartmentSkell(final DepartmentSkell departmentSkell) {
		this.departmentSkell = departmentSkell;
	}

}
