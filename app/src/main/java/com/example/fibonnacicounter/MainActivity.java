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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String METHOD_RECURSION = "recursion";
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
        setCurrentMethod();

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
        switch (position) {
            case 0:
                mDescriptionTxt.setText(R.string.recursion_method_description);
                mNumberInput.setHint(R.string.recursion_method_hint);
                mFormulaImg.setVisibility(View.GONE);
                setCurrentMethod();
                break;
            case 1:
                mDescriptionTxt.setText(R.string.array_method_description);
                mNumberInput.setHint(R.string.remembering_method_hint);
                mFormulaImg.setVisibility(View.GONE);
                setCurrentMethod();
                break;
            case 2:
                mDescriptionTxt.setText(R.string.binet_method_description);
                mNumberInput.setHint(R.string.remembering_method_hint);
                mFormulaImg.setVisibility(View.VISIBLE);
                setCurrentMethod();
                break;
        }
    }

    private void onCalculateBtnClick(View v) {
        if (mNumberInput.getText().toString().length() > 0) {
            onCalculateClick();
        } else {
            Toast.makeText(v.getContext(), "Please enter the number", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCurrentMethod() {
        switch (mMethodSpinner.getSelectedItemPosition()) {
            case 0:
                mCurrentMethod = CalculationMethods.RECURSION;
                break;
            case 1:
                mCurrentMethod = CalculationMethods.ARRAY;
                break;
            case 2:
                mCurrentMethod = CalculationMethods.BINET;
                break;
        }
    }

    private void onCalculateClick() {
        int inputNum = Integer.parseInt(mNumberInput.getText().toString());
        if (checkInput(inputNum)) {
            new CalculateTask().execute(inputNum);
        } else {
            Toast.makeText(this, "Wrong number", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkInput(int inputNum) {
        switch (mCurrentMethod) {
            case RECURSION:
                return inputNum <= 40;
            case ARRAY:
            case BINET:
                return inputNum <= 92;
            default:
                return true;

        }
    }

    private void setupViews() {
        mMethodSpinner = findViewById(R.id.spin_calc_method);
        mCalculateBtn = findViewById(R.id.btn_calculate);
        mDescriptionTxt = findViewById(R.id.txt_method_description);
        mNumberInput = findViewById(R.id.number_input);
        mResultTxt = findViewById(R.id.txt_results);
        mFormulaImg = findViewById(R.id.img_formula);

    }


    enum CalculationMethods {RECURSION, ARRAY, BINET}

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
                case BINET:
                    return MathUtils.calcBinetFormula(integers[0]);

                default:
                    return 0l;
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
