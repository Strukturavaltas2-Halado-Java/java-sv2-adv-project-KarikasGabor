package tabletennis.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tabletennis.model.LicenseType;
import tabletennis.model.Organization;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePlayerCommandForTest {

    private String playerName;
    private LocalDate birthDate;
    private String motherName;
    private Organization organization;
    private LocalDate licenseDate;
    private LocalDate licenseValidityDate;
    private LicenseType licenseType;
}
