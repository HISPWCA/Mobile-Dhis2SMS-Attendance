package org.hwca.dhis2sms.utils;

public class URLConstants {
    public static final String BASE_URL = "/api";
    public static final String LOGIN_URL = BASE_URL.concat("/me.json?fields=id,displayName,name,surname,firstName,phoneNumber,organisationUnits[id,displayName],userGroups,programs,dataSets");
    public static final String DATASETS = BASE_URL.concat("/dataSets");
    public static final String DATA_ELEMENTS = BASE_URL.concat("/dataElements");
    public static final String SMS_COMMANDS = BASE_URL.concat("/smsCommands.json?paging=false&fields=id,name,displayName,smsCodes,dataset,separator");
    public static final String GATEWAY_SETTINGS = BASE_URL.concat("/dataStore/emisConfig/settings.json");
//    public static final String ORGANISATION_UNITS = BASE_URL.concat("/organisationUnits.json?paging=false&fields=id,displayName,level,parent[id]&filter=displayName:!ilike:ECD&filter=displayName:!ilike:DCC");
//    public static final String ORGANISATION_UNITS = BASE_URL.concat("/organisationUnits.json?paging=false&fields=id,displayName,level,parent[id]&filter=displayName:!ilike:%20ECD&filter=displayName:!ilike:%20DCC");
    public static final String ORGANISATION_UNITS = BASE_URL.concat("/organisationUnits.json?paging=false&fields=id,displayName,level,parent[id,displayName,level,parent[id,displayName,level,parent[id,displayName,level,parent[id,displayName,level,parent[id,displayName,level]]]]&filter=organisationUnitGroups.id:in:[wdanqWKUSe5,ASPBz7xrlEd,pSOiYGqPgy4,Nc4Oroz4oLh]");

    private URLConstants() {
        // do nothing here
    }
}