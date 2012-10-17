package br.net.woodstock.epm.store.jackrabbit;

import br.net.woodstock.epm.store.api.StoreSevice;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;

public class JackRabbitStoreService implements StoreSevice, Service {

	private static final long		serialVersionUID	= 8196306738398173957L;

	private JackRabbitRepository	repository;

	public JackRabbitStoreService(final String storePath) {
		super();
		try {
			this.repository = new JackRabbitRepository(storePath);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] get(final String id) {
		try {
			byte[] data = this.repository.get(id);
			return data;
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
	public void save(final String id, final byte[] data) {
		try {
			this.repository.store(id, data);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
