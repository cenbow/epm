package br.net.woodstock.epm.repository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.rockframework.domain.Entity;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMFilter;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.domain.persistence.orm.impl.AbstractJPARepository;

@Repository
public class ORMRepositoryImpl extends AbstractJPARepository {

	private static final long	serialVersionUID	= -6146536936263260688L;

	public static final String	PERSISTENCE_UNIT	= "epmPU";

	@PersistenceContext(name = ORMRepositoryImpl.PERSISTENCE_UNIT, unitName = ORMRepositoryImpl.PERSISTENCE_UNIT)
	private EntityManager		entityManager;

	public ORMRepositoryImpl() {
		super();
	}

	@Override
	@SuppressWarnings("rawtypes")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(final Entity e) {
		super.save(e);
	}

	@Override
	@SuppressWarnings("rawtypes")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(final Entity e) {
		super.update(e);
	}

	@Override
	@SuppressWarnings("rawtypes")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(final Entity e) {
		super.delete(e);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <E extends Entity<T>, T extends Serializable> E get(final Class<E> clazz, final T id) {
		return super.get(clazz, id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <E> E getSingle(final ORMFilter filter) {
		return super.getSingle(filter);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ORMResult getCollection(final ORMFilter filter) {
		return super.getCollection(filter);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void executeUpdate(final ORMFilter filter) {
		super.executeUpdate(filter);
	}

	@Override
	protected EntityManager getEntityManager() {
		return this.entityManager;
	}

}
