package com.example.stocktrackereod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stocktrackereod.databinding.ActivityMainBinding;
import com.example.stocktrackereod.portfolio.Portfolio;
import com.example.stocktrackereod.position.Position;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String BASE_URL = "https://api.polygon.io/v2/aggs/ticker/";
    private final String API_KEY = "2guV5i8O4ZqgNchftZ1WvhIbAqJMPRLf";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private  Portfolio portfolio = new Portfolio();
    RequestQueue requestQueue;
    private PositionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RecyclerView positionsList = findViewById(R.id.positions_list);
        if (getPortfolioFromSharedPreferences(this.getBaseContext()) != null) {
            portfolio = getPortfolioFromSharedPreferences(this.getBaseContext());
        }
        //TODO: Remove hardcoded entries
        Position position = new Position();
        position.setSymbol("AAPL");
        position.setAmount(10);
        position.setDifferential(0);
        position.setPreviousValue(1500);
        position.setCurrentValue(1600);
        Position position1 = new Position();
        position1.setSymbol("ONON");
        position1.setAmount(10);
        position1.setDifferential(0);
        position1.setPreviousValue(1500);
        position1.setCurrentValue(1600);
        portfolio.getPositions().add(position1);
        portfolio.getPositions().add(position);

        adapter = new PositionsAdapter(portfolio.getPositions());
        adapter = new PositionsAdapter(portfolio.getPositions());
        positionsList.setAdapter(adapter);
        positionsList.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(binding.toolbar);
        requestQueue = Volley.newRequestQueue(this);
        updatePortfolio();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        binding.addNewPosition.setOnClickListener(view -> {
            navController.navigate(R.id.addSymbolFragment);
        });
        vibrate();
    }

    @Override
    public void onStop() {
        super.onStop();
        savePortfolioToSharedPreferences(this.getBaseContext(), portfolio);
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
                    System.out.println("That didn't work");
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
        List<Position> positions = this.portfolio.getPositions();
        positions.forEach(this::getPriceForPosition);
        this.portfolio.updateValueAndDifferential();
        dataSetChanged();
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void dataSetChanged() {
        this.adapter.notifyDataSetChanged();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(vibrationEffect);
    }

    public void savePortfolioToSharedPreferences(Context context, Portfolio portfolio) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonPortfolio = gson.toJson(portfolio);
        editor.putString("portfolio", jsonPortfolio);
        editor.apply();
    }

    public Portfolio getPortfolioFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("myObject", null);

        Gson gson = new Gson();
        return gson.fromJson(json, Portfolio.class);
    }


}
