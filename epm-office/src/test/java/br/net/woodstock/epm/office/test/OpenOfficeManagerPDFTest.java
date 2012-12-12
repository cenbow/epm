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
import br.net.woodstock.epm.office.oo.impl.ConversionExecutor;
import br.net.woodstock.epm.office.oo.impl.DefaultOpenOfficeManager;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficeConnection;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class OpenOfficeManagerPDFTest {

	public OpenOfficeManagerPDFTest() {
		super();
	}

	@Test
	public void testConvert() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.pdf");
		OpenOfficeConnection connection = new SocketOpenOfficeConnection("localhost", 8100);
		OpenOfficeManager manager = new DefaultOpenOfficeManager(connection);
		ConversionExecutor template = new ConversionExecutor(input, OfficeDocumentType.HTML);
		InputStream output = manager.execute(template);

		File file = File.createTempFile("teste", ".html");
		FileOutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(output, outputStream);

		input.close();
		output.close();
		outputStream.close();
		System.out.println("File: " + file.getAbsolutePath());
	}

}
