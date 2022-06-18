package tabletennis.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "player_id")
    private long playerId;

    @Column(name = "player_name")
    private String playerName; //(nem lehet üres, legalább 2 tagból áll)

    @Column(name = "player_birthdate")
    private LocalDate birthDate; // (nem lehet későbbi az aktuális dátumnál)

    @Column(name = "player_mother")
    private String motherName; // (nem lehet üres, legalább 2 tagból áll)

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Column(name = "license_date")
    private LocalDate licenseDate; //(leigazolás/átigazolás dátuma)

    @Column(name = "license_validity_date")
    private LocalDate licenseValidityDate; //játékengedély érvényessége

    @Column(name = "license_type")
    @Enumerated(EnumType.STRING)
    private LicenseType licenseType; //(ez lehet TELJESKÖRŰ vagy EGYÉNI)
}
