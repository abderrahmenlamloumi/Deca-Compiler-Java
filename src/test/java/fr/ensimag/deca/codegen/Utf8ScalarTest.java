package fr.ensimag.deca.codegen;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static fr.ensimag.deca.codegen.Utf8Scalar.asUtf8Scalars;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Utf8ScalarTest {

    @Test
    public void oneByte() {
        assertArrayEquals(new int[]{37}, asUtf8Scalars("%"));
        assertArrayEquals(new int[]{65}, asUtf8Scalars("A"));
        assertArrayEquals(new int[]{104, 101, 108, 108, 111}, asUtf8Scalars("hello"));
    }

    @Test
    public void twoBytes() {
        assertArrayEquals(new int[]{50080, 53120}, asUtf8Scalars("àπ"));
        assertDoesNotThrow(() -> asUtf8Scalars(unsafeStringFromBytes((byte) 128)));
    }

    @Test
    public void threeBytes() {
        assertArrayEquals(new int[]{14846082, 14846106}, asUtf8Scalars("∂√"));
        assertDoesNotThrow(() -> asUtf8Scalars(unsafeStringFromBytes((byte) 224)));
    }

    @Test
    public void fourBytes() {
        assertArrayEquals(new int[]{-257978744, 0}, asUtf8Scalars("🎈"));
        assertDoesNotThrow(() -> asUtf8Scalars(unsafeStringFromBytes((byte) 240)));
    }

    private static String unsafeStringFromBytes(byte ...bytes) {
        char[] charData = new char[bytes.length];
        for(int i = 0; i < charData.length; i++) {
            charData[i] = (char) (((int) bytes[i]) & 0xFF);
        }
        return new String(charData);
    }
}
