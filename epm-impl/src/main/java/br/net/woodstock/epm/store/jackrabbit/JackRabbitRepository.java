package br.net.woodstock.epm.store.filesystem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;

import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.security.digest.DigestType;
import br.net.woodstock.rockframework.security.digest.impl.AsStringDigester;
import br.net.woodstock.rockframework.security.digest.impl.BasicDigester;
import br.net.woodstock.rockframework.security.digest.impl.HexDigester;
import br.net.woodstock.rockframework.security.store.StoreException;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.IOUtils;

public class JackRabbitRepository implements Service {

	private static final long	serialVersionUID	= -7290717732247236271L;

	private static final String	REPOSITORY_SETTINGS	= "repository.xml";

	private static final String	REPOSITORY_USER		= "username";

	private static final char[]	REPOSITORY_PASSWORD	= "password".toCharArray();

	private static final String	CONTENT_NODE_NAME	= "jcr:content";

	private static final String	CONTENT_NODE_TYPE	= "nt:resource";

	private static final String	DATA_NODE_PROPERTY	= "jcr:data";

	private AsStringDigester	digester			= new AsStringDigester(new HexDigester(new BasicDigester(DigestType.SHA1)));

	private Credentials			credentials			= new SimpleCredentials(JackRabbitRepository.REPOSITORY_USER, JackRabbitRepository.REPOSITORY_PASSWORD);

	private Repository			repository;

	public JackRabbitRepository(final String path) {
		super();
		Assert.notEmpty(path, "path");
		this.init(path);
	}

	private void init(final String path) {
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JackRabbitRepository.REPOSITORY_SETTINGS);
			RepositoryConfig config = RepositoryConfig.create(inputStream, path);
			this.repository = new TransientRepository(config);
		} catch (RepositoryException e) {
			throw new StoreException(e);
		}
	}

	public byte[] get(final String id) throws IOException {
		String realId = this.parseId(id);
		Session session = null;
		try {
			session = this.repository.login(this.credentials);
			Node root = session.getRootNode();
			Node node = root.getNode(realId);
			if (node != null) {
				Node content = node.getNode(JackRabbitRepository.CONTENT_NODE_NAME);
				Property property = content.getProperty(JackRabbitRepository.DATA_NODE_PROPERTY);
				if (property != null) {
					Binary binary = property.getBinary();
					byte[] bytes = IOUtils.toByteArray(binary.getStream());
					return bytes;
				}
			}
			return null;
		} catch (RepositoryException e) {
			throw new StoreException(e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	public boolean remove(final String id) {
		String realId = this.parseId(id);
		Session session = null;
		try {
			session = this.repository.login(this.credentials);
			Node root = session.getRootNode();
			Node node = root.getNode(realId);
			boolean ok = false;
			if (node != null) {
				node.remove();
			}
			session.save();
			return ok;
		} catch (RepositoryException e) {
			throw new StoreException(e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	public void store(final String id, final byte[] data) {
		String realId = this.parseId(id);
		Session session = null;
		try {
			session = this.repository.login(this.credentials);
			Node root = session.getRootNode();
			Node node = root.addNode(realId);
			Node content = node.addNode(JackRabbitRepository.CONTENT_NODE_NAME, JackRabbitRepository.CONTENT_NODE_TYPE);
			Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(data));
			content.setProperty(JackRabbitRepository.DATA_NODE_PROPERTY, binary);
			session.save();
		} catch (RepositoryException e) {
			throw new StoreException(e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	private String parseId(final String id) {
		return this.digester.digestAsString(id);
	}
}
