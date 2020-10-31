package dev.s1ck;

public class RequiredBytes {

    public static byte branching(int num) {
        byte bytes;
        if (num < (1 << 8)) {
            bytes = 0;
        } else if (num < (1 << 16)) {
            bytes = 1;
        } else if (num < (1 << 24)) {
            bytes = 2;
        } else {
            bytes = 3;
        }
        return bytes;
    }

    public static byte leadingZeros(int num) {
        if (num == 0) {
            return 0;
        }
        return (byte) (Integer.BYTES - (Integer.numberOfLeadingZeros(num) >> 3) - 1);
    }
 }
