package tabletennis.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "organizations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "org_address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Column(name = "email")
    private String email;

    @Column(name = "tel_number")
    private String telNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "organization")
    private List<Player> players;

    public Organization(String orgName, String address, String contact, String email, String telNumber) {
        this.orgName = orgName;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.telNumber = telNumber;
    }
}
