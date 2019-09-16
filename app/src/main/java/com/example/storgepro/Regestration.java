package com.example.storgepro;


import android.app.DownloadManager;
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


public class Regestration extends Fragment {
    TextView Regest_txt;
    Button Regist_btn;
    ProgressBar bar;
    private EditText emailEditText;
    private EditText passEditText;
    private EditText userEditText;
    private EditText phoneEditText;
    DB_Sqlite DB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_regestration, container, false);
        DB =  new DB_Sqlite(getActivity());
        emailEditText = (EditText) v.findViewById(R.id.Regist_email);
        passEditText = (EditText) v.findViewById(R.id.Regist_password);
        phoneEditText = (EditText) v.findViewById(R.id.Regist_phone);
        userEditText = (EditText) v.findViewById(R.id.Regist_username);
        bar = v.findViewById(R.id.progressBarRegist);
        Regist_btn = v.findViewById(R.id.Regist_btn);
        Regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString().replace(' ', '_');;
                final String pass = passEditText.getText().toString().replace(' ', '_');;
                final String user = userEditText.getText().toString().replace(' ', '_');;
                final String phone = phoneEditText.getText().toString().replace(' ', '_');;
                if (!isValidEmail(email)) {
                    emailEditText.setError("Invalid Email");
                }else if (!isValidPassword(pass)) {
                    passEditText.setError("Invalid Password");
                }else if (!isValidPassword(user)) {
                    userEditText.setError("Invalid username");
                }else if (!isValidPhone(phone)) {
                    phoneEditText.setError("Invalid phone Number");
                }else if (!isNetworkConnected()){
                    Toast.makeText(getContext(),"Internet Connection Failed",Toast.LENGTH_LONG).show();
                }else {
                    bar.setVisibility(View.VISIBLE);
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    String url_add_like = "https://midship-lender.000webhostapp.com/storge/Registration.php?username="+user+"&email="+email+"&password="+pass+"&phone="+phone;
                    final StringRequest request = new StringRequest(Request.Method.GET, url_add_like, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success){
                                    DB.updateData_R(user,pass,"1",email,phone);
                                    Toast.makeText(getContext(), "successful", Toast.LENGTH_LONG).show();
                                    bar.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(getContext(),Products.class));
                                    getActivity().finish();
                                }else
                                {
                                    bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "not successful", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "not not successfuly\n"+error, Toast.LENGTH_LONG).show();
                        }
                    }

                    );
                    queue.add(request);
                }
            }
        });
        return v;
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }
    private boolean isValidPhone(String phone) {
        if (phone != null && phone.length() > 10) {
            return true;
        }
        return false;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
