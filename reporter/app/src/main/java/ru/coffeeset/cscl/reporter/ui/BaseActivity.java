package ru.coffeeset.cscl.reporter.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseFragment;



public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.ISupportActionBarListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true); // for api19
    }


    @Override
    public void onTitleSet(Object title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        if (title == null) return;
        if (title instanceof Integer) {
            actionBar.setTitle((int) title);
        } else if (title instanceof CharSequence) {
            actionBar.setTitle(title.toString());
        }
    }


    @Override
    public void onHomeButtonSet(Boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(enabled);
    }




    private static boolean isFragmentInBackstack(final FragmentManager fragmentManager, final String fragmentTagName) {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getClass().getSimpleName())) {
                Logger.d("Fragment is exists in backstack: " + fragmentTagName);
                return true;
            }
        }
        return false;
    }


    public void setFragmentToContainer(FragmentManager manager, Fragment fragment, int frameId, boolean addToBackStack, boolean animated) {
//        if (!Helper.Security.fragmentPermissionGranted(fragment)) {
//            return;
//        }
        final String tag = fragment.getClass().getSimpleName();
        if (isFragmentInBackstack(manager, tag)) {
            try {
                manager.popBackStackImmediate(tag, 0);
            } catch (IllegalStateException ignored) {
                // There's no way to avoid getting this if saveInstanceState has already been called.
            }
        } else {
            FragmentTransaction transaction = manager.beginTransaction();
            //if (animated) setAnimationToTransaction(transaction, SupportAnimation.FADE_IN);
            transaction.replace(frameId, fragment);
            if (addToBackStack) transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        }
    }


    public void replaceCurrentFragmentWith(FragmentManager manager, int container, Fragment fragment, SupportAnimation animation, boolean addToBackStack) {

        final String tag = fragment.getClass().getSimpleName();
        if (isFragmentInBackstack(manager, tag)) {
            try {
                manager.popBackStackImmediate(tag, 0);
            } catch (IllegalStateException ignored) {
                // There's no way to avoid getting this if saveInstanceState has already been called.
            }
        } else {
            FragmentTransaction transaction = manager.beginTransaction();
            setAnimationToTransaction(transaction, animation);
            transaction.replace(container, fragment, tag);
            if (addToBackStack) transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        }
    }


    public void addFragmentWith(FragmentManager manager, int container, Fragment fragment, SupportAnimation animation) {
        final String tag = fragment.getClass().getSimpleName();

        try {
            boolean fragmentPopped = manager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (fragmentPopped) return;
        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }

        FragmentTransaction transaction = manager.beginTransaction();
        setAnimationToTransaction(transaction, animation);
        transaction.add(container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }


    public void setAnimationToTransaction(FragmentTransaction transaction, SupportAnimation animation) {
        if (animation == null) {
            return;
        }
        switch (animation) {
            case FROM_LEFT_TO_RIGHT:
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right);
                break;
            case FROM_RIGHT_TO_LEFT:
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                        R.anim.enter_from_right, R.anim.exit_to_left);
                break;
            case FROM_DOWN_TO_UP:
                transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up,
                        R.anim.enter_from_up, R.anim.exit_to_down);
                break;
            case FROM_UP_TO_DOWN:
                transaction.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down,
                        R.anim.enter_from_down, R.anim.exit_to_up);
                break;
            default:
                break;
        }
    }

}
