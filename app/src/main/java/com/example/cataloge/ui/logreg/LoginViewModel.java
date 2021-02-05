package com.example.cataloge.ui.logreg;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.util.Patterns;

import com.example.cataloge.R;
import com.example.cataloge.ui.model.AppRepo;
import com.google.firebase.auth.FirebaseUser;


public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState;
    private MutableLiveData<LoginResult> loginResult;
    private AppRepo repo;
    private  MutableLiveData<FirebaseUser> userFB;



    LoginViewModel(Application app){
        super(app);

        repo = new AppRepo(app);
         loginFormState= new MutableLiveData<>();
        loginResult = new MutableLiveData<>();
        userFB = new MutableLiveData<>();
        userFB = repo.getUserMutableLiveData();
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }


    public void login(String username, String password) {
        //firebase login
           int suxes = repo.login(username,password);
           if(suxes == 0){
               loginResult.setValue(new LoginResult(new LoggedInUserView(username)));
           }else if(suxes==1) {
               loginResult.setValue(new LoginResult(R.string.login_failed));
           }
    }

    public MutableLiveData<FirebaseUser> getUserFB() {
        return userFB;
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}