package com.dsm.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dsm.R;
import com.dsm.ui.adapter.SearchItemAdapter;
import com.dsm.ui.listener.onSearchItemClick;
import com.dsm.ui.model.SearchItemModel;
import com.dsm.ui.util.MailCredentials;
import com.dsm.ui.util.MailSender;
import com.dsm.ui.util.ProgressD;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    boolean onCheck = true;
    public ProgressD progressD;
    public Toast toast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void openSelectedImageDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_image,null,false);
        builder1.setView(view);
        builder1.setCancelable(true);
        AlertDialog alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert11.show();


        //Open Dialog with 50% width and height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alert11.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.5f);
        int dialogWindowHeight = (int) (displayHeight * 0.5f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        alert11.getWindow().setGravity(Gravity.CENTER);
        alert11.getWindow().setAttributes(layoutParams);
    }

    public void openSelectedImageDialog(String imagePath,String lot){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_image,null,false);
        ImageView img=view.findViewById(R.id.img);
        TextView tLot=view.findViewById(R.id.tLot);
        if(imagePath.isEmpty()){
            img.setImageResource(R.drawable.diamond);
        }else{
            Glide.with(this)
                    .load(imagePath)
                    .into(img);
        }
        tLot.setText(lot);
        builder1.setView(view);
        builder1.setCancelable(true);
        AlertDialog alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert11.show();


        //Open Dialog with 50% width and height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alert11.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.5f);
        int dialogWindowHeight = (int) (displayHeight * 0.5f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        alert11.getWindow().setGravity(Gravity.CENTER);
        alert11.getWindow().setAttributes(layoutParams);
    }

    public void openDialog(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.email_dialog,null,false);
        builder1.setView(view);
        builder1.setCancelable(true);
        AlertDialog alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert11.show();
        TextView btnClose = view.findViewById(R.id.btnClose);
        TextView tvCheck = view.findViewById(R.id.tvCheck);
        MaterialButton btnSend=view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("MESSAGE-->"+msg);
                        MailSender mailSender=new MailSender(MailCredentials.INSTANCE.getMAIL(), MailCredentials.INSTANCE.getPASSWORD());
                        try {
                            mailSender.sendMail("Hello",MailCredentials.INSTANCE.EmailTemplate("nisarg",msg,"Hello Test").toString(),MailCredentials.INSTANCE.getMAIL(),"nisarg.trivedi786@gmail.com");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
        tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCheck) {
                    tvCheck.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_rectangle, 0, 0, 0);
                    onCheck = false;
                }else{
                    tvCheck.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_chk, 0, 0, 0);
                    onCheck = true;
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert11.cancel();
                alert11.dismiss();
            }
        });


        //Open Dialog with 50% width and height
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int displayWidth = displayMetrics.widthPixels;
//        int displayHeight = displayMetrics.heightPixels;
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(alert11.getWindow().getAttributes());
//        int dialogWindowWidth = (int) (displayWidth * 0.5f);
//        int dialogWindowHeight = (int) (displayHeight * 0.5f);
//        layoutParams.width = dialogWindowWidth;
//        layoutParams.height = dialogWindowHeight;
//        alert11.getWindow().setGravity(Gravity.CENTER);
//        alert11.getWindow().setAttributes(layoutParams);
    }

    public void showLoading(Context context){
        //progressD=new ProgressD(context);
        progressD = ProgressD.show(context, "Loading", false, false);
    }
    public void hideLoading(){
        if (progressD != null && progressD.isShowing()) {
            progressD.cancel();
            progressD.dismiss();
            progressD = null;
        }
    }

    //Email Validation
    public Boolean isEmail(String s) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }
    //Phone Validation
    public Boolean isPhones(String s){
        return android.util.Patterns.PHONE.matcher(s).matches();
    }


    public void showToast(String msg){
        if(toast!=null){
            toast.cancel();
        }
        toast=Toast.makeText(this,msg,Toast.LENGTH_SHORT);
        toast.show();
    }

    public void openSearch(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_search,null,false);

        ImageView imgClose=view.findViewById(R.id.imgClose);
        RecyclerView rvSearchItems=view.findViewById(R.id.rvSearchItems);
        RecyclerView rvSearchColor=view.findViewById(R.id.rvSearchColor);
        RecyclerView rvCut=view.findViewById(R.id.rvCut);
        RecyclerView rvPol=view.findViewById(R.id.rvPol);
        RecyclerView rvSym=view.findViewById(R.id.rvSym);
        RecyclerView rvClarity=view.findViewById(R.id.rvClarity);

//        loadSearchItem(rvSearchItems);
//        loadColor(rvSearchColor);
//        loadCut(rvCut);
//        loadCut(rvPol);
//        loadCut(rvSym);
//        loadClarity(rvClarity);
        builder1.setView(view);
        builder1.setCancelable(true);
        AlertDialog alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert11.show();

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.hide();
                alert11.dismiss();
            }
        });

        //Open Dialog with 90% width and height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alert11.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 1f);
        int dialogWindowHeight = (int) (displayHeight * 1f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        alert11.getWindow().setAttributes(layoutParams);
    }



}
