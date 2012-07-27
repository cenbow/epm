package br.net.woodstock.epm.document.lucene.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.document.api.DocumentMetadata;
import br.net.woodstock.epm.document.api.DocumentSevice;
import br.net.woodstock.epm.document.api.Field;
import br.net.woodstock.epm.document.api.OrderBy;
import br.net.woodstock.epm.document.lucene.DocumentServiceImpl;

@RunWith(BlockJUnit4ClassRunner.class)
public class DocumentServiceTest {

	private static final String	PATH_STORE	= "/home/lourival/tmp/store";

	private static final String	PATH_LUCENE	= "/home/lourival/tmp/lucene";

	// @Test
	public void test1() throws Exception {
		DocumentSevice service = new DocumentServiceImpl(DocumentServiceTest.PATH_STORE, DocumentServiceTest.PATH_LUCENE);
		Collection<String> ids = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			String text = "Lourival Sabino " + i;
			byte[] data = text.getBytes();

			DocumentMetadata document = new DocumentMetadata();

			document.setContentType("text/plain");
			document.setDate(new Date().toString());
			document.setGroup("admin");
			document.setId(Integer.toString(i));
			document.setName("test" + i + ".txt");
			document.setText(text);
			document.setUser("admin");
			document.setVersion(i + ".0");

			service.save(document, data);
			ids.add(document.getId());
		}

		Thread.sleep(30000);

		for (String id : ids) {
			DocumentMetadata document = service.getMetadata(id);
			byte[] data = service.getData(id);
			System.out.println(document.getId());
			System.out.println("\t" + new String(document.getContentType()));
			System.out.println("\t" + new String(data));
			System.out.println("\t" + document.getDate());
			System.out.println("\t" + document.getGroup());
			System.out.println("\t" + document.getName());
			System.out.println("\t" + document.getText());
			System.out.println("\t" + document.getUser());
			System.out.println("\t" + document.getVersion());
		}
	}

	//@Test
	public void test2() throws Exception {
		DocumentSevice service = new DocumentServiceImpl(DocumentServiceTest.PATH_STORE, DocumentServiceTest.PATH_LUCENE);
		for (int i = 0; i < 10; i++) {
			DocumentMetadata document = service.getMetadata(Integer.toString(i));
			byte[] data = service.getData(Integer.toString(i));
			System.out.println(document.getId());
			System.out.println("\t" + new String(document.getContentType()));
			System.out.println("\t" + new String(data));
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
		DocumentSevice service = new DocumentServiceImpl(DocumentServiceTest.PATH_STORE, DocumentServiceTest.PATH_LUCENE);
		DocumentMetadata[] array = service.search("lourival", new OrderBy[] { new OrderBy(Field.NAME) }, 5);
		for (DocumentMetadata document : array) {
			byte[] data = service.getData(document.getId());
			System.out.println(document.getId());
			System.out.println("\t" + new String(document.getContentType()));
			System.out.println("\t" + new String(data));
			System.out.println("\t" + document.getDate());
			System.out.println("\t" + document.getGroup());
			System.out.println("\t" + document.getName());
			System.out.println("\t" + document.getText());
			System.out.println("\t" + document.getUser());
			System.out.println("\t" + document.getVersion());
		}
	}

}
