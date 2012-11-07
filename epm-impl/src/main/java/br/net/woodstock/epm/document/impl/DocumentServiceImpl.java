package br.net.woodstock.epm.document.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.epm.document.api.ContentRepository;
import br.net.woodstock.epm.document.api.DocumentService;
import br.net.woodstock.epm.document.util.DocumentContent;
import br.net.woodstock.epm.orm.Document;
import br.net.woodstock.epm.repository.util.RepositoryHelper;
import br.net.woodstock.rockframework.domain.service.ServiceException;
import br.net.woodstock.rockframework.persistence.orm.GenericRepository;
import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryMetadata;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;
import br.net.woodstock.rockframework.persistence.orm.QueryableRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

	private static final long	serialVersionUID			= 7929971241857912337L;

	// USER
	private static final String	JPQL_LIST_DOCUMENT_BY_NAME	= "SELECT d FROM Document AS d WHERE to_ascii(lower(d.name)) LIKE to_ascii(lower(:name)) ORDER BY d.name";

	private static final String	JPQL_COUNT_DOCUMENT_BY_NAME	= "SELECT COUNT(*) FROM Document AS d WHERE to_ascii(lower(d.name)) LIKE to_ascii(lower(:name))";

	@Autowired(required = true)
	private GenericRepository	genericRepository;

	@Autowired(required = true)
	private QueryableRepository	queryableRepository;

	@Autowired(required = true)
	private ContentRepository	contentRepository;

	public DocumentServiceImpl() {
		super();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Document getDocumentById(final Integer id) {
		try {
			return this.genericRepository.get(new Document(id));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] getContentById(final Integer id) {
		try {
			return this.contentRepository.getContentById(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveDocument(final Document document, final byte[] bytes) {
		try {
			DocumentContent documentContent = DocumentContent.getInstance(bytes);
			document.setMimeType(documentContent.getMimeType());
			document.setText(documentContent.getText());
			this.genericRepository.save(document);
			this.contentRepository.saveContent(document.getId(), bytes);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateDocument(final Document document, final byte[] bytes) {
		try {
			Document d = this.genericRepository.get(document);
			d.setMimeType(document.getMimeType());
			d.setModified(new Date());
			d.setName(document.getName());

			if (bytes != null) {
				DocumentContent documentContent = DocumentContent.getInstance(bytes);
				d.setMimeType(documentContent.getMimeType());
				d.setText(documentContent.getText());
				this.contentRepository.updateContent(document.getId(), bytes);
			}

			this.genericRepository.update(d);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public QueryResult listDocumentsByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", RepositoryHelper.getLikeValue(name, false));

			QueryMetadata metadata = RepositoryHelper.toQueryMetadata(DocumentServiceImpl.JPQL_LIST_DOCUMENT_BY_NAME, DocumentServiceImpl.JPQL_COUNT_DOCUMENT_BY_NAME, page, parameters);
			return this.queryableRepository.getCollection(metadata);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
