package com.example.highcash.data.view_model;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.highcash.data.db.model.CashAccount;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<CashAccount> accountLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> accountIdLiveData = new MutableLiveData<>();

    public void setAccount(CashAccount account){
        accountLiveData.setValue(account);
    }
    public MutableLiveData<CashAccount>  getAccount(){
        return  accountLiveData;
    }

    public void setAccountId(int accountId){
        accountIdLiveData.setValue(accountId);
    }
    public MutableLiveData<Integer>  getAccountId(){
        return  accountIdLiveData;
    }
}
