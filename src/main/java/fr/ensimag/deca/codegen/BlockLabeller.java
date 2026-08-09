package fr.ensimag.deca.codegen;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.ima.pseudocode.Label;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * A {@link Label} factory.
 * <p>
 * When generating a label from a user-controlled name, or for a construct that
 * could be used multiple times, this class ensures that the label is unique.
 * Always prefer using this class than creating labels directly.
 */
public class BlockLabeller {

    private int index;

    private final Map<ClassDefinition, Label> classInitLabels = new HashMap<>();
    private final Map<String, Integer> labels = new TreeMap<>(String.CASE_INSENSITIVE_ORDER); // IMA labels are case-insensitive

    /**
     * Get the label for a class constructor.
     * <p>
     * If called multiple times with the same class definition, it will return
     * the same label instance.
     *
     * @param classDefinition the class definition for which to create the label
     * @return the label for the class constructor
     */
    public Label create(ClassDefinition classDefinition) {
        return classInitLabels.computeIfAbsent(classDefinition, (def) -> normalize("init." + def.getType().toString()));

    }

    /**
     * Create a label with a prefix and an incrementing index.
     * <p>
     * This is useful for creating labels that are unique within a block of code,
     * for example, for internal loops or conditionals.
     *
     * @param prefix the prefix for the label
     * @return a new unique label
     */
    public Label create(String prefix) {
        return new Label(prefix + "." + this.index++);
    }

    /**
     * Normalize a label name by ensuring it is unique.
     *
     * @param name the name of the label to normalize
     * @return a new unique label
     */
    public Label normalize(String name) {
        // IMA labels cannot contain the dollar sign, but Deca identifiers can.
        name = name.replace("$", "_");
        if (name.length() > 512) {
            // IMA labels have a maximum length of 1024 characters.
            // 512 characters is a safe limit
            name = name.substring(0, 512);
        }

        // Ensure the label is unique by appending a count if necessary.
        int count = this.labels.merge(name, 1, Integer::sum);
        if (count < 2) {
            return new Label(name);
        }
        return new Label(name + "." + count);
    }
}
