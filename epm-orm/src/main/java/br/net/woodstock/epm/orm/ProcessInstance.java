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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_process_instance")
public class ProcessInstance extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= -6971991870020680931L;

	@Id
	@Column(name = "process_instance_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "process_instance_ref", length = 100, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 100)
	private String				processInstanceId;

	@Column(name = "process_instance_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean				active;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "process_id", referencedColumnName = "process_id", nullable = false)
	@NotNull
	private Process				process;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "simple_process_id", referencedColumnName = "simple_process_id", nullable = false)
	@NotNull
	private SimpleProcess		simpleProcess;

	@ManyToMany(mappedBy = "processInstance", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<TaskInstance>	taskInstances;

	public ProcessInstance() {
		super();
	}

	public ProcessInstance(final Integer id) {
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

	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(final String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Process getProcess() {
		return this.process;
	}

	public void setProcess(final Process process) {
		this.process = process;
	}

	public SimpleProcess getSimpleProcess() {
		return this.simpleProcess;
	}

	public void setSimpleProcess(final SimpleProcess simpleProcess) {
		this.simpleProcess = simpleProcess;
	}

	public Set<TaskInstance> getTaskInstances() {
		return this.taskInstances;
	}

	public void setTaskInstances(final Set<TaskInstance> taskInstances) {
		this.taskInstances = taskInstances;
	}

}
