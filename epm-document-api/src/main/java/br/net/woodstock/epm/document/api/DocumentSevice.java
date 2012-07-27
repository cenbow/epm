package br.net.woodstock.epm.document.api;

import java.io.Serializable;

public interface DocumentSevice extends Serializable {

	DocumentMetadata getMetadata(String id);

	byte[] getData(String id);

	void save(DocumentMetadata document, byte[] data);

	DocumentMetadata[] search(String filter, OrderBy[] orders, int maxResult);

}
