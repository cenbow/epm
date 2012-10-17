package br.net.woodstock.epm.search.api;

import br.net.woodstock.epm.util.Page;
import br.net.woodstock.epm.util.ResultContainer;

public class SearchResultContainer extends ResultContainer<Item> {

	private static final long	serialVersionUID	= -6974465503700636964L;

	public SearchResultContainer(final int total, final Item[] items, final Page currentPage) {
		super(total, items, currentPage);
	}

}
