package com.alxan.noteefy.web.exception;

import com.alxan.noteefy.notification.exception.NoteefyException;

import java.util.UUID;

public class MessageHandlerException extends NoteefyException {
    public MessageHandlerException(UUID aNodeUuid) {
        super(aNodeUuid);
    }

    public MessageHandlerException(Exception anException, UUID aNodeUuid) {
        super(anException, aNodeUuid);
    }
}
