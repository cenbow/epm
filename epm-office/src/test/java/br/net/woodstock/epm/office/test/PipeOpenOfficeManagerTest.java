package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.AbstractOpenOfficeConnection;
import br.net.woodstock.epm.office.oo.ConversionExecutor;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.epm.office.oo.PipeOpenOfficeConnection;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class PipeOpenOfficeManagerTest {

	static {
		//System.setProperty("java.library.path", "/usr/lib64/libreoffice/ure-link/lib/");
	}

	public PipeOpenOfficeManagerTest() {
		super();
	}

	@Test
	public void testConvert() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		AbstractOpenOfficeConnection connection = new PipeOpenOfficeConnection("teste");
		OpenOfficeManager manager = new OpenOfficeManager(connection);
		ConversionExecutor template = new ConversionExecutor(input, OfficeDocumentType.DOCX);
		InputStream output = manager.execute(template);

		File file = File.createTempFile("teste", ".docx");
		FileOutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(output, outputStream);

		input.close();
		output.close();
		outputStream.close();
		System.out.println("File: " + file.getAbsolutePath());
	}

}
