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
public class InsertUserTest {

	@Autowired(required = true)
	private SecurityService	securityService;

	public InsertUserTest() {
		super();
	}

	@Test
	public void testUser() throws Exception {
		for (int i = 0; i < 50; i++) {
			User user = new User();
			user.setActive(Boolean.TRUE);
			user.setEmail("user" + i + "@woodstock.net.br");
			user.setLogin("user" + i);
			user.setName("User " + i);
			user.setPassword("user" + i);

			this.securityService.saveUser(user);
		}
	}

}
