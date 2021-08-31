package com.dsm.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dsm.R;
import com.dsm.databinding.FragmentProfileBinding;
import com.dsm.ui.model.BaseModel;
import com.dsm.ui.model.ProfileModel;
import com.dsm.ui.network.APIInterface;
import com.dsm.ui.network.RetrofitBuilder;
import com.dsm.ui.repo.ProfileRepo;
import com.dsm.ui.util.AppPreferences;
import com.dsm.ui.util.Resource;
import com.dsm.ui.viewmodel.ProfileViewModel;
import com.dsm.ui.viewmodel.ViewModelFactory;


public class ProfileFragment extends BaseFragment {

    FragmentProfileBinding binding;
    AppPreferences appPreferences;

    public static ProfileFragment INSTANCE;

    public static ProfileFragment newInstance(){
        if(INSTANCE==null){
            INSTANCE=new ProfileFragment();
        }
        return INSTANCE;
    }

    ProfileViewModel viewModel;

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
        viewModel=new ViewModelProvider(this,new ViewModelFactory(RetrofitBuilder.INSTANCE.getApiService())).get(ProfileViewModel.class);
        showLoading(getActivity());
        viewModel.getProfile(appPreferences.getString("EMAIL")).observe(getActivity(), new Observer<Resource<ProfileModel>>() {
            @Override
            public void onChanged(Resource<ProfileModel> baseModelResource) {
//                Log.d("Resepone",baseModelResource.getMessage());
                hideLoading();
                if(baseModelResource!=null && baseModelResource.getData()!=null){
                    try {
                        binding.tvTitle.setText("Customer Email : " + appPreferences.getString("EMAIL") + "\n" + "Company Name : " + baseModelResource.getData().user.userModel.getCompany_name());
                    }catch (Exception ex){
                        binding.tvTitle.setText("Customer Email : " + appPreferences.getString("EMAIL") + "\n" + "Company Name : " + appPreferences.getString("COMPANY"));
                    }
                }
            }
        });
        //binding.tvTitle.setText("Customer Email : "+appPreferences.getString("EMAIL")+"\n"+"Company Name : "+appPreferences.getString("COMPANY"));
    }
}
