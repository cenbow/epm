package br.net.woodstock.epm.document.jackrabbit;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.JackrabbitSession;

import br.net.woodstock.rockframework.security.store.StoreException;

public class JackRabbitTemplate {

	private Credentials	credentials;

	private Repository	repository;

	public JackRabbitTemplate(final Credentials credentials, final Repository repository) {
		super();
		this.credentials = credentials;
		this.repository = repository;
	}

	@SuppressWarnings("unchecked")
	public <T> T doInSession(final JackRabbitCallback callback) {
		JackrabbitSession session = null;
		try {
			session = (JackrabbitSession) this.repository.login(this.credentials);
			return (T) callback.execute(session);
		} catch (RepositoryException e) {
			throw new StoreException(e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

}
