package com.example.hackathon30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener {
private MapView mapView;
private MapboxMap map;
private PermissionsManager PM;
private LocationEngine LE;
private LocationLayerPlugin LLP;
private Location originLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
   mapView=(MapView) findViewById(R.id.mapView);
   mapView.onCreate(savedInstanceState);
   mapView.getMapAsync(this);
    }
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
map=mapboxMap;
enableLocation();
    }
    private void enableLocation()
    {
        if(PermissionsManager.areLocationPermissionsGranted(this))
        {
            intializelocationengine();
            intializelocationlayer();
        }
        else
        {
            PM = new PermissionsManager (this);
        PM.requestLocationPermissions(this);
        }

    }
    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
LE.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
if (location!=null)
{
    originLocation=location;
    camerapos(location);
}    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        // present toast or dialog.
    }

    @Override
    public void onPermissionResult(boolean granted) {
if(granted)
{
    enableLocation();
}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PM.onRequestPermissionsResult(requestCode , permissions,grantResults);
    }
    @SuppressWarnings("MissingPermission")
    private void intializelocationengine()
    {
        LE=new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        LE.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        LE.activate();
        Location lastloc = LE.getLastLocation();
        if(lastloc!=null)
        {
            originLocation=lastloc;
            camerapos(lastloc);
        }
        else {LE.addLocationEngineListener(this);}
    }
    @SuppressWarnings("MissingPermission")
    private void intializelocationlayer()
    {
LLP= new LocationLayerPlugin(mapView,map,LE);
LLP.setLocationLayerEnabled(true);
LLP.setCameraMode(CameraMode.TRACKING);
LLP.setRenderMode(RenderMode.NORMAL);
    }
    private  void camerapos(Location location)
    {map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
            location.getLongitude()),12.0));}

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    mapView.onDestroy();}


}
