package pl.kurs.models.operator;

public class Addition implements IOperator {

    @Override
    public double calculate(double a, double b) {
        return a + b;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
