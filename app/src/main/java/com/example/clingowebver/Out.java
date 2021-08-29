package com.example.clingowebver;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class Out extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);







        String message = getIntent().getStringExtra("inputT");
       String options = getIntent().getStringExtra("option");
        final WebView browser = findViewById(R.id.webViewID);

        Log.d("url :-------------------------> ",getFilesDir()+ "/Clingo/clingo.html");


        //call webview with JS function in HTML file
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setDomStorageEnabled(true);

        browser.getSettings().setAllowFileAccess(true);
        browser.getSettings().setAllowFileAccessFromFileURLs(true);
        browser.getSettings().setAllowUniversalAccessFromFileURLs(true);

        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl("file://"+getFilesDir()+"/Clingo/clingo.html");

      final String inText = String.format("\"%s\"", message);
       final String opText = String.format("\"%s\"", options);


      browser.setWebViewClient(new WebViewClient() {
         public void onPageFinished(WebView view, String weburl) {

           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                  browser.evaluateJavascript("javascript:solve(" + inText + "," + opText + ");", null);
             } else {
                   browser.loadUrl("javascript:solve(" + inText + "," + opText + ")");
             }
           }
       });


    }




}
