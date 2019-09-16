package com.example.storgepro;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search_Product_frame extends Fragment {
    public static TextView textView;
    ProgressBar bar;
    Button btn_Search;
    String barcode;
    RequestQueue requestQueue;
    ImageView imageView;
    TextView pro_name,pro_price,pro_date;
    public Search_Product_frame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search__product_frame, container, false);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        bar = v.findViewById(R.id.progressBar4);
        textView = v.findViewById(R.id.Search_barcode);
        pro_name = v.findViewById(R.id.Search_name);
        pro_price = v.findViewById(R.id.Search_price);
        pro_date = v.findViewById(R.id.Search_date);
        imageView = v.findViewById(R.id.Search_image);
        btn_Search = v.findViewById(R.id.Search_btn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getContext(),ScanCodeActivity.class));
                Intent i = new Intent(getContext(),ScanCodeActivity.class);
                i.putExtra("move",4);
                startActivity(i);
            }
        });
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode = textView.getText().toString();
                if (barcode.equals("BarCode Number")){
                    Toast.makeText(getContext(),"please enter barcode",Toast.LENGTH_LONG).show();
                }else if (!isNetworkConnected()){
                    Toast.makeText(getContext(),"Internet Connection Failed",Toast.LENGTH_LONG).show();
                }else{
                    bar.setVisibility(View.VISIBLE);
                    String URL = "https://midship-lender.000webhostapp.com/storge/Search_Product.php?barcode="+barcode;
                    requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("Movies");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    String price = jsonObject.getString("price");
                                    String product_date = jsonObject.getString("pro_date");
                                    String img = jsonObject.getString("img");
                                    bar.setVisibility(View.INVISIBLE);
                                    Picasso.with(getContext().getApplicationContext()).load("https://midship-lender.000webhostapp.com/storge/img/"+img).into(imageView);
                                    pro_name.setText("Product Name : "+name);
                                    pro_price.setText("Price : "+price);
                                    pro_date.setText("date : "+product_date);
                                    textView.setText("BarCode Number");

                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY","ERROR");
                        }
                    }
                    );
                    requestQueue.add(jsonObjectRequest);

                }
            }
        });
        return v;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
