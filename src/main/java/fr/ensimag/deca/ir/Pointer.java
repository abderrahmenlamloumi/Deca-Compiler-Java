package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.DVal;

public class Pointer implements Value {

    private boolean isNullable = true;

    public void setNotNull() {
        this.isNullable = false;
    }

    public boolean isNullable() {
        return isNullable;
    }

    @Override
    public DVal getDVal() {
        return null;
    }
}
