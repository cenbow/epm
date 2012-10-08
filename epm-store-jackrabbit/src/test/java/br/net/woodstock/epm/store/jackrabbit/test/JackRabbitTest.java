package br.net.woodstock.epm.store.jackrabbit.test;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class JackRabbitTest {

	static {
		// System.setProperty("org.apache.jackrabbit.repository.home", "/tmp/jackrabbit");
	}

	// @Test
	public void test1() throws Exception {
		RepositoryConfig config = RepositoryConfig.create(this.getClass().getClassLoader().getResourceAsStream("repository.xml"), "/tmp/jackrabbit");
		Repository repository = new TransientRepository(config);
		Session session = repository.login();
		try {
			String user = session.getUserID();
			String name = repository.getDescriptor(Repository.REP_NAME_DESC);
			System.out.println("Logged in as " + user + " to a " + name + " repository.");
		} finally {
			session.logout();
		}
	}

	// @Test
	public void test2() throws Exception {
		RepositoryConfig config = RepositoryConfig.create(this.getClass().getClassLoader().getResourceAsStream("repository.xml"), "/tmp/jackrabbit");
		Repository repository = new TransientRepository(config);
		Session session = repository.login(new SimpleCredentials("username", "password".toCharArray()), "default");
		try {
			Node root = session.getRootNode();

			// Store content
			Node hello = root.addNode("hello");
			Node world = hello.addNode("world");
			world.setProperty("message", "Hello, World!");
			session.save();

			// Retrieve content
			Node node = root.getNode("hello/world");
			System.out.println(node.getPath());
			System.out.println(node.getProperty("message").getString());

			// Remove content
			root.getNode("hello").remove();
			session.save();
		} finally {
			session.logout();
		}
	}

}
