package br.net.woodstock.epm.document.api;

import br.net.woodstock.epm.util.Page;
import br.net.woodstock.epm.util.ResultContainer;

public class DocumentResultContainer extends ResultContainer<Document> {

	private static final long	serialVersionUID	= -156166129488368838L;

	public DocumentResultContainer(final int total, final Document[] items, final Page currentPage) {
		super(total, items, currentPage);
	}

}
