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
import javax.validation.constraints.Size;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_business_process_instance")
public class BusinessProcessInstance extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= -6971991870020680931L;

	@Id
	@Column(name = "business_process_instance_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "business_process_instance_name", length = 30, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 30)
	private String				name;

	@Column(name = "business_process_instance_description", length = 100, nullable = true)
	@Size(min = 1, max = 100)
	private String				description;

	@Column(name = "business_process_instance_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean				active;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "business_process_id", referencedColumnName = "business_process_id", nullable = false)
	@NotNull
	private BusinessProcess		businessProcess;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "simple_process_id", referencedColumnName = "simple_process_id", nullable = false)
	@NotNull
	private SimpleProcess		simpleProcess;

	public BusinessProcessInstance() {
		super();
	}

	public BusinessProcessInstance(final Integer id) {
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public BusinessProcess getBusinessProcess() {
		return this.businessProcess;
	}

	public void setBusinessProcess(final BusinessProcess businessProcess) {
		this.businessProcess = businessProcess;
	}

	public SimpleProcess getSimpleProcess() {
		return this.simpleProcess;
	}

	public void setSimpleProcess(final SimpleProcess simpleProcess) {
		this.simpleProcess = simpleProcess;
	}

}
