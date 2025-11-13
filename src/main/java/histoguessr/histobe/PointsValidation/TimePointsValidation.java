package histoguessr.histobe.PointsValidation;

import java.time.LocalDate;

public class TimePointsValidation {

    public int validateTime(int histoYear, int validationYear) {

        int yearDifference = Math.abs(validationYear - histoYear);

        int timePoints = 25000;

        return Math.max(timePoints - yearDifference * 500, 0);

    }

    public int validateEndTime(int histoYear, int validationYear, int endDate) {

        int yearDifference;

            if (validationYear>= histoYear && validationYear<= endDate){
                yearDifference = 0;
            }
            else{
                yearDifference = Math.abs(validationYear - histoYear);

                if (yearDifference > Math.abs(validationYear-endDate)){
                    yearDifference = Math.abs(validationYear-endDate);
                }
            }

        int timePoints = 25000;

        return Math.max(timePoints - yearDifference * 500, 0);

    }
}

