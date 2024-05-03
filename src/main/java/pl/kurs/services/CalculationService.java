package pl.kurs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kurs.dao.IOperationEventDao;
import pl.kurs.dao.OperationEventDao;
import pl.kurs.exceptions.InvalidEquationFormatException;
import pl.kurs.exceptions.UnknownOperatorException;
import pl.kurs.models.events.OperationEvent;
import pl.kurs.models.operator.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class CalculationService implements ICalculationService {

    @Autowired
    private IOperationEventDao operationEventDao;

    @Override
    public List<Object> convertExpressionToPostfixNotation(String expression) throws UnknownOperatorException, InvalidEquationFormatException {
        List<Object> postFixExpression = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        StringBuilder buffer = new StringBuilder();

        operationEventDao.save(new OperationEvent(new Timestamp(System.currentTimeMillis()), expression));

        expression.trim();

        if(!isExpressionValid(expression)) {
            throw new InvalidEquationFormatException("Invalid equation format");
        }

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                buffer.append(ch);
            } else if (getOperator(ch) instanceof IOperator) {
                if(buffer.length() > 0) {
                    postFixExpression.add(Double.parseDouble(buffer.toString()));
                    buffer = new StringBuilder();
                }
                while (!stack.isEmpty() && getOperatorPriorityWeight(stack.peek()) >= getOperatorPriorityWeight(ch)) {

                    postFixExpression.add(getOperator(stack.pop()));
                }
                stack.push(ch);
            }
        }

        if(buffer.length() > 0) {
            postFixExpression.add(Double.parseDouble(buffer.toString()));
        }

        while (!stack.isEmpty()) {
            postFixExpression.add(getOperator(stack.pop()));
        }

        return postFixExpression;
    }

    @Override
    public double calculatePostFixExpression(List<Object> expression) {
        Stack<Double> stack = new Stack<>();

        for (Object obj : expression) {
            if (obj instanceof Double) {
                stack.push((Double) obj);
            } else if (obj instanceof IOperator) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Not enough operands in expression");
                }
                double a = stack.pop();
                double b = stack.pop();
                double result = ((IOperator) obj).calculate(b, a);
                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Not enough operands in expression");
        }

        return stack.pop();
    }

    private boolean isExpressionValid(String expression) {
        if (expression.isEmpty()) {
            return false;
        }

        if (expression.startsWith("+") || expression.startsWith("-") || expression.startsWith("*") || expression.startsWith("/") ||
                expression.endsWith("+") || expression.endsWith("-") || expression.endsWith("*") || expression.endsWith("/")) {
            return false;
        }

        if (expression.matches(".*[a-zA-Z].*")) {
            return false;
        }

        return true;
    }

    private IOperator getOperator(char ch) throws UnknownOperatorException {
        switch (ch) {
            case '+':
                return new Addition();
            case '-':
                return new Subtraction();
            case '*':
                return new Multiplication();
            case '/':
                return new Division();
            default:
                throw new UnknownOperatorException("Unsupported operator " + ch + ". Only + - * / are allowed");

        }
    }

    private int getOperatorPriorityWeight(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}
