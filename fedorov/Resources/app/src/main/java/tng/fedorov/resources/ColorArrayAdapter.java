package tng.fedorov.resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by fedorov on 01.10.2015.
 */
public class ColorArrayAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final String[] mValues;

    public ColorArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mValues = objects;
    }

    static class ViewHolder {
        TextView text;
        DrawView icon;
    }

    public static enum ITEM_TYPE {
        IN,
        OUT
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE.values().length;
    }

    @Override
    public int getItemViewType(int position) {

        if (position % 2 == 0) {
            return ITEM_TYPE.IN.ordinal();
        } else {
            return ITEM_TYPE.OUT.ordinal();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ITEM_TYPE viewType = ITEM_TYPE.values()[getItemViewType(position)];

        ViewHolder viewHolder;
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            switch (viewType) {
                case IN:
                    convertView = inflater.inflate(R.layout.rowlayout1, viewGroup, false);
                    break;
                case OUT:
                    convertView = inflater.inflate(R.layout.rowlayout2, viewGroup, false);
                    break;
            }

            viewHolder = new ViewHolder();
            viewHolder.icon = (DrawView) convertView.findViewById(R.id.icon);
            viewHolder.text = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.text.setText(mValues[position]);

        String s = mValues[position];
        switch (s) {
            case "red":
                viewHolder.text.setTextColor(mContext.getResources().getColor(R.color.red));
                viewHolder.icon.mPaint.setColor(mContext.getResources().getColor(R.color.red));
                break;
            case "orange":
                viewHolder.text.setTextColor(mContext.getResources().getColor(R.color.orange));
                viewHolder.icon.mPaint.setColor(mContext.getResources().getColor(R.color.orange));
                break;
            case "yellow":
                viewHolder.text.setTextColor(mContext.getResources().getColor(R.color.yellow));
                viewHolder.icon.mPaint.setColor(mContext.getResources().getColor(R.color.yellow));
                break;
            case "green":
                viewHolder.text.setTextColor(mContext.getResources().getColor(R.color.green));
                viewHolder.icon.mPaint.setColor(mContext.getResources().getColor(R.color.green));
                break;
            case "cyan":
                viewHolder.text.setTextColor(mContext.getResources().getColor(R.color.cyan));
                viewHolder.icon.mPaint.setColor(mContext.getResources().getColor(R.color.cyan));
                break;
            case "blue":
                viewHolder.text.setTextColor(mContext.getResources().getColor(R.color.blue));
                viewHolder.icon.mPaint.setColor(mContext.getResources().getColor(R.color.blue));
                break;
            case "violet":
                viewHolder.text.setTextColor(mContext.getResources().getColor(R.color.violet));
                viewHolder.icon.mPaint.setColor(mContext.getResources().getColor(R.color.violet));
                break;
            default:
                viewHolder.text.setTextColor(mContext.getResources().getColor(android.R.color.black));
                viewHolder.icon.mPaint.setColor(mContext.getResources().getColor(android.R.color.black));
                break;
        }

        return convertView;
    }
}
