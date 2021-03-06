package com.vazquez.julio.googleis.ACTIVITYS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.vazquez.julio.googleis.HTTPMANAGER.service;
import com.vazquez.julio.googleis.PARSERS.ParserCoordenadas;
import com.vazquez.julio.googleis.POJO.Coordenadas;
import com.vazquez.julio.googleis.POJO.Trayecto;
import com.vazquez.julio.googleis.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetallesTrayectoActivity extends AppCompatActivity implements OnMapReadyCallback {

    List<Coordenadas> coordenadasList;
    List<Trayecto> trayectoList;
    private GoogleMap mMap;
    ArrayList<LatLng> latLngArray = new ArrayList<>();
    TextView fecha;
    TextView inicio;
    TextView end;
    TextView duracion;
    TextView distancia;
    TextView velocidad;
    TextView calorias;

    Trayecto trajet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fecha = findViewById(R.id.txtFecha3);
        inicio = findViewById(R.id.txtStart3);
        end = findViewById(R.id.txtEnd3);
        duracion = findViewById(R.id.txtTiempo3);
        distancia = findViewById(R.id.txtDistancia3);
        velocidad = findViewById(R.id.txtVelocidad3);
        calorias = findViewById(R.id.txtCalorias3);

        Intent intent = getIntent();
        trajet = intent.getParcelableExtra("trajet");

        trayectoList = trajet.getTrayectos();
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (isOnline()) {
            final Thread tr = new Thread() {
                @Override
                public void run() {
                    super.run();
                    service s = new service();
                    final String resTra = s.enviarPost(trajet.getIdTrayecto(), "http://juliovazquez.net/TrailWebServices/coordenadas.php");
                    coordenadasList = ParserCoordenadas.parser(resTra);
                    if (coordenadasList.size() > 0) {
                        for (int i = 0; i < coordenadasList.size(); i++) {
                            LatLng coordenadas = new LatLng(coordenadasList.get(i).getLatitud(), coordenadasList.get(i).getLongitud());
                            latLngArray.add(coordenadas);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMap = googleMap;
                                PolylineOptions options = new PolylineOptions().width(15).color(Color.BLUE).geodesic(true);
                                if (latLngArray.size() > 0) {
                                    mMap.addMarker(new MarkerOptions().position(latLngArray.get(0)).title(getString(R.string.inicio)));
                                    mMap.addMarker(new MarkerOptions().position(latLngArray.get(latLngArray.size() - 1)).title(getString(R.string.posicion_actual)));
                                    for (int z = 0; z < latLngArray.size(); z++) {
                                        options.add(latLngArray.get(z));
                                    }
                                    Polyline line = mMap.addPolyline(options);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngArray.get(latLngArray.size() - 1), 19));
                                    DecimalFormat objFormato = new DecimalFormat("#.###");
                                    fecha.setText(R.string.fecha+": " + trajet.getFecha());
                                    inicio.setText(R.string.inicio+": " + trajet.getHoraI());
                                    end.setText(R.string.final_+": " + trajet.getHoraF());
                                    duracion.setText(R.string.duracion+": " + trajet.getDuracion());
                                    distancia.setText(R.string.distancia+": " + objFormato.format(trajet.getDistancia()) + "km");
                                }
                            }
                        });
                    }
                }
            };
            tr.start();
        } else {
            Toast.makeText(getApplicationContext(), R.string.sin_internet, Toast.LENGTH_SHORT).show();
        }
    }
}
