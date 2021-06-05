package es.studium.pasitosapp.Modelos;

public class InformacionMovil
{
    private int bateriaMovil;

    private double longitud;

    private double latitud;
    private long id;
    public InformacionMovil(double latitud, double longitud, int bateria){
        this.latitud = latitud;
        this.longitud = longitud;
        this.bateriaMovil = bateria;
    }

    //Creamos los datos que vamos a introducir en la BD
    public InformacionMovil(double latitud, double longitud, int bateria, long id){
        this.latitud = latitud;
        this.longitud = longitud;
        this.bateriaMovil = bateria;
        this.id = id;
    }

    public long getId(){return id;}

    public void setId(long id){this.id=id;}

    public int getBateriaMovil() {return bateriaMovil;}

    public void setBateriaMovil(int bateriaMovil){this.bateriaMovil = bateriaMovil;}



    public double getLongitud(){return longitud;}

    public void setLongitud(double longitud){this.longitud=longitud;}


    public double getLatitud(){return latitud;}

    public void setLatitud(double latitud){this.latitud=latitud;}



}
