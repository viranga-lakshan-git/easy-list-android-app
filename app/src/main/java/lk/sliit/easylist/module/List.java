package lk.sliit.easylist.module;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class List {
    private String listName;
    private String listType;

    public List(){}

    public List(String listName, String listType) {
        this.listName = listName;
        this.listType = listType;
    }

    @NonNull
    @Override
    public String toString() {
        return listType+": "+listName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }
}
