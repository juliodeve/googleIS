package com.vazquez.julio.googleis.ACTIVITYS;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.vazquez.julio.googleis.HTTPMANAGER.global;
import com.vazquez.julio.googleis.HTTPMANAGER.service;
import com.vazquez.julio.googleis.R;

import java.text.DecimalFormat;
import java.util.Calendar;

public class ActivityPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    String loginMail, result = "no";
    ImageView imgPhoto;
    TextView txtEmail, txtNom;
    TextView distancia;
    TextView txtVelocidad;
    static final int LOCATION_REQUEST_CODE = 2;
    Chronometer cr;
    Button btnStart, btnEnd;
    private Marker marcador;
    double lat = 0.0;
    String fecha = "";
    String horaI = "";
    String horaF = "", resTra = "";
    double vel;
    double distance = 0.0;
    String duracion = "";
    int calorias = 0;
    LatLng coordenadas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cr = (Chronometer) findViewById(R.id.chronometer1);
        intent();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hview = navigationView.getHeaderView(0);
        imgPhoto = hview.findViewById(R.id.imgPhoto2);
        txtEmail = hview.findViewById(R.id.txtEmail2);
        txtNom = hview.findViewById(R.id.txtNom2);
        distancia = (TextView) findViewById(R.id.txtDistancia2);
        txtVelocidad = (TextView) findViewById(R.id.txtVelocidad2);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnEnd = (Button) findViewById(R.id.btnEnd);
        btnEnd.setEnabled(false);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cr.setBase(SystemClock.elapsedRealtime());
                mMap.clear();
                btnStart.setEnabled(false);
                global.latLngArray.clear();
                global.hora.clear();
                Calendar c1 = Calendar.getInstance();
                String dia = Integer.toString(c1.get(Calendar.DATE));
                String mes = Integer.toString(c1.get(Calendar.MONTH)+1);
                String annio = Integer.toString(c1.get(Calendar.YEAR));
                String hora = Integer.toString(c1.get(Calendar.HOUR_OF_DAY));
                String minutos = Integer.toString(c1.get(Calendar.MINUTE));
                String seg = Integer.toString(c1.get(Calendar.SECOND));
                fecha = annio +"/"+mes+"/"+dia;
                horaI = hora+":"+minutos+":"+seg;
                Toast.makeText(ActivityPrincipal.this, "Empiece a correr cuando aparezca su posicion", Toast.LENGTH_LONG).show();

                final Thread tr = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        service s = new service();
                        resTra = s.enviarPost(global.email, horaI, "", fecha, 0, 0, "", 0, "http://www.juliovazquez.net/TrailWebServices/ingresarTrajecto.php");
                    }
                };
                tr.start();

                global.map = true;

                LocationManager locationManager = (LocationManager) ActivityPrincipal.this.getSystemService(Context.LOCATION_SERVICE);
                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        coordenadas = new LatLng(location.getLatitude(), location.getLongitude());

                        Calendar c1 = Calendar.getInstance();
                        String hora = Integer.toString(c1.get(Calendar.HOUR_OF_DAY));
                        String minutos = Integer.toString(c1.get(Calendar.MINUTE));
                        String seg = Integer.toString(c1.get(Calendar.SECOND));

                        global.hora.add(hora+":"+minutos+":"+seg);
                        global.latLngArray.add(coordenadas);
                        if (global.map) {
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(global.latLngArray.get(0)).title("Inicio"));
                            mMap.addMarker(new MarkerOptions().position(global.latLngArray.get(global.latLngArray.size()-1)).title("Final"));
                            task();
                        }
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                    }
                };

                int permissionCheck = ContextCompat.checkSelfPermission(ActivityPrincipal.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cr.stop();
                duracion = cr.getText().toString();
                global.map = false;
                if (global.latLngArray.size() > 1) {
                    mMap.addMarker(new MarkerOptions().position(global.latLngArray.get(global.latLngArray.size() - 1)).title("Final"));
                }
                long elapsedMillis = SystemClock.elapsedRealtime() - cr.getBase();
                double heures = elapsedMillis * 0.000001;
                Calendar c1 = Calendar.getInstance();
                String hora = Integer.toString(c1.get(Calendar.HOUR_OF_DAY));
                String minutos = Integer.toString(c1.get(Calendar.MINUTE));
                String seg = Integer.toString(c1.get(Calendar.SECOND));
                horaF = hora+":"+minutos+":"+seg;
                Intent intent = new Intent(ActivityPrincipal.this, ResultadosActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("horaI", horaI);
                intent.putExtra("horaF", horaF);
                intent.putExtra("idtrajet", resTra);
                intent.putExtra("fecha", fecha);
                intent.putExtra("velocidad", vel);
                intent.putExtra("distancia", distance);
                intent.putExtra("duracion", duracion);
                intent.putExtra("calorias", calorias);
                intent.putExtra("idT",resTra);
                startActivity(intent);
            }
        });
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }
    }

    //ViewMap
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        goLoginScreen();
                    } else {
                        Toast.makeText(getApplicationContext(), "No close session", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            // Handle the camera action
            Intent intent = new Intent(this,PerfilActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_historial) {
            Intent intent = new Intent(this,HistorialActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void intent() {
        if (global.ivar1 == 1) {
            Intent intent = getIntent();
            result = intent.getStringExtra("result");
            loginMail = intent.getStringExtra("mail");
            global.ivar1 = 0;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else if (result.equals("si")) {
            handleSignInResult();
        } else if (global.email != null) {
            loginMail = global.email;
            handleSignInResult();
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            txtNom.setText(account.getDisplayName());
            global.email = account.getEmail();
            global.photo = account.getPhotoUrl();
            txtEmail.setText(account.getEmail());
            Glide.with(this).load(account.getPhotoUrl()).into(imgPhoto);
            final Thread tr = new Thread() {
                @Override
                public void run() {
                    super.run();
                    service s = new service();
                    s.enviarPost(txtEmail.getText().toString(), "", "http://juliovazquez.net/TrailWebServices/loginGoogle.php");
                }
            };
            tr.start();
        } else {
            goLoginScreen();
        }
    }

    private void handleSignInResult() {
        txtEmail.setText(loginMail);
    }

    private void goLoginScreen() {
        global.email = null;
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLoginScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "No close session", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void task() {
//                        mMap.addMarker(new MarkerOptions().position(coordenadas).title("Estas aqui"));

        PolylineOptions options = new PolylineOptions().width(15).color(Color.BLUE).geodesic(true);
        if (global.latLngArray.size() > 0) {
            cr.start();
            distance = 0;
            btnEnd.setEnabled(true);
            long elapsedMillis = SystemClock.elapsedRealtime() - cr.getBase();
            double heures = elapsedMillis * 0.000001;
            for (int z = 0; z < global.latLngArray.size(); z++) {
                options.add(global.latLngArray.get(z));
                if (z + 1 <= global.latLngArray.size() - 1) {
                    distance += SphericalUtil.computeDistanceBetween(global.latLngArray.get(z), global.latLngArray.get(z + 1));
                }
                DecimalFormat objFormato = new DecimalFormat("#.###");
                double x = distance;
                x = x / 1000;
                distancia.setText(objFormato.format(x) + " Km");
                vel = x / heures;
                txtVelocidad.setText(objFormato.format(vel) + " Km/h");
            }
        }

        Polyline line = mMap.addPolyline(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(global.latLngArray.get(global.latLngArray.size() - 1), 19));
    }
}