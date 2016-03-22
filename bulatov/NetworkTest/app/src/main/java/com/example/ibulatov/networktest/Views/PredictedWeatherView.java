package com.example.ibulatov.networktest.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibulatov.networktest.R;
import com.example.ibulatov.networktest.WeatherCodeMapper;
import com.example.ibulatov.networktest.YahooWeatherItem.Forecast;

public class PredictedWeatherView extends FrameLayout {

    private TextView maxTemp;
    private TextView minTemp;
    private TextView condition;
    private TextView date;
    private ImageView weatherImage;

    public PredictedWeatherView(Context context) {
        super(context);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.predicted_weather_item_layout, this);

        maxTemp = (TextView) v.findViewById(R.id.high_temp);
        minTemp = (TextView) v.findViewById(R.id.low_temp);
        condition = (TextView) v.findViewById(R.id.condition_textView);
        date = (TextView) v.findViewById(R.id.day_textView);
        weatherImage = (ImageView) v.findViewById(R.id.weather_condition_icon);
    }

    public void setData(Forecast predictedForecast) {
        maxTemp.setText(this.getContext().getString(R.string.max_temp_str, predictedForecast.getMaxTemp()));
        minTemp.setText(this.getContext().getString(R.string.min_temp_str, predictedForecast.getMinTemp()));
        condition.setText(predictedForecast.getConditions());
        date.setText(this.getContext().getString(R.string.date_str, predictedForecast.getDay(), predictedForecast.getDate()));
        weatherImage.setBackgroundResource(WeatherCodeMapper.getWeatherImageResource(predictedForecast.getConditionsCode()));
    }
}