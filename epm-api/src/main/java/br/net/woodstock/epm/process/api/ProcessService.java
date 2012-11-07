package br.net.woodstock.epm.process.api;

import br.net.woodstock.rockframework.domain.service.Service;

public interface ProcessService extends Service {

	// Deploy
	String deploy(String processName, byte[] data, DeploymentType type);

}