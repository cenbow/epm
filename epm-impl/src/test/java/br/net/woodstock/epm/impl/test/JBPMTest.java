package br.net.woodstock.epm.impl.test;

import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class JBPMTest {

	@Autowired(required = true)
	private StatefulKnowledgeSession	session;

	public JBPMTest() {
		super();
	}

	@Test
	public void testListDeployment() throws Exception {
		System.out.println(this.session);
	}
}
