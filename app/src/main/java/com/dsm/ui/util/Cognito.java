package com.dsm.ui.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.dsm.ui.MainActivity;
import com.dsm.ui.ResetPassword;
import com.dsm.ui.SelectPortalActivity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Cognito {

    private String poolID = Constants.poolID;
    private String clientID = Constants.clientID;
    private String clientSecret = Constants.clientSecret;
//    private Regions awsRegion = Regions.valueOf("ap-southeast-2");
        private Regions awsRegion = Constants.awsRegion;

    public CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;       // Used for adding attributes to the user
    private Context appContext;
    private String userPassword;
    AppPreferences appPreferences;
    public String userEMAIl;

    public Cognito(Context context){
        appContext = context;
        appPreferences=new AppPreferences(context);
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
        userPool = new CognitoUserPool(context, this.poolID, this.clientID, this.clientSecret, this.awsRegion);
        userAttributes = new CognitoUserAttributes();
    }
    public void userLogin(String userId, String password){
        //CognitoUser cognitoUser =  userPool.getUser(userId);
        Util.showDialog(appContext);
        CognitoUser cognitoUser =  userPool.getUser(userId);
        this.userPassword = password;
        this.userEMAIl = userId;
        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    public void userLogout(Activity activity, String userId, AlertDialog alertDialog){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        cognitoUser.signOut();
        alertDialog.hide();alertDialog.dismiss();
        activity.finishAffinity();
        activity.startActivity(new Intent(appContext, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
        );
    }
    // Callback handler for the sign-in process
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.d("Challange--->",continuation.getChallengeName());

            Util.hideDialog();
            if ("NEW_PASSWORD_REQUIRED".equalsIgnoreCase(continuation.getChallengeName().toString())) {
                appContext.startActivity(new Intent(appContext, ResetPassword.class)
                .putExtra("email",userEMAIl)
                                .putExtra("password",userPassword)
                );
            }else if ("FORCE_CHANGE_PASSWORD".equalsIgnoreCase(continuation.getChallengeName().toString())) {
                appContext.startActivity(new Intent(appContext, ResetPassword.class)
                        .putExtra("email",userEMAIl)
                        .putExtra("password",userPassword)
                );
            }

//            CognitoUser cognitoUser =  userPool.getUser("yankitpatel91@gmail.com");
//            cognitoUser.forgotPasswordInBackground(new ForgotPasswordHandler() {
//                @Override
//                public void onSuccess() {
//                    Log.d("CHANGE PASSWORD SUCCESS","SUCCESS");
//                }
//
//                @Override
//                public void getResetCode(ForgotPasswordContinuation continuation) {
//
//                    Log.d("CHANGE PASSWORD RESET CODE---",continuation.getParameters().getDestination().toString());
//                    Log.d("CHANGE PASSWORD RESET---",continuation.getParameters().getAttributeName().toString());
//                   // continuation.continueTask();
//                }
//
//                @Override
//                public void onFailure(Exception exception) {
//                    Log.d("CHANGE ERROR---",exception.toString());
//                }
//            });


        }
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            //Toast.makeText(appContext,"Sign in success", Toast.LENGTH_LONG).show();
            //Log.d("SESSION--->",userSession.getIdToken().toString());
            //System.out.println("Called Session------"+userSession.getRefreshToken().getToken());

            Util.hideDialog();
            appPreferences.set("ACCESS_TOKEN",userSession.getAccessToken().getJWTToken());
            appPreferences.set("USERNAME",userSession.getUsername());
            appPreferences.set("EMAIL",userEMAIl);
            appPreferences.set("ID_TOKEN",userSession.getIdToken().getJWTToken());
            appPreferences.set("REFRESH_TOKEN",userSession.getRefreshToken().getToken());

            appContext.startActivity(new Intent(appContext, SelectPortalActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
            );
        }
        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            // The API needs user sign-in credentials to continue
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
            // Pass the user sign-in credentials to the continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);
            // Allow the sign-in to continue
            authenticationContinuation.continueTask();

            System.out.println("Called");
        }
        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            // Multi-factor authentication is required; get the verification code from user
            //multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
            // Allow the sign-in process to continue
            //multiFactorAuthenticationContinuation.continueTask();
        }
        @Override
        public void onFailure(Exception exception) {
            // Sign-in failed, check exception for the cause
            Util.hideDialog();
            Toast.makeText(appContext,"Sign in Failure", Toast.LENGTH_LONG).show();
        }
    };
}
