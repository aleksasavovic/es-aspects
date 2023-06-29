package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.Product;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
@Component
@Order(2)
public class CachingAspect {
    private static final String KEY = "all products";
    private Map<String, Set<Product>> cachedProducts = new ConcurrentHashMap<>();

    @Around("execution(* asavovic.courseProject.controllers.ProductController.getAllProducts(..))")
    public Set<Product> getAllProducts(ProceedingJoinPoint joinPoint) throws Throwable {

        return cachedProducts.computeIfAbsent(KEY, v -> {
            try {
                Set<Product> products = (Set<Product>) joinPoint.proceed();
                return products;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });

    }

    @AfterReturning("execution(* asavovic.courseProject.controllers.CartController.checkout(..))")
    public void removeCachedProducts(JoinPoint joinPoint) throws Throwable {
        cachedProducts.clear();
    }

    public Map<String, Set<Product>> getCachedProducts() {
        return cachedProducts;
    }
}
