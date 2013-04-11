package br.net.woodstock.epm.bpmn2.beans;

public class StringFormItem extends FormItem {

	private static final long	serialVersionUID	= -4190664791689378661L;

	public StringFormItem() {
		super();
	}

	public StringFormItem(final String id, final String name) {
		super(id, name, FormItemType.STRING);
	}

}
