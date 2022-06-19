package tabletennis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tabletennis.model.Organization;
import tabletennis.model.Player;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

}
