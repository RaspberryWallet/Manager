package io.raspberrywallet.manager.cryptography.crypto.exceptions;

public class DecryptionException extends Exception {
    public static final int NO_DATA = -1;
    public static final int BAD_KEY = -2;

    private int code;

    public DecryptionException(int code) {
        this.code = code;
    }

    public DecryptionException(String message) {
        super(message);
    }

    public DecryptionException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String getMessage() {
        switch (code) {
            case NO_DATA:
                return "-1: No paylaod data specified.";
            case BAD_KEY:
                return "-2: Wrong key provided for decryption.";
            default:
                return "-3: Unknown error.";
        }
    }
}