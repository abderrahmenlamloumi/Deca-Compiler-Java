package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.DVal;

interface Value {

    DVal getDVal();

    default ConditionCode cmp(Value other) {
        return null;
    }
}
