package br.net.woodstock.epm.store.filesystem.test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.store.api.StoreSevice;
import br.net.woodstock.epm.store.filesystem.FileSystemStoreService;

@RunWith(BlockJUnit4ClassRunner.class)
public class StoreServiceTest {

	private static final String	PATH_STORE	= "/home/lourival/tmp/store";

	// @Test
	public void test1() throws Exception {
		StoreSevice service = new FileSystemStoreService(StoreServiceTest.PATH_STORE);
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
			byte[] data = service.getData(id);
			System.out.println("\t" + new String(data));
		}
	}

	// @Test
	public void test2() throws Exception {
		StoreSevice service = new FileSystemStoreService(StoreServiceTest.PATH_STORE);
		for (int i = 0; i < 10; i++) {
			byte[] data = service.getData(Integer.toString(i));
			System.out.println("\t" + new String(data));
		}
	}

}
