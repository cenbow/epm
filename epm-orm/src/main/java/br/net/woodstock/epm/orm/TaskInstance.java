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
@Table(name = "epm_task_instance")
public class TaskInstance extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= -6971991870020680931L;

	@Id
	@Column(name = "task_instance_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "task_instance_ref", length = 100, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 100)
	private String				taskInstanceId;

	@Column(name = "task_instance_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean				active;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "task_id", referencedColumnName = "task_id", nullable = false)
	@NotNull
	private Task				task;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "process_instance_id", referencedColumnName = "process_instance_id", nullable = false)
	@NotNull
	private ProcessInstance		processInstance;

	@ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true)
	@NotNull
	private User				user;

	public TaskInstance() {
		super();
	}

	public TaskInstance(final Integer id) {
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

	public String getTaskInstanceId() {
		return this.taskInstanceId;
	}

	public void setTaskInstanceId(final String taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(final Task task) {
		this.task = task;
	}

	public ProcessInstance getProcessInstance() {
		return this.processInstance;
	}

	public void setProcessInstance(final ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

}
