package com.dsm.ui.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

public class Util {

    public static ProgressDialog dialog;
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void showDialog(Context context,String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setTitle("Alert");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void showDialog(Context context){
        if(dialog==null){
            dialog=new ProgressDialog(context);
            dialog.setMessage("Loading..");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }
    }
    public static void hideDialog(){
        if(dialog!=null){
            dialog.cancel();
            dialog.dismiss();
            dialog=null;
        }
    }

}
