package ru.coffeeset.cscl.reporter.ui.fragments.dashboard;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.ReporterApplication;
import ru.coffeeset.cscl.reporter.controllers.utils.Formatter;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.databinding.FragmentDashboardBinding;
import ru.coffeeset.cscl.reporter.models.DateEntry;
import ru.coffeeset.cscl.reporter.models.reports.ReportState;
import ru.coffeeset.cscl.reporter.models.reports.Revenue;
import ru.coffeeset.cscl.reporter.ui.MainActivity;
import ru.coffeeset.cscl.reporter.ui.fragments.reports.BaseReportFragment;
import ru.coffeeset.cscl.reporter.ui.viewmodels.AccessListViewModel;
import ru.coffeeset.cscl.reporter.ui.viewmodels.DashboardViewModel;


public class DashboardFragment extends BaseReportFragment {

    private FragmentDashboardBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;

    private AccessListViewModel viewModelList;
    private DashboardViewModel viewModelDash;

    private DateEntry date;
    private ChartDash chartDash;


    public ObservableField<Revenue> revenueToday = new ObservableField<>();
    public ObservableField<Revenue> revenueYesterday = new ObservableField<>();
    public ObservableField<Revenue> revenueMonth = new ObservableField<>();


    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onAttach(Context context) {
        Logger.d("dash onAttach");
        super.onAttach(context);
        this.mContext = context;
        try {
            mListener = (ISupportActionBarListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ISupportActionBarListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.d("dash onCreate");
        super.onCreate(savedInstanceState);
        viewModelList = ViewModelProviders.of((MainActivity) mContext).get(AccessListViewModel.class);
        viewModelDash = ViewModelProviders.of((MainActivity) mContext).get(DashboardViewModel.class);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d("dash onCreateView");
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_dashboard,
                container,
                false);

        revenueToday.set(new Revenue());
        revenueYesterday.set(new Revenue());
        revenueMonth.set(new Revenue());

        viewDataBinding.setDash(this);

        return viewDataBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.d("dash onActivityCreated");

        refreshActionBar();
    }

    @Override
    public void refreshActionBar() {
        if (isVisible() || ReporterApplication.getInstance().dashboardFirstRun) {
            mListener.onTitleSet(R.string.STR_ACTION_DASHBOARD);
            mListener.onHomeButtonSet(false);
        }
    }


    @Override
    public void onStart() {
        Logger.d("dash onStart");
        super.onStart();

        chartDash = new ChartDash(viewDataBinding.chart, mContext, viewModelList.getAnimation());
        subscribe();


        viewModelList.getLiveDataFilteredDivisions().observe(this, list -> {
                    if (isVisible() || ReporterApplication.getInstance().dashboardFirstRun) {
                        ReporterApplication.getInstance().dashboardFirstRun = false;

                        viewModelDash.setListFilteredDivisions(list);
                        refresh();
                    }
                }
        );
    }

    @Override
    public void onResume() {
        Logger.d("dash onResume");
        super.onResume();
    }


//------------------------------------------------------------------------------------------


    @Override
    public void refresh() {
        cancelReQuests();

        viewModelDash.refresh();

        revenueToday.set(new Revenue());
        revenueYesterday.set(new Revenue());
        revenueMonth.set(new Revenue());

    }


    private void subscribe() {
        viewModelDash.getLiveDataRevenue()
                .observe(this, (Revenue revenue) -> {

                            if (revenue != null) {
                                revenue.setState(ReportState.IDLE);
                                date = viewModelDash.getDate();



                                if (revenue.getReportDate().equals(Formatter.getDateStringShortISO(date.getDateToNow())))
                                    revenueToday.set(revenue);

                                if (revenue.getReportDate().equals(Formatter.getDateStringShortISO(date.getDateToNow().minusDays(1))))
                                    revenueYesterday.set(revenue);

                                if (revenue.getReportDate().equals(Formatter.getDateStringShortISO(date.getMonthFrom()))) {
                                    revenueMonth.set(revenue);

                                    chartDash.refreshData(revenue);
                                    progressbar(revenue);
                                }

                            } else
                                Toast.makeText(mContext, R.string.TOAST_REPORT_NOTHING, Toast.LENGTH_SHORT).show();
                        }
                );
    }


    private void progressbar(Revenue revenue) {
        if (viewModelList.getAnimation()) {
            ObjectAnimator.ofInt(viewDataBinding.progressBar, "progress", 0, (int) revenue.getProgress())
                    .setDuration(1500)
                    .start();
        } else {
            viewDataBinding.progressBar.setProgress((int) revenue.getProgress());
        }


        String period = String.format("%1$s - %2$s",
                Formatter.getDateStringShortWithMonth(viewModelDash.getDate().getMonthFrom()),
                Formatter.getDateStringShortWithMonth(viewModelDash.getDate().getMonthToNow()));
        viewDataBinding.textProgressPeriod.setText(period);
    }


//------------------------------------------------------------------------------------------


    @Override
    public void onPause() {
        super.onPause();
        Logger.d("dash onPause");

        //cancelReQuests();
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d("dash onStop");

        //cancelReQuests();
    }

    @Override
    public void onDestroyView() {
        Logger.d("dash onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Logger.d("dash onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Logger.d("dash onDetach");
        super.onDetach();
    }
}
