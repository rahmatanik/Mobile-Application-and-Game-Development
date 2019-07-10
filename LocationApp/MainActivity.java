package com.example.lbsdemo;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{
    LocationClient mLocationClient;
    private TextView addressLabel;
    private TextView locationLabel;
    private Button getLocationBtn;
    private Button disconnectBtn;
    private Button connectBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationLabel = (TextView) findViewById(R.id.locationLabel);
        addressLabel = (TextView) findViewById(R.id.addressLabel);
        getLocationBtn = (Button) findViewById(R.id.getLocation);
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayCurrentLocation();
            }
        });
        disconnectBtn = (Button) findViewById(R.id.disconnect);
        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mLocationClient.disconnect();
                locationLabel.setText("Got disconnected....");
            }
        });
        connectBtn = (Button) findViewById(R.id.connect);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mLocationClient.connect();
                locationLabel.setText("Got connected....");
            }
        });
        mLocationClient = new LocationClient(this, this, this);
    }
    @Override
    protected void onStart() {
        super.onStart();

        mLocationClient.connect();
        locationLabel.setText("Got connected....");
    }
    @Override
    protected void onStop() {

        mLocationClient.disconnect();
        super.onStop();
        locationLabel.setText("Got disconnected....");
    }
    @Override
    public void onConnected(Bundle dataBundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this, "Connection Failure : " +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
    public void displayCurrentLocation() {

        Location currentLocation = mLocationClient.getLastLocation();
        String msg = "Current Location: " +
                Double.toString(currentLocation.getLatitude()) + "," +
                Double.toString(currentLocation.getLongitude());

        locationLabel.setText(msg);

        (new GetAddressTask(this)).execute(currentLocation);
    }
    private class GetAddressTask<addresses> extends AsyncTask<Location, Void, String>{
        Context mContext;
        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }
        @Override
        protected void onPostExecute(String address) {
// Display the current address in the UI
            addressLabel.setText(address);
        }
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());

            Location loc = params[0];

            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {

    }

            String errorString = "Illegal arguments " +
                    Double.toString(loc.getLatitude()) +
                    " , " +
                    Double.toString(loc.getLongitude()) +
                    " passed to address service";
            Log.e("LocationSampleActivity", errorString);
            Throwable e2 = null;
            e2.printStackTrace();
            return errorString;
        }

if (addresses!= null && addresses.size() > 0) {

            Address address = addresses.get(0);

            String addressText = String.format(
                    "%s, %s, %s",

                    address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(0) : "",

                    address.getLocality(),

                    address.getCountryName());

            String addressText1 = addressText;
            return addressText1;
        } else {
            return "No address found";
        }
    }
}
