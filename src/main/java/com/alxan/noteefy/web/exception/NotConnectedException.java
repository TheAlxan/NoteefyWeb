package com.alxan.noteefy.web.exception;

import com.alxan.noteefy.notification.exception.NoteefyException;

import java.util.UUID;

public class NotConnectedException extends NoteefyException {
    public NotConnectedException(UUID aNodeUuid) {
        super(aNodeUuid);
    }
}
