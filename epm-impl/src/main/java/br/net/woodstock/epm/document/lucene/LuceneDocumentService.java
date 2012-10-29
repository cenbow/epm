package br.net.woodstock.epm.document.lucene;

import java.io.File;

import br.net.woodstock.epm.document.api.Document;
import br.net.woodstock.epm.document.api.DocumentResultContainer;
import br.net.woodstock.epm.document.api.DocumentService;
import br.net.woodstock.epm.util.EPMLog;
import br.net.woodstock.epm.util.Page;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.domain.service.ServiceException;

public class LuceneDocumentService implements DocumentService, Service {

	private static final long		serialVersionUID	= 8196306738398173957L;

	private static final String		LUCENE_DIR			= "lucene";

	private static final String		FILE_DIR			= "files";

	private LuceneRepository		luceneRepository;

	private FileSystemRepository	fileSystemRepository;

	public LuceneDocumentService(final String home) {
		super();
		try {
			this.luceneRepository = new LuceneRepository(home + File.separator + LuceneDocumentService.LUCENE_DIR);
			this.fileSystemRepository = new FileSystemRepository(home + File.separator + LuceneDocumentService.FILE_DIR);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void close() {
		try {
			EPMLog.getLogger().info("Closing repository...");
			this.luceneRepository.close();
			EPMLog.getLogger().info("Closing repository OK");
		} catch (Exception e) {
			EPMLog.getLogger().error("Closing repository Error: " + e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	public Document get(final String id) {
		try {
			Document d = this.luceneRepository.get(id);
			if (d != null) {
				d.setBinary(this.fileSystemRepository.get(id));
			}
			return d;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean remove(final String id) {
		try {
			if (this.luceneRepository.remove(id)) {
				return this.fileSystemRepository.remove(id);
			}
			return false;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void save(final Document document) {
		try {
			this.luceneRepository.save(document);
			this.fileSystemRepository.save(document.getId(), document.getBinary());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean update(final Document document) {
		try {
			if (this.luceneRepository.update(document)) {
				this.fileSystemRepository.update(document.getId(), document.getBinary());
			}
			return false;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public DocumentResultContainer search(final String filter, final Page page) {
		try {
			DocumentResultContainer container = this.luceneRepository.search(filter, page);
			return container;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
