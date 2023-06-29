package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.Product;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class MethodsCalledCounterAspectTest {
    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private MethodsCalledCounterAspect aspect;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testMethodsCalledCounter() throws Throwable {
        aspect.getAllProducts(joinPoint);
        aspect.getAllProducts(joinPoint);
        aspect.getAllProducts(joinPoint);

        aspect.checkout(joinPoint);
        aspect.checkout(joinPoint);
        Field getProductsCounterField = aspect.getClass().getDeclaredField("getProductsCounter");
        getProductsCounterField.setAccessible(true);
        AtomicInteger getProductsCounter = (AtomicInteger) getProductsCounterField.get(aspect);

        Field checkoutCounterField = aspect.getClass().getDeclaredField("checkoutCounter");
        checkoutCounterField.setAccessible(true);
        AtomicInteger checkoutCounter = (AtomicInteger) checkoutCounterField.get(aspect);
        assertEquals(checkoutCounter.get(), 2);
        assertEquals(getProductsCounter.get(), 3);

    }
}