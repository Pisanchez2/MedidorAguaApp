package com.sr.tasrfb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androidadvance.topsnackbar.TSnackbar;
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
import java.util.Objects;

public class TomarMediciones extends Fragment {


    private TextView NumSocioText, NombreSocioText, MzText,LtText,NumMedidorText,MedicionText,CedulaText, NoHabText;
    private TextView[] Tcons= new TextView[6];
    private TextView[] Tmes= new TextView[6];
    private TextView[] Tmed= new TextView[6];
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

    private void mostrarTabla(final View view){

        DatabaseReference reffConsumo = FirebaseDatabase.getInstance().getReference().child("Consumo").child(NumSocioText.getText().toString());
        reffConsumo.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                datacons = new String[6];
                 int mesostabla= Integer.parseInt(mesos)+1;
                int aniotabla= Integer.parseInt(anios);

                for(int i=0;i<6;i++){

                    if (mesostabla==1){
                        mesostabla=12;
                        aniotabla=aniotabla-1;
                    }else {
                        mesostabla=mesostabla-1;
                    }

                    if (dataSnapshot.child("Valores/"+aniotabla+"/").hasChild(""+mesostabla+"/Consumido_m3")){
                        Tcons[i].setText(Objects.requireNonNull(dataSnapshot.child("Valores/" + aniotabla + "/" + mesostabla + "/Consumido_m3").getValue()).toString());
                        Tcons[i].setTextColor(Color.WHITE);
                    }else{
                        Tcons[i].setText("No info");
                        Tcons[i].setTextColor(Color.YELLOW);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        reffMedidas = FirebaseDatabase.getInstance().getReference().child("Medidas").child(NumSocioText.getText().toString());
        reffMedidas.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data = new String[6];
                datames = new String[6];
                int mesostabla= Integer.parseInt(mesos)+1;
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
                    Tmes[i].setText(mesaux);

                    if (dataSnapshot.child("Mediciones/"+aniotabla+"/").hasChild(""+mesostabla)){
                        Tmed[i].setText(Objects.requireNonNull(dataSnapshot.child("Mediciones/" + aniotabla + "/" + mesostabla + "/medicion").getValue()).toString());
                        Tmed[i].setTextColor(Color.WHITE);
                    }else{
                       Tmed[i].setText("No info");
                       Tmed[i].setTextColor(Color.YELLOW);
                    }
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
        final Button enviarMedidasBtn = v.findViewById(R.id.TEnviarMedBtn);
        ImageButton TBuscarSocio = v.findViewById(R.id.TBuscarBtn);

        //Tablas
        Tmes[0]=v.findViewById(R.id.Tmes1);
        Tmes[1]=v.findViewById(R.id.Tmes2);
        Tmes[2]=v.findViewById(R.id.Tmes3);
        Tmes[3]=v.findViewById(R.id.Tmes4);
        Tmes[4]=v.findViewById(R.id.Tmes5);
        Tmes[5]=v.findViewById(R.id.Tmes6);

        Tmed[0]=v.findViewById(R.id.Tmed1);
        Tmed[1]=v.findViewById(R.id.Tmed2);
        Tmed[2]=v.findViewById(R.id.Tmed3);
        Tmed[3]=v.findViewById(R.id.Tmed4);
        Tmed[4]=v.findViewById(R.id.Tmed5);
        Tmed[5]=v.findViewById(R.id.Tmed6);

        Tcons[0]=v.findViewById(R.id.Tcons1);
        Tcons[1]=v.findViewById(R.id.Tcons2);
        Tcons[2]=v.findViewById(R.id.Tcons3);
        Tcons[3]=v.findViewById(R.id.Tcons4);
        Tcons[4]=v.findViewById(R.id.Tcons5);
        Tcons[5]=v.findViewById(R.id.Tcons6);

        enviarMedidasBtn.setEnabled(false);

        // TOMAR LA FECHA INSTALACION

        FechaInsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.TFechaInsText) {
                    showDatePickerDialog();
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat diaf = new SimpleDateFormat("dd-M-yyyy");
        final String dia = diaf.format(c);
        FechaMedicionText.setText(dia);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat mesff = new SimpleDateFormat("M");
        final String mesf= mesff.format(c);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat aniof = new SimpleDateFormat("yyyy");

        anios= aniof.format(c);
        mesos= mesf;

        FechaMedicionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.TFechaMedText) {
                    showDatePickerDialog();
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
            public void onClick(final View view) {
                // Funcion para actualizar los campos en el frame de Tomar Mediciones


                if(!NumSocioText.getText().toString().equals("")){

                    reffMedidas = FirebaseDatabase.getInstance().getReference().child("Medidas").child(NumSocioText.getText().toString());

                    reffMedidas.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("Medidor").hasChild("fecha_Instalacion")){
                                String fins = dataSnapshot.child("Medidor").child("fecha_Instalacion").getValue().toString();
                                FechaInsText.setText(fins);
                                if(fins.equals("01-1-2000")){
                                    FechaInsText.setTextColor(Color.YELLOW);
                                }else{
                                    FechaInsText.setTextColor(Color.GRAY);
                                }
                            }else{
                                FechaInsText.setTextColor(Color.YELLOW);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    reffBuscar=FirebaseDatabase.getInstance().getReference().child("Socio");
                    reffBuscar.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String Mensaje= "";
                            int color;
                            if (dataSnapshot.hasChild(NumSocioText.getText().toString())){

                                String nombre= dataSnapshot.child(NumSocioText.getText().toString()).child("nombre").getValue().toString();
                                String lt= dataSnapshot.child(NumSocioText.getText().toString()).child("lt").getValue().toString();
                                String mz= dataSnapshot.child(NumSocioText.getText().toString()).child("mz").getValue().toString();
                                String num= dataSnapshot.child(NumSocioText.getText().toString()).child("num").getValue().toString();
                                String cedula= dataSnapshot.child(NumSocioText.getText().toString()).child("cedula").getValue().toString();
                                String n_habitantes= dataSnapshot.child(NumSocioText.getText().toString()).child("n_habitantes").getValue().toString();

                                NombreSocioText.setText(nombre);

                                if(lt.equals("null")){
                                    LtText.setTextColor(Color.YELLOW);
                                }else{
                                    LtText.setTextColor(Color.GRAY);
                                }

                                if(mz.equals("null")){
                                    MzText.setTextColor(Color.YELLOW);
                                }else{
                                    MzText.setTextColor(Color.GRAY);
                                }

                                if(cedula.equals("null")){
                                    CedulaText.setTextColor(Color.YELLOW);
                                }else{
                                    CedulaText.setTextColor(Color.GRAY);
                                }

                                if(n_habitantes.equals("null")){
                                    NoHabText.setTextColor(Color.YELLOW);
                                }else{
                                    NoHabText.setTextColor(Color.GRAY);
                                }
                                LtText.setText(lt);
                                MzText.setText(mz);
                                CedulaText.setText(cedula);
                                NoHabText.setText(n_habitantes);
                                mostrarTabla(view);
                                Mensaje="Socio Encontrado";
                                color = R.color.sucess;
                                enviarMedidasBtn.setEnabled(true);

                                Log.d("ESTADO", "true ");
                            }else{
                                NombreSocioText.setText("");
                                LtText.setText("");
                                MzText.setText("");
                                CedulaText.setText("");
                                NoHabText.setText("");
                                mostrarTabla(view);
                                Mensaje="Socio NO Encontrado";
                                color= R.color.error;
                                enviarMedidasBtn.setEnabled(false);
                                Log.d("ESTADO", "false ");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    //Ingresar mensaje de notificacion
                }

            }
        });

        enviarMedidasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NumSocioText.getText().toString().equals("") || MedicionText.getText().toString().equals("")  || FechaMedicionText.getText().toString().equals("") ){
                  //  Snackbar.make(view, "Sin Casillas Vacias", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //Ingresar mensaje para "Sin Casillas Vacias"
                }else {
                    ClaseMes mes;
                    mes = new ClaseMes();
                    mes.setMedicion(Integer.parseInt(MedicionText.getText().toString()));
                    mes.setFecha_Medicion(FechaMedicionText.getText().toString());
                    reff2 = FirebaseDatabase.getInstance().getReference().child("Medidas/" + NumSocioText.getText().toString() + "/Mediciones");
                    reff2.child(anios).child(mesos).setValue(mes);
                    enviarMedidasBtn.setEnabled(false);
                }
            }
        });
        return v;
    }
}
