package ru.coffeeset.cscl.reporter.ui.fragments.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.models.accessList.Property;
import ru.coffeeset.cscl.reporter.models.reports.Report;
import ru.coffeeset.cscl.reporter.models.reports.Revenue;

public class ChartDash {


    private BarChart chart;
    private Context context;
    private boolean animation;


    public ChartDash(BarChart chart, Context context, boolean animation) {
        this.chart = chart;
        this.context = context;
        this.animation = animation;

        initChart();
    }


    private void initChart() {

//Axis
        List<String> xValues = new ArrayList<>();
        xValues.add(context.getResources().getString(R.string.STR_PROPERTY_MASTER));
        xValues.add(context.getResources().getString(R.string.STR_PROPERTY_FRANCHISE));
        xValues.add(context.getResources().getString(R.string.STR_PROPERTY_SPV));


        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(13);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setAxisMinimum(0f);
        yAxis.setValueFormatter((value, axis) -> "");

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(15);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(3f);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter((value, axis) -> {
            if (value >= 0 && value < xValues.size())
                return xValues.get((int) value);
            return "";
        });

//legend
        Legend l = chart.getLegend();
        l.setFormSize(20f);
        l.setTextSize(15);
        l.setTextColor(Color.WHITE);
        l.setFormToTextSpace(5f);
        l.setXEntrySpace(10f);


//property
        chart.setTouchEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setClipValuesToContent(false);
    }


    public void refreshData(Revenue revenueMonth) {

        Map<String, List<BarEntry>> map = new HashMap<>();

        List<BarEntry> fact = new ArrayList<>();
        fact.add(new BarEntry(Property.MASTER, (int) revenueMonth.getSumRevenueByProperty(Property.MASTER)));
        fact.add(new BarEntry(Property.FRANCHISE, (int) revenueMonth.getSumRevenueByProperty(Property.FRANCHISE)));
        fact.add(new BarEntry(Property.SPV, (int) revenueMonth.getSumRevenueByProperty(Property.SPV)));

        List<BarEntry> plan = new ArrayList<>();
        plan.add(new BarEntry(Property.MASTER, (int) revenueMonth.getSumPlanRevenueByProperty(Property.MASTER)));
        plan.add(new BarEntry(Property.FRANCHISE, (int) revenueMonth.getSumPlanRevenueByProperty(Property.FRANCHISE)));
        plan.add(new BarEntry(Property.SPV, (int) revenueMonth.getSumPlanRevenueByProperty(Property.SPV)));

        map.put("fact", fact);
        map.put("plan", plan);

        refreshChart(map);
    }


    private void refreshChart(Map<String, List<BarEntry>> map) {

//data set
        BarDataSet set1 = new BarDataSet(map.get("fact"), "FACT");
        set1.setColor(context.getResources().getColor(R.color.dashChartFact));
        set1.setDrawValues(false);
        BarDataSet set2 = new BarDataSet(map.get("plan"), "PLAN");
        set2.setColor(context.getResources().getColor(R.color.dashChartPlan));
        set2.setDrawValues(false);

//group
        float groupSpace = 0.36f;
        float barSpace = 0.07f;
        float barWidth = 0.25f;

        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth);
        chart.setData(data);
        chart.groupBars(0, groupSpace, barSpace);

//animate
        if (animation)
            chart.animateY(1000);

        chart.invalidate();
    }
}
