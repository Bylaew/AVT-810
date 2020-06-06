package ru.nstu.javaprog.net.dto;

import java.io.Serializable;
import java.util.Objects;

public final class Parcel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Object data;
    private final Request request;
    private final long senderSessionId;
    private final long recipientSessionId;

    public Parcel(long sender, long recipient, Request request, Object data) {
        this.senderSessionId = sender;
        this.recipientSessionId = recipient;
        this.data = data;
        this.request = Objects.requireNonNull(request);
    }

    public Object getData() {
        return data;
    }

    public Request getRequest() {
        return request;
    }

    public long getRecipient() {
        return recipientSessionId;
    }

    public long getSender() {
        return senderSessionId;
    }

    public enum Request {
        GET,
        PROVIDE,
        UPDATE,
        CONNECT,
        DISCONNECT
    }
}

