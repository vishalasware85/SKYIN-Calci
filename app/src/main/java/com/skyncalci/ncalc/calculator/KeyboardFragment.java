/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.skyncalci.ncalc.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.skyncalci.R;
import com.skyncalci.ncalc.document.MarkdownListDocumentFragment;
import com.skyncalci.ncalc.settings.SettingsActivity;
import com.skyncalci.ncalc.view.ButtonID;
import com.skyncalci.ncalc.view.CalculatorEditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import hari.bounceview.BounceView;

/**
 * Created by Duy on 9/21/2017.
 */

public class KeyboardFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    public static final String TAG = "KeyboardFragment";
    @Nullable
    private KeyboardListener mCalculatorListener;
    private View view2;

    private AppCompatButton solve,fact,ddx,comma,degree,x,cuberoot,sin,cos,tan,a,b,c,tenx,bracketopen,bracketclose,factorial,limit,roottwo,rootthree,underrrot,
    one,two,three,four,five,six,seven,eight,nine,zero,dot,pie,clear,multi,divide,plus,minus,ans,equal;
    private ImageButton back;


    public static KeyboardFragment newInstance() {

        Bundle args = new Bundle();

        KeyboardFragment fragment = new KeyboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCalculatorListener = (KeyboardListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view2 = inflater.inflate(R.layout.fragment_keyboard, container, false);

        solve=view2.findViewById(R.id.btn_solve_);
        fact=view2.findViewById(R.id.btn_fact);
        ddx=view2.findViewById(R.id.btn_derivative);
        comma=view2.findViewById(R.id.btn_comma);
        degree=view2.findViewById(R.id.btn_degree);
        x=view2.findViewById(R.id.btn_varx);
        cuberoot=view2.findViewById(R.id.btn_cbrt);
        sin=view2.findViewById(R.id.btn_sin);
        cos=view2.findViewById(R.id.btn_cos);
        tan=view2.findViewById(R.id.btn_tan);
        a=view2.findViewById(R.id.btn_vara);
        b=view2.findViewById(R.id.btn_varb);
        c=view2.findViewById(R.id.btn_varc);
        tenx=view2.findViewById(R.id.btn_ten_power);
        bracketopen=view2.findViewById(R.id.btn_left_paratheses);
        bracketclose=view2.findViewById(R.id.btn_right_parathese);
        factorial=view2.findViewById(R.id.btn_factorial);
        limit=view2.findViewById(R.id.btn_power);
        roottwo=view2.findViewById(R.id.btn_power_2);
        rootthree=view2.findViewById(R.id.btn_power_3);
        underrrot=view2.findViewById(R.id.btn_sqrt);
        one=view2.findViewById(R.id.digit1);
        two=view2.findViewById(R.id.digit2);
        three=view2.findViewById(R.id.digit3);
        four=view2.findViewById(R.id.digit4);
        five=view2.findViewById(R.id.digit5);
        six=view2.findViewById(R.id.digit6);
        seven=view2.findViewById(R.id.digit7);
        eight=view2.findViewById(R.id.digit8);
        nine=view2.findViewById(R.id.digit9);
        zero=view2.findViewById(R.id.digit0);
        dot=view2.findViewById(R.id.btn_dot);
        pie=view2.findViewById(R.id.btn_pi);
        clear=view2.findViewById(R.id.btn_clear);
        back=view2.findViewById(R.id.btn_delete);
        multi=view2.findViewById(R.id.btn_mul);
        divide=view2.findViewById(R.id.btn_div);
        plus=view2.findViewById(R.id.btn_plus);
        minus=view2.findViewById(R.id.btn_minus);
        ans=view2.findViewById(R.id.btn_ans);
        equal=view2.findViewById(R.id.btn_equal);

        BounceView.addAnimTo(solve);
        BounceView.addAnimTo(fact);
        BounceView.addAnimTo(ddx);
        BounceView.addAnimTo(comma);
        BounceView.addAnimTo(degree);
        BounceView.addAnimTo(x);
        BounceView.addAnimTo(cuberoot);
        BounceView.addAnimTo(sin);
        BounceView.addAnimTo(cos);
        BounceView.addAnimTo(tan);
        BounceView.addAnimTo(a);
        BounceView.addAnimTo(b);
        BounceView.addAnimTo(c);
        BounceView.addAnimTo(tenx);
        BounceView.addAnimTo(bracketopen);
        BounceView.addAnimTo(bracketclose);
        BounceView.addAnimTo(factorial);
        BounceView.addAnimTo(limit);
        BounceView.addAnimTo(rootthree);
        BounceView.addAnimTo(roottwo);
        BounceView.addAnimTo(underrrot);
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
        BounceView.addAnimTo(dot);
        BounceView.addAnimTo(pie);
        BounceView.addAnimTo(clear);
        BounceView.addAnimTo(back);
        BounceView.addAnimTo(multi);
        BounceView.addAnimTo(divide);
        BounceView.addAnimTo(plus);
        BounceView.addAnimTo(minus);
        BounceView.addAnimTo(ans);
        BounceView.addAnimTo(equal);


        SlidingUpPanelLayout slidingPaneLayout=view2.findViewById(R.id.slide);
        TextView arrowTxt=view2.findViewById(R.id.arrowTxt);
        slidingPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset==1){
                    arrowTxt.setVisibility(View.INVISIBLE);
                }else {
                    arrowTxt.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {}
        });

        return view2;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (int id : ButtonID.getIdBasic()) {
            try {
                View v = view.findViewById(id);
                if (v != null) {
                    v.setOnClickListener(this);
                    v.setOnLongClickListener(this);
                } else {
                    View padAdvance = view.findViewById(R.id.pad_advance);
                    v = padAdvance.findViewById(id);
                    if (v != null) {
                        v.setOnClickListener(this);
                        v.setOnLongClickListener(this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (mCalculatorListener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_derivative:
                mCalculatorListener.clickDerivative();
                break;
            case R.id.btn_graph_main:
                mCalculatorListener.clickGraph();
                break;
            case R.id.btn_ten_power:
                mCalculatorListener.insertText("10^");
                break;
            case R.id.btn_power_2:
                mCalculatorListener.insertText("^");
                mCalculatorListener.insertText("2");
                break;
            case R.id.btn_power_3:
                mCalculatorListener.insertText("^");
                mCalculatorListener.insertText("3");
                break;
            case R.id.btn_fact:
                mCalculatorListener.clickFactorPrime();
                break;
            case R.id.btn_help: {
                Intent intent = new Intent(getContext(), MarkdownListDocumentFragment.class);
                startActivity(intent);
            }
            case R.id.btn_solve_:
                mCalculatorListener.clickSolveEquation();
                break;
            case R.id.btn_delete:
                mCalculatorListener.onDelete();
                break;
            case R.id.btn_clear:
                mCalculatorListener.clickClear();
                break;
            case R.id.btn_equal:
                mCalculatorListener.onEqual();
                break;
            case R.id.btn_ans:
                mCalculatorListener.insertText("Ans");
                break;
            case R.id.btn_sqrt:
                mCalculatorListener.insertText("Sqrt(" + CalculatorEditText.CURSOR + ")");
                break;
            default:
                if (view instanceof AppCompatButton) {
                    AppCompatButton calcButton = (AppCompatButton) view;
                    String text = calcButton.getText().toString();
                    if (text.length() >= 2) {
                        mCalculatorListener.insertText(text + "(" + CalculatorEditText.CURSOR + ")");
                    } else {
                        mCalculatorListener.insertText(((Button) view).getText().toString());
                    }
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mCalculatorListener == null) {
            return false;
        }
        switch (view.getId()) {
            case R.id.btn_delete:
                mCalculatorListener.clickClear();
                return true;
        }
        return false;
    }
}
