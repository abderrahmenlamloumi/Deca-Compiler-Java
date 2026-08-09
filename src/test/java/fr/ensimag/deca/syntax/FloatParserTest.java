package fr.ensimag.deca.syntax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FloatParserTest {

    @Test
    public void decimalUnderflow() {
        assertThrows(
                NumberFormatException.class,
                () -> FloatParser.checkedParseFloat("1.4e-46")
        );
        assertThrows(
                NumberFormatException.class,
                () -> FloatParser.checkedParseFloat("0.0000000000000000000000000000000000000000000001f")
        );
    }

    @Test
    public void decimalNormal() {
        assertEquals(1.4e-45f, FloatParser.checkedParseFloat("1.4e-45"));
        assertEquals(0.0f, FloatParser.checkedParseFloat("0.0e-50"));
    }

    @Test
    public void hexUnderflow() {
        assertThrows(
            NumberFormatException.class,
            () -> FloatParser.checkedParseFloat("0x0.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001p0")
        );
        assertThrows(
                NumberFormatException.class,
                () -> FloatParser.checkedParseFloat("0x0.ap-180")
        );
    }

    @Test
    public void hexNormal() {
        assertEquals(0x1.0p-112f, FloatParser.checkedParseFloat("0x0.0000000000000000000000000001p0"));
        assertEquals(0.0f, FloatParser.checkedParseFloat("0x0.0p190"));
    }
}
