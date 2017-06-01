package smshostingverify.icontact.it.smshostingverify.SmshostingVerify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by svil3 on 31/05/17.
 */

public class SmshostingVerify {


    //Url
    static String baseUrl = "https://api.smshosting.it";
    static String urlSendCode = baseUrl + "/rest/api/verify/send";
    static String urlVerifyCode = baseUrl + "/rest/api/verify/check";
    static String countryCodesList = "http://api.smshosting.it/sp/api/user/country/available";

    //keys
    static String saveTokenKey = "smsh-auth-key";
    static String saveSecretKey = "smsh-auth-secret";

    //Country Codes
    static String countryCodes = "[{\"countyName\":\"AFGHANISTAN\",\"countyCode\":\"AF\",\"prefix\":93},{\"countyName\":\"ALBANIA\",\"countyCode\":\"AL\",\"prefix\":355},{\"countyName\":\"ALGERIA\",\"countyCode\":\"DZ\",\"prefix\":213},{\"countyName\":\"ANDORRA\",\"countyCode\":\"AD\",\"prefix\":376},{\"countyName\":\"ARGENTINA\",\"countyCode\":\"AR\",\"prefix\":54},{\"countyName\":\"ARMENIA\",\"countyCode\":\"AM\",\"prefix\":374},{\"countyName\":\"AUSTRALIA\",\"countyCode\":\"AU\",\"prefix\":61},{\"countyName\":\"AUSTRIA\",\"countyCode\":\"AT\",\"prefix\":43},{\"countyName\":\"AZERBAIJAN\",\"countyCode\":\"AZ\",\"prefix\":994},{\"countyName\":\"BAHRAIN\",\"countyCode\":\"BH\",\"prefix\":973},{\"countyName\":\"BANGLADESH\",\"countyCode\":\"BD\",\"prefix\":880},{\"countyName\":\"BELARUS\",\"countyCode\":\"BY\",\"prefix\":375},{\"countyName\":\"BELGIUM\",\"countyCode\":\"BE\",\"prefix\":32},{\"countyName\":\"BENIN\",\"countyCode\":\"BJ\",\"prefix\":229},{\"countyName\":\"BOLIVIA\",\"countyCode\":\"BO\",\"prefix\":591},{\"countyName\":\"BOSNIA\",\"countyCode\":\"BA\",\"prefix\":387},{\"countyName\":\"BOTSWANA\",\"countyCode\":\"BW\",\"prefix\":267},{\"countyName\":\"BRAZIL\",\"countyCode\":\"BR\",\"prefix\":55},{\"countyName\":\"BRUNEI\",\"countyCode\":\"BN\",\"prefix\":673},{\"countyName\":\"BULGARIA\",\"countyCode\":\"BG\",\"prefix\":359},{\"countyName\":\"BURUNDI\",\"countyCode\":\"BI\",\"prefix\":257},{\"countyName\":\"CAMBODJA\",\"countyCode\":\"KH\",\"prefix\":855},{\"countyName\":\"CAMEROON\",\"countyCode\":\"CM\",\"prefix\":237},{\"countyName\":\"CAPE VERDE\",\"countyCode\":\"CV\",\"prefix\":238},{\"countyName\":\"CENTRAL AFRICA REP.\",\"countyCode\":\"CF\",\"prefix\":236},{\"countyName\":\"CHILE\",\"countyCode\":\"CL\",\"prefix\":56},{\"countyName\":\"CHINA\",\"countyCode\":\"CN\",\"prefix\":86},{\"countyName\":\"CONGO\",\"countyCode\":\"CG\",\"prefix\":242},{\"countyName\":\"CROATIA\",\"countyCode\":\"HR\",\"prefix\":385},{\"countyName\":\"CUBA\",\"countyCode\":\"CU\",\"prefix\":53},{\"countyName\":\"CYPRUS\",\"countyCode\":\"CY\",\"prefix\":357},{\"countyName\":\"CZEK REPUBLIC\",\"countyCode\":\"CZ\",\"prefix\":420},{\"countyName\":\"DENMARK\",\"countyCode\":\"DK\",\"prefix\":45},{\"countyName\":\"DOMINICAN REPUBLIC\",\"countyCode\":\"DO\",\"prefix\":1809},{\"countyName\":\"ECUADOR\",\"countyCode\":\"EC\",\"prefix\":593},{\"countyName\":\"EGYPT\",\"countyCode\":\"EG\",\"prefix\":20},{\"countyName\":\"EL SALVADOR\",\"countyCode\":\"SV\",\"prefix\":503},{\"countyName\":\"ESTONIA\",\"countyCode\":\"EE\",\"prefix\":372},{\"countyName\":\"ETHIOPIA\",\"countyCode\":\"ET\",\"prefix\":251},{\"countyName\":\"FAROE ISLANDS\",\"countyCode\":\"FO\",\"prefix\":298},{\"countyName\":\"FIJI\",\"countyCode\":\"FJ\",\"prefix\":679},{\"countyName\":\"FINLAND\",\"countyCode\":\"FI\",\"prefix\":358},{\"countyName\":\"FRANCE\",\"countyCode\":\"FR\",\"prefix\":33},{\"countyName\":\"GABON\",\"countyCode\":\"GA\",\"prefix\":241},{\"countyName\":\"GEORGIA\",\"countyCode\":\"GE\",\"prefix\":995},{\"countyName\":\"GERMANY\",\"countyCode\":\"DE\",\"prefix\":49},{\"countyName\":\"GHANA\",\"countyCode\":\"GH\",\"prefix\":233},{\"countyName\":\"GIBILTERRA\",\"countyCode\":\"GI\",\"prefix\":350},{\"countyName\":\"GREECE\",\"countyCode\":\"GR\",\"prefix\":30},{\"countyName\":\"GREENLAND\",\"countyCode\":\"GL\",\"prefix\":299},{\"countyName\":\"GUADALUPE\",\"countyCode\":\"GP\",\"prefix\":590},{\"countyName\":\"GUATEMALA\",\"countyCode\":\"GT\",\"prefix\":502},{\"countyName\":\"GUINEA\",\"countyCode\":\"GQ\",\"prefix\":240},{\"countyName\":\"GUINEA\",\"countyCode\":\"GN\",\"prefix\":224},{\"countyName\":\"HONG KONG\",\"countyCode\":\"HK\",\"prefix\":852},{\"countyName\":\"HUNGARY\",\"countyCode\":\"HU\",\"prefix\":36},{\"countyName\":\"ICELAND\",\"countyCode\":\"IS\",\"prefix\":354},{\"countyName\":\"INDIA\",\"countyCode\":\"IN\",\"prefix\":91},{\"countyName\":\"INDONESIA\",\"countyCode\":\"ID\",\"prefix\":62},{\"countyName\":\"IRELAND\",\"countyCode\":\"IE\",\"prefix\":353},{\"countyName\":\"ISRAELE\",\"countyCode\":\"IL\",\"prefix\":972},{\"countyName\":\"ITALY\",\"countyCode\":\"IT\",\"prefix\":39},{\"countyName\":\"IVORY COAST\",\"countyCode\":\"CI\",\"prefix\":225},{\"countyName\":\"JAPAN\",\"countyCode\":\"JP\",\"prefix\":81},{\"countyName\":\"JORDAN\",\"countyCode\":\"JO\",\"prefix\":962},{\"countyName\":\"KENYA\",\"countyCode\":\"KE\",\"prefix\":254},{\"countyName\":\"KUWAIT\",\"countyCode\":\"KW\",\"prefix\":965},{\"countyName\":\"KYRGYSTHAN\",\"countyCode\":\"KG\",\"prefix\":996},{\"countyName\":\"LATVIA\",\"countyCode\":\"LV\",\"prefix\":371},{\"countyName\":\"LEBANON\",\"countyCode\":\"LB\",\"prefix\":961},{\"countyName\":\"LIBYA\",\"countyCode\":\"LY\",\"prefix\":218},{\"countyName\":\"LIECHTENSTEIN\",\"countyCode\":\"LI\",\"prefix\":423},{\"countyName\":\"LITHUANIA\",\"countyCode\":\"LT\",\"prefix\":370},{\"countyName\":\"LUSSEMBURGO\",\"countyCode\":\"LU\",\"prefix\":352},{\"countyName\":\"MACAO\",\"countyCode\":\"MO\",\"prefix\":853},{\"countyName\":\"MACEDONIA\",\"countyCode\":\"MK\",\"prefix\":389},{\"countyName\":\"MADACASCAR\",\"countyCode\":\"MG\",\"prefix\":261},{\"countyName\":\"MALAWI\",\"countyCode\":\"MW\",\"prefix\":265},{\"countyName\":\"MALAYSIA\",\"countyCode\":\"MY\",\"prefix\":60},{\"countyName\":\"MALDIVES\",\"countyCode\":\"MV\",\"prefix\":960},{\"countyName\":\"MALTA\",\"countyCode\":\"MT\",\"prefix\":356},{\"countyName\":\"MARTINIQUE\",\"countyCode\":\"MQ\",\"prefix\":596},{\"countyName\":\"MAURITANIA\",\"countyCode\":\"MR\",\"prefix\":222},{\"countyName\":\"MAURITIUS\",\"countyCode\":\"MU\",\"prefix\":230},{\"countyName\":\"MEXICO\",\"countyCode\":\"MX\",\"prefix\":52},{\"countyName\":\"MOLDOVA\",\"countyCode\":\"MD\",\"prefix\":373},{\"countyName\":\"MONACO\",\"countyCode\":\"MC\",\"prefix\":377},{\"countyName\":\"MONGOLIA\",\"countyCode\":\"MN\",\"prefix\":976},{\"countyName\":\"MOROCCO\",\"countyCode\":\"MA\",\"prefix\":212},{\"countyName\":\"MOZAMBICO\",\"countyCode\":\"MZ\",\"prefix\":258},{\"countyName\":\"NAMIDIA\",\"countyCode\":\"NA\",\"prefix\":264},{\"countyName\":\"NETHERLANDS\",\"countyCode\":\"NL\",\"prefix\":31},{\"countyName\":\"NEW CALEDONIA\",\"countyCode\":\"NC\",\"prefix\":687},{\"countyName\":\"NEW ZELAND\",\"countyCode\":\"NZ\",\"prefix\":64},{\"countyName\":\"NIGERIA\",\"countyCode\":\"NG\",\"prefix\":234},{\"countyName\":\"NORWAY\",\"countyCode\":\"NO\",\"prefix\":47},{\"countyName\":\"OMAN\",\"countyCode\":\"OM\",\"prefix\":968},{\"countyName\":\"PAKISTAN\",\"countyCode\":\"PK\",\"prefix\":92},{\"countyName\":\"PERU\",\"countyCode\":\"PE\",\"prefix\":51},{\"countyName\":\"PHILIPPINES\",\"countyCode\":\"PH\",\"prefix\":63},{\"countyName\":\"POLAND\",\"countyCode\":\"PL\",\"prefix\":48},{\"countyName\":\"PORTUGAL\",\"countyCode\":\"PT\",\"prefix\":351},{\"countyName\":\"QUATAR\",\"countyCode\":\"QA\",\"prefix\":974},{\"countyName\":\"REUNION\",\"countyCode\":\"RE\",\"prefix\":262},{\"countyName\":\"ROMANIA\",\"countyCode\":\"RO\",\"prefix\":40},{\"countyName\":\"RUSSIA\",\"countyCode\":\"RU\",\"prefix\":7},{\"countyName\":\"SAN MARINO\",\"countyCode\":\"SM\",\"prefix\":378},{\"countyName\":\"SATELLITE\",\"prefix\":882},{\"countyName\":\"SAUDI ARABIA\",\"countyCode\":\"SA\",\"prefix\":966},{\"countyName\":\"SENEGAL\",\"countyCode\":\"SN\",\"prefix\":221},{\"countyName\":\"SEYCHELLES\",\"countyCode\":\"SC\",\"prefix\":248},{\"countyName\":\"SINGAPORE\",\"countyCode\":\"SG\",\"prefix\":65},{\"countyName\":\"SLOVAK REPUBLIC\",\"countyCode\":\"SK\",\"prefix\":421},{\"countyName\":\"SLOVENIA\",\"countyCode\":\"SI\",\"prefix\":386},{\"countyName\":\"SOUTH AFRICA\",\"countyCode\":\"ZA\",\"prefix\":27},{\"countyName\":\"SPAIN\",\"countyCode\":\"ES\",\"prefix\":34},{\"countyName\":\"SRI LANKA\",\"countyCode\":\"LK\",\"prefix\":94},{\"countyName\":\"SUD COREA\",\"countyCode\":\"KR\",\"prefix\":82},{\"countyName\":\"SUDAN\",\"countyCode\":\"SD\",\"prefix\":249},{\"countyName\":\"SWAZILAND\",\"countyCode\":\"SZ\",\"prefix\":268},{\"countyName\":\"SWEDEN\",\"countyCode\":\"SE\",\"prefix\":46},{\"countyName\":\"SWITZERLAND\",\"countyCode\":\"CH\",\"prefix\":41},{\"countyName\":\"SYRIA\",\"countyCode\":\"SY\",\"prefix\":963},{\"countyName\":\"TAIWAN\",\"countyCode\":\"TW\",\"prefix\":886},{\"countyName\":\"TANZANIA\",\"countyCode\":\"TZ\",\"prefix\":255},{\"countyName\":\"THAILAND\",\"countyCode\":\"TH\",\"prefix\":66},{\"countyName\":\"TOGO\",\"countyCode\":\"TG\",\"prefix\":228},{\"countyName\":\"TUNISIA\",\"countyCode\":\"TN\",\"prefix\":216},{\"countyName\":\"TURKEY\",\"countyCode\":\"TR\",\"prefix\":90},{\"countyName\":\"TURKMENISTAN\",\"countyCode\":\"TM\",\"prefix\":993},{\"countyName\":\"UGANDA\",\"countyCode\":\"UG\",\"prefix\":256},{\"countyName\":\"UKRAINE\",\"countyCode\":\"UA\",\"prefix\":380},{\"countyName\":\"UNITED ARAB EMIRATES\",\"countyCode\":\"AE\",\"prefix\":971},{\"countyName\":\"UNITED KINGDOM\",\"countyCode\":\"GB\",\"prefix\":44},{\"countyName\":\"URUGUAY\",\"countyCode\":\"UY\",\"prefix\":598},{\"countyName\":\"USA/CANADA\",\"countyCode\":\"US\",\"prefix\":1},{\"countyName\":\"UZBEKISTAN\",\"countyCode\":\"UZ\",\"prefix\":998},{\"countyName\":\"VENEZUELA\",\"countyCode\":\"VE\",\"prefix\":58},{\"countyName\":\"VIETNAM\",\"countyCode\":\"VN\",\"prefix\":84},{\"countyName\":\"YEMEN\",\"countyCode\":\"YE\",\"prefix\":967},{\"countyName\":\"YUGOSLAVIA\",\"prefix\":381},{\"countyName\":\"ZAMBIA\",\"countyCode\":\"ZM\",\"prefix\":260},{\"countyName\":\"ZIMBABWE\",\"countyCode\":\"ZW\",\"prefix\":263}]";


