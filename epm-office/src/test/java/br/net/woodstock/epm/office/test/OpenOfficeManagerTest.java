package br.net.woodstock.epm.office.test;

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
import br.net.woodstock.epm.office.oo.ConversionExecutor;
import br.net.woodstock.epm.office.oo.GetFieldNameExecutor;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.epm.office.oo.PopulateTemplateExecutor;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class OpenOfficeManagerTest {

	public OpenOfficeManagerTest() {
		super();
	}

	// @Test
	public void testConvert() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		OpenOfficeManager manager = new OpenOfficeManager(8100);
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

	// @Test
	public void testGetNames() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		OpenOfficeManager manager = new OpenOfficeManager(8100);
		GetFieldNameExecutor template = new GetFieldNameExecutor(input);
		Set<String> names = manager.execute(template);

		input.close();
		for (String name : names) {
			System.out.println(name);
		}
	}

	@Test
	public void testSetValues() throws Exception {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		OpenOfficeManager manager = new OpenOfficeManager(8100);

		Map<String, String> values = new HashMap<String, String>();
		values.put("Nome", "Lourival Sabino");
		values.put("Endereco", "QNL 19 Bloco J Casa 8");
		values.put("Telefone", "92859582");

		PopulateTemplateExecutor template = new PopulateTemplateExecutor(input, values, OfficeDocumentType.ODT);
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
