package com.example.ibulatov.networktest.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibulatov.networktest.R;
import com.example.ibulatov.networktest.WeatherCodeMapper;
import com.example.ibulatov.networktest.YahooWeatherItem.*;

public class CurrentWeatherView extends FrameLayout {

    private TextView currTemp;
    private TextView maxTempTextView;
    private TextView minTempTextView;
    private TextView conditionTextView;
    private TextView dateTextView;
    private ImageView weatherImage;

    public CurrentWeatherView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.current_weather_item_layout, this);

        maxTempTextView = (TextView) v.findViewById(R.id.curr_temp_max);
        minTempTextView = (TextView) v.findViewById(R.id.curr_temp_min);
        conditionTextView = (TextView) v.findViewById(R.id.curr_condition_textView);
        dateTextView = (TextView) v.findViewById(R.id.curr_day_textView);
        currTemp = (TextView) v.findViewById(R.id.curr_temp);
        weatherImage = (ImageView) v.findViewById(R.id.curr_weather_condition_icon);

    }

    public void setData(Condition condition, Forecast predictedForecast) {
        maxTempTextView.setText(this.getContext().getString(R.string.max_temp_str, predictedForecast.getMaxTemp()));
        minTempTextView.setText(this.getContext().getString(R.string.min_temp_str, predictedForecast.getMinTemp()));
        conditionTextView.setText(condition.getCurrConditions());
        dateTextView.setText(this.getContext().getString(R.string.date_str, predictedForecast.getDay(),predictedForecast.getDate()));
        currTemp.setText(String.valueOf(condition.getCurrTemp()));
        weatherImage.setBackgroundResource(WeatherCodeMapper.getWeatherImageResource(condition.getCurrConditionsCode()));
    }
}
