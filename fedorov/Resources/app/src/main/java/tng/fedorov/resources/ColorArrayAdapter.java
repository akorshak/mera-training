package tng.fedorov.resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fedorov on 01.10.2015.
 */
public class ColorArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public ColorArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);

        String s = values[position];
        switch (s) {
            case "red":
                textView.setTextColor(context.getResources().getColor(R.color.red));
                imageView.setImageResource(R.drawable.shape_oval_red);
                break;
            case "orange":
                textView.setTextColor(context.getResources().getColor(R.color.orange));
                imageView.setImageResource(R.drawable.shape_oval_orange);
                break;
            case "yellow":
                textView.setTextColor(context.getResources().getColor(R.color.yellow));
                imageView.setImageResource(R.drawable.shape_oval_yellow);
                break;
            case "green":
                textView.setTextColor(context.getResources().getColor(R.color.green));
                imageView.setImageResource(R.drawable.shape_oval_green);
                break;
            case "cyan":
                textView.setTextColor(context.getResources().getColor(R.color.cyan));
                imageView.setImageResource(R.drawable.shape_oval_cyan);
                break;
            case "blue":
                textView.setTextColor(context.getResources().getColor(R.color.blue));
                imageView.setImageResource(R.drawable.shape_oval_blue);
                break;
            case "violet":
                textView.setTextColor(context.getResources().getColor(R.color.violet));
                imageView.setImageResource(R.drawable.shape_oval_violet);
                break;
            default:
                textView.setTextColor(context.getResources().getColor(android.R.color.black));
                imageView.setImageResource(android.R.drawable.ic_dialog_alert);
        }

        return rowView;
    }
}
