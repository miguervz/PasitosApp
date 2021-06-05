package es.studium.pasitosapp;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import es.studium.pasitosapp.Controlador.Controlador;
import es.studium.pasitosapp.Modelos.InformacionMovil;

public class Main extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap map;
    private double latitud;
    private double longitud;
    int bateriaMovil;
    private Controlador pasitosController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //Muestra el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync( this);




        //Comprobamos permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        else
        {
            locationStart();
        }

        // Batería
        registerReceiver(nivelBateria, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        //Controlador de la BS
        pasitosController = new Controlador(Main.this);
    }

    // Bateria
    private BroadcastReceiver nivelBateria = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bateriaMovil = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        }
    };

    //Obtener la posición
    private void locationStart()
    {
        LocationManager mlocManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled)

        {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 0,
                (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, (LocationListener)
                Local);

    }
    //Prueba los permisos
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults)
    {
        if (requestCode == 1000)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                locationStart();
                return;
            }
        }
    }


    public class Localizacion implements LocationListener
    {
        Main mainActivity;
        public Main getMainActivity()

        {
            return mainActivity;
        }
        public void setMainActivity(Main mainActivity)
        {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc)
        {
            // Este método se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la detección de un cambio de ubicación
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            this.mainActivity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider)
        {
            // Este método se ejecuta cuando el GPS es desactivado
           Log.d("debug","GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            // Este método se ejecuta cuando el GPS es activado
            Log.d("debug","GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            switch (status)
            {
                case 0:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case 1:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
                case 2:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
            }
        }
    }

    private void setLocation(Location loc) {
        //Obtener la dirección actual
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0)
        {
            try
            {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty())
                {
                    LatLng posicion = new LatLng(latitud, longitud);
                    map.addMarker(new MarkerOptions()
                            .position(posicion)
                            .title("Latitud = "+latitud+" Longitud = "+longitud)
                            .snippet("Nivel Bateria: "+ bateriaMovil +"%")
                            .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_myplaces))
                            .anchor(0.5f,0.5f));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 18));
                }
                //Guardar los datos
               InformacionMovil datos = new InformacionMovil(latitud, longitud, bateriaMovil);
               pasitosController.nuevoDatos(datos);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
        }
    }
}