package smshostingverify.icontact.it.smshostingverify;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import smshostingverify.icontact.it.smshostingverify.SmshostingVerify.SmshostingVerify;

public class TestActivity extends Activity {

    EditText countryEditText;
    EditText numberEditText;
    EditText pinEditText;
    TextView doneButton;
    TextView infoLabel;
    String verifyId;
    LinearLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //SmshostingVerify Start
        SmshostingVerify.startWithKeyAndSecret("SMSHZ9J9TXJ8MD7DZ33G3","K51RGU8PKJQT2P0BCLVGR4SJLF3ACHMB",this);

        //SmshostingVerify get Country Codes
        SmshostingVerify.getCountryCodes(new SmshostingVerify.SmshostingCountryCodeListener() {
            @Override
            public void onResponse(List<JSONObject> codes) {

                Log.d("SmshostingVerify-Log","*****"+codes.size());

            }
        });

        verifyId = null;
        loadingView = (LinearLayout) findViewById(R.id.loading_view);
        infoLabel = (TextView) findViewById(R.id.info_label);
        countryEditText = (EditText) findViewById(R.id.country_textedit);
        numberEditText = (EditText) findViewById(R.id.phone_textedit);
        pinEditText = (EditText) findViewById(R.id.pin_textedit);
        doneButton = (TextView) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pinEditText.isEnabled()){
                    verifyPin();
                }
                else{
                    sendPin();
                }

            }
        });


    }

    void sendPin(){

        String completeNumber = countryEditText.getText().toString() + numberEditText.getText().toString();

        showLoadingView();

        SmshostingVerify.sendPinWithPhoneNumberAndText(completeNumber, "SMSHosting code ${verify_code}", true, this, new SmshostingVerify.SmshostingSendPinListener() {
            @Override
            public void onResponse(JSONObject result) {

                if(result!=null){

                    if(result.has("errorCode")){

                        String title = "";
                        String message = "";

                        try {
                            title = "Error "+result.getString("errorCode");
                            message = result.getString("errorMsg");
                            showAlert(title,message);
                        } catch (JSONException e) {
                            showAlert(getResources().getString(R.string.notice),getResources().getString(R.string.generic_error));
                            e.printStackTrace();
                        }

                    }
                    else{
                        if(result.has("verify_id")){
                            try {
                                verifyId = result.getString("verify_id");
                                numberEditText.setEnabled(false);
                                numberEditText.setAlpha(0.4f);
                                countryEditText.setEnabled(false);
                                countryEditText.setAlpha(0.4f);
                                pinEditText.setEnabled(true);
                                pinEditText.setAlpha(1.0f);
                                doneButton.setText(getResources().getString(R.string.verify));
                                infoLabel.setText(getResources().getString(R.string.step2));

                                showAlert(getResources().getString(R.string.pin_sent_title),getResources().getString(R.string.pin_sent_description));


                            } catch (JSONException e) {
                                showAlert(getResources().getString(R.string.notice),getResources().getString(R.string.generic_error));
                                e.printStackTrace();
                            }
                        }
                    }

                }
                else{
                    Log.d("SmshostingVerify-Log","NO DATA");
                    showAlert(getResources().getString(R.string.notice),getResources().getString(R.string.generic_error));
                }

                hideLoadingView();

            }
        });

    }

    void verifyPin(){

        showLoadingView();

        SmshostingVerify.verifyPinWithIdandCode(verifyId, pinEditText.getText().toString(), this, new SmshostingVerify.SmshostingVerifyPinListener() {
            @Override
            public void onResponse(JSONObject result) {
                if(result!=null){

                    if(result.has("errorCode")){

                        String title = "";
                        String message = "";

                        try {
                            title = "Error "+result.getString("errorCode");
                            message = result.getString("errorMsg");
                            showAlert(title,message);
                        } catch (JSONException e) {
                            showAlert(getResources().getString(R.string.notice),getResources().getString(R.string.generic_error));
                            e.printStackTrace();
                        }

                    }
                    else{
                        if(result.has("verify_status")){
                            try {
                                String status = result.getString("verify_status");

                                if(status != null && status.equals("VERIFIED")){
                                    showAlert(getResources().getString(R.string.done),getResources().getString(R.string.mobile_phone_verified));
                                }
                                else{
                                    showAlert(getResources().getString(R.string.failed),getResources().getString(R.string.pin_not_valid));
                                }


                            } catch (JSONException e) {
                                showAlert(getResources().getString(R.string.notice),getResources().getString(R.string.generic_error));
                                e.printStackTrace();
                            }
                        }
                    }

                }
                else{
                    Log.d("SmshostingVerify-Log","NO DATA");
                    showAlert(getResources().getString(R.string.notice),getResources().getString(R.string.generic_error));
                }

                hideLoadingView();

            }
        });

    }

    void showAlert(String title, String message){
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

    void showAlertAndRestart(String title, String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        //restart
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    void showLoadingView(){
        loadingView.setVisibility(View.VISIBLE);
    }

    void hideLoadingView(){
        loadingView.setVisibility(View.INVISIBLE);
    }

}
