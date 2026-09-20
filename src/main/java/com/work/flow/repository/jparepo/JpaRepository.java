package com.work.flow.repository.jparepo;

/*
EntityManager:
هو اساس قاعدة البينات الي بدك تشتغل عليها ولي هي (jpa)
اعتبره الشخص بين كود الجافا وعمليات sql


 */

import com.work.flow.exception.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public abstract class JpaRepository<T, ID> implements Repository<T, ID> {

    //Dependency Injection
    private final EntityManager entityManager;
    private final Class<T> entityClass;

    protected JpaRepository(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    @Override
    public T save(T entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            if (entityManager.contains(entity)) {
                entity = entityManager.merge(entity);
            } else {
                entityManager.persist(entity);
            }
            tx.commit();
            return entity;
        } catch (RuntimeException e) {
            tx.rollback();
            throw new DatabaseException("Entity save failed.", e);
        }
    }

    public void saveAll(List<T> entities) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            entities.forEach(entityManager::merge);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw new DatabaseException("Entities save failed.", e);
        }
    }

    /*
    فكرة انه ممكن الاستعلام يرجع قيمة null
    Optional<T> نستعمل هاي الطريقة
    بصير يرجعلنا
    Optional.empty()
    وما يصير nullpointerEX
    */
    @Override
    public Optional<T> findById(ID id) {
        T entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findAll() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return entityManager.createQuery(jpql, entityClass).getResultList();
    }

    @Override
    public void deleteById(ID id) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            T entity = entityManager.find(entityClass, id);
            if (entity != null) {
                entityManager.remove(entity);
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw new DatabaseException("Entities save failed.", e);
        }
    }
}
