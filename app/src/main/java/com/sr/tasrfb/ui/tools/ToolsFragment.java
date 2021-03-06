package com.sr.tasrfb.ui.tools;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sr.tasrfb.DatePickerFragment;
import com.sr.tasrfb.R;
import com.sr.tasrfb.ui.DBs.Socios.ClaseMes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ToolsFragment extends Fragment {

    public ToolsFragment() {
        // Required empty public constructor
    }

    private TextView  NombreSocioText, MzText,LtText,MedicionText,CedulaText, NoHabText , Mensaje;
    private TextView[] Tcons= new TextView[6];
    private TextView[] Tmes= new TextView[6];
    private TextView[] Tmed= new TextView[6];
    private EditText FechaInsText, FechaMedicionText;
    private DatabaseReference reffBuscar ;
    private DatabaseReference reffMedidas;
    private DatabaseReference reff2;
    private AutoCompleteTextView NumSocioText;
    private String[] currencies;
    private String medanter;
    private Button enviarMedidasBtn;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String anios;
    private String mesos;


    private void tomaranios(int anio, int me){
        anios= anio + "";
        mesos = me+"";

    }

    private void mostrarTabla(){

        DatabaseReference reffConsumo = FirebaseDatabase.getInstance().getReference().child("Consumo").child(NumSocioText.getText().toString());
        reffConsumo.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
                medanter=Tmed[1].getText().toString();
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
        View v = inflater.inflate(R.layout.fragment_tools, container, false);
        NumSocioText = v.findViewById(R.id.QNumSocioText);
        NombreSocioText = v.findViewById(R.id.QNombreSocioText);
        MzText = v.findViewById(R.id.QMzText);
        LtText = v.findViewById(R.id.QLtText);
        // NumMedidorText=v.findViewById(R.id.TNumMedText);
        FechaInsText = v.findViewById(R.id.QFechaInsText);
        FechaMedicionText = v.findViewById(R.id.QFechaMedText);
        CedulaText = v.findViewById(R.id.QCedText);
        NoHabText = v.findViewById(R.id.QNhabitext);
        MedicionText = v.findViewById(R.id.QMedText);
        enviarMedidasBtn = v.findViewById(R.id.QEnviarMedBtn);
        final ImageButton QBuscarSocio = v.findViewById(R.id.QBuscarBtn);
        Mensaje = v.findViewById(R.id.mensaje);
        //Tablas
        Tmes[0] = v.findViewById(R.id.Tmes1);
        Tmes[1] = v.findViewById(R.id.Tmes2);
        Tmes[2] = v.findViewById(R.id.Tmes3);
        Tmes[3] = v.findViewById(R.id.Tmes4);
        Tmes[4] = v.findViewById(R.id.Tmes5);
        Tmes[5] = v.findViewById(R.id.Tmes6);

        Tmed[0] = v.findViewById(R.id.Tmed1);
        Tmed[1] = v.findViewById(R.id.Tmed2);
        Tmed[2] = v.findViewById(R.id.Tmed3);
        Tmed[3] = v.findViewById(R.id.Tmed4);
        Tmed[4] = v.findViewById(R.id.Tmed5);
        Tmed[5] = v.findViewById(R.id.Tmed6);

        Tcons[0] = v.findViewById(R.id.Tcons1);
        Tcons[1] = v.findViewById(R.id.Tcons2);
        Tcons[2] = v.findViewById(R.id.Tcons3);
        Tcons[3] = v.findViewById(R.id.Tcons4);
        Tcons[4] = v.findViewById(R.id.Tcons5);
        Tcons[5] = v.findViewById(R.id.Tcons6);
        final DatabaseReference reffKEY = FirebaseDatabase.getInstance().getReference().child("Socio");
        MedicionText.setEnabled(false);

        reffKEY.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder dataKeys = new StringBuilder();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    dataKeys.append(child.getKey()).append("/");
                }
                currencies = dataKeys.toString().split("/");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                        final String selectedDate = day + "-" + (month + 1) + "-" + year;
                        FechaInsText.setText(selectedDate);
                    }
                });
                newFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "datePicker");
            }

        });


        //VALIDACIONES

        NumSocioText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                boolean valid = NumSocioText.getText().toString().matches("[A-Za-z0-9-_]{1,8}");
                Log.v("VALID", "onClick: " + valid);
                if (valid) {
                    if (s.toString().equals("")) {
                        Mensaje.setText("Ingrese Numero de Socio a Buscar");
                        Mensaje.setTextColor(Color.YELLOW);
                        limpiar();
                    } else {

                        reffKEY.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.hasChild(NumSocioText.getText().toString()) || NumSocioText.getText().toString().equals("")) {
                                    if (!NumSocioText.getText().toString().equals("")) {
                                        limpiar();
                                        Mensaje.setText("Socio NO Existe");
                                        Mensaje.setTextColor(Color.RED);
                                    }
                                } else {
                                    limpiar();
                                    Mensaje.setText("Socio SI Existe");
                                    Mensaje.setTextColor(Color.GREEN);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    }
                    try {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, currencies);
                        NumSocioText.setThreshold(1);
                        NumSocioText.setAdapter(adapter);
                    } catch (Exception e) {
                        Log.e("AUTOCOMPLETE", "SOMETHING GOES WRONG: ");
                    }
                    //
                } else {
                    Mensaje.setText("El numero de socio no puede tener los siguientes caracteres : '.', '#', '$', '/' , '[' , ó ']' ");
                    Mensaje.setTextColor(Color.RED);
                }
            }
        });

        //SET fecha actual en el campo de fecha de medicion
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat diaf = new SimpleDateFormat("dd-M-yyyy");
        final String dia = diaf.format(c);
        FechaMedicionText.setText(dia);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat mesff = new SimpleDateFormat("M");
        final String mesf = mesff.format(c);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat aniof = new SimpleDateFormat("yyyy");

        anios = aniof.format(c);
        mesos = mesf;

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
                        tomaranios(year, (month + 1));
                        final String selectedDate = day + "-" + (month + 1) + "-" + year;
                        FechaMedicionText.setText(selectedDate);
                    }
                });
                newFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "datePicker");
            }
        });

        QBuscarSocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Funcion para actualizar los campos en el frame de Tomar Mediciones
                    escanear();
            }
        });

        enviarMedidasBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int medant;
                int medctu;
                if (!(medanter.equals("No info")) && !MedicionText.getText().toString().equals("")) {
                    medant = Integer.parseInt(medanter);
                    medctu = Integer.parseInt(MedicionText.getText().toString());
                } else {
                    if (medanter.equals("No info")) {
                        medant = 0;
                        medctu = 1;
                    } else {
                        medant = 1;
                        medctu = 0;
                    }
                }
                Log.d("MEDANTERIOR", "onClick: " + medant + "-MED ACTUAL " + medctu);
                if (NumSocioText.getText().toString().equals("") || (medant >= medctu) || MedicionText.getText().toString().equals("") || FechaMedicionText.getText().toString().equals("")) {
                    Mensaje.setText("Ingrese una Medición Válida");
                    Mensaje.setTextColor(Color.YELLOW);
                } else {
                    ClaseMes mes;
                    mes = new ClaseMes();
                    mes.setMedicion(Integer.parseInt(MedicionText.getText().toString()));
                    mes.setFecha_Medicion(FechaMedicionText.getText().toString());
                    reff2 = FirebaseDatabase.getInstance().getReference().child("Medidas/" + NumSocioText.getText().toString() + "/Mediciones");
                    reff2.child(anios).child(mesos).setValue(mes);
                    enviarMedidasBtn.setEnabled(false);
                    Mensaje.setText("Medicion Enviada Correctamente");
                    Mensaje.setTextColor(Color.GREEN);
                }
            }
        });


        return v;
    }

    private void buscar(){
        if (!NumSocioText.getText().toString().equals("")) {

            reffMedidas = FirebaseDatabase.getInstance().getReference().child("Medidas").child(NumSocioText.getText().toString());

            reffMedidas.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Medidor").hasChild("fecha_Instalacion")) {
                        String fins = Objects.requireNonNull(dataSnapshot.child("Medidor").child("fecha_Instalacion").getValue()).toString();
                        FechaInsText.setText(fins);
                        if (fins.equals("01-1-2000")) {
                            FechaInsText.setTextColor(Color.YELLOW);
                        } else {
                            FechaInsText.setTextColor(Color.GRAY);
                        }
                    } else {
                        FechaInsText.setTextColor(Color.YELLOW);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            reffBuscar = FirebaseDatabase.getInstance().getReference().child("Socio");
            reffBuscar.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(NumSocioText.getText().toString())) {

                        String nombre = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("nombre").getValue()).toString();
                        String lt = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("lt").getValue()).toString();
                        String mz = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("mz").getValue()).toString();
                        String cedula = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("cedula").getValue()).toString();
                        String n_habitantes = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("n_habitantes").getValue()).toString();

                        NombreSocioText.setText(nombre);

                        if (lt.equals("null")) {
                            LtText.setTextColor(Color.YELLOW);
                        } else {
                            LtText.setTextColor(Color.GRAY);
                        }

                        if (mz.equals("null")) {
                            MzText.setTextColor(Color.YELLOW);
                        } else {
                            MzText.setTextColor(Color.GRAY);
                        }

                        if (cedula.equals("null")) {
                            CedulaText.setTextColor(Color.YELLOW);
                        } else {
                            CedulaText.setTextColor(Color.GRAY);
                        }

                        if (n_habitantes.equals("null")) {
                            NoHabText.setTextColor(Color.YELLOW);
                        } else {
                            NoHabText.setTextColor(Color.GRAY);
                        }
                        LtText.setText(lt);
                        MzText.setText(mz);
                        CedulaText.setText(cedula);
                        NoHabText.setText(n_habitantes);
                        mostrarTabla();
                        enviarMedidasBtn.setEnabled(true);
                        MedicionText.setEnabled(true);
                    } else {
                        NombreSocioText.setText("");
                        LtText.setText("");
                        MzText.setText("");
                        CedulaText.setText("");
                        NoHabText.setText("");
                        mostrarTabla();
                        enviarMedidasBtn.setEnabled(false);
                        MedicionText.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
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

    @SuppressLint("SetTextI18n")
    private void limpiar(){
        NombreSocioText.setText("");
        LtText.setText("");
        MzText.setText("");
        CedulaText.setText("");
        NoHabText.setText("");
        FechaInsText.setText("01-1-2000");
        for(int i=0;i<6;i++){
            Tcons[i].setText("");
            Tmed[i].setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(getContext(),"Se cancelo el escaneo", Toast.LENGTH_SHORT).show();
            }else {
                NumSocioText.setText(result.getContents());
                buscar();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
