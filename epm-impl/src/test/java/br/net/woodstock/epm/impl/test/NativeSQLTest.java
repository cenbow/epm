package br.net.woodstock.epm.impl.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.repository.impl.ORMRepositoryImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class NativeSQLTest {

	@PersistenceContext(unitName = ORMRepositoryImpl.PERSISTENCE_UNIT)
	private EntityManager	entityManager;

	public NativeSQLTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		Query query = this.entityManager.createNativeQuery("SELECT user_name FROM epm_user WHERE user_id IN (:ids)");
		Integer[] ids = new Integer[] { 1, 2, 3, 4, 5, 6 };
		query.setParameter("ids", ids);

		List<String> list = query.getResultList();
		for (String name : list) {
			System.out.println(name);
		}
	}
}
