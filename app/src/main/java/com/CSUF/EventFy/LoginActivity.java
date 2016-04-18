package com.CSUF.EventFy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.CSUF.EventFy_Beans.SignUp;
import com.CSUF.EventFy_Beans.User;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    CallbackManager callbackManager;
    private EditText _emailText;
    private static String TAG_ERROR = "Error";
    private static String TAG_CANCEL = "Cancel";
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    SharedPreferences preferences;
    ProgressDialog progressDialog;

    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String signUpjson = preferences.getString("signUp", "");

        Log.e("data in ca : ", signUpjson);
        Log.e("data len : ", ""+signUpjson.length());

//        if (signUpjson.length()>5 && signUpjson != null) {
//
//            Log.e("in else : ", signUpjson);
//            Gson gson = new Gson();
//            SignUp signUp = gson.fromJson(signUpjson, SignUp.class);
//            onLoginSuccess(signUp, true);
//        } else {



            FacebookSdk.sdkInitialize(getApplicationContext());

            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");

            Log.e("in if state : ", signUpjson);
            setContentView(R.layout.activity_login);
            _emailText = (EditText) findViewById(R.id.input_email);
            _loginButton = (Button) findViewById(R.id.btn_login);
            _passwordText = (EditText) findViewById(R.id.input_password);
            _signupLink = (TextView) findViewById(R.id.link_signup);
            LoginButton fbLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);

            fbLoginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFblogin();

                }

            });

            _loginButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });
            _signupLink.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    progressDialog.show();
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    // Private method to handle Facebook login and callback
    private void onFblogin() {
        callbackManager = Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                //  String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_UserName = json.getString("name");
                                                //  String str_lastname = json.getString("last_name");

                                                Log.e("fb id : ", "" + str_id);
                                               SignUp signUp = new SignUp();
                                                signUp.setUserName(""+str_UserName);
                                                signUp.setIsFacebook("true");
                                                onLoginSuccess(signUp, false);

//                                                Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
//                                                intent.putExtra("userId", str_UserName);
//                                                intent.putExtra("userFbId", str_id);
//                                                intent.putExtra("isFacebook", true);
//                                                startActivity(intent);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();
                    }


                    @Override
                    public void onCancel() {
                        Log.d(TAG_CANCEL, "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG_ERROR, error.toString());
                    }

                });
    }


    public void login() {



        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        _loginButton.setEnabled(false);

                        String result = null;
                        if (!validate()) {
                            onLoginFailed();
                            return;
                        } else {
                            senddata senddataObj = new senddata(true);
                            try {
                                senddataObj.execute(_emailText.getText().toString(), _passwordText.getText().toString()).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, 500);

    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(SignUp signUp, boolean isSetAlready) {
        // _loginButton.setEnabled(true);
        finish();
        progressDialog.dismiss();
        if (!isSetAlready)
            setsharedPref(signUp);

      // Intent intent = new Intent(this, Main2Activity.class);
      //  intent.putExtra("signUp", signUp);

    //    startActivity(intent);
        //finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();


    }


    public class senddata extends AsyncTask<String, String, SignUp> {
        String responseText;

        public senddata(boolean b) {
        }

        @Override
        protected SignUp doInBackground(String... strings) {

            User user = new User();
            user.setUsername(strings[0]);
            user.setPassword(strings[1]);

            final String url = "http://192.168.0.5:8080/EventFy/webapi/login";
            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<User> request = new HttpEntity<>(user);

            ResponseEntity<SignUp> rateResponse =
                    restTemplate.exchange(url,
                            HttpMethod.POST, request, SignUp.class);
            SignUp signUp = rateResponse.getBody();


            Log.e("return : ", "" + signUp.getUserName());

            return signUp;

        }

        @Override
        protected void onPostExecute(SignUp signUp) {
            super.onPostExecute(signUp);
            _loginButton.setEnabled(true);
            if (signUp != null && signUp.getUserName().length() > 0)
                onLoginSuccess(signUp, false);
            else
                onLoginFailed();

        }
    }

    public void setsharedPref(SignUp signUp) {

        Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(signUp);
        Log.e("data shared 1 : ", ""+json);

        prefsEditor.putString("signUp", json);

        String signUpjson = preferences.getString("signUp", "");

        Log.e("data shared 2 : ", ""+signUpjson);
        prefsEditor.commit();
    }

}


