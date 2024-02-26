package in.vakrangee.franchisee.smsretrieval;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SMSRetrievalReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSRetrievalReceiver";
    private IOTPReceiveListener otpListener;

    /**
     * @param otpListener
     */
    public void setOTPListener(IOTPReceiveListener otpListener) {
        this.otpListener = otpListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "Testing: SMSRetrievalReceiver: onReceive()");

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String otp;
                    String msg = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.e(TAG, "Testing: SMSRetrieval: " + msg);

                    // Extract one-time code from the message and complete verification

                    /*<#> Your ExampleApp code is: 123ABC78
                    FA+9qCX9VSu*/

                    //Extract the OTP code and send to the listener

                    if (otpListener != null) {
                        otpListener.onOTPReceived(msg);
                    }
                    break;

                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    if (otpListener != null) {
                        otpListener.onOTPTimeOut();
                    }
                    break;

                case CommonStatusCodes.API_NOT_CONNECTED:

                    if (otpListener != null) {
                        otpListener.onOTPReceivedError("API NOT CONNECTED");
                    }

                    break;

                case CommonStatusCodes.NETWORK_ERROR:

                    if (otpListener != null) {
                        otpListener.onOTPReceivedError("NETWORK ERROR");
                    }

                    break;

                case CommonStatusCodes.ERROR:

                    if (otpListener != null) {
                        otpListener.onOTPReceivedError("SOME THING WENT WRONG");
                    }

                    break;

            }
        }
    }
}
