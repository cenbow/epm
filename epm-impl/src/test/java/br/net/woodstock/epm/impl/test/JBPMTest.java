package br.net.woodstock.epm.impl.test;

import java.io.File;
import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.woodstock.rockframework.core.utils.IO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class JBPMTest {

	@Autowired(required = true)
	private StatefulKnowledgeSession	session;

	public JBPMTest() {
		super();
	}

	// @Test
	public void testSession() throws Exception {
		System.out.println(this.session);
	}

	@Test
	public void testDeploy() throws Exception {
		File file = new File("/tmp/jbpm5-test.bpmn");
		byte[] bytes = IO.toByteArray(file);
		KnowledgeBase kbase = this.session.getKnowledgeBase();
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbase);
		Resource resource = ResourceFactory.newByteArrayResource(bytes);

		kbuilder.add(resource, ResourceType.BPMN2);

		if (!kbuilder.hasErrors()) {
			Collection<KnowledgePackage> collection = kbuilder.getKnowledgePackages();
			kbase.addKnowledgePackages(collection);
			System.out.println("OK");
		} else {
			System.out.println("Error " + kbuilder.getErrors());
		}
	}

	@Test
	public void testList() throws Exception {
		KnowledgeBase kbase = this.session.getKnowledgeBase();
		for (org.drools.definition.process.Process process : kbase.getProcesses()) {
			System.out.println(process.getId() + " => " + process.getName());
		}
	}
}
