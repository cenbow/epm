package br.net.woodstock.epm.bpmn2.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class Process implements Serializable {

	private static final long	serialVersionUID	= 5609188602985011805L;

	private String				id;

	private String				name;

	private Collection<Task>	tasks;

	public Process() {
		this(null, null);
	}

	public Process(final String id, final String name) {
		super();
		this.id = id;
		this.name = name;
		this.tasks = new HashSet<Task>();

	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Collection<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(final Collection<Task> tasks) {
		this.tasks = tasks;
	}

}
