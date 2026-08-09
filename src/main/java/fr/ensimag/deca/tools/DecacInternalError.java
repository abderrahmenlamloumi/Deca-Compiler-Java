package fr.ensimag.deca.tools;

/**
 * Internal error of the compiler. Should never happen.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class DecacInternalError extends RuntimeException {
    private static final long serialVersionUID = -7489681854632778463L;

    public DecacInternalError(String message, Throwable cause) {
        super(message, cause);
    }

    public DecacInternalError(String message) {
        super(message);
    }

}
