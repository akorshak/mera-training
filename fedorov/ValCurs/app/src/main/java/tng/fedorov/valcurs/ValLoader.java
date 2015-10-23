package tng.fedorov.valcurs;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class ValLoader extends AsyncTaskLoader<ArrayList<ValItem>>{

    Bundle mBundle;

    public ValLoader(Context context, Bundle args) {
        super(context);
        mBundle = args;
    }

    @Override
    public ArrayList<ValItem> loadInBackground() {
        String data;
        ArrayList<ValItem> items = null;
        try {
            data = new DataDownloader().downloadData(mBundle.getString("url"));
            items = new XmlParser().parseData(data);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("log", "loadInBackground");
        return items;
    }

    @Override
    public void deliverResult(ArrayList<ValItem> data) {
        super.deliverResult(data);

        Log.d("log", "deliverResult");
    }
}