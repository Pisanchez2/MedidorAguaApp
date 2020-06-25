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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TomarMediciones extends Fragment {


    private TextView NumSocioText, NombreSocioText, MzText,LtText,NumMedidorText,MedicionText,CedulaText, NoHabText;
    private TextView Tmes1,Tmes2,Tmes3,Tmes4,Tmes5,Tmes6, Tcons1,Tcons2,Tcons3,Tcons4,Tcons5,Tcons6,Tmed1,Tmed2,Tmed3,Tmed4,Tmed5,Tmed6;
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

    private String data[];
    private String datames[];
    private String datacons[];


    public void tomaranios(int anio, int me){
        anios= anio + "";
        mesos = me+"";

    }


    public void mostrarTabla(){

        DatabaseReference reffConsumo = FirebaseDatabase.getInstance().getReference().child("Consumo").child(NumSocioText.getText().toString());
        reffConsumo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                datacons = new String[6];
                 int mesostabla= Integer.parseInt(mesos)+1;
                //int mesostabla=4;
                int aniotabla= Integer.parseInt(anios);

                for(int i=0;i<6;i++){

                    if (mesostabla==1){
                        mesostabla=12;
                        aniotabla=aniotabla-1;
                    }else {
                        mesostabla=mesostabla-1;
                    }

                    if (dataSnapshot.child("Valores/"+aniotabla+"/").hasChild(""+mesostabla+"/Consumido_m3")){
                        datacons[i]= dataSnapshot.child("Valores/"+aniotabla+"/"+mesostabla+"/Consumido_m3").getValue().toString();
                    }else{
                        datacons[i] = "No info";
                    }
                }
                Tcons1.setText(datacons[0]);
                Tcons2.setText(datacons[1]);
                Tcons3.setText(datacons[2]);
                Tcons4.setText(datacons[3]);
                Tcons5.setText(datacons[4]);
                Tcons6.setText(datacons[5]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        reffMedidas = FirebaseDatabase.getInstance().getReference().child("Medidas").child(NumSocioText.getText().toString());
        reffMedidas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data = new String[6];
                datames = new String[6];
                int mesostabla= Integer.parseInt(mesos)+1;
                //int mesostabla=4;
                int aniotabla= Integer.parseInt(anios);

                for(int i=0;i<6;i++){

                    if (mesostabla==1){
                        mesostabla=12;
                        aniotabla=aniotabla-1;
                    }else {
                        mesostabla=mesostabla-1;
                    }

                    String mesaux;
                    switch(mesostabla){
                        case 1:
                            mesaux="Enero";
                            break;
                        case 2:
                            mesaux="Febrero";
                            break;
                        case 3:
                            mesaux="Marzo";
                            break;
                        case 4:
                            mesaux="Abril";
                            break;
                        case 5:
                            mesaux="Mayo";
                            break;
                        case 6:
                            mesaux="Junio";
                            break;
                        case 7:
                            mesaux="Julio";
                            break;
                        case 8:
                            mesaux="Agosto";
                            break;
                        case 9:
                            mesaux="Septiembre";
                            break;
                        case 10:
                            mesaux="Octubre";
                            break;
                        case 11:
                            mesaux="Noviembre";
                            break;
                        case 12:
                            mesaux="Diciembre";
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + mesostabla);
                    }
                    datames[i]=mesaux;

                    if (dataSnapshot.child("Mediciones/"+aniotabla+"/").hasChild(""+mesostabla)){
                        data[i]= dataSnapshot.child("Mediciones/"+aniotabla+"/"+mesostabla+"/medicion").getValue().toString();
                    }else{
                       data[i] = "No info";

                    }
                }

                Tmes1.setText(datames[0]);
                Tmes2.setText(datames[1]);
                Tmes3.setText(datames[2]);
                Tmes4.setText(datames[3]);
                Tmes5.setText(datames[4]);
                Tmes6.setText(datames[5]);

                Tmed1.setText(data[0]);
                Tmed2.setText(data[1]);
                Tmed3.setText(data[2]);
                Tmed4.setText(data[3]);
                Tmed5.setText(data[4]);
                Tmed6.setText(data[5]);

                for(int i=0;i<6;i++) {
                    Log.v("DATA", "."+ i + data[i] +"MES"+datames[i]);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

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

        //Tablas
        Tmes1=v.findViewById(R.id.Tmes1);
        Tmes2=v.findViewById(R.id.Tmes2);
        Tmes3=v.findViewById(R.id.Tmes3);
        Tmes4=v.findViewById(R.id.Tmes4);
        Tmes5=v.findViewById(R.id.Tmes5);
        Tmes6=v.findViewById(R.id.Tmes6);

        Tmed1=v.findViewById(R.id.Tmed1);
        Tmed2=v.findViewById(R.id.Tmed2);
        Tmed3=v.findViewById(R.id.Tmed3);
        Tmed4=v.findViewById(R.id.Tmed4);
        Tmed5=v.findViewById(R.id.Tmed5);
        Tmed6=v.findViewById(R.id.Tmed6);

        Tcons1=v.findViewById(R.id.Tcons1);
        Tcons2=v.findViewById(R.id.Tcons2);
        Tcons3=v.findViewById(R.id.Tcons3);
        Tcons4=v.findViewById(R.id.Tcons4);
        Tcons5=v.findViewById(R.id.Tcons5);
        Tcons6=v.findViewById(R.id.Tcons6);

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

                mostrarTabla();

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
