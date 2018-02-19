package com.vazquez.julio.googleis.ACTIVITYS;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vazquez.julio.googleis.HTTPMANAGER.global;
import com.vazquez.julio.googleis.HTTPMANAGER.service;
import com.vazquez.julio.googleis.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultadosActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String fecha, horaI, horaF, duracion, idT;
    double velocidad, distancia;
    int calorias;
    TextView txtFecha, txtInicio, txtFinal, txtDuracion, txtVelocidad, txtDistancia, txtCalorias;
    Button btnMain;
    String res = "";
    int i = 0;
    ArrayList<LatLng> latLngArray = global.latLngArray;
    ArrayList<String> hora = global.hora;
    String email = global.email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DecimalFormat objFormato = new DecimalFormat("#.###");

        txtFecha = (TextView) findViewById(R.id.txtFecha3);
        txtInicio = (TextView) findViewById(R.id.txtStart3);
        txtFinal = (TextView) findViewById(R.id.txtEnd3);
        txtDuracion = (TextView) findViewById(R.id.txtTiempo3);
        txtVelocidad = (TextView) findViewById(R.id.txtVelocidad3);
        txtDistancia = (TextView) findViewById(R.id.txtDistancia3);
        txtCalorias = (TextView) findViewById(R.id.txtCalorias3);
        btnMain = (Button) findViewById(R.id.btnMain3);

        Intent intent = getIntent();
        fecha = intent.getStringExtra("fecha");
        horaI = intent.getStringExtra("horaI");
        horaF = intent.getStringExtra("horaF");
        duracion = intent.getStringExtra("duracion");
        velocidad = intent.getDoubleExtra("velocidad", 0.0);
        distancia = intent.getDoubleExtra("distancia", 0.0);
        calorias = intent.getIntExtra("calorias", 0);
        idT = intent.getStringExtra("idT");

        txtFecha.setText("Fecha: " + fecha);
        txtInicio.setText(horaI);
        txtFinal.setText(horaF);
        txtDuracion.setText(duracion);
        distancia = distancia / 1000;
        txtDistancia.setText(objFormato.format(distancia) + " Km");
        txtVelocidad.setText(objFormato.format(velocidad) + " Km/h");
        txtCalorias.setText(calorias + " Kcal");

        final Thread tr = new Thread() {
            @Override
            public void run() {
                super.run();
                service s = new service();
                String resTra = s.enviarPost(idT, horaI, horaF, fecha, velocidad, distancia, duracion, 0, "http://www.juliovazquez.net/TrailWebServices/actualizarTrayecto.php");
            }
        };
        tr.start();


        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultadosActivity.this, ActivityPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PolylineOptions options = new PolylineOptions().width(15).color(Color.BLUE).geodesic(true);
        mMap.addMarker(new MarkerOptions().position(latLngArray.get(0)).title("Inicio"));
        mMap.addMarker(new MarkerOptions().position(latLngArray.get(latLngArray.size() - 1)).title("Final"));
        for (int z = 0; z < latLngArray.size(); z++) {
            options.add(latLngArray.get(z));
        }
        Polyline line = mMap.addPolyline(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngArray.get(latLngArray.size() - 1), 19));

        for (i = 0; i < global.latLngArray.size() - 1; i++) {
            final Thread tr2 = new Thread() {
                @Override
                public void run() {
                    super.run();
                    service s = new service();
                    LatLng coordenadas = latLngArray.get(i);
                    String hor = hora.get(i);
                    if (i > 0) {
                        if (coordenadas != latLngArray.get(i - 1)) {
                            double lat = coordenadas.latitude;
                            double lng = coordenadas.longitude;
                            final String res2 = s.enviarPost(hor, idT, lat, lng, "http://www.juliovazquez.net/TrailWebServices/ingresarCoordenadas.php");
                        }
                    } else {
                        double lat = coordenadas.latitude;
                        double lng = coordenadas.longitude;
                        final String res2 = s.enviarPost(hor, idT, lat, lng, "http://www.juliovazquez.net/TrailWebServices/ingresarCoordenadas.php");
                    }
                }
            };
            tr2.start();
        }
        Toast.makeText(getApplicationContext(),fecha +" " +horaI+" " +horaF+" " +distancia+" dura" +duracion+" vrlo" +velocidad,Toast.LENGTH_LONG).show();
    }

}