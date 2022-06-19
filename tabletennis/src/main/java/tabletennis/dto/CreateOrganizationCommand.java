package tabletennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tabletennis.model.Player;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrganizationCommand {

    private String orgName;
    private String address;
    private String contact;
    private String email;
    private String telNumber;

}
