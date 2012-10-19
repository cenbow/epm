package br.net.woodstock.epm.impl.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.acl.api.User;
import br.net.woodstock.epm.document.api.Document;
import br.net.woodstock.epm.document.api.DocumentResultContainer;
import br.net.woodstock.epm.document.api.DocumentService;
import br.net.woodstock.epm.document.lucene.LuceneDocumentService;
import br.net.woodstock.epm.util.Page;

@RunWith(BlockJUnit4ClassRunner.class)
public class LuceneServiceTest {

	private static final String	PATH_LUCENE	= "/home/lourival/tmp/lucene";

	public LuceneServiceTest() {
		super();
	}

	// @Test
	public void testSave() throws Exception {
		LuceneDocumentService service = new LuceneDocumentService(LuceneServiceTest.PATH_LUCENE);
		Collection<String> ids = new ArrayList<String>();
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

			service.save(item);
			ids.add(item.getId());
		}

		// service.close();
	}

	// @Test
	public void testList() throws Exception {
		DocumentService service = new LuceneDocumentService(LuceneServiceTest.PATH_LUCENE);
		for (int i = 1000; i < 2000; i++) {
			Document item = service.get(Integer.toString(i));
			this.print(item);
		}
	}

	@Test
	public void testSearch() throws Exception {
		DocumentService service = new LuceneDocumentService(LuceneServiceTest.PATH_LUCENE);
		DocumentResultContainer container = service.search("lourival", new Page(1, 20));
		System.out.println("Total : " + container.getTotal());
		System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		Document[] array = container.getItems();
		for (Document item : array) {
			this.print(item);
		}
		while (container.getNextPage() != null) {
			container = service.search("lourival", container.getNextPage());
			System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
			array = container.getItems();
			for (Document item : array) {
				this.print(item);
			}
		}
	}

	// @Test
	public void testPagging() throws Exception {
		DocumentService service = new LuceneDocumentService(LuceneServiceTest.PATH_LUCENE);
		DocumentResultContainer container = service.search("lourival", new Page(1, 20));
		System.out.println("Total : " + container.getTotal());
		System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		while (container.getNextPage() != null) {
			container = service.search("lourival", container.getNextPage());
			System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		}
	}

	// @Test
	public void testDel() throws Exception {
		DocumentService service = new LuceneDocumentService(LuceneServiceTest.PATH_LUCENE);
		DocumentResultContainer container = service.search("lourival", new Page(1, 20));
		Document[] array = container.getItems();
		for (Document item : array) {
			System.out.println("Del: " + item.getId());
			service.remove(item.getId());
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
