package pl.kurs.services;

import pl.kurs.exceptions.InvalidEquationFormatException;
import pl.kurs.exceptions.UnknownOperatorException;
import java.util.List;

public interface ICalculationService {
    List<Object> convertExpressionToPostfixNotation(String expression) throws UnknownOperatorException, InvalidEquationFormatException;
    double calculatePostFixExpression(List<Object> expression);
}
