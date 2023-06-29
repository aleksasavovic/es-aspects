package asavovic.courseProject.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class GetAllProductsPerformanceAspectTest {
    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private BruteForcePreventionAspect aspect;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}