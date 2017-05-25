package com.example.ensai.glm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class RecepteurSms extends BroadcastReceiver {

    public static final String action = "android.provider.Telephony.SMS_RECEIVED";

    public String allMessage;
    public String sender;
    private Uri.Builder contentResolver;

    public void onReceive(Context context, Intent intent) {

        // TODO Auto-generated method stub
        if (intent.getAction().equals(action)){
            SmsMessage[] smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            StringBuilder messageBody = new StringBuilder( smsMessage.length-1 );
            for (int i=0; i < smsMessage.length; i++){
                messageBody.append( smsMessage[i].getDisplayMessageBody()) ;
            }

            allMessage = messageBody.toString();
            sender = smsMessage[0].getDisplayOriginatingAddress();

            //Toast.makeText(context,"Message de :"+sender+"\n"+'"'+allMessage+'"', Toast.LENGTH_LONG).show();

        }
    }


}

