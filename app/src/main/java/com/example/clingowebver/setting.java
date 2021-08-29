package com.example.clingowebver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class setting extends Fragment {

    Button format;
    Button dlFolder;
    Button uRLBtn;
    TextView internetCheck;
    EditText urlInput;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_setting, container, false);


        format = view.findViewById(R.id.factoryBtn);
        dlFolder = view.findViewById(R.id.dlFolderBtn);
        uRLBtn = view.findViewById(R.id.downloadFromUrl);
        internetCheck = view.findViewById(R.id.internetText);
        urlInput = view.findViewById(R.id.inputUrl);


        //Reset Factory BTN
        format.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (new File(getContext().getFilesDir().toString() + "/Clingo/").exists()) {
                    File x = new File(getContext().getFilesDir().toString() + "/Clingo/");
                    x.delete();
                    internetCheck.setText("Clingo folder has been Deleted");
                    File temp = new File(getContext().getFilesDir().toString() + "/Clingo/");
                    temp.mkdir();

                    internetCheck.setText("Clingo folder has been Created");
                } else {
                    File temp = new File(getContext().getFilesDir().toString() + "/Clingo/");
                    temp.mkdir();

                    internetCheck.setText("Clingo folder has been Created");
                }
                copyAssets();
                internetCheck.setText("Clingo file has been Created");

            }
        });
        //Copy js From dl folder
        dlFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int REQUEST_EXTERNAL_STORAGE = 1;
                String[] PERMISSIONS_STORAGE = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                // Check if we have write permission
                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }


                if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/clingo.js").exists()) {

                    File src = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/clingo.js");
                    File dst = new File(getContext().getFilesDir().toString() + "/Clingo/clingo.js");
                    try {
                        copyDownload(src, dst);
                        internetCheck.setText("Clingo has been copied from download folder");
                    } catch (IOException e) {

                    }

                } else {
                    internetCheck.setText("no file in download folder");
                }


            }
        });



        //Call AsyncTask to download from URK
        uRLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = urlInput.getText().toString();
                if (isNetworkAvailable()) {

                    new setting.DownloadFileFromURL().execute(temp, (String) getContext().getFilesDir().toString() + "/Clingo/clingo.js");
                } else {

                }


            }
        });

        return view;
    }

    //check Internet Connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //copy Files From Assets Folder
    private boolean copyAssets() {

        boolean temp = false;
        AssetManager assetManager = getContext().getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);

                String outDir = getContext().getFilesDir().toString() + "/Clingo/";

                File outFile = new File(outDir, filename);

                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                temp = true;
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
                temp = false;
            }
        }
        return temp;
    }

    //Copy Files from Download Folder
    public static void copyDownload(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException e) {
                Log.e("tag", "Failed to copy Download file: ", e);

            }

        } catch (IOException e) {
            Log.e("tag", "Failed to copy Download file: ", e);

        }

    }

    //Copy Files from input in output
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    //works with AsyncTask Class
    void processValue(String myValue) {
        if (myValue.contains("html")) {
            internetCheck.setText("RESULT'S PAGE-FORMAT HAS BEEN DOWNLOADED ...");
        } else if (myValue.contains("js")) {
            internetCheck.setText("CLINGO HAS BEEN DOWNLOADED -- ALL DONE --");
        } else
            internetCheck.setText("Downloading..");
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            internetCheck.setText("Downloading..");
        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                url = new URL(f_url[0]);
                System.out.println("url Created");
            } catch (
                    MalformedURLException e) {
            }
            File file = new File(f_url[1]);
            System.out.println("file Created");
            try {
                InputStream input = url.openStream();

                System.out.println("url opened");
                if (file.exists()) {
                    if (file.isDirectory())
                        throw new IOException("File '" + file + "' is a directory");
                    if (!file.canWrite())
                        throw new IOException("File '" + file + "' cannot be written");
                } else {
                    File parent = file.getParentFile();
                    if ((parent != null) && (!parent.exists()) && (!parent.mkdirs())) {
                        throw new IOException("File '" + file + "' could not be created");
                    }
                }
                FileOutputStream output = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                input.close();
                output.close();
                System.out.println("File '" + file.getName() + "' downloaded successfully!");
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
            return file.getName();
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            processValue(result);
        }
    }


}


