package fr.ensimag.deca.feature;

public enum FeatureFlag {

    ARRAY("array", "Enable arrays"),
    FANCY_ERRORS("fancy-errors", "Enable fancy contextual errors"),
    STRING_OBJECT("string-object", "Let the string type exists"),
    ASSERT("assert", "Enable assert function"),
    OPTIMIZATION("optimization", "Enable optimizations");

    private final String shortName;
    private final String description;

    FeatureFlag(String shortName, String description) {
        this.shortName = shortName;
        this.description = description;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getDescription() {
        return this.description;
    }
}
