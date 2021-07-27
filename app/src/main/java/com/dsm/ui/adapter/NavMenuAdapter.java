package com.dsm.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsm.R;
import com.dsm.ui.listener.onDialogClick;
import com.dsm.ui.model.NavMenuModel;

public class NavMenuAdapter extends ArrayAdapter<NavMenuModel> {

    Context mContext;
    int layoutResourceId;
    NavMenuModel data[] = null;

    public NavMenuAdapter(Context mContext, int layoutResourceId, NavMenuModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        NavMenuModel folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewName.setText(Html.fromHtml(folder.name,Html.FROM_HTML_MODE_COMPACT));
        }else{
            textViewName.setText(Html.fromHtml(folder.name));
        }

        return listItem;
    }
}