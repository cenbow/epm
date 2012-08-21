package br.net.woodstock.epm.search.api;

import java.io.Serializable;

public interface SearchSevice extends Serializable {

	DocumentMetadata getMetadata(String id);

	void save(DocumentMetadata document);

	DocumentMetadata[] search(String filter, OrderBy[] orders, int maxResult);

}
