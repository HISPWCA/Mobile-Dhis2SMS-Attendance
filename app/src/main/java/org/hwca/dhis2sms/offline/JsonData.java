package org.hwca.dhis2sms.offline;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

public abstract class JsonData {
    public static byte[] getMeResponse() {
        final String response ="{\"name\":\"user Integration\",\"id\":\"Aw7fJp8GVeP\",\"firstName\":\"user\",\"displayName\":\"user Integration\",\"surname\":\"Integration\",\"userGroups\":[{\"id\":\"GgS3AKbVlxw\"}],\"organisationUnits\":[{\"id\":\"d893Lz77NrG\",\"displayName\":\"The Gambia\"}],\"programs\":[\"gWHYaO6iHK8\",\"Q8ueO979C02\",\"usgdL20pSS6\",\"el6BJLOrYLi\",\"LXKwPPht67d\"],\"dataSets\":[\"aUhJQglsVCI\",\"MaOu90TPS5K\",\"BvXwZ4ViEjh\",\"O84j339AA2D\",\"bsocWYVMpAF\",\"jpGJUlVqrcm\",\"DP6zCzow6Sr\",\"hiljk6ZivNP\",\"Xm9ytVpOdIK\",\"AicPsN5y46Q\",\"USSU4IIW01o\",\"Abp17dyKIx0\",\"CWbF5sEOY14\"]}";
        return response.getBytes(Charset.forName("UTF-8"));
    }

