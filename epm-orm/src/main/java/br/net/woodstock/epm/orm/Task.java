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
@Table(name = "epm_task")
public class Task extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= 6261267274337111940L;

	@Id
	@Column(name = "task_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "task_name", length = 200, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 200)
	private String				name;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "process_id", referencedColumnName = "process_id", nullable = false)
	@NotNull
	private Process				process;

	@ManyToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<CandidateGroup>	groups;

	@ManyToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<TaskInstance>	taskInstances;

	public Task() {
		super();
	}

	public Task(final Integer id) {
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

	public Process getProcess() {
		return this.process;
	}

	public void setProcess(final Process process) {
		this.process = process;
	}

	public Set<CandidateGroup> getGroups() {
		return this.groups;
	}

	public void setGroups(final Set<CandidateGroup> groups) {
		this.groups = groups;
	}

	public Set<TaskInstance> getTaskInstances() {
		return this.taskInstances;
	}

	public void setTaskInstances(final Set<TaskInstance> taskInstances) {
		this.taskInstances = taskInstances;
	}

}
