package br.net.woodstock.epm.process.api;

import br.net.woodstock.epm.orm.BusinessProcess;
import br.net.woodstock.rockframework.domain.Service;

public interface BusinessProcessService extends Service {

	BusinessProcess getBusinessProcessById(Integer id);

	BusinessProcess getBusinessProcessByName(String name);

	byte[] getBusinessProcessImage(Integer id);

	void save(BusinessProcess businessProcess);

}