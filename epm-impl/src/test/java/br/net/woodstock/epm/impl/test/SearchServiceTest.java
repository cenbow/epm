package br.net.woodstock.epm.impl.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.search.api.Field;
import br.net.woodstock.epm.search.api.Item;
import br.net.woodstock.epm.search.api.OrderBy;
import br.net.woodstock.epm.search.api.SearchResultContainer;
import br.net.woodstock.epm.search.api.SearchSevice;
import br.net.woodstock.epm.search.lucene.LuceneSearchService;
import br.net.woodstock.epm.util.Page;

@RunWith(BlockJUnit4ClassRunner.class)
public class SearchServiceTest {

	private static final String	PATH_LUCENE	= "/home/lourival/tmp/lucene";

	public SearchServiceTest() {
		super();
	}

	// @Test
	public void test1() throws Exception {
		SearchSevice service = new LuceneSearchService(SearchServiceTest.PATH_LUCENE);
		Collection<String> ids = new ArrayList<String>();
		for (int i = 0; i < 1000; i++) {
			String text = "Lourival Sabino " + i;

			Item item = new Item();

			item.setContentType("text/plain");
			item.setDate(new Date().toString());
			item.setDescription(text);
			item.setExtension("txt");
			item.setId(Integer.toString(i));
			item.setName("test" + i + ".txt");
			item.setOwner("admin");
			item.setText(text);
			item.setType("doc");

			service.save(item);
			ids.add(item.getId());
		}

		Thread.sleep(30000);
	}

	// @Test
	public void test2() throws Exception {
		SearchSevice service = new LuceneSearchService(SearchServiceTest.PATH_LUCENE);
		for (int i = 0; i < 10; i++) {
			Item item = service.get(Integer.toString(i));
			this.print(item);
		}
	}

	@Test
	public void test3() throws Exception {
		SearchSevice service = new LuceneSearchService(SearchServiceTest.PATH_LUCENE);
		SearchResultContainer container = service.search("lourival", new OrderBy[] { new OrderBy(Field.ID) }, new Page(1, 20));
		System.out.println("Total : " + container.getTotal());
		System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
		Item[] array = container.getItems();
		for (Item item : array) {
			this.print(item);
		}
		while (container.getNextPage() != null) {
			container = service.search("lourival", new OrderBy[] { new OrderBy(Field.ID) }, container.getNextPage());
			System.out.println("Pagina: " + container.getCurrentPage().getPageNumber());
			array = container.getItems();
			for (Item item : array) {
				this.print(item);
			}
		}
	}

	// @Test
	public void test4() throws Exception {
		SearchSevice service = new LuceneSearchService(SearchServiceTest.PATH_LUCENE);
		SearchResultContainer container = service.search("lourival", new OrderBy[] { new OrderBy(Field.ID) }, new Page(1, 20));
		Item[] array = container.getItems();
		for (Item item : array) {
			System.out.println("Del: " + item.getId());
			service.remove(item.getId());
		}
		Thread.sleep(30000);
	}

	private void print(final Item item) {
		System.out.println("\t" + item.getId());
		System.out.println("\t\t" + item.getContentType());
		System.out.println("\t\t" + item.getDate());
		System.out.println("\t\t" + item.getDescription());
		System.out.println("\t\t" + item.getExtension());
		System.out.println("\t\t" + item.getName());
		System.out.println("\t\t" + item.getOwner());
		System.out.println("\t\t" + item.getText());
		System.out.println("\t\t" + item.getType());
	}

}
