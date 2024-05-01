package com.alxan.noteefy.web.serialize;

public interface NoteefySerializer<A> {
    A serialize(Object object);

    <T> T deserialize(A data, Class<T> type);
}
