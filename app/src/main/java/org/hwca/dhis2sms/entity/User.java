package org.hwca.dhis2sms.entity;


import java.util.Arrays;
import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String id;
    private String email;
    private String username;
    private String password;
    private String displayName;
    private byte[] loginResponse;

    public User() {
        super();
    }

    public User(String id, String email, String username, String password, String displayName, byte[] loginResponse) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.loginResponse = loginResponse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public byte[] getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(byte[] loginResponse) {
        this.loginResponse = loginResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(displayName, user.displayName) && Arrays.equals(loginResponse, user.loginResponse);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, email, username, password, displayName);
        result = 31 * result + Arrays.hashCode(loginResponse);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                ", loginResponse=" + Arrays.toString(loginResponse) +
                '}';
    }
}
