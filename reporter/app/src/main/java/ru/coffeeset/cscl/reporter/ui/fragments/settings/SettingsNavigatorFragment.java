package ru.coffeeset.cscl.reporter.ui.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseNavigatorFragment;


public class SettingsNavigatorFragment extends BaseNavigatorFragment {

    public static SettingsNavigatorFragment newInstance() {
        return new SettingsNavigatorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_navigation_settings, container, false);

        replaceChildFragmentWith(getContext(), getFragmentManager(), R.id.settings_nav_container, SettingsFragment.newInstance(),
                SupportAnimation.NO_ANIMATION,
                false);
        return view;
    }

}
