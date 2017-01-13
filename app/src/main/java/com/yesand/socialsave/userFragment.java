package com.yesand.socialsave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.constants.AccountType;
import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.reimaginebanking.api.nessieandroidsdk.models.Customer;
import com.reimaginebanking.api.nessieandroidsdk.models.Purchase;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        ResourceManager.getCurrUser().child(Constants.NESSIE_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String customerId = (String) dataSnapshot.getValue();

                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "http://api.reimaginebanking.com/customers/" + customerId + "/accounts?key=" + ResourceManager.getNessieKey();

                JsonArrayRequest jsArrRequest = new JsonArrayRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    ResourceManager.getNessieClient().PURCHASE.getPurchasesByAccount((String)((JSONObject) response.get(0)).get("_id"), new NessieResultsListener() {
                                        @Override
                                        public void onSuccess(Object result) {
                                            Calendar startOfWeek = ResourceManager.getStartOfThisWeek();
                                            SimpleDateFormat nessieDateFormat = ResourceManager.getNessieDateFormat();

                                            List<Purchase> purchases = (List<Purchase>) result;

                                            double amountSpent = 0;
                                            for (Purchase purchase : purchases){
                                                try {
                                                    Date date = nessieDateFormat.parse(purchase.getPurchaseDate());

                                                    if (startOfWeek.getTime().getTime() <= date.getTime()){
                                                        amountSpent += purchase.getAmount();
                                                    }
                                                } catch (ParseException e) {
                                                    // Handle?
                                                }
                                            }

                                            final double finalAmountSpent = amountSpent;

                                            ResourceManager.getCurrUser().child(Constants.GOAL).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    double goal = Double.parseDouble(dataSnapshot.getValue().toString());

                                                    moneyLeft.setMax((int)goal);
                                                    moneyLeft.setProgress(Math.max(0, (int)goal - (int)finalAmountSpent));

                                                    Toast.makeText(getActivity(), "" + finalAmountSpent, Toast.LENGTH_LONG).show();

                                                    availibleFundsMessage.setText("$" + ((int)goal - (int)finalAmountSpent) + " left / $" + goal + " budget:");

                                                    refresher.setRefreshing(false);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Toast.makeText(getActivity(), "Error0..." + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                    refresher.setRefreshing(false);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(NessieError error) {
                                            Toast.makeText(getActivity(), "Error1..." + error.getMessage(), Toast.LENGTH_LONG).show();
                                            refresher.setRefreshing(false);
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error5..." + e.getMessage(), Toast.LENGTH_LONG).show();
                                    refresher.setRefreshing(false);}
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Error6..." + error.getMessage(), Toast.LENGTH_LONG).show();
                                refresher.setRefreshing(false);
                            }
                        });
                queue.add(jsArrRequest);


/*
                ResourceManager.getNessieClient().ACCOUNT.getCustomerAccounts(customerId, new NessieResultsListener() {
                    @Override
                    public void onSuccess(Object result) {
                        List<Account> accounts = (List<Account>) result;

                        ResourceManager.getNessieClient().PURCHASE.getPurchasesByAccount(accounts.get(0).getId(), new NessieResultsListener() {
                            @Override
                            public void onSuccess(Object result) {
                                Calendar startOfWeek = ResourceManager.getStartOfThisWeek();
                                SimpleDateFormat nessieDateFormat = ResourceManager.getNessieDateFormat();

                                List<Purchase> purchases = (List<Purchase>) result;

                                double amountSpent = 0;
                                for (Purchase purchase : purchases){
                                    try {
                                        Date date = nessieDateFormat.parse(purchase.getPurchaseDate());

                                        if (startOfWeek.getTime().getTime() <= date.getTime()){
                                            amountSpent += purchase.getAmount();
                                        }
                                    } catch (ParseException e) {
                                        // Handle?
                                    }
                                }

                                final double finalAmountSpent = amountSpent;

                                ResourceManager.getCurrUser().child(Constants.GOAL).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        double goal = Double.parseDouble(dataSnapshot.getValue().toString());

                                        moneyLeft.setMax((int)goal);
                                        moneyLeft.setProgress(Math.max(0, (int)goal - (int)finalAmountSpent));

                                        Toast.makeText(getActivity(), "" + finalAmountSpent, Toast.LENGTH_LONG).show();

                                        availibleFundsMessage.setText("$" + (goal - finalAmountSpent) + " left / $" + goal + " budget:");

                                        refresher.setRefreshing(false);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getActivity(), "Error0..." + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                        refresher.setRefreshing(false);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(NessieError error) {
                                Toast.makeText(getActivity(), "Error1..." + error.getMessage(), Toast.LENGTH_LONG).show();
                                refresher.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onFailure(NessieError error) {
                        Log.e("User Frag", error.getMessage());

                        Toast.makeText(getActivity(), "Error2..." + error.getMessage(), Toast.LENGTH_LONG).show();
                        refresher.setRefreshing(false);
                    }
                });*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error3...", Toast.LENGTH_LONG).show();
                refresher.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Waiting...", Toast.LENGTH_LONG);
    }
}