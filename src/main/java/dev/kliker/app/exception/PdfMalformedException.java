package dev.kliker.app.exception;

public class PdfMalformedException extends Exception {

    public PdfMalformedException() {
    }

    public PdfMalformedException(String message) {
        super(message);
    }

    public PdfMalformedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdfMalformedException(Throwable cause) {
        super(cause);
    }

    public PdfMalformedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
