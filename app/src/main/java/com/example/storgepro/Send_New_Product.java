package com.example.storgepro;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Send_New_Product extends StringRequest {

    private static final String Send_Data_Url = "https://midship-lender.000webhostapp.com/storge/Add_Product.php";
    private Map<String,String> Map_Data;

    public Send_New_Product(String name, String barcode, String price,String pro_date,String img ,Response.Listener<String> responselisner) {
        super(Method.POST,Send_Data_Url,responselisner,null);
        Map_Data = new HashMap<>();
        Map_Data.put("name",name);
        Map_Data.put("barcode",barcode);
        Map_Data.put("price",price);
        Map_Data.put("pro_date",pro_date);
        Map_Data.put("img",img);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return Map_Data;
    }
    }

