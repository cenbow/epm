package br.net.woodstock.epm.orm;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_simple_process")
public class SimpleProcess extends AbstractIntegerEntity {

	private static final long				serialVersionUID	= 6261267274337111940L;

	@Id
	@Column(name = "simple_process_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer							id;

	@Column(name = "simple_process_number", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String							number;

	@Column(name = "simple_process_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean							active;

	@ManyToMany(mappedBy = "simpleProcess", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<BusinessProcessInstance>	processInstances;

	public SimpleProcess() {
		super();
	}

	public SimpleProcess(final Integer id) {
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

	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Set<BusinessProcessInstance> getProcessInstances() {
		return this.processInstances;
	}

	public void setProcessInstances(final Set<BusinessProcessInstance> processInstances) {
		this.processInstances = processInstances;
	}

}
