package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.epm.office.oo.callback.MergeCallback;
import br.net.woodstock.epm.office.oo.impl.SimpleOpenOfficeManager;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficeConnection;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class MergeTest {

	public MergeTest() {
		super();
	}

	@Test
	public void testMerge() throws Exception {
		InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream("teste1.odt");
		InputStream inputStream2 = this.getClass().getClassLoader().getResourceAsStream("teste2.odt");
		InputStream inputStream3 = this.getClass().getClassLoader().getResourceAsStream("teste3.odt");
		// OpenOfficeConnection connection = new
		// DefaultOpenOfficeConnection("socket,host=127.0.0.1,port=8100,tcpNoDelay=1");
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		connection.connect();
		OpenOfficeManager manager = new SimpleOpenOfficeManager(connection);

		InputStream[] sources = new InputStream[] { inputStream1, inputStream2, inputStream3 };

		MergeCallback callback = new MergeCallback(sources, OfficeDocumentType.ODT);

		InputStream output = manager.execute(callback);

		File file = File.createTempFile("teste", ".odt");
		FileOutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(output, outputStream);

		inputStream1.close();
		inputStream2.close();
		inputStream3.close();
		output.close();
		outputStream.close();
		System.out.println("File: " + file.getAbsolutePath());
	}
}
