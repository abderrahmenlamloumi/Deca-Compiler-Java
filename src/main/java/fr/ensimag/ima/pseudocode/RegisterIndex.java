package fr.ensimag.ima.pseudocode;

public class RegisterIndex extends RegisterOffset {

    private final Register index;

    public RegisterIndex(int offset, Register source, Register index) {
        super(offset, source);
        this.index = index;
    }

    @Override
    public String toString() {
        return getOffset() + "(" + getRegister() + ", " + this.index + ")";
    }
}
