package br.net.woodstock.epm.store.filesystem;

import br.net.woodstock.epm.store.api.StoreSevice;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;

public class FileSystemStoreService implements StoreSevice, Service {

	private static final long		serialVersionUID	= 8196306738398173957L;

	private FileSystemRepository	repository;

	public FileSystemStoreService(final String storePath) {
		super();
		try {
			this.repository = new FileSystemRepository(storePath);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] getData(final String id) {
		try {
			byte[] data = this.repository.get(id);
			return data;
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
