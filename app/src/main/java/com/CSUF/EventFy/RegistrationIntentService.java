package com.CSUF.EventFy;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.CSUF.EventFy_Beans.Notification;
import com.CSUF.EventFy_Beans.SignUp;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by swapnil on 4/25/16.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private SignUp signUp;
    private String token;
    private  ProgressDialog progressDialog;
    SharedPreferences preferences;

    public RegistrationIntentService() {
        super(TAG);
        Log.e(TAG, "GCM up: " );


    }

    @Override
    protected void onHandleIntent(Intent intent) {



        signUp = (SignUp) intent.getSerializableExtra("signup");


Log.e("sign up is:  ",""+signUp);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.e(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            if(token!=null)
            sendRegistrationToServer(token);

            // Subscribe to topic channels
         //   subscribeTopics(token);




            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
           // sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
       // Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
       // LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */

    public void sendRegistrationToServer(String token)
    {
        PreferenceManager.getDefaultSharedPreferences(this);
        AddUserId addUserId = new AddUserId(true);
        addUserId.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private class AddUserId extends AsyncTask<Void, Void, Void> {
private String response;
        public AddUserId(String str) {

        }

        public AddUserId(boolean b) {
            super();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.reg_gcm_userid);

            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            Notification notification = new Notification();

            notification.setRegId(token);
            notification.setUserId(signUp.getUserId());

            HttpEntity<Notification> request = new HttpEntity<>(notification);

            ResponseEntity<String> result =
                    restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            response = result.getBody();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.e("resp : ", ""+response);
            if(response.equals("Success"))
            {
                Editor prefsEditor = preferences.edit();

                prefsEditor.putString("isGcmRegistered", response);
                prefsEditor.commit();
            }
        }
    }



    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]




}
