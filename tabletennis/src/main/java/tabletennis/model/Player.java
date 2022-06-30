package tabletennis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "players")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "player_birthdate")
    private LocalDate birthDate;

    @Column(name = "player_mother")
    private String motherName;

    @ManyToOne
    @JoinColumn(name = "org_id")
    @JsonBackReference
    private Organization organization;

    @Column(name = "license_date")
    private LocalDate licenseDate;

    @Column(name = "license_validity_date")
    private LocalDate licenseValidityDate;

    @Column(name = "license_type")
    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    public Player(String playerName, LocalDate birthDate, String motherName) {
        this.playerName = playerName;
        this.birthDate = birthDate;
        this.motherName = motherName;
    }

    public void setValidity() {
        if (LocalDate.now().getMonthValue() > 6) {
            this.setLicenseType(LicenseType.FULL);
            this.setLicenseValidityDate(LocalDate.of(LocalDate.now().getYear() + 1, 6, 30));
        } else {
            this.setLicenseType(LicenseType.INDIVIDUAL);
            this.setLicenseValidityDate(LocalDate.of(LocalDate.now().getYear(), 6, 30));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return getPlayerName().equals(player.getPlayerName()) && getBirthDate().equals(player.getBirthDate()) && getMotherName().equals(player.getMotherName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayerName(), getBirthDate(), getMotherName());
    }
}
