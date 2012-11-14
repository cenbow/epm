package br.net.woodstock.epm.office.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.form.FieldManager;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class TemplateTest {

	public TemplateTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		FileInputStream fileInputStream = new FileInputStream("/tmp/teste.odt");
		byte[] inDoc = IOUtils.toByteArray(fileInputStream);
		fileInputStream.close();

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("Nome", "Lourival Sabino");
		values.put("Endereco", "QNL 19 Bloco J Casa 8");
		values.put("Telefone", "9825-9582");

		byte[] outDoc = new FieldManager().setValues(inDoc, values);

		FileOutputStream outputStream = new FileOutputStream("/tmp/teste2.odt");
		outputStream.write(outDoc);
		outputStream.close();
	}

}
