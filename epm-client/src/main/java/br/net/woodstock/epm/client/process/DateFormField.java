package br.net.woodstock.epm.client.process;

public class DateFormField extends FormField {

	private static final long	serialVersionUID	= -360522575046044183L;

	private String				pattern;

	public DateFormField() {
		super();
	}

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

}
