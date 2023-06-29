package asavovic.courseProject.aspects;

import asavovic.courseProject.aspects.util.LoginAttempt;
import asavovic.courseProject.exceptions.BruteForcePreventionException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BruteForcePreventionAspectTest {
    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private BruteForcePreventionAspect aspect;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        aspect = new BruteForcePreventionAspect();
        aspect.attempts = new ConcurrentHashMap<>();
    }

    @Test
    public void testLoginAttemptSuccess() throws Throwable {
        String ipAddress = "ip address";

        when(joinPoint.getArgs()).thenReturn(new Object[]{null, request});
        when(request.getRemoteAddr()).thenReturn(ipAddress);
        when(joinPoint.proceed()).thenReturn(new ResponseEntity(HttpStatus.OK));

        assertDoesNotThrow(() -> aspect.bruteforcePrevention(joinPoint));

        verify(joinPoint).proceed();
    }

    @Test
    public void testBlockedLoginAttempt() throws Throwable {
        String ipAddress = "ip address";
        LocalDateTime blockedUntil = LocalDateTime.now().plusSeconds(10);

        aspect.attempts.put(ipAddress, new LoginAttempt(1L, blockedUntil));

        when(joinPoint.getArgs()).thenReturn(new Object[]{null, request});
        when(request.getRemoteAddr()).thenReturn(ipAddress);

        assertThrows(BruteForcePreventionException.class, () -> aspect.bruteforcePrevention(joinPoint));

        verify(joinPoint, never()).proceed();
    }

    @Test
    public void testLoginAttemptSuccessAfterBlockedTimePassed() throws Throwable {
        String ipAddress = "ip address";
        LocalDateTime blockedUntil = LocalDateTime.now().minusSeconds(10);

        aspect.attempts.put(ipAddress, new LoginAttempt(1L, blockedUntil));

        when(joinPoint.getArgs()).thenReturn(new Object[]{null, request});
        when(request.getRemoteAddr()).thenReturn(ipAddress);
        when(joinPoint.proceed()).thenReturn(new ResponseEntity(HttpStatus.OK));

        assertDoesNotThrow(() -> aspect.bruteforcePrevention(joinPoint));

        verify(joinPoint).proceed();
        assertEquals(aspect.attempts.size(), 0);
    }

    @Test
    public void testLoginAttemptFailedAfterBlockedTimePassed() throws Throwable {
        String ipAddress = "ip address";
        LocalDateTime blockedUntil = LocalDateTime.now().minusSeconds(10);

        aspect.attempts.put(ipAddress, new LoginAttempt(1L, blockedUntil));

        when(joinPoint.getArgs()).thenReturn(new Object[]{null, request});
        when(request.getRemoteAddr()).thenReturn(ipAddress);
        when(joinPoint.proceed()).thenReturn(new ResponseEntity(HttpStatus.UNAUTHORIZED));

        assertDoesNotThrow(() -> aspect.bruteforcePrevention(joinPoint));

        verify(joinPoint).proceed();
        assertEquals(aspect.attempts.size(), 1);
        assertEquals(aspect.attempts.get(ipAddress).getNumberOfAttempts(), 2);
    }

}