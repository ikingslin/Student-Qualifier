package com.example.student.Frag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.student.R;

public class Fee extends Fragment {

    public Fee() {
        // Required empty public constructor
    }
    WebView fee;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fee, container, false);
        fee = root.findViewById(R.id.feepay);
        fee.loadUrl("https://www.drngpasc.ac.in/student-fees-payment.php");

        WebSettings webSettings = fee.getSettings();
        webSettings.setJavaScriptEnabled(true);


        fee.setWebViewClient(new WebViewClient());
        return root;
    }
}