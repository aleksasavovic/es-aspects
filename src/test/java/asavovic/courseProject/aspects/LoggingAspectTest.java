package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.Customer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class LoggingAspectTest {
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private ResponseEntity responseEntity;

    @InjectMocks
    private LoggingAspect aspect;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLogBeforeRegistration(){
        Customer customer = new Customer();
        customer.setEmail("test@example.com");

        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{customer});

        aspect.logBeforeRegisterUser(joinPoint);

    }

    @Test
    void testRegistrationSuccessful(){
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{customer});

        aspect.logRegisterUserOutcome(joinPoint, responseEntity);
    }
    @Test
    void testRegistrationNotSuccessful(){
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.CONFLICT);
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{customer});

        aspect.logRegisterUserOutcome(joinPoint, responseEntity);
    }

}