package com.dsm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.regions.Regions;
import com.dsm.R;
import com.dsm.databinding.ActivityForgotPasswordBinding;
import com.dsm.ui.util.AppPreferences;
import com.dsm.ui.util.Constants;
import com.dsm.ui.util.KeyBoardHandling;
import com.dsm.ui.util.Util;


public class ForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    private String poolID = Constants.poolID;
    private String clientID = Constants.clientID;
    private String clientSecret = Constants.clientSecret;
    //    private Regions awsRegion = Regions.valueOf("ap-southeast-2");
    private Regions awsRegion = Constants.awsRegion;

    private CognitoUserPool userPool;
    private String userPassword;
    AppPreferences appPreferences;
    ForgotPasswordContinuation forgotPasswordContinuation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        appPreferences=new AppPreferences(this);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            byte[] base64decodedClientID = Base64.getDecoder().decode(this.clientID);
//            byte[] base64decodedClientSecret = Base64.getDecoder().decode(this.clientSecret);
//            byte[] base64decodedClientPool = Base64.getDecoder().decode(this.poolID);
//            try {
//                this.clientID=new String(base64decodedClientID, "utf-8");
//                this.clientSecret=new String(base64decodedClientSecret, "utf-8");
//                this.poolID=new String(base64decodedClientPool, "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }

        binding= DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        userPool = new CognitoUserPool(this, this.poolID, this.clientID, this.clientSecret, this.awsRegion);



            //binding.edtEmail.setText(getIntent().getStringExtra("email"));
            binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyBoardHandling.hideSoftKeyboard(ForgotPassword.this);

//
                    if (TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                        Util.showDialog(ForgotPassword.this, "Please enter email address");
                    }else if (!Util.isValidEmail(binding.edtEmail.getText().toString())) {
                        Util.showDialog(ForgotPassword.this, "Please enter proper email address");
                    } else {
                        if (DSM.getInstance().isNetworkAvailable()) {
//                            ChallengeContinuation continuation = (ChallengeContinuation) getIntent().getSerializableExtra("config");
//                            NewPasswordContinuation newPasswordContinuation = (NewPasswordContinuation) continuation;
//                            newPasswordContinuation.setPassword(binding.etNewpassword.getText().toString());
//                            continuation.continueTask();

                            CognitoUser cognitoUser = userPool.getUser(binding.edtEmail.getText().toString());

                            if(binding.btnSignIn.getText().toString().equalsIgnoreCase("Send Code")){
                                Util.showDialog(ForgotPassword.this);
                                cognitoUser.forgotPasswordInBackground(new ForgotPasswordHandler() {
                                    @Override
                                    public void onSuccess() {
                                        Util.hideDialog();
                                        Util.showDialog(ForgotPassword.this, "Password Change successful");
                                        finish();
                                    }

                                    @Override
                                    public void getResetCode(ForgotPasswordContinuation continuation) {

                                        Util.hideDialog();
                                        CognitoUserCodeDeliveryDetails codeSent = continuation.getParameters();
                                        Log.d("RESET CODE------>", codeSent.getAttributeName());
                                        Log.d("RESET CODE------>", continuation.toString());
                                        Util.showDialog(ForgotPassword.this, "Send code on you email address");
                                        //binding.etCode.setText(codeSent.getDestination());
                                        forgotPasswordContinuation = continuation;
                                        binding.tiCode.setVisibility(View.VISIBLE);
                                        binding.tiPass.setVisibility(View.VISIBLE);
                                        binding.btnSignIn.setText("Reset Your Password");

                                    }

                                    @Override
                                    public void onFailure(Exception exception) {
                                        Util.hideDialog();
                                        Util.showDialog(ForgotPassword.this, "New Password should have one special character");
                                        Log.d("PASSWORD CHANGE FAILED------>", exception.getLocalizedMessage());
                                    }
                                });
                            }else {
                                if (TextUtils.isEmpty(binding.etCode.getText().toString())) {
                                    Util.showDialog(ForgotPassword.this, "Please enter code");
                                } else if (TextUtils.isEmpty(binding.etNewpassword.getText().toString())) {
                                    Util.showDialog(ForgotPassword.this, "Please enter new password");
                                }else if (binding.etNewpassword.getText().toString().length()<8) {
                                    Util.showDialog(ForgotPassword.this, "New Password should be minimum 8 character");
                                } else if (!isValidPassword(binding.etNewpassword.getText().toString())) {
                                    Util.showDialog(ForgotPassword.this, "New Password should be minimum 8 character , at least one digit , at least one special char , at least one upper case.");
                                }else {
                                    Util.showDialog(ForgotPassword.this);
                                    forgotPasswordContinuation.setPassword(binding.etNewpassword.getText().toString());
                                    forgotPasswordContinuation.setVerificationCode(binding.etCode.getText().toString());
                                    forgotPasswordContinuation.continueTask();
                                }
                            }
                        } else {
                            Util.showDialog(ForgotPassword.this, "No Internet Available");
                        }
                    }
                }
            });


    }

    public boolean isValidPassword(final String password) {

        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean specialCh = false;
        for(int i=0;i < password.length();i++) {
            ch = password.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;

    }
}
