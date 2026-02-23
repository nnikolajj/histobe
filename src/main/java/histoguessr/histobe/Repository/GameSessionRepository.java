package histoguessr.histobe.Repository;

import histoguessr.histobe.Entity.GameSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSessionEntity, Long> {


    @Query("SELECT gs FROM GameSessionEntity gs WHERE gs.histoId = :id")
    GameSessionEntity findGameSessionByHistoId(long id);
}
