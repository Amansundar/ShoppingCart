/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.interview.android.shoppingcart;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.interview.android.sqlhelper.DatabaseHandler;
import com.interview.android.dto.ProductObject;
import com.interview.android.dto.PurchaseObject;
import com.interview.android.interfaces.TotalValueListener;

import com.interview.android.adapter.ProductAdapter;
import com.interview.android.adapter.PurchaseAdapter;
import com.interview.android.adapter.PurchaseDetailAdapter;
import com.interview.android.adapter.ShoppingAdapter;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener{

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private boolean autoSync = true;
    private int menuPosition=-1;
    private DatabaseHandler databaseHandler;
    private ArrayList<ProductObject> mProductObject = new ArrayList<ProductObject>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler = new DatabaseHandler(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, getResources().getStringArray(R.array.menu_array)));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setHomeButtonEnabled(true);
        selectItem(0);
    }


    private ArrayList<ProductObject> generateProductList() {
        ArrayList<ProductObject> mProductObject = new ArrayList<ProductObject>();
        String[] productName=getResources().getStringArray(R.array.product_array);
        String[] productPrice=getResources().getStringArray(R.array.product_price_array);
        for(int i=0;i<productName.length;i++) {
            ProductObject productObject = new ProductObject(i+1,productName[i], Double.parseDouble(productPrice[i]));
            mProductObject.add(productObject);
        }
        return mProductObject;
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void setActionBarTitle(int menuPosition)
    {
        String[] tabTitle=null;
        switch(menuPosition)
        {
            case 0:
                tabTitle=getResources().getStringArray(R.array.shopping_cart_array);
                break;
            case 1:
                tabTitle=getResources().getStringArray(R.array.purchase_array);
                break;
        }
        final ActionBar actionBar =getActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int i = 0; i < tabTitle.length; i++)
            actionBar.addTab(actionBar.newTab().setText(tabTitle[i]).setTabListener(this));
    }

    public boolean isAutoSync() {
        return autoSync;
    }

    public   DatabaseHandler getDatabaseHandler(){
        return databaseHandler;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        switch(menuPosition) {
            case 0:
                switch(tab.getPosition()) {
                    case 0:
                        Fragment shoppingFragment = new ShoppingFragment(mProductObject);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, shoppingFragment).commit();
                        break;
                    case 1:
                        Fragment productFragment = new ProductFragment(mProductObject);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productFragment).commit();
                        break;
                }
                break;
            case 1:
                switch(tab.getPosition()) {
                    case 0:
                        Fragment purchaseFragment = new PurchaseFragment(databaseHandler.getAllPurchase());
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, purchaseFragment).commit();
                        break;
                    case 1:
                        Fragment shoppingFragment = new PurchaseDetailFragment(mProductObject);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, shoppingFragment).commit();
                        break;
                }
                break;

        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // mAppSectionsPagerAdapter.getItem(tab.getPosition());
    }

    private void selectItem(int position) {
      switch(position)
      {

          case 0:
              setActionBarTitle(menuPosition=0);
              mProductObject=generateProductList();
              Fragment shoppingFragment = new ShoppingFragment(mProductObject);
              getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, shoppingFragment).commit();
              break;
          case 1:
              setActionBarTitle(menuPosition=1);
              Fragment purchaseFragment = new PurchaseFragment(databaseHandler.getAllPurchase());
              getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, purchaseFragment).commit();
              break;
          case 2:
          case 3:
          case 4:
          case 5:
              Toast.makeText(getApplicationContext(), "Feature not implemented", Toast.LENGTH_SHORT).show();
              break;
      }

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(mDrawerList);
                break;
            case R.id.action_sync:
                autoSync=!autoSync;
                item.setTitle(autoSync ? "Disable sync" : "Enable sync");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class ShoppingFragment extends Fragment implements TotalValueListener,View.OnClickListener{

        ListView mListView;
        TextView mEmptyTextView;
        Button mPayButton;
        Button mWishListButton;
        TextView mTotalPrice;
        ShoppingAdapter mShoppingAdapter;
        RelativeLayout mFooterLayout;

        ArrayList<ProductObject> mProductObject = new ArrayList<ProductObject>();

        public ShoppingFragment() {

        }
        // Demonstration of a collection-browsing activity.
        public ShoppingFragment(ArrayList<ProductObject> productObject) {
            for(int i=0;i<productObject.size();i++)
                if(productObject.get(i).isEnabled())
                    mProductObject.add(productObject.get(i));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
            mListView = (ListView)rootView.findViewById(R.id.shopping_list);
            mEmptyTextView = (TextView)rootView.findViewById(R.id.empty_list);
            mFooterLayout = (RelativeLayout)rootView.findViewById(R.id.footer_Layout);
            mPayButton = (Button)rootView.findViewById(R.id.pay);
            mWishListButton = (Button)rootView.findViewById(R.id.wish_list);
            mTotalPrice = (TextView)rootView.findViewById(R.id.total_price);
            mShoppingAdapter = new ShoppingAdapter(getActivity().getApplicationContext(),this,mProductObject);
            mListView.setAdapter(mShoppingAdapter);
            if(mListView.getCount()!=0) {
                mEmptyTextView.setVisibility(View.GONE);
                mFooterLayout.setVisibility(View.VISIBLE);
            }
            mWishListButton.setOnClickListener(this);
            mPayButton.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pay:
                    // add to database if sync enabled
                    //clear count,
                    if(  ((MainActivity)getActivity()).isAutoSync()) {
                        ((MainActivity) getActivity()).getDatabaseHandler().addPurchase(mProductObject);
                    }
                    Toast.makeText((MainActivity) getActivity(), "Your payment done successfully", Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).selectItem(0);
                    break;
                case R.id.wish_list:
                    Toast.makeText((MainActivity) getActivity(), "Feature not implemented", Toast.LENGTH_SHORT).show();
                    break;
            }

        }


        @Override
        public void calculatePrice() {
            double totalAmount=0;
            for(int i=0;i<mProductObject.size();i++)
            {
                ProductObject productObject = mProductObject.get(i);
                totalAmount =totalAmount+(productObject.getPrice()*productObject.getCount());
            }
            mTotalPrice.setText("Total :"+totalAmount);
        }

    }


    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class PurchaseDetailFragment extends Fragment implements TotalValueListener{

        ListView mListView;
        TextView mEmptyTextView;
        TextView mTotalPrice;
        PurchaseDetailAdapter mPurchaseDetailAdapter;
        RelativeLayout mFooterLayout;

        ArrayList<ProductObject> mProductObject = new ArrayList<ProductObject>();

        public PurchaseDetailFragment() {

        }
        // Demonstration of a collection-browsing activity.
        public PurchaseDetailFragment(ArrayList<ProductObject> productObject) {
            for(int i=0;i<productObject.size();i++)
                if(productObject.get(i).isEnabled())
                    mProductObject.add(productObject.get(i));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_purchasedetail_list, container, false);
            mListView = (ListView)rootView.findViewById(R.id.shopping_list);
            mEmptyTextView = (TextView)rootView.findViewById(R.id.empty_list);
            mFooterLayout = (RelativeLayout)rootView.findViewById(R.id.footer_Layout);
            mTotalPrice = (TextView)rootView.findViewById(R.id.total_price);
            mPurchaseDetailAdapter = new PurchaseDetailAdapter(getActivity().getApplicationContext(),this,mProductObject);
            mListView.setAdapter(mPurchaseDetailAdapter);
            if(mListView.getCount()!=0) {
                mEmptyTextView.setVisibility(View.GONE);
                mFooterLayout.setVisibility(View.VISIBLE);
            }
            return rootView;
        }

        @Override
        public void calculatePrice() {
            double totalAmount=0;
            for(int i=0;i<mProductObject.size();i++)
            {
                ProductObject productObject = mProductObject.get(i);
                totalAmount =totalAmount+(productObject.getPrice()*productObject.getCount());
            }
            mTotalPrice.setText("Total :"+totalAmount);
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class ProductFragment extends Fragment {

        ListView mListView;
        ProductAdapter mProductAdapter;
        ArrayList<ProductObject> mProductObject = new ArrayList<ProductObject>();

        public ProductFragment() {

        }
        // Demonstration of a collection-browsing activity.
        public ProductFragment(ArrayList<ProductObject> productObject) {
            this.mProductObject = productObject;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
            mListView = (ListView)rootView.findViewById(R.id.product_list);
            mProductAdapter = new ProductAdapter(getActivity().getApplicationContext(),mProductObject);
            mListView.setAdapter(mProductAdapter);
            return rootView;
        }

    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class PurchaseFragment extends Fragment implements AdapterView.OnItemClickListener{

        ListView mListView;
        PurchaseAdapter mPurchaseAdapter;
        ArrayList<PurchaseObject> purchaseObject = new ArrayList<PurchaseObject>();
        ArrayList<ArrayList<ProductObject>>   mStoredlist = new ArrayList<ArrayList<ProductObject>>();
        public PurchaseFragment() {
        }

        public PurchaseFragment(ArrayList<ArrayList<ProductObject>> storedlist) {
            this.mStoredlist = storedlist;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
            mListView = (ListView)rootView.findViewById(R.id.product_list);
            mPurchaseAdapter = new PurchaseAdapter(getActivity().getApplicationContext(),mStoredlist);
            mListView.setAdapter(mPurchaseAdapter);
            mListView.setOnItemClickListener(this);
            return rootView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ((MainActivity)getActivity()).mProductObject = mStoredlist.get(position);
            ActionBar myActionBar = getActivity().getActionBar();
            ActionBar.Tab tab1 = myActionBar.getTabAt(1);
            tab1.select();
        }
    }
}
