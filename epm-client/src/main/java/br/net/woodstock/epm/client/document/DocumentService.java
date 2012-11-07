package br.net.woodstock.epm.client.document;

import java.io.Serializable;

import br.net.woodstock.epm.client.util.Page;

public interface DocumentService extends Serializable {

	Document get(String id);

	boolean remove(String id);

	void save(Document document);

	boolean update(Document document);

	DocumentResultContainer search(String filter, Page page);

}
