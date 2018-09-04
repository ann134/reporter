package ru.coffeeset.cscl.reporter.ui.fragments.reportsList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseNavigatorFragment;


public class ReportsNavigatorFragment extends BaseNavigatorFragment {

    public static ReportsNavigatorFragment newInstance() {
        return new ReportsNavigatorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_navigation_reports, container, false);

        replaceChildFragmentWith(getContext(), getFragmentManager(), R.id.reports_nav_container, ReportsTabFragment.newInstance(),
                SupportAnimation.NO_ANIMATION,
                false);

        return view;
    }


//    @Override
//    public void replaceChildFragmentWith(Context context, FragmentManager manager, int container, Fragment fragment, SupportAnimation animation, boolean addToBackStack) {
//        super.replaceChildFragmentWith(context, getFragmentManager(), container, fragment, animation, addToBackStack);
//    }

}
