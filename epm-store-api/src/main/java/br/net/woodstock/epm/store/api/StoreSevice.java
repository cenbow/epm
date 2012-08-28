package br.net.woodstock.epm.store.api;

import java.io.Serializable;

public interface StoreSevice extends Serializable {

	byte[] get(String id);

	boolean remove(String id);

	void save(String id, byte[] data);

}
