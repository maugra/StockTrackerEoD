package com.example.stocktrackereod;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stocktrackereod.databinding.ActivityMainBinding;
import com.example.stocktrackereod.portfolio.Portfolio;
import com.example.stocktrackereod.position.Position;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private final String BASE_URL = "https://api.polygon.io/v2/aggs/ticker/";
    private final String API_KEY = "2guV5i8O4ZqgNchftZ1WvhIbAqJMPRLf";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private final Portfolio portfolio = new Portfolio();
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updatePortfolio();
        setSupportActionBar(binding.toolbar);
        requestQueue = Volley.newRequestQueue(this);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        binding.addNewPosition.setOnClickListener(view -> {
            navController.navigate(R.id.addSymbolFragment);
        });
    }

    public void getPriceForPosition(Position position) {
        requestQueue.add(buildGetPriceRequest(position));
        requestQueue.start();
    }

    private JsonObjectRequest buildGetPriceRequest(Position position) {
        JSONObject jsonObject = new JSONObject();
        return new JsonObjectRequest(Request.Method.GET, constructURL(position.getSymbol()), jsonObject,
                response -> {
                    try {
                        double price = extractClosingPrice(response);
                        position.updateValueAndDifferential(price);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    System.out.println(error.networkResponse.statusCode);
                });
    }

    private double extractClosingPrice(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONArray("results").getJSONObject(0).getDouble("c");
    }

    private String constructURL(String symbol) {
        return BASE_URL + symbol + "/prev?adjusted=true&apiKey=" + API_KEY;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void updatePortfolio() {
        Set<Position> positions = this.portfolio.getPositions();
        positions.forEach(this::getPriceForPosition);
        this.portfolio.updateValueAndDifferential();
    }
}