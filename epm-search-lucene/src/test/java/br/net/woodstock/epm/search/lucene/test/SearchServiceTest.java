package br.net.woodstock.epm.search.lucene.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.search.api.DocumentMetadata;
import br.net.woodstock.epm.search.api.Field;
import br.net.woodstock.epm.search.api.OrderBy;
import br.net.woodstock.epm.search.api.SearchSevice;
import br.net.woodstock.epm.search.lucene.LuceneSearchService;

@RunWith(BlockJUnit4ClassRunner.class)
public class SearchServiceTest {

	private static final String	PATH_LUCENE	= "/home/lourival/tmp/lucene";

	// @Test
	public void test1() throws Exception {
		SearchSevice service = new LuceneSearchService(SearchServiceTest.PATH_LUCENE);
		Collection<String> ids = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			String text = "Lourival Sabino " + i;

			DocumentMetadata document = new DocumentMetadata();

			document.setContentType("text/plain");
			document.setDate(new Date().toString());
			document.setGroup("admin");
			document.setId(Integer.toString(i));
			document.setName("test" + i + ".txt");
			document.setText(text);
			document.setUser("admin");
			document.setVersion(i + ".0");

			service.save(document);
			ids.add(document.getId());
		}

		Thread.sleep(30000);

		for (String id : ids) {
			DocumentMetadata document = service.getMetadata(id);
			System.out.println(document.getId());
			System.out.println("\t" + document.getContentType());
			System.out.println("\t" + document.getDate());
			System.out.println("\t" + document.getGroup());
			System.out.println("\t" + document.getName());
			System.out.println("\t" + document.getText());
			System.out.println("\t" + document.getUser());
			System.out.println("\t" + document.getVersion());
		}
	}

	// @Test
	public void test2() throws Exception {
		SearchSevice service = new LuceneSearchService(SearchServiceTest.PATH_LUCENE);
		for (int i = 0; i < 10; i++) {
			DocumentMetadata document = service.getMetadata(Integer.toString(i));
			System.out.println(document.getId());
			System.out.println("\t" + document.getContentType());
			System.out.println("\t" + document.getDate());
			System.out.println("\t" + document.getGroup());
			System.out.println("\t" + document.getName());
			System.out.println("\t" + document.getText());
			System.out.println("\t" + document.getUser());
			System.out.println("\t" + document.getVersion());
		}
	}

	@Test
	public void test3() throws Exception {
		SearchSevice service = new LuceneSearchService(SearchServiceTest.PATH_LUCENE);
		DocumentMetadata[] array = service.search("lourival", new OrderBy[] { new OrderBy(Field.NAME) }, 5);
		for (DocumentMetadata document : array) {
			System.out.println(document.getId());
			System.out.println("\t" + document.getContentType());
			System.out.println("\t" + document.getDate());
			System.out.println("\t" + document.getGroup());
			System.out.println("\t" + document.getName());
			System.out.println("\t" + document.getText());
			System.out.println("\t" + document.getUser());
			System.out.println("\t" + document.getVersion());
		}
	}

}
