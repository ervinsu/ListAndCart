package com.example.ervin.listcart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior mBottomSheetBehavior;
    private static RequestQueue queue;
    private static TextView tvTotalPrice;
    private static NotificationBadge mBadge;
    private static RecyclerView recyclerViewProductList;
    private static RecyclerView recyclerViewCartList;
    private static List<Product> productList = new ArrayList<>();
    private static List<Product> cartList = new ArrayList<>();
    private static Context context;
    private static CartAdapter adapterRvBelow;
    private static ProductRepurchaseAdapter adapterRvTop;
    private static int totalPrice = 0;
    private static int count = 0;
    private static RelativeLayout pbRepurchase;
    static String url = "http://pharmanet.apodoc.id/select_repurchase_owner.php?id=";
    static String urlbawah = "http://pharmanet.apodoc.id/selectCartOwner.php";
    static String urlbawah_new= "http://pharmanet.apodoc.id/select_currentCart_owner_new.php?id=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        queue = Volley.newRequestQueue(this);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        mBadge = findViewById(R.id.badge);
        pbRepurchase = findViewById(R.id.progressBarRePurchase);
        initBottomSheet();
        initiateTopAdapter();
        initiateBelowAdapter();
    }

    private void initiateTopAdapter()
    {
        recyclerViewProductList = findViewById(R.id.rvProductList);
        recyclerViewProductList.setHasFixedSize(true);
        LinearLayoutManager setLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewProductList.setLayoutManager(setLayout);
        //cartList.clear();
        //add product
        show_view(cartList,url,1);
    }

    public void initiateBelowAdapter(){
        recyclerViewCartList = findViewById(R.id.rvCartList);
        recyclerViewCartList.setHasFixedSize(true);
        LinearLayoutManager setLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewCartList.setLayoutManager(setLayout);
        show_cart(urlbawah_new,1);
    }

    public static void refresh_cart(List<Product> cartList){
        count=0;
        totalPrice=0;

        for (int i = 0; i < cartList.size(); i++) {
            totalPrice+= cartList.get(i).productPrice*cartList.get(i).productQty;
            count += cartList.get(i).productQty;
        }
        tvTotalPrice.setText(ConvertNominal(totalPrice));
        mBadge.setNumber(count);
        adapterRvBelow = new CartAdapter(context,cartList,1);
        recyclerViewCartList.setAdapter(adapterRvBelow);
    }

    public static void refresh_total_cart(List<Product> cartList){
        count=0;
        totalPrice=0;

        for (int i = 0; i < cartList.size(); i++) {
            totalPrice+= cartList.get(i).productPrice*cartList.get(i).productQty;
            count += cartList.get(i).productQty;
        }
        tvTotalPrice.setText(ConvertNominal(totalPrice));
        mBadge.setNumber(count);
    }

public static void show_cart(String urlbawah, int UserID) {

    pbRepurchase.setVisibility(View.VISIBLE);
    JsonObjectRequest rec = new JsonObjectRequest(urlbawah+UserID, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONArray products = null;
            try {
                products = response.getJSONArray("result");
                cartList.clear();
                count=0;
                totalPrice=0;
                for (int i = 0; i < products.length(); i++) {
                    try {
                        recyclerViewCartList.setVisibility(View.VISIBLE);
                        JSONObject obj = products.getJSONObject(i);
                        cartList.add(new Product(obj.getString("ProductName")
                                ,obj.getString("ProductID")
                                ,obj.getInt("CartProductPrice"),
                                obj.getInt("CartProductQty")
                        ));
                        pbRepurchase.setVisibility(View.GONE);
                        totalPrice += obj.getInt("CartProductQty")*obj.getInt("CartProductPrice");
                        count += obj.getInt("CartProductQty");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                tvTotalPrice.setText(ConvertNominal(totalPrice));
                mBadge.setNumber(count);
                //Toast.makeText(context, cartList.size()+"", Toast.LENGTH_SHORT).show();
                adapterRvBelow = new CartAdapter(context,cartList,1);
                recyclerViewCartList.setAdapter(adapterRvBelow);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Main2Activity.this, "error", Toast.LENGTH_SHORT).show();
        }
    });
    queue.add(rec);
}

    public static void show_view(final List<Product> cartList, String url, int userID){
        pbRepurchase.setVisibility(View.VISIBLE);
        JsonObjectRequest rec = new JsonObjectRequest(url+userID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray products = null;
                try {

                    products = response.getJSONArray("result");
                    productList.clear();

                    for (int i = 0; i < products.length(); i++) {
                        try {
                            recyclerViewProductList.setVisibility(View.VISIBLE);
                            JSONObject obj = products.getJSONObject(i);
                            productList.add(new Product(obj.getString("ProductID")
                                    ,obj.getString("ProductName")
                                    ,obj.getString("MenuImage")
                                    ,obj.getInt("ProductPrice")
                                    ,obj.getInt("OutletProductStockQty")
                                    ,obj.getDouble("OutletLevelStock")
                                    ,obj.getInt("OutletProductQtyRekomendation")
                            ));


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    pbRepurchase.setVisibility(View.GONE);
                    adapterRvTop = new ProductRepurchaseAdapter(context, productList, cartList);
                    recyclerViewProductList.setAdapter(adapterRvTop);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(rec);
    }

    public static String ConvertNominal(int input){
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);
        String hsl = df.format(input);
        return hsl;
    }

    private void initBottomSheet() {
        //BOTTOM SHEET STATE
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBadge.setMaxTextLength(4);
        mBadge.setTextSize((float) 30);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
}
