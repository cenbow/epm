package br.net.woodstock.epm.bpmn2.beans;

public class DateFormItem extends FormItem {

	private static final long	serialVersionUID	= -7011383774973112298L;

	private String				pattern;

	public DateFormItem() {
		super();
	}

	public DateFormItem(final String id, final String name) {
		super(id, name, FormItemType.DATE);
	}

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

}
