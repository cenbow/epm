package br.net.woodstock.epm.document.api;

import br.net.woodstock.epm.orm.Document;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;

public interface DocumentService extends Service {

	Document getDocumentById(Integer id);

	byte[] getContentById(Integer id);

	void saveDocument(Document document, byte[] content);

	void updateDocument(Document document, byte[] content);

	QueryResult listDocumentsByName(String name, Page page);

}
