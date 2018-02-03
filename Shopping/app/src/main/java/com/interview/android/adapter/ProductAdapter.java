package com.interview.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.interview.android.dto.ProductObject;
import com.interview.android.shoppingcart.R;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

  Context mContext;
  String inflater = Context.LAYOUT_INFLATER_SERVICE;
  LayoutInflater mLayoutInflater=null;
  ArrayList<ProductObject> mProductObject = new ArrayList<ProductObject>();

  public ProductAdapter(Context context,ArrayList<ProductObject> items) {
    super();
    mContext=context;
    mProductObject = items;
    mLayoutInflater = (LayoutInflater)mContext.getSystemService(inflater);
  }

  @Override
  public int getCount() {
    return mProductObject.size();
  }

  @Override
  public Object getItem(int position) {
    return mProductObject.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  private  class ViewHolder{
    TextView nameView;
    TextView priceView;
    EditText countView;
    View mCheckBox;

    public ViewHolder(View view){
      nameView = (TextView)view.findViewById(R.id.name);
      priceView = (TextView)view.findViewById(R.id.price);
      countView = (EditText)view.findViewById(R.id.count);
      mCheckBox = view.findViewById(R.id.checkbox);
      mCheckBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          ProductObject productObject =(ProductObject) mCheckBox.getTag();
                  (productObject).setEnabled(!productObject.isEnabled());
          mCheckBox.setBackgroundColor(mContext.getResources().getColor(productObject.isEnabled() ? R.color.color_blue : R.color.color_white));
        }
      });
      countView.setVisibility(View.GONE);
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    final ProductObject productObject = mProductObject.get(position);
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = mLayoutInflater.inflate( R.layout.productitem, null, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    }else
      viewHolder = (ViewHolder)convertView.getTag();
      viewHolder.mCheckBox.setTag(productObject);
      viewHolder.mCheckBox.setBackgroundColor(mContext.getResources().getColor(productObject.isEnabled()?R.color.color_blue:R.color.color_white));
      viewHolder.nameView.setText(productObject.getName());
      viewHolder.priceView.setText("MRP : " + productObject.getPrice());

    return convertView;
  }
}