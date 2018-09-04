package ru.coffeeset.cscl.reporter.ui.fragments.dashboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import ru.coffeeset.cscl.reporter.R;

public class WidgetDash extends ConstraintLayout {

    ConstraintLayout layout;

    TextView master;
    TextView franchise;
    TextView spv;
    TextView factMaster;
    TextView factFranchise;
    TextView factSpv;
    TextView planMaster;
    TextView planFranchise;
    TextView planSpv;
    TextView empty;
    TextView fact;
    TextView plan;


    public WidgetDash(Context context) {
        super(context);
    }

    public WidgetDash(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WidgetDash);


        String factMasterText = a.getString(R.styleable.WidgetDash_factMaster);
        String factFranchiseText = a.getString(R.styleable.WidgetDash_factFranchise);
        String factSpvText = a.getString(R.styleable.WidgetDash_factSpv);
        String planMasterText = a.getString(R.styleable.WidgetDash_planMaster);
        String planFranchiseText = a.getString(R.styleable.WidgetDash_planFranchise);
        String planSpvText = a.getString(R.styleable.WidgetDash_planSpv);

        factMasterText = factMasterText == null ? "" : factMasterText;
        factFranchiseText = factFranchiseText == null ? "" : factFranchiseText;
        factSpvText = factSpvText == null ? "" : factSpvText;
        planMasterText = planMasterText == null ? "" : planMasterText;
        planFranchiseText = planFranchiseText == null ? "" : planFranchiseText;
        planSpvText = planSpvText == null ? "" : planSpvText;


        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);

        layout = (ConstraintLayout) li.inflate(R.layout.widget_dash, this, true);


        factMaster = layout.findViewById(R.id.fact_master);
        factFranchise = layout.findViewById(R.id.fact_franchise);
        factSpv = layout.findViewById(R.id.fact_spv);
        planMaster = layout.findViewById(R.id.plan_master);
        planFranchise = layout.findViewById(R.id.plan_franchise);
        planSpv = layout.findViewById(R.id.plan_spv);


        master = layout.findViewById(R.id.master);
        franchise = layout.findViewById(R.id.franchise);
        spv = layout.findViewById(R.id.spv);
        factMaster = layout.findViewById(R.id.fact_master);
        factFranchise = layout.findViewById(R.id.fact_franchise);
        factSpv = layout.findViewById(R.id.fact_spv);
        planMaster = layout.findViewById(R.id.plan_master);
        planFranchise = layout.findViewById(R.id.plan_franchise);
        planSpv = layout.findViewById(R.id.plan_spv);
        empty = layout.findViewById(R.id.empty);
        fact = layout.findViewById(R.id.fact);
        plan = layout.findViewById(R.id.plan);


        master.setText(getResources().getString(R.string.STR_PROPERTY_MASTER));
        franchise.setText(getResources().getString(R.string.STR_PROPERTY_FRANCHISE));
        spv.setText(getResources().getString(R.string.STR_PROPERTY_SPV));
        factMaster.setText(factMasterText);
        factFranchise.setText(factFranchiseText);
        factSpv.setText(factSpvText);
        planMaster.setText(planMasterText);
        planFranchise.setText(planFranchiseText);
        planSpv.setText(planSpvText);
        empty.setText("");
        fact.setText(getResources().getString(R.string.STR_REPORT_DASH_FACT));
        plan.setText(getResources().getString(R.string.STR_REPORT_DASH_PLAN));


        a.recycle();
    }

    public WidgetDash(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setFactMaster(String text) {
        factMaster.setText(text);
    }

    public void setFactFranchise(String text) {
        factFranchise.setText(text);
    }

    public void setFactSpv(String text) {
        factSpv.setText(text);
    }

    public void setPlanMaster(String text) {
        planMaster.setText(text);
    }

    public void setPlanFranchise(String text) {
        planFranchise.setText(text);
    }

    public void setPlanSpv(String text) {
        planSpv.setText(text);
    }

}
