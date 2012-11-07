package br.net.woodstock.epm.impl.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.document.api.DocumentService;
import br.net.woodstock.epm.orm.Document;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class DocumentServiceTest {

	@Autowired(required = true)
	private DocumentService	service;

	public DocumentServiceTest() {
		super();
	}

	// @Test
	public void testSave() throws Exception {
		for (int i = 1000; i < 2000; i++) {
			System.out.println("Salvando " + i);
			String text = "Lourival Sabino " + i;

			Document document = new Document();

			document.setCreated(new Date());
			document.setMimeType("text/plain");
			document.setModified(new Date());
			document.setName("test" + i + ".txt");
			document.setUser(new User(Integer.valueOf(1)));
			document.setText(text);

			this.service.saveDocument(document, text.getBytes());
		}
	}

	// @Test
	public void testSaveDoc() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("/home/lourival/Documents/curriculum.odt");
		list.add("/home/lourival/Documents/curriculum.pdf");
		list.add("/home/lourival/Documents/registro_de_ponto_posto_de_trabalho_vs1_0.doc");
		list.add("/home/lourival/Documents/ws-sedesc1.xls");
		list.add("/home/lourival/Documents/teste.rtf");
		list.add("/home/lourival/Documents/pje-cnj-1.png");
		list.add("/home/lourival/Documents/Acesso-soapui-project.xml");
		for (String l : list) {
			File file = new File(l);
			byte[] binary = IOUtils.toByteArray(file);
			Document document = new Document();

			document.setCreated(new Date());
			document.setMimeType("text/plain");
			document.setModified(new Date());
			document.setName(file.getName());
			document.setUser(new User(Integer.valueOf(1)));

			this.service.saveDocument(document, binary);
		}
	}

	@Test
	public void testSearchDoc() throws Exception {
		// DocumentResultContainer container = this.service.search("Testando AND NAME:\"curriculum.odt\"", new
		// Page(1, 1000));
		// System.out.println("Total : " + container.getTotal());
		// System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		// Document[] array = container.getItems();
		// for (Document item : array) {
		// System.out.println(item.getName());
		// this.print(item);
		// }
	}

	// @Test
	public void testList() throws Exception {
		for (int i = 1000; i < 2000; i++) {
			Document item = this.service.getDocumentById(Integer.valueOf(i));
			this.print(item);
		}
	}

	private void print(final Document document) {
		if (document != null) {
			System.out.println("\t" + document.getId());
			System.out.println("\t\t" + document.getMimeType());
			System.out.println("\t\t" + document.getCreated());
			System.out.println("\t\t" + document.getModified());
			System.out.println("\t\t" + document.getName());
			System.out.println("\t\t" + document.getUser().getName());
			System.out.println("\t\t" + document.getText());
		} else {
			System.out.println("Null");
		}
	}

}
