package com.skyncalci.Floating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.floating_bubble.Content;
import com.skyncalci.R;
import com.skyncalci.ncalc.calculator.BasicCalculatorActivity;

import java.text.DecimalFormat;

import hari.bounceview.BounceView;

public class SimpleHoverMenuScreen implements Content {

    private Context mContext;
    private LayoutInflater layoutInflate;
    private View view;

    private TextView expration_text;
    private TextView number_text;
    private double number1;
    private double number2;
    private String znak;
    private boolean last_znak;
    private DecimalFormat df = new DecimalFormat("#.####");
    private Button acBtn,cBtn,plus,minus,plusMinus,dot,equal,into,divide,one,two,three,four,five,six,seven,eight,nine,zero;

    public SimpleHoverMenuScreen(Context context, String mPageTitle) {
        mContext = context.getApplicationContext();
        layoutInflate =(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflate = createScreenView();
    }

    @NonNull
    private LayoutInflater createScreenView() {
        view=layoutInflate.inflate(R.layout.floating_basic_calc,null);

        expration_text = view.findViewById(R.id.text_exp);
        number_text = view.findViewById(R.id.text_number);
        last_znak = false;

        acBtn = view.findViewById(R.id.acFloat);
        cBtn = view.findViewById(R.id.cFloat);
        plus = view.findViewById(R.id.plusFloat);
        minus = view.findViewById(R.id.minusFloat);
        plusMinus = view.findViewById(R.id.plusMinusFloat);
        dot = view.findViewById(R.id.dotFloat);
        equal = view.findViewById(R.id.equalFloat);
        into = view.findViewById(R.id.intoFloat);
        divide = view.findViewById(R.id.divideFloat);
        one = view.findViewById(R.id.oneFloat);
        two = view.findViewById(R.id.twoFloat);
        three = view.findViewById(R.id.threeFloat);
        four = view.findViewById(R.id.fourFloat);
        five = view.findViewById(R.id.fiveFloat);
        six = view.findViewById(R.id.sixFloat);
        seven = view.findViewById(R.id.sevenFloat);
        eight = view.findViewById(R.id.eightFloat);
        nine = view.findViewById(R.id.nineFloat);
        zero = view.findViewById(R.id.zeroFloat);

        BounceView.addAnimTo(acBtn);
        BounceView.addAnimTo(cBtn);
        BounceView.addAnimTo(plus);
        BounceView.addAnimTo(minus);
        BounceView.addAnimTo(plusMinus);
        BounceView.addAnimTo(dot);
        BounceView.addAnimTo(equal);
        BounceView.addAnimTo(into);
        BounceView.addAnimTo(divide);
        BounceView.addAnimTo(one);
        BounceView.addAnimTo(two);
        BounceView.addAnimTo(three);
        BounceView.addAnimTo(four);
        BounceView.addAnimTo(five);
        BounceView.addAnimTo(six);
        BounceView.addAnimTo(seven);
        BounceView.addAnimTo(eight);
        BounceView.addAnimTo(nine);
        BounceView.addAnimTo(zero);


        acBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expration_text.setText("");
                number_text.setText("0");
                number1 = 0;
                number2 = 0;
            }
        });

        cBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_text.setText("0");
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(last_znak==true){
                    String e_text = expration_text.getText().toString();
                    String res = e_text.substring(0, e_text.length()-1) + "+";
                    expration_text.setText(res);
                    number_text.setText("0");
                    last_znak = true;
                    znak = "+";
                    return;
                }
                last_znak = true;
                String e_text = expration_text.getText().toString();
                String n_text = number_text.getText().toString();
                number2 = Double.parseDouble(n_text);
                if(e_text.equals("") || e_text.contains("=") || e_text.equals("Error")){
                    number1 = Double.parseDouble(n_text);
                    znak = "+";
                    String res = n_text + "+";
                    expration_text.setText(res);
                    number_text.setText("0");
                    return;
                }

                if(e_text.endsWith("=")){
                    String res = n_text + "+";
                    expration_text.setText(res);
                    number_text.setText("0");
                    znak = "+";
                    return;
                }

                double r = calculate();

                if (expration_text.getText().equals("Error"))
                    return;

                String res = String.valueOf(r) + "+";
                expration_text.setText(res);
                number_text.setText("0");
                znak = "+";
                number1 = r;
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(last_znak==true){
                    String e_text = expration_text.getText().toString();
                    String res = e_text.substring(0, e_text.length()-1) + "-";
                    expration_text.setText(res);
                    number_text.setText("0");
                    last_znak = true;
                    znak = "-";
                    return;
                }
                last_znak = true;
                String e_text = expration_text.getText().toString();
                String n_text = number_text.getText().toString();
                number2 = Double.parseDouble(n_text);
                if(e_text.equals("") || e_text.contains("=")|| e_text.equals("Error")){
                    number1 = Double.parseDouble(n_text);
                    znak = "-";
                    String res = n_text + "-";
                    expration_text.setText(res);
                    number_text.setText("0");
                    return;
                }

                if(e_text.endsWith("=")){
                    String res = n_text + "-";
                    expration_text.setText(res);
                    number_text.setText("0");
                    znak = "-";
                    return;
                }

                double r = calculate();

                if (expration_text.getText().equals("Error"))
                    return;

                String res = String.valueOf(r) + "-";
                expration_text.setText(res);
                number_text.setText("0");
                znak = "-";
                number1 = r;
            }
        });

        plusMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.charAt(0) == '-')
                    result = result.substring(1, result.length());
                else if (Double.parseDouble(result) == 0)
                    result=result;
                else
                    result = "-" + result;
                number_text.setText(result);
                last_znak = false;
            }
        });

        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (!result.contains("."))
                    result += ".";
                number_text.setText(result);
                last_znak = false;
            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_text = expration_text.getText().toString();
                String n_text = number_text.getText().toString();
                number2 = Double.parseDouble(n_text);

                System.out.println("exp = " + e_text);
                System.out.println("num = " + n_text);

                if(e_text.equals("")){
                    String res = n_text + "=" + n_text;
                    expration_text.setText("0");
                    return;
                }

                if(e_text.endsWith("=")){
                    String res = n_text + "=";
                    expration_text.setText(res);
                    number_text.setText(n_text);
                    number1 = number2;
                    return;
                }

                double r = calculate();
                if (expration_text.getText().equals("Error"))
                    return;

                String res = String.valueOf(number1) + znak + String.valueOf(number2) + "=";
                expration_text.setText(res);
                number_text.setText(String.valueOf(df.format(r)));
            }
        });

        into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(last_znak==true){
                    String e_text = expration_text.getText().toString();
                    String res = e_text.substring(0, e_text.length()-1) + "*";
                    expration_text.setText(res);
                    number_text.setText("0");
                    last_znak = true;
                    znak = "*";
                    return;
                }
                last_znak = true;
                String e_text = expration_text.getText().toString();
                String n_text = number_text.getText().toString();
                number2 = Double.parseDouble(n_text);
                if(e_text.equals("") || e_text.contains("=")|| e_text.equals("Error")){
                    number1 = Double.parseDouble(n_text);
                    znak = "*";
                    String res = n_text + "*";
                    expration_text.setText(res);
                    number_text.setText("0");
                    return;
                }

                if(e_text.endsWith("=")){
                    String res = n_text + "*";
                    expration_text.setText(res);
                    number_text.setText("0");
                    znak = "*";
                    return;
                }

                double r = calculate();

                if (expration_text.getText().equals("Error"))
                    return;

                String res = String.valueOf(r) + "*";
                expration_text.setText(res);
                number_text.setText("0");
                znak = "*";
                number1 = r;
                return;
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(last_znak==true){
                    String e_text = expration_text.getText().toString();
                    String res = e_text.substring(0, e_text.length()-1) + "/";
                    expration_text.setText(res);
                    number_text.setText("0");
                    last_znak = true;
                    znak = "/";
                    return;
                }
                last_znak = true;
                String e_text = expration_text.getText().toString();
                String n_text = number_text.getText().toString();
                number2 = Double.parseDouble(n_text);
                if(e_text.equals("") || e_text.contains("=")|| e_text.equals("Error")){
                    number1 = Double.parseDouble(n_text);
                    znak = "/";
                    String res = n_text + "/";
                    expration_text.setText(res);
                    number_text.setText("0");
                    return;
                }

                if(e_text.endsWith("=")){
                    String res = n_text + "/";
                    expration_text.setText(res);
                    number_text.setText("0");
                    znak = "/";
                    return;
                }

                double r = calculate();

                if (expration_text.getText().equals("Error"))
                    return;

                String res = String.valueOf(r) + "/";
                expration_text.setText(res);
                number_text.setText("0");
                znak = "/";
                number1 = r;
                return;
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "1";
                else
                    result += "1";
                number_text.setText(result);
                last_znak = false;
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "2";
                else
                    result += "2";
                number_text.setText(result);
                last_znak = false;
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "3";
                else
                    result += "3";
                number_text.setText(result);
                last_znak = false;
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "4";
                else
                    result += "4";
                number_text.setText(result);
                last_znak = false;
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "5";
                else
                    result += "5";
                number_text.setText(result);
                last_znak = false;
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "6";
                else
                    result += "6";
                number_text.setText(result);
                last_znak = false;
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "7";
                else
                    result += "7";
                number_text.setText(result);
                last_znak = false;
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "8";
                else
                    result += "8";
                number_text.setText(result);
                last_znak = false;
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (result.equals("0"))
                    result = "9";
                else
                    result += "9";
                number_text.setText(result);
                last_znak = false;
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = number_text.getText().toString();
                if (!result.equals("0"))
                    result += "0";
                number_text.setText(result);
                last_znak = false;
            }
        });

        return layoutInflate;
    }

    public double calculate() {
        double r = 0;
        System.out.println(number1);
        System.out.println(znak);
        System.out.println(number2);
        switch (znak){
            case "+":
                r = number1 + number2;
                break;
            case "-":
                r = number1 - number2;
                break;
            case "*":
                r = number1 * number2;
                break;
            case "/":
                if(number2==0){
                    expration_text.setText("Error");
                    number_text.setText("0");
                    return 0;
                }
                r = number1 / number2;
                break;
        }
        System.out.println(r);
        last_znak=false;
        return r;
    }

    // Make sure that this method returns the SAME View.  It should NOT create a new View each time
    // that it is invoked.
    @NonNull
    @Override
    public View getView() {
        return view;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {
        // No-op.
    }

    @Override
    public void onHidden() {
        // No-op.
    }
}