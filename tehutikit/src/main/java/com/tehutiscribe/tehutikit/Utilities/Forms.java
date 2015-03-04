package com.tehutiscribe.tehutikit.Utilities;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class Forms {
    public static String FormatUSCurrency(double price) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
    }

    public static void EmulateHomeButton(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }

    // Checks if trimmed value of an EditText Field is not empty or null
    private static boolean validateFormField(EditText field) {

        return (field != null &&
                !field.getText().toString().trim().isEmpty());
    }

    // Checks if trimmed value of an EditText Field is not empty or null
    public static boolean validateFormFields(EditText... fields) {
        String text;
        boolean isValid = true;

        for (EditText field : fields) {
            text = field.getText().toString().trim();

            try {
                if (text.isEmpty()) {
                    field.setError("Required");
                    isValid = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isValid = false;
            }
        }
        return isValid;
    }

    // Checks that two password fields have identical values
    public static boolean comparePasswords(Context context, EditText passwordOne, EditText passwordTwo) {
        boolean isValid = true;
        String p1 = passwordOne.getText().toString().trim();
        String p2 = passwordTwo.getText().toString().trim();


        if (validateFormField(passwordOne) && validateFormField(passwordTwo)) {
            if (!p1.equals(p2)) {
                passwordOne.setError("Re-enter");
                passwordTwo.setError("Re-enter");
                Toast.makeText(context, "Passwords must match", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        } else {
            isValid = false;
        }

        return isValid;
    }
}
