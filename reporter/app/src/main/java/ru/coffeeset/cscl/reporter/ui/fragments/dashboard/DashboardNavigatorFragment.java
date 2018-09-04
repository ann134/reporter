package ru.coffeeset.cscl.reporter.ui.fragments.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseNavigatorFragment;


public class DashboardNavigatorFragment extends BaseNavigatorFragment {

    public static DashboardNavigatorFragment newInstance() {
        return new DashboardNavigatorFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //((MainActivity)context).setActionBarIsVisible(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_navigation_dashboard, container, false);

        replaceChildFragmentWith(getContext(), getFragmentManager(), R.id.dashboard_nav_container, DashboardFragment.newInstance(),
                SupportAnimation.NO_ANIMATION,
                false);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //((MainActivity)getActivity()).setActionBarIsVisible(true);
    }
}
