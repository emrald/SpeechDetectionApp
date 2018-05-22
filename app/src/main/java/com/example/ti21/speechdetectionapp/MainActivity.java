package com.example.ti21.speechdetectionapp;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 1234;
    private ListView resultList;
    Button speakButton;
    ArrayList<String> matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matches = new ArrayList<String>();
        speakButton = (Button) findViewById(R.id.speakButton);
        resultList = (ListView) findViewById(R.id.list);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            speakButton.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Recognizer Not Found",
                    Toast.LENGTH_SHORT).show();
        }

/*

        //DIRECT MESSGAE
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, "CONTENT..", null, null);

        // MESSAGE UISNG INTENT
        Uri uri1 = Uri.parse("smsto:" + phone);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri1);
        smsIntent.putExtra("sms_body", "CONTENT..");
        startActivity(smsIntent);


*/


    /*     //SET ALARM
    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "message")
                .putExtra(AlarmClock.EXTRA_HOUR, 16)
                .putExtra(AlarmClock.EXTRA_MINUTES, 30);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
*/
       /*
       // OPEN GOOGLE
       Intent i1 = new Intent(Intent.ACTION_WEB_SEARCH);
        i1.setPackage("com.google.android.googlequicksearchbox");
        i1.putExtra(SearchManager.QUERY,"What is ios ?");
        startActivity(i1);
*/

      /*
      // DIRECT CALL
      Log.e("LIST...", matches.size() + "....");
        resultList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, matches));

        matches.add("Call");
        for (int i = 0; i < matches.size(); i++) {
            Log.e("matches....", matches.get(i) + "....");
            if ("call".equalsIgnoreCase(matches.get(i))) {

                Log.e("Contain...", matches.get(i) + "...");

                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode("shruti"));
                Cursor mapContact = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID}, null, null, null);
                if (mapContact.moveToNext()) {
                    String _id = mapContact.getString(mapContact.getColumnIndex(ContactsContract.Contacts._ID));
                    Log.e("ID...", _id + "...");

                    String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                    String[] whereNameParams2 = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, _id};
                    Cursor nameCur2 = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams2, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                    while (nameCur2.moveToNext()) {
                        String phone = nameCur2.getString(nameCur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Log.e("phone...", phone);

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phone));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }
                    nameCur2.close();

                }
            }
        }*/

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });
    }

    private void startVoiceRecognitionActivity() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "AndroidBite Voice Recognition...");
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE &&
                resultCode == RESULT_OK) {
            matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            Log.e("LIST...", matches.size() + "....");
            resultList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches));

            for (int i = 0; i < matches.size(); i++) {
                Log.e("matches....", matches.get(i) + "....");
                if (matches.get(0).contains("call")) {

                    Log.e("Contain...", matches.get(i) + "...");


                    String NewString = matches.get(i).replaceAll("call", "");
                    Log.e("NewString...", NewString + "...");


                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(NewString.trim()));
                    Cursor mapContact = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID}, null, null, null);
                    if (mapContact.moveToNext()) {
                        String _id = mapContact.getString(mapContact.getColumnIndex(ContactsContract.Contacts._ID));
                        Log.e("ID...", _id + "...");

                        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                        String[] whereNameParams2 = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, _id};
                        Cursor nameCur2 = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams2, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                        while (nameCur2.moveToNext()) {
                            String phone = nameCur2.getString(nameCur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Log.e("phone...", phone);

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(callIntent);

                        }
                        nameCur2.close();

                    }
                    break;
                } else if (matches.get(0).contains("message")) {

                    String NewString = matches.get(i).replaceAll("message", "");
                    Log.e("NewString...message..", NewString + "...message");

                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(NewString.trim()));
                    Cursor mapContact = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID}, null, null, null);
                    if (mapContact.moveToNext()) {
                        String _id = mapContact.getString(mapContact.getColumnIndex(ContactsContract.Contacts._ID));
                        Log.e("ID...", _id + "...");

                        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                        String[] whereNameParams2 = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, _id};
                        Cursor nameCur2 = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams2, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                        while (nameCur2.moveToNext()) {
                            String phone = nameCur2.getString(nameCur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Log.e("phone...", phone);

                          /*  SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phone, null, "CONTENT..", null, null);
*/
                            Uri uri1 = Uri.parse("smsto:" + phone);
                            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri1);
                            smsIntent.putExtra("sms_body", "CONTENT..");
                            startActivity(smsIntent);

                        }
                        nameCur2.close();

                    }
                    break;


                } else {

                /*    Uri uri = Uri.parse("googlechrome://navigate?url=");
                    Intent i1 = new Intent(Intent.ACTION_WEB_SEARCH, Uri.parse("http://www.google.com"));
                    i1.putExtra(SearchManager.QUERY, matches.get(0));
                    startActivity(i1);*/


                    Intent i1 = new Intent(Intent.ACTION_WEB_SEARCH);
                    i1.setPackage("com.google.android.googlequicksearchbox");
                    i1.putExtra(SearchManager.QUERY, matches.get(0));
                    startActivity(i1);
                    break;
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
