package com.dsm.ui.model;

import com.google.gson.annotations.SerializedName;

public class ProfileModel extends BaseModel{

    @SerializedName("data")
    public User user;
    public class User{
        @SerializedName("user")
        public UserModel userModel;

    }
}
