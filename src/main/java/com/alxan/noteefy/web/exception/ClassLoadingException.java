package com.alxan.noteefy.web.exception;

import com.alxan.noteefy.notification.exception.NoteefyException;

import java.util.UUID;

public class ClassLoadingException extends NoteefyException {
    public ClassLoadingException(Exception anException, UUID aNodeUuid) {
        super(anException, aNodeUuid);
    }
}
