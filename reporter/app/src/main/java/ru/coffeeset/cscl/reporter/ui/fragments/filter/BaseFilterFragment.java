package ru.coffeeset.cscl.reporter.ui.fragments.filter;

import android.view.Menu;
import android.view.MenuInflater;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseFragment;

public class BaseFilterFragment extends BaseFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.date).setVisible(false);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.refresh).setVisible(false);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.close).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
