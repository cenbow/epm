package br.net.woodstock.epm.orm;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_process")
public class Process extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= 6261267274337111940L;

	@Id
	@Column(name = "process_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "process_definition_id", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String				processDefinition;

	@Column(name = "process_name", length = 30, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 30)
	private String				name;

	@Column(name = "process_description", length = 100, nullable = true)
	@Size(min = 1, max = 100)
	private String				description;

	@Column(name = "process_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean				active;

	@Column(name = "deployment_type", nullable = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private DeploymentType		type;

	@Column(name = "process_bin", nullable = false)
	@NotNull
	@Lob
	@Basic(optional = false, fetch = FetchType.LAZY)
	private byte[]				bin;

	@ManyToMany(mappedBy = "process", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<Task>			tasks;

	public Process() {
		super();
	}

	public Process(final Integer id) {
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

	public String getProcessDefinition() {
		return this.processDefinition;
	}

	public void setProcessDefinition(final String processDefinition) {
		this.processDefinition = processDefinition;
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

	public DeploymentType getType() {
		return this.type;
	}

	public void setType(final DeploymentType type) {
		this.type = type;
	}

	public byte[] getBin() {
		return this.bin;
	}

	public void setBin(final byte[] bin) {
		this.bin = bin;
	}

	public Set<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(final Set<Task> tasks) {
		this.tasks = tasks;
	}

}
