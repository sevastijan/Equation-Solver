package pl.kurs.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import pl.kurs.dao.IOperationEventDao;
import pl.kurs.exceptions.InvalidEquationFormatException;
import pl.kurs.exceptions.UnknownOperatorException;
import pl.kurs.models.events.OperationEvent;
import pl.kurs.services.CalculationService;
import pl.kurs.services.ICalculationService;

import java.sql.Timestamp;
import java.util.List;

@ComponentScan(basePackages = "pl.kurs")
public class Application {

    public static void main(String[] args) {
        if(args.length > 0) {
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
            ICalculationService cs = ctx.getBean(ICalculationService.class);

            try {
                List<Object> convertedExpressionToPostfixNotation = cs.convertExpressionToPostfixNotation(args[0]);
                double result = cs.calculatePostFixExpression(convertedExpressionToPostfixNotation);

                System.out.println("Wynik: " + result);
            } catch (UnknownOperatorException e) {
                e.printStackTrace();
            } catch (InvalidEquationFormatException e) {
                e.printStackTrace();
            }

            ctx.close();
        } else {
            System.err.println("Give an arithmetic expression as a parameter");
        }
    }
}
