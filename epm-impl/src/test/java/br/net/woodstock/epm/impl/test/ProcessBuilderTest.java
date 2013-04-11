package br.net.woodstock.epm.impl.test;

import org.activiti.engine.impl.pvm.ProcessDefinitionBuilder;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.epm.bpmn2.beans.EndEvent;
import br.net.woodstock.epm.bpmn2.beans.Process;
import br.net.woodstock.epm.bpmn2.beans.StartEvent;
import br.net.woodstock.epm.bpmn2.beans.Task;
import br.net.woodstock.epm.bpmn2.beans.Transition;
import br.net.woodstock.epm.process.api.BusinessProcessService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ProcessBuilderTest {

	@Autowired(required = true)
	private BusinessProcessService	service;

	public ProcessBuilderTest() {
		super();
	}

	@Test
	public void testBuild() {
		Process process = new Process("beantest1", "Bean Test 1");

		StartEvent startEvent = new StartEvent("start-event", "Start event");
		EndEvent endEvent = new EndEvent("end-event", "End event");

		Transition toEnd = new Transition("to-end", "To End", endEvent);
		startEvent.getTransitions().add(toEnd);

		process.getTasks().add(startEvent);
		process.getTasks().add(endEvent);

		ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder(process.getId());

		for (Task task : process.getTasks()) {
			builder.createActivity(task.getId());
			if (task instanceof StartEvent) {
				builder.initial();
			}

			for (Transition transition : task.getTransitions()) {
				builder.transition(transition.getTask().getId(), transition.getId());
			}

			builder.endActivity();
		}

		PvmProcessDefinition processDefinition = builder.buildProcessDefinition();
		System.out.println(processDefinition.getId());
		System.out.println(processDefinition.getDeploymentId());
	}
}
