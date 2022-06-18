package tabletennis.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Organization {

    @Id
    @Column(name = "org_id")
    private long orgId;

    @Column(name = "org_name")
    private String orgName; // (nem lehet üres)

    @Column(name = "org_address")
    private String address; // (nem lehet üres)

    @Column(name = "contact")
    private String contact; // (nem lehet üres, legalább 2 tagból áll)

    @Column(name = "email")
    private String email; // (valid email formátum)

    @Column(name = "tel_number")
    private String telNumber; // (nem kötelező)

    @OneToMany(mappedBy = "organization")
    private List<Player> players; //lista az egyesület igazolt játékosairól (players)
}
