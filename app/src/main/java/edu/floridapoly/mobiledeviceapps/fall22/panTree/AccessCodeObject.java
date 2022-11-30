package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.annotation.NonNull;

public class AccessCodeObject {

    private String email;
    private String field_name;
    private String code;

    public AccessCodeObject(String field_name, String code, String email){
        this.field_name = field_name;
        this.code = code;
        this.email = email;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getField_name() {
        return field_name;
    }
    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    @NonNull
    @Override
    public String toString(){
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
