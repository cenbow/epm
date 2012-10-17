package br.net.woodstock.epm.search.api;

import java.io.Serializable;

public class OrderBy implements Serializable {

	private static final long	serialVersionUID	= 4276692192925162321L;

	private Field				field;

	private boolean				reverse;

	public OrderBy(final Field field) {
		super();
		this.field = field;
	}

	public OrderBy(final Field field, final boolean reverse) {
		super();
		this.field = field;
		this.reverse = reverse;
	}

	public Field getField() {
		return this.field;
	}

	public boolean isReverse() {
		return this.reverse;
	}
}
