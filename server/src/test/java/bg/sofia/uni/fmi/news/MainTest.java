package bg.sofia.uni.fmi.news;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MainTest {

    @Test
    void TestMain_Success() {
        assertDoesNotThrow(() -> {
            Main.main(new String[]{"some args"});
        });
    }
}
