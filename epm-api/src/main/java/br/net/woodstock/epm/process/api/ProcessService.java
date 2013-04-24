package br.net.woodstock.epm.process.api;

import br.net.woodstock.epm.orm.Process;
import br.net.woodstock.rockframework.domain.Service;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

public interface ProcessService extends Service {

	// Crud
	Process getBusinessProcessById(Integer id);

	Process getBusinessProcessByName(String name);

	byte[] getBusinessProcessImage(Integer id);

	void save(Process businessProcess);

	ORMResult listBusinessProcessByName(String name, Page page);

	// BPM
	Integer createSimpleProcess(String number, Integer businessProcessId);

}