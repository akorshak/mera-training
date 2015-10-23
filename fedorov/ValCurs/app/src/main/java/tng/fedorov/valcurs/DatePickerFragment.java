package tng.fedorov.valcurs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    int mNowYear;
    int mNowMonth;
    int mNowDay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        mNowYear = c.get(Calendar.YEAR);
        mNowMonth = c.get(Calendar.MONTH);
        mNowDay = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, mNowYear, mNowMonth, mNowDay);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar newDate = Calendar.getInstance();
        if (year <= mNowYear && month <= mNowMonth && day <= mNowDay) {
            newDate.set(year, month, day);
        }
        String stringDate = new SimpleDateFormat("dd.MM.yyyy").format(newDate.getTime());

        MainFragment f = (MainFragment) getFragmentManager().findFragmentById(R.id.activityMain);
        TextView textView = (TextView) f.getView().getRootView().findViewById(R.id.textViewDate);
        textView.setText(stringDate);
    }
}
