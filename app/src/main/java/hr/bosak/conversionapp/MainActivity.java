package hr.bosak.conversionapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Spinner firstCurrency;
    Spinner secondCurrency;
    TextView result;
    Button submit;
    EditText sumET;

    List<Rates> rates;
    Double buyingRate = 0.0;
    Double sellingRate = 0.0;

    String currency1 ="";
    String  currency2= "";
    String[] currencycodeArray;
    int[] unitValueArray;

    String[] buyingRateArray;
    String[] sellingRateArray;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initWidgets();
        loadJson();
        initListeners();


    }

    private void initWidgets(){
        firstCurrency = findViewById(R.id.firstCurrencySP);
        secondCurrency = findViewById(R.id.secondCurrencySP);
        result = findViewById(R.id.resultTV);
        submit = findViewById(R.id.submitBTN);
        sumET = findViewById(R.id.sumET);

    }

    private void loadJson(){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Rates>> call = api.getRates();

        call.enqueue(new Callback<List<Rates>>() {
            @Override
            public void onResponse(Call<List<Rates>> call, Response<List<Rates>> response) {

                rates = response.body();

                currencycodeArray = new String[rates.size()];
                buyingRateArray = new String[rates.size()];
                sellingRateArray = new String[rates.size()];
                unitValueArray = new int[rates.size()];


                for(int i = 0; i < rates.size(); i++){
                    currencycodeArray[i] = rates.get(i).getCurrencyCode();
                    buyingRateArray[i] = rates.get(i).getBuyingRate();
                    sellingRateArray[i] = rates.get(i).getSellingRate();
                }

                // U slučaju da api servis šalje različiti unit value

                for(int i = 0; i < rates.size(); i++){
                    unitValueArray[i] = rates.get(i).getUnitValue();
                    if(unitValueArray[i] != 1){
                        Double newBuyingValue = Double.parseDouble(buyingRateArray[i])  /  Double.parseDouble(String.valueOf(unitValueArray[i]));
                        buyingRateArray[i] = String.valueOf(newBuyingValue);

                        Double newSellingValue = Double.parseDouble(sellingRateArray[i])  /  Double.parseDouble(String.valueOf(unitValueArray[i]));
                        sellingRateArray[i] = String.valueOf(newSellingValue);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, currencycodeArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                firstCurrency.setAdapter(adapter);
                secondCurrency.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<Rates>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void initListeners(){

       firstCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               buyingRate = Double.parseDouble(buyingRateArray[position]);
               currency1 = currencycodeArray[position];
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        secondCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sellingRate = Double.parseDouble(sellingRateArray[position]);
                currency2 = currencycodeArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String empty = "";
                if(String.valueOf(sumET.getText()).equals(empty)){
                    sumET.setText("1.0");
                }

                Double sum = Double.parseDouble(String.valueOf(sumET.getText()));

                if(!currency1.equals(currency2)) {
                    Double calculatedResult = (sum * buyingRate) / sellingRate;
                    String r = String.format("%.4f", calculatedResult);
                    String text = sum + " " + currency1 + " = " + r + " " + currency2;
                    result.setText(text);
                } else {
                    String error = "Choose different currency!";
                    result.setText(error);
                }




            }
        });
    }









}
