package com.CSUF.EventFy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.CSUF.EventFy_Beans.SignUp;
import com.CSUF.EventFy_Beans.VerificationCode;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

<<<<<<< HEAD
=======
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
>>>>>>> origin/master
import java.util.concurrent.ExecutionException;

public class VerifyAccount extends ActionBarActivity {

    private TextView mCodeEditText;
    private Button mCodeSendButton;
    private boolean isEmail;

    private GetVcode getVcode;
    private CheckVcode checkVcode;
    private ProgressDialog progressDialog;
    private SignUp signUp;
    private AddUser addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        Intent in = getIntent();
        String UserFbId = null;
        signUp = (SignUp) in.getSerializableExtra("signup");

        mCodeEditText = (TextView) findViewById(R.id.vcode_editText);
        mCodeSendButton = (Button) findViewById(R.id.vcode_button);
        mCodeSendButton.setEnabled(false);

        getVcode = new GetVcode(true);

        progressDialog = new ProgressDialog(VerifyAccount.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Generating Code...");
        progressDialog.show();



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        try {

                            if(mCodeSendButton.isEnabled()==false)
                                  getVcode.execute(signUp.getUserId()).get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                }, 500);


        mCodeSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // datePickerDialog.setVibrate(isVibrate());
                try {

                    String result = null;

                    if (mCodeEditText!=null && mCodeEditText.length()==4) {
                        mCodeSendButton.setEnabled(false);
                        progressDialog.show();
                        progressDialog.setMessage("Verifying Code...");
                        checkVcode = new CheckVcode(true);
                        result = checkVcode.execute(signUp.getUserId(), mCodeEditText.getText().toString()).get();
                    }

                    if (result != null && result.equals("Success")) {

                        addUser = new AddUser(true);
                        signUp = addUser.execute().get();
                        if(signUp!= null)
                            onVerifySuccess(signUp);
                        else
                            onVerifyFail();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public void onVerifySuccess(SignUp signUp)
    {
        finish();
        Intent intent = new Intent(VerifyAccount.this, Main2Activity.class);
        intent.putExtra("signup", signUp);
        mCodeSendButton.setEnabled(true);
        progressDialog.dismiss();

        startActivity(intent);

    }

    public void onVerifyFail()
    {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        mCodeSendButton.setEnabled(true);
        progressDialog.dismiss();
    }

    private class GetVcode extends AsyncTask<String, String, String> {

        public GetVcode(String email) {

        }

        public GetVcode(boolean b) {
            super();

        }

        @Override
        protected String doInBackground(String... strings) {

<<<<<<< HEAD
            final String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.verification_get);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

            headers.add("Content-Type", "text/plain");

            Log.e("ing get vcode : ", ""+strings[0]);
            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<String> request = new HttpEntity<>(strings[0], headers);

            ResponseEntity<String> rateResponse =
                    restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            String result = rateResponse.getBody();

            return result;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mCodeSendButton.setEnabled(true);
=======
//            HttpResponse resp = null;
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost post = new HttpPost(
//                    "http://192.168.0.5:8080/EventFy/webapi/signup/getverificationcode");
//            post.setHeader("content-type", "application/json");
//
//            JSONObject dato = new JSONObject();
//            try {
//                dato.put("username", strings[0]);
//
//                dato.put("password", strings[0]);
//
//                StringEntity entity = new StringEntity(dato.toString());
//
//                post.setEntity(entity);
//                resp = httpClient.execute(post);
//
//                String result = EntityUtils.toString(resp.getEntity());
//
//                Log.e("get code : ", "" + result);
//                return result;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return null;
>>>>>>> origin/master
        }
    }


    private class CheckVcode extends AsyncTask<String, String,String> {

        public CheckVcode(String email) {

        }

        public CheckVcode(boolean b) {
            super();

        }

        @Override
        protected String doInBackground(String... strings) {

<<<<<<< HEAD
             String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.verification_check);

            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            VerificationCode vCode = new VerificationCode();
            vCode.setUserId(strings[0]);
            vCode.setvCode(strings[1]);

            HttpEntity<VerificationCode> request = new HttpEntity<>(vCode);

            ResponseEntity<String> rateResponse =
                    restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            String result = rateResponse.getBody();

            return result;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mCodeSendButton.setEnabled(true);
        }
    }

    private class AddUser extends AsyncTask<String, String, SignUp> {

        public AddUser(SignUp signUp) {

        }

        public AddUser(boolean b) {
            super();

        }

        @Override
        protected SignUp doInBackground(String... strings) {

            String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.signup_adduser);

            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            signUp.setIsVerified("true");
            HttpEntity<SignUp> request = new HttpEntity<>(signUp);

            ResponseEntity<SignUp> rateResponse =
                    restTemplate.exchange(url, HttpMethod.POST, request, SignUp.class);
            SignUp result = rateResponse.getBody();

            return result;

=======
//            HttpResponse resp = null;
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost post = new HttpPost(
//                    "http://192.168.0.5:8080/EventFy/webapi/signup/checkverificationcode");
//            post.setHeader("content-type", "application/json");
//
//            JSONObject dato = new JSONObject();
//            try {
//                dato.put("userName", strings[0]);
//
//                dato.put("vCode", strings[1]);
//
//                Log.e("data : ", "" + dato);
//
//                StringEntity entity = new StringEntity(dato.toString());
//
//                post.setEntity(entity);
//                resp = httpClient.execute(post);
//
//                String result = EntityUtils.toString(resp.getEntity());
//
//                Log.e("check code : ", "" + result);
//                return result;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return null;
>>>>>>> origin/master
        }



        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(SignUp result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mCodeSendButton.setEnabled(true);
        }
    }


}


