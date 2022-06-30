package tabletennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tabletennis.validation.Name;

import javax.validation.constraints.Past;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePlayerCommand {

    @Name
    private String playerName;
    @Past(message = "The birthday must be a past date")
    private LocalDate birthDate;
    @Name
    private String motherName;

}
