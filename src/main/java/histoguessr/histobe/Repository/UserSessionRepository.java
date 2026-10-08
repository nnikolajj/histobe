package histoguessr.histobe.Repository;

import histoguessr.histobe.Entity.UserSessionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessionEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE UserSessionEntity us SET us.points = points WHERE us.id = :id")
    UserSessionEntity updatePoints(int id, int points);

    @Query("SELECT us FROM UserSessionEntity us WHERE us.seedId = :gameSeed")
    List<UserSessionEntity> getAllBySeedId(String gameSeed);
}
