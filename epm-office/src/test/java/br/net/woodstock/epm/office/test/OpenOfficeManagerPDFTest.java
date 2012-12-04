package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.ConversionExecutor;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class OpenOfficeManagerPDFTest {

	public OpenOfficeManagerPDFTest() {
		super();
	}

	@Test
	public void testConvert() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.pdf");
		OpenOfficeManager manager = new OpenOfficeManager(8100);
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
