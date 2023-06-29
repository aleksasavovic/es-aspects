package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.Product;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
@Order(1)
public class MethodsCalledCounterAspect{
    private static final Logger logger = Logger.getLogger(MethodsCalledCounterAspect.class.getName());
    private AtomicInteger getProductsCounter = new AtomicInteger(0);
    private AtomicInteger checkoutCounter = new AtomicInteger(0);

    @AfterReturning("execution(* asavovic.courseProject.controllers.ProductController.getAllProducts(..))")
    public void getAllProducts(JoinPoint joinPoint) throws Throwable {
        int numberOfCalls = getProductsCounter.incrementAndGet();
        logger.log(Level.INFO,"Get all products method was successfully executed :{0} times ", getProductsCounter);
    }

    @AfterReturning("execution(* asavovic.courseProject.controllers.CartController.checkout(..))")
    public void checkout(JoinPoint joinPoint) throws Throwable {
        int numberOfCalls = checkoutCounter.incrementAndGet();
        logger.log(Level.INFO,"checkout method was successfully executed :{0} times ", checkoutCounter);
    }
}
