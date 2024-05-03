package pl.kurs.models.events;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class OperationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private Timestamp date;
    private String expression;

    public OperationEvent(Timestamp date, String expression) {
        this.date = date;
        this.expression = expression;
    }

    public OperationEvent() {
    }
}
