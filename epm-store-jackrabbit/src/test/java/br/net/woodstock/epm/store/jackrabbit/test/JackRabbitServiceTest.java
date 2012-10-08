package br.net.woodstock.epm.store.jackrabbit.test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.store.api.StoreSevice;
import br.net.woodstock.epm.store.filesystem.JackRabbitStoreService;

@RunWith(BlockJUnit4ClassRunner.class)
public class JackRabbitServiceTest {

	private static final String	PATH_STORE	= "/home/lourival/tmp/jackrabbit";

	// @Test
	public void test1() throws Exception {
		StoreSevice service = new JackRabbitStoreService(JackRabbitServiceTest.PATH_STORE);
		Collection<String> ids = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			String text = "Lourival Sabino " + i;
			byte[] data = text.getBytes();
			String id = Integer.toString(i);

			service.save(id, data);
			ids.add(id);
		}

		Thread.sleep(30000);

		for (String id : ids) {
			byte[] data = service.get(id);
			System.out.println("\t" + new String(data));
		}
	}

	@Test
	public void test2() throws Exception {
		StoreSevice service = new JackRabbitStoreService(JackRabbitServiceTest.PATH_STORE);
		for (int i = 0; i < 10; i++) {
			byte[] data = service.get(Integer.toString(i));
			System.out.println("\t" + new String(data));
		}
	}

}
