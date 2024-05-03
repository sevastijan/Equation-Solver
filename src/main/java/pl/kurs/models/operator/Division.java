package pl.kurs.models.operator;

public class Division implements IOperator {
    @Override
    public double calculate(double a, double b) {
        if(b == 0) throw new ArithmeticException("Division by zero!");

        return a/b;
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
