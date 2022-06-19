package tabletennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tabletennis.model.LicenseType;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PlayerDto {

    private Long playerId;
    private String playerName;
    private LocalDate birthDate;
    private String motherName;
    private OrganizationListDto organization;
    private LocalDate licenseDate;
    private LocalDate licenseValidityDate;
    private LicenseType licenseType;
}
