package org.piss2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MainTest {

    @Test
    void testMain_Success() {
        assertDoesNotThrow(() -> {
           Main.main(new String[]{"some args"});
        });
    }
}
