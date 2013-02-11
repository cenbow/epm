package br.net.woodstock.epm.impl.test;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class InsertAdminTest {

	@Autowired(required = true)
	private SecurityService	securityService;

	public InsertAdminTest() {
		super();
	}

	@Test
	public void testRole() throws Exception {
		Role role = new Role();
		role.setActive(Boolean.TRUE);
		role.setName("ADMINISTRATOR");

		this.securityService.saveRole(role);
	}

	@Test
	public void testUser() throws Exception {
		User user = new User();
		user.setActive(Boolean.TRUE);
		user.setEmail("admin@woodstock.net.br");
		user.setLogin("admin");
		user.setName("Administrator");
		user.setPassword("admin");

		this.securityService.saveUser(user);
	}

	@Test
	public void testUserRole() throws Exception {
		this.securityService.saveUserRoles(new User(Integer.valueOf(1)), new Role(Integer.valueOf(1)));
	}

	// @Test
	public void testGetUser() throws Exception {
		User user = this.securityService.getUserByLoginPassword("admin", "admin");
		System.out.println(user.getName());
	}

	//@Test
	public void testListUser() throws Exception {
		QueryResult result = this.securityService.listUsersByName("admin", null);
		Collection<User> users = result.getResult();
		for (User user : users) {
			System.out.println(user.getName());
		}
	}

}
