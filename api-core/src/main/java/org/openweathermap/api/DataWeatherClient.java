package org.openweathermap.api;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.model.forecast.ForecastInformation;
import org.openweathermap.api.model.forecast.daily.DailyForecast;
import org.openweathermap.api.model.forecast.hourly.HourlyForecast;
import org.openweathermap.api.query.currentweather.CurrentWeatherMultipleLocationsQuery;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;
import org.openweathermap.api.query.forecast.daily.DailyForecastQuery;
import org.openweathermap.api.query.forecast.hourly.HourlyForecastQuery;

import java.util.List;

public interface DataWeatherClient extends WeatherClient {

    ImmutablePair<CurrentWeather, RequestResponse> getCurrentWeather(CurrentWeatherOneLocationQuery query);

    ImmutablePair<List<CurrentWeather>, RequestResponse> getCurrentWeather(CurrentWeatherMultipleLocationsQuery query);

    ImmutablePair<ForecastInformation<HourlyForecast>, RequestResponse> getForecastInformation(HourlyForecastQuery query);

    ImmutablePair<ForecastInformation<DailyForecast>, RequestResponse> getForecastInformation(DailyForecastQuery query);
}
