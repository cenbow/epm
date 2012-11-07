package br.net.woodstock.epm.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.rockframework.persistence.orm.QueryMetadata;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;
import br.net.woodstock.rockframework.persistence.orm.QueryableRepository;
import br.net.woodstock.rockframework.persistence.orm.impl.JPAQueryableRepository;

@Repository
public class QueryableRepositoryImpl implements QueryableRepository {

	private static final long	serialVersionUID	= -3139155797801102518L;

	@PersistenceContext(name = PersistenceHelper.PERSISTENCE_UNIT)
	private EntityManager		entityManager;

	public QueryableRepositoryImpl() {
		super();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <E> E getSingle(final QueryMetadata query) {
		return new JPAQueryableRepository(this.entityManager).getSingle(query);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public QueryResult getCollection(final QueryMetadata query) {
		return new JPAQueryableRepository(this.entityManager).getCollection(query);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void executeUpdate(final QueryMetadata query) {
		new JPAQueryableRepository(this.entityManager).executeUpdate(query);
	}

}