package br.net.woodstock.epm.document.api;

import br.net.woodstock.epm.orm.Document;
import br.net.woodstock.rockframework.domain.Service;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

public interface DocumentService extends Service {

	Document getDocumentById(Integer id);

	byte[] getContentById(Integer id);

	void saveDocument(Document document, byte[] content);

	void updateDocument(Document document, byte[] content);

	ORMResult listDocumentsByName(String name, Page page);

}
