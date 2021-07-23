package com.example.reactchesslive.db.repo.h2.impl;

import com.example.reactchesslive.db.repo.AbstractRepo;
import com.example.reactchesslive.model.entity.AbstractEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Transactional(transactionManager = "h2TransactionManager")
public abstract class H2AbstractRepoImpl<T extends AbstractEntity> implements AbstractRepo<T> {

    private final Class<T> clazz;

    protected EntityManager entityManager;

    public H2AbstractRepoImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void save(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(T entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            entityManager.remove(entityManager.merge(entity));
        }
    }

    @Override
    public T merge(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void deleteById(Object id) {
        var entity = entityManager.find(clazz, id);
        entityManager.remove(entity);
    }

    @Override
    public Optional<T> findById(Object id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return (List<T>) entityManager.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e").getResultList();
    }

    @PersistenceContext(unitName = "H2PersistenceUnit")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}