package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.impl.AbstractOpenOfficeConnection;
import br.net.woodstock.epm.office.oo.impl.ConversionExecutor;
import br.net.woodstock.epm.office.oo.impl.PipeOpenOfficeConnection;
import br.net.woodstock.epm.office.oo.impl.SimpleOpenOfficeManager;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class PipeOpenOfficeManagerTest {

	public PipeOpenOfficeManagerTest() {
		super();
	}

	@Test
	public void testConvert() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		AbstractOpenOfficeConnection connection = new PipeOpenOfficeConnection("teste");
		SimpleOpenOfficeManager manager = new SimpleOpenOfficeManager(connection);
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
