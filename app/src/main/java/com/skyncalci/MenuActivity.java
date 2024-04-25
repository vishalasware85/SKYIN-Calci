package com.skyncalci;

import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.EXPAND;
import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.EXPONENT;
import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.REDUCE;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skyncalci.calculator.symja.activities.DerivativeActivity;
import com.skyncalci.calculator.symja.activities.ExpandAllExpressionActivity;
import com.skyncalci.calculator.symja.activities.FactorExpressionActivity;
import com.skyncalci.calculator.symja.activities.FactorPrimeActivity;
import com.skyncalci.calculator.symja.activities.IdeActivity;
import com.skyncalci.calculator.symja.activities.IntegrateActivity;
import com.skyncalci.calculator.symja.activities.LimitActivity;
import com.skyncalci.calculator.symja.activities.ModuleActivity;
import com.skyncalci.calculator.symja.activities.NumberActivity;
import com.skyncalci.calculator.symja.activities.PermutationActivity;
import com.skyncalci.calculator.symja.activities.PiActivity;
import com.skyncalci.calculator.symja.activities.PrimitiveActivity;
import com.skyncalci.calculator.symja.activities.SimplifyExpressionActivity;
import com.skyncalci.calculator.symja.activities.SolveEquationActivity;
import com.skyncalci.calculator.symja.activities.TrigActivity;
import com.skyncalci.calculator.symja.activities.TrigActivityExponent;
import com.skyncalci.calculator.symja.activities.TrigActivityReduce;
import com.skyncalci.ncalc.calculator.BasicCalculatorActivity;
import com.skyncalci.ncalc.calculator.LogicCalculatorActivity;
import com.skyncalci.ncalc.document.MarkdownListDocumentActivity;
import com.skyncalci.ncalc.document.info.InfoActivity;
import com.skyncalci.ncalc.geom2d.GeometryDescartesActivity;
import com.skyncalci.ncalc.graph.GraphActivity;
import com.skyncalci.ncalc.matrix.MatrixCalculatorActivity;
import com.skyncalci.ncalc.settings.SettingsActivity;
import com.skyncalci.ncalc.systemequations.SystemEquationActivity;
import com.skyncalci.ncalc.unitconverter.UnitCategoryActivity;

public class MenuActivity extends AppCompatActivity {

    protected final Handler handler = new Handler();
    private LinearLayout allFunc,idemode,floatingMode,scientificCalc,baseNCalc,solveEqn,solveSYSEqn,graph,geometry2d,simplifyExpression,factorPolynomial,unitConverter,
    newtonBinomialExpre,matrix,derivative,primitive,integrate,findlimit,combinatation,permutation,primeFactor,module,catalanNo,primeNo,divisors,piNo,fibonacciNo,trigExpand,
    trigReduce,trigExponent,setting,rate,about;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        allFunc=findViewById(R.id.allFunctions);
        idemode=findViewById(R.id.ideMode);
        floatingMode=findViewById(R.id.floatingMode);
        scientificCalc=findViewById(R.id.scientificCalc);
        baseNCalc=findViewById(R.id.baseNCalc);
        solveEqn=findViewById(R.id.solveEqn);
        solveSYSEqn=findViewById(R.id.solveSysEqn);
        graph=findViewById(R.id.graphBtn);
        geometry2d=findViewById(R.id.geometry2d);
        simplifyExpression=findViewById(R.id.simplifyExpression);
        factorPolynomial=findViewById(R.id.factorPolynomial);
        unitConverter=findViewById(R.id.unitConverter);
        newtonBinomialExpre=findViewById(R.id.newtonBinomialExpre);
        matrix=findViewById(R.id.matrix);
        primitive=findViewById(R.id.primative);
        derivative=findViewById(R.id.derivative);
        primeFactor=findViewById(R.id.primative);
        integrate=findViewById(R.id.integrate);
        findlimit=findViewById(R.id.findLimit);
        combinatation=findViewById(R.id.combination);
        permutation=findViewById(R.id.permutation);
        primeFactor=findViewById(R.id.primeFactor);
        module=findViewById(R.id.module);
        catalanNo=findViewById(R.id.catalanNumber);
        primeNo=findViewById(R.id.primeNumber);
        divisors=findViewById(R.id.divisors);
        piNo=findViewById(R.id.piNumber);
        fibonacciNo=findViewById(R.id.fibonacciNo);
        trigExpand=findViewById(R.id.trignoExpand);
        trigReduce=findViewById(R.id.trignoReduce);
        trigExponent=findViewById(R.id.trignoExponent);
        setting=findViewById(R.id.settings);
        rate=findViewById(R.id.rateApp);
        about=findViewById(R.id.aboutApp);

        allFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), MarkdownListDocumentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        idemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), IdeActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        floatingMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        scientificCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), BasicCalculatorActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        baseNCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), LogicCalculatorActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        solveEqn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), SolveEquationActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        solveSYSEqn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), SystemEquationActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), GraphActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        geometry2d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), GeometryDescartesActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        simplifyExpression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), SimplifyExpressionActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        factorPolynomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), FactorExpressionActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        unitConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), UnitCategoryActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        newtonBinomialExpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ExpandAllExpressionActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        matrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), MatrixCalculatorActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        derivative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), DerivativeActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        primitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), PrimitiveActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        integrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), IntegrateActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        findlimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), LimitActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        permutation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), PermutationActivity.class);
                intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_PERMUTATION);
                postStartActivity(intent);
                finish();
            }
        });

        combinatation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), PermutationActivity.class);
                intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_COMBINATION);
                postStartActivity(intent);
                finish();
            }
        });

        primeFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), FactorPrimeActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ModuleActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        catalanNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.CATALAN);
                postStartActivity(intent);
                finish();
            }
        });

        primeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.PRIME);
                postStartActivity(intent);
                finish();
            }
        });

        divisors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.DIVISORS);
                postStartActivity(intent);
                finish();
            }
        });

        piNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), PiActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        fibonacciNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.FIBONACCI);
                postStartActivity(intent);
                finish();
            }
        });

        trigExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, EXPAND);
                postStartActivity(intent);
                finish();
            }
        });

        trigReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), TrigActivityReduce.class);
                intent.putExtra(TrigActivity.TYPE, REDUCE);
                postStartActivity(intent);
                finish();
            }
        });

        trigExponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), TrigActivityExponent.class);
                intent.putExtra(TrigActivity.TYPE, EXPONENT);
                postStartActivity(intent);
                finish();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                postStartActivity(intent);
                finish();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPlayStore();
                finish();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), InfoActivity.class);
                postStartActivity(intent);
                finish();
            }
        });
    }

    private void postStartActivity(final Intent intent) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 100);
    }

    public void gotoPlayStore() {
        gotoPlayStore(BuildConfig.APPLICATION_ID);
    }

    public void gotoPlayStore(String appId) {
        Uri uri = Uri.parse("market://details?id=" + appId);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId)));
        }
    }
}
