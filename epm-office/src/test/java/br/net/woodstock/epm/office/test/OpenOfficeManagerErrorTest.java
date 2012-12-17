package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.OpenOfficeConfig;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.epm.office.oo.callback.ConversionCallback;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficeConfig;
import br.net.woodstock.epm.office.oo.impl.ExecutableOpenOfficeManager;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class OpenOfficeManagerErrorTest {

	public OpenOfficeManagerErrorTest() {
		super();
	}

	@Test
	public void testConvert() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.pdf");
		OpenOfficeConfig config = new SocketOpenOfficeConfig(8100);
		OpenOfficeManager manager = new ExecutableOpenOfficeManager(config);
		ConversionCallback template = new ConversionCallback(input, OfficeDocumentType.HTML);
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
