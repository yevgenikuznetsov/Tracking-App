package com.example.readapplication.Aid;

import com.google.android.material.textfield.TextInputLayout;

public class CheckInputValue {

    public boolean validateName(TextInputLayout name) {
        String val = name.getEditText().getText().toString();

        if (val.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateEmail(TextInputLayout email) {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatePassword(TextInputLayout password) {
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (val.length() < 6) {
            password.setError("Password too short, you need more " + (6 - val.length() + " char"));
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}
