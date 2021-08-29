package com.example.clingowebver;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FirstPage extends Fragment {
    View view;
    Button runBtn;
    Button updateBtn;
    Button settingBtn;
    EditText inputText;
    String Options;
    Switch stSwitch;
    Switch proSwitch;

    public FirstPage() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_first_page,container,false);
        stSwitch = view.findViewById(R.id.switchstatistics);
        proSwitch = view.findViewById(R.id.switchproject);
        runBtn = view.findViewById(R.id.runBtn);
        inputText = view.findViewById(R.id.inputText);


        //copy file from assets folder for the first time
        if (new File(getContext().getFilesDir().toString() + "/Clingo/clingo.js").exists() && new File(getContext().getFilesDir().toString() + "/Clingo/clingo.html").exists()) {
            //files exist
        } else {
        //Create Clingo folder in data/data/
            if (new File(getContext().getFilesDir().toString() + "/Clingo/").exists()) {

            } else {
                File temp = new File(getContext().getFilesDir().toString() + "/Clingo/");
                temp.mkdir();
            }
            copyAssets();
        }




        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Options = "";
                if (stSwitch.isChecked()) {
                    Options += " --stats";
                } else if (proSwitch.isChecked()) {
                    Options += " --project";
                }



                //call our webpage with inputs
                Intent intent = new Intent(getActivity(), Out.class);
                String intext = inputText.getText().toString();
                intext = intext.replace("\n", "").replace("\r", "");
                intent.putExtra("inputT", intext);
                intent.putExtra("option", Options);
                startActivity(intent);

            }
        });





        return view;
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

    //Copy Files from input in output
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }



}
