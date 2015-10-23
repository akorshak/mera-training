package tng.fedorov.valcurs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ValAdapter extends ArrayAdapter<ValItem> {

    private Context mContext;
    private ArrayList<ValItem> mItems;

    public ValAdapter(Context context, int resource, ArrayList<ValItem> items) {
        super(context, resource, items);
        this.mContext = context;
        this.mItems = items;
    }

    static class ViewHolder {
        TextView charCode;
        TextView name;
        TextView value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.charCode = (TextView) convertView.findViewById(R.id.textViewCharCode);
            viewHolder.name = (TextView) convertView.findViewById(R.id.textViewName);
            viewHolder.value = (TextView) convertView.findViewById(R.id.textViewValue);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.charCode.setText(mItems.get(position).getCharCode());
        viewHolder.name.setText(mItems.get(position).getName());
        viewHolder.value.setText(mItems.get(position).getValue());

        Log.d("log", "ValAdapter  getView");

        return convertView;
    }
}
