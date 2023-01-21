package com.example.expmanagementjava.ui.analytics_earned;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AnalyticsEarnedViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AnalyticsEarnedViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is analytics earned fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}