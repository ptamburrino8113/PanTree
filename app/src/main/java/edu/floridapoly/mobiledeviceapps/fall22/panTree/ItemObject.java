package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.annotation.NonNull;

public class ItemObject {
    private String parent_document;
    private String field_name;
    private String name;

    public ItemObject(String parent_document, String field_name, String name){
        this.parent_document = parent_document;
        this.field_name = field_name;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getField_name() {
        return field_name;
    }
    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getParent_document() {
        return parent_document;
    }

    public void setParent_document(String parent_document) {
        this.parent_document = parent_document;
    }

    @NonNull
    @Override
    public String toString(){
        return name;
    }
}
