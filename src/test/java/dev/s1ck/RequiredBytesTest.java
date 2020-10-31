package dev.s1ck;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequiredBytesTest {

    @Test
    void branchingInt() {
        assertEquals(0, RequiredBytes.branching(0));
        assertEquals(0, RequiredBytes.branching(42));
        assertEquals(1, RequiredBytes.branching(1337));
        assertEquals(2, RequiredBytes.branching(421337));
        assertEquals(3, RequiredBytes.branching(42421337));
    }

    @Test
    void trailingBitsInt() {
        assertEquals(0, RequiredBytes.leadingZeros(0));
        assertEquals(0, RequiredBytes.leadingZeros(42));
        assertEquals(1, RequiredBytes.leadingZeros(1337));
        assertEquals(2, RequiredBytes.leadingZeros(421337));
        assertEquals(3, RequiredBytes.leadingZeros(42421337));
    }

}