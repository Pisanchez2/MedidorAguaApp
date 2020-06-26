package com.sr.tasrfb;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sr.tasrfb.ui.DBs.Socios.Medidor;
import com.sr.tasrfb.ui.DBs.Socios.Socio;

public class Socios extends Fragment {


    private TextView NumSocioText, NombreSocioText, MzText,LtText,NumMedidorText,CedText, NoHabit;
    private EditText FechaInsText;
    private Socio socio;
    private DatabaseReference reff;
    private DatabaseReference reffBuscar , reffMedidas;
    private DatabaseReference reff2;

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
        View v= inflater.inflate(R.layout.fragment_socios, container, false);
        NumSocioText=v.findViewById(R.id.SNumsocioText);
        NombreSocioText=v.findViewById(R.id.SNombreSocioText);
        MzText=v.findViewById(R.id.SMzText);
        LtText=v.findViewById(R.id.SLtText);
        CedText=v.findViewById(R.id.SCedText);
        NoHabit=v.findViewById(R.id.SNhabitext);
        //NumMedidorText=v.findViewById(R.id.SNumMedidorText);
        FechaInsText=v.findViewById(R.id.SFechaInstalacionText);
        final Button EnviarMedidasBtn=v.findViewById(R.id.ActualizarBtn);
        ImageButton BuscarSocioBtn2=v.findViewById(R.id.BuscarSocioBtn);
        EnviarMedidasBtn.setEnabled(false);

      final boolean ESTADO = false;

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

        BuscarSocioBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int cestado = 0;
                if(!NumSocioText.getText().toString().equals("")) {

                    reffMedidas = FirebaseDatabase.getInstance().getReference().child("Medidas").child(NumSocioText.getText().toString());
                    reffMedidas.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("Medidor").hasChild("fecha_Instalacion")) {
                                String fins = dataSnapshot.child("Medidor").child("fecha_Instalacion").getValue().toString();
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

                    reffBuscar = FirebaseDatabase.getInstance().getReference().child("Socio");
                    reffBuscar.addValueEventListener(new ValueEventListener() {
                        int contar=0;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String Mensaje= "";
                            int color;
                            if (dataSnapshot.hasChild(NumSocioText.getText().toString())) {
                                if(contar==0) {
                                    EnviarMedidasBtn.setEnabled(true);
                                }
                                String nombre = dataSnapshot.child(NumSocioText.getText().toString()).child("nombre").getValue().toString();
                                String lt = dataSnapshot.child(NumSocioText.getText().toString()).child("lt").getValue().toString();
                                String mz = dataSnapshot.child(NumSocioText.getText().toString()).child("mz").getValue().toString();
                                String cedula = dataSnapshot.child(NumSocioText.getText().toString()).child("cedula").getValue().toString();
                                String n_habitantes = dataSnapshot.child(NumSocioText.getText().toString()).child("n_habitantes").getValue().toString();

                                NombreSocioText.setText(nombre);

                                if (lt.equals("null") ) {
                                    LtText.setTextColor(Color.YELLOW);
                                } else {
                                    LtText.setTextColor(Color.WHITE);
                                }
                                if (mz.equals("null") ) {
                                    MzText.setTextColor(Color.YELLOW);
                                } else {
                                    MzText.setTextColor(Color.WHITE);
                                }
                                if (cedula.equals("null") ) {
                                    CedText.setTextColor(Color.YELLOW);
                                } else {
                                    CedText.setTextColor(Color.WHITE);
                                }
                                if (n_habitantes.equals("null") ) {
                                    NoHabit.setTextColor(Color.YELLOW);
                                } else {
                                    NoHabit.setTextColor(Color.WHITE);
                                }

                                LtText.setText(lt);
                                MzText.setText(mz);
                                CedText.setText(cedula);
                                NoHabit.setText(n_habitantes);
                                Mensaje="Socio Encontrado";
                                color = R.color.sucess;
                                contar=1;
                            } else {
                                NombreSocioText.setText("");
                                LtText.setText("");
                                MzText.setText("");
                                CedText.setText("");
                                NoHabit.setText("");
                                Mensaje="Socio NO Encontrado";
                                color= R.color.error;
                                EnviarMedidasBtn.setEnabled(false);
                            }
                            //Snackbar.make(view, Mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }else{
                  //  Snackbar.make(view, "Ingrese socio a buscar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //Ingresar mensaje de notificacion
                }
            }

        });

        EnviarMedidasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NumSocioText.getText().toString().equals("") || LtText.getText().toString().equals("") || MzText.getText().toString().equals("") ||
                        NombreSocioText.getText().toString().equals("") || NumSocioText.getText().toString().equals("") || CedText.getText().toString().equals("")
                        || NoHabit.getText().toString().equals("") || FechaInsText.getText().toString().equals("") ){
                        //Notificacion de que algunos campos estan vacios
                }else {
                    reff= FirebaseDatabase.getInstance().getReference().child("Socio");
                    reff.child(NumSocioText.getText().toString() + "/lt").setValue(LtText.getText().toString());
                    reff.child(NumSocioText.getText().toString() + "/mz").setValue(MzText.getText().toString());
                    reff.child(NumSocioText.getText().toString() + "/nombre").setValue(NombreSocioText.getText().toString());
                    reff.child(NumSocioText.getText().toString() + "/num").setValue(NumSocioText.getText().toString());
                    reff.child(NumSocioText.getText().toString() + "/cedula").setValue(CedText.getText().toString());
                    reff.child(NumSocioText.getText().toString() + "/n_habitantes").setValue(NoHabit.getText().toString());
                    reff2 = FirebaseDatabase.getInstance().getReference().child("Medidas/" + NumSocioText.getText().toString() + "/Medidor/fecha_Instalacion");
                    reff2.setValue(FechaInsText.getText().toString());
                    EnviarMedidasBtn.setEnabled(false);
                }
            }
        });

        return v;
    }

}
