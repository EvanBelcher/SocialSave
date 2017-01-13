package com.yesand.socialsave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Purchase;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by puyus on 1/12/2017.
 */

public class TransFragment extends TabMainFragment {

    private TextView dateOfTransaction;
    private TextView tittleTransaction;
    private SwipeRefreshLayout refresher;
    private TextView moneyTransaction;
    private View view;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {

        moneyTransaction = (TextView) view.findViewById(R.id.moneyTransaction);

        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refreshViewForTrans);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });
    }

    public void refreshPage(){
        NessieClient nessieClient = ResourceManager.getNessieClient();
        nessieClient.PURCHASE.getPurchasesByAccount("5877e1401756fc834d8ea103", new NessieResultsListener() {
            //nessieClient.CUSTOMER.getCustomers(new NessieResultsListener() {
            @Override
            public void onSuccess(Object result) {

                Calendar startOfWeek = ResourceManager.getStartOfThisWeek();
                SimpleDateFormat nessieDateFormat = ResourceManager.getNessieDateFormat();

                List<Purchase> purchases = (List<Purchase>) result;

                for (Purchase purchase : purchases){
//                    Toast.makeText(TransFragment.this.getActivity(), purchase.getPurchaseDate(), Toast.LENGTH_LONG).show();
                    moneyTransaction.setText(purchase.getAmount().toString()+" \n");
                    try {
                        Date date = nessieDateFormat.parse(purchase.getPurchaseDate());

//                        if (startOfWeek.getTime().getTime() <= date.getTime()){
//                            amountSpent += purchase.getAmount();
//                        }
                    } catch (ParseException e) {
                        // Handle?
                    }
                }
                refresher.setRefreshing(false);
            }

            @Override
            public void onFailure(NessieError error) {
                // Break?
                refresher.setRefreshing(false);
            }
        });
    }

}
