package ru.coffeeset.cscl.reporter.ui.fragments.login;



import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.ReporterApplication;
import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.controllers.utils.Helper;
import ru.coffeeset.cscl.reporter.databinding.FragmentLoginBinding;
import ru.coffeeset.cscl.reporter.ui.LoginActivity;


public class LoginFragment extends Fragment implements View.OnFocusChangeListener {

    private FragmentLoginBinding viewDataBinding;
    private Context mContext;

    private int maxLength = 15;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login,
                container,
                false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (MainRepository.users().getUser().getLogin() != null)
            viewDataBinding.editLogin.setText(MainRepository.users().getUser().getLogin());

        // editText listeners
        viewDataBinding.editLogin.setOnFocusChangeListener(this);
        viewDataBinding.editPassword.setOnFocusChangeListener(this);
        setTextChangeListener(viewDataBinding.editLogin);
        setTextChangeListener(viewDataBinding.editPassword);


        // keyboard enter listener
        viewDataBinding.editPassword.setOnEditorActionListener((v, actionId, event) -> {
            loginButtonPressed(v);
            return false;
        });


        // layout listener
        viewDataBinding.layout.setOnClickListener(Helper.Utils::hideKeyboard);

        // button listeners
        viewDataBinding.buttonClearLogin.setOnClickListener(v -> clear(viewDataBinding.editLogin));
        viewDataBinding.buttonClearPassword.setOnClickListener(v -> clear(viewDataBinding.editPassword));
        viewDataBinding.buttonLogin.setOnClickListener(this::loginButtonPressed);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText editText = (EditText) v;

        if (hasFocus) {
            if (!editText.getText().toString().equals(""))
                setClearButtonVisible(editText, true);
        } else {
            setClearButtonVisible(editText, false);
        }
    }

    public void setTextChangeListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setClearButtonVisible(editText, true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= maxLength) s.delete(maxLength, s.length());
            }
        });
    }

    public void clear(EditText editText) {
        editText.setText("");
        setClearButtonVisible(editText, false);
    }

    public void setClearButtonVisible(EditText editText, boolean visible) {
        if (visible)
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0);
        else
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }


    void loginButtonPressed(View v) {
        Helper.Utils.hideKeyboard(v);

        loading(true);

        String login = viewDataBinding.editLogin.getText().toString().trim();
        String password = viewDataBinding.editPassword.getText().toString().trim();


        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            Toast.makeText(ReporterApplication.getInstance(), R.string.TOAST_INCORRECT, Toast.LENGTH_SHORT).show();
            clear(viewDataBinding.editPassword);
            return;
        }


        MainRepository.users().logIn(login, password, new RequestListener.ObjectCallback<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                if (success) {
                    ((LoginActivity) mContext).startMainActivity();
                } else {
                    Toast.makeText(ReporterApplication.getInstance(), R.string.TOAST_AUTHORIZATION_ERROR, Toast.LENGTH_SHORT).show();
                    clear(viewDataBinding.editPassword);
                    loading(false);
                }
            }

            @Override
            public void onError(HttpError e) {
                Toast.makeText(ReporterApplication.getInstance(), R.string.TOAST_AUTHORIZATION_ERROR, Toast.LENGTH_SHORT).show();
                clear(viewDataBinding.editPassword);
                loading(false);
            }
        });

    }

    private void loading(boolean show){
        if (show) {
            viewDataBinding.spinner.setVisibility(View.VISIBLE);
            viewDataBinding.buttonLogin.setVisibility(View.GONE);
        } else {
            viewDataBinding.spinner.setVisibility(View.GONE);
            viewDataBinding.buttonLogin.setVisibility(View.VISIBLE);
        }
    }

}
