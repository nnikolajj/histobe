package histoguessr.histobe.PointsValidation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Data
@Accessors(chain = true)
public class ValidationRequest {

    private int id;
    private int year;
    private String place;
}
