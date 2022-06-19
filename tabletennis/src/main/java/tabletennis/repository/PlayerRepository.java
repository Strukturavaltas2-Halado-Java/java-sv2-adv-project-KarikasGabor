package tabletennis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tabletennis.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

}