package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.Product;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CachingAspectTest {
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private JoinPoint joinPoint;
    @InjectMocks
    private CachingAspect cachingAspect;
    Set<Product> allProducts;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Chocolate");
        product1.setQuantity(10L);
        product1.setPrice(100);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Eggs");
        product2.setQuantity(5L);
        product2.setPrice(50);

        allProducts = new HashSet<>();
        allProducts.add(product1);
        allProducts.add(product2);
    }

    @Test
    public void cacheProducts() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn(allProducts);

        cachingAspect.getAllProducts(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    public void cacheProductsMultipleTimes() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn(allProducts);

        cachingAspect.getAllProducts(proceedingJoinPoint);
        cachingAspect.getAllProducts(proceedingJoinPoint);
        cachingAspect.getAllProducts(proceedingJoinPoint);
        cachingAspect.getAllProducts(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    public void removeCachedProducts() throws Throwable {
        cachingAspect.removeCachedProducts(joinPoint);
        assertEquals(cachingAspect.getCachedProducts().size(),0);
    }

    @Test
    public void testCombination() throws Throwable{
        when(proceedingJoinPoint.proceed()).thenReturn(allProducts);


        cachingAspect.getAllProducts(proceedingJoinPoint);
        cachingAspect.getAllProducts(proceedingJoinPoint);
        cachingAspect.getAllProducts(proceedingJoinPoint);

        cachingAspect.removeCachedProducts(joinPoint);

        cachingAspect.getAllProducts(proceedingJoinPoint);
        cachingAspect.getAllProducts(proceedingJoinPoint);
        cachingAspect.getAllProducts(proceedingJoinPoint);

        cachingAspect.removeCachedProducts(joinPoint);

        assertEquals(cachingAspect.getCachedProducts().size(),0);
        verify(proceedingJoinPoint, times(2)).proceed();
    }
}