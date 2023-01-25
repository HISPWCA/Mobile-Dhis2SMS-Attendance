package org.hwca.dhis2sms.entity;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GatewayNumber extends RealmObject {
    @PrimaryKey
    private String id;
    private String number;

    public GatewayNumber() {
        super();
    }

    public GatewayNumber(String id, String number) {
        this.id = id;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayNumber that = (GatewayNumber) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public String toString() {
        return "GatewayNumber{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
