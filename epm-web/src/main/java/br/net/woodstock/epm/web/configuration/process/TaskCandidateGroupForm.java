package br.net.woodstock.epm.web.configuration.process;

import br.net.woodstock.epm.orm.CandidateGroup;
import br.net.woodstock.epm.orm.Department;
import br.net.woodstock.epm.orm.Process;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.Task;
import br.net.woodstock.epm.web.AbstractForm;

public class TaskCandidateGroupForm extends AbstractForm {

	private static final long	serialVersionUID	= 7435757525851023718L;

	private Integer				id;

	private String				name;

	private Process				process;

	private Task				task;

	private CandidateGroup		candidateGroup;

	private Role				role;

	private Department			department;

	public TaskCandidateGroupForm() {
		super();
	}

	@Override
	public void reset() {
		this.setCandidateGroup(null);
		this.setDepartment(null);
		this.setId(null);
		this.setName(null);
		this.setProcess(null);
		this.setRole(null);
		this.setTask(null);
	}

	public Integer getId() {
		return this.id;
	}

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

	public Task getTask() {
		return this.task;
	}

	public void setTask(final Task task) {
		this.task = task;
	}

	public CandidateGroup getCandidateGroup() {
		return this.candidateGroup;
	}

	public void setCandidateGroup(final CandidateGroup candidateGroup) {
		this.candidateGroup = candidateGroup;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(final Role role) {
		this.role = role;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(final Department department) {
		this.department = department;
	}

}
