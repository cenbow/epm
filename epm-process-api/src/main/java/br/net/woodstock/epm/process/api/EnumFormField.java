package br.net.woodstock.epm.process.api;

import java.util.Map;

public class EnumFormField extends FormField {

	private static final long	serialVersionUID	= -7669207693091577448L;

	private Map<String, String>	values;

	public EnumFormField() {
		super();
	}

	public Map<String, String> getValues() {
		return this.values;
	}

	public void setValues(final Map<String, String> values) {
		this.values = values;
	}

}
