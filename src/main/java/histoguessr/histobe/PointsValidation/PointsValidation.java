package histoguessr.histobe.PointsValidation;


import histoguessr.histobe.Entity.HistoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointsValidation {

    Logger logger = LoggerFactory.getLogger(PointsValidation.class);

    int placePoints = 0;
    int timePoints = 0;

   PlacePointsValidation placeValidation = new PlacePointsValidation();
   TimePointsValidation timeValidation = new TimePointsValidation();

    public int validatePoints(HistoEntity histo, ValidationRequest validation) {

        if (!histo.getPlace().isBlank() || !histo.getPlace().isEmpty()){
           placePoints = placeValidation.validatePlace(histo.getPlace(), validation.getPlace());
        }

        if (histo.getDate() != null){
            if (histo.getEndDate() != null) {
                timePoints = timeValidation.validateEndTime(histo.getDate().getYear(), validation.getYear(), histo.getEndDate().getYear());
            }
            else {
                timePoints = timeValidation.validateTime(histo.getDate().getYear(), validation.getYear());
            }
        }

        logger.info("Points: {} {}", placePoints, timePoints);

        return placePoints + timePoints;
    }
}
