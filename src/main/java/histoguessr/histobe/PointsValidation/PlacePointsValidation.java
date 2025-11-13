package histoguessr.histobe.PointsValidation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class PlacePointsValidation {
    Logger logger = LoggerFactory.getLogger(PlacePointsValidation.class);

    public int validatePlace(String histoPlace, String validationPlace){
        if (histoPlace.matches("^-?\\d+(\\.\\d+)?,\\s*-?\\d+(\\.\\d+)?$")){
            double[] coordinates = Arrays.stream(histoPlace.split(", "))
                    .mapToDouble(Double::parseDouble).toArray();

            double [] validationCoordinates = Arrays.stream(validationPlace.split(", "))
                    .mapToDouble(Double::parseDouble).toArray();


            double latPoints = Math.abs(coordinates[0]) - Math.abs(validationCoordinates[0]);
            double lonPoints = Math.abs(coordinates[1] - Math.abs(validationCoordinates[1]));

            double distance = Math.sqrt(Math.abs(latPoints) * Math.abs(latPoints) + Math.abs(lonPoints) * Math.abs(lonPoints));

            int placePoints = 25000;

            if (placePoints - distance * 1000 < 0){
                return 0;
            }

            return (int) (placePoints - distance * 1000);
        }
        else {
            logger.error("Invalid histoPlace {}. HistoPlace isn't the right coordinates Format of xx.xx, xx.xx ", histoPlace);
            return 0;
        }
    }
}
