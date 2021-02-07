package com.example.cataloge.ui.loginregister;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;
    private boolean isSame;
    private Integer numberError;
    private  Integer Pass2Error;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.numberError = null;
        this.Pass2Error = null;
        this.isDataValid = false;
        this.isSame = false;
    }
//for sign up
    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError,Integer numberError, Integer Pass2Error) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.Pass2Error = Pass2Error;
        this.numberError = numberError;
        this.isDataValid = false;
        this.isSame = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }
    //for sign up
    LoginFormState(boolean isDataValid,boolean isSame) {
        this.usernameError = null;
        this.passwordError = null;
        this.numberError = null;
        this.Pass2Error = null;
        this.isDataValid = isDataValid;
        this.isSame = isSame;
    }

    public Integer getNumberError() {
        return numberError;
    }

    public Integer getPass2Error() {
        return Pass2Error;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
    boolean isSame(){
        return isSame;
    }
}