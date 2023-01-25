package org.hwca.dhis2sms.entity;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
public class SMSMessage extends RealmObject {

    @PrimaryKey
    private Integer id;
    private String message;
    private String messageSent;

    public SMSMessage() {
        super();
    }

    public SMSMessage(Integer id, String message, String messageSent) {
        this.id = id;
        this.message = message;
        this.messageSent = messageSent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(String messageSent) {
        this.messageSent = messageSent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SMSMessage that = (SMSMessage) o;
        return Objects.equals(id, that.id) && Objects.equals(message, that.message) && Objects.equals(messageSent, that.messageSent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, messageSent);
    }

    @Override
    public String toString() {
        return "SMSMessage{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", messageSent='" + messageSent + '\'' +
                '}';
    }
}
