package test.java.bg.sofia.uni.fmi.news.verification;

import bg.sofia.uni.fmi.news.verification.Verifier;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerifierTest {
    @Test
    void TestIsEmptyOrNullWorksCorrectly() {
        Object[][] testCases = new Object[][]{
                {null, true},
                {" ", true},
                {"value", false}
        };

        for (Object[] testCase : testCases) {
            boolean value = Verifier.isEmptyOrNull((String) testCase[0]);
            boolean expected = (boolean) testCase[1];
            assertEquals(expected, value, "should calculate empty or null correctly");
        }
    }

    @Test
    void TestContainsEmptyOrNullWorksCorrectly() {
        List<Collection<String>> collections = List.of(List.of("", "a"), List.of(" ", "a"), List.of("a", "b"));
        List<Boolean> expectedValues = List.of(true, true, false);
        for (int i = 0; i < collections.size(); i++) {
            Collection<String> collection = collections.get(i);
            boolean value = Verifier.containsEmptyOrNull(collection);
            boolean expected = expectedValues.get(i);
            assertEquals(expected, value, "should calculate if container contains empty or null correctly");
        }
    }
}
