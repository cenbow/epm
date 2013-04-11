package br.net.woodstock.epm.bpmn2.beans;

import java.util.Map;

public class EnumFormItem extends FormItem {

	private static final long	serialVersionUID	= -7011383774973112298L;

	private Map<String, String>	values;

	public EnumFormItem() {
		super();
	}

	public EnumFormItem(final String id, final String name) {
		super(id, name, FormItemType.ENUM);
	}

	public Map<String, String> getValues() {
		return this.values;
	}

	public void setValues(final Map<String, String> values) {
		this.values = values;
	}

}
