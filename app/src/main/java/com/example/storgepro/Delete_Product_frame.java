package com.example.storgepro;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Delete_Product_frame extends Fragment {
    public static TextView textView;
    ProgressBar bar;
    Button btn_delete;
    String barcode;
    public Delete_Product_frame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_delete__product_frame, container, false);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(4).setChecked(true);
        bar = v.findViewById(R.id.progressBar3);
        textView = v.findViewById(R.id.Delete_barcode);
        btn_delete = v.findViewById(R.id.Delete_btn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getContext(),ScanCodeActivity.class));
                Intent i = new Intent(getContext(),ScanCodeActivity.class);
                i.putExtra("move",3);
                startActivity(i);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode = textView.getText().toString();
                if (barcode.equals("BarCode Number")){
                    Toast.makeText(getContext(),"please enter barcode",Toast.LENGTH_LONG).show();
                }else if (!isNetworkConnected()){
                    Toast.makeText(getContext(),"Internet Connection Failed",Toast.LENGTH_LONG).show();
                }else{
                    bar.setVisibility(View.VISIBLE);
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    String url_add_like = "https://midship-lender.000webhostapp.com/storge/Delete_Product.php?barcode="+barcode;
                    final StringRequest request = new StringRequest(Request.Method.GET, url_add_like, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(getContext(), barcode, Toast.LENGTH_LONG).show();
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success){
                                    bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Delete successfuly", Toast.LENGTH_LONG).show();
                                    textView.setText("BarCode Number");
                                }else
                                {
                                    bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Delete not successfuly", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "not not successfuly", Toast.LENGTH_LONG).show();
                        }
                    }

                    );
                    queue.add(request);
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
