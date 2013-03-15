package br.net.woodstock.epm.impl.test;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.orm.UserRole;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class InsertAdminTest {

	@Autowired(required = true)
	private SecurityService	securityService;

	public InsertAdminTest() {
		super();
	}

	// @Test
	public void testRole() throws Exception {
		Role role = new Role();
		role.setActive(Boolean.TRUE);
		role.setName("ADMINISTRATOR");

		this.securityService.saveRole(role);
	}

	// @Test
	public void testUser() throws Exception {
		User user = new User();
		user.setActive(Boolean.TRUE);
		user.setEmail("admin@woodstock.net.br");
		user.setLogin("admin");
		user.setName("Administrator");
		user.setPassword("admin");

		this.securityService.saveUser(user);
	}

	// @Test
	public void testUserRole() throws Exception {
		User user = new User(Integer.valueOf(1));
		Role role = new Role(Integer.valueOf(1));
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		this.securityService.saveUserRole(userRole);
	}

	// @Test
	public void testInsertResources() throws Exception {
		Resource resource1 = new Resource();
		resource1.setName("/spring/index");
		this.securityService.saveResource(resource1);

		Resource resource2 = new Resource();
		resource2.setName("/spring/security/user");
		this.securityService.saveResource(resource2);

		Resource resource3 = new Resource();
		resource3.setName("/spring/security/role");
		this.securityService.saveResource(resource3);

		Resource resource4 = new Resource();
		resource4.setName("/spring/security/department");
		this.securityService.saveResource(resource4);

		Resource resource5 = new Resource();
		resource5.setName("/spring/security/resource");
		this.securityService.saveResource(resource5);

		this.securityService.setRoleResources(new Role(Integer.valueOf(1)), new Resource[] { resource1, resource2, resource3, resource4, resource5 });
	}

	// @Test
	public void testGetUser() throws Exception {
		User user = this.securityService.getUserByLoginPassword("admin", "admin");
		System.out.println(user.getName());
	}

	@Test
	public void testListUser() throws Exception {
		ORMResult result = this.securityService.listUsersByName("admin", null);
		Collection<User> users = result.getItems();
		for (User user : users) {
			System.out.println(user.getName());
		}
	}

}
