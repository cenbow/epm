package br.net.woodstock.epm.document.lucene;

import br.net.woodstock.epm.document.api.DocumentMetadata;
import br.net.woodstock.epm.document.api.DocumentSevice;
import br.net.woodstock.epm.document.api.OrderBy;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;

public class DocumentServiceImpl implements DocumentSevice, Service {

	private static final long	serialVersionUID	= 8196306738398173957L;

	private StoreService		storeService;

	private LuceneService		luceneService;

	public DocumentServiceImpl(final String storePath, final String indexPath) {
		super();
		try {
			this.storeService = new StoreService(storePath);
			this.luceneService = new LuceneService(indexPath);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public DocumentMetadata getMetadata(final String id) {
		try {
			DocumentMetadata document = this.luceneService.get(id);
			return document;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] getData(final String id) {
		try {
			byte[] data = this.storeService.get(id);
			return data;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void save(final DocumentMetadata document, final byte[] data) {
		try {
			this.storeService.store(document.getId(), data);
			this.luceneService.save(document);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public DocumentMetadata[] search(final String filter, final OrderBy[] orders, final int maxResult) {
		try {
			DocumentMetadata[] array = this.luceneService.search(filter, orders, maxResult);
			return array;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
