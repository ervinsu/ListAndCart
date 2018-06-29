package com.example.ervin.listcart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder>{
    Context context;
    List<Product> cartList;
    int userID;

    public CartAdapter(Context context, List<Product> cartList, int userID) {
        this.context = context;
        this.cartList = cartList;
        this.userID = userID;
    }

    @NonNull
    @Override
    public CartAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_row, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ProductViewHolder holder, final int position) {

        final Product product = cartList.get(position);
        holder.tvCartProductName.setText(product.productName);
        holder.tvCartProductPrice.setText(product.productPrice + "");
        holder.edtQty.setText(product.productQty + "");

        holder.edtQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (holder.edtQty.getText().toString().equals("")){
                        holder.edtQty.setText("0");
                    }else {
                        int input = Integer.parseInt(holder.edtQty.getText().toString());
                        ubah(cartList.get(position).productId, input, userID);
                    }
                    return true;
                }
                return false;
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edtQty.setText(++cartList.get(position).productQty+"");
                Log.d("qtynya",product.productQty+"");
                notifyDataSetChanged();
            }
        });

        holder.btnIncrease.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    ubah(cartList.get(position).productId, ++cartList.get(position).productQty, userID);

            }
        });


        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qtynya",product.productQty+"");
                if (product.productQty==0) {
                    Log.d("prdId",cartList.get(position).productId+"");
                    delete(cartList.get(position).productId, cartList.get(position),userID);
                }else {
                    ubah(cartList.get(position).productId, --cartList.get(position).productQty,userID);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cartList.size() == 0) {
            return 0;
        } else {
            return cartList.size();
        }

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnIncrease;
        ImageButton btnDecrease;
        TextView tvCartProductName;
        TextView tvCartProductPrice;
        EditText edtQty;
        public ProductViewHolder(View itemView) {
            super(itemView);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            edtQty = itemView.findViewById(R.id.edtQty);
        }
    }

    public void ubah(String id, int qty, int userID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("Id_Product", id);
            objDetail.put("Qty", qty);
            objDetail.put("UserID",userID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/updateCartOwner.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                cartList.clear();
                                MainActivity.show_cart(MainActivity.urlbawah_new,1);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void delete (final String product_id, final Product removedProduct, final int userID){

        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID", product_id);
            objDetail.put("EntryID", userID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/deleteCartOwner.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {

                                cartList.remove(removedProduct);

                                //Toast.makeText(context, cartList.size()+"", Toast.LENGTH_SHORT).show();
                                MainActivity.show_view(cartList,MainActivity.url,userID);
                                MainActivity.refresh_cart(cartList);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"ERROR FROM SERVER" + error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
