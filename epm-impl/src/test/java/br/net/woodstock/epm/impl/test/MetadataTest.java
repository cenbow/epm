package br.net.woodstock.epm.impl.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import br.net.woodstock.epm.document.util.DocumentContent;
import br.net.woodstock.rockframework.core.utils.IO;
import br.net.woodstock.rockframework.core.utils.Strings;

public class MetadataTest {

	public MetadataTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("/home/lourival/Documents/curriculum.odt");
		list.add("/home/lourival/Documents/curriculum.pdf");
		list.add("/home/lourival/Documents/registro_de_ponto_posto_de_trabalho_vs1_0.doc");
		list.add("/home/lourival/Documents/ws-sedesc1.xls");
		list.add("/home/lourival/Documents/teste.rtf");
		list.add("/home/lourival/Documents/pje-cnj-1.png");
		list.add("/home/lourival/Documents/Acesso-soapui-project.xml");
		for (String l : list) {
			System.out.println(Strings.repeat("=", 100));
			System.out.println("File: " + l);
			File file = new File(l);
			byte[] bytes = IO.toByteArray(file);
			DocumentContent content = DocumentContent.getInstance(bytes);
			for (Entry<String, String> entry : content.getMetadata().entrySet()) {
				System.out.println(entry.getKey() + " => " + entry.getValue());
			}
			System.out.println("Mime: " + content.getMimeType());
			System.out.println(content.getText());
			System.out.println(Strings.repeat("=", 100));
		}
	}

}
