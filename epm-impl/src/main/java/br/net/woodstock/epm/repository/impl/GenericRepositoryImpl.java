package br.net.woodstock.epm.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.rockframework.domain.Entity;
import br.net.woodstock.rockframework.persistence.orm.GenericRepository;
import br.net.woodstock.rockframework.persistence.orm.impl.JPAGenericRepository;

@Repository
public class GenericRepositoryImpl implements GenericRepository {

	private static final long	serialVersionUID	= -6146536936263260688L;

	@PersistenceContext(name = PersistenceHelper.PERSISTENCE_UNIT)
	private EntityManager		entityManager;

	public GenericRepositoryImpl() {
		super();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(final Entity<?> e) {
		new JPAGenericRepository(this.entityManager).delete(e);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <E extends Entity<?>> E get(final E entity) {
		return new JPAGenericRepository(this.entityManager).get(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(final Entity<?> e) {
		new JPAGenericRepository(this.entityManager).save(e);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(final Entity<?> e) {
		new JPAGenericRepository(this.entityManager).update(e);
	}

}
