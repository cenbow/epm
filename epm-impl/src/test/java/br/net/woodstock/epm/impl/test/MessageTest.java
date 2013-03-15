package br.net.woodstock.epm.impl.test;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.util.EPMMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class MessageTest {

	@Autowired(required = true)
	private EPMMessage	message;

	public MessageTest() {
		super();
	}

	@Test
	public void testListUser() throws Exception {
		System.out.println(this.message.getMessage("msg.ok", null, Locale.getDefault()));
	}

}
