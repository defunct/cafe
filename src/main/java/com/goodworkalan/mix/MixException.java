package com.goodworkalan.mix;

import com.goodworkalan.go.go.GoException;

public class MixException extends GoException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    public MixException(int code) {
        super(code);
    }

    public MixException(int code, Throwable cause) {
        super(code, cause);
    }
}
