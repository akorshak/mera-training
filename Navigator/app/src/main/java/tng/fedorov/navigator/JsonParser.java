package tng.fedorov.navigator;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public ArrayList<LatLng> getPointsList(String json) {
        Log.d("mylog", "getPointsList");
        ArrayList<LatLng> resultList = new ArrayList<>();
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            JSONArray steps = new JSONObject(json).getJSONArray("routes")
                    .getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONArray("steps");

            JSONObject jsonObjectPolyline;
            String encodedPolylinePoints;
            List<LatLng> decodedPolylinePoints;

            for (int i = 0; i < steps.length(); i++) {
                jsonObjectPolyline = steps.getJSONObject(i).getJSONObject("polyline");
                encodedPolylinePoints = jsonObjectPolyline.getString("points");
                decodedPolylinePoints = decodePolyLine(encodedPolylinePoints);
                resultList.addAll(decodedPolylinePoints);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * Uses method from project https://github.com/jd-alexander/Google-Directions-Android
     *
     * Decode a polyline string into a list of GeoPoints.
     */
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }


    /**
     * 1st variant. It draw straight lines instead road relief.
     */
    /*public ArrayList<LatLng> getPointsList(String json) {
        ArrayList<LatLng> resultList = new ArrayList<>();
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            JSONArray steps = new JSONObject(json).getJSONArray("routes")
                    .getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONArray("steps");

            JSONObject jsonObject = steps.getJSONObject(0).getJSONObject("start_location");
            Double lat = Double.parseDouble(jsonObject.getString("lat"));
            Double lng = Double.parseDouble(jsonObject.getString("lng"));
            resultList.add(new LatLng(lat, lng));

            for (int i = 0; i < steps.length(); i++) {
                jsonObject = steps.getJSONObject(i).getJSONObject("end_location");
                lat = Double.parseDouble(jsonObject.getString("lat"));
                lng = Double.parseDouble(jsonObject.getString("lng"));
                resultList.add(new LatLng(lat, lng));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }*/
}

