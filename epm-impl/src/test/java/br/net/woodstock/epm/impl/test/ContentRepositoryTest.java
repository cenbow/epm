package br.net.woodstock.epm.impl.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.document.api.ContentRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ContentRepositoryTest {

	@Autowired(required = true)
	private ContentRepository	contentRepository;

	public ContentRepositoryTest() {
		super();
	}

	// @Test
	public void testAddFile() throws Exception {
		this.contentRepository.saveContent(Integer.valueOf(1), "Teste".getBytes());
	}
	
	// @Test
	public void testUpdateFile() throws Exception {
		this.contentRepository.updateContent(Integer.valueOf(1), "Teste Teste Teste Teste".getBytes());
	}

	@Test
	public void testGetFile() throws Exception {
		byte[] bytes = this.contentRepository.getContentById(Integer.valueOf(1));
		System.out.println(new String(bytes));
	}

}
