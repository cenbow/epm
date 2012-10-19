package br.net.woodstock.epm.document.jackrabbit;

import br.net.woodstock.epm.document.api.Document;
import br.net.woodstock.epm.document.api.DocumentResultContainer;
import br.net.woodstock.epm.document.api.DocumentService;
import br.net.woodstock.epm.util.Page;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;

public class JackRabbitDocumentService implements DocumentService, Service {

	private static final long		serialVersionUID	= 8196306738398173957L;

	private JackRabbitRepository	repository;

	public JackRabbitDocumentService(final String jndiName, final String home) {
		super();
		try {
			this.repository = new JackRabbitRepository(jndiName, home);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Document get(final String id) {
		try {
			Document d = this.repository.get(id);
			return d;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean remove(final String id) {
		try {
			boolean b = this.repository.remove(id);
			return b;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void save(final Document document) {
		try {
			this.repository.save(document);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public DocumentResultContainer search(final String filter, final Page page) {
		try {
			DocumentResultContainer c = this.repository.search(filter, page);
			return c;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean update(final Document document) {
		try {
			boolean b = this.repository.update(document);
			return b;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
