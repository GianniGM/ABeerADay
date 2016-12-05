package com.gmod.gianni.querbeer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmod.gianni.querbeer.model.Beer;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<Beer> {


    public static final String TAG = "BAD.MainActivity";
    private static final String apiKey = "4e575388b47e83690f10637f851fd9ff";
    private static final String format = "json";

    private BeerInterface instance;

    private ProgressBar progressLoading;
    private FloatingActionButton fab;

    private EditText inputText;
    private TextInputLayout inputLayoutText;

    private LinearLayout beerDetails;
    private TextView beerName;
    private TextView beerDescription;
    private TextView type;

    private ImageView beerLogo;


    private void bindViews() {
        progressLoading = (ProgressBar) findViewById(R.id.progress_loading);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        inputLayoutText = (TextInputLayout) findViewById(R.id.input_layout_text);
        inputText = (EditText) findViewById(R.id.input_text);
        beerDetails = (LinearLayout) findViewById(R.id.beer_details);
        beerName = (TextView) findViewById(R.id.beer_name);
        beerDescription = (TextView) findViewById(R.id.beer_description);
        beerLogo = (ImageView) findViewById(R.id.beer_logo);
        type = (TextView) findViewById(R.id.beer_type);
    }

    private boolean validateText() {
        if (inputText.getText().toString().trim().isEmpty()) {
            inputLayoutText.setError(getString(R.string.err_msg_text));
            requestFocus(inputText);
            return false;
        } else {
            inputLayoutText.setErrorEnabled(false);
        }

        return true;
    }

    private void randomBeer (){
        //fare richiesta http con l'url random e stampare tutto quello che ti da come schermata iniziale
        //mettere anche la barra di caricamento on qui.

        progressLoading.setVisibility(View.VISIBLE);
        beerDetails.setVisibility(View.GONE);

        String beerQuery = "random";
        instance.searchForBeer(beerQuery,apiKey, format).enqueue(this);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE);
        }
    }

    private BeerInterface buildBeerInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.brewerydb.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //reflection
        return retrofit.create(BeerInterface.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindViews();
        instance = buildBeerInstance();
        randomBeer();

        inputText.setVisibility(View.GONE);
        inputLayoutText.setVisibility(View.GONE);
        fab.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fab) {
//            if (!validateText()) {
//                return;
//            }

            Snackbar.make(v, "Searching beer, please wait!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            progressLoading.setVisibility(View.VISIBLE);
            beerDetails.setVisibility(View.GONE);

            randomBeer();

//           TODO gestire qui
//            String beerQuery = inputText.getText().toString();
//            instance.searchForBeer(beerQuery).enqueue(this);
        }

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
    public void onResponse(Call<Beer> call, Response<Beer> response) {
        Beer result = response.body();

        //Picasso

        String description = result.getData().getDescription();
        String typeName = result.getData().getStyle().getName();
        String abv = result.getData().getAbv();

        if(abv == null || abv.contentEquals("null")){
            abv = "";
        }else{
            abv =  " (" +  abv + "%)";
        }

        if(description == null || abv.contentEquals("null")){
            description = "";
        }

        if(typeName == null || typeName.contentEquals("null")){
            description = "";
        }

        beerLogo.setImageResource(R.drawable.ic_demo);
        progressLoading.setVisibility(View.GONE);
        beerDetails.setVisibility(View.VISIBLE);
        beerName.setText(result.getData().getName());
        beerDescription.setText(description);
        type.setText(typeName + abv);
        Log.d(TAG, result.getData().getName() + "-" +  result.getData().getName());

    }

    private void handleError(Throwable e) {
        Log.d(TAG, "Runtime error during Beer download", e);
        progressLoading.setVisibility(View.GONE);

        Toast.makeText(MainActivity.this, "Connection Refused!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<Beer> call, Throwable t) {
        handleError(t);
    }
}