package org.hwca.dhis2sms.entity;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DataSet extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String user;
    private String periodType;
    private String displayName;
    private Integer openFuturePeriods;

    public DataSet() {
        super();
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getOpenFuturePeriods() {
        return openFuturePeriods;
    }

    public void setOpenFuturePeriods(Integer openFuturePeriods) {
        this.openFuturePeriods = openFuturePeriods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSet dataSet = (DataSet) o;
        return Objects.equals(id, dataSet.id) && Objects.equals(name, dataSet.name) && Objects.equals(user, dataSet.user) && Objects.equals(periodType, dataSet.periodType) && Objects.equals(displayName, dataSet.displayName) && Objects.equals(openFuturePeriods, dataSet.openFuturePeriods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, user, periodType, displayName, openFuturePeriods);
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", periodType='" + periodType + '\'' +
                ", displayName='" + displayName + '\'' +
                ", openFuturePeriods=" + openFuturePeriods +
                '}';
    }
}
