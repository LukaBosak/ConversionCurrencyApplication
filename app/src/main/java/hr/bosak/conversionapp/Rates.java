package hr.bosak.conversionapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rates {


    @SerializedName("median_rate")
    @Expose
    private String medianRate;

    @SerializedName("unit_value")
    @Expose
    private Integer unitValue;

    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    @SerializedName("selling_rate")
    @Expose
    private String sellingRate;

    @SerializedName("buying_rate")
    @Expose
    private String buyingRate;


    public String getMedianRate() {
        return medianRate;
    }

    public Integer getUnitValue() {
        return unitValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getSellingRate() {
        return sellingRate;
    }

    public String getBuyingRate() {
        return buyingRate;
    }
}
