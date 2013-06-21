package br.net.woodstock.epm.process.api;

import br.net.woodstock.epm.orm.Process;
import br.net.woodstock.rockframework.domain.Service;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

public interface ProcessService extends Service {

	// Crud
	Process getProcessById(Integer id);

	Process getProcessByName(String name);

	byte[] getProcessImageById(Integer id);

	void save(Process process);

	ORMResult listBusinessProcessByName(String name, Page page);

	// BPM
	Integer createSimpleProcess(String number, Integer processId);

}