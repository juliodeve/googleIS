package com.vazquez.julio.googleis.ACTIVITYS;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.vazquez.julio.googleis.ADAPTERS.TrayectoAdapter;
import com.vazquez.julio.googleis.HTTPMANAGER.global;
import com.vazquez.julio.googleis.HTTPMANAGER.service;
import com.vazquez.julio.googleis.POJO.Trayecto;
import com.vazquez.julio.googleis.R;

import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    List<Trayecto> usuarioList;
    RecyclerView recyclerView;
    TrayectoAdapter adapterRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (isOnline()){
            final Thread tr = new Thread() {
                @Override
                public void run() {
                    super.run();
                    service s = new service();
                    final String resTra = s.enviarPost(global.email,"http://juliovazquez.net/TrailWebServices/trayectos.php");

                    if (resTra.equals("NOHAY")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"No tiene trayectos",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        usuarioList = service.parser(resTra);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (usuarioList.size()>0){
                                    adapterRV = new TrayectoAdapter(getApplicationContext(),usuarioList);
                                    recyclerView.setAdapter(adapterRV);
                                    recyclerView.setHasFixedSize(true);

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

}
