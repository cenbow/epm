package br.net.woodstock.epm.store.api;

import java.io.Serializable;

public interface StoreSevice extends Serializable {

	byte[] getData(String id);

	void save(String id, byte[] data);

}