    public static void startWithKeyAndSecret(String key, String secret, Context context) {

        SharedPreferences preferences = context.getSharedPreferences("SmshostingVerify", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(saveTokenKey, key);
        editor.putString(saveSecretKey, secret);
        editor.commit();

    }

    public static void sendPinWithPhoneNumberAndText(String phoneNumber, String text, Boolean sandbox, Context context, SmshostingSendPinListener listener) {

        SharedPreferences preferences = context.getSharedPreferences("SmshostingVerify", Context.MODE_PRIVATE);
        String key = preferences.getString(saveTokenKey,"");
        String secret = preferences.getString(saveSecretKey,"");

        SendPinTask task = new SendPinTask();
        task.key = key;
        task.secret = secret;
        task.phone = phoneNumber;
        task.text = text;
        task.isSandbox = sandbox;
        task.listener = listener;
        task.execute();

    }

    public static void verifyPinWithIdandCode(String verifyId, String code, Context context, SmshostingVerifyPinListener listener) {

        SharedPreferences preferences = context.getSharedPreferences("SmshostingVerify", Context.MODE_PRIVATE);
        String key = preferences.getString(saveTokenKey,"");
        String secret = preferences.getString(saveSecretKey,"");

        VerifyPinTask task = new VerifyPinTask();
        task.key = key;
        task.secret = secret;
        task.verifyId = verifyId;
        task.code = code;
        task.listener = listener;
        task.execute();

    }

    public static void getCountryCodes(SmshostingCountryCodeListener listener) {

        GetCountryCodesTask task = new GetCountryCodesTask();
        task.listener = listener;
        task.execute();

    }


    private static class GetCountryCodesTask extends AsyncTask<URL, Integer, String> {

        SmshostingCountryCodeListener listener = null;

        protected String doInBackground(URL... urls) {

            String result = "";

            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL(countryCodesList);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    result = result + String.valueOf(current);
                }
            } catch (Exception e) {
                Log.d("SmshostingVerify-Log", "Request Exception");
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {

            if (listener != null) {

                List<JSONObject> countriesList = new ArrayList<JSONObject>();

                try {
                    JSONArray countriesArray = new JSONArray(result);

                    for (int i = 0; i < countriesArray.length(); i++) {
                        countriesList.add(countriesArray.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    Log.d("SmshostingVerify-Log", "CountryCode data error");
                    e.printStackTrace();
                }

                listener.onResponse(countriesList);
            } else {
                Log.d("SmshostingVerify-Log", "No Listener");
            }
        }

    }

    private static class SendPinTask extends AsyncTask<URL, Integer, String> {

        SmshostingSendPinListener listener = null;
        String key = null;
        String secret = null;
        String phone = null;
        String text = null;
        Boolean isSandbox = false;
        Context context;

        protected String doInBackground(URL... urls) {

            String result = "";

            if(key != null && secret != null){
                HttpURLConnection urlConnection = null;
                try {

                    URL url = new URL(urlSendCode);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setUseCaches(false);

                    //Auth
                    String basicAuth = Base64.encodeToString((key+":"+secret).getBytes("UTF-8"),Base64.DEFAULT);
                    urlConnection.setRequestProperty("Authorization", "Basic "+basicAuth);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    //Parameters
                    String parameters = "to="+phone+"&text="+text;
                    if(isSandbox){
                        parameters = parameters+"&sandbox=true";
                    }

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(parameters);
                    writer.flush();
                    writer.close();
                    os.close();

                    int code = urlConnection.getResponseCode();
                    String message = urlConnection.getResponseMessage();

                    InputStream in = null;
                    if(code == 200){
                        in = urlConnection.getInputStream();
                    }
                    else{
                        in = urlConnection.getErrorStream();
                    }
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        char current = (char) data;
                        data = isw.read();
                        result = result + String.valueOf(current);
                    }

                    if(result.length()<1){
                        result = "{\"errorCode\":\""+code+"\",\"errorMsg\":\""+message+"\"}";
                    }

                } catch (IOException e) {
                    Log.d("SmshostingVerify-Log", "Request Exception");
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null && result.length()<1) {
                        int code = -1;
                        String message = "";
                        try {
                            code = urlConnection.getResponseCode();
                            if(urlConnection.getResponseMessage() != null){
                                message = urlConnection.getResponseMessage();
                            }
                        } catch (IOException e) {
                            Log.d("SmshostingVerify-Log", "Request Exception");
                            e.printStackTrace();
                        }
                        result = "{\"errorCode\":\""+code+"\",\"errorMsg\":\""+message+"\"}";
                        urlConnection.disconnect();
                    }
                }
            }
            else{

                result = "{\"errorCode\":\"-1\",\"errorMsg\":\"Set key and secret with method \\\"startWithKeyAndSecret\\\"\"}";

            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {

            if (listener != null) {

                try {
                    JSONObject responseJson = new JSONObject(result);
                    listener.onResponse(responseJson);
                } catch (JSONException e) {
                    Log.d("SmshostingVerify-Log", "Bad Data");
                    listener.onResponse(null);
                    e.printStackTrace();
                }

            } else {
                Log.d("SmshostingVerify-Log", "No Listener");
            }
        }

    }

    private static class VerifyPinTask extends AsyncTask<URL, Integer, String> {

        SmshostingVerifyPinListener listener = null;
        String key = null;
        String secret = null;
        String verifyId = null;
        String code = null;
        Context context;

        protected String doInBackground(URL... urls) {

            String result = "";

            if(key != null && secret != null){
                HttpURLConnection urlConnection = null;
                try {

                    URL url = new URL(urlVerifyCode+"?verify_id="+verifyId+"&verify_code="+code);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(false);

                    //Auth
                    String basicAuth = Base64.encodeToString((key+":"+secret).getBytes("UTF-8"),Base64.DEFAULT);
                    urlConnection.setRequestProperty("Authorization", "Basic "+basicAuth);

                    int code = urlConnection.getResponseCode();
                    String message = urlConnection.getResponseMessage();

                    InputStream in = null;
                    if(code == 200){
                        in = urlConnection.getInputStream();
                    }
                    else{
                        in = urlConnection.getErrorStream();
                    }
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        char current = (char) data;
                        data = isw.read();
                        result = result + String.valueOf(current);
                    }

                    if(result.length()<1){
                        result = "{\"errorCode\":\""+code+"\",\"errorMsg\":\""+message+"\"}";
                    }

                } catch (IOException e) {
                    Log.d("SmshostingVerify-Log", "Request Exception");
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null && result.length()<1) {
                        int code = -1;
                        String message = "";
                        try {
                            code = urlConnection.getResponseCode();
                            if(urlConnection.getResponseMessage() != null){
                                message = urlConnection.getResponseMessage();
                            }
                        } catch (IOException e) {
                            Log.d("SmshostingVerify-Log", "Request Exception");
                            e.printStackTrace();
                        }
                        result = "{\"errorCode\":\""+code+"\",\"errorMsg\":\""+message+"\"}";
                        urlConnection.disconnect();
                    }
                }
            }
            else{

                result = "{\"errorCode\":\"-1\",\"errorMsg\":\"Set key and secret with method \\\"startWithKeyAndSecret\\\"\"}";

            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {

            if (listener != null) {

                try {
                    JSONObject responseJson = new JSONObject(result);
                    listener.onResponse(responseJson);
                } catch (JSONException e) {
                    Log.d("SmshostingVerify-Log", "Bad Data");
                    listener.onResponse(null);
                    e.printStackTrace();
                }

            } else {
                Log.d("SmshostingVerify-Log", "No Listener");
            }
        }

    }

    //Interfaces
    public interface SmshostingCountryCodeListener {
        public void onResponse(List<JSONObject> codes);
    }

    public interface SmshostingSendPinListener {
        public void onResponse(JSONObject result);
    }

    public interface SmshostingVerifyPinListener {
        public void onResponse(JSONObject result);
    }

}