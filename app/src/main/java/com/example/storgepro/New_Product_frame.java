package com.example.storgepro;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Result;


/**
 * A simple {@link Fragment} subclass.
 */
public class New_Product_frame extends Fragment {

    Button Add_btn;
    EditText pro_name,pro_price,pro_date;
    ImageView Add_image;
    public static TextView textView;
    String image_encode,barcode,name,price,date;
    ProgressBar bar;
    private Bitmap oldDrawable;
    public New_Product_frame() {
        // Required empty public constructor
    }



    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new__product_frame, container, false);
        final NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        Add_image = (ImageView)v.findViewById(R.id.Add_image);
        oldDrawable = ((BitmapDrawable) Add_image.getDrawable()).getBitmap();
        pro_name = (EditText) v.findViewById(R.id.Add_name);
        pro_price = (EditText) v.findViewById(R.id.Add_price);
        pro_date = (EditText) v.findViewById(R.id.Add_date);
        bar = v.findViewById(R.id.progressBar);
        Add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);
            }
        });
        //btn = (Button) v.findViewById(R.id.btn_add);
        textView = (TextView) v.findViewById(R.id.Add_barcode);
        Add_btn = (Button) v.findViewById(R.id.Add_btn);
        Add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable) Add_image.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
                image_encode = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

                barcode = textView.getText().toString();
                name = pro_name.getText().toString();
                price = pro_price.getText().toString();
                date = pro_date.getText().toString();

                if (oldDrawable == bitmap){
                    Toast.makeText(getContext(),"please chose image for product",Toast.LENGTH_LONG).show();
                }else if (barcode.equals("BarCode Number")){
                    Toast.makeText(getContext(),"please enter barcode",Toast.LENGTH_LONG).show();
                }else if (name.isEmpty()){
                    Toast.makeText(getContext(),"please enter product name",Toast.LENGTH_LONG).show();
                }else if (price.isEmpty()){
                    Toast.makeText(getContext(),"please enter product price",Toast.LENGTH_LONG).show();
                }else if (date.isEmpty()){
                    Toast.makeText(getContext(),"please enter product date",Toast.LENGTH_LONG).show();
                }else if (!isNetworkConnected()){
                    Toast.makeText(getContext(),"Internet Connection Failed",Toast.LENGTH_LONG).show();
                }else {
                    bar.setVisibility(View.VISIBLE);
                    Response.Listener<String> responsLisner = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success)
                                {
                                    bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(),"send new product successfuly",Toast.LENGTH_SHORT).show();
                                    textView.setText("BarCode Number");
                                    pro_name.setText("");
                                    pro_price.setText("");
                                    pro_date.setText("");
                                    Add_image.setImageResource(R.drawable.product_image);
                                }else
                                {
                                    bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(),"send new product successfuly",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    Send_New_Product send_new_product = new Send_New_Product(name,barcode,price,date,image_encode,responsLisner);
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    requestQueue.add(send_new_product);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //startActivity(new Intent(getContext(),ScanCodeActivity.class));
               Intent i = new Intent(getContext(),ScanCodeActivity.class);
               i.putExtra("move",1);
               startActivity(i);
            }
        });
        return v;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK ){
            Uri uri = data.getData();
            Add_image.setImageURI(uri);
        }
    }
}
