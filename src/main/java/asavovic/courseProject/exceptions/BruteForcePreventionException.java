package asavovic.courseProject.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class BruteForcePreventionException extends RuntimeException {
    public BruteForcePreventionException(String msg) {
        super(msg);
    }
}
