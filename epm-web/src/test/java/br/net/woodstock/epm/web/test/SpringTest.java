package br.net.woodstock.epm.web.test;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-web-test.xml" })
public class SpringTest implements ApplicationContextAware {

	@Autowired(required = true)
	@Qualifier(value = "AuthenticationProvider")
	private AuthenticationProvider	authenticationProvider;

	private ApplicationContext		applicationContext;

	public SpringTest() {
		super();
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Test
	public void test1() throws Exception {
		System.out.println("Authentication Provider: " + this.authenticationProvider);
	}

	@Test
	public void test2() throws Exception {
		Map<String, Object> map = this.applicationContext.getBeansOfType(Object.class);
		for (Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " => " + entry.getValue());
		}
	}
}
