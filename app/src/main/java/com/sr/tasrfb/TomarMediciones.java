package com.sr.tasrfb;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sr.tasrfb.ui.DBs.Socios.ClaseMes;
import com.sr.tasrfb.ui.DBs.Socios.CurrentDate;
import com.sr.tasrfb.ui.DBs.Socios.Medidor;
import com.sr.tasrfb.ui.DBs.Socios.Socio;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TomarMediciones extends Fragment {


    private TextView NumSocioText, NombreSocioText, MzText,LtText,NumMedidorText,MedicionText,CedulaText, NoHabText;

    private EditText FechaInsText, FechaMedicionText;

    private DatabaseReference reffBuscar;
    private DatabaseReference reffMedidas;
    private DatabaseReference reff2;


    public TomarMediciones() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String anios;
    private String mesos;

    public void tomaranios(int anio, int me){
        anios= anio + "";
        mesos = me+"";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_tomarmediciones, container, false);
        NumSocioText=v.findViewById(R.id.TNumSocioText);
        NombreSocioText=v.findViewById(R.id.TNombreSocioText);
        MzText=v.findViewById(R.id.TMzText);
        LtText=v.findViewById(R.id.TLtText);
       // NumMedidorText=v.findViewById(R.id.TNumMedText);
        FechaInsText=v.findViewById(R.id.TFechaInsText);
        FechaMedicionText= v.findViewById(R.id.TFechaMedText);
        CedulaText=v.findViewById(R.id.TCedText);
        NoHabText=v.findViewById(R.id.TNhabitext);
        MedicionText= v.findViewById(R.id.TMedText);
        Button enviarMedidasBtn = v.findViewById(R.id.TEnviarMedBtn);
        Button TBuscarSocio = v.findViewById(R.id.TBuscarBtn);

        // TOMAR LA FECHA INSTALACION

        FechaInsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.TFechaInsText:

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

        //SET fecha actual en el campo de fecha de medicion
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat diaf = new SimpleDateFormat("dd-M-yyyy");
        final String dia = diaf.format(c);
        FechaMedicionText.setText(dia);

        SimpleDateFormat mesff = new SimpleDateFormat("M");
        final String mesf= mesff.format(c);

        SimpleDateFormat aniof = new SimpleDateFormat("yyyy");
        final String anio = aniof.format(c);

        anios= anio;
        mesos= mesf;

        FechaMedicionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.TFechaMedText:
                        showDatePickerDialog();
                        break;
                }
            }

            private void showDatePickerDialog() {
                        final DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        tomaranios(year,(month+1));
                        final String selectedDate = day + "-" + (month+1) + "-" + year;
                        FechaMedicionText.setText(selectedDate);
                    }
                });
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        TBuscarSocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Funcion para actualizar los campos en el frame de Tomar Mediciones

                reffMedidas = FirebaseDatabase.getInstance().getReference().child("Medidas").child(NumSocioText.getText().toString());

                reffMedidas.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("Medidor").hasChild("fecha_Instalacion")){
                            String fins = dataSnapshot.child("Medidor").child("fecha_Instalacion").getValue().toString();
                            FechaInsText.setText(fins);
                        }else{
                            reffMedidas.child("Medidor").child("fecha_Instalacion").setValue("01-1-2000");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                reffBuscar=FirebaseDatabase.getInstance().getReference().child("Socio").child(NumSocioText.getText().toString());
                reffBuscar.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String nombre= dataSnapshot.child("nombre").getValue().toString();
                        String lt= dataSnapshot.child("lt").getValue().toString();
                        String mz= dataSnapshot.child("mz").getValue().toString();
                        String num= dataSnapshot.child("num").getValue().toString();
                        String cedula= dataSnapshot.child("cedula").getValue().toString();
                        String n_habitantes= dataSnapshot.child("n_habitantes").getValue().toString();

                        NombreSocioText.setText(nombre);
                        LtText.setText(lt);
                        MzText.setText(mz);
                        CedulaText.setText(cedula);
                        NoHabText.setText(n_habitantes);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        enviarMedidasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClaseMes mes;
                mes = new ClaseMes();
                mes.setMedicion(Integer.parseInt(MedicionText.getText().toString()));
                mes.setFecha_Medicion(FechaMedicionText.getText().toString());
                reff2= FirebaseDatabase.getInstance().getReference().child("Medidas/"+NumSocioText.getText().toString()+"/Mediciones");
                reff2.child(anios).child(mesos).setValue(mes);
                Snackbar.make(view, "Medicion Enviada Correctamente", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        return v;
    }
}
