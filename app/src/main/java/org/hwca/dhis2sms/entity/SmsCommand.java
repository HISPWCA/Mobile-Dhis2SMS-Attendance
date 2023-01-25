package org.hwca.dhis2sms.entity;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SmsCommand extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String dataSet;
    private String separator;
    private String displayName;

    public SmsCommand() {
        super();
    }

    public SmsCommand(String id, String name, String dataSet, String separator, String displayName) {
        this.id = id;
        this.name = name;
        this.dataSet = dataSet;
        this.separator = separator;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsCommand that = (SmsCommand) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(dataSet, that.dataSet) && Objects.equals(separator, that.separator) && Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dataSet, separator, displayName);
    }

    @Override
    public String toString() {
        return "SmsCommand{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dataSet='" + dataSet + '\'' +
                ", separator='" + separator + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
