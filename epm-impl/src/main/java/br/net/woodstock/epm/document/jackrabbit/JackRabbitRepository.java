package br.net.woodstock.epm.document.jackrabbit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.VersionManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.core.jndi.RegistryHelper;

import br.net.woodstock.epm.acl.api.User;
import br.net.woodstock.epm.document.api.Document;
import br.net.woodstock.epm.document.api.DocumentResultContainer;
import br.net.woodstock.epm.util.Page;
import br.net.woodstock.rockframework.security.store.StoreException;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.CollectionUtils;
import br.net.woodstock.rockframework.utils.IOUtils;

class JackRabbitRepository implements br.net.woodstock.rockframework.persistence.Repository {

	private static final long	serialVersionUID	= -7290717732247236271L;

	private static final int	DEFAULT_PAGE_SIZE	= 10;

	private static final int	DEFAULT_PAGE_NUMBER	= 0;

	private static final String	REPOSITORY_USER		= "username";

	private static final char[]	REPOSITORY_PASSWORD	= "password".toCharArray();

	private Credentials			credentials			= new SimpleCredentials(JackRabbitRepository.REPOSITORY_USER, JackRabbitRepository.REPOSITORY_PASSWORD);

	private Repository			repository;

	public JackRabbitRepository(final String jndiName, final String home) {
		super();
		Assert.notEmpty(jndiName, "jndiName");
		Assert.notEmpty(home, "home");
		this.init(jndiName, home);
	}

	private void init(final String jndiName, final String home) {
		try {
			URL url = this.getClass().getClassLoader().getResource(JackRabbitHelper.REPOSITORY_SETTINGS);
			Hashtable<String, String> table = new Hashtable<String, String>();
			table.put(Context.PROVIDER_URL, JackRabbitHelper.CONTEXT_PROVIDER_URL);
			table.put(Context.INITIAL_CONTEXT_FACTORY, JackRabbitHelper.CONTEXT_FACTORY);
			Context context = new InitialContext(table);
			RegistryHelper.registerRepository(context, jndiName, url.getFile(), home, true);

			this.repository = (Repository) context.lookup(jndiName);
		} catch (NamingException e) {
			throw new StoreException(e);
		} catch (RepositoryException e) {
			throw new StoreException(e);
		}
	}

	public Document get(final String id) {
		Document d = this.execute(new JackRabbitCallback() {

			@Override
			public Object execute(final JackrabbitSession session) throws RepositoryException {
				String realId = JackRabbitHelper.parseId(id);
				Node root = session.getRootNode();
				Node node = root.getNode(realId);
				if (node != null) {
					Document document = new Document();
					Node content = node.getNode(JackRabbitHelper.NODE_NAME_CONTENT);
					JackRabbitRepository.this.toDocument(node, content, document);
					return document;
				}
				return null;
			}
		});
		return d;
	}

	public boolean remove(final String id) {
		Boolean b = this.execute(new JackRabbitCallback() {

			@Override
			public Object execute(final JackrabbitSession session) throws RepositoryException {
				String realId = JackRabbitHelper.parseId(id);
				Node root = session.getRootNode();
				Node node = root.getNode(realId);
				boolean ok = false;
				if (node != null) {
					node.remove();
					ok = true;
				}
				session.save();
				return Boolean.valueOf(ok);
			}
		});
		return b.booleanValue();
	}

	public void save(final Document document) {
		this.execute(new JackRabbitCallback() {

			@Override
			public Void execute(final JackrabbitSession session) throws RepositoryException {
				String realId = JackRabbitHelper.parseId(document.getId());
				Node root = session.getRootNode();
				Node node = root.addNode(realId);
				Node content = node.addNode(JackRabbitHelper.NODE_NAME_CONTENT, JackRabbitHelper.NOTE_TYPE_RESOURCE);

				JackRabbitRepository.this.toNode(document, node, content, session);

				session.save();
				return null;
			}
		});
	}

