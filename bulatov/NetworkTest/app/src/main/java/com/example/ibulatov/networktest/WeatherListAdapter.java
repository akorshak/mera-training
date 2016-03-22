package com.example.ibulatov.networktest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ibulatov.networktest.Views.CurrentWeatherView;
import com.example.ibulatov.networktest.Views.PredictedWeatherView;
import com.example.ibulatov.networktest.YahooWeatherItem.*;

import java.util.List;

public class WeatherListAdapter extends BaseAdapter {

    private static final int TYPE_COUNT = 2;

    private static final int CURRENT_TYPE = 0;
    private static final int PREDICTION_TYPE = 1;

    private Context context;

    private Condition condition;
    private List<Forecast> forecastList;

    public WeatherListAdapter(Context context, YahooWeatherItem weatherItem) {
        this.context = context;
        this.condition = weatherItem.getCondition();
        this.forecastList = weatherItem.getForecastList();
    }

    @Override
    public int getCount() {
        return forecastList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? CURRENT_TYPE : PREDICTION_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        if(type == CURRENT_TYPE) {

            if(convertView == null) {
                convertView = new CurrentWeatherView(context);
            }
            ((CurrentWeatherView) convertView).setData(condition, forecastList.get(0));

        } else if(type == PREDICTION_TYPE) {

            if(convertView == null) {
                convertView = new PredictedWeatherView(context);
            }
            ((PredictedWeatherView) convertView).setData(forecastList.get(position));

        }

        return convertView;
    }
}
