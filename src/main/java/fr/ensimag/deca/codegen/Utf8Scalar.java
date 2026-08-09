package fr.ensimag.deca.codegen;

import java.nio.charset.StandardCharsets;

public class Utf8Scalar {

    public static int[] asUtf8Scalars(String string) {
        int[] scalars = new int[string.length()];
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        int i = 0;
        for (int j = 0; j < bytes.length; ++j) {
            int unsignedByte = extractUnsignedByte(bytes, j);
            if (unsignedByte < 128) { // 1 byte
                scalars[i] = unsignedByte;
            } else if (unsignedByte < 224) { // 2 bytes
                scalars[i] = (unsignedByte << 8) | extractUnsignedByte(bytes, ++j);
            } else if (unsignedByte < 240) { // 3 bytes
                scalars[i] = (unsignedByte << 16) | (extractUnsignedByte(bytes, ++j) << 8) | extractUnsignedByte(bytes, ++j);
            } else { // 4 bytes
                scalars[i] = (unsignedByte << 24) | (extractUnsignedByte(bytes, ++j) << 16) | (extractUnsignedByte(bytes, ++j) << 8) | extractUnsignedByte(bytes, ++j);
            }
            ++i;
        }
        return scalars;
    }

    private static int extractUnsignedByte(byte[] bytes, int index) {
        return bytes[index] & 0xFF;
    }
}
