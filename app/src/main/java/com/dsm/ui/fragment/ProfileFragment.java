package com.dsm.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dsm.R;
import com.dsm.databinding.FragmentProfileBinding;
import com.dsm.ui.util.AppPreferences;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    AppPreferences appPreferences;

    public static ProfileFragment INSTANCE;

    public static ProfileFragment newInstance(){
        if(INSTANCE==null){
            INSTANCE=new ProfileFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreferences=new AppPreferences(getActivity());
        binding.tvTitle.setText("Employee Email : "+appPreferences.getString("USERNAME"));
    }
}
