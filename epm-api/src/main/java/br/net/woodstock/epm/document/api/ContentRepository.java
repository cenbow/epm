package br.net.woodstock.epm.document.api;

import br.net.woodstock.rockframework.domain.persistence.Repository;

public interface ContentRepository extends Repository {

	byte[] getContentById(Integer id);

	void saveContent(Integer id, byte[] content);

	void updateContent(Integer id, byte[] content);

}
