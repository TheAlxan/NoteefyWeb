package com.alxan.noteefy.web.exception;

import com.alxan.noteefy.notification.exception.NoteefyException;

import java.util.UUID;

public class ParseException extends NoteefyException {
    public ParseException(UUID aNodeUuid) {
        super(aNodeUuid);
    }

    public ParseException(Exception anException, UUID aNodeUuid) {
        super(anException, aNodeUuid);
    }
}
