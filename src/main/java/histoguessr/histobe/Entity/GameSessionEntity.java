package histoguessr.histobe.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "GameSession")
public class GameSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long histoId;
    private String imageUrl;
    private String title;
    private Integer year;
    private LocalDate date;
    private String place;

    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean solved = false;
}