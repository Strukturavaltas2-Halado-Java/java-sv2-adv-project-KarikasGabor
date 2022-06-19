package tabletennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tabletennis.model.Player;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrganizationDto {

    private long orgId;
    private String orgName;
    private String address;
    private String contact;
    private String email;
    private String telNumber;
    private List<Player> players;
}
