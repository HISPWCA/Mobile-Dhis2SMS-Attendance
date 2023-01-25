package org.hwca.dhis2sms.entity;

import java.util.Date;
import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Settings extends RealmObject {
    @PrimaryKey
    private String id;
    private String baseUrl;
    private String pinCode;
    private Date lastConnection;
    private String gatewayNumber;

    public Settings() {
        super();
    }

    public Settings(String id, String baseUrl, String pinCode, Date lastConnection, String gatewayNumber) {
        this.id = id;
        this.baseUrl = baseUrl;
        this.pinCode = pinCode;
        this.lastConnection = lastConnection;
        this.gatewayNumber = gatewayNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Date getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }

    public String getGatewayNumber() {
        return gatewayNumber;
    }

    public void setGatewayNumber(String gatewayNumber) {
        this.gatewayNumber = gatewayNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return Objects.equals(id, settings.id) && Objects.equals(baseUrl, settings.baseUrl) && Objects.equals(pinCode, settings.pinCode) && Objects.equals(lastConnection, settings.lastConnection) && Objects.equals(gatewayNumber, settings.gatewayNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseUrl, pinCode, lastConnection, gatewayNumber);
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id='" + id + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", lastConnection=" + lastConnection +
                ", gatewayNumber='" + gatewayNumber + '\'' +
                '}';
    }
}
