package com.payflow.paymentapi.logging;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogMaskerTest {

    @Test
    void testMaskUserId() {
        assertEquals("U***1", LogMasker.maskUserId("U1231"));
        assertEquals("***", LogMasker.maskUserId("AB"));
        assertEquals("null", LogMasker.maskUserId(null));
    }

    @Test
    void testMaskReference() {
        assertEquals("RE****", LogMasker.maskReference("REF123"));
        assertEquals("****", LogMasker.maskReference("123"));
    }
}
