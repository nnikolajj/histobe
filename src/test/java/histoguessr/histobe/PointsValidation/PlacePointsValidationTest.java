package histoguessr.histobe.PointsValidation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlacePointsValidationTest {

    private PlacePointsValidation validation;

    @BeforeEach
    void setUp() {
        validation = new PlacePointsValidation();
    }

    @Test
    void testExactMatchShouldReturnMaxPoints() {
        int result = validation.validatePlace("10.000, 8.000", "10.000, 8.000");
        assertEquals(25000, result, "Identische Koordinaten sollten 25000 Punkte ergeben");
    }

    @Test
    void testSmallDifferenceLat() {
        int result = validation.validatePlace("10.000, 8.000", "9.000, 8.000");
        assertTrue(result == 24000, "Lat um 1 anders, Long gleich");
    }

    @Test
    void testSmallDifferenceLong() {
        int result = validation.validatePlace("10.000, 7.000", "10.000, 8.000");
        assertTrue(result == 24000, "Long gleich, Long um 1 anders");
    }

    @Test
    void testSmallDifferenceLatLong() {
        int result = validation.validatePlace("10.000, 8.000", "9.000, 7.000");
        assertTrue(result == 23585, "Lat um 1 anders, Long um 1 anders");
    }

    @Test
    void testBigDifferenceLatLong() {
        int result = validation.validatePlace("25.000, 16.000", "10.000, 8.000");
        assertTrue(result <= 10000, "Lat um 15 anders, Long um 8 anders");
    }

    @Test
    void testUnderZeroPointerLatLong() {
        int result = validation.validatePlace("40.000, 16.000", "10.000, 8.000");
        assertTrue(result == 0, "Lat um 30 anders, Long um 8 anders");
    }

}
