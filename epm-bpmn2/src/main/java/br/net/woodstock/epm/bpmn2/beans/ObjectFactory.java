package br.net.woodstock.epm.bpmn2.beans;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

	public ObjectFactory() {
		super();
	}

	public Definitions createDefinitions() {
		return new Definitions();
	}

}
