package com.interview.android.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.interview.android.dto.ProductObject;
import com.interview.android.interfaces.TotalValueListener;
import com.interview.android.shoppingcart.R;

import java.util.ArrayList;

public class PurchaseDetailAdapter extends ArrayAdapter<ProductObject> {

  ArrayList<ProductObject> mProductObject = new ArrayList<ProductObject>();
  Handler mHandler;
  TotalValueListener mTotalValueListener;


  public PurchaseDetailAdapter(Context context, TotalValueListener totalValueListener, ArrayList<ProductObject> items) {
    super(context, R.layout.purchase_detail_item);
    mProductObject = items;
    this.mTotalValueListener =totalValueListener;
    mHandler = new Handler();
  }

  @Override
  public int getCount() {
    return mProductObject.size();
  }


  private  class ViewHolder{
    TextView nameView;
    TextView priceView;
    TextView countView;
    View mView;
    ProductObject mProductObject;
    int count=0;

    public ViewHolder(View view) {
      nameView = (TextView) view.findViewById(R.id.name);
      priceView = (TextView) view.findViewById(R.id.price);
      countView = (TextView) view.findViewById(R.id.count);
      mView = view.findViewById(R.id.checkbox);
      countView.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
          count=Integer.parseInt(s.toString());
          mProductObject =(ProductObject) countView.getTag();
          mHandler.removeCallbacks(mRunnable);
          mHandler.postDelayed(mRunnable, 500);
        }
      });
      mView.setVisibility(View.GONE);
    }

    Runnable mRunnable = new Runnable() {
      @Override
      public void run() {
        mProductObject.setCount(count);
        mTotalValueListener.calculatePrice();
      }
    };

  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    final ProductObject productObject = mProductObject.get(position);
    ViewHolder viewHolder = null;
    if (convertView == null) {
      String inflater = Context.LAYOUT_INFLATER_SERVICE;
      LayoutInflater mLayoutInflater;
      mLayoutInflater = (LayoutInflater)getContext().getSystemService(inflater);
      convertView = mLayoutInflater.inflate( R.layout.purchase_detail_item, null, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);

    } else
      viewHolder = (ViewHolder)convertView.getTag();
    viewHolder.countView.setTag(productObject);
    viewHolder.countView.setText(String.valueOf(productObject.getCount()));
    viewHolder.nameView.setText(productObject.getName());
    viewHolder.priceView.setText("MRP : " + productObject.getPrice());

    return convertView;
  }
}