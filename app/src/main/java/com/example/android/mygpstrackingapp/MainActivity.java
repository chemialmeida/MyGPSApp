package com.example.android.mygpstrackingapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mensaje1;
    TextView mensaje2;

    //Establece el minimo intervalo de distancia entre actualizaciones
    private static final long MIN_CAMBIO_DISTANCIA_PARA_UPDATES = 1; //1 metro

    //Establece el minimo intervalo de tiempo entre actualizaciones de ubicación en milisegundos
    private static final long MIN_TIEMPO_ENTRE_UPDATES = 1000 * 60 * 10; // 10 minutos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asigno el id del TextView en el XML a nuestra variable del mismo tipo en Java
        mensaje1 = (TextView) findViewById(R.id.textViewLatitudeValue);
        mensaje2 = (TextView) findViewById(R.id.textViewLongitudeValue);

        //Verifico los permisos de acceso dentro del Manifest.xml
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
    }

    //funcion que inicia la busqueda de la localización
    private void locationStart() {
        //inicializa el LocationManager
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Inicializo una nueva variable del tipo LocationListener
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);

        //declaro una variable boolean para verificar si el GPS se encuentra activo en el dispositivo
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }

        //Llamada al metodo requestLocationUpdates el cual hace la llamada al metodo onLocationChanged()
        //Este metodo especifica cada cuanto tiempo y la distancia minima requerida para que se observen cambios
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIEMPO_ENTRE_UPDATES, MIN_CAMBIO_DISTANCIA_PARA_UPDATES, Local);

    }

    //Verificación del permiso
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    // Aqui empieza la Clase Localizacion del tipo LocationListener
    //LocationListener es el metodo de escucha en el cambio de ubicación del dispositivo
    //Es una clase abstracta que implementa las siguientes funciones, onLocationChanged,
    // onProviderDisabled, onProviderEnabled, onStatusChanged
    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String text1 = Double.toString(loc.getLatitude());
            String text2 = Double.toString(loc.getLongitude());
            mensaje1.setText(text1);
            mensaje2.setText(text2);

        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            mensaje1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            mensaje1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}