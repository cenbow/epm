package br.net.woodstock.epm.process.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ProcessInstance implements Serializable {

	private static final long		serialVersionUID	= 8874015973197883393L;

	private String					id;

	private String					key;

	private boolean					suspended;

	private boolean					finished;

	private Date					start;

	private Date					end;

	private ProcessDefinition					process;

	private Collection<Activity>	current;

	private Collection<Activity>	history;

	public ProcessInstance() {
		super();
		this.current = new ArrayList<Activity>();
		this.history = new ArrayList<Activity>();
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public boolean isSuspended() {
		return this.suspended;
	}

	public void setSuspended(final boolean suspended) {
		this.suspended = suspended;
	}

	public boolean isFinished() {
		return this.finished;
	}

	public void setFinished(final boolean finished) {
		this.finished = finished;
	}

	public Date getStart() {
		return this.start;
	}

	public void setStart(final Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return this.end;
	}

	public void setEnd(final Date end) {
		this.end = end;
	}

	public ProcessDefinition getProcess() {
		return this.process;
	}

	public void setProcess(final ProcessDefinition process) {
		this.process = process;
	}

	public Collection<Activity> getCurrent() {
		return this.current;
	}

	public void setCurrent(final Collection<Activity> current) {
		this.current = current;
	}

	public Collection<Activity> getHistory() {
		return this.history;
	}

	public void setHistory(final Collection<Activity> history) {
		this.history = history;
	}

	@Override
	public String toString() {
		return this.getId();
	}

}
