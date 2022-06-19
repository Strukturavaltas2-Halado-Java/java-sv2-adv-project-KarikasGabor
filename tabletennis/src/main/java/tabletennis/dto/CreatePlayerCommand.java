package tabletennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tabletennis.model.LicenseType;
import tabletennis.model.Organization;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePlayerCommand {

    private String playerName;
    private LocalDate birthDate;
    private String motherName;
    private Organization organization;
    private LocalDate licenseDate;
    private LocalDate licenseValidityDate;
    private LicenseType licenseType;
}
