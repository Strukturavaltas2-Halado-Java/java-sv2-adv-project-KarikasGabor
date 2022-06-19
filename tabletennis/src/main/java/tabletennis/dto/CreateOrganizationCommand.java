package tabletennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tabletennis.validation.Name;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrganizationCommand {

    @NotBlank(message = "The name of organization cannot be empty")
    private String orgName;
    @NotBlank(message = "The address cannot be empty")
    private String address;
    @Name
    private String contact;
    @NotBlank(message = "The name cannot be empty")
    @Email(message = "Wrong email format")
    private String email;
    private String telNumber;

}
