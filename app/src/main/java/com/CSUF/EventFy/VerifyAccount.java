package com.CSUF.EventFy;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

public class VerifyAccount extends ActionBarActivity {

    private String userName;
    private String password;
    private String DOB;
    private String fName;
    private TextView mCodeEditText;
    private Button mCodeSendButton;
    private boolean isEmail;

    public senddata senddataObj = new senddata(true);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        mCodeEditText = (TextView) findViewById(R.id.vcode_editText);
        mCodeSendButton = (Button) findViewById(R.id.vcode_button);

        Intent in = getIntent();
        userName = in.getExtras().getString("userId");
        password = in.getExtras().getString("password");
        fName = in.getExtras().getString("userName");
        DOB = in.getExtras().getString("DOB");
        isEmail = in.getExtras().getBoolean("isEmail");

        try {

            String result;

            if(isEmail)
            {}
            else {
                result = senddataObj.execute(userName, password).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        mCodeSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // datePickerDialog.setVibrate(isVibrate());
                try {
                    sendVcode sendVcodeObj = new sendVcode(true);
                    String result= null;

                    if (isEmail) {


                    } else {

                      result = sendVcodeObj.execute(userName, mCodeEditText.getText().toString()).get();
                    }

                    if (result != null && result.equals("Success")) {
                        Intent intent = new Intent(VerifyAccount.this, Main2Activity.class);
                        intent.putExtra("userId", userName);
                        intent.putExtra("password", password);
                        intent.putExtra("userName", fName);
                        intent.putExtra("DOB", DOB);

                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VerifyAccount Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.CSUF.EventFy/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VerifyAccount Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.CSUF.EventFy/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private class senddata extends AsyncTask<String, String, String> {

        public senddata(String email) {

        }

        public senddata(boolean b) {
            super();

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpResponse resp = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(
                    "https://eventfy.herokuapp.com/webapi/signup/getverificationcode");
            post.setHeader("content-type", "application/json");

            JSONObject dato = new JSONObject();
            try {
                dato.put("username", strings[0]);

                dato.put("password", strings[0]);

                StringEntity entity = new StringEntity(dato.toString());

                post.setEntity(entity);
                resp = httpClient.execute(post);

                String result = EntityUtils.toString(resp.getEntity());

                Log.e("result : ", "" + result);
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class sendVcode extends AsyncTask<String, String, String> {

        public sendVcode(String email) {

        }

        public sendVcode(boolean b) {
            super();

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpResponse resp = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(
                    "https://eventfy.herokuapp.com/webapi/signup/checkverificationcode");
            post.setHeader("content-type", "application/json");

            JSONObject dato = new JSONObject();
            try {
                dato.put("userName", strings[0]);

                dato.put("vCode", strings[1]);

                Log.e("data : ", "" + dato);

                StringEntity entity = new StringEntity(dato.toString());

                post.setEntity(entity);
                resp = httpClient.execute(post);

                String result = EntityUtils.toString(resp.getEntity());

                Log.e("result : ", "" + result);
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
