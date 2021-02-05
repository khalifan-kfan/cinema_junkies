package com.example.cataloge.ui.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class DocIds {

    @Exclude
    public String DocIds;
    public <T extends DocIds> T withID(@NonNull final String id){
        this.DocIds = id;
        return (T) this;
    }

}
