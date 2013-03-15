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
import br.net.woodstock.epm.repository.util.ORMRepositoryHelper;
import br.net.woodstock.rockframework.domain.ServiceException;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMFilter;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMRepository;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

@Service
public class DocumentServiceImpl implements DocumentService {

	private static final long	serialVersionUID			= 7929971241857912337L;

	// USER
	private static final String	JPQL_LIST_DOCUMENT_BY_NAME	= "SELECT d FROM Document AS d WHERE d.name LIKE :name ORDER BY d.name";

	private static final String	JPQL_COUNT_DOCUMENT_BY_NAME	= "SELECT COUNT(*) FROM Document AS d WHERE d.name LIKE :name";

	@Autowired(required = true)
	private ORMRepository		repository;

	@Autowired(required = true)
	private ContentRepository	contentRepository;

	public DocumentServiceImpl() {
		super();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Document getDocumentById(final Integer id) {
		try {
			return this.repository.get(Document.class, id);
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
			this.repository.save(document);
			this.contentRepository.saveContent(document.getId(), bytes);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateDocument(final Document document, final byte[] bytes) {
		try {
			Document d = this.repository.get(Document.class, document.getId());
			d.setMimeType(document.getMimeType());
			d.setModified(new Date());
			d.setName(document.getName());

			if (bytes != null) {
				DocumentContent documentContent = DocumentContent.getInstance(bytes);
				d.setMimeType(documentContent.getMimeType());
				d.setText(documentContent.getText());
				this.contentRepository.updateContent(document.getId(), bytes);
			}

			this.repository.update(d);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ORMResult listDocumentsByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", ORMRepositoryHelper.getLikeValue(name, false));

			ORMFilter filter = ORMRepositoryHelper.toORMFilter(DocumentServiceImpl.JPQL_LIST_DOCUMENT_BY_NAME, DocumentServiceImpl.JPQL_COUNT_DOCUMENT_BY_NAME, page, parameters);
			return this.repository.getCollection(filter);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
