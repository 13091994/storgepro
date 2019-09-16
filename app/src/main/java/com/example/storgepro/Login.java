package com.example.storgepro;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends Fragment {
    private EditText nameEditText;
    private EditText passEditText;
    TextView login_txt;
    DB_Sqlite DB;
    ProgressBar bar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_login, container, false);
         DB = new DB_Sqlite(getActivity());
         login_txt = (TextView)v.findViewById(R.id.Login_txt);
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setFragment(new Regestration());
            }
        });
        nameEditText = (EditText) v.findViewById(R.id.Login_name);
        passEditText = (EditText) v.findViewById(R.id.login_password);
        bar = v.findViewById(R.id.progressBarLogin);
        v.findViewById(R.id.Login_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String pass = passEditText.getText().toString().replace(' ', '_');;
                final String name = nameEditText.getText().toString().replace(' ', '_');;
                if (!isValidPassword(name)) {
                    nameEditText.setError("Invalid username");
                }else if (!isValidPassword(pass)) {
                    passEditText.setError("Invalid Password");
                }else if (!isNetworkConnected()){
                    Toast.makeText(getContext(),"Internet Connection Failed",Toast.LENGTH_LONG).show();
                }else {
                    bar.setVisibility(View.VISIBLE);
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    String url_add_like = "https://midship-lender.000webhostapp.com/storge/Login.php?name="+name+"&password="+pass;
                    final StringRequest request = new StringRequest(Request.Method.GET, url_add_like, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success){
                                    DB.updateData_Login(name,pass,"1");
                                    bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "successful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getContext(),Products.class));
                                    getActivity().finish();
                                }else
                                {
                                    bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Invalid username and password", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "not not successful", Toast.LENGTH_LONG).show();
                        }
                    }

                    );
                    queue.add(request);
                }

            }
        });
        return v;
    }
    public void setFragment(Fragment f){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame,f);
        ft.addToBackStack(null);
        ft.commit();
    }


    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
