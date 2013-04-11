package br.net.woodstock.epm.bpmn2.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public abstract class Task implements Serializable {

	private static final long		serialVersionUID	= -5308471831140510426L;

	private String					id;

	private String					name;

	private Collection<Transition>	transitions;

	public Task() {
		this(null, null);
	}

	public Task(final String id, final String name) {
		super();
		this.id = id;
		this.name = name;
		this.transitions = new HashSet<Transition>();
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

	public Collection<Transition> getTransitions() {
		return this.transitions;
	}

	public void setTransitions(final Collection<Transition> transitions) {
		this.transitions = transitions;
	}

}
