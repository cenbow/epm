package br.net.woodstock.epm.process.api;

import java.io.Serializable;
import java.util.Date;

import br.net.woodstock.epm.acl.api.User;

public class Activity implements Serializable {

	private static final long	serialVersionUID	= 6794271803811922687L;

	private String				id;

	private String				name;

	private String				type;

	private Date				start;

	private Date				end;

	private User				user;

	public Activity() {
		super();
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

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
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

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return this.getId();
	}

}
