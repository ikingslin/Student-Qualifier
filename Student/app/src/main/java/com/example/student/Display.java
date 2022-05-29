package com.example.student;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;


public class Display extends Fragment {

    public int id;
    public Display(int id) {
        this.id=id;
    }
    WebView syl;
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference pdf;

    //private PDFDoc mPdfDoc;
    Uri pdflink;
    String sempath,subpath,urlpath,convey;
    private LinearLayout pdfLayout;
    PDFView pdfView;
    View root;

    public Display(String sem, String sub, String con){
        sempath=sem;
        subpath=sub;
        convey = con;
    }
    public Display(String sem, String sub){
        sempath=sem;
        subpath=sub;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseAuth.getInstance();
        root = inflater.inflate(R.layout.fragment_listing_display, container, false);
        pdfView = root.findViewById(R.id.pdfDisp);
        if(sempath.equals("Faculty")) {
            pdf = storage.getReference().child("Faculty/").child(subpath + ".pdf");
        }
        else if(convey.equals("Syllabus")){
            pdf = storage.getReference().child("Syllabus/").child(sempath + "/").child(subpath + ".pdf");
        }
        else if(convey.equals("Question")){
            pdf = storage.getReference().child("Question Bank/").child(sempath + "/").child(subpath + ".pdf");
        }
        pdf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url =""+uri;
                try {
                    //"https://firebasestorage.googleapis.com"
                    //https://docs.google.com/viewerng?embedded=true&url=
                    //syl.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+ URLEncoder.encode(url,"ISO-8859-1"));
                    Toast.makeText(root.getContext(), "Please Wait", Toast.LENGTH_LONG).show();
                    new RetrivePDFfromUrl().execute(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return root;
        }
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }
}
