package tng.fedorov.valcurs;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class XmlParser {

    private static final String ITEM_TAG = "Valute";

    public ArrayList<ValItem> parseData(String data) throws XmlPullParserException, IOException {
        ArrayList<ValItem> items = new ArrayList<>();
        ValItem item = null;

        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserFactory.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new StringReader(data));

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String tagName = parser.getName();
                if (ITEM_TAG.equals(tagName)) {
                    item = new ValItem();
                } else if (item != null) {
                    switch (tagName) {
                        case "CharCode":
                            parser.next();
                            item.setCharCode(parser.getText());
                            break;
                        case "Name":
                            parser.next();
                            item.setName(parser.getText());
                            break;
                        case "Value":
                            parser.next();
                            item.setValue(parser.getText());
                            break;
                        default:
                            break;
                    }
                }
            }
            if (parser.getEventType() == XmlPullParser.END_TAG && ITEM_TAG.equals(parser.getName())
                    && item != null) {
                items.add(item);
            }
            parser.next();
        }

        Log.d("log", "parseData");

        return items;
    }
}
