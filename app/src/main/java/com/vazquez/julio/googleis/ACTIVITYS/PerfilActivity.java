package com.vazquez.julio.googleis.ACTIVITYS;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vazquez.julio.googleis.HTTPMANAGER.global;
import com.vazquez.julio.googleis.R;

import java.util.Calendar;

public class PerfilActivity extends AppCompatActivity {

    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView mDateDisplay;
    private Button mPickDate;
    static final int DATE_DIALOG_ID = 0;
    EditText txtCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mDateDisplay = (TextView) findViewById(R.id.showMyDate);
        mPickDate = (Button) findViewById(R.id.myDatePickerButton);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo4) ;

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        txtCorreo.setText(global.email);
        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        updateDisplay();

    }

    public String onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String res = "";
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_pirates:
                if (checked)
                    // Pirates are the best
                    res = getString(R.string.hombre);
                break;
            case R.id.radio_ninjas:
                if (checked)
                    // Ninjas rule
                    res = getString(R.string.mujer);
                break;
        }
        return res;
    }

    private void updateDisplay() {
        this.mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
}
