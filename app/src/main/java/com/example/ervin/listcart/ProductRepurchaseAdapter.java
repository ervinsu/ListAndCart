package com.example.ervin.listcart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductRepurchaseAdapter extends RecyclerView.Adapter<ProductRepurchaseAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private List<Product> cartList;

    public ProductRepurchaseAdapter(Context context, List<Product> productList, List<Product>cartList) {
        this.context = context;
        this.productList = productList;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ProductRepurchaseAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_row_repurchase, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductRepurchaseAdapter.ProductViewHolder holder, final int position) {
        final Product product = productList.get(position);
        holder.tvProductID.setText(product.productId);
        holder.tvProductName.setText(product.productName);
        holder.tvProductPrice.setText(MainActivity.ConvertNominal(product.productPrice));
        holder.tvProdcutQtyOutlet.setText(product.productQtyOutlet+"");
        holder.tvProductQtyLvl.setText(product.getProductQtyLvl+"");
        holder.tvProductQtyRekomendasi.setText(product.productQtyRekomendasi+"");
        Picasso.get().load(product.productMenuImage).into(holder.ivProductMenuImage);

        for(int j=0;j<cartList.size();j++){
            if(product.productId.equals(cartList.get(j).productId)){
                holder.btnAdd.setEnabled(false);
                break;
            }
        }
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add(product.productId,product.productName, product.productPrice,1, 1);
                holder.btnAdd.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        Button btnAdd;
        TextView tvProductID,tvProductName,tvProductPrice,tvProdcutQtyOutlet,tvProductQtyLvl,tvProductQtyRekomendasi;
        ImageView ivProductMenuImage;
        public ProductViewHolder(View itemView) {
            super(itemView);
            tvProdcutQtyOutlet = itemView.findViewById(R.id.tvProductQtyOutlet);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            tvProductID = itemView.findViewById(R.id.tvProductID);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQtyLvl = itemView.findViewById(R.id.tvProductQtyLvl);
            tvProductQtyRekomendasi = itemView.findViewById(R.id.tvProductQtyRekomendasi);
            ivProductMenuImage = itemView.findViewById(R.id.ivProductMenuImage);
        }
    }

    public void add(String product_id, String product_name, int product_price, int qty, int userID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductName", product_name);
            objDetail.put("Price", product_price);
            objDetail.put("Qty", qty);
            objDetail.put("EntryID", userID);
            objDetail.put("UpdatedBy",userID);
            objDetail.put("ProductID", product_id);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/addCartOwner.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                MainActivity.show_cart( MainActivity.urlbawah_new,1);
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
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
