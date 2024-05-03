package pl.kurs.dao;

import pl.kurs.models.events.OperationEvent;

public interface IOperationEventDao {
    void save(OperationEvent operationEvent);
}
