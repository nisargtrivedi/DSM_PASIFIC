package com.dsm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Regions;
import com.dsm.R;
import com.dsm.databinding.ActivityResetPasswordBinding;
import com.dsm.ui.util.AppPreferences;
import com.dsm.ui.util.Constants;
import com.dsm.ui.util.Util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPassword extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    private String poolID = Constants.poolID;
    private String clientID = Constants.clientID;
    private String clientSecret = Constants.clientSecret;
    //    private Regions awsRegion = Regions.valueOf("ap-southeast-2");
    private Regions awsRegion = Constants.awsRegion;

    private CognitoUserPool userPool;
    private String userPassword;
    AppPreferences appPreferences;
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

        binding= DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        userPool = new CognitoUserPool(this, this.poolID, this.clientID, this.clientSecret, this.awsRegion);

        if(getIntent().getExtras()!=null) {

            binding.edtEmail.setText(getIntent().getStringExtra("email"));
            binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "Please enter email address");
                    }else if (!Util.isValidEmail(binding.edtEmail.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "Please enter proper email address");
                    }else if (TextUtils.isEmpty(binding.etPassword.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "Please enter old password");
                    }else if (TextUtils.isEmpty(binding.etNewpassword.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "Please enter new password");
                    }else if (binding.etNewpassword.getText().toString().length()<8) {
                        Util.showDialog(ResetPassword.this, "New Password should be minimum 8 character");
                    } else if (!isValidPassword(binding.etNewpassword.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "New Password should be minimum 8 character , at least one digit , at least one special char , at least one upper case.");
                    }else if (TextUtils.isEmpty(binding.etReNewpassword.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "Please enter confirm password");
                    }else if (binding.etReNewpassword.getText().toString().length()<8) {
                        Util.showDialog(ResetPassword.this, "Confirm Password should be minimum 8 character");
                    }else if (!isValidPassword(binding.etReNewpassword.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "Confirm Password should be minimum 8 character , at least one digit , at least one special char , at least one upper case.");
                    }else if (binding.etPassword.getText().toString().equalsIgnoreCase(binding.etNewpassword.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "Old Password and New password not same");
                    }else if (!binding.etNewpassword.getText().toString().equalsIgnoreCase(binding.etReNewpassword.getText().toString())) {
                        Util.showDialog(ResetPassword.this, "New Password and Confirm Password not match");
                    } else {
                        if (DSM.getInstance().isNetworkAvailable()) {
//                            ChallengeContinuation continuation = (ChallengeContinuation) getIntent().getSerializableExtra("config");
//                            NewPasswordContinuation newPasswordContinuation = (NewPasswordContinuation) continuation;
//                            newPasswordContinuation.setPassword(binding.etNewpassword.getText().toString());
//                            continuation.continueTask();
                            Util.showDialog(ResetPassword.this);
                            CognitoUser cognitoUser = userPool.getUser(binding.edtEmail.getText().toString());
                            userPassword = getIntent().getStringExtra("password");
                            cognitoUser.getSessionInBackground(new AuthenticationHandler() {
                                @Override
                                public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                                    Util.hideDialog();
                                    appPreferences.set("ACCESS_TOKEN", userSession.getAccessToken().getJWTToken());
                                    appPreferences.set("USERNAME", userSession.getUsername());
                                    appPreferences.set("ID_TOKEN", userSession.getIdToken().getJWTToken());
                                    appPreferences.set("REFRESH_TOKEN", userSession.getRefreshToken().getToken());

                                    startActivity(new Intent(ResetPassword.this, SelectPortalActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    );
                                }

                                @Override
                                public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                                    AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
                                    // Pass the user sign-in credentials to the continuation
                                    authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                                    // Allow the sign-in to continue
                                    authenticationContinuation.continueTask();

                                }

                                @Override
                                public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

                                }

                                @Override
                                public void authenticationChallenge(ChallengeContinuation continuation) {
                                    Util.hideDialog();
                                    if ("NEW_PASSWORD_REQUIRED".equalsIgnoreCase(continuation.getChallengeName().toString())) {
                                        NewPasswordContinuation newPasswordContinuation = (NewPasswordContinuation) continuation;
                                        newPasswordContinuation.setPassword(binding.etNewpassword.getText().toString());
                                        continuation.continueTask();
                                    }
                                }

                                @Override
                                public void onFailure(Exception exception) {
                                    Util.hideDialog();
                                    Toast.makeText(ResetPassword.this, "Sign in Failure", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Util.showDialog(ResetPassword.this, "No Internet Available");
                        }
                    }
                }
            });

        }
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
