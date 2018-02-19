package com.vazquez.julio.googleis.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by julio on 12/02/2018.
 */

public class Trayecto implements Parcelable {
    private int idTrayecto;
    private String fecha;
    private String email;
    private String horaI;
    private String horaF;
    private double velocidad;
    private double distancia;
    private int calorias;
    private String duracion;

    public ArrayList<Trayecto> getTrayectos() {
        return trayectos;
    }

    public void setTrayectos(ArrayList<Trayecto> trayectos) {
        this.trayectos = trayectos;
    }

    private ArrayList<Trayecto>  trayectos;

    public Trayecto() {

    }

    public int getIdTrayecto() {
        return idTrayecto;
    }

    public void setIdTrayecto(int idTrayecto) {
        this.idTrayecto = idTrayecto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoraI() {
        return horaI;
    }

    public void setHoraI(String horaI) {
        this.horaI = horaI;
    }

    public String getHoraF() {
        return horaF;
    }

    public void setHoraF(String horaF) {
        this.horaF = horaF;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    protected Trayecto(Parcel in) {
        idTrayecto = in.readInt();
        fecha = in.readString();
        email = in.readString();
        horaI = in.readString();
        horaF = in.readString();
        velocidad = in.readDouble();
        distancia = in.readDouble();
        calorias = in.readInt();
        duracion = in.readString();

        if (in.readByte() == 0x01) {
            trayectos = new ArrayList<Trayecto>();
            in.readList(trayectos, Trayecto.class.getClassLoader());
        } else {
            trayectos = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idTrayecto);
        parcel.writeString(fecha);
        parcel.writeString(email);
        parcel.writeString(horaI);
        parcel.writeString(horaF);
        parcel.writeDouble(velocidad);
        parcel.writeDouble(distancia);
        parcel.writeInt(calorias);
        parcel.writeString(duracion);
        if (trayectos == null) {
            parcel.writeByte((byte) (0x00));
        }
        else {
            parcel.writeByte((byte) (0x01));
            parcel.writeList(trayectos);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trayecto> CREATOR = new Parcelable.Creator<Trayecto>() {
        @Override
        public Trayecto createFromParcel(Parcel in) {
            return new Trayecto(in);
        }

        @Override
        public Trayecto[] newArray(int size) {
            return new Trayecto[size];
        }
    };
}
