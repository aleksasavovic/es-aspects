package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {


    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Before("execution(* asavovic.courseProject.controllers.CustomerController.register(..))")
    public void logBeforeRegisterUser(JoinPoint joinPoint) {
        Object object = joinPoint.getArgs()[0];
        Customer request = (Customer) object;
        logger.log(Level.INFO, "Registration has been attempted with email :{0} ", request.getEmail());
    }

    @AfterReturning(pointcut = "execution(* asavovic.courseProject.controllers.CustomerController.register(..))", returning = "result")
    public void logRegisterUserOutcome(JoinPoint joinPoint, ResponseEntity<Void> result){
        Object object = joinPoint.getArgs()[0];
        Customer request = (Customer) object;
        if(result.getStatusCode().equals(HttpStatus.OK))
            logger.log(Level.INFO,"Registration for user with email :{0} has succeeded",request.getEmail());
        else
            logger.log(Level.INFO,"Registration for user with email :{0} has failed",request.getEmail());
    }


}
