package br.net.woodstock.epm.impl.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.acl.api.User;
import br.net.woodstock.epm.document.api.Document;
import br.net.woodstock.epm.document.api.DocumentService;
import br.net.woodstock.epm.document.jackrabbit.JackRabbitDocumentService;

@RunWith(BlockJUnit4ClassRunner.class)
public class JackRabbitServiceTest {

	private static final String	JNDI_NAME	= "jcrRepo";

	private static final String	HOME		= "/home/lourival/tmp/jackrabbit";

	public JackRabbitServiceTest() {
		super();
	}

	// @Test
	public void test1() throws Exception {
		DocumentService service = new JackRabbitDocumentService(JackRabbitServiceTest.JNDI_NAME, JackRabbitServiceTest.HOME);
		for (int i = 0; i < 10; i++) {
			String id = Integer.toString(i);
			String text = "Lourival Sabino " + i;
			byte[] binary = text.getBytes();
			Document document = new Document();
			document.setBinary(binary);
			document.setCreated(new Date());
			document.setId(id);
			document.setMimeType("text/plain");
			document.setModified(new Date());
			document.setName("test-" + i + ".txt");
			document.setOwner(new User("admin"));
			document.setText(text);

			service.save(document);
		}
	}

	@Test
	public void test2() throws Exception {
		DocumentService service = new JackRabbitDocumentService(JackRabbitServiceTest.JNDI_NAME, JackRabbitServiceTest.HOME);
		for (int i = 0; i < 10; i++) {
			Document document = service.get(Integer.toString(i));
			System.out.println("\t" + new String(document.getBinary()));
		}
	}

}
