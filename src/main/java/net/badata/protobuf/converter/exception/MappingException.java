package net.badata.protobuf.converter.exception;

/**
 * Exception that notifies about field mapping errors during conversion.
 *
 * @author jsjem
 * @author Roman Gushel
 */
public class MappingException extends Exception {

    /**
     * Constructs a new MappingException with the specified detail message.  The
     * cause is not initialized.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public MappingException(String message) {
        super(message);
    }

    /**
     * Constructs a new MappingException with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
