package com.yesand.socialsave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Customer;
import com.reimaginebanking.api.nessieandroidsdk.models.Purchase;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by puyus on 1/12/2017.
 */

public class UserFragment extends TabMainFragment {

    private GraphView mGraphView;
    private ProgressBar moneyLeft;
    private SwipeRefreshLayout refresher;
    private TextView availibleFundsMessage;
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container , false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGraphView = (GraphView) view.findViewById(R.id.graph);
        mGraphView.removeAllSeries();


        moneyLeft = (ProgressBar) view.findViewById(R.id.progressBar);

        availibleFundsMessage = (TextView) view.findViewById(R.id.current_saving_text);

        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
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

                double amountSpent = 0;
                for (Purchase purchase : purchases){
                    Toast.makeText(UserFragment.this.getActivity(), purchase.getPurchaseDate(), Toast.LENGTH_LONG).show();
                    try {
                        Date date = nessieDateFormat.parse(purchase.getPurchaseDate());

                        if (startOfWeek.getTime().getTime() <= date.getTime()){
                            amountSpent += purchase.getAmount();
                        }
                    } catch (ParseException e) {
                        // Handle?
                    }
                }

                moneyLeft.setProgress((int)amountSpent);

                refresher.setRefreshing(false);
            }

            @Override
            public void onFailure(NessieError error) {
                // Break?z
                refresher.setRefreshing(false);
            }
        });
    }
}
