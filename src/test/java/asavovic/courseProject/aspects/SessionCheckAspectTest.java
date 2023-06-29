package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.Session;
import asavovic.courseProject.exceptions.ResourceNotFoundException;
import asavovic.courseProject.repositories.SessionRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class SessionCheckAspectTest {
    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionCheckAspect aspect;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void checkValidSession() throws Throwable {
        Long sessionId = 1L;
        Session session = new Session();
        session.setSessionId(sessionId);
        Object[] arguments = new Object[]{1L};
        when(joinPoint.getArgs()).thenReturn(arguments);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        aspect.checkSession(joinPoint);

        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void getSessionByIdNotFound() {
        Long sessionId = 1L;
        Object[] arguments = new Object[]{1L};

        when(joinPoint.getArgs()).thenReturn(arguments);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aspect.checkSession(joinPoint));
        verify(sessionRepository, times(1)).findById(sessionId);
    }
}