    public static JSONObject getGatewayNumber(){
        final String response = "{\n" +
                "    \"gatewayNumber\": \"445\",\n" +
                "    \"gatewayNumberArray\": [\n" +
                "        \"445\",\n" +
                "        \"+22897584836\"\n" +
                "    ]\n" +
                "}";

        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getSmsCommand(){
        final String response = "{\"smsCommands\":[{\"name\":\"ATT_AM_LBE\",\"id\":\"rlXw5hGax8t\",\"displayName\":\"ATT_AM_LBE\",\"separator\":\".\",\"dataset\":{\"id\":\"BvXwZ4ViEjh\"},\"smsCodes\":[{\"compulsory\":false,\"code\":\"TP\",\"optionId\":0,\"dataElement\":{\"id\":\"Tw4beCX4CDR\"}},{\"compulsory\":false,\"code\":\"SA\",\"optionId\":0,\"dataElement\":{\"id\":\"HB3mrs4Y1fr\"}},{\"compulsory\":false,\"code\":\"AW\",\"optionId\":0,\"dataElement\":{\"id\":\"AWvBlo6Sb3U\"}},{\"compulsory\":false,\"code\":\"LB\",\"optionId\":0,\"dataElement\":{\"id\":\"sZqTVA1E5sL\"}},{\"compulsory\":false,\"code\":\"AP\",\"optionId\":0,\"dataElement\":{\"id\":\"AdEPmAOzWld\"}},{\"compulsory\":false,\"code\":\"LA\",\"optionId\":0,\"dataElement\":{\"id\":\"nVL8eRQ0Bah\"}},{\"compulsory\":false,\"code\":\"SP\",\"optionId\":0,\"dataElement\":{\"id\":\"bqGnhjyut3v\"}},{\"compulsory\":false,\"code\":\"FP\",\"optionId\":0,\"dataElement\":{\"id\":\"kMBbcgRKCeH\"}}]},{\"name\":\"ATT_AM_SSE\",\"id\":\"Km6FVXTXjH7\",\"displayName\":\"ATT_AM_SSE\",\"separator\":\".\",\"dataset\":{\"id\":\"MaOu90TPS5K\"},\"smsCodes\":[{\"compulsory\":false,\"code\":\"FP\",\"optionId\":0,\"dataElement\":{\"id\":\"yA6Az1t4wFL\"}},{\"compulsory\":false,\"code\":\"AW\",\"optionId\":0,\"dataElement\":{\"id\":\"JUAZ1Lx3mJN\"}},{\"compulsory\":false,\"code\":\"SP\",\"optionId\":0,\"dataElement\":{\"id\":\"FbfsIUHmiCQ\"}},{\"compulsory\":false,\"code\":\"LA\",\"optionId\":0,\"dataElement\":{\"id\":\"NReYplKwZg4\"}},{\"compulsory\":false,\"code\":\"TP\",\"optionId\":0,\"dataElement\":{\"id\":\"mH6UdlHyf7d\"}},{\"compulsory\":false,\"code\":\"AP\",\"optionId\":0,\"dataElement\":{\"id\":\"Ldain0NVT1O\"}},{\"compulsory\":false,\"code\":\"SA\",\"optionId\":0,\"dataElement\":{\"id\":\"kFXr91g1rGV\"}},{\"compulsory\":false,\"code\":\"LB\",\"optionId\":0,\"dataElement\":{\"id\":\"sMzBzRmuna0\"}}]},{\"name\":\"ATT_AM_UBE\",\"id\":\"oKwPX6TkKed\",\"displayName\":\"ATT_AM_UBE\",\"separator\":\".\",\"dataset\":{\"id\":\"USSU4IIW01o\"},\"smsCodes\":[{\"compulsory\":false,\"code\":\"SA\",\"optionId\":0,\"dataElement\":{\"id\":\"TwMWsSscV58\"}},{\"compulsory\":false,\"code\":\"FP\",\"optionId\":0,\"dataElement\":{\"id\":\"Wuy58zQN4Yf\"}},{\"compulsory\":false,\"code\":\"LB\",\"optionId\":0,\"dataElement\":{\"id\":\"b9QOdI7MM8o\"}},{\"compulsory\":false,\"code\":\"LA\",\"optionId\":0,\"dataElement\":{\"id\":\"yH5WdmOvEL4\"}},{\"compulsory\":false,\"code\":\"TP\",\"optionId\":0,\"dataElement\":{\"id\":\"aJLsztrf1Cr\"}},{\"compulsory\":false,\"code\":\"AW\",\"optionId\":0,\"dataElement\":{\"id\":\"U4sXO1xrKqv\"}},{\"compulsory\":false,\"code\":\"AP\",\"optionId\":0,\"dataElement\":{\"id\":\"fmSFHbvcXxs\"}},{\"compulsory\":false,\"code\":\"SP\",\"optionId\":0,\"dataElement\":{\"id\":\"UH9ssqUwx2A\"}}]},{\"name\":\"ATT_PM_LBE\",\"id\":\"QdXSlS5hQaF\",\"displayName\":\"ATT_PM_LBE\",\"separator\":\".\",\"dataset\":{\"id\":\"AicPsN5y46Q\"},\"smsCodes\":[{\"compulsory\":false,\"code\":\"FP\",\"optionId\":0,\"dataElement\":{\"id\":\"uOHJ66GsC8A\"}},{\"compulsory\":false,\"code\":\"AP\",\"optionId\":0,\"dataElement\":{\"id\":\"ZcbPlHdUJR5\"}},{\"compulsory\":false,\"code\":\"LB\",\"optionId\":0,\"dataElement\":{\"id\":\"sxvI0YNhFEB\"}},{\"compulsory\":false,\"code\":\"SA\",\"optionId\":0,\"dataElement\":{\"id\":\"uVi8OhLXwNJ\"}},{\"compulsory\":false,\"code\":\"AW\",\"optionId\":0,\"dataElement\":{\"id\":\"n4z81nVF7zo\"}},{\"compulsory\":false,\"code\":\"LA\",\"optionId\":0,\"dataElement\":{\"id\":\"WKGidrQFgFJ\"}},{\"compulsory\":false,\"code\":\"SP\",\"optionId\":0,\"dataElement\":{\"id\":\"TO5YTpXiNoq\"}},{\"compulsory\":false,\"code\":\"TP\",\"optionId\":0,\"dataElement\":{\"id\":\"FHwFmw3WfGM\"}}]},{\"name\":\"ATT_PM_SSE\",\"id\":\"HnpPUhzPsqS\",\"displayName\":\"ATT_PM_SSE\",\"separator\":\".\",\"dataset\":{\"id\":\"Xm9ytVpOdIK\"},\"smsCodes\":[{\"compulsory\":false,\"code\":\"LB\",\"optionId\":0,\"dataElement\":{\"id\":\"n8cK5VVNrtH\"}},{\"compulsory\":false,\"code\":\"SA\",\"optionId\":0,\"dataElement\":{\"id\":\"No0zWPhQNK9\"}},{\"compulsory\":false,\"code\":\"FP\",\"optionId\":0,\"dataElement\":{\"id\":\"bR1Ml4l9d3b\"}},{\"compulsory\":false,\"code\":\"AP\",\"optionId\":0,\"dataElement\":{\"id\":\"HZ7lIdZ8J82\"}},{\"compulsory\":false,\"code\":\"SP\",\"optionId\":0,\"dataElement\":{\"id\":\"pjb0GQ4NrOG\"}},{\"compulsory\":false,\"code\":\"LA\",\"optionId\":0,\"dataElement\":{\"id\":\"WMJmi0vMcEK\"}},{\"compulsory\":false,\"code\":\"AW\",\"optionId\":0,\"dataElement\":{\"id\":\"TEaTWWYrFjN\"}},{\"compulsory\":false,\"code\":\"TP\",\"optionId\":0,\"dataElement\":{\"id\":\"g0R4kD3YlZ8\"}}]},{\"name\":\"ATT_PM_UBE\",\"id\":\"uPTs7rrwSWY\",\"displayName\":\"ATT_PM_UBE\",\"separator\":\".\",\"dataset\":{\"id\":\"jpGJUlVqrcm\"},\"smsCodes\":[{\"compulsory\":false,\"code\":\"AW\",\"optionId\":0,\"dataElement\":{\"id\":\"PFkCYqYtCPR\"}},{\"compulsory\":false,\"code\":\"LB\",\"optionId\":0,\"dataElement\":{\"id\":\"Lk4vssJ6VDa\"}},{\"compulsory\":false,\"code\":\"LA\",\"optionId\":0,\"dataElement\":{\"id\":\"Z6oXLF2P6Kj\"}},{\"compulsory\":false,\"code\":\"FP\",\"optionId\":0,\"dataElement\":{\"id\":\"YwwjxCMX3K1\"}},{\"compulsory\":false,\"code\":\"SA\",\"optionId\":0,\"dataElement\":{\"id\":\"xoXzEigcyQh\"}},{\"compulsory\":false,\"code\":\"TP\",\"optionId\":0,\"dataElement\":{\"id\":\"osPQBP03bCW\"}},{\"compulsory\":false,\"code\":\"SP\",\"optionId\":0,\"dataElement\":{\"id\":\"MgpoEsP7yCF\"}},{\"compulsory\":false,\"code\":\"AP\",\"optionId\":0,\"dataElement\":{\"id\":\"ClO5UqA28h2\"}}]}]}";
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataSet(){
        String response = "{\n" +
                "    \"dataSets\": [\n" +
                "        {\n" +
                "            \"name\": \"Daily Attendance - Afternoon LBE\",\n" +
                "            \"id\": \"AicPsN5y46Q\",\n" +
                "            \"periodType\": \"Daily\",\n" +
                "            \"displayName\": \"Daily Attendance - Afternoon LBE\",\n" +
                "            \"openFuturePeriods\": 1,\n" +
                "            \"dataSetElements\": [\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent with Permission - Afternoon Shift LBE\",\n" +
                "                        \"id\": \"ZcbPlHdUJR5\",\n" +
                "                        \"displayName\": \"Absent with Permission - Afternoon Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Present - Afternoon Shift LBE\",\n" +
                "                        \"id\": \"TO5YTpXiNoq\",\n" +
                "                        \"displayName\": \"Students Present - Afternoon Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late After 30mins - Afternoon Shift LBE\",\n" +
                "                        \"id\": \"WKGidrQFgFJ\",\n" +
                "                        \"displayName\": \"Late After 30mins - Afternoon Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent without Permission - Afternoon Shift LBE\",\n" +
                "                        \"id\": \"n4z81nVF7zo\",\n" +
                "                        \"displayName\": \"Absent without Permission - Afternoon Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Teachers Present On Time - Afternoon Shift LBE\",\n" +
                "                        \"id\": \"FHwFmw3WfGM\",\n" +
                "                        \"displayName\": \"Teachers Present On Time - Afternoon Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late Before 30mins - Afternoon Shift LBE\",\n" +
                "                        \"id\": \"sxvI0YNhFEB\",\n" +
                "                        \"displayName\": \"Late Before 30mins - Afternoon Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Absent - Afternoon Shift LBE\",\n" +
                "                        \"id\": \"uVi8OhLXwNJ\",\n" +
                "                        \"displayName\": \"Students Absent - Afternoon Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Female Teachers Present - Afternoon Shift LBE\",\n" +
                "                        \"id\": \"uOHJ66GsC8A\",\n" +
                "                        \"displayName\": \"Female Teachers Present - Afternoon Shift LBE\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sections\": [\n" +
                "                {\n" +
                "                    \"id\": \"WOR8OEFVRfP\",\n" +
                "                    \"displayName\": \"Daily attendance\",\n" +
                "                    \"sortOrder\": 1,\n" +
                "                    \"greyedFields\": [],\n" +
                "                    \"dataElements\": [\n" +
                "                        {\n" +
                "                            \"name\": \"Teachers Present On Time - Afternoon Shift LBE\",\n" +
                "                            \"id\": \"FHwFmw3WfGM\",\n" +
                "                            \"displayName\": \"Teachers Present On Time - Afternoon Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent with Permission - Afternoon Shift LBE\",\n" +
                "                            \"id\": \"ZcbPlHdUJR5\",\n" +
                "                            \"displayName\": \"Absent with Permission - Afternoon Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent without Permission - Afternoon Shift LBE\",\n" +
                "                            \"id\": \"n4z81nVF7zo\",\n" +
                "                            \"displayName\": \"Absent without Permission - Afternoon Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late After 30mins - Afternoon Shift LBE\",\n" +
                "                            \"id\": \"WKGidrQFgFJ\",\n" +
                "                            \"displayName\": \"Late After 30mins - Afternoon Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late Before 30mins - Afternoon Shift LBE\",\n" +
                "                            \"id\": \"sxvI0YNhFEB\",\n" +
                "                            \"displayName\": \"Late Before 30mins - Afternoon Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Female Teachers Present - Afternoon Shift LBE\",\n" +
                "                            \"id\": \"uOHJ66GsC8A\",\n" +
                "                            \"displayName\": \"Female Teachers Present - Afternoon Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Present - Afternoon Shift LBE\",\n" +
                "                            \"id\": \"TO5YTpXiNoq\",\n" +
                "                            \"displayName\": \"Students Present - Afternoon Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Absent - Afternoon Shift LBE\",\n" +
                "                            \"id\": \"uVi8OhLXwNJ\",\n" +
                "                            \"displayName\": \"Students Absent - Afternoon Shift LBE\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Daily attendance - Afternoon SSE\",\n" +
                "            \"id\": \"Xm9ytVpOdIK\",\n" +
                "            \"periodType\": \"Daily\",\n" +
                "            \"displayName\": \"Daily attendance - Afternoon SSE\",\n" +
                "            \"openFuturePeriods\": 0,\n" +
                "            \"dataSetElements\": [\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late After 30mins - Afternoon Shift SSE\",\n" +
                "                        \"id\": \"WMJmi0vMcEK\",\n" +
                "                        \"displayName\": \"Late After 30mins - Afternoon Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent with Permission - Afternoon Shift SSE\",\n" +
                "                        \"id\": \"HZ7lIdZ8J82\",\n" +
                "                        \"displayName\": \"Absent with Permission - Afternoon Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Absent - Afternoon Shift SSE\",\n" +
                "                        \"id\": \"No0zWPhQNK9\",\n" +
                "                        \"displayName\": \"Students Absent - Afternoon Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Present - Afternoon Shift SSE\",\n" +
                "                        \"id\": \"pjb0GQ4NrOG\",\n" +
                "                        \"displayName\": \"Students Present - Afternoon Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent without Permission - Afternoon Shift SSE\",\n" +
                "                        \"id\": \"TEaTWWYrFjN\",\n" +
                "                        \"displayName\": \"Absent without Permission - Afternoon Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late Before 30mins - Afternoon Shift SSE\",\n" +
                "                        \"id\": \"n8cK5VVNrtH\",\n" +
                "                        \"displayName\": \"Late Before 30mins - Afternoon Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Female Teachers Present - Afternoon Shift SSE\",\n" +
                "                        \"id\": \"bR1Ml4l9d3b\",\n" +
                "                        \"displayName\": \"Female Teachers Present - Afternoon Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Teachers Present On Time - Afternoon Shift SSE\",\n" +
                "                        \"id\": \"g0R4kD3YlZ8\",\n" +
                "                        \"displayName\": \"Teachers Present On Time - Afternoon Shift SSE\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sections\": [\n" +
                "                {\n" +
                "                    \"id\": \"CbvZZmhROv8\",\n" +
                "                    \"displayName\": \"Daily attendance\",\n" +
                "                    \"sortOrder\": 1,\n" +
                "                    \"greyedFields\": [],\n" +
                "                    \"dataElements\": [\n" +
                "                        {\n" +
                "                            \"name\": \"Teachers Present On Time - Afternoon Shift SSE\",\n" +
                "                            \"id\": \"g0R4kD3YlZ8\",\n" +
                "                            \"displayName\": \"Teachers Present On Time - Afternoon Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent with Permission - Afternoon Shift SSE\",\n" +
                "                            \"id\": \"HZ7lIdZ8J82\",\n" +
                "                            \"displayName\": \"Absent with Permission - Afternoon Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent without Permission - Afternoon Shift SSE\",\n" +
                "                            \"id\": \"TEaTWWYrFjN\",\n" +
                "                            \"displayName\": \"Absent without Permission - Afternoon Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late After 30mins - Afternoon Shift SSE\",\n" +
                "                            \"id\": \"WMJmi0vMcEK\",\n" +
                "                            \"displayName\": \"Late After 30mins - Afternoon Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late Before 30mins - Afternoon Shift SSE\",\n" +
                "                            \"id\": \"n8cK5VVNrtH\",\n" +
                "                            \"displayName\": \"Late Before 30mins - Afternoon Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Female Teachers Present - Afternoon Shift SSE\",\n" +
                "                            \"id\": \"bR1Ml4l9d3b\",\n" +
                "                            \"displayName\": \"Female Teachers Present - Afternoon Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Present - Afternoon Shift SSE\",\n" +
                "                            \"id\": \"pjb0GQ4NrOG\",\n" +
                "                            \"displayName\": \"Students Present - Afternoon Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Absent - Afternoon Shift SSE\",\n" +
                "                            \"id\": \"No0zWPhQNK9\",\n" +
                "                            \"displayName\": \"Students Absent - Afternoon Shift SSE\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Daily attendance - Afternoon UBE\",\n" +
                "            \"id\": \"jpGJUlVqrcm\",\n" +
                "            \"periodType\": \"Daily\",\n" +
                "            \"displayName\": \"Daily attendance - Afternoon UBE\",\n" +
                "            \"openFuturePeriods\": 0,\n" +
                "            \"dataSetElements\": [\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Female Teachers Present - Afternoon Shift UBE\",\n" +
                "                        \"id\": \"YwwjxCMX3K1\",\n" +
                "                        \"displayName\": \"Female Teachers Present - Afternoon Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent with Permission - Afternoon Shift UBE\",\n" +
                "                        \"id\": \"ClO5UqA28h2\",\n" +
                "                        \"displayName\": \"Absent with Permission - Afternoon Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late Before 30mins - Afternoon Shift UBE\",\n" +
                "                        \"id\": \"Lk4vssJ6VDa\",\n" +
                "                        \"displayName\": \"Late Before 30mins - Afternoon Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Present - Afternoon Shift UBE\",\n" +
                "                        \"id\": \"MgpoEsP7yCF\",\n" +
                "                        \"displayName\": \"Students Present - Afternoon Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late After 30mins - Afternoon Shift UBE\",\n" +
                "                        \"id\": \"Z6oXLF2P6Kj\",\n" +
                "                        \"displayName\": \"Late After 30mins - Afternoon Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Teachers Present On Time - Afternoon Shift UBE\",\n" +
                "                        \"id\": \"osPQBP03bCW\",\n" +
                "                        \"displayName\": \"Teachers Present On Time - Afternoon Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Absent - Afternoon Shift UBE\",\n" +
                "                        \"id\": \"xoXzEigcyQh\",\n" +
                "                        \"displayName\": \"Students Absent - Afternoon Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent without Permission - Afternoon Shift UBE\",\n" +
                "                        \"id\": \"PFkCYqYtCPR\",\n" +
                "                        \"displayName\": \"Absent without Permission - Afternoon Shift UBE\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sections\": [\n" +
                "                {\n" +
                "                    \"id\": \"PglEXOYw6cC\",\n" +
                "                    \"displayName\": \"Daily attendance\",\n" +
                "                    \"sortOrder\": 1,\n" +
                "                    \"greyedFields\": [],\n" +
                "                    \"dataElements\": [\n" +
                "                        {\n" +
                "                            \"name\": \"Teachers Present On Time - Afternoon Shift UBE\",\n" +
                "                            \"id\": \"osPQBP03bCW\",\n" +
                "                            \"displayName\": \"Teachers Present On Time - Afternoon Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent with Permission - Afternoon Shift UBE\",\n" +
                "                            \"id\": \"ClO5UqA28h2\",\n" +
                "                            \"displayName\": \"Absent with Permission - Afternoon Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent without Permission - Afternoon Shift UBE\",\n" +
                "                            \"id\": \"PFkCYqYtCPR\",\n" +
                "                            \"displayName\": \"Absent without Permission - Afternoon Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late After 30mins - Afternoon Shift UBE\",\n" +
                "                            \"id\": \"Z6oXLF2P6Kj\",\n" +
                "                            \"displayName\": \"Late After 30mins - Afternoon Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late Before 30mins - Afternoon Shift UBE\",\n" +
                "                            \"id\": \"Lk4vssJ6VDa\",\n" +
                "                            \"displayName\": \"Late Before 30mins - Afternoon Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Female Teachers Present - Afternoon Shift UBE\",\n" +
                "                            \"id\": \"YwwjxCMX3K1\",\n" +
                "                            \"displayName\": \"Female Teachers Present - Afternoon Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Present - Afternoon Shift UBE\",\n" +
                "                            \"id\": \"MgpoEsP7yCF\",\n" +
                "                            \"displayName\": \"Students Present - Afternoon Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Absent - Afternoon Shift UBE\",\n" +
                "                            \"id\": \"xoXzEigcyQh\",\n" +
                "                            \"displayName\": \"Students Absent - Afternoon Shift UBE\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Daily Attendance - Morning LBE\",\n" +
                "            \"id\": \"BvXwZ4ViEjh\",\n" +
                "            \"periodType\": \"Daily\",\n" +
                "            \"displayName\": \"Daily Attendance - Morning LBE\",\n" +
                "            \"openFuturePeriods\": 1,\n" +
                "            \"dataSetElements\": [\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Present - Morning Shift LBE\",\n" +
                "                        \"id\": \"bqGnhjyut3v\",\n" +
                "                        \"displayName\": \"Students Present - Morning Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Absent - Morning Shift LBE\",\n" +
                "                        \"id\": \"HB3mrs4Y1fr\",\n" +
                "                        \"displayName\": \"Students Absent - Morning Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Female Teachers Present - Morning Shift LBE\",\n" +
                "                        \"id\": \"kMBbcgRKCeH\",\n" +
                "                        \"displayName\": \"Female Teachers Present - Morning Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late Before 30mins - Morning Shift LBE\",\n" +
                "                        \"id\": \"sZqTVA1E5sL\",\n" +
                "                        \"displayName\": \"Late Before 30mins - Morning Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late After 30mins - Morning Shift LBE\",\n" +
                "                        \"id\": \"nVL8eRQ0Bah\",\n" +
                "                        \"displayName\": \"Late After 30mins - Morning Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent with Permission - Morning Shift LBE\",\n" +
                "                        \"id\": \"AdEPmAOzWld\",\n" +
                "                        \"displayName\": \"Absent with Permission - Morning Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent without Permission - Morning Shift LBE\",\n" +
                "                        \"id\": \"AWvBlo6Sb3U\",\n" +
                "                        \"displayName\": \"Absent without Permission - Morning Shift LBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Teachers Present On Time - Morning Shift LBE\",\n" +
                "                        \"id\": \"Tw4beCX4CDR\",\n" +
                "                        \"displayName\": \"Teachers Present On Time - Morning Shift LBE\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sections\": [\n" +
                "                {\n" +
                "                    \"id\": \"K9ki9TbOMxH\",\n" +
                "                    \"displayName\": \"Daily attendance\",\n" +
                "                    \"sortOrder\": 1,\n" +
                "                    \"greyedFields\": [],\n" +
                "                    \"dataElements\": [\n" +
                "                        {\n" +
                "                            \"name\": \"Teachers Present On Time - Morning Shift LBE\",\n" +
                "                            \"id\": \"Tw4beCX4CDR\",\n" +
                "                            \"displayName\": \"Teachers Present On Time - Morning Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent with Permission - Morning Shift LBE\",\n" +
                "                            \"id\": \"AdEPmAOzWld\",\n" +
                "                            \"displayName\": \"Absent with Permission - Morning Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent without Permission - Morning Shift LBE\",\n" +
                "                            \"id\": \"AWvBlo6Sb3U\",\n" +
                "                            \"displayName\": \"Absent without Permission - Morning Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late After 30mins - Morning Shift LBE\",\n" +
                "                            \"id\": \"nVL8eRQ0Bah\",\n" +
                "                            \"displayName\": \"Late After 30mins - Morning Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late Before 30mins - Morning Shift LBE\",\n" +
                "                            \"id\": \"sZqTVA1E5sL\",\n" +
                "                            \"displayName\": \"Late Before 30mins - Morning Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Female Teachers Present - Morning Shift LBE\",\n" +
                "                            \"id\": \"kMBbcgRKCeH\",\n" +
                "                            \"displayName\": \"Female Teachers Present - Morning Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Present - Morning Shift LBE\",\n" +
                "                            \"id\": \"bqGnhjyut3v\",\n" +
                "                            \"displayName\": \"Students Present - Morning Shift LBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Absent - Morning Shift LBE\",\n" +
                "                            \"id\": \"HB3mrs4Y1fr\",\n" +
                "                            \"displayName\": \"Students Absent - Morning Shift LBE\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Daily attendance - Morning SSE\",\n" +
                "            \"id\": \"MaOu90TPS5K\",\n" +
                "            \"periodType\": \"Daily\",\n" +
                "            \"displayName\": \"Daily attendance - Morning SSE\",\n" +
                "            \"openFuturePeriods\": 0,\n" +
                "            \"dataSetElements\": [\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late Before 30mins - Morning Shift SSE\",\n" +
                "                        \"id\": \"sMzBzRmuna0\",\n" +
                "                        \"displayName\": \"Late Before 30mins - Morning Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late After 30mins - Morning Shift SSE\",\n" +
                "                        \"id\": \"NReYplKwZg4\",\n" +
                "                        \"displayName\": \"Late After 30mins - Morning Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent with Permission - Morning Shift SSE\",\n" +
                "                        \"id\": \"Ldain0NVT1O\",\n" +
                "                        \"displayName\": \"Absent with Permission - Morning Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent without Permission - Morning Shift SSE\",\n" +
                "                        \"id\": \"JUAZ1Lx3mJN\",\n" +
                "                        \"displayName\": \"Absent without Permission - Morning Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Present - Morning Shift SSE\",\n" +
                "                        \"id\": \"FbfsIUHmiCQ\",\n" +
                "                        \"displayName\": \"Students Present - Morning Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Absent - Morning Shift SSE\",\n" +
                "                        \"id\": \"kFXr91g1rGV\",\n" +
                "                        \"displayName\": \"Students Absent - Morning Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Female Teachers Present - Morning Shift SSE\",\n" +
                "                        \"id\": \"yA6Az1t4wFL\",\n" +
                "                        \"displayName\": \"Female Teachers Present - Morning Shift SSE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Teachers Present On Time - Morning Shift SSE\",\n" +
                "                        \"id\": \"mH6UdlHyf7d\",\n" +
                "                        \"displayName\": \"Teachers Present On Time - Morning Shift SSE\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sections\": [\n" +
                "                {\n" +
                "                    \"id\": \"zYUykS1obsX\",\n" +
                "                    \"displayName\": \"Daily attendance\",\n" +
                "                    \"sortOrder\": 1,\n" +
                "                    \"greyedFields\": [],\n" +
                "                    \"dataElements\": [\n" +
                "                        {\n" +
                "                            \"name\": \"Teachers Present On Time - Morning Shift SSE\",\n" +
                "                            \"id\": \"mH6UdlHyf7d\",\n" +
                "                            \"displayName\": \"Teachers Present On Time - Morning Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent with Permission - Morning Shift SSE\",\n" +
                "                            \"id\": \"Ldain0NVT1O\",\n" +
                "                            \"displayName\": \"Absent with Permission - Morning Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent without Permission - Morning Shift SSE\",\n" +
                "                            \"id\": \"JUAZ1Lx3mJN\",\n" +
                "                            \"displayName\": \"Absent without Permission - Morning Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late After 30mins - Morning Shift SSE\",\n" +
                "                            \"id\": \"NReYplKwZg4\",\n" +
                "                            \"displayName\": \"Late After 30mins - Morning Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late Before 30mins - Morning Shift SSE\",\n" +
                "                            \"id\": \"sMzBzRmuna0\",\n" +
                "                            \"displayName\": \"Late Before 30mins - Morning Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Female Teachers Present - Morning Shift SSE\",\n" +
                "                            \"id\": \"yA6Az1t4wFL\",\n" +
                "                            \"displayName\": \"Female Teachers Present - Morning Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Present - Morning Shift SSE\",\n" +
                "                            \"id\": \"FbfsIUHmiCQ\",\n" +
                "                            \"displayName\": \"Students Present - Morning Shift SSE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Absent - Morning Shift SSE\",\n" +
                "                            \"id\": \"kFXr91g1rGV\",\n" +
                "                            \"displayName\": \"Students Absent - Morning Shift SSE\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Daily attendance - Morning UBE\",\n" +
                "            \"id\": \"USSU4IIW01o\",\n" +
                "            \"periodType\": \"Daily\",\n" +
                "            \"displayName\": \"Daily attendance - Morning UBE\",\n" +
                "            \"openFuturePeriods\": 0,\n" +
                "            \"dataSetElements\": [\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Female Teachers Present - Morning Shift UBE\",\n" +
                "                        \"id\": \"Wuy58zQN4Yf\",\n" +
                "                        \"displayName\": \"Female Teachers Present - Morning Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late After 30mins - Morning Shift UBE\",\n" +
                "                        \"id\": \"yH5WdmOvEL4\",\n" +
                "                        \"displayName\": \"Late After 30mins - Morning Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Teachers Present On Time - Morning Shift UBE\",\n" +
                "                        \"id\": \"aJLsztrf1Cr\",\n" +
                "                        \"displayName\": \"Teachers Present On Time - Morning Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent without Permission - Morning Shift UBE\",\n" +
                "                        \"id\": \"U4sXO1xrKqv\",\n" +
                "                        \"displayName\": \"Absent without Permission - Morning Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Absent - Morning Shift UBE\",\n" +
                "                        \"id\": \"TwMWsSscV58\",\n" +
                "                        \"displayName\": \"Students Absent - Morning Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Absent with Permission - Morning Shift UBE\",\n" +
                "                        \"id\": \"fmSFHbvcXxs\",\n" +
                "                        \"displayName\": \"Absent with Permission - Morning Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Students Present - Morning Shift UBE\",\n" +
                "                        \"id\": \"UH9ssqUwx2A\",\n" +
                "                        \"displayName\": \"Students Present - Morning Shift UBE\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dataElement\": {\n" +
                "                        \"name\": \"Late Before 30mins - Morning Shift UBE\",\n" +
                "                        \"id\": \"b9QOdI7MM8o\",\n" +
                "                        \"displayName\": \"Late Before 30mins - Morning Shift UBE\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sections\": [\n" +
                "                {\n" +
                "                    \"id\": \"Thx9neNdwFA\",\n" +
                "                    \"displayName\": \"Daily attendance\",\n" +
                "                    \"sortOrder\": 1,\n" +
                "                    \"greyedFields\": [],\n" +
                "                    \"dataElements\": [\n" +
                "                        {\n" +
                "                            \"name\": \"Teachers Present On Time - Morning Shift UBE\",\n" +
                "                            \"id\": \"aJLsztrf1Cr\",\n" +
                "                            \"displayName\": \"Teachers Present On Time - Morning Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent with Permission - Morning Shift UBE\",\n" +
                "                            \"id\": \"fmSFHbvcXxs\",\n" +
                "                            \"displayName\": \"Absent with Permission - Morning Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Absent without Permission - Morning Shift UBE\",\n" +
                "                            \"id\": \"U4sXO1xrKqv\",\n" +
                "                            \"displayName\": \"Absent without Permission - Morning Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late After 30mins - Morning Shift UBE\",\n" +
                "                            \"id\": \"yH5WdmOvEL4\",\n" +
                "                            \"displayName\": \"Late After 30mins - Morning Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Late Before 30mins - Morning Shift UBE\",\n" +
                "                            \"id\": \"b9QOdI7MM8o\",\n" +
                "                            \"displayName\": \"Late Before 30mins - Morning Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Female Teachers Present - Morning Shift UBE\",\n" +
                "                            \"id\": \"Wuy58zQN4Yf\",\n" +
                "                            \"displayName\": \"Female Teachers Present - Morning Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Present - Morning Shift UBE\",\n" +
                "                            \"id\": \"UH9ssqUwx2A\",\n" +
                "                            \"displayName\": \"Students Present - Morning Shift UBE\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Students Absent - Morning Shift UBE\",\n" +
                "                            \"id\": \"TwMWsSscV58\",\n" +
                "                            \"displayName\": \"Students Absent - Morning Shift UBE\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        try {
            return new JSONObject(response);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    private JsonData() {
    }
}
