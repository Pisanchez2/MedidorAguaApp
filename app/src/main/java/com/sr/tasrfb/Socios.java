package com.sr.tasrfb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;


public class Socios extends Fragment {


    private TextView  NombreSocioText, MzText,LtText,CedText, NoHabit, Mensaje;
    private EditText FechaInsText;
    private DataSnapshot data;
    private String finsa;
    private String[] currencies;
    private AutoCompleteTextView NumSocioText;

    public Socios() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_socios, container, false);

        NombreSocioText = v.findViewById(R.id.SNombreSocioText);
        MzText = v.findViewById(R.id.SMzText);
        LtText = v.findViewById(R.id.SLtText);
        CedText = v.findViewById(R.id.SCedText);
        NoHabit = v.findViewById(R.id.SNhabitext);
        FechaInsText = v.findViewById(R.id.SFechaInstalacionText);
        final Button EnviarMedidasBtn = v.findViewById(R.id.ActualizarBtn);
        final ImageButton BuscarSocioBtn2 = v.findViewById(R.id.BuscarSocioBtn);
        final ImageButton AddSocioBtn = v.findViewById(R.id.AddSocio);
        final ImageButton BorrarSocio = v.findViewById(R.id.borrarBtn);
        BorrarSocio.setEnabled(false);
        EnviarMedidasBtn.setEnabled(false);
        BuscarSocioBtn2.setEnabled(false);
        AddSocioBtn.setEnabled(false);
        NombreSocioText.setEnabled(false);
        MzText.setEnabled(false);
        LtText.setEnabled(false);
        CedText.setEnabled(false);
        NoHabit.setEnabled(false);
        FechaInsText.setEnabled(false);
        Mensaje = v.findViewById(R.id.Mensaje);
        NumSocioText= v.findViewById(R.id.SNumsocioText);

        final DatabaseReference reffKEY = FirebaseDatabase.getInstance().getReference().child("Socio");
        final DatabaseReference reffFecha = FirebaseDatabase.getInstance().getReference().child("Medidas");

        reffKEY.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder dataKeys= new StringBuilder();

                for (DataSnapshot child: dataSnapshot.getChildren()){
                    dataKeys.append(child.getKey()).append("/");
                }
                currencies = dataKeys.toString().split("/");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // TOMAR LA FECHA INSTALACION

        FechaInsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.SFechaInstalacionText) {
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


        NumSocioText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
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
                        BuscarSocioBtn2.setEnabled(false);
                        AddSocioBtn.setEnabled(false);
                        desatext();
                        limpiar();
                    } else {

                        reffKEY.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                data = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()));
                                if (!dataSnapshot.hasChild(NumSocioText.getText().toString()) || NumSocioText.getText().toString().equals("")) {
                                    BuscarSocioBtn2.setEnabled(false);
                                    if (!NumSocioText.getText().toString().equals("")) {
                                        AddSocioBtn.setEnabled(true);
                                        desatext();
                                        limpiar();
                                        Mensaje.setText("Socio NO Existe");
                                        Mensaje.setTextColor(Color.RED);
                                    }
                                } else {
                                    BuscarSocioBtn2.setEnabled(true);
                                    AddSocioBtn.setEnabled(false);
                                    limpiar();
                                    desatext();
                                    Mensaje.setText("Socio SI Existe");
                                    Mensaje.setTextColor(Color.GREEN);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });


                        reffFecha.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Medidor").hasChild("fecha_Instalacion")){
                                    String fins = Objects.requireNonNull(dataSnapshot.child("Medidor").child("fecha_Instalacion").getValue()).toString();
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
                    }
                    try {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, currencies);
                        NumSocioText.setThreshold(1);
                        NumSocioText.setAdapter(adapter);
                    } catch (Exception e) {
                        Log.e("AUTOCOMPLETE", "SOMETHING GOES WRONG: ");
                    }
                    //
                }else{
                    Mensaje.setText("El numero de socio no puede tener los siguientes caracteres : '.', '#', '$', '/' , '[' , ó ']' ");
                    Mensaje.setTextColor(Color.RED);
                    AddSocioBtn.setEnabled(false);
                    BuscarSocioBtn2.setEnabled(false);
                    BorrarSocio.setEnabled(false);
                }
                //
                BorrarSocio.setEnabled(false);
            }
        });

        LtText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
              //  try{
                    if(data.hasChild("lt")) {
                        if (!s.toString().equals(Objects.requireNonNull(data.child("lt").getValue()).toString()) && !s.toString().equals("")) {
                            EnviarMedidasBtn.setEnabled(true);
                        } else {
                            EnviarMedidasBtn.setEnabled(false);
                        }
                    }
                /*}catch (Exception e){
                    Log.v("DB", "EMPTY ");
                }*/
            }

        });

        MzText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
               // try{
                    if(data.hasChild("mz")) {
                        if (!s.toString().equals(Objects.requireNonNull(data.child("mz").getValue()).toString()) && !s.toString().equals("")) {
                            EnviarMedidasBtn.setEnabled(true);
                        } else {
                            EnviarMedidasBtn.setEnabled(false);
                        }
                    }
                /*}catch (Exception e){
                    Log.v("DB", "EMPTY ");
                }*/
            }

        });

        CedText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }
            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                boolean valid = CedText.getText().toString().matches("[0-9]{10}");
                Log.v("VALID", "onTextChanged: " + valid);
                //try{

                    if(data.hasChild("cedula")  ) {
                        if (!s.toString().equals(Objects.requireNonNull(data.child("cedula").getValue()).toString()) && !s.toString().equals("") && valid) {
                            EnviarMedidasBtn.setEnabled(true);
                            Mensaje.setText("");
                            Mensaje.setTextColor(Color.GREEN);
                        } else if(s.toString().equals(Objects.requireNonNull(data.child("cedula").getValue()).toString())){
                            Mensaje.setText("");
                            Mensaje.setTextColor(Color.GREEN);
                            EnviarMedidasBtn.setEnabled(false);
                        }else{
                            Mensaje.setText("Tiene que ser un numero de cédula valido");
                            Mensaje.setTextColor(Color.YELLOW);
                            EnviarMedidasBtn.setEnabled(false);
                        }
                    }else{
                        Mensaje.setText("ERROR BASE DE DATOS");
                        Mensaje.setTextColor(Color.RED);
                    }


               /* }catch (Exception e){
                    Log.v("DB", "EMPTY ");
                }*/
            }
        });

        NoHabit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
               // try{
                    if(data.hasChild("n_habitantes")){
                    if (!s.toString().equals(Objects.requireNonNull(data.child("n_habitantes").getValue()).toString()) && !s.toString().equals("")) {
                        EnviarMedidasBtn.setEnabled(true);
                    } else {
                        EnviarMedidasBtn.setEnabled(false);
                    }
                    }

               /* }catch (Exception e){
                    Log.v("DB", "EMPTY ");
                }*/
            }
        });

        FechaInsText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                EnviarMedidasBtn.setEnabled(true);

               /* }catch (Exception e){
                    Log.v("DB", "EMPTY ");
                }*/
            }
        });

        NombreSocioText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
               // try{
                    if(data.hasChild("nombre")){
                    if (!s.toString().equals(Objects.requireNonNull(data.child("nombre").getValue()).toString()) && !s.toString().equals("")) {
                        EnviarMedidasBtn.setEnabled(true);
                    } else {
                        EnviarMedidasBtn.setEnabled(false);
                    }
                    }

            }
        });



        FechaInsText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

               if (finsa != null){
                   if (!s.toString().equals(Objects.requireNonNull(finsa)) && !s.toString().equals("")) {
                       EnviarMedidasBtn.setEnabled(true);
                   } else {
                       EnviarMedidasBtn.setEnabled(false);
                   }
                   Log.v("FINSA", ""+ finsa);
               }

    }

});


        BuscarSocioBtn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(final View view) {


                if(!NumSocioText.getText().toString().equals("")) {

                    //reffMedidas = FirebaseDatabase.getInstance().getReference().child("Medidas").child(NumSocioText.getText().toString());
                    reffFecha.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(NumSocioText.getText().toString()).child("Medidor").hasChild("fecha_Instalacion")) {
                                String fins = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("Medidor").child("fecha_Instalacion").getValue()).toString();
                                FechaInsText.setText(fins);
                                if (fins.equals("01-1-2000")) {
                                    FechaInsText.setTextColor(Color.YELLOW);
                                } else {
                                    FechaInsText.setTextColor(Color.WHITE);
                                }
                            } else {
                                FechaInsText.setTextColor(Color.YELLOW);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    reffKEY.addValueEventListener(new ValueEventListener() {
                        int contar=0;
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.hasChild(NumSocioText.getText().toString())) {
                                limpiar();
                                EnviarMedidasBtn.setEnabled(false);
                                BorrarSocio.setEnabled(false);
                                Mensaje.setText("Socio NO Existe");
                                Mensaje.setTextColor(Color.RED);
                            } else {
                                if (contar == 0) {
                                    Mensaje.setText("Socio Encontrado");
                                    Mensaje.setTextColor(Color.GREEN);
                                }
                                try {

                                    String nombre = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("nombre").getValue()).toString();
                                    String lt = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("lt").getValue()).toString();
                                    String mz = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("mz").getValue()).toString();
                                    String cedula = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("cedula").getValue()).toString();
                                    String n_habitantes = Objects.requireNonNull(dataSnapshot.child(NumSocioText.getText().toString()).child("n_habitantes").getValue()).toString();

                                    NombreSocioText.setText(nombre);
                                    if (nombre.equals("null")) {
                                        LtText.setTextColor(Color.YELLOW);
                                    } else {
                                        LtText.setTextColor(Color.WHITE);
                                    }
                                    if (lt.equals("null")) {
                                        LtText.setTextColor(Color.YELLOW);
                                    } else {
                                        LtText.setTextColor(Color.WHITE);
                                    }
                                    if (mz.equals("null")) {
                                        MzText.setTextColor(Color.YELLOW);
                                    } else {
                                        MzText.setTextColor(Color.WHITE);
                                    }
                                    if (cedula.equals("null")) {
                                        CedText.setTextColor(Color.YELLOW);
                                    } else {
                                        CedText.setTextColor(Color.WHITE);
                                    }
                                    if (n_habitantes.equals("null")) {
                                        NoHabit.setTextColor(Color.YELLOW);
                                    } else {
                                        NoHabit.setTextColor(Color.WHITE);
                                    }

                                    LtText.setText(lt);
                                    MzText.setText(mz);
                                    CedText.setText(cedula);
                                    NoHabit.setText(n_habitantes);
                                    contar = 1;

                                    habtext();
                                    BorrarSocio.setEnabled(true);
                                } catch (Exception e) {
                                    Log.e("ERROR BUSCAR", "Something goes wrong: " );
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }else{
                    Mensaje.setText("Ingrese un Socio a Buscar");
                    Mensaje.setTextColor(Color.YELLOW);
                }
            }

        });

        BorrarSocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Add the buttons
                builder.setTitle("Eliminar Socio");
                builder.setMessage("Está seguro de eliminar el siguiente Socio: " + NumSocioText.getText().toString()+" ?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        reffKEY.child(NumSocioText.getText().toString()).removeValue();
                        reffFecha.child(NumSocioText.getText().toString()).removeValue();
                        desatext();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                        desatext();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        AddSocioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        // Add the buttons
                builder.setTitle("Creación de Socio");
                builder.setMessage("Desea Añadir el siguiente Socio: " + NumSocioText.getText().toString()+" ?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        reffKEY.child(NumSocioText.getText().toString() + "/num").setValue(NumSocioText.getText().toString());
                        reffKEY.child(NumSocioText.getText().toString() + "/lt").setValue("");
                        reffKEY.child(NumSocioText.getText().toString() + "/mz").setValue("");
                        reffKEY.child(NumSocioText.getText().toString() + "/nombre").setValue("");
                        reffKEY.child(NumSocioText.getText().toString() + "/cedula").setValue("");
                        reffKEY.child(NumSocioText.getText().toString() + "/n_habitantes").setValue("");
                        reffFecha.child(NumSocioText.getText().toString() + "/Medidor/fecha_Instalacion").setValue("01-1-2000");
                        desatext();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        desatext();
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        EnviarMedidasBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                if (!NumSocioText.getText().toString().equals("") && !LtText.getText().toString().equals("") && !MzText.getText().toString().equals("") &&
                        !NombreSocioText.getText().toString().equals("") && !NumSocioText.getText().toString().equals("") && !CedText.getText().toString().equals("")
                        && !NoHabit.getText().toString().equals("") && !FechaInsText.getText().toString().equals("")) {
                            reffKEY.child(NumSocioText.getText().toString() + "/lt").setValue(LtText.getText().toString());
                            reffKEY.child(NumSocioText.getText().toString() + "/mz").setValue(MzText.getText().toString());
                            reffKEY.child(NumSocioText.getText().toString() + "/nombre").setValue(NombreSocioText.getText().toString().toUpperCase());
                            reffKEY.child(NumSocioText.getText().toString() + "/cedula").setValue(CedText.getText().toString());
                            reffKEY.child(NumSocioText.getText().toString() + "/n_habitantes").setValue(NoHabit.getText().toString());
                            reffFecha.child(NumSocioText.getText().toString() + "/Medidor/fecha_Instalacion").setValue(FechaInsText.getText().toString());
                            EnviarMedidasBtn.setEnabled(false);
                            Mensaje.setText("Socio Actualizado Correctamente");
                            Mensaje.setTextColor(Color.GREEN);


                        } else {
                    Mensaje.setText("Todos los campos deben ser llenados correctamente");
                    Mensaje.setTextColor(Color.YELLOW);
                    EnviarMedidasBtn.setEnabled(false);
                }
            }
        });

        return v;
    }

    //FUNCIONES
    @SuppressLint("SetTextI18n")
    private void limpiar(){
        NombreSocioText.setText("");
        LtText.setText("");
        MzText.setText("");
        CedText.setText("");
        NoHabit.setText("");
        FechaInsText.setText("01-1-2000");
    }

    private void desatext(){
        NombreSocioText.setEnabled(false);
        LtText.setEnabled(false);
        MzText.setEnabled(false);
        CedText.setEnabled(false);
        NoHabit.setEnabled(false);
        FechaInsText.setEnabled(false);
    }
    private void habtext(){
        NombreSocioText.setEnabled(true);
        LtText.setEnabled(true);
        MzText.setEnabled(true);
        CedText.setEnabled(true);
        NoHabit.setEnabled(true);
        FechaInsText.setEnabled(true);
    }

}
