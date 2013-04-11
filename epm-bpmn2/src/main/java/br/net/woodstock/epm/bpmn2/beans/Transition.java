package br.net.woodstock.epm.bpmn2.beans;

import java.io.Serializable;

public class Transition implements Serializable {

	private static final long	serialVersionUID	= -917443225102221975L;

	private String				id;

	private String				name;

	private Task				task;

	public Transition() {
		super();
	}

	public Transition(final String id, final String name, final Task task) {
		super();
		this.id = id;
		this.name = name;
		this.task = task;
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

	public Task getTask() {
		return this.task;
	}

	public void setTask(final Task task) {
		this.task = task;
	}

}
