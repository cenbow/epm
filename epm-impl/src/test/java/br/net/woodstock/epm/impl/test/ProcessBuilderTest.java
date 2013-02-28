package br.net.woodstock.epm.impl.test;

import org.activiti.engine.impl.pvm.ProcessDefinitionBuilder;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.process.api.ProcessService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ProcessBuilderTest {

	@Autowired(required = true)
	private ProcessService	service;

	public ProcessBuilderTest() {
		super();
	}

	@Test
	public void testBuild() {
		ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder();
		ActivityBehavior behavior = null;
		builder.createActivity("teste").behavior(behavior);
	}
}
