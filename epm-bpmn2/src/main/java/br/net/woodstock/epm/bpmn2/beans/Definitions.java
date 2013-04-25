package br.net.woodstock.epm.bpmn2.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "definitions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Definitions implements Serializable {

	private static final long	serialVersionUID	= 4930808981287709828L;

	@XmlAttribute
	private String				targetNamespace;

	@XmlAttribute
	private String				typeLanguage		= "http://www.w3.org/2001/XMLSchema";

	@XmlAttribute
	private String				expressionLanguage	= "http://www.w3.org/1999/XPath";

	private Process				process;

	public Definitions() {
		super();
	}

	public Definitions(final String targetNamespace) {
		super();
		this.targetNamespace = targetNamespace;

	}

	public String getTargetNamespace() {
		return this.targetNamespace;
	}

	public void setTargetNamespace(final String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public String getTypeLanguage() {
		return this.typeLanguage;
	}

	public void setTypeLanguage(final String typeLanguage) {
		this.typeLanguage = typeLanguage;
	}

	public String getExpressionLanguage() {
		return this.expressionLanguage;
	}

	public void setExpressionLanguage(final String expressionLanguage) {
		this.expressionLanguage = expressionLanguage;
	}

	public Process getProcess() {
		return this.process;
	}

	public void setProcess(final Process process) {
		this.process = process;
	}
}
