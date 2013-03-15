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
import br.net.woodstock.epm.office.oo.impl.ExecutableOpenOfficeManager;
import br.net.woodstock.epm.office.oo.impl.PipeOpenOfficeConfig;
import br.net.woodstock.rockframework.core.utils.IO;

@RunWith(BlockJUnit4ClassRunner.class)
public class PipeOpenOfficeManagerTest {

	public PipeOpenOfficeManagerTest() {
		super();
	}

	@Test
	public void testConvert() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		OpenOfficeConfig config = new PipeOpenOfficeConfig("teste");
		OpenOfficeManager manager = new ExecutableOpenOfficeManager(config);
		ConversionCallback template = new ConversionCallback(input, OfficeDocumentType.DOCX);
		InputStream output = manager.execute(template);

		File file = File.createTempFile("teste", ".docx");
		FileOutputStream outputStream = new FileOutputStream(file);
		IO.copy(output, outputStream);

		input.close();
		output.close();
		outputStream.close();
		System.out.println("File: " + file.getAbsolutePath());
	}

}
