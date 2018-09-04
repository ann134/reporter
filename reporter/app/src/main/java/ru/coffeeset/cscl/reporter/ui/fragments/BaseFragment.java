package ru.coffeeset.cscl.reporter.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.ui.BaseActivity;



public abstract class BaseFragment extends Fragment {

    public interface ISupportActionBarListener {
        void onTitleSet(Object title);

        void onHomeButtonSet(Boolean enabled);

    }

    public void replaceChildFragmentWith(Context context, FragmentManager manager, int container, Fragment fragment, SupportAnimation animation, boolean addToBackStack) {
        ((BaseActivity) context).replaceCurrentFragmentWith(manager, container, fragment, animation, addToBackStack);
    }

    public int getParentContainer(View view) {
        ViewGroup vg = (ViewGroup) view.getParent();
        return vg.getId();
    }

    public void addFragmentWith(Context context, FragmentManager manager, int container, Fragment fragment, SupportAnimation animation) {
        ((BaseActivity) context).addFragmentWith(manager, container, fragment, animation);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    public void refreshActionBar(){

    }

}

