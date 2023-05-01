package com.bharatintern.prasant.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button backspace;
    TextView expression, result;
    MaterialButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0;
    MaterialButton  btnClear, btnBrace1, btnBrace2, btnEquals, btnPoint;
    MaterialButton btnPlus, btnMinus, btnMultiply, btnDivide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expression = findViewById(R.id.expression);
        result = findViewById(R.id.result);
        backspace = findViewById(R.id.buttonBackspace);

        assignId(btn0, R.id.button0);
        assignId(btn1, R.id.button1);
        assignId(btn2, R.id.button2);
        assignId(btn3, R.id.button3);
        assignId(btn4, R.id.button4);
        assignId(btn5, R.id.button5);
        assignId(btn6, R.id.button6);
        assignId(btn7, R.id.button7);
        assignId(btn8, R.id.button8);
        assignId(btn9, R.id.button9);

        assignId(btnClear, R.id.buttonC);
        assignId(btnBrace1, R.id.buttonBrace1);
        assignId(btnBrace2, R.id.buttonBrace2);
        assignId(btnPlus, R.id.buttonAddition);
        assignId(btnMinus, R.id.buttonMinus);
        assignId(btnMultiply, R.id.buttonMultiply);
        assignId(btnDivide, R.id.buttonDivision);
        assignId(btnPoint, R.id.buttonPoint);
        assignId(btnEquals, R.id.buttonEquals);

        TextView urlBtn = findViewById(R.id.dev);
        urlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.linkedin.com/in/prasantcp/";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st = expression.getText().toString();

                if (st.length() == 0) return;

                if (st.length() == 1) {
                    result.setText("");
                    expression.setText("");
                    return;
                }

                st = st.substring(0, st.length()-1);

                expression.setText(st);
                String calcValue = calculate(st);

                if (!calcValue.equals("Error")) {
                    result.setText(calcValue);
                } else {
                    result.setText("");
                }
            }
        });
    }

    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String msg = "Invalid format used!";
        MaterialButton button = (MaterialButton) view;
        String buttonTxt = button.getText().toString();
        String statement = expression.getText().toString();

        if (buttonTxt.equals("C")) {
            expression.setText("");
            result.setText("");
            return;
        } else if (buttonTxt.equals("=")) {
            if (expression.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                return;
            }
            String calcValue = calculate(statement);

            if (!calcValue.equals("Error")) {
                result.setText(calcValue);
            } else {
                result.setText("");
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                return;
            }

            String res = result.getText().toString();
            DecimalFormat df_obj = new DecimalFormat("#.#####");
            df_obj.setRoundingMode(RoundingMode.FLOOR);
            res = df_obj.format(Double.valueOf(res));

            expression.setText(res);
            result.setText("");
            return;
        } else {
            if (expression.getText().toString().length() == 0) {
                if (buttonTxt.equals(")") || buttonTxt.equals("/") || buttonTxt.equals("*")) {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            statement += buttonTxt;
        }

        expression.setText(statement);
        String calcValue = calculate(statement);

        if (!calcValue.equals("Error")) {
            result.setText(calcValue);
        } else {
            result.setText("");
        }
    }

    private String calculate(String statement) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initSafeStandardObjects();
            String calcValue = context.evaluateString(scriptable, statement, "Javascript", 1, null).toString();
            if (calcValue.endsWith(".0")) {
                calcValue = calcValue.replace(".0", "");
            }
            return calcValue;
        } catch (Exception e) {
            return "Error";
        }
    }
}