package histoguessr.histobe.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "GameSeed")
@Getter
@Setter
@Data
@Accessors(chain = true)
public class GameSeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    private List<String> histoId;

    private short type;

    @Column(length = 6, unique = true, nullable = false)
    private String shortId;

    private LocalDateTime date;
}
