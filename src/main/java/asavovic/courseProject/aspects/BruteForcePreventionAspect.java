package asavovic.courseProject.aspects;

import asavovic.courseProject.aspects.util.LoginAttempt;
import asavovic.courseProject.entities.Customer;
import asavovic.courseProject.exceptions.BruteForcePreventionException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class BruteForcePreventionAspect {
    Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();


    @Around("execution(* asavovic.courseProject.controllers.CustomerController.login(..))")
    public Object bruteforcePrevention(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = joinPoint.getArgs()[1];
        HttpServletRequest request = (HttpServletRequest) object;
        String ipAddress = request.getRemoteAddr();

        LoginAttempt loginAttempt = attempts.getOrDefault(ipAddress, new LoginAttempt(1L, LocalDateTime.now().minusSeconds(2)));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime blockedUntilTime = loginAttempt.getTime().plusSeconds(loginAttempt.getNumberOfAttempts());

        if (blockedUntilTime.isAfter(now))
            throw new BruteForcePreventionException("too many requests");
        ResponseEntity retValue = (ResponseEntity) joinPoint.proceed();
        if (retValue.getStatusCode().equals(HttpStatus.OK))
            attempts.remove(ipAddress);
        else
            updateAttempt(ipAddress);
        return retValue;
    }

    public void updateAttempt(String ipAddress) {
        LoginAttempt loginAttempt = attempts.getOrDefault(ipAddress, new LoginAttempt(1L, LocalDateTime.now()));
        loginAttempt.setNumberOfAttempts(loginAttempt.getNumberOfAttempts() * 2);
        loginAttempt.setTime(LocalDateTime.now());
        attempts.put(ipAddress, loginAttempt);
    }

}
