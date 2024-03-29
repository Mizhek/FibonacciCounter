package com.example.fibonnacicounter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String SPINNER_POSITION = "position";
    public static final String RESULT = "result";
    public static final String INPUT = "input";

    enum CalculationMethods {RECURSION, ARRAY, FORMULA}

    CalculationMethods mCurrentMethod;


    Spinner mMethodSpinner;
    EditText mNumberInput;
    Button mCalculateBtn;
    TextView mDescriptionTxt;
    TextView mResultTxt;
    AlertDialog mAlertDialog;
    ImageView mFormulaImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setCurrentMethod(0);

        mMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSpinnerItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mCalculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCalculateBtnClick(v);
            }
        });
    }


    private void onSpinnerItemSelected(int position) {
        setCurrentMethod(position);
        manageViewsOnSpinnerChanged();
    }

    private void manageViewsOnSpinnerChanged() {
        switch (mCurrentMethod) {
            case RECURSION:
                mDescriptionTxt.setText(R.string.recursion_method_description);
                mNumberInput.setHint(R.string.recursion_method_hint);
                mFormulaImg.setVisibility(View.GONE);
                break;
            case ARRAY:
                mDescriptionTxt.setText(R.string.array_method_description);
                mNumberInput.setHint(R.string.remembering_method_hint);
                mFormulaImg.setVisibility(View.GONE);
                break;
            case FORMULA:
                mDescriptionTxt.setText(R.string.binet_method_description);
                mNumberInput.setHint(R.string.remembering_method_hint);
                mFormulaImg.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setCurrentMethod(int position) {
        switch (position) {
            case 0:
                mCurrentMethod = CalculationMethods.RECURSION;
                break;
            case 1:
                mCurrentMethod = CalculationMethods.ARRAY;
                break;
            case 2:
                mCurrentMethod = CalculationMethods.FORMULA;
                break;
        }
    }

    private void onCalculateBtnClick(View v) {
        String input = mNumberInput.getText().toString();
        if (input.length() > 0) {

            int inputNum = Integer.parseInt(input);
            if (checkInput(inputNum)) {
                new CalculateTask().execute(inputNum);
            } else {
                Toast.makeText(this, "Wrong number", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(v.getContext(), "Please enter the number", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean checkInput(int inputNum) {
        switch (mCurrentMethod) {
            case RECURSION:
                return inputNum <= 40;
            case ARRAY:
            case FORMULA:
                return inputNum <= 92;
            default:
                return true;

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_POSITION, mMethodSpinner.getSelectedItemPosition());
        outState.putString(RESULT, mResultTxt.getText().toString());
        outState.putString(INPUT, mNumberInput.getText().toString());
    }

    private void setupViews() {
        mMethodSpinner = findViewById(R.id.spin_calc_method);
        mCalculateBtn = findViewById(R.id.btn_calculate);
        mDescriptionTxt = findViewById(R.id.txt_method_description);
        mNumberInput = findViewById(R.id.number_input);
        mResultTxt = findViewById(R.id.txt_results);
        mFormulaImg = findViewById(R.id.img_formula);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMethodSpinner.setSelection(savedInstanceState.getInt(SPINNER_POSITION));
        mResultTxt.setText(savedInstanceState.getString(RESULT));
        mNumberInput.setText(savedInstanceState.getString(INPUT));
    }


    private class CalculateTask extends AsyncTask<Integer, Void, Long> {

        @Override
        protected void onPreExecute() {
            if (mCurrentMethod == CalculationMethods.RECURSION) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(R.layout.dialog_loading);
                builder.setCancelable(false);
                mAlertDialog = builder.create();
                mAlertDialog.show();
            }
        }

        @Override
        protected Long doInBackground(Integer... integers) {
            switch (mCurrentMethod) {
                case RECURSION:
                    return MathUtils.calcRecursion(integers[0]);
                case ARRAY:
                    return MathUtils.calculateArray(integers[0]);
                case FORMULA:
                    return MathUtils.calcBinetFormula(integers[0]);

                default:
                    return 0L;
            }
        }


        @Override
        protected void onPostExecute(Long aLong) {

            if (mAlertDialog != null) {
                mAlertDialog.hide();
            }

            mResultTxt.setText(aLong.toString());
        }


    }
}
