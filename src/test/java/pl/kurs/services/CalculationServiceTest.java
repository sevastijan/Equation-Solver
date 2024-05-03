package pl.kurs.services;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.kurs.application.Application;
import pl.kurs.exceptions.InvalidEquationFormatException;
import pl.kurs.exceptions.UnknownOperatorException;
import pl.kurs.models.operator.Addition;
import pl.kurs.models.operator.Division;
import pl.kurs.models.operator.Multiplication;
import pl.kurs.models.operator.Subtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CalculationServiceTest {
    ICalculationService calculationService;

    @Before
    public void init() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
        calculationService = ctx.getBean(ICalculationService.class);
//        calculationService = new CalculationService();
    }

    @Test(expected = InvalidEquationFormatException.class)
    public void shouldThrowInvalidEquationFormatExceptionWhenExpressionHaveIllegalChar() throws InvalidEquationFormatException, UnknownOperatorException {
        calculationService.convertExpressionToPostfixNotation("2+a*3");
    }

    @Test(expected = InvalidEquationFormatException.class)
    public void shouldThrowInvalidEquationFormatExceptionWhenExpressionIsEmpty() throws InvalidEquationFormatException, UnknownOperatorException {
        calculationService.convertExpressionToPostfixNotation("");
    }

    @Test(expected = InvalidEquationFormatException.class)
    public void shouldThrowInvalidEquationFormatExceptionWhenExpressionStartWithOperator() throws InvalidEquationFormatException, UnknownOperatorException {
        calculationService.convertExpressionToPostfixNotation("+2-3");
    }

    @Test(expected = InvalidEquationFormatException.class)
    public void shouldThrowInvalidEquationFormatExceptionWhenExpressionEndWithOperator() throws InvalidEquationFormatException, UnknownOperatorException {
        calculationService.convertExpressionToPostfixNotation("2-3*");
    }


    @Test(expected = UnknownOperatorException.class)
    public void shouldThrowUnknownOperatorExceptionWhenExpressionHaveIllegalOperator() throws InvalidEquationFormatException, UnknownOperatorException {
        calculationService.convertExpressionToPostfixNotation("2+2^3");
    }

    @Test
    public void shouldReturnOneValueWhenPassSingleNumber() throws UnknownOperatorException, InvalidEquationFormatException {
        List<Object> result = calculationService.convertExpressionToPostfixNotation("123");
        assertEquals("[123.0]", result.toString());
    }

    @Test
    public void shouldReturnCorrectPostFixListFromComplexEquation() throws UnknownOperatorException, InvalidEquationFormatException {
        List<Object> result = calculationService.convertExpressionToPostfixNotation("55*3+7*2+3*5-1+6/2");

        assertEquals(17, result.size());
        assertEquals(55.0, result.get(0) );
        assertEquals(3.0, result.get(1) );
        assertTrue(result.get(2) instanceof Multiplication);
        assertEquals(7.0,  result.get(3));
        assertEquals(2.0,  result.get(4));
        assertTrue(result.get(5) instanceof Multiplication);
        assertTrue(result.get(6) instanceof Addition);
        assertEquals(3.0,  result.get(7));
        assertEquals(5.0,  result.get(8));
        assertTrue(result.get(9) instanceof Multiplication);
        assertTrue(result.get(10) instanceof Addition);
        assertEquals(1.0,  result.get(11));
        assertTrue(result.get(12) instanceof Subtraction);
        assertEquals(6.0,  result.get(13));
        assertEquals(2.0,  result.get(14));
        assertTrue(result.get(15) instanceof Division);
        assertTrue(result.get(16) instanceof Addition);
    }

    @Test
    public void shouldReturnCorrectPostFixListFromSimplyEquation() throws UnknownOperatorException, InvalidEquationFormatException {
        List<Object> result = calculationService.convertExpressionToPostfixNotation("2+2*2");

        assertEquals(5, result.size());
        assertEquals(2.0, result.get(0));
        assertEquals(2.0, result.get(1));
        assertEquals(2.0,  result.get(2));
        assertTrue(result.get(3) instanceof Multiplication);
        assertTrue(result.get(4) instanceof Addition);
    }

    @Test
    public void shouldReturnCorrectValueInSimplyExpression() {
        List<Object> expression = new ArrayList<>(Arrays.asList(2.0, 2.0, new Addition()));

        double result = calculationService.calculatePostFixExpression(expression);

        assertEquals(4.0, result, 0.0);
    }

    @Test
    public void shouldReturnCorrectValueInComplexExpression() {
        List<Object> expression = new ArrayList<>(Arrays.asList(
                55.0,
                3.0,
                new Multiplication(),
                7.0,
                2.0,
                new Multiplication(),
                new Addition(),
                3.0,
                5.0,
                new Multiplication(),
                new Addition(),
                1.0,
                new Subtraction(),
                6.0,
                new Addition()
            )
        );
        double result = calculationService.calculatePostFixExpression(expression);

        assertEquals(199, result, 0.0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenIsNotEnoughOperandsInExpression() {
        List<Object> expression = new ArrayList<>(Arrays.asList(
                2.0,
                new Addition()
        ));
        calculationService.calculatePostFixExpression(expression);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenIsNotEnoughOperatorsInExpression() {
        List<Object> expression = new ArrayList<>(Arrays.asList(
                2.0,
                2.0
        ));
        calculationService.calculatePostFixExpression(expression);
    }

}