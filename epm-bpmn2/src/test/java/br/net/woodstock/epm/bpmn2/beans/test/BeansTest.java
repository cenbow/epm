package br.net.woodstock.epm.bpmn2.beans.test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import br.net.woodstock.epm.bpmn2.beans.Definitions;

public class BeansTest {

	public BeansTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		Definitions definitions = new Definitions("http://woodstock.net.br/name1");
		this.printXml(definitions);
	}

	private void printXml(final Object obj) throws Exception {
		JAXBContext context = JAXBContext.newInstance("br.net.woodstock.epm.bpmn2.beans");
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(obj, System.out);
	}

}
