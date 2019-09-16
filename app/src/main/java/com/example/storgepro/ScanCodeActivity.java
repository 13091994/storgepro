package com.example.storgepro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView zXingScannerView;
    int sessionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(this);
        sessionId = getIntent().getIntExtra("move",0);
        setContentView(zXingScannerView);
    }

    @Override
    public void handleResult(Result result) {
        if (sessionId == 1){
            New_Product_frame.textView.setText(result.getText());
            onBackPressed();
        }else if(sessionId == 2){
            Edit_Product_frame.textView.setText(result.getText());
            onBackPressed();
        }else if (sessionId == 3){
            Delete_Product_frame.textView.setText(result.getText());
            onBackPressed();
        }else if (sessionId == 4){
            Search_Product_frame.textView.setText(result.getText());
            onBackPressed();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume(){
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }
}
