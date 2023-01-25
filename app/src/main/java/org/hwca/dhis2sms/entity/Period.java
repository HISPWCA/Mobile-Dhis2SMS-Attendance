package org.hwca.dhis2sms.entity;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Period extends RealmObject {

    @PrimaryKey
    private Integer id;
    private String label;
    private String formContent;
    private String dataElement;
    private boolean isSent = false;

    public Period() {
        super();
    }

    public Period(Integer id, String label, String formContent, String dataElement, boolean isSent) {
        this.id = id;
        this.label = label;
        this.formContent = formContent;
        this.dataElement = dataElement;
        this.isSent = isSent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFormContent() {
        return formContent;
    }

    public void setFormContent(String formContent) {
        this.formContent = formContent;
    }

    public String getDataElement() {
        return dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return isSent == period.isSent && Objects.equals(id, period.id) && Objects.equals(label, period.label) && Objects.equals(formContent, period.formContent) && Objects.equals(dataElement, period.dataElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, formContent, dataElement, isSent);
    }

    @Override
    public String toString() {
        return "Period{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", formContent='" + formContent + '\'' +
                ", dataElement='" + dataElement + '\'' +
                ", isSent=" + isSent +
                '}';
    }
}
