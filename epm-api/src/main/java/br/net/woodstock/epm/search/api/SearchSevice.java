package br.net.woodstock.epm.search.api;

import java.io.Serializable;

import br.net.woodstock.epm.util.Page;

public interface SearchSevice extends Serializable {

	Item get(String id);

	void remove(String id);

	void save(Item item);

	SearchResultContainer search(String filter, OrderBy[] orders, Page page);

}
