package smshostingverify.icontact.it.smshostingverify;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import smshostingverify.icontact.it.smshostingverify.SmshostingVerify.SmshostingCountry;
import smshostingverify.icontact.it.smshostingverify.SmshostingVerify.SmshostingVerify;

public class TestActivity extends Activity {

    EditText numberEditText;
    EditText pinEditText;
    TextView doneButton;
    TextView infoLabel;
    String verifyId;
    LinearLayout loadingView;

    //CountryCodes
    Spinner countryInput;
    ArrayAdapter<SmshostingCountry> countryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //SmshostingVerify Start
        SmshostingVerify.startWithKeyAndSecret("SMSHZ9J9TXJ8MD7DZ33G3", "K51RGU8PKJQT2P0BCLVGR4SJLF3ACHMB", this);

        verifyId = null;
        loadingView = (LinearLayout) findViewById(R.id.loading_view);
        infoLabel = (TextView) findViewById(R.id.info_label);
        numberEditText = (EditText) findViewById(R.id.phone_textedit);
        pinEditText = (EditText) findViewById(R.id.pin_textedit);
        doneButton = (TextView) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pinEditText.isEnabled()) {
                    verifyPin();
                } else {
                    sendPin();
                }

            }
        });

        loadCountryCodes();

    }

    //SEND PIN
    void sendPin() {

        String completeNumber = countryAdapter.getItem(countryInput.getSelectedItemPosition()).getPrefix() + numberEditText.getText().toString();

        showLoadingView();

        SmshostingVerify.sendPinWithPhoneNumberAndText(completeNumber, "SMSHosting code ${verify_code}", false, this, new SmshostingVerify.SmshostingSendPinListener() {
            @Override
            public void onResponse(JSONObject result) {

                if (result != null) {

                    if (result.has("errorCode")) {

                        String title = "";
                        String message = "";

                        try {
                            title = "Error " + result.getString("errorCode");
                            message = result.getString("errorMsg");
                            showAlert(title, message);
                        } catch (JSONException e) {
                            showAlert(getResources().getString(R.string.notice), getResources().getString(R.string.generic_error));
                            e.printStackTrace();
                        }

                    } else {
                        if (result.has("verify_id")) {
                            try {
                                verifyId = result.getString("verify_id");
                                numberEditText.setEnabled(false);
                                numberEditText.setAlpha(0.4f);
                                countryInput.setEnabled(false);
                                countryInput.setAlpha(0.4f);
                                pinEditText.setEnabled(true);
                                pinEditText.setAlpha(1.0f);
                                doneButton.setText(getResources().getString(R.string.verify));
                                infoLabel.setText(getResources().getString(R.string.step2));

                                showAlert(getResources().getString(R.string.pin_sent_title), getResources().getString(R.string.pin_sent_description));


                            } catch (JSONException e) {
                                showAlert(getResources().getString(R.string.notice), getResources().getString(R.string.generic_error));
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    Log.d("SmshostingVerify-Log", "NO DATA");
                    showAlert(getResources().getString(R.string.notice), getResources().getString(R.string.generic_error));
                }

                hideLoadingView();

            }
        });

    }

    //VERIFY PIN
    void verifyPin() {

        showLoadingView();

        SmshostingVerify.verifyPinWithIdandCode(verifyId, pinEditText.getText().toString(), this, new SmshostingVerify.SmshostingVerifyPinListener() {
            @Override
            public void onResponse(JSONObject result) {
                if (result != null) {

                    if (result.has("errorCode")) {

                        String title = "";
                        String message = "";

                        try {
                            title = "Error " + result.getString("errorCode");
                            message = result.getString("errorMsg");
                            showAlert(title, message);
                        } catch (JSONException e) {
                            showAlert(getResources().getString(R.string.notice), getResources().getString(R.string.generic_error));
                            e.printStackTrace();
                        }

                    } else {
                        if (result.has("verify_status")) {
                            try {
                                String status = result.getString("verify_status");

                                if (status != null && status.equals("VERIFIED")) {
                                    showAlertAndRestart(getResources().getString(R.string.done), getResources().getString(R.string.mobile_phone_verified));
                                } else {
                                    showAlert(getResources().getString(R.string.failed), getResources().getString(R.string.pin_not_valid));
                                }


                            } catch (JSONException e) {
                                showAlert(getResources().getString(R.string.notice), getResources().getString(R.string.generic_error));
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    Log.d("SmshostingVerify-Log", "NO DATA");
                    showAlert(getResources().getString(R.string.notice), getResources().getString(R.string.generic_error));
                }

                hideLoadingView();

            }
        });

    }

    //GET COUNTRY CODES
    void loadCountryCodes() {

        List<JSONObject> emptyList = new ArrayList<JSONObject>();
        countryAdapter = new ArrayAdapter<SmshostingCountry>(this, android.R.layout.simple_list_item_1);
        final SmshostingCountry defaultValue = new SmshostingCountry(null);
        defaultValue.setCountryCode("IT");
        defaultValue.setCountryName("ITALY");
        defaultValue.setPrefix("39");
        countryAdapter.add(defaultValue);
        countryInput = (Spinner) findViewById(R.id.country_spinner);
        countryInput.setSelection(0);
        countryInput.setAdapter(countryAdapter);

        countryInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                SmshostingCountry address = countryAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        showLoadingView();

        //SmshostingVerify get Country Codes
        SmshostingVerify.getCountryCodes(new SmshostingVerify.SmshostingCountryCodeListener() {
            @Override
            public void onResponse(List<SmshostingCountry> codes) {

                if (codes.size() > 0) {
                    countryAdapter.clear();
                    countryAdapter.addAll(codes);

                    for (int i = 0; i < countryAdapter.getCount(); i++) {
                        SmshostingCountry obj = countryAdapter.getItem(i);

                        if (obj.getPrefix().equals(defaultValue.getPrefix())) {
                            countryInput.setSelection(i);
                        }

                    }
                }

                hideLoadingView();

            }
        });

    }

    //OTHER METHODS
    void showAlert(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    void showAlertAndRestart(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    void showLoadingView() {
        loadingView.setVisibility(View.VISIBLE);
    }

    void hideLoadingView() {
        loadingView.setVisibility(View.INVISIBLE);
    }

}
