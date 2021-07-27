package com.dsm.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsm.R;
import com.dsm.ui.model.JewelleryCategoryModel;
import com.dsm.ui.model.NavMenuModel;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    Context mContext;
    int layoutResourceId;
   List<JewelleryCategoryModel> list;

    public SpinnerAdapter(Context mContext,List<JewelleryCategoryModel> list) {
        this.mContext = mContext;
        this.list = list;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        listItem = LayoutInflater.from(mContext).inflate(R.layout.spinner_row, parent, false);

        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        JewelleryCategoryModel folder = list.get(position);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewName.setText(Html.fromHtml(folder.getAttribute_name(),Html.FROM_HTML_MODE_COMPACT));
        }else{
            textViewName.setText(Html.fromHtml(folder.getAttribute_name()));
        }
        return listItem;
    }
}