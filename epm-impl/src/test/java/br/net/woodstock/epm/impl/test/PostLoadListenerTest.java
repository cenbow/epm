package br.net.woodstock.epm.impl.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class PostLoadListenerTest {

	@Autowired(required = true)
	private SecurityService	securityService;

	public PostLoadListenerTest() {
		super();
	}

	@Test
	public void testUser() throws Exception {
		User user = this.securityService.getUserById(Integer.valueOf(1));
		System.out.println(user.getName());
	}

}
