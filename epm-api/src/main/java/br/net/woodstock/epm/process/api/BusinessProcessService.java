package br.net.woodstock.epm.process.api;

import br.net.woodstock.epm.orm.BusinessProcess;
import br.net.woodstock.rockframework.domain.Service;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

public interface BusinessProcessService extends Service {

	BusinessProcess getBusinessProcessById(Integer id);

	BusinessProcess getBusinessProcessByName(String name);

	byte[] getBusinessProcessImage(Integer id);

	void save(BusinessProcess businessProcess);

	ORMResult listBusinessProcessByName(String name, Page page);

}