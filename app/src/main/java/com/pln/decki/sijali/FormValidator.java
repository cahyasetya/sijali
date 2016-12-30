package com.pln.decki.sijali;

import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cahya on 9/3/16.
 */
public class FormValidator {
    private Pattern pattern;
    private Matcher matcher;

    public boolean validateUsername(EditText editText){
        String USERNAME_PATTTERN="^[a-zA-Z0-9]{8,15}$";
        pattern=Pattern.compile(USERNAME_PATTTERN);
        matcher=pattern.matcher(editText.getText().toString());
        if(!matcher.matches()){
            editText.setError("Harus diisi dan minimal terdiri dari 3 karakter");
            editText.requestFocus();
            return matcher.matches();
        }
        return matcher.matches();
    }

    public boolean validateRequiredEditText(EditText editText){
        if(TextUtils.isEmpty(editText.getText())){
            return false;
        }
        return true;
    }

    public boolean validateNip(EditText editText){
        String NIP_PATTERN="^[0-9]{18}$";
        pattern=Pattern.compile(NIP_PATTERN);
        matcher=pattern.matcher(editText.getText().toString());
        if(!matcher.matches()){
            editText.setError("Harus diisi dan terdiri dari 18 karakter");
            editText.requestFocus();
            return matcher.matches();
        }
        return matcher.matches();
    }

    public boolean validateNoHp(EditText editText){
        String NOHP_PATTERN="^08[0-9]{9,}$";
        pattern=Pattern.compile(NOHP_PATTERN);
        matcher=pattern.matcher(editText.getText().toString());
        if(!matcher.matches()){
            editText.setError("Harus diisi dengan format 08...");
            editText.requestFocus();
            return matcher.matches();
        }
        return matcher.matches();
    }

    public boolean validateNamaLengkap(EditText editText){
        String NAMALENGKAP_PATTERN="^[a-zA-Z ]{1,}$";
        pattern=Pattern.compile(NAMALENGKAP_PATTERN);
        matcher=pattern.matcher(editText.getText().toString());
        if(!matcher.matches()){
            editText.setError("Harus diisi dan hanya terdiri dari huruf");
            editText.requestFocus();
            return matcher.matches();
        }
        return matcher.matches();
    }

    public boolean validatePassword(EditText editText){
        if(!validateRequiredEditText(editText)){
            editText.setError("Password harus diisi");
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public boolean required(EditText editText){
        if(!validateRequiredEditText(editText)){
            editText.setError("Harus diisi");
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validateHari(EditText editText){
        String HARI_PATTERN1="^[0][0-9]$";
        String HARI_PATTERN2="^[0-3][0-2]$";
        Pattern regex1=Pattern.compile(HARI_PATTERN1);
        Pattern regex2=Pattern.compile(HARI_PATTERN2);
        if(regex1.matcher(editText.getText().toString()).matches()){
            return true;
        }
        if (regex2.matcher(editText.getText().toString()).matches()){
            return true;
        }
        editText.setError("Format salah");
        editText.requestFocus();
        return false;
    }
}
