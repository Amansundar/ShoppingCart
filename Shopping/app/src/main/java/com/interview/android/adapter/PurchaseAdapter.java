package com.interview.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interview.android.dto.ProductObject;
import com.interview.android.shoppingcart.R;

import java.util.ArrayList;
public class PurchaseAdapter extends BaseAdapter {

  Context mContext;
  String inflater = Context.LAYOUT_INFLATER_SERVICE;
  LayoutInflater mLayoutInflater = null;
  ArrayList<ArrayList<ProductObject>> mStoredlist = new ArrayList<ArrayList<ProductObject>>();

  public PurchaseAdapter(Context context, ArrayList<ArrayList<ProductObject>> storedlist) {
    super();
    mContext = context;
    mStoredlist = storedlist;
    mLayoutInflater = (LayoutInflater) mContext.getSystemService(inflater);
  }

  @Override
  public int getCount() {
    return mStoredlist.size();
  }

  @Override
  public Object getItem(int position) {
    return mStoredlist.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  private class ViewHolder {
    TextView nameView;

    public ViewHolder(View view) {
      nameView = (TextView) view.findViewById(R.id.info_purchase);
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    final ArrayList<ProductObject> productObject = mStoredlist.get(position);
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = mLayoutInflater.inflate(R.layout.purchaseitem, null, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    } else
      viewHolder = (ViewHolder) convertView.getTag();
    //viewHolder.mCheckBox.setTag(productObject);
    viewHolder.nameView.setText(productObject.get(0).getDate());

    return convertView;
  }
}
