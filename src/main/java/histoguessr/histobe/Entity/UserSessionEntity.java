package histoguessr.histobe.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "user_session")
@Getter
@Setter
@Data
@Accessors(chain = true)
public class UserSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "seed_id", nullable = false)
    private String seedId;

    @Column(name = "user_id")
    private UUID playerId;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    private int points;
}
