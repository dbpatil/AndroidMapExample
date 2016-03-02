package com.doddanna.mapexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    Location myPos;
    Location selected;
    MarkerOptions title;
    private SeekBar seekBar1, seekBar2, seekBar3;
    int zoomLevel = 10;
    int bearingLevel = 45;
    int tiltLevel = 30;

    private TextView zoomLevelTxt, bearingLevelTxt, tiltLevelTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        zoomLevelTxt = (TextView) findViewById(R.id.zoomLevel);
        bearingLevelTxt = (TextView) findViewById(R.id.bearingLevel);
        tiltLevelTxt = (TextView) findViewById(R.id.tileLevel);


        /* This seekbar is used to control the ZoomLevel  */
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                if (myPos != null) {
                    int zoom = 0;
                    zoom = progresValue * 2;
                    if (zoom == 20)
                        zoom = 21;
                    zoomLevel = zoom;
                    zoomLevelTxt.setText("Zoom : " + zoomLevel);
                    CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(myPos.getLatitude(), myPos.getLongitude())).zoom(zoom).bearing(bearingLevel).tilt(tiltLevel).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /* This seekbar is used to control the bearing level or the direction of camera  */
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                if (myPos != null) {
                    int bearing = 0;
                    bearing = progresValue * 12;
                    if (bearing > 360)
                        bearing = 360;
                    bearingLevel = bearing;
                    bearingLevelTxt.setText("Bearing : " + bearingLevel);
                    CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(myPos.getLatitude(), myPos.getLongitude())).zoom(zoomLevel).bearing(bearingLevel).tilt(tiltLevel).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        /* This seekbar is used to control the tilt level or angle between the surface and camera  */
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                if (myPos != null) {
                    int tilt = 0;
                    tilt = progresValue * 3;
                    if (tilt > 90)
                        tilt = 96;
                    tiltLevel = tilt;
                    tiltLevelTxt.setText("Tilt : " + tiltLevel);
                    CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(myPos.getLatitude(), myPos.getLongitude())).zoom(zoomLevel).bearing(bearingLevel).tilt(tiltLevel).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        mMap = googleMap;
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getFocusedBuilding();


        /* This OnMapLongClickListener helps to choose a location from the map by longclicking on the needed location.  */
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {


                CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(17).bearing(45).tilt(30).build();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location\nLongitude : " + latLng.longitude + "\n" + latLng.latitude));
                selected = new Location("a");
                selected.setLatitude(latLng.latitude);
                selected.setLongitude(latLng.longitude);
                Toast.makeText(getApplicationContext(), "Selected Location\nLongitude : " + latLng.longitude + "\n" + latLng.latitude, Toast.LENGTH_SHORT).show();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                if (myPos != null) {
                    float v = myPos.distanceTo(selected);
                    /* This calculates the distance between the new location and the user location */
                    Toast.makeText(getApplicationContext(), "Distance : " + v, Toast.LENGTH_SHORT).show();
                }
            }
        });
        LatLng sydney = new LatLng(-34, 151);
        CameraPosition cameraPosition = CameraPosition.builder().target(sydney).zoom(10).bearing(45).tilt(30).build();
        title = new MarkerOptions().position(sydney).title("Marker in Sydney");
        mMap.addMarker(title);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

           /*  In this code block helps to handle the options selected by the user,
            *
            *  The user can choose such as
            *  1.Noraml
            *  2.Hybrid
            *  3.Satellite
            *  4.Terrain
            *  5.None
            *
            * */
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.hybrid) {
            if (mMap != null)
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            return true;
        }

        if (id == R.id.normal) {
            if (mMap != null)
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;

        }

        if (id == R.id.satellite) {
            if (mMap != null)
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        }

        if (id == R.id.terrain) {
            if (mMap != null)
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        }

        if (id == R.id.none) {
            if (mMap != null)
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_LONG).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        title.position(new LatLng(location.getLatitude(), location.getLongitude()));
        title.title("New Position");
        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(zoomLevel).bearing(bearingLevel).tilt(tiltLevel).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Not Permited", Toast.LENGTH_LONG).show();
            return;
        }
        mMap.clear();
        myPos = new Location("b");
        myPos.setLatitude(location.getLatitude());
        myPos.setLongitude(location.getLongitude());
        mMap.addMarker(title);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.doddanna.mapexample/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.doddanna.mapexample/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
    }
}


