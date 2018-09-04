package ru.coffeeset.cscl.reporter.ui.fragments.reportsList;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.adapters.TabAdapter;
import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.databinding.FragmentReportsTabBinding;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseFragment;

public class ReportsTabFragment extends BaseFragment {

    private FragmentReportsTabBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;


    public static ReportsTabFragment newInstance() {
        return new ReportsTabFragment();
    }

    @Override
    public void onAttach(Context context) {
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
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_reports_tab,
                container,
                false);
        return viewDataBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshActionBar();
    }

    @Override
    public void refreshActionBar() {
        if (isVisible()) {
            mListener.onTitleSet(R.string.STR_ACTION_REPORTS);
            mListener.onHomeButtonSet(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.date).setVisible(false);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.refresh).setVisible(false);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.close).setVisible(false);
    }


    @Override
    public void onStart() {
        super.onStart();
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        Logger.d(adapter);
        adapter.addFragment(ReportsFragment.newInstance(MainRepository.reports().getReportsList()), "tab1");
        adapter.addFragment(ReportsFragment.newInstance(MainRepository.reports().getReportsList2()), "tab2");

        viewDataBinding.viewPager.setAdapter(adapter);
        viewDataBinding.tabLayout.setupWithViewPager(viewDataBinding.viewPager);

    }


    public void showReport(Fragment fragment) {
        replaceChildFragmentWith(mContext,
                getFragmentManager(),
                getParentContainer(this.getView()),
                /*R.id.reports_nav_container,*/
                fragment,
                SupportAnimation.FROM_LEFT_TO_RIGHT,
                true);
    }

}