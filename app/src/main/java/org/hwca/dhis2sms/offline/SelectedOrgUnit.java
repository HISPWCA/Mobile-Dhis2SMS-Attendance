package org.hwca.dhis2sms.offline;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SelectedOrgUnit extends RealmObject {
    @PrimaryKey
    public String id;
    public String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
