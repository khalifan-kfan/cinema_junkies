package com.example.cataloge.ui.logreg;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cataloge.MainActivity;
import com.example.cataloge.R;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private  SignupviewModel viewModal;
    private SignUpViewModelFactory factory;
    UserStateChange listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        factory = new SignUpViewModelFactory(this.getApplication());
        viewModal = new ViewModelProvider(this)
                .get(SignupviewModel.class);

        listener= (UserStateChange) this;
        final EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        final EditText numberEditText = findViewById(R.id.editTextNumber);
        final EditText passEditText = findViewById(R.id.editTextTextPassword);
        final EditText passEditText2 = findViewById(R.id.editTextTextPassword2);
        final Button signupButton = findViewById(R.id.button_creat);
        final ProgressBar loadingProgressBar = findViewById(R.id.progressBar);

        viewModal.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                // button is only activated when all data is well filled
                signupButton.setEnabled(loginFormState.isDataValid() && loginFormState.isSame());
                if (loginFormState.getUsernameError() != null) {
                    emailEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passEditText.setError(getString(loginFormState.getPasswordError()));
                }
                if(loginFormState.getNumberError() != null){
                    numberEditText.setError(getString(loginFormState.getNumberError()));
                }
                if(loginFormState.getPass2Error() != null){
                    passEditText2.setError(getString(loginFormState.getPass2Error()));
                }
            }
        });
        // alternative , observe firebase user
       viewModal.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }

            }
        });



        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
            @Override
            public void afterTextChanged(Editable s) {
                viewModal.DataChanged(emailEditText.getText().toString(),
                        passEditText.getText().toString(),passEditText2.getText().toString(),numberEditText.getText().toString());
            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        passEditText.addTextChangedListener(afterTextChangedListener);
        numberEditText.addTextChangedListener(afterTextChangedListener);
        passEditText2.addTextChangedListener(afterTextChangedListener);
        passEditText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModal.create(emailEditText.getText().toString(),
                            passEditText2.getText().toString(),numberEditText.getText().toString());
                }
                return false;
            }
        });
 signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                viewModal.create(emailEditText.getText().toString(),
                        passEditText2.getText().toString(),numberEditText.getText().toString());
            }
        });

    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        listener.makeStateTrue();
        setResult(Activity.RESULT_OK);
        //Complete and destroy login activity once successful
        Intent i = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
