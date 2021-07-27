package com.dsm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dsm.ui.util.ProgressD;

import org.jetbrains.annotations.NotNull;

public class BaseFragment extends Fragment {

    public ProgressD progressD;
    public Toast toast;
    @Nullable
    @org.jetbrains.annotations.Nullable

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void showToast(String msg,Context context){
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        toast.show();
    }
}
