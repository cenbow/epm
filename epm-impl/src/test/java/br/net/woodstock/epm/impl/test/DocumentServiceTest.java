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

import br.net.woodstock.epm.acl.api.User;
import br.net.woodstock.epm.document.api.Document;
import br.net.woodstock.epm.document.api.DocumentResultContainer;
import br.net.woodstock.epm.document.api.DocumentService;
import br.net.woodstock.epm.util.Page;
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

			Document item = new Document();

			item.setBinary(text.getBytes());
			item.setCreated(new Date());
			item.setId(Integer.toString(i));
			item.setMimeType("text/plain");
			item.setModified(new Date());
			item.setName("test" + i + ".txt");
			item.setOwner(new User("admin"));
			item.setText(text);

			this.service.save(item);
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
			Document item = new Document();

			item.setBinary(binary);
			item.setCreated(new Date());
			item.setId(file.getName());
			item.setMimeType("text/plain");
			item.setModified(new Date());
			item.setName(file.getName());
			item.setOwner(new User("admin"));

			this.service.save(item);
		}
	}

	@Test
	public void testSearchDoc() throws Exception {
		DocumentResultContainer container = this.service.search("Testando AND NAME:\"curriculum.odt\"", new Page(1, 1000));
		System.out.println("Total : " + container.getTotal());
		System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		Document[] array = container.getItems();
		for (Document item : array) {
			System.out.println(item.getName());
			// this.print(item);
		}
	}

	// @Test
	public void testList() throws Exception {
		for (int i = 1000; i < 2000; i++) {
			Document item = this.service.get(Integer.toString(i));
			this.print(item);
		}
	}

	// @Test
	public void testSearch() throws Exception {
		DocumentResultContainer container = this.service.search("lourival", new Page(1, 20));
		System.out.println("Total : " + container.getTotal());
		System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		Document[] array = container.getItems();
		for (Document item : array) {
			this.print(item);
		}
		while (container.getNextPage() != null) {
			container = this.service.search("lourival", container.getNextPage());
			System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
			array = container.getItems();
			for (Document item : array) {
				this.print(item);
			}
		}
	}

	// @Test
	public void testPagging() throws Exception {
		DocumentResultContainer container = this.service.search("lourival", new Page(1, 20));
		System.out.println("Total : " + container.getTotal());
		System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		while (container.getNextPage() != null) {
			container = this.service.search("lourival", container.getNextPage());
			System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		}
	}

	// @Test
	public void testDel() throws Exception {
		DocumentResultContainer container = this.service.search("lourival", new Page(1, 20));
		Document[] array = container.getItems();
		for (Document item : array) {
			System.out.println("Del: " + item.getId());
			this.service.remove(item.getId());
		}
	}

	private void print(final Document document) {
		if (document != null) {
			System.out.println("\t" + document.getId());
			System.out.println("\t\t" + document.getMimeType());
			System.out.println("\t\t" + document.getCreated());
			System.out.println("\t\t" + document.getModified());
			System.out.println("\t\t" + document.getName());
			System.out.println("\t\t" + document.getOwner());
			System.out.println("\t\t" + document.getText());
		} else {
			System.out.println("Null");
		}
	}

}
