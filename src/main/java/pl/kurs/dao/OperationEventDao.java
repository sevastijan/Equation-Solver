package pl.kurs.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.kurs.models.events.OperationEvent;

@Repository
public class OperationEventDao implements IOperationEventDao{

    @Autowired
    private EntityManagerFactory factory;

    @Override
    public void save(OperationEvent operationEvent) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        entityManager.persist(operationEvent);

        tx.commit();
        entityManager.close();
    }
}
