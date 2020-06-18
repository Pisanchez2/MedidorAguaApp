package com.sr.tasrfb;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sr.tasrfb.ui.DBs.Socios.Medidor;
import com.sr.tasrfb.ui.DBs.Socios.Socio;

public class Socios extends Fragment {


    private TextView NumSocioText, NombreSocioText, MzText,LtText,NumMedidorText;

    private EditText FechaInsText;

    private Button EnviarMedidasBtn,BuscarSocioBtn;

    private Socio socio;

    private DatabaseReference reff;
    private DatabaseReference reffBuscar;
    private DatabaseReference reff2;



    public Socios() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_socios, container, false);
        NumSocioText=v.findViewById(R.id.SNumsocioText);
        NombreSocioText=v.findViewById(R.id.SNombreSocioText);
        MzText=v.findViewById(R.id.SMzText);
        LtText=v.findViewById(R.id.SLtText);
        //NumMedidorText=v.findViewById(R.id.SNumMedidorText);
        FechaInsText=v.findViewById(R.id.SFechaInstalacionText);


        EnviarMedidasBtn=v.findViewById(R.id.ActualizarBtn);
        BuscarSocioBtn=v.findViewById(R.id.BuscarSocioBtn);

        socio = new Socio();
        reff= FirebaseDatabase.getInstance().getReference().child("Socio");


        // TOMAR LA FECHA INSTALACION

        FechaInsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.SFechaInstalacionText:

                    showDatePickerDialog();
                    break;
                }
            }


            private void showDatePickerDialog() {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = day + "-" + (month+1) + "-" + year;
                        FechaInsText.setText(selectedDate);
                    }
                });

                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }


        });



        BuscarSocioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reffBuscar=FirebaseDatabase.getInstance().getReference().child("Socio").child(NumSocioText.getText().toString());
                reffBuscar.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String nombre= dataSnapshot.child("nombre").getValue().toString();
                        String lt= dataSnapshot.child("lt").getValue().toString();
                        String mz= dataSnapshot.child("mz").getValue().toString();
                        String num= dataSnapshot.child("num").getValue().toString();
                       // String fins= dataSnapshot.child("medidor").child("fecha_Instalacion").getValue().toString();
                        //String numed= dataSnapshot.child("medidor").child("num").getValue().toString();

                        NombreSocioText.setText(nombre);
                        LtText.setText(lt);
                        MzText.setText(mz);
                        //NumMedidorText.setText(numed);
                        //FechaInsText.setText(fins);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        EnviarMedidasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reff.child(NumSocioText.getText().toString()+"/lt").setValue(LtText.getText().toString());
                reff.child(NumSocioText.getText().toString()+"/mz").setValue(MzText.getText().toString());
                reff.child(NumSocioText.getText().toString()+"/nombre").setValue(NombreSocioText.getText().toString());
                reff.child(NumSocioText.getText().toString()+"/num").setValue(NumSocioText.getText().toString());



                reff2= FirebaseDatabase.getInstance().getReference().child("Medidas/"+NumSocioText.getText().toString()+"/Medidor/fecha_Instalacion");
                reff2.setValue(FechaInsText.getText().toString());
                Log.v("SOCIO","HASIDOENVIADO");


            }
        });


        return v;


    }
}
