package org.openweathermap.api;

import com.google.gson.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openweathermap.api.gson.WindDirectionDeserializer;
import org.openweathermap.api.gson.WindDirectionSerializer;
import org.openweathermap.api.model.WindDirection;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.model.forecast.Forecast;
import org.openweathermap.api.model.forecast.ForecastInformation;
import org.openweathermap.api.model.forecast.daily.DailyForecast;
import org.openweathermap.api.model.forecast.hourly.HourlyForecast;
import org.openweathermap.api.query.Query;
import org.openweathermap.api.query.ResponseFormat;
import org.openweathermap.api.query.currentweather.CurrentWeatherMultipleLocationsQuery;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;
import org.openweathermap.api.query.forecast.ForecastQuery;
import org.openweathermap.api.query.forecast.daily.DailyForecastQuery;
import org.openweathermap.api.query.forecast.hourly.HourlyForecastQuery;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class AbstractDataWeatherClient implements DataWeatherClient {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(WindDirection.class, new WindDirectionDeserializer())
            .registerTypeAdapter(WindDirection.class, new WindDirectionSerializer())
            .create();
    private final JsonParser jsonParser = new JsonParser();

    @Override
    public ImmutablePair<ForecastInformation<HourlyForecast>, RequestResponse> getForecastInformation(HourlyForecastQuery query) {
        return toForecastInformation(query, HourlyForecast.TYPE);
    }

    @Override
    public ImmutablePair<ForecastInformation<DailyForecast>, RequestResponse> getForecastInformation(DailyForecastQuery query) {
        return toForecastInformation(query, DailyForecast.TYPE);
    }

    @Override
    public String getWeatherData(Query query) {
        return makeRequest(query);
    }


    @Override
    public ImmutablePair<CurrentWeather, RequestResponse> getCurrentWeather(CurrentWeatherOneLocationQuery query) {
        return toCurrentWeather(query);
    }

    @Override
    public ImmutablePair<List<CurrentWeather>, RequestResponse> getCurrentWeather(CurrentWeatherMultipleLocationsQuery query) {
        return toCurrentWeather(query);
    }

    protected abstract String makeRequest(Query query);

    private ImmutablePair<CurrentWeather, RequestResponse> toCurrentWeather(CurrentWeatherOneLocationQuery query) {
        String data = getWeatherData(query);
        ResponseFormat responseFormat = query.getResponseFormat();
        if (responseFormat == null || responseFormat == ResponseFormat.JSON) {
            CurrentWeather currentWeather = gson.fromJson(data, CurrentWeather.TYPE);

            RequestResponse requestResponse = constructRequestResponse(query, data);

            ImmutablePair<CurrentWeather, RequestResponse> currentWeatherRequestResponsePair =
                    new ImmutablePair<CurrentWeather, RequestResponse>(currentWeather, requestResponse);
            return currentWeatherRequestResponsePair;
        }
        return null;
    }

    private ImmutablePair<List<CurrentWeather>, RequestResponse> toCurrentWeather(CurrentWeatherMultipleLocationsQuery query) {
        String data = getWeatherData(query);
        JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
        JsonArray list = jsonObject.getAsJsonArray("list");
        List<CurrentWeather> weatherInfoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CurrentWeather weatherInfo = gson.fromJson(list.get(i), CurrentWeather.TYPE);
            weatherInfoList.add(weatherInfo);
        }

        RequestResponse requestResponse = constructRequestResponse(query, data);

        ImmutablePair<List<CurrentWeather>, RequestResponse> currentWeathersRequestResponsePair =
                new ImmutablePair<List<CurrentWeather>, RequestResponse>(weatherInfoList, requestResponse);

        return currentWeathersRequestResponsePair;
    }

    private <T extends Forecast> ImmutablePair<ForecastInformation<T>, RequestResponse> toForecastInformation(ForecastQuery query, Type type) {
        String data = getWeatherData(query);
        ResponseFormat responseFormat = query.getResponseFormat();
        if (responseFormat == null || responseFormat == ResponseFormat.JSON) {
            ForecastInformation<T> forecast = gson.fromJson(data, type);


            RequestResponse requestResponse = constructRequestResponse(query, data);

            ImmutablePair<ForecastInformation<T>, RequestResponse> forecastRequestResponsePair =
                    new ImmutablePair<ForecastInformation<T>, RequestResponse>(forecast, requestResponse);

            return forecastRequestResponsePair;
        }
        return null;
    }

    private RequestResponse constructRequestResponse(Query query, String data) {
        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setQuery(query.toStringRepresentation("API_KEY"));
        requestResponse.setResponse(data);
        requestResponse.setTime(new Date());

        return requestResponse;
    }
}
