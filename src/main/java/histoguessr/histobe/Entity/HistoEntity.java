package histoguessr.histobe.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "HistoryObject")
@Getter
@Setter
@Data
@Accessors(chain = true)
public class HistoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String picture;

    LocalDate date;

    LocalDate endDate;

    String place;

    String category;

    String description;

    String title;

    public String toString (){
        return id + " " + category + " " + date + " " + endDate + " " + place + " " + title;
    }
}
