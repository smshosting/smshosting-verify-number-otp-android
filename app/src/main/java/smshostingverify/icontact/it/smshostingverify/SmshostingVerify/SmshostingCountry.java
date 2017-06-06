package smshostingverify.icontact.it.smshostingverify.SmshostingVerify;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by svil3 on 06/06/17.
 */

public class SmshostingCountry {

    String countryCode;
    String countryName;
    String prefix;

    public SmshostingCountry(JSONObject countryJson) {

        countryCode = new String("");
        countryName = new String("");
        prefix = new String("");

        if(countryJson != null) {

            try {

                if (countryJson.has("countyCode")) {
                    countryCode = countryJson.getString("countyCode");
                }
                if (countryJson.has("countyName")) {
                    countryName = countryJson.getString("countyName");
                }
                if (countryJson.has("prefix")) {
                    prefix = countryJson.getString("prefix");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        String desc = prefix + " (" + countryName + ")";
        return desc;
    }

}
