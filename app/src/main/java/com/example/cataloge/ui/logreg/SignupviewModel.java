package com.example.cataloge.ui.logreg;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cataloge.R;
import com.example.cataloge.ui.model.AppRepo;
import com.google.firebase.auth.FirebaseUser;

public class SignupviewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private AppRepo repo;
    private  MutableLiveData<FirebaseUser> userFB ;
    private MutableLiveData<Boolean> isloggedIn ;

    public SignupviewModel(@NonNull Application application) {
        super(application);
        repo = new AppRepo(application);
        userFB = new MutableLiveData<>();
        isloggedIn = new MutableLiveData<>();
        userFB = repo.getUserMutableLiveData();
        isloggedIn= repo.getLoggedUser();

    }
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    LiveData<LoginResult> getLoginResult(){
        return loginResult;
    }
    LiveData<FirebaseUser> getUser(){
        return userFB;
    }

    public void create(String email, String pass, String phone){
        userFB = repo.register(email,pass,phone);
        if(userFB!= null){
            loginResult.setValue(new LoginResult(new LoggedInUserView(email)));
        }else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }

    }

    // checking and getting logged in user
    public MutableLiveData<FirebaseUser> getUserFB() {
        return userFB;
    }
    //checking if user is logged in
    private MutableLiveData<Boolean> getIsloggedIn()
    { return isloggedIn; }

    public void DataChanged(String username, String password, String password2,String number) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null,null,null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, null, null));
        }else if(!isnumberValid(number)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.phone_number, null));
        }else if(!isSame(password,password2)){
            loginFormState.setValue(new LoginFormState(null, null, null, R.string.Confirm));
        } else{
            loginFormState.setValue(new LoginFormState(true,true));
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
    public boolean isSame(String pass1,String pass2){
        return pass1.equals(pass2);
    }
    private boolean isnumberValid(String phone) {
        return phone != null && phone.trim().length() >8;
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


}
