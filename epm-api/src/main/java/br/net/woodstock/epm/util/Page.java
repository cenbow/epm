package br.net.woodstock.epm.util;

import java.io.Serializable;

public class Page implements Serializable {

	private static final long	serialVersionUID	= -8145145560754292846L;

	private int					pageNumber;

	private int					pageSize;

	public Page() {
		super();
	}

	public Page(final int pageNumber, final int pageSize) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public void setPageNumber(final int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

}
