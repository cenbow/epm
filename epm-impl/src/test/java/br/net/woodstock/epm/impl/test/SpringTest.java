package br.net.woodstock.epm.impl.test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.repository.impl.ORMRepositoryImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class SpringTest {

	@PersistenceContext(name = ORMRepositoryImpl.PERSISTENCE_UNIT)
	private EntityManager	entityManager;

	public SpringTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		System.out.println("Entity Manager: " + this.entityManager);
	}

}
