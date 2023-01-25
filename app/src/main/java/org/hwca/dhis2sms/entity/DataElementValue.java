package org.hwca.dhis2sms.entity;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class DataElementValue extends RealmObject {

    @PrimaryKey
    private String id;
    private String value;
    private String period;
    private String dataSet;
    private String dataElement;

    public DataElementValue() {
        super();
    }

    public DataElementValue(String id, String value, String period, String dataSet, String dataElement) {
        this.id = id;
        this.value = value;
        this.period = period;
        this.dataSet = dataSet;
        this.dataElement = dataElement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataElement() {
        return dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataElementValue that = (DataElementValue) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(period, that.period) && Objects.equals(dataSet, that.dataSet) && Objects.equals(dataElement, that.dataElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, period, dataSet, dataElement);
    }

    @Override
    public String toString() {
        return "DataElementValue{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", period='" + period + '\'' +
                ", dataSet='" + dataSet + '\'' +
                ", dataElement='" + dataElement + '\'' +
                '}';
    }
}
