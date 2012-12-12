package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeServer;
import br.net.woodstock.epm.office.oo.impl.ConversionExecutor;
import br.net.woodstock.epm.office.oo.impl.DefaultOpenOfficeManager;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficeServer;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class OpenOfficeServerErrorTest {

	public OpenOfficeServerErrorTest() {
		super();
	}

	// @Test
	public void testConvert() throws Exception {
		OpenOfficeServer server = null;
		try {
			InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.pdf");
			server = new SocketOpenOfficeServer(8100);

			server.start();

			OpenOfficeConnection connection = server.getConnection();
			DefaultOpenOfficeManager manager = new DefaultOpenOfficeManager(connection);
			ConversionExecutor template = new ConversionExecutor(input, OfficeDocumentType.HTML);
			InputStream output = manager.execute(template);

			File file = File.createTempFile("teste", ".html");
			FileOutputStream outputStream = new FileOutputStream(file);
			IOUtils.copy(output, outputStream);

			input.close();
			output.close();
			outputStream.close();

			System.out.println("File: " + file.getAbsolutePath());
		} catch (Exception e) {
			if (server != null) {
				server.stop();
			}
			throw e;
		}
	}

	@Test
	public void testConvertLarge() throws Exception {
		OpenOfficeServer server = null;
		try {
			InputStream input = new FileInputStream("/home/lourival/Documents/9781118014400_3946125_9781118014400.pdf");
			server = new SocketOpenOfficeServer(8100);

			server.start();

			OpenOfficeConnection connection = server.getConnection();
			DefaultOpenOfficeManager manager = new DefaultOpenOfficeManager(connection);
			ConversionExecutor template = new ConversionExecutor(input, OfficeDocumentType.HTML);
			InputStream output = manager.execute(template);

			File file = File.createTempFile("teste", ".html");
			FileOutputStream outputStream = new FileOutputStream(file);
			IOUtils.copy(output, outputStream);

			input.close();
			output.close();
			outputStream.close();

			System.out.println("File: " + file.getAbsolutePath());
		} catch (Exception e) {
			if (server != null) {
				server.stop();
			}
			throw e;
		}
	}

}
