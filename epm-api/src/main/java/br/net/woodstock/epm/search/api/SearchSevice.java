package br.net.woodstock.epm.search.api;

import java.io.Serializable;

public interface SearchSevice extends Serializable {

	Item get(String id);

	void remove(String id);

	void save(Item item);

	Item[] search(String filter, OrderBy[] orders, int maxResult);

}
