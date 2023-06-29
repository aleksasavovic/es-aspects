package asavovic.courseProject.aspects;

import asavovic.courseProject.exceptions.ResourceNotFoundException;
import asavovic.courseProject.repositories.SessionRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SessionCheckAspect {
    private final SessionRepository sessionRepository;

    public SessionCheckAspect(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Around("execution(* asavovic.courseProject.controllers.CartController.*(..))")
    public Object checkSession(ProceedingJoinPoint joinPoint) throws Throwable {
        Object sessionId = joinPoint.getArgs()[0];
        if (sessionId != null) {
            Long id = (long) sessionId;
            isSessionValid(id);
            return joinPoint.proceed();
        }
        else throw new IllegalArgumentException("sessionId cant be null");
    }

    private boolean isSessionValid(Long sessionId) {
        sessionRepository.findById(sessionId).orElseThrow(() -> new ResourceNotFoundException("ASPECT::: session not found"));
        return true;
    }
}