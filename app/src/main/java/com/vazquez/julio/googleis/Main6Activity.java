package com.vazquez.julio.googleis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main6Activity extends AppCompatActivity implements OnMapReadyCallback {

    List<Coordenadas> usuarioList;
    List<Trayecto> tra;
    private GoogleMap mMap;
    ArrayList <LatLng> latLngArray = new ArrayList<>();
    TextView fecha;
    TextView inicio;
    TextView end;
    TextView duracion;
    TextView distancia;
    TextView velocidad;
    TextView calorias;
    Button regresar;

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
        regresar = findViewById(R.id.btnMain3);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main5Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        trajet = intent.getParcelableExtra("trajet");

        tra=trajet.getTrayectos();
    }

    public boolean isOnline (){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (isOnline()){
            final Thread tr = new Thread() {
                @Override
                public void run() {
                    super.run();
                    service s = new service();
                    final String resTra = s.enviarPost(trajet.getIdTrayecto(),"http://juliovazquez.net/TrailWebServices/coordenadas.php");
                    usuarioList = service.parser2(resTra);
                    if (usuarioList.size()>0){
                        for (int i = 0; i<usuarioList.size(); i++){
                            LatLng coordenadas = new LatLng(usuarioList.get(i).getLatitud(),usuarioList.get(i).getLongitud());
                            latLngArray.add(coordenadas);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMap = googleMap;
                                PolylineOptions options = new PolylineOptions().width(15).color(Color.BLUE).geodesic(true);
                                if (latLngArray.size()>0){
                                    mMap.addMarker(new MarkerOptions().position(latLngArray.get(0)).title("Inicio"));
                                    mMap.addMarker(new MarkerOptions().position(latLngArray.get(latLngArray.size() - 1)).title("Final"));
                                    for (int z = 0; z < latLngArray.size(); z++) {
                                        options.add(latLngArray.get(z));
                                    }
                                    Polyline line = mMap.addPolyline(options);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngArray.get(latLngArray.size() - 1), 19));
                                    DecimalFormat objFormato = new DecimalFormat("#.###");
                                    fecha.setText("Fecha :"+trajet.getFecha());
                                    inicio.setText("Inicio a :"+trajet.getHoraI());
                                    end.setText("Final a :"+trajet.getHoraF());
                                    duracion.setText("Duracion : "+trajet.getDuracion());
                                    distancia.setText("Distancia :"+objFormato.format(trajet.getDistancia())+"km");
                                }
                            }
                        });
                    }
                }
            };
            tr.start();
        }
        else {
            Toast.makeText(getApplicationContext(),"Sin Conexion", Toast.LENGTH_SHORT).show();
        }
    }
}
