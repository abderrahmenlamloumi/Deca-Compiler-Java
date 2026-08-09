package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.DVal;

class UnknownValue implements Value {

    static final UnknownValue INSTANCE = new UnknownValue();

    private UnknownValue() {}

    @Override
    public DVal getDVal() {
        return null;
    }
}
