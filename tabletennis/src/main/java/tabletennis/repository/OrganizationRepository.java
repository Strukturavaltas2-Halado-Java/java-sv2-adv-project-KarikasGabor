package tabletennis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tabletennis.model.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
