package com.sr.tasrfb.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sr.tasrfb.R;

public class ToolsFragment extends Fragment {

    private TextView VerCodText;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        Button LeerCodBtn= root.findViewById(R.id.LeerCodBtn);
        VerCodText= root.findViewById(R.id.VerTexto);

        LeerCodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanear();
            }
        });

        return root;

    }

    private void escanear() {
        IntentIntegrator intent=IntentIntegrator.forSupportFragment(ToolsFragment.this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setPrompt("ESCANEAR CODIGO");
        intent.setCameraId(0);
        intent.setBeepEnabled(false);
        intent.setOrientationLocked(false);
        intent.setBarcodeImageEnabled(false);
        intent.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(getContext(),"Se cancelo el escaneo", Toast.LENGTH_SHORT).show();
            }else {
                VerCodText.setText(result.getContents().toString());
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
