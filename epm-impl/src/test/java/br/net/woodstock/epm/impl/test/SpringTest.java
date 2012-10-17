package br.net.woodstock.epm.impl.test;

import org.activiti.engine.ProcessEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class SpringTest {

	@Autowired(required = true)
	private ProcessEngine	engine;

	public SpringTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		System.out.println("Engine: " + this.engine);
	}

}
