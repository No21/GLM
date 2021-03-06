package com.example.ensai.glm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.widget.Toast;

import java.util.Locale;

import static com.example.ensai.glm.R.mipmap.ic_notif;


public class MainActivity extends AppCompatActivity {

    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;

    private Switch bouton;
    //private ToggleButton toggle;
    private CompoundButton.OnCheckedChangeListener toggleListener;

    private TextView smsText;
    private TextView smsSender;
    private  String senderNum;
    private  String sender = "---";
    private Button repeat;
    // je stocke le sms pur pouvoir le répéter dans le main
    private  String sms;

    private BroadcastReceiver smsReceiver;

    private Lecteur speaker;
    private TextToSpeech tts;

    public int ID_NOTIFICATION = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView( R.layout.activity_main );

        bouton = (Switch)findViewById(R.id.boutonSwitch);
        smsText = (TextView)findViewById(R.id.sms_text);
        smsText.setMovementMethod(new ScrollingMovementMethod());
        smsSender = (TextView)findViewById(R.id.sms_sender);
        repeat= (Button)findViewById( R.id.repeter );

        toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if(isChecked){
                    speaker.allow(true);
                    speaker.speak(getString(R.string.start_speaking));
                    createNotification();
                }else{
                    speaker.speak(getString(R.string.stop_speaking));
                    speaker.allow(false);
                    deleteNotification();
                }
            }
        };
        bouton.setOnCheckedChangeListener(toggleListener);


        checkTTS();
        initializeSMSReceiver();
        registerSMSReceiver();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();

        smsSender.setText(getResources().getString(R.string.messageDe)+" " + sender );
        repeat.setText( getString( R.string.rep ) );
        speaker = new Lecteur( this );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new Lecteur(this);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }


    private void initializeSMSReceiver(){
        smsReceiver = new BroadcastReceiver(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceive(Context context, Intent intent) {

                SmsMessage[] smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                StringBuilder messageBody = new StringBuilder( smsMessage.length-1 );
                for (int i=0; i < smsMessage.length; i++){
                    messageBody.append( smsMessage[i].getDisplayMessageBody()) ;
                }
                String allMessage = messageBody.toString();
                senderNum = smsMessage[0].getDisplayOriginatingAddress();
                sender = getContactName(smsMessage[0].getDisplayOriginatingAddress());

                speaker.pause(LONG_DURATION);
                speaker.speak(getResources().getString(R.string.messageDe) + sender);
                speaker.pause(SHORT_DURATION);
                speaker.speak(allMessage);

                smsSender.setText(getResources().getString(R.string.messageDe)+" " + sender);
                smsText.setText(allMessage);
                sms=allMessage;

            }
        };
    }


    private String getContactName(String phone){
        Uri uri = Uri.withAppendedPath( ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else {
            return senderNum;
        }
    }

    private void registerSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }
    // pour créer une notification
    private final void createNotification(){
        final NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent launchNotifiactionIntent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 001, launchNotifiactionIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this).setWhen(System.currentTimeMillis()).setTicker(getString(R.string.name))
                .setSmallIcon(ic_notif)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getString(R.string.notification))
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        mNotification.notify(ID_NOTIFICATION, builder.build());
    }


    // pour supprimer une notification
    private void deleteNotification(){
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //la suppression de la notification se fait grâce a son ID
        notificationManager.cancel(ID_NOTIFICATION);
    }

    public void openPrefer() {
        Intent intent = new Intent(this, Prefer.class);
        startActivity(intent);
    }

    public void openInfo() {
        Intent intent = new Intent(this, Info.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
        speaker.destroy();
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                openPrefer();
                return true;
            case R.id.action_image:
                openInfo();
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }


    //pour répéter
    public void repeteSMS(View v){
        speaker.allow(true);
        speaker.speak(sms);
    }




}
