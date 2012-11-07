package br.net.woodstock.epm.client.util;

import java.io.Serializable;

public abstract class ResultContainer<T> implements Serializable {

	private static final long	serialVersionUID	= -8787388401326384026L;

	private int					total;

	private T[]					items;

	private Page				currentPage;

	public ResultContainer(final int total, final T[] items, final Page currentPage) {
		super();
		this.total = total;
		this.items = items;
		this.currentPage = currentPage;
	}

	public int getTotal() {
		return this.total;
	}

	public T[] getItems() {
		return this.items;
	}

	public Page getCurrentPage() {
		return this.currentPage;
	}

	// Aux
	public Page getFirstPage() {
		if (this.total > 0) {
			if (this.currentPage.getPageNumber() == 1) {
				return this.currentPage;
			}
			return new Page(1, this.getCurrentPage().getPageSize());
		}
		return null;
	}

	public Page getPreviousPage() {
		if (this.total > 0) {
			if (this.currentPage.getPageNumber() > 1) {
				return new Page(this.currentPage.getPageNumber() - 1, this.getCurrentPage().getPageSize());
			}
		}
		return null;
	}

	public Page getNextPage() {
		if (this.total > 0) {
			int nextResult = (this.currentPage.getPageNumber() * this.currentPage.getPageSize()) + 1;
			if (nextResult < this.total) {
				return new Page(this.currentPage.getPageNumber() + 1, this.getCurrentPage().getPageSize());
			}
		}
		return null;
	}

	public Page getLastPage() {
		if (this.total > 0) {
			int lastPage = this.total / this.currentPage.getPageSize();
			int mod = this.total % this.currentPage.getPageSize();

			if (mod > 0) {
				lastPage++;
			}

			if (this.currentPage.getPageNumber() == lastPage) {
				return this.currentPage;
			}

			return new Page(lastPage, this.getCurrentPage().getPageSize());
		}
		return null;
	}

}