	public DocumentResultContainer search(final String filter, final Page page) {
		DocumentResultContainer c = this.execute(new JackRabbitCallback() {

			@Override
			public Object execute(final JackrabbitSession session) throws RepositoryException {
				Workspace workspace = session.getWorkspace();
				QueryManager queryManager = workspace.getQueryManager();
				Query query = queryManager.createQuery(filter, Query.JCR_SQL2);

				int total = 0;
				int pageSize = JackRabbitRepository.DEFAULT_PAGE_SIZE;
				int pageNumber = JackRabbitRepository.DEFAULT_PAGE_NUMBER;

				if (page != null) {
					pageNumber = page.getPageNumber();
					pageSize = page.getPageSize();
				}

				query.setLimit(pageSize);
				query.setOffset(pageNumber * pageSize);

				QueryResult result = query.execute();
				NodeIterator iterator = result.getNodes();
				List<Document> list = new ArrayList<Document>();
				while (iterator.hasNext()) {
					Node node = iterator.nextNode();
					Node content = node.getNode(JackRabbitHelper.NODE_NAME_CONTENT);
					Document document = new Document();
					JackRabbitRepository.this.toDocument(node, content, document);
					list.add(document);
				}

				DocumentResultContainer container = new DocumentResultContainer(total, CollectionUtils.toArray(list, Document.class), page);
				return container;
			}
		});
		return c;
	}

	public boolean update(final Document document) {
		Boolean b = this.execute(new JackRabbitCallback() {

			@Override
			public Object execute(final JackrabbitSession session) throws RepositoryException {
				String realId = JackRabbitHelper.parseId(document.getId());
				Node root = session.getRootNode();
				Node node = root.getNode(realId);
				if (node != null) {
					Workspace workspace = session.getWorkspace();
					VersionManager versionManager = workspace.getVersionManager();

					Node content = node.getNode(JackRabbitHelper.NODE_NAME_CONTENT);

					versionManager.checkout(node.getIdentifier());
					versionManager.checkout(content.getIdentifier());

					JackRabbitRepository.this.toNode(document, node, content, session);

					session.save();
					versionManager.checkin(node.getIdentifier());
					versionManager.checkin(content.getIdentifier());

				}
				return Boolean.FALSE;
			}
		});
		return b.booleanValue();
	}

	protected <T> T execute(final JackRabbitCallback callback) {
		return new JackRabbitTemplate(this.credentials, this.repository).doInSession(callback);
	}

	protected void toDocument(final Node node, final Node content, final Document document) throws RepositoryException {
		document.setCreated(node.getProperty(JackRabbitHelper.NODE_NAME_EPM_CREATED).getDate().getTime());
		document.setId(node.getProperty(JackRabbitHelper.NODE_NAME_EPM_ID).getString());
		document.setMimeType(node.getProperty(JackRabbitHelper.NODE_NAME_EPM_MIME_TYPE).getString());
		document.setModified(node.getProperty(JackRabbitHelper.NODE_NAME_EPM_MODIFIED).getDate().getTime());
		document.setName(node.getProperty(JackRabbitHelper.NODE_NAME_EPM_NAME).getString());
		document.setOwner(new User(node.getProperty(JackRabbitHelper.NODE_NAME_EPM_OWNER).getString()));
		document.setText(node.getProperty(JackRabbitHelper.NODE_NAME_EPM_TEXT).getString());

		Property property = content.getProperty(JackRabbitHelper.NODE_NAME_DATA);
		if (property != null) {
			Binary binary = property.getBinary();
			try {
				byte[] bytes = IOUtils.toByteArray(binary.getStream());
				document.setBinary(bytes);
			} catch (IOException e) {
				throw new RepositoryException(e);
			}

		}
	}

	protected void toNode(final Document document, final Node node, final Node content, final Session session) throws RepositoryException {
		node.setProperty(JackRabbitHelper.NODE_NAME_EPM_CREATED, JackRabbitHelper.toCalendar(document.getCreated()));
		node.setProperty(JackRabbitHelper.NODE_NAME_EPM_ID, document.getId());
		node.setProperty(JackRabbitHelper.NODE_NAME_EPM_MIME_TYPE, document.getMimeType());
		node.setProperty(JackRabbitHelper.NODE_NAME_EPM_MODIFIED, JackRabbitHelper.toCalendar(document.getModified()));
		node.setProperty(JackRabbitHelper.NODE_NAME_EPM_NAME, document.getName());
		node.setProperty(JackRabbitHelper.NODE_NAME_EPM_OWNER, document.getOwner().getId());
		node.setProperty(JackRabbitHelper.NODE_NAME_EPM_TEXT, document.getText());

		Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(document.getBinary()));
		content.setProperty(JackRabbitHelper.NODE_NAME_DATA, binary);
		content.setProperty(JackRabbitHelper.NODE_NAME_LAST_MODIFIED, document.getModified().getTime());
		content.setProperty(JackRabbitHelper.NODE_NAME_MIME_TYPE, document.getMimeType());
	}
}
