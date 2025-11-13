package histoguessr.histobe.PointsValidation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimePointsValidationTest {

    private TimePointsValidation validation;

    @BeforeEach
    void setUp() {
        validation = new TimePointsValidation();
    }

    @Test
    void testExactMatchShouldReturnMaxPoints() {
        int result = validation.validateTime(1900, 1900);
        assertEquals(25000, result, "Identische Jahre sollten 25000 Punkte ergeben");
    }

    @Test
    void testSmallDifferenceUnder() {
        int result = validation.validateTime(1900, 1899);
        assertTrue(result == 24500, "Um 1 tiefer");
    }

    @Test
    void testSmallDifferenceOver() {
        int result = validation.validateTime(1900, 1901);
        assertTrue(result == 24500, "Um 1 höher");
    }

    @Test
    void testBigDifference() {
        int result = validation.validateTime(1900, 1945);
        assertTrue(result <= 5000, "year difference of 45");
    }

    @Test
    void testUnderZeroPointerLatLong() {
        int result = validation.validateTime(1900, 1500);
        assertTrue(result == 0, "Lat um 30 anders, Long um 8 anders");
    }
}
