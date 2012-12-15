package br.net.woodstock.epm.office.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.OpenOfficeConfig;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.epm.office.oo.callback.ConversionCallback;
import br.net.woodstock.epm.office.oo.callback.GetFieldNameCallback;
import br.net.woodstock.epm.office.oo.callback.PopulateTemplateCallback;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficeConfig;
import br.net.woodstock.epm.office.oo.impl.SynchronizedOpenOfficeManager;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class OpenOfficeManagerTest {

	public OpenOfficeManagerTest() {
		super();
	}

	// @Test
	public void testConvert() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		OpenOfficeConfig config = new SocketOpenOfficeConfig(8100);
		OpenOfficeManager manager = new SynchronizedOpenOfficeManager(config);
		ConversionCallback template = new ConversionCallback(input, OfficeDocumentType.PDF);
		InputStream output = manager.execute(template);

		File file = File.createTempFile("teste", ".pdf");
		FileOutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(output, outputStream);

		input.close();
		output.close();
		outputStream.close();
		System.out.println("File: " + file.getAbsolutePath());
	}

	@Test
	public void testConvertMultiples() throws Exception {
		OpenOfficeConfig config = new SocketOpenOfficeConfig(8100);
		SynchronizedOpenOfficeManager manager = new SynchronizedOpenOfficeManager(config);
		manager.start();

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		byte[] bytes = IOUtils.toByteArray(input);
		input.close();
		for (int i = 0; i < 10; i++) {
			long l = System.currentTimeMillis();
			ConversionCallback template = new ConversionCallback(new ByteArrayInputStream(bytes), OfficeDocumentType.PDF);
			InputStream output = manager.execute(template);

			File file = File.createTempFile("teste", ".pdf");
			FileOutputStream outputStream = new FileOutputStream(file);
			IOUtils.copy(output, outputStream);

			input.close();
			output.close();
			outputStream.close();
			System.out.println("File: " + file.getAbsolutePath() + " - " + (System.currentTimeMillis() - l) + "ms");
		}

		manager.stop();
	}

	// @Test
	public void testGetNames() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		OpenOfficeConfig config = new SocketOpenOfficeConfig(8100);
		OpenOfficeManager manager = new SynchronizedOpenOfficeManager(config);
		GetFieldNameCallback template = new GetFieldNameCallback(input);
		Set<String> names = manager.execute(template);

		input.close();
		for (String name : names) {
			System.out.println(name);
		}
	}

	// @Test
	public void testSetValues() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		OpenOfficeConfig config = new SocketOpenOfficeConfig(8100);
		OpenOfficeManager manager = new SynchronizedOpenOfficeManager(config);

		Map<String, String> values = new HashMap<String, String>();
		values.put("Nome", "Lourival Sabino");
		values.put("Endereco", "QNL 19 Bloco J Casa 8");
		values.put("Telefone", "92859582");

		PopulateTemplateCallback template = new PopulateTemplateCallback(input, values, OfficeDocumentType.ODT);
		InputStream output = manager.execute(template);

		File file = File.createTempFile("teste", ".odt");
		FileOutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(output, outputStream);

		input.close();
		output.close();
		outputStream.close();
		System.out.println("File: " + file.getAbsolutePath());
	}

}
