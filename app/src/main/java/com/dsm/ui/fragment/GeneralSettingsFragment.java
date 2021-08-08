package com.dsm.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dsm.R;
import com.dsm.databinding.FragmentGeneralSettingsBinding;
import com.dsm.ui.MainNavigation;
import com.dsm.ui.util.AppPreferences;


public class GeneralSettingsFragment extends Fragment {

    FragmentGeneralSettingsBinding binding;
    AppPreferences appPreferences;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_general_settings,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreferences=new AppPreferences(getActivity());


        binding.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainNavigation)getActivity()).ProfileScreens();
           //     ((BaseActivity)getActivity()).binding.tvTitle.setText("PROFILE SETTINGS");
            //    Util.setBg(1,((BaseActivity)getActivity()).binding.img1,((BaseActivity)getActivity()).binding.img2,((BaseActivity)getActivity()).binding.img3,((BaseActivity)getActivity()).binding.img4,((BaseActivity)getActivity()).binding.img5);
            }
        });
    }
}
