package com.yesand.socialsave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Purchase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by puyus on 1/12/2017.
 */

public class TransactionFragment extends TabMainFragment {

    private TextView dateOfTransaction;
    private SwipeRefreshLayout refresher;
    private TextView titleTransaction;
    private TextView moneyTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trans_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        refreshPage();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        moneyTransaction = (TextView) view.findViewById(R.id.moneyTransaction);
        dateOfTransaction = (TextView) view.findViewById(R.id.dateOfTransaction);
        titleTransaction = (TextView) view.findViewById(R.id.titleTransaction);

        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPage();
    }

    public void refreshPage() {
        ResourceManager.getCurrUser().child(Constants.NESSIE_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String customerId = (String) dataSnapshot.getValue();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "http://api.reimaginebanking.com/customers/" + customerId + "/accounts?key=" + ResourceManager.getNessieKey();
                JsonArrayRequest jsArrRequest = new JsonArrayRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    ResourceManager.getNessieClient().PURCHASE.getPurchasesByAccount((String) ((JSONObject) response.get(0)).get("_id"), new NessieResultsListener() {
                                        @Override
                                        public void onSuccess(Object result) {
                                            @SuppressWarnings("unchecked") List<Purchase> purchases = (List<Purchase>) result;
                                            Collections.sort(purchases, new Comparator<Purchase>() {
                                                @Override
                                                public int compare(Purchase o1, Purchase o2) {
                                                    try {
                                                        Date date1 = ResourceManager.getNessieDateFormat().parse(o1.getPurchaseDate());
                                                        Date date2 = ResourceManager.getNessieDateFormat().parse(o2.getPurchaseDate());
                                                        return date1.compareTo(date2);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    return 0;
                                                }
                                            });

                                            for (Purchase purchase : purchases) {
                                                System.out.println(purchase.getPurchaseDate());
                                                dateOfTransaction.setText(purchase.getPurchaseDate() + "\n\n" + dateOfTransaction.getText() + "\n");
                                                titleTransaction.setText(purchase.getDescription() + "\n\n" + titleTransaction.getText().toString() + "\n");
                                                moneyTransaction.setText(ResourceManager.getMoneyFormatter().format(purchase.getAmount()) + "\n\n" + moneyTransaction.getText().toString());
                                            }
                                            refresher.setRefreshing(false);
                                        }

                                        @Override
                                        public void onFailure(NessieError error) {
                                            Constants.error("Error1..." + error.getMessage(), false, getActivity());
                                            refresher.setRefreshing(false);
                                        }
                                    });
                                } catch (Exception e) {
                                    Constants.error("Error5..." + e.getMessage(), false, getActivity());
                                    refresher.setRefreshing(false);
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Constants.error("Error6..." + error.getMessage(), false, getActivity());
                                refresher.setRefreshing(false);
                            }
                        });
                queue.add(jsArrRequest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Constants.error("Error3..." + databaseError, false, getActivity());
                refresher.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Waiting for refresh...", Toast.LENGTH_SHORT).show();
    }

}




