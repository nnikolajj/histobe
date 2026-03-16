package histoguessr.histobe.Repository;

import histoguessr.histobe.Entity.GameSeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameSeedRepository extends JpaRepository<GameSeed, UUID> {

    @Query("SELECT gs FROM GameSeed gs WHERE gs.shortId = :id ORDER BY gs.date DESC limit 1")
    GameSeed findGameSeedByShortId(String id);
}
