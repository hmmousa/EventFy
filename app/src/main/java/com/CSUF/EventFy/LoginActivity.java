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
import com.CSUF.Notifications.GCMNotificationIntentService;
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
    String  userId;
    String password;
    SignUp signUp;

    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String signUpjson = preferences.getString("signUp", "");

        if (signUpjson.length() > 5 && signUpjson != null) {

            Gson gson = new Gson();
            SignUp signUp = gson.fromJson(signUpjson, SignUp.class);
            onLoginSuccess(signUp, true);
        } else {


            FacebookSdk.sdkInitialize(getApplicationContext());

            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(callbackManager!=null)
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

                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {

                                            facebookErrorMsg();
                                        } else {
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                //  String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_UserName = json.getString("name");
                                                //  String str_lastname = json.getString("last_name");

                                                Log.e("fb id : ", "" + str_id);
                                                SignUp signUp = new SignUp();
                                                signUp.setUserId(str_id);
                                                signUp.setIsFacebook("true");
                                                signUp.setUserName(str_UserName);
                                                signUp.setImageUrl("https://graph.facebook.com/" + signUp.getUserId() + "/picture?type=large");
                                                onLoginSuccess(signUp, false);

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
                        //facebookErrorMsg();
                    }

                });
    }


    public void facebookErrorMsg()
    {

//        new AlertDialog.Builder(this.getApplicationContext())
//                .setTitle("Facebook Login")
//                .setMessage("Error while login plase retry")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();

    }


    public void login() {



        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        _loginButton.setEnabled(false);
                        userId = _emailText.getText().toString();
                        password = _passwordText.getText().toString();

                        String result = null;
                        if (!validate()) {
                            onLoginFailed();
                            return;
                        } else {
                            senddata senddataObj = new senddata(true);

                            senddataObj.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        if(progressDialog!=null)
            progressDialog.dismiss();

        if (!isSetAlready)
            setsharedPref(signUp);

        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("signup", signUp);

        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
        if(callbackManager!=null)
            facebookErrorMsg();
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


    public class senddata extends AsyncTask<Void, Void, Void> {
        String responseText;

        public senddata(boolean b) {
        }

        @Override
        protected Void doInBackground(Void... strings) {

            User user = new User();
            user.setUsername(userId);
            user.setPassword(password);
            String url =  getResources().getString(R.string.ip_local)+getResources().getString(R.string.login);

            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<User> request = new HttpEntity<>(user);

            ResponseEntity<SignUp> rateResponse =
                    restTemplate.exchange(url,
                            HttpMethod.POST, request, SignUp.class);
            signUp = rateResponse.getBody();


            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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



        prefsEditor.commit();
    }

}


