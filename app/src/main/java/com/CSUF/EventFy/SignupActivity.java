package com.CSUF.EventFy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.CSUF.EventFy_Beans.SignUp;
import com.CSUF.EventFy_Beans.User;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements OnDateSetListener {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_DOB) TextView _dobtext;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    private boolean isEmail;

    ProgressDialog progressDialog=null;
    public static final String DATEPICKER_TAG = "datepicker";

    private ValidateUserId validateUserId;

@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(SignupActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

        _dobtext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1910, calendar.get(Calendar.YEAR) - 10);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }

        });

    }


    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    private boolean isCloseOnSingleTapMinute() {
        return false;
    }

    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        _dobtext.setText(year + "-" + month + "-" + day);
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }


        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        _signupButton.setEnabled(false);


        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        try {

                            validateUserId = new ValidateUserId(true);
                            String result = validateUserId.execute(_emailText.getText().toString(), _passwordText.getText().toString()).get();

                            if (result != null && result.equals("Success")) {
                                SignUp signUp = new SignUp();
                                signUp.setUserId(_emailText.getText().toString());
                                signUp.setPassword(_passwordText.getText().toString());
                                signUp.setIsVerified("false");
                                signUp.setIsFacebook("false");
                                signUp.setImageUrl("default");
                                signUp.setDob(_dobtext.getText().toString());
                                signUp.setUserName(_nameText.getText().toString());
                                    onSignupSuccess(signUp);
                            }
                            else {
                                onSignupFailed();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        // onSignupFailed();

                    }
                    }, 500);
    }


    public void onSignupSuccess(SignUp signUp) {
        _signupButton.setEnabled(true);

        finish();
        Intent intent = new Intent(SignupActivity.this, VerifyAccount.class);
        intent.putExtra("signup", signUp);

        progressDialog.dismiss();
        startActivity(intent);

     //   setResult(RESULT_OK, null);
   //     finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String dob = _dobtext.getText().toString();

        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            isEmail = true;
        }else{
             isEmail = false;
        }

        if (name.isEmpty() || name.length() < 3 ) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (dob.isEmpty()){
            _dobtext.setError("Enter a valid Date of Birth");
        valid = false;}
        else
        { _dobtext.setError(null);
        }


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


    class ValidateUserId extends AsyncTask<String, String, String>
    {

        public ValidateUserId(String email, String password)
        {

        }

        public ValidateUserId(boolean b) {
            super();

        }
        @Override
        protected String doInBackground(String... strings) {

            String url =  getResources().getString(R.string.ip_local)+getResources().getString(R.string.signup_checkuseridvalid);

            Log.e("string : ", ""+strings[0]);
            Log.e("string : ", ""+strings[1]);

            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            User user = new User();
            user.setUsername(strings[0]);
            user.setPassword(strings[1]);

            HttpEntity<User> request = new HttpEntity<>(user);

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
        }
    }

}