package com.dsm.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dsm.R;
import com.dsm.ui.model.JewelleryCategoryModel;
import com.dsm.ui.model.JewelleryModel;

import java.util.List;

public class ModelAdapter extends BaseAdapter {

    Context mContext;
    int layoutResourceId;
   List<JewelleryModel.Metals> list;

    public ModelAdapter(Context mContext, List<JewelleryModel.Metals> list) {
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
        listItem = LayoutInflater.from(mContext).inflate(R.layout.spinner_row_two, parent, false);

        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        JewelleryModel.Metals folder = list.get(position);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewName.setText(Html.fromHtml(folder.getMetalName(),Html.FROM_HTML_MODE_COMPACT));
        }else{
            textViewName.setText(Html.fromHtml(folder.getMetalName()));
        }
        return listItem;
    }
}