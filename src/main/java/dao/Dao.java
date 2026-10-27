package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/** CRUD genérico para cualquier entidad. T = la entidad, K = el tipo de su id. */
abstract class Dao<T, K> {
    EntityManager em = Persistence
            .createEntityManagerFactory("default")
            .createEntityManager();

    public EntityManager getEntityManager() {
        return this.em;
    }

    public abstract T find(K id);

    public T create(T t) {
        EntityTransaction entityTransaction = em.getTransaction();
        try {
            entityTransaction.begin();
            em.persist(t);
            entityTransaction.commit();
            return t;
        } catch (RuntimeException e) {
            deshacer(entityTransaction);
            throw e;
        }
    }

    public T update(T t) {
        EntityTransaction entityTransaction = em.getTransaction();
        try {
            entityTransaction.begin();
            em.merge(t);
            entityTransaction.commit();
            return t;
        } catch (RuntimeException e) {
            deshacer(entityTransaction);
            throw e;
        }
    }

    public void delete(T t) {
        EntityTransaction entityTransaction = em.getTransaction();
        try {
            entityTransaction.begin();
            em.remove(t);
            entityTransaction.commit();
        } catch (RuntimeException e) {
            deshacer(entityTransaction);
            throw e;
        }
    }

    /**
     * Si una operación falla, la transacción se queda abierta y la siguiente daría
     * "Transaction already active". Se deshace y se vacía el EntityManager para poder seguir.
     */
    private void deshacer(EntityTransaction entityTransaction) {
        if (entityTransaction.isActive()) {
            entityTransaction.rollback();
        }
        em.clear();
    }
}