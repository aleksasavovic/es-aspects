package asavovic.courseProject.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;
@Aspect
@Component
@Order(0)
public class GetAllProductsPerformanceAspect {
    private static final Logger logger = Logger.getLogger(GetAllProductsPerformanceAspect.class.getName());
    @Around("execution(* asavovic.courseProject.controllers.ProductController.getAllProducts(..))")
    public void getAllProductsPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        logger.log(Level.INFO, "Execution time of getAllProducts() is : {0}ms", executionTime);
    }
}
