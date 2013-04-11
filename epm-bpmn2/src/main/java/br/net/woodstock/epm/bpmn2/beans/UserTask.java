package br.net.woodstock.epm.bpmn2.beans;

public class UserTask extends Task {

	private static final long	serialVersionUID	= -684874150284854010L;

	private String				formName;

	public UserTask() {
		super();
	}

	public UserTask(final String id, final String name, final String formName) {
		super(id, name);
		this.formName = formName;
	}

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(final String formName) {
		this.formName = formName;
	}

}
