package com.geeks_studio.localization;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geeks_studio.localization.Utils.JSONParser;
import com.geeks_studio.localization.Utils.PathJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    JSONObject jobj = null;
    JSONParser jsonparser = new JSONParser();

    Dataclass data = new Dataclass(this);
    String[] nombre;
    LatLng[] ubicacion;
    String[] direccion;
    Integer[] foto;
    String[] horario;

    private LatLng CoordDestiny = new LatLng(0, 0);
    private LatLng CoordOrigin = new LatLng(0, 0);
    private static final String LOG_TAG = "success";

    double distan = 0;
    double durat = 0;

    GoogleMap googlemap;
    MapView mapview;
    Polyline polylinefinal;
    public String destino;
    Location location;
    LocationManager locationManager;
    LocationListener locationListener;
    final String locationProvider = LocationManager.GPS_PROVIDER;

    LinearLayout llslidemenu;
    ImageView ivfoto;
    TextView tvnombre, tvhorario, tvdir, tvdis, tvdur;

    private SlidingUpPanelLayout mLayout;
    Toolbar toolbar;

    SharedPreferences sharedPreferences;
    String PREF_TAG = "MY_PREF";
    String TAG_1 = "Logged";
    Boolean logged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        llslidemenu = (LinearLayout) findViewById(R.id.dragView);
        tvnombre = (TextView) findViewById(R.id.TVtitulo);
        tvdir = (TextView) findViewById(R.id.TVdir);
        tvhorario = (TextView) findViewById(R.id.TVhorario);
        ivfoto = (ImageView) findViewById(R.id.IVfoto);
        tvdis = (TextView) findViewById(R.id.TVdist);
        tvdur = (TextView) findViewById(R.id.TVdura);

        StartGPS();
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.GMmain);
        mapFragment.getMapAsync(MainActivity.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void _(String s) {
        Log.e("MainActivity", "#  " + s);
    }

    public void storepreferences(boolean state) {
        sharedPreferences = this.getSharedPreferences(PREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_1, state);
        editor.commit();
    }

    public void StartGPS() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location1) {
                // Called when a new location is found by the network location provider.\
                location = location1;
                _("location" + " " + location1.getLatitude() + " " + location1.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

        };
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                _("listening");
                locationManager.requestLocationUpdates(locationProvider, 5, 10, locationListener);
            }
    }

    public class waitGPS extends AsyncTask<LatLng, LatLng, LatLng>{

        ProgressDialog pdGPS;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdGPS=new ProgressDialog(MainActivity.this);
            pdGPS.setIndeterminate(false);
            pdGPS.setCancelable(false);
            pdGPS.setTitle("GPS");
            pdGPS.setMessage("Espere mientras se obtiene su posición.");
            pdGPS.show();
        }

        @Override
        protected LatLng doInBackground(LatLng... params) {
            LatLng myPos = GetLocation();
            while(location==null){
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //_("posicion nula");
                //myPos=GetLocation();
            }
            myPos=new LatLng(location.getLatitude(),location.getLongitude());
            _(myPos.latitude + " " + myPos.longitude);
            return myPos;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            CoordOrigin=latLng;
            _(latLng.latitude + " " + latLng.longitude);
            googlemap.addMarker(new MarkerOptions()
                    .position(CoordOrigin)
                    .title("Your Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(CoordOrigin,15));
            pdGPS.dismiss();
            super.onPostExecute(latLng);
        }
    }

    public LatLng GetLocation() {
        double lat, lon;
        LatLng mypos;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            _("no permission");
            return null;
        } else {
            location = locationManager.getLastKnownLocation(locationProvider);
            if (location == null) {
                return null;
                //Toast.makeText(context, getResources().getString(R.string.SomethingWrong_msg), Toast.LENGTH_SHORT).show();
            } else {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
            mypos = new LatLng(lat, lon);
        }
        return mypos;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.OpTiendas) {
            toolbar.setTitle("TIENDAS");
            setTiendasinMap();
        } else if (id == R.id.OpRestaurantes) {
            toolbar.setTitle("RESTAURANTES");
            setRestaurantesinMap();
        } else if (id == R.id.OpFarmacias) {
            toolbar.setTitle("FARMACIAS");
            setFarmaciasinMap();
        } else if (id==R.id.LogOut){
            storepreferences(false);
            finish();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        googlemap.clear();
        //resetSlideMenu();
        setMarkers();
        setCloser();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setTiendasinMap(){
        nombre=data.tiendas;
        ubicacion=data.tiendapos;
        direccion=data.tiendadir;
        foto=data.tiendafoto;
        horario=data.tiendahorario;
    }
    public void setRestaurantesinMap(){
        nombre=data.restaurantes;
        ubicacion=data.restpos;
        direccion=data.restdir;
        foto=data.restfoto;
        horario=data.resthorario;
    }
    public void setFarmaciasinMap(){
        nombre=data.farmacias;
        ubicacion=data.farmpos;
        direccion=data.farmdir;
        foto=data.farmfoto;
        horario=data.farmhorario;
    }

    public void setCloser()
    {
        double distmenor = 800000000;
        int posmenor=0;
        for (int i=0;i<4;i++) {
            double dif1 = CoordOrigin.latitude - ubicacion[i].latitude;
            double dif2 = CoordOrigin.longitude-ubicacion[i].longitude;
            double dist = dif1*dif1 + dif2*dif2;
            if(dist<distmenor)
            {
                distmenor=dist;
                posmenor=i;
            }
        }
        setSlideMenu(posmenor);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googlemap=map;
        //GetLocation();
        googlemap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int id;
                if(!marker.getTitle().equals("Your Position")) {
                    if (marker.getTitle().equals(nombre[0])) {
                        id = 0;
                    } else if(marker.getTitle().equals(nombre[1])){
                        id = 1;
                    }else if(marker.getTitle().equals(nombre[2])){
                        id = 2;
                    }else {
                        id = 3;
                    }
                    setSlideMenu(id);
                }
                return false;
            }
        });
        googlemap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                resetSlideMenu();
            }
        });
        waitGPS waitgps = new waitGPS();
        waitgps.execute(CoordOrigin);
    }
    public void setMarkers(){
        GetLocation();
        googlemap.addMarker(new MarkerOptions()
                .position(CoordOrigin)
                .title("Your Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        for(int i=0;i<4;i++) {
            googlemap.addMarker(new MarkerOptions().position(ubicacion[i]).title(nombre[i]));
        }
        //googlemap.addMarker(new MarkerOptions().position(ubicacion[1]).title(nombre[1]));
        LatLng centralpoint = getCentral(CoordOrigin,ubicacion);
        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(centralpoint,12));
    }

    public LatLng getCentral(LatLng place, LatLng[] places)
    {
        double latcent, loncent;
        latcent=place.latitude;
        loncent=place.longitude;
        for (int i=0;i<4;i++){
            latcent=latcent+places[i].latitude;
            loncent=loncent+places[i].longitude;
        }
        latcent=latcent/5;
        loncent=loncent/5;
        return new LatLng(latcent,loncent);
    }

    public void setSlideMenu(int id){
        if (mLayout != null) {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }
        mLayout.setAnchorPoint(0.5f);

        //llslidemenu.setVisibility(View.VISIBLE);
        tvnombre.setText(nombre[id]);
        tvdir.setText(direccion[id]);
        tvhorario.setText(horario[id]);
        ivfoto.setImageDrawable(getResources().getDrawable(foto[id]));
        CoordDestiny=ubicacion[id];
        String url = getDirectionsUrl(CoordOrigin, CoordDestiny);
        googlemap.clear();
        setMarkers();
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    public void resetSlideMenu()
    {
        if (mLayout != null) {
            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        }
        //llslidemenu.setVisibility(View.GONE);
        tvnombre.setText("");
        tvdir.setText("");
        tvhorario.setText("");
        ivfoto.setImageDrawable(null);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while down", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration="";
            int dist=0;

            //List<HashMap<String, String>> path1 = result.get(0);
            //HashMap<String,String> point1 = path1.get(3);
            //distance = (String)point1.get("distance");

            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    /*if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        //dist=dist+Double.parseDouble(distance);
                        continue;

                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        //Toast.makeText(MainActivity.this,duration,Toast.LENGTH_SHORT).show();
                        continue;
                    }*/

                    //        dist=dist+Integer.getInteger(distance);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);



                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.BLUE);
            }
            // Drawing polyline in the Google Map for the i-th route
            polylinefinal=googlemap.addPolyline(lineOptions);

            //final LatLng ll;
            //ll = getcurrentLocation();
            String url = getDirectionsUrl(CoordOrigin, CoordDestiny);
            //TxtViewCoord.setText(CoordOrigin.latitude + ", " + CoordOrigin.longitude);
            //String data="";
            DownloadTaskdata downloadTaskdata = new DownloadTaskdata();
            downloadTaskdata.execute(url);

        }
    }

    // Fetches data from url passed
    private class DownloadTaskdata extends AsyncTask<String, Void, String[]>{

        // Downloading data in non-ui thread
        @Override
        protected String[] doInBackground(String... url) {

            // For storing data from web service
            String datdist = "";
            String datdura = "";
            String[] ab = {"",""};

            jobj = jsonparser.JSONparserread(url[0]);
            //jobj=jsonparser.getJSONObject();
            // check your log for json response
            Log.d("Login attempt", jobj.toString() + url[0]);

            try {
                JSONArray routeArray = jobj.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);

                JSONArray newTempARr = routes.getJSONArray("legs");
                JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

                JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                JSONObject timeOb = newDisTimeOb.getJSONObject("duration");
                datdist=distOb.getString("text");
                datdura=timeOb.getString("text");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ab[0]=datdist;
            ab[1]=datdura;
            return ab;
        }
        protected void onPostExecute(String[] ab){
            tvdis.setText(ab[0]);
            tvdur.setText(ab[1]);
        }
    }
}
