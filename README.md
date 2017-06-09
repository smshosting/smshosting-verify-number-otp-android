# Smshosting Verify OTP
You can use Smshosting Verify to:
* check that a person has access to a specific phone number
* verify a transaction 
* 2-factor authentication

How to verify a phone number:
1. SmshostingVerify sends a pin via sms to the required number
2. The user inserts pin into application
3. SmshostingVerify check that the PIN entered by user is the one you sent

## INSTALLATION
### Gradle
Available soon
### Copy File
Copy **SmshostingVerify.java** file into your project

## USAGE
### Initialization
Put this in **onCreate** method of your activity:
```Java

SmshostingVerify.startWithKeyAndSecret("ENTER_YOUR_KEY", "ENTER_YOUR_SECRET", this);

```
You can obtain your AUTH_KEY and AUTH_SECRET by logging into your [smshosting.it](https://www.smshosting.it) account.
### Send Pin
```Java
SmshostingVerify.sendPinWithPhoneNumberAndText(phoneNumber, "SMSHosting code ${verify_code}", false, this, new SmshostingVerify.SmshostingSendPinListener() {
            @Override
            public void onResponse(JSONObject result) {
                if (result != null) {
                    if (result.has("errorCode")) {
                       //Request Error
                       //Pin NOT sent, handle error...
                    }
                    else {
                       //Request Done
                       //Pin sent, do what you want...
                    }
                } 
                else {
                    //Request Error
                    //Pin NOT sent, handle error...
                }
            }
        });
```
Get **verifyId** from request result
```Java
try {
   verifyId = result.getString("verify_id");
} catch (JSONException e) {
   e.printStackTrace();
}
```
### Verify Pin
```Java
SmshostingVerify.verifyPinWithIdandCode(verifyId, pinEditText.getText().toString(), this, new SmshostingVerify.SmshostingVerifyPinListener() {
            @Override
            public void onResponse(JSONObject result) {
                if (result != null) {
                    if (result.has("errorCode")) {
                       //Request Error
                       //Verification failed, handle error...
                    } 
                    else {
                        //Request Done
                        if (result.has("verify_status")) {
                            String status = null;
                            try {
                                status = result.getString("verify_status");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (status != null && status.equals("VERIFIED")) {
                               //Verification done!
                            }
                            else {
                               //Verification failed, entered pin is not valid
                            }
                        }
                    }
                } 
                else {
                   //Request Error
                   //Verification failed, handle error...
                }
            }
        });
```
## MORE...
Smshosting OTP API documentation: https://www.smshosting.it/it/docs/sms-rest-api/sms-otp

