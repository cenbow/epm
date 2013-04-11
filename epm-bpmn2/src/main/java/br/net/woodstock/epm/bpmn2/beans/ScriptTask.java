package br.net.woodstock.epm.bpmn2.beans;

public class ScriptTask extends Task {

	private static final long	serialVersionUID	= -684874150284854010L;

	private String				script;

	public ScriptTask() {
		super();
	}

	public ScriptTask(final String id, final String name, final String script) {
		super(id, name);
		this.script = script;
	}

	public String getScript() {
		return this.script;
	}

	public void setScript(final String script) {
		this.script = script;
	}

}
