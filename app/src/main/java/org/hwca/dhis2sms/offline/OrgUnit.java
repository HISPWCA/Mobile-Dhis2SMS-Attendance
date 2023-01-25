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
public class OrgUnit extends RealmObject {
    @PrimaryKey
    public String id;
    public String displayName;
    public Integer level;
    public String parent;

    @Override
    public String toString() {
        return displayName;
    }

}
