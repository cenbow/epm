package br.net.woodstock.epm.search.lucene;

import br.net.woodstock.epm.search.api.DocumentMetadata;
import br.net.woodstock.epm.search.api.OrderBy;
import br.net.woodstock.epm.search.api.SearchSevice;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;

public class LuceneSearchService implements SearchSevice, Service {

	private static final long	serialVersionUID	= 8196306738398173957L;

	private LuceneRepository		repository;

	public LuceneSearchService(final String indexPath) {
		super();
		try {
			this.repository = new LuceneRepository(indexPath);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public DocumentMetadata getMetadata(final String id) {
		try {
			DocumentMetadata document = this.repository.get(id);
			return document;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void save(final DocumentMetadata document) {
		try {
			this.repository.save(document);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public DocumentMetadata[] search(final String filter, final OrderBy[] orders, final int maxResult) {
		try {
			DocumentMetadata[] array = this.repository.search(filter, orders, maxResult);
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
