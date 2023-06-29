package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.dto.ProductToAdd;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AddProductValidationAspectTest {
    @Mock
    private JoinPoint joinPoint;
    @InjectMocks
    private AddProductValidationAspect aspect;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddingValidProduct(){
        ProductToAdd productToAdd = new ProductToAdd();
        productToAdd.setAmountToAdd(10L);
        productToAdd.setId(1L);

        Object[] arguments = new Object[]{1, productToAdd};

        when(joinPoint.getArgs()).thenReturn(arguments);

        assertDoesNotThrow(()->aspect.validatieProductsToAdd(joinPoint));
    }

    @Test
    public void testAddProductNegativeAmount(){
        ProductToAdd productToAdd = new ProductToAdd();
        productToAdd.setAmountToAdd(-10L);
        productToAdd.setId(1L);

        Object[] arguments = new Object[]{1, productToAdd};

        when(joinPoint.getArgs()).thenReturn(arguments);

        assertThrows(IllegalArgumentException.class, ()->aspect.validatieProductsToAdd(joinPoint));
    }

    @Test
    public void testAddProductNegativeId(){
        ProductToAdd productToAdd = new ProductToAdd();
        productToAdd.setAmountToAdd(10L);
        productToAdd.setId(-1L);

        Object[] arguments = new Object[]{1, productToAdd};

        when(joinPoint.getArgs()).thenReturn(arguments);

        assertThrows(IllegalArgumentException.class, ()->aspect.validatieProductsToAdd(joinPoint));
    }

    @Test
    public void testAddProductNullValues(){
        ProductToAdd productToAdd = null;

        Object[] arguments = new Object[]{1, productToAdd};

        when(joinPoint.getArgs()).thenReturn(arguments);

        assertThrows(IllegalArgumentException.class, ()->aspect.validatieProductsToAdd(joinPoint));
    }
}