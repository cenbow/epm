package br.net.woodstock.epm.bpmn2.beans;

public class BooleanFormItem extends FormItem {

	private static final long	serialVersionUID	= -6215121229053136698L;

	public BooleanFormItem() {
		super();
	}

	public BooleanFormItem(final String id, final String name) {
		super(id, name, FormItemType.BOOLEAN);
	}

}
