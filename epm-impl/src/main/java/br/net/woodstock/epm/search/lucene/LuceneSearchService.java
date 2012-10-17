package br.net.woodstock.epm.search.lucene;

import br.net.woodstock.epm.search.api.Item;
import br.net.woodstock.epm.search.api.OrderBy;
import br.net.woodstock.epm.search.api.SearchResultContainer;
import br.net.woodstock.epm.search.api.SearchSevice;
import br.net.woodstock.epm.util.Page;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;

public class LuceneSearchService implements SearchSevice, Service {

	private static final long	serialVersionUID	= 8196306738398173957L;

	private LuceneRepository	repository;

	public LuceneSearchService(final String indexPath) {
		super();
		try {
			this.repository = new LuceneRepository(indexPath);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Item get(final String id) {
		try {
			Item item = this.repository.get(id);
			return item;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void remove(final String id) {
		try {
			this.repository.remove(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void save(final Item item) {
		try {
			this.repository.save(item);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public SearchResultContainer search(final String filter, final OrderBy[] orders, final Page page) {
		try {
			SearchResultContainer container = this.repository.search(filter, orders, page);
			return container;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
