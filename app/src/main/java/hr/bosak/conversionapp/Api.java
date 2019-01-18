package hr.bosak.conversionapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "http://hnbex.eu/api/v1/";

    @GET("rates/daily")
    Call<List<Rates>> getRates();

}
