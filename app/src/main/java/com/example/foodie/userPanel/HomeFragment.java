package com.example.foodie.userPanel;


import static android.content.Context.LOCATION_SERVICE;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foodie.R;
import com.example.foodie.UserPanelBottomNavigation;
import com.example.foodie.restaurantDetailActivity.RestaurantDetail;
import com.example.foodie.model.Restaurant;
import com.example.foodie.userPanel.adapters.HomeAdapter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LocationListener {
    RecyclerView recyclerView;
    private List<Restaurant> restaurantList;
    private HomeAdapter adapter;
    DatabaseReference data,databaseReference;
    EditText txt_search;
    String city = "seattle";
    String input;
    TextView showLocation;
    Button showLocationBtn;
    LocationManager locationManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, null);
        getActivity().setTitle("Home");

        Places.initialize(v.getContext(),"AIzaSyAFsef3s1xrJwy3g7EnS_e6AtWokBhN1gs");

        recyclerView = v.findViewById(R.id.recycle_restaurants);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.move);
        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showLocation = v.findViewById(R.id.show_location_textview);
        showLocationBtn = v.findViewById(R.id.get_location_btn);

        txt_search = v.findViewById(R.id.text_search_text);

        if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        showLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = input;
                getRestaurantsFromYelpApi(city);
            }
        });

        txt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                input = s.toString();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

//        txt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                callSearch(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
////                callSearch(newText);
////              }
//                return true;
//            }
//
//            public void callSearch(String query) {
//                List<Restaurant> newRestaurantList = new ArrayList<>();
//                for (Restaurant restaurant : restaurantList) {
//                    if (restaurant.getZipcode() == query) {
//                        newRestaurantList.add(restaurant);
//                    }
//                }
//                adapter = new HomeAdapter(getContext(), newRestaurantList);
//                recyclerView.setAdapter(adapter);
//            }
//
//        });


//        restaurantSearchInputAddress = getParentFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        restaurantSearchInputAddress.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
//        ((EditText)restaurantSearchInputAddress.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Enter an address");
//        ((EditText)restaurantSearchInputAddress.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(14);

//        restaurantSearch.setOnQueryTextListener(this);
        getRestaurantsFromYelpApi(city);
        return v;

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this.getContext(), ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

            showLocation.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        getRestaurantsFromYelpApi(city);
        restaurantCards();
        restaurantList = new ArrayList<>();
        adapter = new HomeAdapter(getContext(), restaurantList);
        recyclerView.setAdapter(adapter);

    }

    public void getRestaurantsFromYelpApi(String city){
        String yelpUrl = "https://api.yelp.com/v3/businesses/search?term=restaurants&location=" + city;
        PingWebServiceTask task = new PingWebServiceTask();
        task.execute(yelpUrl);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


    private class PingWebServiceTask  extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jObject = new JSONObject();
            try {

                URL url = new URL(params[0]);
                // Get String response from the url address
                String resp = getHttpResponse(url);
                jObject = new JSONObject(resp);

                return jObject;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jObject;
        }

        public String getHttpResponse(URL url) throws IOException {
            String accessToken = "ZbToYuLu6sT_E3_Y_6u3j_KX8WknrZHJN4Yn5WgkC_BM3gXVUlz3SUnlSQumA2pUhD5k-3UssyMoBkenjU1na7JQJvAdN9f7cJJMXFzFVtfu2KDsESzpvLmoVUKxYXYx";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("authorization", "Bearer " + accessToken);
            conn.setDoInput(true);

            conn.connect();
            // Read response.
            InputStream inputStream = conn.getInputStream();
            return convertStreamToString(inputStream);
        }

        public String convertStreamToString(InputStream inputStream) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String len;
                while ((len = bufferedReader.readLine()) != null) {
                    stringBuilder.append(len);
                }
                bufferedReader.close();
                return stringBuilder.toString().replace(",", ",\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(JSONObject jObject) {
            super.onPostExecute(jObject);
            restaurantList = new ArrayList<>();
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(jObject.get("businesses").toString());
                for (int i=0; i < jArray.length(); i++) {
                    JSONObject singleObject = jArray.getJSONObject(i);
                    Restaurant restaurant = new Restaurant();
                    restaurant.setRestaurantName(singleObject.get("name").toString());
                    JSONObject jObject2 = (JSONObject) singleObject.get("location");
                    restaurant.setAddress(jObject2.get("address1").toString());
                    restaurant.setRestaurantId(singleObject.get("rating").toString());
                    restaurant.setPhone(singleObject.get("display_phone").toString());
                    restaurant.setImageURL(singleObject.get("image_url").toString());
                    restaurant.setZipcode(jObject2.get("zip_code").toString());
                    restaurantList.add(restaurant);
               }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new HomeAdapter(getContext(), restaurantList);
            ItemClickListener itemClickListener = new ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    //attributions bond to the item has been changed
                    //LinkCard currentItem = linkList.get(position);
                    Restaurant restaurant = restaurantList.get(position);
                    Intent intent = new Intent(getContext(), RestaurantDetail.class);
                    intent.putExtra("restaurant", restaurant);
                    startActivity(intent);
                }
            };
            adapter.setOnItemClickListener(itemClickListener);
            recyclerView.setAdapter(adapter);
        }
    }
    public void restaurantCards() {}
}
