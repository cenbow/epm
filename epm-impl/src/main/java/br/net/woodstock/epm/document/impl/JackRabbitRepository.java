package br.net.woodstock.epm.document.impl;

import java.io.ByteArrayInputStream;
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

import br.net.woodstock.epm.document.api.ContentRepository;
import br.net.woodstock.rockframework.core.util.Assert;
import br.net.woodstock.rockframework.core.utils.IO;
import br.net.woodstock.rockframework.domain.ServiceException;
import br.net.woodstock.rockframework.security.digest.DigestType;
import br.net.woodstock.rockframework.security.digest.impl.AsStringDigester;
import br.net.woodstock.rockframework.security.digest.impl.BasicDigester;
import br.net.woodstock.rockframework.security.digest.impl.HexDigester;

public class JackRabbitRepository implements ContentRepository {

	private static final long	serialVersionUID	= -7290717732247236271L;

	private static final String	REPOSITORY_SETTINGS	= "repository.xml";

	private static final String	CONTENT_NODE_NAME	= "jcr:content";

	private static final String	CONTENT_NODE_TYPE	= "nt:resource";

	private static final String	DATA_NODE_PROPERTY	= "jcr:data";

	private AsStringDigester	digester			= new AsStringDigester(new HexDigester(new BasicDigester(DigestType.SHA1)));

	private Credentials			credentials;

	private Repository			repository;

	public JackRabbitRepository(final String path, final String user, final String password) {
		super();
		Assert.notEmpty(path, "path");
		Assert.notEmpty(user, "user");
		Assert.notEmpty(password, "password");
		this.init(path, user, password);
	}

	private void init(final String path, final String user, final String password) {
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JackRabbitRepository.REPOSITORY_SETTINGS);
			RepositoryConfig config = RepositoryConfig.create(inputStream, path);
			this.repository = new TransientRepository(config);
			this.credentials = new SimpleCredentials(user, password.toCharArray());
		} catch (RepositoryException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] getContentById(final Integer id) {
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
					byte[] bytes = IO.toByteArray(binary.getStream());
					return bytes;
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	@Override
	public void updateContent(final Integer id, final byte[] content) {
		String realId = this.parseId(id);
		Session session = null;
		try {
			session = this.repository.login(this.credentials);
			Node root = session.getRootNode();
			Node node = root.getNode(realId);
			if (node != null) {
				Node contentNode = node.getNodes(JackRabbitRepository.CONTENT_NODE_NAME).nextNode();
				Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(content));
				contentNode.setProperty(JackRabbitRepository.DATA_NODE_PROPERTY, binary);
			}
			session.save();
		} catch (RepositoryException e) {
			throw new ServiceException(e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	@Override
	public void saveContent(final Integer id, final byte[] content) {
		String realId = this.parseId(id);
		Session session = null;
		try {
			session = this.repository.login(this.credentials);
			Node root = session.getRootNode();
			Node node = root.addNode(realId);
			Node contentNode = node.addNode(JackRabbitRepository.CONTENT_NODE_NAME, JackRabbitRepository.CONTENT_NODE_TYPE);
			Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(content));
			contentNode.setProperty(JackRabbitRepository.DATA_NODE_PROPERTY, binary);
			session.save();
		} catch (RepositoryException e) {
			throw new ServiceException(e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	private String parseId(final Integer id) {
		return this.digester.digestAsString(id.toString());
	}
}
