package com.jyong.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.jyong.coolweather.db.CoolWeatherDB;
import com.jyong.coolweather.model.City;
import com.jyong.coolweather.model.County;
import com.jyong.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 加勇 on 2015/9/7.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param db
     * @return
     */
    public synchronized static boolean handleProvinceResponse(
            CoolWeatherDB db, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvince = response.split(",");
            if (allProvince != null && allProvince.length > 0) {
                for (String province : allProvince) {
                    String[] array = province.split("\\|");
                    Province pro = new Province();
                    pro.setProvinceCode(array[0]);
                    pro.setProvinceName(array[1]);
                    db.saveProvince(pro);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param db
     * @param response
     * @param provinceID
     * @return
     */
    public static boolean handleCityResponse(CoolWeatherDB db,
                                             String response, int provinceID) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCity = response.split(",");
            if (allCity != null && allCity.length > 0) {
                for (String c : allCity) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceID(provinceID);
                    db.saveCity(city);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 获取和处理县级数据
     *
     * @param db
     * @param response
     * @param cityID
     * @return
     */
    public static boolean handleCountyResponse(CoolWeatherDB db,
                                               String response, int cityID) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounty = response.split(",");
            if (allCounty != null && allCounty.length > 0) {
                for (String c : allCounty) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityID(cityID);
                    db.saveCounty(county);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析服务返回的JSON数据，并将解析出来的数据存储到本地
     *
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject object = new JSONObject(response);
            JSONObject weatherInfo = object.getJSONObject("weatherinfo");
            saveWeatherInfo(context, weatherInfo.getString("city"), weatherInfo.getString("cityid"),
                    weatherInfo.getString("temp1"), weatherInfo.getString("temp2"), weatherInfo.getString("weather"),
                    weatherInfo.getString("ptime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将获取的天气信息存储到SharedPreference文件
     *
     * @param context
     * @param city
     * @param cityid
     * @param temp1
     * @param temp2
     * @param weather
     * @param ptime
     */
    private static void saveWeatherInfo(Context context, String city, String cityid, String temp1,
                                        String temp2, String weather, String ptime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferences.putBoolean("city_selected", true);
        preferences.putString("city_name", city);
        preferences.putString("weather_code", cityid);
        preferences.putString("temp1", temp1);
        preferences.putString("temp2", temp2);
        preferences.putString("weather_desp",weather);
        preferences.putString("publish_time",ptime);
        preferences.putString("current_date",sdf.format(new Date()));
        preferences.commit();
    }

}
