package asavovic.courseProject.aspects.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt {
    private Long numberOfAttempts;
    private LocalDateTime time;

}
