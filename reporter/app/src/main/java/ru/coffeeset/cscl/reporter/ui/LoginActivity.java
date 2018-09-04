package ru.coffeeset.cscl.reporter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.ui.fragments.login.LoginFragment;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //поднимает layout над клавиатурой
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (!MainRepository.users().getUser().isAuthorized()) {
            showLoginScreen();
            return;
        }

        MainRepository.users().logIn(MainRepository.users().getUser().getToken(), new RequestListener.ObjectCallback<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                if (success) {
                    startMainActivity();
                } else {
                    showLoginScreen();
                }
            }

            @Override
            public void onError(HttpError e) {
                showLoginScreen();
            }
        });
    }


    public void showLoginScreen() {
        FrameLayout container = findViewById(R.id.login_container);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        setFragmentToContainer(fm, LoginFragment.newInstance(), container.getId(), false, true);
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        this.finish();
    }
}
