package com.yesand.socialsave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Purchase;

import java.util.List;

/**
 * Created by puyus on 1/12/2017.
 */

public class TransFragment extends TabMainFragment {

    private TextView dateOfTransaction;
    private TextView tittleTransaction;
    private TextView MoneyTransaction;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trans_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userTransactions();
    }

    public void userTransactions()
    {
        ResourceManager.getNessieClient().PURCHASE.getPurchasesByAccount("5877a57b1756fc834d8e8763",
                new NessieResultsListener() {
                    @Override
                    public void onSuccess(Object result) {
                        List<Purchase> purchase = (List<Purchase>) result;
                        for (int i = 0; i < purchase.size(); i++)
                        {
                            System.out.print(purchase.get(i) + "   get the id");
                        }
                    }
                    @Override
                    public void onFailure(NessieError error) {
                        System.out.print("This didn't work");
                    }
                });
    }


}
