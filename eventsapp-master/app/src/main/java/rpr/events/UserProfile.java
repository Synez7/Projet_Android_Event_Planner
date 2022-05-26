package rpr.events;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.location.LocationListener;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

// Fragment pour la visualisation des infos de profil utilisateur
public class UserProfile extends Fragment implements LocationListener{


    UserSessionManager session;
    private Context context = null;
    //private String name;
    private String username;
    private String usertype_id;
    private String user_id;
    private String usertype;
    private String email;
    private TextView user_name;
    private TextView user_email;
    private TextView user_type;
    private Button changepass;
    private TextView position;
    private Button findPosition;
    FusedLocationProviderClient client;
    LocationManager locationManager;
    LocationListener listener;
    Location loc;
    private boolean canGetLocation;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Profile");
        context = getActivity();
        session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        username = user.get(UserSessionManager.KEY_USERNAME);
        email = user.get(UserSessionManager.KEY_EMAIL);
        user_id = user.get(UserSessionManager.KEY_USER_ID);
        usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);
        usertype = user.get(UserSessionManager.KEY_USERTYPE);
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_name = (TextView) getView().findViewById(R.id.user_name);
        user_email = (TextView) getView().findViewById(R.id.user_email);
        user_type = (TextView) getView().findViewById(R.id.user_type);
        changepass = (Button) getView().findViewById(R.id.bchangepass);

        position = (TextView) getView().findViewById(R.id.textViewAddress);
        findPosition = (Button) getView().findViewById(R.id.bouton_position);

        user_name.setText(username);
        user_email.setText(email);
        user_type.setText(usertype);

        client = LocationServices.getFusedLocationProviderClient(getContext());


        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), changePassword.class);
                startActivity(intent);


            }
        });

        // Action pour récupérer sa position géographique
        findPosition.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.S)
                    @Override
                    public void onClick(View view) {
                        // check condition
                        if (ContextCompat.checkSelfPermission(
                                getActivity(),
                                Manifest.permission
                                        .ACCESS_FINE_LOCATION)
                                == PackageManager
                                .PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(
                                getActivity(),
                                Manifest.permission
                                        .ACCESS_COARSE_LOCATION)
                                == PackageManager
                                .PERMISSION_GRANTED) {
                            // When permission is granted
                            // Call method
                            getCurrentLocation();
                        } else {
                            // When permission is not granted
                            // Call method
                            requestPermissions(
                                    new String[]{
                                            Manifest.permission
                                                    .ACCESS_FINE_LOCATION,
                                            Manifest.permission
                                                    .ACCESS_COARSE_LOCATION},
                                    100);
                        }
                    }
                });

    }



    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint({"MissingPermission", "NewApi"})
    private void getCurrentLocation() {

        LocationManager locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        boolean checkGPS = true;
        boolean checkNetwork = true;
        // getting GPS status
        checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!checkGPS && !checkNetwork) {
            Toast.makeText(getContext(), "No Service Provider Available", Toast.LENGTH_SHORT).show();
        } else {
            this.canGetLocation = true;
            // First get location from Network Provider
            if (checkNetwork) {
                //Toast.makeText(getContext(), "Network", Toast.LENGTH_SHORT).show();

                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this::onLocationChanged);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }

                    if (loc != null) {

                        double latitude = loc.getLatitude();
                        double longitude = loc.getLongitude();
                        onLocationChanged(loc);

                    }
                } catch (SecurityException e) {

                }
            }
        }
        // if GPS Enabled get lat/long using GPS Services
        if (checkGPS) {
            //Toast.makeText(getContext(), "GPS", Toast.LENGTH_SHORT).show();
            if (loc == null) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this::onLocationChanged);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null) {
                            double latitude = loc.getLatitude();
                            double longitude = loc.getLongitude();
                            onLocationChanged(loc);
                            position.setText(String.valueOf(latitude));
                        }
                    }
                } catch (SecurityException e) {

                }
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder = new Geocoder(getContext(), Locale.FRANCE);


        // Possibilité de géolocaliser l'appareil ou non
        if (location != null) {

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                // Affichage des coordonées pour la latitude et la longitude ainsi que l'adresse
                position.setText(String.valueOf(addresses.get(0).getAddressLine(0)));
            }
            catch (IOException e) {
                e.printStackTrace();
            }



        }
        else{
            Toast.makeText(getContext(), "Impossible to find your position !", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println("DEBUG 4");
        Toast.makeText(getContext(),"onStatusChanged",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
